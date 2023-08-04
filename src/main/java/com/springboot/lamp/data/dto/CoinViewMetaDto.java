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
public class CoinViewMetaDto {

  private String market;
  private String koreanName;
  private String englishName;
  private String marketWarning;
  private String logo;
  private int id;
  private String name;
  private String slug;
  private String symbol;
}
