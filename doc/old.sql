--星库第一版 
create database rca_mec_server default charset utf8;

CREATE TABLE IF NOT EXISTS customer (
  id bigint NOT NULL AUTO_INCREMENT,
  tb_id 	  varchar(50),
  tb_username varchar(300),
  tb_nickname varchar(300),
  tb_avatar   varchar(300),
  gender  enum('male','female','privacy'),
  score   int,
  status  int,
  created_at datetime,
  updated_at datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户表';

CREATE TABLE IF NOT EXISTS tb_author (
  id bigint NOT NULL AUTO_INCREMENT,
  customer_id 	  bigint,
  device_id  bigint,
  taobao_user_id 	 varchar(50),
  taobao_user_nick   varchar(300),
  access_token   varchar(300),
  token_type     varchar(50),
  expires_in     varchar(50),
  refresh_token  varchar(300),
  re_expires_in  varchar(50),
  r1_expires_in  varchar(50),
  r2_expires_in  varchar(50),
  w1_expires_in  varchar(50),
  w2_expires_in  varchar(50),
  mobile_token   varchar(100),
  top_sign		 varchar(200),
  invalid_time datetime comment '失效时间',
  status  int,
  created_at datetime,
  updated_at datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment 'taobao授权表';

-- 1. sys_role 
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `name` varchar(32) NOT NULL, 
    `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:valid, 0:invalid', 
    `updated` tinyint(1) unsigned NOT NULL DEFAULT '0', 
    `sequence` mediumint(8) unsigned NOT NULL DEFAULT '99', 
    `purview` text NOT NULL COMMENT 'purview of role', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`), 
    UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 2. sys_user
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `role_id` int(11) unsigned NOT NULL DEFAULT '0', 
    `username` varchar(64) NOT NULL, 
    `password` varchar(40) NOT NULL COMMENT 'sha1 encrypted password', 
    `email` varchar(64) NOT NULL, 
    `realname` varchar(64), 
    `gender` enum('male', 'female', 'privacy') NOT NULL DEFAULT 'privacy', 
    `avatar` varchar(128), 
    `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:valid, 0:invalid', 
    `login_count` mediumint(8) unsigned NOT NULL DEFAULT '0', 
    `login_ip` varchar(16) NOT NULL DEFAULT '0', 
    `last_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`), 
    UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 3. sys_purview
DROP TABLE IF EXISTS `sys_purview`;
CREATE TABLE `sys_purview` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `pid` int(11) unsigned NOT NULL DEFAULT '0', 
    `module` varchar(32) NOT NULL, 
    `method` varchar(32) NOT NULL COMMENT 'add, edit, view, del...', 
    `sequence` mediumint(8) unsigned NOT NULL DEFAULT '99', 
    `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:valid, 0:invalid', 
    PRIMARY KEY (`id`), 
    KEY `pid` (`pid`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 4. sys_session
DROP TABLE IF EXISTS `sys_session`;
CREATE TABLE `sys_session` (
    `session_id` varchar(40) NOT NULL DEFAULT '0', 
    `ip_address` varchar(16) NOT NULL DEFAULT '0', 
    `user_agent` varchar(128) NOT NULL, 
    `last_activity` int(10) unsigned NOT NULL DEFAULT '0', 
    `user_data` text NOT NULL, 
    PRIMARY KEY (`session_id`), 
    KEY `last_activity_idx` (`last_activity`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 5. sys_config
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
    `id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT, 
    `key` varchar(64) NOT NULL, 
    `value` varchar(255) NOT NULL, 
    `description` text, 
    `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:valid, 0:invalid', 
    PRIMARY KEY (`id`), 
    UNIQUE KEY `key` (`key`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 6. person 
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `role_id` int(11) unsigned NOT NULL DEFAULT '0', 
    `name` varchar(64) NOT NULL, 
    `gender` enum('male', 'female', 'privacy'), 
    `avatar` varchar(128), 
    `nickname` varchar(64), 
    `birthday` date NOT NULL DEFAULT '0000-00-00', 
    `tag` varchar(255), 
    `description` text, 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 7. person_role
DROP TABLE IF EXISTS `person_role`;
CREATE TABLE `person_role` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `name` varchar(64) NOT NULL, 
    `description` text, 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`), 
    UNIQUE KEY `name` (`name`)
 ) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 8. channel
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `rid` varchar(64) NOT NULL DEFAULT '0', 
    `name` varchar(64) NOT NULL, 
    `pinyin` varchar(64) NOT NULL, 
    `logo` varchar(128), 
    `description` text, 
    `stream` varchar(255), 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 9. program
DROP TABLE IF EXISTS `program`;
CREATE TABLE `program` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `rid` varchar(64) NOT NULL DEFAULT '0', 
    `title` varchar(128), 
    `type` enum('TV', 'Movie', 'Music', 'Other'), 
    `director_id` int(11) unsigned NOT NULL DEFAULT '0', 
    `writer_id` int(11) unsigned NOT NULL DEFAULT '0', 
    `cast_list` varchar(255), 
    `episode_count` mediumint(4) NOT NULL DEFAULT '0', 
    `description` text, 
    `release_date` date, 
    `recommend` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1:yes, 0:no', 
    `tag` varchar(255), 
    `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:valid, 0:invalid', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 10. episode
DROP TABLE IF EXISTS `episode`;
CREATE TABLE `episode` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `rid` varchar(64) NOT NULL DEFAULT '0', 
    `title` varchar(128), 
    `num` mediumint(4) unsigned NOT NULL DEFAULT '0', 
    `duration` int(11) unsigned NOT NULL DEFAULT '0', 
    `description` text, 
    `views` int(11) unsigned NOT NULL DEFAULT '0', 
    `has_dna` tinyint(1) NOT NULL DEFAULT '0', 
    `tag` varchar(255), 
    `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:valid, 0:invalid', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 11. channel_episode
DROP TABLE IF EXISTS `channel_episode`;
CREATE TABLE `channel_episode` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `channel_id` int(11) unsigned NOT NULL, 
    `episode_id` int(11) unsigned NOT NULL, 
    `start_time` int(10) unsigned NOT NULL, 
    `end_time` int(10) unsigned  NOT NULL, 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 12. item
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `rid` varchar(64) NOT NULL, 
    `name` varchar(128) NOT NULL,
    `img` varchar(255) NOT NULL,
    `price` float(10,2) NOT NULL DEFAULT '0.00',
    `sale_price` float(10,2) NOT NULL DEFAULT '0.00',
    `sku` text NOT NULL, 
    `description` text, 
    `shippable` tinyint(1) NOT NULL DEFAULT '1', 
    `free_shipping` tinyint(1) NOT NULL DEFAULT '1', 
    `ship_price` float(10,2) NOT NULL DEFAULT '0.00',
    `weight` float(10,2) NOT NULL DEFAULT '0.00', 
    `track_stock` tinyint(1) NOT NULL DEFAULT '0', 
    `stock` int(11) unsigned NOT NULL DEFAULT '0', 
    `on_sale` tinyint(1) NOT NULL DEFAULT '0', 
    `recommend` tinyint(1) NOT NULL DEFAULT '0', 
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 13. device
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `udid` varchar(40) NOT NULL,
    `ucid` varchar(32) NOT NULL,  
    `os` enum('IOS', 'Android', 'Unknow') NOT NULL DEFAULT 'Unknow', 
    `version` varchar(32), 
    `brand` varchar(32), 
    `tb_id` int(11) unsigned, 
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 14. customer
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `tb_id` varchar(64) NOT NULL,
    `tb_username` varchar(128), 
    `tb_nickname` varchar(128), 
    `tb_avatar` varchar(255), 
    `gender` enum('male', 'female', 'privacy') NOT NULL DEFAULT 'privacy', 
    `score` int(11) NOT NULL DEFAULT '0',
    `status` tinyint(1) NOT NULL DEFAULT '1',  
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 15. trade
DROP TABLE IF EXISTS `trade`;
CREATE TABLE `trade` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `customer_id` int(11) unsigned NOT NULL, 
    `item_id` int(11) unsigned NOT NULL, 
    `sku_id` varchar(255) NOT NULL, 
    `address_id` varchar(64) NOT NULL,
    `count` int,
    `status` tinyint(1) NOT NULL DEFAULT '0', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 16. tb_author
DROP TABLE IF EXISTS `tb_author`;
CREATE TABLE `tb_author` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `device_id` int(11) unsigned, 
    `customer_id` int(11) unsigned, 
    `taobao_user_id` varchar(128), 
    `taobao_user_nick` varchar(128), 
    `access_token` varchar(128), 
    `token_type` varchar(64), 
    `expires_in` varchar(64), 
    `refresh_token` varchar(128), 
    `re_expires_in` varchar(64), 
    `r1_expires_in` varchar(64), 
    `r2_expires_in` varchar(64), 
    `w1_expires_in` varchar(64), 
    `w2_expires_in` varchar(64), 
    `mobile_token` varchar(128), 
    `top_sign` varchar(128), 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 17. poster_folder
DROP TABLE IF EXISTS `poster_folder`;
CREATE TABLE `poster_folder` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `name` varchar(64) NOT NULL, 
    `dir` varchar(128) NOT NULL, 
    `tag` varchar(255), 
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 18. poster 
DROP TABLE IF EXISTS `poster`;
CREATE TABLE `poster` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `fid` int(11) unsigned NOT NULL, 
    `path` varchar(128), 
    `description` text, 
    `tag` varchar(255),
    `favorite_count` int,
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 19. person_poster_folder
DROP TABLE IF EXISTS `person_poster_folder`;
CREATE TABLE `person_poster_folder` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `person_id` int(11) unsigned NOT NULL, 
    `poster_folder_id` int(11) unsigned NOT NULL, 
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`), 
    UNIQUE key `person_poster_folder_idx` (`person_id`, `poster_folder_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 20. person_poster
DROP TABLE IF EXISTS `person_poster`;
CREATE TABLE `person_poster` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `person_id` int(11) unsigned NOT NULL, 
    `poster_id` int(11) unsigned NOT NULL, 
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    PRIMARY KEY (`id`), 
    UNIQUE key `person_poster_idx` (`person_id`, `poster_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 21. program_poster_folder
DROP TABLE IF EXISTS `program_poster_folder`;
CREATE TABLE `program_poster_folder` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `program_id` int(11) unsigned NOT NULL, 
    `poster_folder_id` int(11) unsigned NOT NULL, 
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    PRIMARY KEY (`id`), 
    UNIQUE key `program_poster_folder_idx` (`program_id`, `poster_folder_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 22. progam_poster
DROP TABLE IF EXISTS `program_poster`;
CREATE TABLE `program_poster` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `program_id` int(11) unsigned NOT NULL, 
    `poster_id` int(11) unsigned NOT NULL, 
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`), 
    UNIQUE key `program_poster_idx` (`program_id`, `poster_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 23. poster_item
DROP TABLE IF EXISTS `poster_item`;
CREATE TABLE `poster_item` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `poster_id` int(11) unsigned NOT NULL, 
    `item_id` int(11) unsigned NOT NULL, 
    `description` text, 
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 24. feed
DROP TABLE IF EXISTS `feed`;
CREATE TABLE `feed` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `rid` int(11) unsigned NOT NULL, 
    `type` enum('person', 'program') NOT NULL DEFAULT 'person', 
    `poster_id` int(11) unsigned NOT NULL, 
    `item_list` varchar(255) NOT NULL, 
    `start_time` int(10) unsigned NOT NULL, 
    `end_time` int(10) unsigned NOT NULL, 
    `fixed_time` int(10) unsigned NOT NULL, 
    `content` text, 
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 25. customer_feed_history
DROP TABLE IF EXISTS `customer_feed_history`;
CREATE TABLE `customer_feed_history` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `customer_id` int(11) unsigned NOT NULL, 
    `rid` int(11) unsigned NOT NULL COMMENT 'program_id or feed_id, depends on type', 
    `type` enum('feed', 'program') NOT NULL DEFAULT 'feed', 
    `poster_id` int(11) unsigned NOT NULL, 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 26. customer_wishlist
DROP TABLE IF EXISTS `customer_wishlist`;
CREATE TABLE `customer_wishlist` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `customer_id` int(11) unsigned NOT NULL, 
    `poster_id` int(11) unsigned NOT NULL, 
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 27. score_history
DROP TABLE IF EXISTS `score_history`;
CREATE TABLE `score_history` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `customer_id` int(11) unsigned NOT NULL, 
    `type` varchar(64) NOT NULL, 
    `score` int(11) unsigned NOT NULL DEFAULT '0', 
    `description` text, 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;





CREATE TABLE `blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` mediumtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


INSERT INTO `blog` VALUES (1,'JFinal Demo Title here','JFinal Demo Content here');
INSERT INTO `blog` VALUES (3,'test 2','test 2');
INSERT INTO `blog` VALUES (5,'test 4','test 4dsd');
INSERT INTO `blog` VALUES (6,'d','d');


