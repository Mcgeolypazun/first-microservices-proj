package com.sparta.msa_exam.order.dto;



import com.sparta.msa_exam.order.entity.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDto {

    private Long productId;
    private String name;
    private Integer supplyPrice;

    public static ProductResponseDto fromEntity(Product product) {
        return ProductResponseDto.builder()
            .productId(product.getProductId())
            .name(product.getName())
            .supplyPrice(product.getSupplyPrice())
            .build();
    }
}

