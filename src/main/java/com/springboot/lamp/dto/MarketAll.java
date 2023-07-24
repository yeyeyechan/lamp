package com.springboot.lamp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketAll {
    private  String market;
    private  String korean_name;
    private  String english_name;
    private  String market_warning;

}
