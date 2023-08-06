package com.springboot.lamp.controller;

import com.springboot.lamp.data.dto.CoinViewMetaResponseDto;
import com.springboot.lamp.data.entity.coinview.SoarMeta;
import com.springboot.lamp.service.LampService;
import com.springboot.lamp.service.impl.UpbitServiceImpl;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class WsController {
  private final Logger LOGGER = LoggerFactory.getLogger(UpbitServiceImpl.class);
  private final RedisTemplate<String, List<SoarMeta>> redisTemplate;



  private final LampService lampService;

  @Autowired
  WsController(LampService lampService,RedisTemplate<String, List<SoarMeta>> redisTemplate) {
    this.lampService = lampService;
    this.redisTemplate = redisTemplate;

  }

  @MessageMapping("/call")
  public List<SoarMeta> soarmeta(String msg) throws Exception {
    //LOGGER.info("receive message : " + msg);
    ListOperations<String, List<SoarMeta>> soarmetas = redisTemplate.opsForList();
    String key = "soarmeta";
    long size = soarmetas.size(key) == null ? 0 : soarmetas.size(key); // NPE 체크해야함.
    List<SoarMeta> soarMetas = soarmetas.range(key, -1, -1).get(0);
    //LOGGER.info("soarmeta before send  {}", soarMetas);

    return soarMetas;
  }
  @SendTo("/coinview/getSoarCoin")
public String hello()throws  Exception{
    return "ok";
  }
}


