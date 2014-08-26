//天猫版schemal
alter table question add column created_user int(11) unsigned after created_at;
alter table question add column updated_user int(11) unsigned after updated_at;

alter table mec_person add column created_user int(11) unsigned after created_at;
alter table mec_person add column updated_user int(11) unsigned after updated_at;

alter table baike add column created_user int(11) unsigned after created_at;
alter table baike add column updated_user int(11) unsigned after updated_at;

alter table music add column created_user int(11) unsigned after created_at;
alter table music add column updated_user int(11) unsigned after updated_at;

alter table syn_weibo_task add column min_time datetime after times;
alter table syn_weibo_task add column max_time datetime after min_time;


update mec_person set created_user=1,updated_user=1;

重新导入: question

--抽奖活动
create table `lucky_draw_event`(
	`id` int(11) unsigned primary key auto_increment,
	`title` varchar(255) comment '标题',
	`summary` text comment '说明',
	`start_time` datetime COMMENT '开始时间',
  	`end_time` datetime COMMENT '结束时间',
	`estimate_count` int comment '预计参与人数',
	`url` varchar(255) comment '抽奖地址',
	`win_day` int(6) comment '中奖自然天',
	`win_count` int(6) comment '中奖次数',
	`seq` int(11) unsigned comment '顺序',
	`win_seq` int(11) unsigned comment '中奖顺序',
	`status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=MyISAM CHARSET=utf8;


--抽奖活动和剧集关系
create table `lucky_draw_episode`(
	`id` int(11) unsigned primary key auto_increment,
	`lucky_id` int(11) unsigned,
	`program_id` int(11) unsigned,
    `episode_id` int(11) unsigned,
	`start_time` int(10) unsigned COMMENT '开始时间', 
	`end_time` int(10) unsigned COMMENT '结束时间',
	`status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=MyISAM CHARSET=utf8;


--抽奖奖项
create table `lucky_draw_option`(
	`id` int(11) unsigned primary key auto_increment,
	`lucky_id` int(11) unsigned,
	`position` int(6) unsigned comment '奖项级别',
	`title` varchar(255) comment '奖项标题',
	`summary` text comment '说明',
	`prize_count` int(6) comment '奖品数',
	`surplus_prize_count` int(6) comment '剩余奖品数',
	`win_rate` varchar(16) comment '中奖比率',
	`estimate_count` int comment '预计参与人数',
	`url` varchar(255) comment '商品url',
	`isluck` tinyint(2) NOT NULL DEFAULT '1',
	`status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=MyISAM CHARSET=utf8;

--用户抽奖记录
CREATE TABLE `lucky_draw_history` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `udid` varchar(64), 
    `lucky_id` int(11) unsigned comment '抽奖活动id',
    `option_id` int(11) unsigned comment '抽奖奖品id',
    `seq` int(11) unsigned comment '顺序',
    `status` tinyint(2) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=MyISAM CHARSET=utf8;


--预先中奖纪录
CREATE TABLE `lucky_draw_before` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `lucky_id` int(11) unsigned comment '抽奖活动id',
    `option_id` int(11) unsigned comment '抽奖奖品id',
    `seq` int(6) unsigned comment '顺序',
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=MyISAM CHARSET=utf8;


CREATE TABLE `banner_img` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `program_id` int(11) unsigned comment '节目id',
    `episode_id` int(11) unsigned comment '剧集id',
    `question_id` int(11) unsigned comment '问题id',
    `path` varchar(128) ,
    `lev` int(2) unsigned comment '0:系统 1:节目 2：剧集 3:问题',
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=MyISAM CHARSET=utf8;


CREATE TABLE `banner_title` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `title` varchar(255),
    `question_status` tinyint(2),
    `status` tinyint(1) NOT NULL DEFAULT '1',
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=MyISAM CHARSET=utf8;




WEIBO_FEED_DEALY_MILLISECOND
60000
微博feed的延迟时间

CREATE TABLE IF NOT EXISTS customer (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  tb_id 	  varchar(50),
  tb_username varchar(300),
  tb_nickname varchar(300),
  tb_avatar   varchar(300),
  gender  enum('male','female','privacy'),
  status  int,
  created_at datetime,
  updated_at datetime,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 comment '淘宝用户表';

