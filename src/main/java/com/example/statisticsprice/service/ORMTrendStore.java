package com.example.statisticsprice.service;

import com.example.statisticsprice.dto.TrendDTO;
import com.example.statisticsprice.entity.TrendEntity;
import com.example.statisticsprice.repo.DataProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Profile("!RAM")
@Service
@AllArgsConstructor
public class ORMTrendStore implements TrendStore {

    private final DataProductRepo dataProductRepo;

    @Override
    public void addData(String gameId, String price) {
        var game = dataProductRepo.findByUuid(gameId);
        if (game.isEmpty())
            throw new RuntimeException("Game not found");

        var ent = new TrendEntity(price, LocalDateTime.now(ZoneId.of("UTC")));
        ent.setDataProduct(game.get());
        game.get().getTrends().add(ent);
        dataProductRepo.save(game.get());
    }

    @Override
    public List<TrendDTO> getTrend(String gameId) {

        var game = dataProductRepo.findByUuid(gameId);
        if (game.isEmpty())
            throw new RuntimeException("Game not found");
        return game.get().getTrends().stream().map(e -> new TrendDTO(e.getPrice(), e.getDate())).toList();
    }

    @Override
    public List<String> getAllIds() {
        return dataProductRepo.findAll().stream().map(e -> e.getUuid()).collect(Collectors.toList());
    }
}
