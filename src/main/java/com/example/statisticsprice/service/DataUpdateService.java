package com.example.statisticsprice.service;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DataUpdateService {
    private static final String url = "https://store.steampowered.com/api/appdetails?appids={ID}&cc=ru&filters=price_overview";
    final String regex = "\"final\":(\\d*)";
    @Autowired
    private TrendStore trendStore;

    @Scheduled(fixedRate = 1,timeUnit = TimeUnit.MINUTES)
    public void dataUpdate() {
        System.out.println("Начали обновление всех списков!");
        for (String id : trendStore.getAllIds()) {
            try {
                String newPrice = updatePrice(id);
                trendStore.addData(id, newPrice);
            }
            catch (RuntimeException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public String updatePrice(String Id) {
        // Создаем экземпляр клиента
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Создаем запрос GET
            HttpGet request = new HttpGet(url.replace("{ID}", Id));

            // Отправляем запрос и получаем ответ
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Проверяем статус ответа
                int statusCode = response.getCode();
                if (statusCode != 200) {
                    throw new RuntimeException("ошибка в момент запроса!");
                }
                String responseBody = EntityUtils.toString(response.getEntity());


                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(responseBody);

                matcher.find();
                return matcher.group(1);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
