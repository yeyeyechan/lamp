package com.springboot.lamp.data.dao.impl;

import com.springboot.lamp.data.dao.CoinViewMetaDAO;
import com.springboot.lamp.data.entity.CoinViewMeta;
import com.springboot.lamp.data.repository.CoinViewMetaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoinViewMetaDAOimpl implements CoinViewMetaDAO {
private  final CoinViewMetaRepository coinViewMetaRepository;

@Autowired
CoinViewMetaDAOimpl(CoinViewMetaRepository coinViewMetaRepository){
  this.coinViewMetaRepository = coinViewMetaRepository;
}
  @Override
  public List<CoinViewMeta> makeCoinViewMeta() {
    return this.coinViewMetaRepository.makeCoinViewMeta();
  }

  @Override
  public List<CoinViewMeta> findAllCoinViewMeta() {
    return this.coinViewMetaRepository.findAll();
  }

  @Override
  public CoinViewMeta findByMarket(String market) {
    return this.coinViewMetaRepository.findByMarket(market);
  }
}
