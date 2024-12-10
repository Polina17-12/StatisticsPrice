package com.example.statisticsprice.controllers;

import com.example.statisticsprice.service.DataUpdateService;
import com.example.statisticsprice.dto.TrendDTO;
import com.example.statisticsprice.service.TrendStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class APIController {
    @Autowired
    private TrendStore trendStore;
    @Autowired
    private DataUpdateService dataUpdateService;

    @GetMapping("/{id}")
    public List<TrendDTO> getTrendById(@PathVariable String id) {
        var trend = trendStore.getTrend(id);
        if (trend == null) {
            return List.of();
        }
        return trend;
    }

    @GetMapping("/all")
    public List<String> getAllTrendsNames() {
        return trendStore.getAllIds();
    }


}
