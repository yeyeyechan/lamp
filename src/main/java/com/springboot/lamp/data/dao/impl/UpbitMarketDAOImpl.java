package com.springboot.lamp.data.dao.impl;

import com.springboot.lamp.data.dao.ProductDAO;
import com.springboot.lamp.data.dao.UpbitMarketDAO;
import com.springboot.lamp.data.entity.UpbitMarket;
import com.springboot.lamp.data.repository.UpbitMarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Component
public class UpbitMarketDAOImpl implements UpbitMarketDAO {
    private UpbitMarketRepository upbitMarketRepository;


    @Autowired
    public UpbitMarketDAOImpl(UpbitMarketRepository upbitMarketRepository){
        this.upbitMarketRepository = upbitMarketRepository;
    }
    @Override
    public UpbitMarket insertUpbitMarket(UpbitMarket upbitMarket){
        UpbitMarket savedUpbitMarket =  this.upbitMarketRepository.save(upbitMarket);
        return savedUpbitMarket;
    }
    @Override
    public List<UpbitMarket> insertAllUpbitMarkets(List<UpbitMarket> upbitMarkets){
        List<UpbitMarket> savedUpbitMarkets = this.upbitMarketRepository.saveAll(upbitMarkets);
        return savedUpbitMarkets;
    }

    @Override

    public UpbitMarket selectUpbitMarket(String market){
        UpbitMarket findUpbitMarket = this.upbitMarketRepository.getById(market);

        return findUpbitMarket;
    }
    @Override

    public UpbitMarket updateUpbitMarket(String market, String koreanName, String englishName, String marketWarning) throws Exception{
        Optional<UpbitMarket> findedUpbitMaret = this.upbitMarketRepository.findById(market);

        UpbitMarket updatedUpbitMarket;

        if(findedUpbitMaret.isPresent()){
            UpbitMarket upbitMarket = findedUpbitMaret.get();
            upbitMarket.setKoreanName(koreanName);
            upbitMarket.setEnglishName(englishName);
            upbitMarket.setMarketWarning(marketWarning);
            updatedUpbitMarket = upbitMarket;
        }else{
            throw new Exception();
        }
        return updatedUpbitMarket;
    }
    @Override

    public void deleteUpbitMarket(String market) throws Exception{
        Optional<UpbitMarket> findedUpbitMarket = this.upbitMarketRepository.findById(market);
        if(findedUpbitMarket.isPresent()){
            UpbitMarket upbitMarket = findedUpbitMarket.get();
            this.upbitMarketRepository.delete(upbitMarket);
        }else{
            throw new Exception();
        }
    }

    @Override
    public List<UpbitMarket> selectMarketByMarketKeyword(String keyword) {

        return this.upbitMarketRepository.findBymarketStartsWith(keyword);
    }


}
