package com.example.statisticsprice.controllers;

import com.example.statisticsprice.entity.DataProductEntity;
import com.example.statisticsprice.repo.DataProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DataProductsController {

    @Autowired
    private DataProductRepo dataProductRepository;

    @GetMapping("/products")
    public String listDataProducts(Model model) {
        List<DataProductEntity> products = dataProductRepository.findAll();
        model.addAttribute("products", products);
        return "data-products";
    }

    @GetMapping("/product/{uuid}")
    public String productDetails(@PathVariable String uuid, Model model) {
        DataProductEntity product = dataProductRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Продукт не найден: " + uuid));
        model.addAttribute("product", product);
        return "product-details";
    }

    @PostMapping("/products/add")
    public String addProduct(@RequestParam String uuid, Model model) {
        if (dataProductRepository.findByUuid(uuid).isPresent()) {
            model.addAttribute("error", "UUID уже существует!");
            return listDataProducts(model); // Отобразить список снова с ошибкой
        }

        DataProductEntity product = new DataProductEntity();
        product.setUuid(uuid);
        dataProductRepository.save(product);
        return "redirect:/products";
    }
}
