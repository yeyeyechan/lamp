package com.springboot.lamp.service;

import com.springboot.lamp.data.dto.ProductDto;
import com.springboot.lamp.data.dto.ProductResponseDto;


public interface ProductService {

    ProductResponseDto getProduct(Long number);

    ProductResponseDto saveProduct(ProductDto productDto);

    ProductResponseDto changeProductName(Long number, String name) throws Exception;

    void deleteProduct(Long number) throws Exception;

}

