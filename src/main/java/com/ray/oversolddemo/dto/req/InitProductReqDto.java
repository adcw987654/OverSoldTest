package com.ray.oversolddemo.dto.req;

import lombok.Data;

@Data
public class InitProductReqDto {
    private Long productId;
    private Integer stockAmount;
}
