package com.springboot.lamp.service;

import com.springboot.lamp.data.dto.CoinViewMetaResponseDto;
import com.springboot.lamp.data.entity.CoinViewMeta;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;

public interface LampService extends InitializingBean {

  List<CoinViewMetaResponseDto> getAllCoinViewMeta();

}
