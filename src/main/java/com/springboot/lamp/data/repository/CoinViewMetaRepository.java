package com.springboot.lamp.data.repository;

import com.springboot.lamp.data.entity.CoinViewMeta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoinViewMetaRepository extends JpaRepository<CoinViewMeta, String> {

  @Query(value= "REPLACE INTO coin_view_meta (market, korean_name,english_name,  market_warning,logo, id,name,slug,symbol) "
      +"(SELECT totalresult.market, totalresult.korean_name, totalresult.english_name, totalresult.market_warning,totalresult.logo, totalresult.id,totalresult.name,totalresult.slug, totalresult.symbol "+
      "FROM (SELECT result1.market, result1.korean_name, result1.english_name, result1.market_warning,result1.logo, result1.id,result1.name,result1.slug, result1.symbol FROM (SELECT a.market, a.korean_name,"+
      "a.english_name, a.market_warning,b.logo, b.id,b.name,b.slug, b.symbol FROM upbit_market a INNER JOIN coin_market_meta b ON a.english_name=b.name) as result1 "+
      "UNION ALL "
      +"SELECT result2.market, result2.korean_name, result2.english_name, result2.market_warning,result2.logo, result2.id,result2.name,result2.slug, result2.symbol FROM (SELECT a.market, a.korean_name, a.english_name,"
      +"a.market_warning,b.logo, b.id,b.name,b.slug, b.symbol FROM upbit_market a INNER JOIN coin_market_meta b ON (SELECT SUBSTRING_INDEX(a.market,\"KRW-\",-1))=b.symbol) as result2  ) as totalresult WHERE "
      +"totalresult.market LIKE 'KRW-%' GROUP BY totalresult.korean_name)", nativeQuery = true)
  List<CoinViewMeta> makeCoinViewMeta();
  CoinViewMeta findByMarket(String market);

}
