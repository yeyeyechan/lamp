package com.springboot.lamp.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpbitTickerResponseDto {
  private String market	;
  private String logo;
  private String  trade_date;
private  String korean_name;
private String english_name;
private int id;
  private String  trade_time	;
  private String  trade_date_kst ;
  private String  trade_time_kst	;
  private long trade_timestamp	;
  private Double opening_price;
  private Double high_price	;
  private Double low_price	;
  private Double trade_price	;
  private Double prev_closing_price;
  private String change	;

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
