請用 java17 執行

運行多實例 :  
java -jar oversold-demo-0.0.1-SNAPSHOT.jar --server.port=8081  
java -jar oversold-demo-0.0.1-SNAPSHOT.jar --server.port=8082

MySQL Server版本 : 5.7.29-log

## 測試情境一:  
### 使用DB樂觀鎖，實現下訂功能  

DB樂觀鎖測試Client端:
https://github.com/adcw987654/NodejsCrawler/blob/master/java_stress_testing/overSoldTest.js

創建Table 及 模擬資料
```roomsql
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `amount` int(10) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `purchase_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `quantity` int(10) NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO product(`name`,`amount`) VALUES ('A',100);
```

下單邏輯:
```roomsql
    @Override
    @Transactional
    public BuyProductResDto orderProductById(BuyProductDto request) {
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
            } else if(retry < 3){
                productRepository.updateAmountById(oldAmount - orderAmount, oldAmount, productId);
                retry ++;
            }
        }
        return new BuyProductResDto("購買失敗，請稍後再試!,購買user:" + request.getUserId(), false);
    }
```
### 樂觀鎖的條件需額外注意ABA問題，  
此案例，假設只會購買成功，所以票數只會減，不會加，故不用額外加入version欄位，  
如果有購買失敗/退票機制或者讓票數增加的邏輯，則需額外加入version欄位，  
並將狀態條件指定為version。


## 測試情境二: 
### 透過Redis原子性特性，進行下單
下單邏輯:
```roomsql
    public BuyProductResDto orderProduct(BuyProductReqDto request) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        if (valueOps.decrement("k1") >= 0) {
            String userQTKey = String.format(USER_ORDER_QT_KEY, request.getUserId());
            Boolean userWasOrdered = valueOps.setIfAbsent(userQTKey, request.getAmount());
            if (!userWasOrdered) {
                valueOps.increment(userQTKey, request.getAmount());
            }
            return new BuyProductResDto("購買成功!", true);
        }
        return new BuyProductResDto("購買失敗!", false);
    }
```


跑完可以查詢 purchase_record table;  
確認票券數量是否正確，有無超賣問題。


