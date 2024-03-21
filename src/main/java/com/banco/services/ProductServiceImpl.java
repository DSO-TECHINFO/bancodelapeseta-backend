package com.banco.services;

import com.banco.entities.Entity;
import com.banco.entities.Product;
import com.banco.repositories.ProductRepository;
import com.banco.utils.EntityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;
    private EntityUtils entityUtils;
    @Override
    public List<Product> getProducts() {
        Entity user = entityUtils.checkIfEntityExists(entityUtils.extractUser());
        return productRepository.findAllByEntityTypeAndActiveTrue(user.getType());
    }
}
