package com.example.statisticsprice.service;
import com.example.statisticsprice.dto.TrendDTO;

import java.util.List;

public interface TrendStore {
    public void addData(String gameId,  String price);
    public List<TrendDTO> getTrend(String gameId);
    List<String> getAllIds ();
}
