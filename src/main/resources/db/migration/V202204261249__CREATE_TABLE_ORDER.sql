CREATE TABLE IF NOT EXISTS `order` (
    `id` bigint unsigned auto_increment NOT NULL COMMENT '订单id',
    `order_code` varchar(255) NOT NULL COMMENT '订单编号',
    `description` varchar(255) COMMENT '描述',
    `order_title` varchar(255) COMMENT '订单标题',
    `total_price` bigint unsigned NOT NULL COMMENT '总价',
    `creator` varchar(255) NOT NULL COMMENT '创建者',
    `modifier` varchar(255) NOT NULL COMMENT '修改者',
    `create_time` varchar(255) NOT NULL COMMENT '创建时间',
    `update_time` varchar(255) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE =utf8mb4_0900_ai_ci;