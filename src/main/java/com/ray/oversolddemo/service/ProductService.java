package com.ray.oversolddemo.service;

import com.ray.oversolddemo.dto.req.BuyProductReqDto;
import com.ray.oversolddemo.dto.res.BuyProductResDto;
import com.ray.oversolddemo.dto.req.InitProductReqDto;
import com.ray.oversolddemo.dto.res.InitProductResDto;

public interface ProductService {

    public InitProductResDto initProduct(InitProductReqDto request);

    public BuyProductResDto orderProduct(BuyProductReqDto request);

}
