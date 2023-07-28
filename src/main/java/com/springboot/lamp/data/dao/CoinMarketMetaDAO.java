package com.springboot.lamp.data.dao;

import com.springboot.lamp.data.entity.CoinMarketMeta;

import java.util.List;

public interface CoinMarketMetaDAO {
    CoinMarketMeta insertCoinMarketMeta(CoinMarketMeta coinMarketMeta);
    List<CoinMarketMeta> insertAllCoinMarketMeta(List<CoinMarketMeta> coinMarketMetas);
    List<CoinMarketMeta> findAllCoinMarketMetaByName(List<String> nameList);



}
