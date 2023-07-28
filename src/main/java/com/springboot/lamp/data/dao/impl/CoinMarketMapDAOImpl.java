package com.springboot.lamp.data.dao.impl;

import com.springboot.lamp.data.dao.CoinMarketMapDAO;
import com.springboot.lamp.data.entity.CoinMarketMap;
import com.springboot.lamp.data.repository.CoinMarketMapRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class CoinMarketMapDAOImpl implements CoinMarketMapDAO {

    private final CoinMarketMapRepostitory coinMarketMapRepostitory;

    @Autowired
    public CoinMarketMapDAOImpl(CoinMarketMapRepostitory coinMarketMapRepostitory){
        this.coinMarketMapRepostitory = coinMarketMapRepostitory;

    }
    @Override
    public CoinMarketMap insertCoinMarketMap(CoinMarketMap coinMarketMap) {
        CoinMarketMap insertedCoinMarketMap = this.coinMarketMapRepostitory.save(coinMarketMap);
        return insertedCoinMarketMap;
    }

    @Override
    public List<CoinMarketMap> insertAllCoinMarketMap(List<CoinMarketMap> coinMarketMaps) {
        List<CoinMarketMap> insertedCoinMarketMaps  = this.coinMarketMapRepostitory.saveAll(coinMarketMaps);
        return insertedCoinMarketMaps;
    }
}
