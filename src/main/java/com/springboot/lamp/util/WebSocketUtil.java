package com.springboot.lamp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.lamp.controller.HelloController;
import com.springboot.lamp.data.dao.CoinViewMetaDAO;
import com.springboot.lamp.data.dto.UpbitMarketDto;
import com.springboot.lamp.data.dto.UpbitTickerDto;
import com.springboot.lamp.data.dto.upbit.UpbitWsTickerDto;
import com.springboot.lamp.data.entity.CoinViewMeta;
import com.springboot.lamp.data.entity.coinview.SoarMeta;
import java.util.List;
import kong.unirest.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketUtil extends WebSocketClient {
    private JSONObject obj;
    private  String json;
    private final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
    public final CoinViewMetaDAO coinViewMetaDAO;
    public final SimpMessagingTemplate simpMessagingTemplate;
    @Value("${upbit.wsuri}")
    private URI wsuri;
    public void setParameter(String str){
        this.json = str;
    }

    @Autowired
    public WebSocketUtil(@Value("${upbit.wsuri}") URI wsuri, CoinViewMetaDAO coinViewMetaDAO, SimpMessagingTemplate simpMessagingTemplate) {
        super(wsuri);
        this.coinViewMetaDAO = coinViewMetaDAO;
        this.simpMessagingTemplate = simpMessagingTemplate;

    }

    @Override
    public void onMessage( String message ) {
        LOGGER.info("message from upbit ws  {} ", message);
        obj = new JSONObject(message);

    }
    @Override
    public void onMessage(ByteBuffer bytes){
        try {
            LOGGER.info("message from upbit ws  {} ", new String(bytes.array(), "UTF-8"));
            ObjectMapper objectMapper = new ObjectMapper();
            String wsdata =new String(bytes.array(), "UTF-8");
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            UpbitWsTickerDto upbitWsTickerDto =  objectMapper.readValue(wsdata, new TypeReference<UpbitWsTickerDto>(){});
            LOGGER.error("upbitWsTickerDto {} ", upbitWsTickerDto);
            LOGGER.error("upbitWsTickerDto.getMarket() {} ", upbitWsTickerDto.getCode());

            CoinViewMeta coinViewMeta = this.coinViewMetaDAO.findByMarket(upbitWsTickerDto.getCode());
            LOGGER.error("coinviewMeta {} ", coinViewMeta);
            SoarMeta soarMeta = new SoarMeta();

            soarMeta.setMarket(upbitWsTickerDto.getCode());
            soarMeta.setTrade_price(upbitWsTickerDto.getTrade_price());
            soarMeta.setChange_(upbitWsTickerDto.getChange());
            soarMeta.setSigned_change_price(upbitWsTickerDto.getSigned_change_price());
            soarMeta.setSigned_change_rate(upbitWsTickerDto.getSigned_change_rate());
            soarMeta.setLogo(coinViewMeta.getLogo());
            soarMeta.setId(coinViewMeta.getId());
            soarMeta.setKorean_name(coinViewMeta.getKoreanName());
            soarMeta.setEnglish_name(coinViewMeta.getEnglishName());
            this.simpMessagingTemplate.convertAndSend("/coinview/getSoarCoin", soarMeta);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //obj = new JSONObject(message);

    }


    @Override
    public void onOpen(ServerHandshake handshake ) {
        LOGGER.info( "opened connection" );
        LOGGER.info(" this.json  {} ", this.json);
        send(this.json);

    }

    @Override
    public void onClose( int code, String reason, boolean remote ) {
        LOGGER.info(" code {}  reason {} ", code, reason);
        LOGGER.info( "closed connection" );
    }

    @Override
    public void onError( Exception ex ) {
        ex.printStackTrace();
    }

    public JSONObject getResult() throws Exception{
        return this.obj;
    }
}
