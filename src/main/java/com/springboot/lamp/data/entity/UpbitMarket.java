package com.springboot.lamp.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString()
@Table(name="upbit_market")
public class UpbitMarket {
    @Id
    private String market;

    @Column(nullable = false ,name="korean_name")
    private String koreanName;

    @Column(nullable = false,name="english_name")
    private String englishName;

    @Column(nullable = true,name="market_warning")
    private String marketWarning;
}
