package com.springboot.lamp.data.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CoinMarketMapResponseDto {
    private  int id;
    private  int rank;
    private  String name;
    private  String symbol;
    private  String slug;

}
