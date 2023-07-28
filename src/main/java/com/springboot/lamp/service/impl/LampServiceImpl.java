package com.springboot.lamp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.lamp.data.dao.CoinViewMetaDAO;
import com.springboot.lamp.data.dao.impl.CoinViewMetaDAOimpl;
import com.springboot.lamp.data.repository.CoinViewMetaRepository;
import com.springboot.lamp.service.LampService;
import com.springboot.lamp.service.UpbitService;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LampServiceImpl implements LampService {

  private final UpbitService upbitService;
private final CoinViewMetaDAO coinViewMetaDAO;
  @Autowired
  LampServiceImpl(UpbitService upbitService, CoinViewMetaDAO coinViewMetaDAO){
    this.upbitService = upbitService;
    this.coinViewMetaDAO = coinViewMetaDAO;
  }
  @Override
  public void afterPropertiesSet() throws Exception {
    init();

  }

  public void init() throws ParseException, JsonProcessingException, InterruptedException {
    this.upbitService.scheduleSaveAllUpbitMarket();
    this.coinViewMetaDAO.makeCoinViewMeta();
  }


}
