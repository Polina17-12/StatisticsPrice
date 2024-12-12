package com.example.statisticsprice.repo;

import com.example.statisticsprice.entity.DataProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DataProductRepo extends JpaRepository<DataProductEntity, Integer> {
    Optional<DataProductEntity> findByUuid(String uuid);
}
