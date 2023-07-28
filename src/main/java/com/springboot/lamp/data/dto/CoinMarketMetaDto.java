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
public class CoinMarketMetaDto {
    private  int id;
    private  int rank;
    private  String name;
    private  String symbol;
    private  String slug;
    private  String logo;

}
