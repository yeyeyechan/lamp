package com.springboot.lamp.data.dao.impl;

import com.springboot.lamp.data.dao.ProductDAO;
import com.springboot.lamp.data.entity.Product;
import com.springboot.lamp.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ProductDAOImpl implements ProductDAO {
    private final ProductRepository productRepository;

    @Autowired
    public ProductDAOImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Product insertProduct(Product product){
        Product saveProduct = this.productRepository.save(product);
        return saveProduct;
    }
    @Override
    public Product selectProduct(Long number){
        Product SelectedProduct = this.productRepository.getById(number);
        return SelectedProduct;
    }
    @Override
    public Product updateProductName(Long number, String name) throws Exception{
        Optional<Product> selectedProduct = this.productRepository.findById(number);

        Product updatedProduct;
        if(selectedProduct.isPresent()){
            Product product = selectedProduct.get();
            product.setName(name);
            product.setUpdatedAt(LocalDateTime.now());
            updatedProduct = productRepository.save(product);

        }else{
            throw new Exception();
        }
        return updatedProduct;
    }

    @Override
    public void deleteProduct(Long number) throws Exception{
        Optional<Product> selectedProduct = this.productRepository.findById(number);

        if(selectedProduct.isPresent()){
            Product product = selectedProduct.get();
            this.productRepository.delete(product);
        }else{
            throw new Exception();
        }
    }
}
