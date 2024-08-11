package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.dto.OrderDto;
import com.sparta.msa_exam.order.dto.ProductDto;
import com.sparta.msa_exam.order.dto.ProductRequestDto;
import com.sparta.msa_exam.order.dto.ProductResponseDto;
import com.sparta.msa_exam.order.service.OrderService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Cacheable(value = "products")  // 'products'라는 캐시 이름으로 캐시 적용
    @GetMapping("/products")
    public List<ProductResponseDto> getAllProducts() {
        return orderService.getAllProductsFromProductService();
    }

    @CacheEvict(value = "products", allEntries = true)  // 상품 추가 시 캐시 삭제
    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> createProduct(
        @RequestBody ProductRequestDto productDto) {
        ProductResponseDto createdProduct = orderService.createProductInProductService(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PostMapping("/order")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        OrderDto createdOrder = orderService.createOrder(orderDto);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @PutMapping("/order/{orderId}")
    public ResponseEntity<OrderDto> addProductToOrder(
        @PathVariable Long orderId,
        @RequestBody ProductDto productDto
    ) {
        OrderDto updatedOrder = orderService.addProductToOrder(orderId, productDto.getProductId());
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long orderId) {
        OrderDto order = orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
