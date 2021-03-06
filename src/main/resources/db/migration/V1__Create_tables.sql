CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `username` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '账号',
  `avatar` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像地址',
  `password` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` bit(1) DEFAULT false COMMENT '是否删除。0-未删除；1-删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


CREATE TABLE `blogs` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '博客编号',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `title` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '博客标题',
  `description` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '博客描述',
  `content` text COLLATE utf8mb4_bin DEFAULT NULL COMMENT '博客内容',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` bit(1) DEFAULT false COMMENT '是否删除。0-未删除；1-删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;