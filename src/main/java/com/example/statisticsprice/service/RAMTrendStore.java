package com.example.statisticsprice.service;

import com.example.statisticsprice.dto.TrendDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Profile("RAM")
@Component
public class RAMTrendStore implements TrendStore {
    private HashMap<String, LinkedList<TrendDTO>> store = new HashMap<>();

    @Override
    public void addData(String gameId, String price) {
        if (store.containsKey(gameId)) {
            store.get(gameId).add(new TrendDTO(price,  LocalDateTime.now(ZoneId.of("UTC"))));
        } else {
            LinkedList<TrendDTO> list = new LinkedList<>();
            list.add(new TrendDTO(price,  LocalDateTime.now(ZoneId.of("UTC"))));
            store.put(gameId, list);
        }
    }

    @Override
    public List<TrendDTO> getTrend(String gameId) {
        return store.get(gameId);
    }

    @Override
    public List<String> getAllIds() {
        return store.keySet().stream().collect(Collectors.toList());
    }
}
