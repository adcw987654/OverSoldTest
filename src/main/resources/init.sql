CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `amount` int(10) NOT NULL,
  INDEX idx_product_amount (amount),
  PRIMARY KEY (`id`)
);
CREATE TABLE `purchase_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `quantity` int(10) NOT NULL,
  PRIMARY KEY (`id`)
);
insert into product(`name`,`amount`) values ('A',10),('B',10),('C',10);
