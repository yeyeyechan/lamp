package com.springboot.lamp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.lamp.controller.UpbitController;
import com.springboot.lamp.data.dao.CoinMarketMapDAO;
import com.springboot.lamp.data.dao.CoinMarketMetaDAO;
import com.springboot.lamp.data.dao.CoinViewMetaDAO;
import com.springboot.lamp.data.dao.UpbitMarketDAO;
import com.springboot.lamp.data.dto.CoinMarketMapDto;
import com.springboot.lamp.data.dto.CoinMarketMapResponseDto;
import com.springboot.lamp.data.dto.CoinMarketMetaDto;
import com.springboot.lamp.data.dto.CoinMarketMetaResponseDto;
import com.springboot.lamp.data.dto.UpbitMarketDto;
import com.springboot.lamp.data.dto.UpbitMarketResponseDto;
import com.springboot.lamp.data.dto.UpbitTickerDto;
import com.springboot.lamp.data.dto.UpbitTickerResponseDto;
import com.springboot.lamp.data.entity.CoinMarketMap;
import com.springboot.lamp.data.entity.CoinMarketMeta;
import com.springboot.lamp.data.entity.CoinViewMeta;
import com.springboot.lamp.data.entity.UpbitMarket;
import com.springboot.lamp.data.entity.coinview.SoarMeta;
import com.springboot.lamp.data.entity.upbit.UpbitTicker;
import com.springboot.lamp.data.repository.UpbitMarketRepository;
import com.springboot.lamp.service.UpbitService;
import com.springboot.lamp.util.WebSocketUtil;
import java.util.UUID;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UpbitServiceImpl implements UpbitService {
    private final Logger LOGGER = LoggerFactory.getLogger(UpbitServiceImpl.class);
    private final RedisTemplate<String, List<SoarMeta>> redisTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Value("${upbit.marketalluri}")
    private String marketalluri;

    @Value("${upbit.tickeruri}")
    private String tickeruri;
    @Value("${coinmarketcap.coininfouri}")
    private String  coininfouri;
    @Value("${coinmarketcap.mapinfouri}")
    private String  mapinfouri;
    @Value("${coinmarketcap.apikey}")
    private String  coinmarketapikey;
    public final UpbitMarketDAO upbitMarketDAO ;

    public final CoinMarketMapDAO coinMarketMapDAO;
    public final CoinMarketMetaDAO coinMarketMetaDAO;
    public final CoinViewMetaDAO coinViewMetaDAO;
    public final WebSocketUtil webSocketUtil;
    @Autowired
    UpbitServiceImpl(UpbitMarketDAO upbitMarketDAO, CoinMarketMetaDAO coinMarketMetaDAO, CoinMarketMapDAO coinMarketMapDAO, RedisTemplate redisTemplate, SimpMessagingTemplate simpMessagingTemplate,CoinViewMetaDAO coinViewMetaDAO, WebSocketUtil webSocketUtil){
        this.upbitMarketDAO = upbitMarketDAO;
        this.coinMarketMapDAO = coinMarketMapDAO;
        this.coinMarketMetaDAO = coinMarketMetaDAO;
        this.redisTemplate = redisTemplate;
        this.simpMessagingTemplate= simpMessagingTemplate;
        this.coinViewMetaDAO= coinViewMetaDAO;
        this.webSocketUtil = webSocketUtil;
    }

    @Override
    public UpbitMarketResponseDto getUpbitMarket(String market){
        UpbitMarket upbitMarket =   this.upbitMarketDAO.selectUpbitMarket(market);

        UpbitMarketResponseDto upbitMarketResponseDto = new UpbitMarketResponseDto();
        upbitMarketResponseDto.setMarket(upbitMarket.getMarket());
        upbitMarketResponseDto.setKoreanName(upbitMarket.getKoreanName());
        upbitMarketResponseDto.setEnglishName(upbitMarket.getEnglishName());
        upbitMarketResponseDto.setMarketWarning(upbitMarket.getMarketWarning());

        return upbitMarketResponseDto;
    }

    @Override
    public List<UpbitMarketResponseDto> getAllUpbitMarket() {
        List<UpbitMarket> upbitMarkets =  new ArrayList<UpbitMarket>();
        List<UpbitMarketResponseDto> upbitMarketResponseDtos = new ArrayList<UpbitMarketResponseDto>();

        upbitMarkets.forEach(upbitMarket -> {
            UpbitMarketResponseDto upbitMarketResponseDto = new UpbitMarketResponseDto();
            upbitMarketResponseDto.setMarket(upbitMarket.getMarket());
            upbitMarketResponseDto.setKoreanName(upbitMarket.getKoreanName());
            upbitMarketResponseDto.setEnglishName(upbitMarket.getEnglishName());
            upbitMarketResponseDto.setMarketWarning(upbitMarket.getMarketWarning());
            upbitMarketResponseDtos.add(upbitMarketResponseDto);
        });
        return upbitMarketResponseDtos;
    }

    @Override
    public UpbitMarketResponseDto saveUpbitMarket(UpbitMarketDto upbitMarketDto) {
        UpbitMarket upbitMarket = new UpbitMarket();
        upbitMarket.setKoreanName(upbitMarketDto.getKorean_name());
        upbitMarket.setEnglishName(upbitMarketDto.getEnglish_name());
        upbitMarket.setMarket(upbitMarketDto.getMarket());
        upbitMarket.setMarketWarning(upbitMarketDto.getMarket_warning());

        UpbitMarket savedUpbitMarket = this.upbitMarketDAO.insertUpbitMarket(upbitMarket);

        UpbitMarketResponseDto upbitMarketResponseDto  = new UpbitMarketResponseDto();
        upbitMarketResponseDto.setMarket(savedUpbitMarket.getMarket());
        upbitMarketResponseDto.setEnglishName(savedUpbitMarket.getEnglishName());
        upbitMarketResponseDto.setKoreanName(savedUpbitMarket.getKoreanName());
        upbitMarketResponseDto.setMarketWarning(savedUpbitMarket.getMarketWarning());
        return upbitMarketResponseDto;
    }

    @Override
    public List<UpbitMarketResponseDto> saveAllUpbitMarket(List<UpbitMarketDto> UpbitMarketDtos) {
        List<UpbitMarket> upbitMarkets = new ArrayList<UpbitMarket>();
        UpbitMarketDtos.forEach(upbitMarketDto -> {
            UpbitMarket upbitMarket = new UpbitMarket();
            upbitMarket.setMarket(upbitMarketDto.getMarket());
            upbitMarket.setKoreanName(upbitMarketDto.getKorean_name());
            upbitMarket.setEnglishName(upbitMarketDto.getEnglish_name());
            upbitMarket.setMarketWarning(upbitMarketDto.getMarket_warning());
            upbitMarkets.add(upbitMarket);
        });
       List<UpbitMarket> savedUpbitMarkets = this.upbitMarketDAO.insertAllUpbitMarkets(upbitMarkets);
        List<UpbitMarketResponseDto> upbitMarketResponseDtos =  new ArrayList<UpbitMarketResponseDto>();
        savedUpbitMarkets.forEach(upbitMarket -> {
            UpbitMarketResponseDto upbitMarketResponseDto = new UpbitMarketResponseDto();
            upbitMarketResponseDto.setMarket(upbitMarket.getMarket());
            upbitMarketResponseDto.setKoreanName(upbitMarket.getKoreanName());
            upbitMarketResponseDto.setEnglishName(upbitMarket.getEnglishName());
            upbitMarketResponseDto.setMarketWarning(upbitMarket.getMarketWarning());
            upbitMarketResponseDtos.add(upbitMarketResponseDto);
        });
        return upbitMarketResponseDtos;
    }

    @Override
    public UpbitMarketResponseDto updateUpbitMarket(UpbitMarketDto upbitMarketDto) throws Exception {

        UpbitMarket updatedUpbitMarket = this.upbitMarketDAO.updateUpbitMarket(upbitMarketDto.getMarket(),upbitMarketDto.getMarket(),upbitMarketDto.getEnglish_name(),upbitMarketDto.getMarket_warning());

        UpbitMarketResponseDto upbitMarketResponseDto = new UpbitMarketResponseDto();
        upbitMarketResponseDto.setMarket(updatedUpbitMarket.getMarket());
        upbitMarketResponseDto.setKoreanName(updatedUpbitMarket.getKoreanName());
        upbitMarketResponseDto.setEnglishName(updatedUpbitMarket.getEnglishName());
        upbitMarketResponseDto.setMarketWarning(updatedUpbitMarket.getMarketWarning());
        return upbitMarketResponseDto;
    }

    @Override
    public void deleteUpbitMarket(String market) throws Exception {
        this.upbitMarketDAO.deleteUpbitMarket(market);

    }
    public List<CoinMarketMapResponseDto> saveAllCoinMarketMap(List<CoinMarketMapDto> coinMarketMapDtos){
        List<CoinMarketMap> coinMarketMaps = new ArrayList<CoinMarketMap>();
        coinMarketMapDtos.forEach(coinMarketMapDto -> {
            CoinMarketMap coinMarketMap = new CoinMarketMap();
            coinMarketMap.setId(coinMarketMapDto.getId());
            coinMarketMap.setName(coinMarketMapDto.getName());
            coinMarketMap.setRank(coinMarketMapDto.getRank());
            coinMarketMap.setSymbol(coinMarketMapDto.getSymbol());
            coinMarketMap.setSlug(coinMarketMapDto.getSlug());
            coinMarketMaps.add(coinMarketMap);
        });
        List<CoinMarketMap>  savedCoinMarketMaps = this.coinMarketMapDAO.insertAllCoinMarketMap(coinMarketMaps);
        List<CoinMarketMapResponseDto> coinMarketMapResponseDtos = new ArrayList<CoinMarketMapResponseDto>();
        savedCoinMarketMaps.forEach(coinMarketMap -> {
            CoinMarketMapResponseDto coinMarketMapResponseDto = new CoinMarketMapResponseDto();
            coinMarketMapResponseDto.setId(coinMarketMap.getId());
            coinMarketMapResponseDto.setName(coinMarketMap.getName());
            coinMarketMapResponseDto.setRank(coinMarketMap.getRank());
            coinMarketMapResponseDto.setSymbol(coinMarketMap.getSymbol());
            coinMarketMapResponseDto.setSlug(coinMarketMap.getSlug());

            coinMarketMapResponseDtos.add(coinMarketMapResponseDto);
        });
        return coinMarketMapResponseDtos;
    }
    public List<CoinMarketMetaResponseDto> saveAllCoinMarketMeta(List<CoinMarketMetaDto> coinMarketMetaDtos){
        List<CoinMarketMeta> coinMarketMetas = new ArrayList<CoinMarketMeta>();
        coinMarketMetaDtos.forEach(coinMarketMetaDto -> {
            CoinMarketMeta coinMarketMeta = new CoinMarketMeta();
            coinMarketMeta.setId(coinMarketMetaDto.getId());
            coinMarketMeta.setName(coinMarketMetaDto.getName());
            coinMarketMeta.setRank(coinMarketMetaDto.getRank());
            coinMarketMeta.setSymbol(coinMarketMetaDto.getSymbol());
            coinMarketMeta.setSlug(coinMarketMetaDto.getSlug());
            coinMarketMeta.setLogo(coinMarketMetaDto.getLogo());
            coinMarketMetas.add(coinMarketMeta);
        });
        List<CoinMarketMeta>  savedCoinMarketMetas = this.coinMarketMetaDAO.insertAllCoinMarketMeta(coinMarketMetas);
        List<CoinMarketMetaResponseDto> coinMarketMetaResponseDtos = new ArrayList<CoinMarketMetaResponseDto>();
        savedCoinMarketMetas.forEach(coinMarketMeta -> {
            CoinMarketMetaResponseDto coinMarketMetaResponseDto = new CoinMarketMetaResponseDto();
            coinMarketMetaResponseDto.setId(coinMarketMeta.getId());
            coinMarketMetaResponseDto.setName(coinMarketMeta.getName());
            coinMarketMetaResponseDto.setRank(coinMarketMeta.getRank());
            coinMarketMetaResponseDto.setSymbol(coinMarketMeta.getSymbol());
            coinMarketMetaResponseDto.setSlug(coinMarketMeta.getSlug());
            coinMarketMetaResponseDto.setLogo(coinMarketMeta.getLogo());

            coinMarketMetaResponseDtos.add(coinMarketMetaResponseDto);
        });
        return coinMarketMetaResponseDtos;
    }
    @Override
    //@Scheduled(fixedDelay = 500000)
    public void scheduleSaveAllUpbitMarket()
        throws JsonProcessingException, ParseException, InterruptedException {
        HttpResponse<String> response = Unirest.get(marketalluri)
                .header("accept", "application/json")
                .asString();
        System.out.println(response.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        List<UpbitMarketDto> list =  objectMapper.readValue(response.getBody(), new TypeReference<List<UpbitMarketDto>>(){});
        LOGGER.info("list size {} ", list.size());
        this.saveAllUpbitMarket(list);

        HttpResponse<String> response2 = Unirest.get(mapinfouri)
                .header("accept", "application/json")
                .header("X-CMC_PRO_API_KEY", coinmarketapikey)
                .asString();
        //List<CoinMarketMapDto> coinmarketmapdtos =  objectMapper.readValue(response2.getBody(), new TypeReference<Map<String,List<CoinMarketMapDto>>>(){}).get("data");
        //LOGGER.info("coinmarketmapdtos {} ", response2.getBody().toString());
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject)parser.parse(response2.getBody().toString());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<CoinMarketMapDto> coinmarketmapdtos =  objectMapper.readValue(jsonObject.get("data").toString(), new TypeReference<List<CoinMarketMapDto>>(){});

        //List<CoinMarketMapDto> coinMarketMapDtos = (List<CoinMarketMapDto>) jsonObject.get("data");
        this.saveAllCoinMarketMap(coinmarketmapdtos);
        //LOGGER.info("coinmarketmapdtos {} ", coinmarketmapdtos);
        StringBuilder idBuilder = new StringBuilder();
        List<Integer> idList = new ArrayList<Integer>();
        coinmarketmapdtos.forEach(coinMarketMapDto -> {
            idList.add(coinMarketMapDto.getId());
        });

        List<CoinMarketMetaDto> coinMarketMetaDtos = new ArrayList<CoinMarketMetaDto>();
        int count = 1000;
        for(int i =1000 ; i<=idList.size() ; i +=count){
            idBuilder = new StringBuilder();
            for(int j =i-count; j <i ; j++){
                if(j==i-1) idBuilder.append(idList.get(j)+"");
                else idBuilder.append(idList.get(j)+",");
            }
            HttpResponse<String> response3 = Unirest.get(coininfouri)
                    .header("accept", "application/json")
                    .header("X-CMC_PRO_API_KEY", coinmarketapikey)
                    .queryString("id", idBuilder.toString())
                    .asString();
            System.out.println(i);
            //System.out.println(idBuilder.toString().length());
            System.out.println(idBuilder.toString());
            System.out.println(response3.getStatus());
            //System.out.println(response3.getBody().toString());
            int getStatus = response3.getStatus();
            while(getStatus!= 200) {
                System.out.println(response3.getHeaders().toString());
                System.out.println(response3.getBody().toString());
                Thread.sleep(60000);
                System.out.println("retry");
                System.out.println(idBuilder.toString());
               response3 = Unirest.get(coininfouri)
                    .header("accept", "application/json")
                    .header("X-CMC_PRO_API_KEY", coinmarketapikey)
                    .queryString("id", idBuilder.toString())
                    .asString();
                System.out.println(response3.getHeaders().toString());
                System.out.println(response3.getBody().toString());
                getStatus = response3.getStatus();
            }
            parser = new JSONParser();
            jsonObject = (JSONObject)parser.parse(response3.getBody().toString());
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Map<Integer, CoinMarketMetaDto>  coinMaretMetaDtoMap =
                objectMapper.readValue(jsonObject.get("data").toString(), new TypeReference <Map<Integer , CoinMarketMetaDto>>(){});

            coinMaretMetaDtoMap.forEach((key, coinMarketMetaDto) ->{
                coinMarketMetaDtos.add(coinMarketMetaDto);
            });
            if(i==idList.size())break;
            String idlen = "";
            if(i<idList.size())  idlen = idList.get(i)+",";
            while(idlen.length() * count >2000) {
                count -=100;
            }
            if(i+count >= idList.size() || count <1) count = idList.size() -i;
        }
        this.saveAllCoinMarketMeta(coinMarketMetaDtos);
    }
    @Override
    public void makeCoinViewMeta(){
        LOGGER.info("makeCoinViewMeta starts");
        List<UpbitMarket> upbitMarkets =this.upbitMarketDAO.selectMarketByMarketKeyword("KRW-");
        System.out.println(upbitMarkets);


    }
    public List<UpbitTickerResponseDto> getUpbitTickerAll(String markets) throws JsonProcessingException {
        LOGGER.info("getUpbitTickerAll starts");
        LOGGER.info("getUpbitTickerAll ids {}" ,markets);

        HttpResponse<String> response = Unirest.get(tickeruri)
            .header("accept", "application/json")
            .queryString("markets", markets)
            .asString();
        System.out.println(response.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<UpbitTickerDto> list =  objectMapper.readValue(response.getBody(), new TypeReference<List<UpbitTickerDto>>(){});
        List<UpbitTickerResponseDto> upbitTickerResponseDtos = new ArrayList<UpbitTickerResponseDto>();
        list.forEach(upbitTickerDto -> {
            UpbitTickerResponseDto upbitTickerResponseDto = new UpbitTickerResponseDto();
            upbitTickerResponseDto.setMarket(upbitTickerDto.getMarket());
            upbitTickerResponseDto.setTrade_price(upbitTickerDto.getTrade_price());
            upbitTickerResponseDto.setChange(upbitTickerDto.getChange());
            upbitTickerResponseDto.setSigned_change_price(upbitTickerDto.getSigned_change_price());
            upbitTickerResponseDto.setSigned_change_rate(upbitTickerDto.getSigned_change_rate());
            CoinViewMeta coinViewMeta = coinViewMetaDAO.findByMarket(upbitTickerDto.getMarket());
            LOGGER.error("coinviewMeta {} ", coinViewMeta);
            upbitTickerResponseDto.setLogo(coinViewMeta.getLogo());
            upbitTickerResponseDto.setEnglish_name(coinViewMeta.getEnglishName());
            upbitTickerResponseDto.setKorean_name(coinViewMeta.getKoreanName());
            upbitTickerResponseDto.setId(coinViewMeta.getId());

            upbitTickerResponseDtos.add(upbitTickerResponseDto);
        });

        return upbitTickerResponseDtos;
    }
    @Scheduled(fixedDelay = 100000)
    public void redisTest(){
        ListOperations<String, List<SoarMeta>> soarmetas = redisTemplate.opsForList();
        String key = "soarmeta";
        long size = soarmetas.size(key) == null ? 0 : soarmetas.size(key); // NPE 체크해야함.
        LOGGER.info("soarmeta test {}", soarmetas.range(key, -1, -1));
        this.simpMessagingTemplate.convertAndSend("/coinview/getSoarCoin",  soarmetas.range(key, -1, -1).get(0));

    }
    public void upbitTickerWS(markets){
        LOGGER.info("upbitTickerWS  call ");
        this.webSocketUtil.connect();
        JSONArray jsonArray = new JSONArray();

        jsonArray.put(new kong.unirest.json.JSONObject().put("ticket", UUID.randomUUID()));
        kong.unirest.json.JSONObject json = new kong.unirest.json.JSONObject();
        json.put("type", "ticker");
        List<String> list =new ArrayList<>();
        list.add("KRW-BTC");
        json.put("codes", list);
        jsonArray.put(json);

        LOGGER.info("jsonArray ticker input {} ", jsonArray.toString());
        webSocketUtil.setParameter(jsonArray.toString());
        //webSocketUtil.send(jsonArray.toString());
        return "ok";
    }
}
