package com.ray.oversolddemo.controller;

import com.ray.oversolddemo.dto.req.BuyProductReqDto;
import com.ray.oversolddemo.dto.res.BuyProductResDto;
import com.ray.oversolddemo.dto.req.InitProductReqDto;
import com.ray.oversolddemo.dto.res.InitProductResDto;
import com.ray.oversolddemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/order")
public class ProductController {

    @Autowired
    @Qualifier("ProductMySQLService")
    ProductService productMySqlService;

    @Autowired
    @Qualifier("ProductRedisService")
    ProductService productRedisService;

    @PostMapping("/mysql")
    public BuyProductResDto orderProductByMysql(@RequestBody BuyProductReqDto request) {
        return productMySqlService.orderProduct(request);
    }

    @PostMapping("/mysql/init")
    public InitProductResDto initProductByMySql(@RequestBody InitProductReqDto request) {
        return productMySqlService.initProduct(request);
    }

    @PostMapping("/redis")
    public BuyProductResDto orderProduct(@RequestBody BuyProductReqDto request) {
        return productRedisService.orderProduct(request);
    }

    @PostMapping("/redis/init")
    public InitProductResDto initProductByRedis(@RequestBody InitProductReqDto request) {
        return productRedisService.initProduct(request);
    }

}
