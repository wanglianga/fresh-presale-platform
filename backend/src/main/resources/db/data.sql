-- 初始化基础数据

-- 团长
INSERT INTO leader (id, name, phone, address, active, created_at, updated_at) VALUES
(1, '李团长', '13900139001', '阳光花园小区物业办公室', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '王团长', '13900139002', '幸福里小区东门便利店', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 小区团点
INSERT INTO community (id, name, address, contact_phone, leader_id, leader_name, active, created_at, updated_at) VALUES
(1, '阳光花园小区', '北京市朝阳区建国路88号', '13900139001', 1, '李团长', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '幸福里小区', '北京市海淀区中关村大街1号', '13900139002', 2, '王团长', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 供应商
INSERT INTO supplier (id, name, contact_person, phone, address, freshness_capability, active, created_at, updated_at) VALUES
(1, '绿源蔬菜基地', '张经理', '13800138001', '山东省寿光市蔬菜产业园', '蔬菜冷藏保鲜', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '宏达肉业', '李总', '13800138002', '河南省漯河市食品工业园', '冷链运输-18°C', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, '冰泉冷冻食品', '王经理', '13800138003', '天津市滨海新区冷冻食品基地', '速冻冷链配送', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 预售批次
INSERT INTO presale_batch (id, batch_name, start_time, end_time, delivery_date, status, total_amount, remark, created_at, updated_at) VALUES
(1, '6月8日预售批次', '2026-06-08 00:00:00', '2026-06-08 18:00:00', '2026-06-09 08:00:00', 'ACTIVE', 0, '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '6月9日预售批次', '2026-06-09 00:00:00', '2026-06-09 18:00:00', '2026-06-10 08:00:00', 'ACTIVE', 0, '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 商品
INSERT INTO product (id, name, description, image_url, price, original_price, unit, stock, category, product_type, freshness_condition, active, created_at, updated_at) VALUES
(1, '有机小白菜', '新鲜有机小白菜，当日采摘', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=fresh%20organic%20bok%20choy%20vegetable&image_size=square', 3.50, 4.50, '斤', 500, '蔬果', '叶菜', '冷藏0-4°C', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '土猪五花肉', '散养土猪五花肉，肥瘦相间', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=fresh%20pork%20belly%20meat%20on%20white%20background&image_size=square', 28.80, 32.00, '斤', 200, '肉禽', '猪肉', '冷藏0-4°C', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, '速冻水饺', '猪肉白菜馅速冻水饺，500g装', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=frozen%20dumplings%20package&image_size=square', 19.90, 25.00, '袋', 300, '冷冻品', '速冻食品', '冷冻-18°C以下', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, '新鲜鸡蛋30枚', '农家散养鸡蛋30枚装', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=fresh%20farm%20eggs%2030%20pack&image_size=square', 25.80, 30.00, '盒', 150, '组合套餐', '蛋类', '常温阴凉处', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, '精选西红柿', '自然成熟沙瓤西红柿', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=fresh%20red%20tomatoes%20vegetables&image_size=square', 5.80, 7.00, '斤', 400, '蔬果', '茄果', '冷藏0-4°C', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, '冰鲜三文鱼', '进口冰鲜三文鱼刺身', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=fresh%20salmon%20sashimi%20fillet&image_size=square', 68.00, 88.00, '斤', 80, '肉禽', '海鲜', '冷藏0-4°C，当日食用', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 预售单
INSERT INTO presale_order (id, order_no, presale_batch_id, customer_name, customer_phone, community_id, community_name, leader_id, leader_name, total_amount, refund_amount, status, pickup_code, remark, created_at, updated_at) VALUES
(1, 'SO202606080001', 1, '张三', '13800138000', 1, '阳光花园小区', 1, '李团长', 35.80, 0.00, 'PAID', 'A8K2M9', '请早上8点后自提', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'SO202606080002', 1, '李四', '13900139000', 2, '幸福里小区', 2, '王团长', 59.70, 0.00, 'PAID', 'B3P7Q2', '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'SO202606080003', 1, '王五', '13700137000', 1, '阳光花园小区', 1, '李团长', 51.60, 5.00, 'PICKED_UP', 'C5N8R1', '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 预售单明细
INSERT INTO presale_order_item (id, order_id, product_id, product_name, product_image, price, quantity, subtotal, actual_weight, status) VALUES
(1, 1, 1, '有机小白菜', NULL, 3.50, 2, 7.00, NULL, 'NORMAL'),
(2, 1, 2, '土猪五花肉', NULL, 28.80, 1, 28.80, NULL, 'NORMAL'),
(3, 2, 3, '速冻水饺', NULL, 19.90, 3, 59.70, NULL, 'NORMAL'),
(4, 3, 4, '新鲜鸡蛋30枚', NULL, 25.80, 2, 51.60, NULL, 'WEIGHT_DIFF');

-- 采购批次
INSERT INTO purchase_batch (id, batch_no, presale_batch_id, presale_batch_name, supplier_id, supplier_name, status, total_amount, freshness_condition, remark, confirm_time, ship_time, arrive_time, created_at, updated_at) VALUES
(1, 'PB20260608001', 1, '6月8日预售批次', 1, '绿源蔬菜基地', 'ARRIVED', 1200.00, '冷藏0-4°C运输', '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'PB20260608002', 1, '6月8日预售批次', 2, '宏达肉业', 'SHIPPED', 4400.00, '冷链运输-18°C', '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 采购批次明细
INSERT INTO purchase_batch_item (id, purchase_batch_id, product_id, product_name, plan_quantity, confirm_quantity, actual_quantity, price, subtotal, status) VALUES
(1, 1, 1, '有机小白菜', 500, 480, 480, 2.50, 1200.00, 'ARRIVED'),
(2, 2, 2, '土猪五花肉', 200, 200, NULL, 22.00, 4400.00, 'SHIPPED');

-- 分拣记录
INSERT INTO sorting_record (id, sorting_no, purchase_batch_id, purchase_batch_no, community_id, community_name, status, remark, sorting_time, created_at, updated_at) VALUES
(1, 'SR20260608001', 1, 'PB20260608001', 1, '阳光花园小区', 'COMPLETED', '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'SR20260608002', 1, 'PB20260608001', 2, '幸福里小区', 'PENDING', '', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 分拣明细
INSERT INTO sorting_item (id, sorting_record_id, order_item_id, product_id, product_name, expected_quantity, actual_quantity, expected_weight, actual_weight, status, remark) VALUES
(1, 1, 1, 1, '有机小白菜', 2, 2, NULL, NULL, 'NORMAL', ''),
(2, 1, 4, 4, '新鲜鸡蛋30枚', 2, 1, NULL, NULL, 'DISCREPANCY', '缺货1盒');

-- 分拣差异
INSERT INTO sorting_discrepancy (id, sorting_record_id, sorting_item_id, product_id, product_name, discrepancy_type, quantity, weight_diff, replaced_product_id, replaced_product_name, replaced_quantity, compensation_amount, status, remark, created_at, updated_at) VALUES
(1, 1, 2, 4, '新鲜鸡蛋30枚', 'OUT_OF_STOCK', 1, NULL, NULL, NULL, NULL, 25.80, 'REFUNDED', '供货商缺货，已退款', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 2, 4, '新鲜鸡蛋30枚', 'WEIGHT_DIFF', 0, 0.3, NULL, NULL, NULL, 5.00, 'COMPENSATED', '称重不足，差额补偿', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 团长签收
INSERT INTO leader_receipt (id, receipt_no, sorting_record_id, sorting_no, leader_id, leader_name, community_id, community_name, status, remark, receipt_time, pickup_notify_time, created_at, updated_at) VALUES
(1, 'LR20260608001', 1, 'SR20260608001', 1, '李团长', 1, '阳光花园小区', 'NOTIFIED', '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 售后申请
INSERT INTO after_sale_request (id, request_no, order_id, order_no, request_type, source, request_amount, approved_amount, status, description, image_urls, handle_remark, handle_time, created_at, updated_at) VALUES
(1, 'AS20260608001', 3, 'SO202606080003', 'WEIGHT_DIFF', 'SORTING', 5.00, 5.00, 'APPROVED', '分拣称重不足约0.3斤', NULL, '已批准补偿5元', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'AS20260608002', 1, 'SO202606080001', 'OUT_OF_STOCK', 'SORTING', 25.80, NULL, 'PENDING', '分拣发现鸡蛋缺货1盒', NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 售后明细
INSERT INTO after_sale_item (id, after_sale_request_id, order_item_id, product_id, product_name, quantity, amount, reason) VALUES
(1, 1, 4, 4, '新鲜鸡蛋30枚', 1, 5.00, '重量差异补偿'),
(2, 2, 1, 4, '新鲜鸡蛋30枚', 1, 25.80, '缺货退款');

-- 退款记录
INSERT INTO refund_record (id, refund_no, order_id, order_no, after_sale_request_id, request_no, discrepancy_id, refund_type, reason, refund_amount, status, remark, refund_time, created_at, updated_at) VALUES
(1, 'RF20260608001', 3, 'SO202606080003', 1, 'AS20260608001', 2, 'WEIGHT_DIFF', '称重差异补偿', 5.00, 'COMPLETED', '已原路退回', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 补偿记录
INSERT INTO compensation_record (id, compensation_no, order_id, order_no, after_sale_request_id, discrepancy_id, compensation_type, reason, compensation_amount, status, remark, compensate_time, created_at, updated_at) VALUES
(1, 'CP20260608001', 3, 'SO202606080003', 1, 2, 'WEIGHT_DIFF', '重量不足补偿', 5.00, 'COMPLETED', '已发放优惠券', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
