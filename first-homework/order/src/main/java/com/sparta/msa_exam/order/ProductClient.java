package com.sparta.msa_exam.order;

import com.sparta.msa_exam.order.dto.ProductRequestDto;
import com.sparta.msa_exam.order.dto.ProductResponseDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/products")
    List<ProductResponseDto> getProductList();

    @PostMapping("/products")
    ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto productDto);
}
