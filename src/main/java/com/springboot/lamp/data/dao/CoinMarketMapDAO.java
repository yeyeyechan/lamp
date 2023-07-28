package com.springboot.lamp.data.dao;

import com.springboot.lamp.data.entity.CoinMarketMap;

import java.util.List;

public interface CoinMarketMapDAO {

    CoinMarketMap insertCoinMarketMap(CoinMarketMap coinMarketMap);
    List<CoinMarketMap> insertAllCoinMarketMap(List<CoinMarketMap> coinMarketMaps);

}
