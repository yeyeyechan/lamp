package com.springboot.lamp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.lamp.data.dto.MarketAll;
import com.springboot.lamp.data.dto.UpbitMarketDto;
import com.springboot.lamp.data.dto.UpbitMarketResponseDto;
import com.springboot.lamp.service.UpbitService;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/upbit")
public class UpbitController {
    private final Logger LOGGER = LoggerFactory.getLogger(UpbitController.class);
    @Value("${upbit.tickeruri}")
    private String tickeruri;
    public final UpbitService upbitservice;

    @Autowired
    public UpbitController(UpbitService upbitservice){
        this.upbitservice = upbitservice;
    }

    @GetMapping("/saveUpbitMarketAll")
    public List<UpbitMarketResponseDto> saveUpbitMarketAll()  throws JsonProcessingException {
        HttpResponse<String> response = Unirest.get(tickeruri)
                .header("accept", "application/json")
                .asString();
        System.out.println(response.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        List<UpbitMarketDto> list =  objectMapper.readValue(response.getBody(), new TypeReference<List<UpbitMarketDto>>(){});
        LOGGER.info("list size {} ", list.size());

        return this.upbitservice.saveAllUpbitMarket(list);
    }
}
