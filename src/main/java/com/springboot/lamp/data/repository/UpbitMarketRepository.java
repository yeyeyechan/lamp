package com.springboot.lamp.data.repository;

import com.springboot.lamp.data.entity.UpbitMarket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpbitMarketRepository extends JpaRepository<UpbitMarket, String> {
  List<UpbitMarket>  findBymarketStartsWith(String keyword);

}
