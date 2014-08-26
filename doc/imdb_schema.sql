create database media default charset utf8;

CREATE TABLE media.`imdb_episode` (
  `id` varchar(32) NOT NULL,
  `previous_episode` varchar(32),
  `next_episode` varchar(32),
  `pid` varchar(32) DEFAULT NULL,
  `title` varchar(128) NOT NULL,
  `orig_title` varchar(128) DEFAULT NULL,
  `aka` varchar(255) DEFAULT NULL,
  `cover` varchar(128) DEFAULT NULL,
  `type` varchar(32) NOT NULL DEFAULT 'Movie',
  `website` varchar(255) DEFAULT NULL,
  `year` varchar(16) DEFAULT NULL,
  `season` varchar(16) DEFAULT NULL,
  `start_year` varchar(16) DEFAULT NULL,
  `end_year` varchar(16) DEFAULT NULL,
  `languages` varchar(255) DEFAULT NULL,
  `country` varchar(64) DEFAULT NULL,
  `episodes_count` int(8) unsigned NOT NULL DEFAULT '0',
  `current_episode` int(8) unsigned NOT NULL DEFAULT '0',
  `duration` int(11) unsigned NOT NULL DEFAULT '0',
  `tags` varchar(255) DEFAULT NULL,
  `images` varchar(255),
  `summary` text,
  `photos` text,
  `json_txt` mediumtext,
  `series` varchar(64) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `download_url` varchar(255) DEFAULT NULL,
  `match_state` int NOT NULL DEFAULT '-1',
  `mdm_program_id` int(11) unsigned NOT NULL DEFAULT '0',
  `mdm_episode_id` int(11) unsigned NOT NULL DEFAULT '0',
  `msg` varchar(255) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



CREATE TABLE media.`imdb_plot` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
  `pid` varchar(32) NOT NULL,
  `type` varchar(32),
  `author` varchar(64),
  `text` text NOT NULL,
  `json_txt` text,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


CREATE TABLE `imdb_person` (
  `id` varchar(32) NOT NULL,
  `name` varchar(64) NOT NULL,
  `name_en` varchar(64) DEFAULT NULL,
  `aka` varchar(255) DEFAULT NULL,
  `aka_en` varchar(255) DEFAULT NULL,
  `real_name` varchar(255) DEFAULT NULL,
  `gender` enum('male','female','privacy') NOT NULL DEFAULT 'privacy',
  `avatar` varchar(128) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `birthday` date NOT NULL DEFAULT '0000-00-00',
  `born_place` varchar(64) DEFAULT NULL,
  `constellation` varchar(32) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `description` text,
  `photos` text,
  `r_avatar` varchar(255) DEFAULT NULL,
  `education` varchar(255) DEFAULT NULL,
  `family` varchar(255) DEFAULT NULL,
  `blood_type` varchar(32) DEFAULT NULL,
  `height` varchar(32) DEFAULT NULL,
  `weight` varchar(32) DEFAULT NULL,
  `country` varchar(64) DEFAULT NULL,
  `imdb_url` varchar(255) DEFAULT NULL,
  `json_txt` text,
  `match_state` int NOT NULL DEFAULT '-1',
  `mdm_id` int(11) unsigned NOT NULL DEFAULT '0',
  `msg` varchar(255) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



CREATE TABLE media.`imdb_person_video` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `person_id` varchar(32),
  `video_id` varchar(32),
  `profession` varchar(32),
  `character_name` varchar(64) DEFAULT NULL,
  `character_name_en` varchar(64) DEFAULT NULL,
  `character_avatar` varchar(128) DEFAULT NULL,
  `is_primary` tinyint(1) NOT NULL DEFAULT '0',
  `character_desc` text,
  `lev` enum('program','episode','event') NOT NULL DEFAULT 'event',
  `json_txt` text,
  `match_state` int NOT NULL DEFAULT '-1',
  `mdm_id` int(11) unsigned NOT NULL DEFAULT '0',
  `msg` varchar(255) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


//歌曲
CREATE TABLE media.`imdb_sound_item` (
  `id` varchar(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  `summary` text,
  `artist` varchar(128) DEFAULT NULL,
  `image` varchar(128) DEFAULT NULL,
  `r_image` varchar(255) DEFAULT NULL,
  `products_count` int DEFAULT 0,
  `related_name_count` int DEFAULT 0,
  `amazon_marketplace_id` varchar(32) DEFAULT NULL,
  `json_txt` text,
  `match_state` int NOT NULL DEFAULT '-1',
  `match_id` int(11) unsigned NOT NULL DEFAULT '0',
  `msg` varchar(255) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


CREATE TABLE media.`imdb_sound_item_video` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sound_item_id` varchar(64),
  `video_id` varchar(32),
  `lev` enum('program','episode','event') NOT NULL DEFAULT 'event',
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


//专辑
CREATE TABLE media.`imdb_sound_album` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
  `title` varchar(128) NOT NULL,
  `summary` text,
  `artist` varchar(128) DEFAULT NULL,
  `image` varchar(128) DEFAULT NULL,
  `r_image` varchar(255) DEFAULT NULL,
  `amazon_marketplace_id` varchar(32) DEFAULT NULL,
  `json_txt` text,
  `match_state` int NOT NULL DEFAULT '-1',
  `match_id` int(11) unsigned NOT NULL DEFAULT '0',
  `msg` varchar(255) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


CREATE TABLE media.`imdb_sound_album_video` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sound_albums_id` int(11) unsigned,
  `video_id` varchar(32),
  `lev` enum('program','episode','event') NOT NULL DEFAULT 'event',
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



CREATE TABLE media.`imdb_scene` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `pid` varchar(32) Not NULL,
  `title` varchar(128) DEFAULT NULL,
  `start_time` int(11) unsigned DEFAULT NULL,
  `end_time` int(11) unsigned DEFAULT NULL,
  `duration` int(11) unsigned NOT NULL DEFAULT '0',
  `summary` text,
  `json_txt` text,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



CREATE TABLE media.`imdb_event` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` enum('person','fact','lines','video','music') NOT NULL,
  `fid` varchar(128) DEFAULT NULL,
  `c1`  varchar(255) DEFAULT NULL,
  `program_id` varchar(32) DEFAULT NULL,
  `episode_id` varchar(32) DEFAULT NULL,
  `pid` varchar(32) DEFAULT NULL,
  `scene_id` int(11) unsigned DEFAULT NULL,
  `start_time` int(10) unsigned DEFAULT NULL,
  `end_time` int(10) unsigned DEFAULT NULL,
  `duration` int(10) unsigned DEFAULT NULL,
  `json_txt` text,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE media.`imdb_image` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `rid` varchar(64),
  `fid` varchar(64),
  `type` enum('sound_item_product','sound_album','person','episode') NOT NULL,
  `height` int,
  `width` int,
  `url` varchar(255) DEFAULT NULL,
  `filename` varchar(128) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


CREATE TABLE media.`imdb_product` (
  `id` varchar(32) NOT NULL,
  `fid` varchar(64),
  `type` enum('sound_item','sound_album') NOT NULL,
  `amazon_marketplace_id` varchar(32) DEFAULT NULL,
  `key` varchar(32) DEFAULT NULL,
  `key_type` varchar(32) DEFAULT NULL,
  `region` varchar(32) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


CREATE TABLE media.`imdb_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `lev` enum('info','debug','warn','errror') NOT NULL DEFAULT 'info',
  `type` varchar(32),
  `txt` varchar(255),  
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



CREATE TABLE media.`imdb_person_principalt` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
  `pid` varchar(32) NOT NULL,
  `name` varchar(64),
  `name_id` varchar(32),
  `sort_num` int(8),
  `json_txt` text,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



CREATE TABLE `imdb_merge_sound` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `summary` text,
  `artist` varchar(255) DEFAULT NULL,
  `image` varchar(128) DEFAULT NULL,
  `r_image` varchar(255) DEFAULT NULL,
  `product_key` varchar(32) DEFAULT NULL,
  `match_state` int(1) NOT NULL DEFAULT '0',
  `match_id` int(11) unsigned NOT NULL DEFAULT '0',
  `msg` varchar(255) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

create index idx_imdb_event_queryobj on imdb_event(type,fid,start_time,end_time); 

//mdm的人物节目关系表
CREATE TABLE `imdb_mdm_person_program` select distinct a.id as 'uid',a.name as 'name',a.name_en as 'name_en',c.id 'program_id' from mdm.person a,mdm.person_program b,mdm.program c where a.id=b.person_id and b.program_id=c.id