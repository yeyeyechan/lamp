package com.springboot.lamp.util;

import com.springboot.lamp.controller.HelloController;
import kong.unirest.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;

public class WebSocketUtil extends WebSocketClient {
    private JSONObject obj;
    private  String json;
    private final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    public void setParameter(String str){
        this.json = str;
    }
    public WebSocketUtil(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }
    public WebSocketUtil(URI serverURI) {
        super(serverURI);
    }

    public WebSocketUtil(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
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
        } catch (UnsupportedEncodingException e) {
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
