package com.example.statisticsprice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(mappedBy = "dataProducts")
    private Set<UserEntity> users = new HashSet<>(); //todo связь с таблицей пользователй

    public DataProductEntity(String uuid) {
        this.uuid = uuid;
    }
}

