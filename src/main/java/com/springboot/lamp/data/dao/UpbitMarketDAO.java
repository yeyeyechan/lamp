package com.springboot.lamp.data.dao;

import com.springboot.lamp.data.entity.Product;
import com.springboot.lamp.data.entity.UpbitMarket;

import java.util.List;

public interface UpbitMarketDAO {
    UpbitMarket insertUpbitMarket(UpbitMarket upbitMarket);
    List<UpbitMarket> insertAllUpbitMarkets(List<UpbitMarket> upbitMarkets);

     UpbitMarket selectUpbitMarket(String market);

      UpbitMarket updateUpbitMarket(String market, String koreanName, String englishName, String marketWarning) throws Exception;

     void deleteUpbitMarket(String market) throws Exception;

     List<UpbitMarket> selectMarketByMarketKeyword(String keyword);
}
