package com.springboot.lamp.data.repository;

import com.springboot.lamp.data.entity.CoinMarketMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinMarketMapRepostitory  extends JpaRepository<CoinMarketMap, Integer> {
}
