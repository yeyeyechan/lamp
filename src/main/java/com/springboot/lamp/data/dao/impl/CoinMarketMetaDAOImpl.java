package com.springboot.lamp.data.dao.impl;

import com.springboot.lamp.data.dao.CoinMarketMetaDAO;
import com.springboot.lamp.data.entity.CoinMarketMeta;
import com.springboot.lamp.data.repository.CoinMarketMetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public class CoinMarketMetaDAOImpl implements CoinMarketMetaDAO {
    private final CoinMarketMetaRepository coinMarketMetaRepository;

    @Autowired
    CoinMarketMetaDAOImpl(CoinMarketMetaRepository coinMarketMetaRepository){
        this.coinMarketMetaRepository = coinMarketMetaRepository;
    }

    @Override
    public CoinMarketMeta insertCoinMarketMeta(CoinMarketMeta coinMarketMeta){
        CoinMarketMeta insertedCoinMarketMeta = this.coinMarketMetaRepository.save(coinMarketMeta);
        return insertedCoinMarketMeta;
    }
    @Override

    public List <CoinMarketMeta> insertAllCoinMarketMeta(List<CoinMarketMeta> coinMarketMetas){
        List<CoinMarketMeta> insertedCoinMarketMetas = this.coinMarketMetaRepository.saveAll(coinMarketMetas);
        return insertedCoinMarketMetas;

    }

    @Override
    public List<CoinMarketMeta> findAllCoinMarketMetaByName(List<String> nameList) {
        return this.coinMarketMetaRepository.findAllByNameIn(nameList);
    }

}
