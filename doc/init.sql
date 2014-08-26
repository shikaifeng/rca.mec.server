-- Init system configuration 
truncate sys_config;
insert into sys_config(`key`, `value`) values('sys_name', '星库运营系统');
insert into sys_config(`key`, `value`) values('_TV_TAOBAO_HOST', 'http://218.108.129.132:7430');
truncate sys_role;
insert into sys_role(`id`, `name`, `status`, `created_at`) values(1, '超级管理员', 1, now());
truncate sys_user;
insert into sys_user(`id`, `role_id`, `username`, `password`, `email`, `realname`, `status`, `created_at`) values(1, 1, 'admin', md5('admin'), 'admin@zhiping.tv', '智屏', 1, now());