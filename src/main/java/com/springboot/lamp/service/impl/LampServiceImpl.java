package com.springboot.lamp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.lamp.data.dao.CoinViewMetaDAO;
import com.springboot.lamp.data.dto.CoinViewMetaResponseDto;
import com.springboot.lamp.data.dto.UpbitTickerResponseDto;
import com.springboot.lamp.data.entity.CoinViewMeta;
import com.springboot.lamp.data.entity.coinview.SoarMeta;
import com.springboot.lamp.service.LampService;
import com.springboot.lamp.service.UpbitService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minidev.json.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class LampServiceImpl implements LampService {

  private final UpbitService upbitService;
  private final RedisTemplate<String, List<SoarMeta>> redisTemplate;
  private final RedisTemplate<String, String> redisStringTemplate;
  private final CoinViewMetaDAO coinViewMetaDAO;
  private final Logger LOGGER = LoggerFactory.getLogger(UpbitServiceImpl.class);

  @Autowired
  LampServiceImpl(UpbitService upbitService, CoinViewMetaDAO coinViewMetaDAO, RedisTemplate<String, List<SoarMeta>> redisTemplate, RedisTemplate<String, String> redisStringTemplate){
    this.upbitService = upbitService;
    this.coinViewMetaDAO = coinViewMetaDAO;
    this.redisTemplate = redisTemplate;
    this.redisStringTemplate =redisStringTemplate;
  }
  @Override
  public void afterPropertiesSet() throws Exception {
    init();
    ListOperations<String, List<SoarMeta>> soarmetas = redisTemplate.opsForList();
    String key = "soarmeta";
    long size = soarmetas.size(key) == null ? 0 : soarmetas.size(key); // NPE 체크해야함.
    List<SoarMeta> soarMetas = soarmetas.range(key, -1, -1).get(0);
    List<String> marktets = new ArrayList<>();
    soarMetas.forEach(soarMeta -> {
      marktets.add(soarMeta.getMarket());
    });
    this.upbitService.upbitTickerWS(marktets);
  }

  public void init() throws ParseException, JsonProcessingException, InterruptedException {
    //업비트 마켓 데이터 업뎃
    this.upbitService.scheduleSaveAllUpbitMarket();
    //코인 뷰 메타 업뎃
    this.coinViewMetaDAO.makeCoinViewMeta();
    //상승코인찾기
    List<UpbitTickerResponseDto> reverseOrderList =  this.getRisingCoin();
    //레디스에 저장
    this.saveToRedisRisingCoinMeta(reverseOrderList);

  }


  @Override
  public List<CoinViewMetaResponseDto> getAllCoinViewMeta() {
    List<CoinViewMeta> coinViewMetas = this.coinViewMetaDAO.findAllCoinViewMeta();
    List<CoinViewMetaResponseDto> coinViewMetaResponseDtos = new ArrayList<CoinViewMetaResponseDto>();
    coinViewMetas.forEach(coinviewmeta ->{
      CoinViewMetaResponseDto coinViewMetaResponseDto = new CoinViewMetaResponseDto();
      coinViewMetaResponseDto.setId(coinviewmeta.getId());
      coinViewMetaResponseDto.setLogo(coinviewmeta.getLogo());
      coinViewMetaResponseDto.setName(coinviewmeta.getName());
      coinViewMetaResponseDto.setSlug(coinviewmeta.getSlug());
      coinViewMetaResponseDto.setMarket(coinviewmeta.getMarket());
      coinViewMetaResponseDto.setKoreanName(coinviewmeta.getKoreanName());
      coinViewMetaResponseDto.setEnglishName(coinviewmeta.getEnglishName());
      coinViewMetaResponseDto.setMarketWarning(coinviewmeta.getMarketWarning());
      coinViewMetaResponseDto.setSymbol(coinviewmeta.getSymbol());

      coinViewMetaResponseDtos.add(coinViewMetaResponseDto);
    });
    return coinViewMetaResponseDtos;
  }

  public List<UpbitTickerResponseDto> getRisingCoin() throws JsonProcessingException {
    List<CoinViewMeta> coinViewMetas =  this.coinViewMetaDAO.findAllCoinViewMeta();
    StringBuilder markets = new StringBuilder();
    coinViewMetas.forEach(coinViewMeta -> {
      markets.append(coinViewMeta.getMarket()+",");
    });
    String marketString = markets.toString().substring(0, markets.toString().length()-1);
    List<UpbitTickerResponseDto> coinViewMetaResponseDtos = this.upbitService.getUpbitTickerAll(marketString);
    LOGGER.info("coinViewMetaResponseDtos {} ", coinViewMetaResponseDtos);

    List<UpbitTickerResponseDto> reverseOrderList =  coinViewMetaResponseDtos.stream()
        .sorted(Comparator.comparing(UpbitTickerResponseDto::getSigned_change_rate, Comparator.reverseOrder())).filter(upbitTickerResponseDto
            -> upbitTickerResponseDto.getSigned_change_rate()>0.02).collect(
            Collectors.toList());
    LOGGER.info("reverseOrderList {} ", reverseOrderList);
    return reverseOrderList;
  }
  public void saveToRedisRisingCoinMeta(List<UpbitTickerResponseDto> upbitTickerResponseDtos){
    List<SoarMeta> soarmetas = new ArrayList<SoarMeta>();
    upbitTickerResponseDtos.forEach(upbitTickerResponseDto -> {
      SoarMeta soarMeta = new SoarMeta();
      soarMeta.setMarket(upbitTickerResponseDto.getMarket());
      soarMeta.setKorean_name(upbitTickerResponseDto.getKorean_name());
      soarMeta.setEnglish_name(upbitTickerResponseDto.getEnglish_name());
      soarMeta.setTrade_price(upbitTickerResponseDto.getTrade_price());
      soarMeta.setChange_(upbitTickerResponseDto.getChange());
      soarMeta.setSigned_change_price(upbitTickerResponseDto.getSigned_change_price());
      soarMeta.setSigned_change_rate(upbitTickerResponseDto.getSigned_change_rate());
      soarMeta.setLogo(upbitTickerResponseDto.getLogo());
      soarMeta.setId(upbitTickerResponseDto.getId());
      soarmetas.add(soarMeta);
    });
    LOGGER.info("soarmetas init {} ", soarmetas);

    ListOperations<String, List<SoarMeta>> sormetaredis = redisTemplate.opsForList();
    ValueOperations<String, String> sorStringmetaredis = redisStringTemplate.opsForValue();
    sorStringmetaredis.set("soarStringmeta", soarmetas.toString());
    sormetaredis.rightPush("soarmeta" , soarmetas);
    String key = "soarmeta";
    long size = sormetaredis.size(key) == null ? 0 : sormetaredis.size(key); // NPE 체크해야함.
    LOGGER.info("sormetaredisinit {}", sormetaredis.range(key, -1, -1).toString());

    LOGGER.info("sormetaredisinit {}", sormetaredis.range(key, -1, -1).toString());

    LOGGER.info("soarStringmeta init {} ", sorStringmetaredis.get("soarStringmeta"));
  }
}
