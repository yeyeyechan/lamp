package com.springboot.lamp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.lamp.data.dto.UpbitMarketDto;
import com.springboot.lamp.data.dto.UpbitMarketResponseDto;
import com.springboot.lamp.data.dto.UpbitTickerResponseDto;
import com.springboot.lamp.data.entity.UpbitMarket;
import net.minidev.json.parser.ParseException;

import java.util.List;

public interface UpbitService {

    UpbitMarketResponseDto getUpbitMarket(String market) ;

    List<UpbitMarketResponseDto> getAllUpbitMarket() ;

    UpbitMarketResponseDto saveUpbitMarket(UpbitMarketDto upbitMarketDto);

    List<UpbitMarketResponseDto> saveAllUpbitMarket( List<UpbitMarketDto> upbitMarketDtos );

    void scheduleSaveAllUpbitMarket(  ) throws JsonProcessingException, ParseException, InterruptedException;
    UpbitMarketResponseDto  updateUpbitMarket(UpbitMarketDto upbitMarketDto) throws Exception;

    void deleteUpbitMarket(String market)throws Exception;
    void makeCoinViewMeta();
   List<UpbitTickerResponseDto> getUpbitTickerAll(String markets) throws JsonProcessingException ;
   void upbitTickerWS(List<String> markets);
    }
