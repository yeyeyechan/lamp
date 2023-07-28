package com.springboot.lamp.data.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpbitMarketDto {
    private String market;

    private String korean_name;

    private String english_name;

    private String market_warning;
}
