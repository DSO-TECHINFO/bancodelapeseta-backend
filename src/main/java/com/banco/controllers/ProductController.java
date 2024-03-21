package com.banco.controllers;

import com.banco.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/products")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    @GetMapping
    public ResponseEntity<?> getproducts(){
        return ResponseEntity.ok(productService.getProducts());
    }

}
