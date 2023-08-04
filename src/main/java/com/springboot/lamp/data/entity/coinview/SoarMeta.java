package com.springboot.lamp.data.entity.coinview;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash(value="SortMeta"
)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoarMeta {
  @Id
  private String market	;
  private  int id;
  private String  trade_date;
  private String  korean_name;
  private String  english_name;
  private String  logo;

  private String  trade_time	;
  private String  trade_date_kst ;
  private String  trade_time_kst	;
  private long trade_timestamp	;
  private Double opening_price;
  private Double high_price	;
  private Double low_price	;
  private Double trade_price	;
  private Double prev_closing_price;
  private String change_	;

  private Double change_price	;
  private Double change_rate	;
  private Double signed_change_price	;
  private Double signed_change_rate	;
  private Double trade_volume	;
  private Double acc_trade_price	;
  private Double acc_trade_price_24h	;
  private Double acc_trade_volume	;
  private Double acc_trade_volume_24h	;
  private Double highest_52_week_price	;
  private String highest_52_week_date	;
  private Double lowest_52_week_price	;
  private String lowest_52_week_date	;
  private long timestamp	;

}
