package com.sparta.msa_exam.product.dto;


import com.sparta.msa_exam.product.entity.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequestDto {

    private String name;
    private Integer supplyPrice;

    public static ProductRequestDto fromEntity(Product product) {
        return ProductRequestDto.builder()
            .name(product.getName())
            .supplyPrice(product.getSupplyPrice())
            .build();
    }
}

