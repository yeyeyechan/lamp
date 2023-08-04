package com.springboot.lamp.data.repository;

import com.springboot.lamp.data.entity.CoinViewMeta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoinViewMetaRepository extends JpaRepository<CoinViewMeta, String> {

  @Query(value= "INSERT INTO coin_view_meta (market, korean_name,english_name,  market_warning,logo, id,name,slug,symbol) "
      +" (SELECT a.market, a.korean_name, a.english_name, a.market_warning,b.logo, b.id,b.name,b.slug,b.symbol FROM upbit_market a "+
      "INNER JOIN coin_market_meta b "+
      "ON a.english_name=b.name "+
      "WHERE a.market LIKE 'KRW-%')", nativeQuery = true)
  List<CoinViewMeta> makeCoinViewMeta();
  CoinViewMeta findByMarket(String market);

}
