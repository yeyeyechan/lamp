package com.springboot.lamp.data.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpbitMarketResponseDto {
    private String Market;

    private String KoreanName;

    private String EnglishName;

    private String MarketWarning;
}
