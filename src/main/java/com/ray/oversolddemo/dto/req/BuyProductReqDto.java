package com.ray.oversolddemo.dto.req;

import lombok.Data;

@Data
public class BuyProductReqDto {
    private Long productId;
    private Integer amount;
    private Long userId;
}
