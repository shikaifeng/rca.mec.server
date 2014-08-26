create database rca_mec_server default charset utf8;

--删除所有表
DROP TABLE IF EXISTS `sys_role`;
DROP TABLE IF EXISTS `sys_user`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `episode_mdm_epg`;
DROP TABLE IF EXISTS `program_mdm_epg`;
DROP TABLE IF EXISTS `user_episode`;
DROP TABLE IF EXISTS `sys_purview`;
DROP TABLE IF EXISTS `sys_session`;
DROP TABLE IF EXISTS `app_config`;
DROP TABLE IF EXISTS `sys_config`;
DROP TABLE IF EXISTS `mec_channel`;
DROP TABLE IF EXISTS `mec_schedule`;
DROP TABLE IF EXISTS `scene`;
DROP TABLE IF EXISTS `element`;
DROP TABLE IF EXISTS `music`;
DROP TABLE IF EXISTS `element_addon`;
DROP TABLE IF EXISTS `baike`;
DROP TABLE IF EXISTS `baike_property`;
DROP TABLE IF EXISTS `down_history`;
DROP TABLE IF EXISTS `mec_epg_program_type`;



-- 系统运营有关
-- 1. sys_user
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

-- 2. sys_role 
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


-- 3. sys_purview
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
CREATE TABLE `sys_session` (
    `session_id` varchar(40) NOT NULL DEFAULT '0', 
    `ip_address` varchar(16) NOT NULL DEFAULT '0', 
    `user_agent` varchar(128) NOT NULL, 
    `last_activity` int(10) unsigned NOT NULL DEFAULT '0', 
    `user_data` text NOT NULL, 
    PRIMARY KEY (`session_id`), 
    KEY `last_activity_idx` (`last_activity`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--app端参数表
CREATE TABLE `app_config` (
   `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
  `title` varchar(64) NOT NULL,
  `value` varchar(255) NOT NULL,
  `description` text,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:valid, 0:invalid',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`)
)

--web系统参数表
CREATE TABLE `sys_config` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
  `title` varchar(64) NOT NULL,
  `value` varchar(255) NOT NULL,
  `description` text,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:valid, 0:invalid',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`)
)

--频道表
CREATE TABLE `mec_channel` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `rid` varchar(64) NOT NULL, 
    `name` varchar(64) NOT NULL, 
    `pinyin` varchar(64), 
    `logo` varchar(255), 
    `description` text, 
    `stream` varchar(255),
    `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:valid, 0:invalid',
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--频道剧集表
CREATE TABLE `mec_schedule` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `rid` varchar(64) NOT NULL,
    `epg_channel_id` varchar(64),
    `epg_program_id` varchar(64),
    `epg_episode_id` varchar(64),
    `epg_start_at` bigint, 
    `epg_end_at` bigint,
    `epg_name` varchar(128), 
    `mec_channel_id` int(11) unsigned,
    `mdm_program_id` int (11) unsigned,
    `mdm_episode_id` int (11) unsigned,
    `mec_start_at` bigint,
    `mec_end_at` bigint,
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--场景表
CREATE TABLE `scene` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `pid` int(11) unsigned NOT NULL,
    `title` varchar(128),
    `cover` varchar(128),
    `images` varchar(128),
    `start_time` int(10) unsigned, 
    `end_time` int(10) unsigned,
    `duration` int(10) unsigned,
    `summary` text,
    `tags` varchar(255),
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--元素表
CREATE TABLE `element` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `type` enum('person','mec_person','baike','lines','video','music','fact') NOT NULL,
    `fid` int(11) unsigned NOT NULL default '0',
    `title` varchar(128),
    `cover` varchar(255),
    `program_id` int(11) unsigned,
    `episode_id` int(11) unsigned,
    `scene_id` int(11) unsigned,
    `start_time` int(10) unsigned, 
    `end_time` int(10) unsigned,
    `duration` int(10) unsigned,
    `tag` varchar(255),
    `url` varchar(255),
    `content` text,
    
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--元素扩展表
CREATE TABLE `element_addon` (
    `id` int(11) unsigned NOT NULL,
    `c1` varchar(255),
    `c2` varchar(255),
    `c3` varchar(255),
    `c4` varchar(255),
    `t1` text,
    `t2` text,
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


--音乐表
CREATE TABLE `music` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `title` varchar(128),
    `org_title` varchar(128),
    `cover` varchar(255),
    `lyricist` varchar(128),
    `composer` varchar(128),
    `singer`   varchar(128),
    `lyric`   varchar(512),
    `summary` text,
    `file_type` enum('mp3','wma','mp4'),
    `source_url` varchar(255),
    `path` varchar(128),
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--百科
CREATE TABLE `baike` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `program_id` int(11) unsigned DEFAULT NULL 
    `title` varchar(128),
    `cover` varchar(128),
    `summary` text,
    `source_url` varchar(255),
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--百科属性表
CREATE TABLE `baike_property` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `pid` int(11) unsigned,
    `title` varchar(128),
    `value` varchar(128),
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



--设备表
CREATE TABLE `user` (
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


--设备剧集记录表
CREATE TABLE `user_episode` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `ucid` int(11) unsigned NOT NULL, 
    `program_id` int(11) unsigned,    
    `episode_id` int(11) unsigned,    
    `start_time` int(11) unsigned,
    `end_time`   int(11) unsigned,
    `last_at`  datetime,
    'type'	enum('program','episode')
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--和epg有关的接口
--节目扩展表
CREATE TABLE `program_mdm_epg` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `mdm_id` int(11) unsigned, 
    `epg_id` int(11) unsigned,
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--剧集扩展表
CREATE TABLE `episode_mdm_epg` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `mdm_program_id` int(11) unsigned, 
    `epg_program_id` int(11) unsigned,
    `mdm_id` int(11) unsigned, 
    `epg_id` int(11) unsigned,
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--下载记录
CREATE TABLE `down_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
  `url` varchar(255) NOT NULL DEFAULT '',
  `filename` varchar(64) NOT NULL DEFAULT '',
  `type` varchar(64) NOT NULL DEFAULT '',
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;

--epg的节目类型
CREATE TABLE `mec_epg_program_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `rid` varchar(64) NOT NULL DEFAULT '0',
  `name` varchar(64) NOT NULL,
  `status` tinyint(1) unsigned DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `weibo_feed` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `program_id` int(11) unsigned, 
    `episode_id` int(11) unsigned,
    `series` varchar(64),
    `start_time` int(10) unsigned,
    `start_time_str` varchar(20), 
    `content` text,
    images varchar(255),
    videos varchar(255),
    `status` tinyint(1) NOT NULL DEFAULT '1', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


//百科信息
CREATE TABLE `imdb_fact` (
  `id` varchar(64),
  `type` varchar(32),
  `pid` varchar(32),
  `title` varchar(128) DEFAULT NULL,
  `text` text,
  `json_txt` text,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


CREATE TABLE `imdb_fact_title` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title_id` varchar(32),
  `fact_id` varchar(64),
  `json_txt` text,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


CREATE TABLE `imdb_fact_name` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name_id` varchar(32),
  `fact_id` varchar(64),
  `json_txt` text,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

create table question(
	`id` int(11) unsigned primary key AUTO_INCREMENT,
	`title` varchar(255) comment '名称',
	`program_id` int(11) unsigned,
    `episode_id` int(11) unsigned,
	`answer_id` int(11) unsigned comment '正确选项id',
	`start_time` int(10) unsigned, 
    `end_time` int(10) unsigned,
    `deadline` int(10) unsigned, 
    `public_time` int(10) unsigned,
    `summary` text,
	`status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:valid, 0:invalid', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='互动问答';

create table question_option(
	`id` int(11) unsigned primary key AUTO_INCREMENT,
	`question_id` int(11) unsigned comment '问题id',
	`title` varchar(255) comment '名称',
	`status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:valid, 0:invalid', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '问题选项';

CREATE TABLE `mec_person` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `program_id` int(11) unsigned,
  `name` varchar(64) NOT NULL,
  `name_en` varchar(64) DEFAULT NULL,
  `aka` varchar(255) DEFAULT NULL,
  `aka_en` varchar(255) DEFAULT NULL,
  `gender` enum('male','female','privacy') NOT NULL DEFAULT 'privacy',
  `avatar` varchar(128) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `birthday` date NOT NULL DEFAULT '0000-00-00',
  `born_place` varchar(255) DEFAULT NULL,
  `constellation` varchar(32) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `description` text,
  `photos` text,
  `mtime_url` varchar(255) DEFAULT NULL,
  `douban_url` varchar(255) DEFAULT NULL,
  `imdb_url` varchar(255) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `avatar_mtime` varchar(255) DEFAULT NULL,
  `education` varchar(255) DEFAULT NULL,
  `family` varchar(255) DEFAULT NULL,
  `blood_type` varchar(32) DEFAULT NULL,
  `height` varchar(32) DEFAULT NULL,
  `weight` varchar(32) DEFAULT NULL,
  `baike_url` varchar(255) DEFAULT NULL,
  `baidu_url` varchar(255) DEFAULT NULL,
  `soku_url` varchar(255) DEFAULT NULL,
  `tudou_url` varchar(255) DEFAULT NULL,
  `country` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--问答记录表
CREATE TABLE `user_question` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
    `udid` varchar(64) NOT NULL, 
    `question_id` int(11) unsigned,    
    `option_id` int(11) unsigned,
    `client_time` varchar(32),
    `status` tinyint(2) NOT NULL DEFAULT '1' comment '1: 记录成功，2：打对了，3：打错了', 
    `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', 
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=MyISAM CHARSET=utf8;




CREATE TABLE `mec_weibo_feed` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `mdm_id` int(11) unsigned DEFAULT NULL,
  `program_id` int(11) unsigned DEFAULT NULL,
  `episode_id` int(11) unsigned DEFAULT NULL,
  `series` varchar(64) DEFAULT NULL,
  `minute` int(5) unsigned DEFAULT NULL,
  `show_start_time` int(10) unsigned DEFAULT NULL,
  `start_time` int(10) unsigned DEFAULT NULL,
  `start_time_str` varchar(20) DEFAULT NULL,
  `content` text,
  `images` text,
  `videos` text,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sender_name` varchar(64) DEFAULT NULL,
  `sender_avatar` varchar(255) DEFAULT NULL,
  `sender_url` varchar(255) DEFAULT NULL,
  `type` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



CREATE TABLE `syn_weibo_task` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `program_id` int(11) unsigned DEFAULT NULL,
  `episode_id` int(11) unsigned DEFAULT NULL,
  `max_id` int(11) unsigned DEFAULT NULL,
  `last_updated_at` datetime,
  `times` int(11) unsigned
  `thread_state` int(2) NOT NULL DEFAULT '-1',
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;