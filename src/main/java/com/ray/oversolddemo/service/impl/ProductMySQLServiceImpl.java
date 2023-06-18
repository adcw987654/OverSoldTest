package com.ray.oversolddemo.service.impl;

import com.ray.oversolddemo.dao.ProductRepository;
import com.ray.oversolddemo.dao.PurchaseRecordRepository;
import com.ray.oversolddemo.dto.req.BuyProductReqDto;
import com.ray.oversolddemo.dto.req.InitProductReqDto;
import com.ray.oversolddemo.dto.res.BuyProductResDto;
import com.ray.oversolddemo.dto.res.InitProductResDto;
import com.ray.oversolddemo.entity.PurchaseRecord;
import com.ray.oversolddemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("ProductMySQLService")
public class ProductMySQLServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PurchaseRecordRepository purchaseRecordRepository;

    @Override
    @Transactional
    public InitProductResDto initProduct(InitProductReqDto request) {
        purchaseRecordRepository.deleteAll();
        productRepository.updateAmount(request.getStockAmount());
        return null;
    }

    @Override
    @Transactional
    public BuyProductResDto orderProduct(BuyProductReqDto request) {
        Long productId = request.getProductId();
        Integer orderAmount = request.getAmount();
        Optional<Integer> amountOpt = productRepository.findAmountById(productId);
        if (amountOpt.isPresent()) {
            Integer oldAmount = amountOpt.get();
            if (orderAmount > oldAmount) {
                return new BuyProductResDto("下單數量超出當前剩餘數量!,購買user:" + request.getUserId(), false);
            }
            int updateRow = productRepository.updateAmountById(oldAmount - orderAmount, oldAmount, productId);
            int retry = 0; //樂觀鎖重試機制
            if (updateRow > 0) {
                PurchaseRecord purchaseRecord = new PurchaseRecord(null, request.getUserId(), productId, orderAmount);
                purchaseRecordRepository.save(purchaseRecord);
                return new BuyProductResDto("購買成功!,購買user:" + request.getUserId(), true);
            } else if (retry < 3) {
                productRepository.updateAmountById(oldAmount - orderAmount, oldAmount, productId);
                retry++;
            }
        }
        return new BuyProductResDto("購買失敗，請稍後再試!,購買user:" + request.getUserId(), false);
    }


}
