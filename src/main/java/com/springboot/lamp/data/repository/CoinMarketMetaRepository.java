package com.springboot.lamp.data.repository;

import com.springboot.lamp.data.entity.CoinMarketMeta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinMarketMetaRepository  extends JpaRepository<CoinMarketMeta, Integer> {

  List<CoinMarketMeta> findAllByNameIn(List<String> nameList);

}
