package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.ProductRequestDto;
import com.sparta.msa_exam.product.dto.ProductResponseDto;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productDto) {
        // Product 엔티티 생성
        Product product = Product.builder()
            .name(productDto.getName())
            .supplyPrice(productDto.getSupplyPrice())
            .build();

        // 데이터베이스에 저장
        Product savedProduct = productRepository.save(product);

        return ProductResponseDto.fromEntity(savedProduct);
    }

    public List<ProductResponseDto> getProductList() {
        List<Product> productList = productRepository.findAll();

        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();

        for (Product product : productList) {
            ProductResponseDto dto = new ProductResponseDto(
                product.getProductId(),
                product.getName(),
                product.getSupplyPrice()
            );
            productResponseDtoList.add(dto);
        }

        return productResponseDtoList;
    }


}
