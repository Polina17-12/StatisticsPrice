package com.example.statisticsprice.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class DataProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //волшебные цифры в стиме
    @Column(nullable = false, unique = true)
    private String uuid;

    @OneToMany(mappedBy = "dataProduct", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TrendEntity> trends;


}

