package com.springboot.lamp.data.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString()
@Table()
public class CoinMarketMeta {

    @Id
    private int id;
    @Column(nullable = false)
    private int rank;
    @Column(nullable = false)

    private String name;
    @Column(nullable = false)

    private String symbol;
    @Column(nullable = false)

    private String slug;
    @Column(nullable = false)

    private String logo;

}
