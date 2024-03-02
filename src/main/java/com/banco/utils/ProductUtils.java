package com.banco.utils;

import com.amazonaws.services.codeartifact.model.PackageOriginConfiguration;
import com.banco.entities.Product;
import com.banco.exceptions.CustomException;
import com.banco.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductUtils {
    private ProductRepository productRepository;
    public Optional<Product> extractProduct(Long productId){
        return productRepository.findById(productId);
    }
    public Product checkProduct(Optional<Product> product) throws CustomException {
        if(product.isEmpty()){
            throw new CustomException("PRODUCTS-001", "Product not found", 404);
        }
        Product tmp = product.get();
        if (!tmp.getActive())
            throw new CustomException("PRODUCTS-002", "That product is not available", 400);
        return tmp;
    }
}
