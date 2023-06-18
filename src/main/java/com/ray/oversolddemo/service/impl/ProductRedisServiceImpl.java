package com.ray.oversolddemo.service.impl;

import com.ray.oversolddemo.dto.req.BuyProductReqDto;
import com.ray.oversolddemo.dto.res.BuyProductResDto;
import com.ray.oversolddemo.dto.req.InitProductReqDto;
import com.ray.oversolddemo.dto.res.InitProductResDto;
import com.ray.oversolddemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service("ProductRedisService")
public class ProductRedisServiceImpl implements ProductService {

    @Autowired
    RedisTemplate redisTemplate;

    private static final String STOCK_KEY = "product:%s:st";
    private static final String USER_ORDER_QT_KEY = "user:%s:%s:qt";


    @Override
    public InitProductResDto initProduct(InitProductReqDto request) {
        Long productId = request.getProductId();
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        Set<String> keysToDelete = redisTemplate.keys("user:*:qt");
        redisTemplate.delete(keysToDelete);
        valueOps.set(String.format(STOCK_KEY, productId), request.getStockAmount(), Duration.ofSeconds(100));
        return new InitProductResDto(true);
    }

    @Override
    public BuyProductResDto orderProduct(BuyProductReqDto request) {
        Long productId = request.getProductId();
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        String userQtKey = String.format(USER_ORDER_QT_KEY, productId, request.getUserId());
        if (valueOps.decrement(String.format(STOCK_KEY, productId)) >= 0) {
            Boolean userWasOrdered = valueOps.setIfAbsent(userQtKey, request.getAmount());
            if (!userWasOrdered) {
                valueOps.increment(userQtKey, request.getAmount());
            }
            return new BuyProductResDto("購買成功!", true);
        } else {
            valueOps.increment(userQtKey);
        }
        return new BuyProductResDto("購買失敗!", false);
    }


}
