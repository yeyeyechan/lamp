package com.springboot.lamp.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString()
@Table(name="coin_view_meta")
public class CoinViewMeta {

  private String market;

  @Column(nullable = false ,name="korean_name")
  private String koreanName;

  @Column(nullable = false,name="english_name")
  private String englishName;
  @Column(nullable = true,name="market_warning")
  private String marketWarning;
  @Column(nullable = false,name="logo")
  private String logo;

  @Id
  private int id;

  private String name;
  private String slug;
  private String symbol;

}
