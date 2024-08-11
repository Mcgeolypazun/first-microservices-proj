package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.ProductClient;
import com.sparta.msa_exam.order.dto.OrderDto;
import com.sparta.msa_exam.order.dto.ProductDto;
import com.sparta.msa_exam.order.dto.ProductRequestDto;
import com.sparta.msa_exam.order.dto.ProductResponseDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.OrderProduct;
import com.sparta.msa_exam.order.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;
    private final OrderRepository orderRepository;

    public List<ProductResponseDto> getAllProductsFromProductService() {
        return productClient.getProductList();
    }

    public ProductResponseDto createProductInProductService(ProductRequestDto productDto) {
        ResponseEntity<ProductResponseDto> responseEntity = productClient.createProduct(productDto);
        return responseEntity.getBody(); // 생성된 상품의 정보를 반환
    }


    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        // Order 엔티티 생성
        Order order = Order.builder()
            .name(orderDto.getName())
            .build();

        // OrderProduct 엔티티 생성 및 Order와 연관관계 설정
        List<OrderProduct> orderProducts = (orderDto.getProductIds() != null ?
            orderDto.getProductIds().stream()
                .map(productId -> OrderProduct.builder()
                    .productId(productId)
                    .order(order)
                    .build())
                .collect(Collectors.toList()) :
            new ArrayList<>());

        // Order 엔티티에 OrderProduct 리스트 설정
        order.setProductIds(orderProducts);

        // 데이터베이스에 저장
        Order savedOrder = orderRepository.save(order);

        // 저장된 Order 엔티티를 DTO로 변환
        return convertToDto(savedOrder);
    }

    private OrderDto convertToDto(Order order) {
        List<Long> productIds = (order.getProductIds() != null ?
            order.getProductIds().stream()
                .map(OrderProduct::getProductId)
                .collect(Collectors.toList()) :
            new ArrayList<>());

        return OrderDto.builder()
            .orderId(order.getId())
            .name(order.getName())
            .productIds(productIds)
            .build();
    }

    @Transactional
    public OrderDto addProductToOrder(Long orderId, Long productId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));

        // productId가 null인지 확인 후 처리
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        OrderProduct orderProduct = OrderProduct.builder()
            .order(order)
            .productId(productId)
            .build();

        // Order 엔티티의 productIds 리스트에 추가
        if (order.getProductIds() == null) {
            order.setProductIds(new ArrayList<>());
        }
        order.getProductIds().add(orderProduct);

        return OrderDto.fromEntity(orderRepository.save(order));
    }


    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"주문을 찾을 수 없습니다."));

        return OrderDto.fromEntity(order);
    }
}
