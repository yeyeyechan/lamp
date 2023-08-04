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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lamp")
public class LampController {
  private final Logger LOGGER = LoggerFactory.getLogger(UpbitServiceImpl.class);
  private final RedisTemplate<String, List<SoarMeta>> redisTemplate;



  private final LampService lampService;

  @Autowired
  LampController(LampService lampService,RedisTemplate<String, List<SoarMeta>> redisTemplate) {
    this.lampService = lampService;
    this.redisTemplate = redisTemplate;

  }

  @GetMapping("/getSoarCoin")
  public List<CoinViewMetaResponseDto> getSoarCoin() {

    return lampService.getAllCoinViewMeta();
  }

}


