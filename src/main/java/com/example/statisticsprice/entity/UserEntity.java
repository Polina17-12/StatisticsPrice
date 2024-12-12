package com.example.statisticsprice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_entity")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_data_product",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "data_product_id")
    )
    private Set<DataProductEntity> dataProducts = new HashSet<>(); // todo  Связь таблицы пользователей с таблицей продуктов

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addDataProduct(DataProductEntity dataProduct) { // todo  Добавление пользователю нового товара в отслеживание
        this.dataProducts.add(dataProduct);
        dataProduct.getUsers().add(this);
    }

    public void removeDataProduct(DataProductEntity dataProduct) { // todo  Удаление пользователю нового товара в отслеживание
        this.dataProducts.remove(dataProduct);
        dataProduct.getUsers().remove(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
