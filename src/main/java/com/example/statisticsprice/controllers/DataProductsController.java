package com.example.statisticsprice.controllers;

import com.example.statisticsprice.entity.DataProductEntity;
import com.example.statisticsprice.entity.UserEntity;
import com.example.statisticsprice.repo.DataProductRepo;
import com.example.statisticsprice.repo.UserRepo;
import com.example.statisticsprice.service.DataUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DataProductsController {

    @Autowired
    private DataProductRepo dataProductRepository;

    @Autowired
    private DataUpdateService dataUpdateService;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/products")
    public String listDataProducts(Model model, @AuthenticationPrincipal UserEntity user) {
        var dbUser = userRepo.findByUsername(user.getUsername());
        model.addAttribute("products", dbUser.get().getDataProducts());
        return "data-products";
    }

    @RequestMapping("/**")
    public String fallback() {
        return "redirect:/products"; // Переадресация на нужный путь
    }

    @GetMapping("/products/{uuid}")
    public String productDetails(@PathVariable String uuid, Model model, @AuthenticationPrincipal UserEntity user) {
        var dbUser = userRepo.findByUsername(user.getUsername());
        var dbProduct = dbUser.get().getDataProducts().stream()
                .filter(dataProductEntity -> uuid.equals(dataProductEntity.getUuid())).findFirst();
        if (dbProduct.isEmpty()) {

            model.addAttribute("error", "попытка обращения к несуществующему id " + uuid);
            return listDataProducts(model, user);
        }
        model.addAttribute("product", dbProduct.get());
        return "product-details";
    }

    @PostMapping("/products/delete")
    public String productDetailsDelete(@RequestParam String uuid, @AuthenticationPrincipal UserEntity user) {
        var dbUser = userRepo.findByUsername(user.getUsername());
        dbUser.get().getDataProducts().removeIf(dataProduct -> dataProduct.getUuid().equals(uuid));
        userRepo.save(dbUser.get());
        return "redirect:/products";
    }

    @PostMapping("/products/add")
    public String addProduct(@RequestParam String uuid, Model model, @AuthenticationPrincipal UserEntity user) {


        var existingProduct = dataProductRepository.findByUuid(uuid);
        if (existingProduct.isPresent()) {
            addExistedProductToUser(existingProduct.get(), user);
            return listDataProducts(model, user);
        }

        String gameName = "";
        try {
            gameName = dataUpdateService.getName(uuid);
        } catch (RuntimeException e) {
            // Если при обновлении произошла ошибка, отобразим её пользователю
            model.addAttribute("error", "Не удалось проверить валидность id. Он не будет добавлен");
            // Возвращаем обновлённую страницу со списком продуктов и сообщением об ошибке
            return listDataProducts(model, user);
        }

        // Если всё прошло успешно, сохраняем новый продукт
        addNewProductToUser(uuid, gameName, user);
        return "redirect:/products";
    }

    private void addExistedProductToUser(DataProductEntity product, UserEntity user) {
        var dbUser = userRepo.findByUsername(user.getUsername());
        dbUser.get().addDataProduct(product);
        userRepo.save(dbUser.get());
    }

    private void addNewProductToUser(String uuid, String gameName, UserEntity user) {
        var dbUser = userRepo.findByUsername(user.getUsername());
        var product = new DataProductEntity(uuid, gameName);
        dataProductRepository.save(product);
        dbUser.get().addDataProduct(product);
        userRepo.save(dbUser.get());
    }
}
