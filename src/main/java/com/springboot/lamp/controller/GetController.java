package com.springboot.lamp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.lamp.data.dao.CoinViewMetaDAO;
import com.springboot.lamp.data.dto.MarketAll;
import com.springboot.lamp.util.WebSocketUtil;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/v1/get-coin")
public class GetController {
    private final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
    public final CoinViewMetaDAO coinViewMetaDAO;
    public final WebSocketUtil webSocketUtil;
    @Value("${upbit.wsuri}")
    private URI wsuri;

    GetController(CoinViewMetaDAO coinViewMetaDAO , WebSocketUtil webSocketUtil){
        this.coinViewMetaDAO =coinViewMetaDAO;
        this.webSocketUtil = webSocketUtil;
    }
    @GetMapping(value="/soar" )
    public String getSoar(){
        HttpResponse<String> response = Unirest.get("https://api.upbit.com/v1/candles/minutes/1?market=KRW-BTC&count=1")
                .header("accept", "application/json")
                .asString();
        System.out.print(response.getBody());
        return "hello";
    }

    @GetMapping(value="/market/all")
    public List<MarketAll> getMarketAll() throws JsonProcessingException {
        HttpResponse<String> response = Unirest.get("https://api.upbit.com/v1/market/all")
                .header("accept", "application/json")
                .asString();
        System.out.println(response.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        List<MarketAll> list =  objectMapper.readValue(response.getBody(), new TypeReference<List<MarketAll>>(){});
        LOGGER.info("list size {} ", list.size());
        return objectMapper.readValue(response.getBody(), new TypeReference<List<MarketAll>>(){});
    }

    @GetMapping(value="/ticker")
    public String getTicker() throws Exception {
        LOGGER.info("wsuri   {} ", wsuri);
        this.webSocketUtil.connect();
        JSONArray jsonArray = new JSONArray();

        jsonArray.put(new JSONObject().put("ticket", UUID.randomUUID()));
        JSONObject json = new JSONObject();
        json.put("type", "ticker");
        List<String> list =new ArrayList<>();
        list.add("KRW-BTC");
        json.put("codes", list);
        jsonArray.put(json);

        LOGGER.info("jsonArray ticker input {} ", jsonArray.toString());
        webSocketUtil.setParameter(jsonArray.toString());
        //webSocketUtil.send(jsonArray.toString());
        return "ok";
    }
}
