package com.example.statisticsprice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TrendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public TrendEntity(String price, LocalDateTime date) {
        this.price = price;
        this.date = date;
    }

    String price;
    LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "data_product_id") // Указываем внешний ключ
    private DataProductEntity dataProduct;

}
