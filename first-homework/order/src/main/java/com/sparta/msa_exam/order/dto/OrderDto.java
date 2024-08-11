package com.sparta.msa_exam.order.dto;

import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.OrderProduct;
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long orderId;
    private String name;
    private List<Long> productIds = new ArrayList<>(); // 기본값을 빈 리스트로 설정

    public static OrderDto fromEntity(Order order) {
        return OrderDto.builder()
            .orderId(order.getId())
            .name(order.getName())
            .productIds(order.getProductIds() == null ?
                new ArrayList<>() :
                order.getProductIds().stream()
                    .map(OrderProduct::getProductId)
                    .collect(Collectors.toList()))
            .build();
    }
}


