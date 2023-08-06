package com.springboot.lamp.service.websocket;

import com.springboot.lamp.data.entity.coinview.SoarMeta;
import com.springboot.lamp.service.impl.UpbitServiceImpl;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@ServerEndpoint(value="/soar")
public class WebSocketService {
  private  static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
  private final Logger LOGGER = LoggerFactory.getLogger(UpbitServiceImpl.class);
  private final RedisTemplate<String, List<SoarMeta>> redisTemplate;

  @Autowired
  WebSocketService(RedisTemplate<String, List<SoarMeta>> redisTemplate){
    this.redisTemplate = redisTemplate;
  }
  @OnOpen
  public void opOpen(Session s){
    LOGGER.info("open session : " + s.toString());
    if(!clients.contains(s)){
      clients.add(s);
      LOGGER.info("session open : " + s.toString());
    }else{
      LOGGER.info("already connected " );
    }
  }

  @OnMessage
  public void onMessage(String msg, Session session ) throws Exception{
    LOGGER.info("receive message : " + msg);
    ListOperations<String, List<SoarMeta>> soarmetas = redisTemplate.opsForList();
    String key = "soarmeta";
    long size = soarmetas.size(key) == null ? 0 : soarmetas.size(key); // NPE 체크해야함.
    List<SoarMeta> soarMetas = soarmetas.range(key, -1, -1).get(0);
    LOGGER.info("soarmeta before send  {}", soarMetas);

    for(Session s : clients){
      //LOGGER.info("send message : " + soarMetas.toString());
      s.getBasicRemote().sendText(soarMetas.toString());
    }
  }

  @OnClose
  public void onClose(Session s){
    LOGGER.info("session close  : " + s);
    clients.remove(s);
  }
}
