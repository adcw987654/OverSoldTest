package com.ray.oversolddemo.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BuyProductResDto {
    private String msg;
    private Boolean isSuccess;
}
