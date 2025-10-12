INSERT INTO public.gen_field_type (id,column_type,attr_type,package_name,create_time,create_by,update_time,update_by,del_flag) VALUES
	 (1,'datetime','LocalDateTime','java.time.LocalDateTime','2023-02-06 08:45:10',NULL,NULL,NULL,'0'),
	 (2,'date','LocalDate','java.time.LocalDate','2023-02-06 08:45:10',NULL,NULL,NULL,'0'),
	 (3,'tinyint','Integer',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (4,'smallint','Integer',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (5,'mediumint','Integer',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (6,'int','Integer',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (7,'integer','Integer',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (8,'bigint','Long',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (9,'float','Float',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (10,'double','Double',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0');
INSERT INTO public.gen_field_type (id,column_type,attr_type,package_name,create_time,create_by,update_time,update_by,del_flag) VALUES
	 (11,'decimal','BigDecimal','java.math.BigDecimal','2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (12,'bit','Boolean',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (13,'char','String',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (14,'varchar','String',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (15,'tinytext','String',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (16,'text','String',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (17,'mediumtext','String',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (18,'longtext','String',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (19,'timestamp','LocalDateTime','java.time.LocalDateTime','2023-02-06 08:45:11',NULL,NULL,NULL,'0'),
	 (20,'NUMBER','Integer',NULL,'2023-02-06 08:45:11',NULL,NULL,NULL,'0');
INSERT INTO public.gen_field_type (id,column_type,attr_type,package_name,create_time,create_by,update_time,update_by,del_flag) VALUES
	 (21,'BINARY_INTEGER','Integer',NULL,'2023-02-06 08:45:12',NULL,NULL,NULL,'0'),
	 (22,'BINARY_FLOAT','Float',NULL,'2023-02-06 08:45:12',NULL,NULL,NULL,'0'),
	 (23,'BINARY_DOUBLE','Double',NULL,'2023-02-06 08:45:12',NULL,NULL,NULL,'0'),
	 (24,'VARCHAR2','String',NULL,'2023-02-06 08:45:12',NULL,NULL,NULL,'0'),
	 (25,'NVARCHAR','String',NULL,'2023-02-06 08:45:12',NULL,NULL,NULL,'0'),
	 (26,'NVARCHAR2','String',NULL,'2023-02-06 08:45:12',NULL,NULL,NULL,'0'),
	 (27,'CLOB','String',NULL,'2023-02-06 08:45:12',NULL,NULL,NULL,'0'),
	 (28,'int8','Long',NULL,'2023-02-06 08:45:12',NULL,NULL,NULL,'0'),
	 (29,'int4','Integer',NULL,'2023-02-06 08:45:12',NULL,NULL,NULL,'0'),
	 (30,'int2','Integer',NULL,'2023-02-06 08:45:12',NULL,NULL,NULL,'0');
INSERT INTO public.gen_field_type (id,column_type,attr_type,package_name,create_time,create_by,update_time,update_by,del_flag) VALUES
	 (31,'numeric','BigDecimal','java.math.BigDecimal','2023-02-06 08:45:12',NULL,NULL,NULL,'0'),
	 (32,'json','String',NULL,'2023-02-06 08:45:12',NULL,NULL,NULL,'0');
INSERT INTO public.sys_dept (dept_id,"name",sort_order,create_by,update_by,create_time,update_time,del_flag,parent_id) VALUES
	 (1,'总裁办',1,'admin','admin','2023-04-03 13:04:47','2023-04-03 13:07:49','0',0),
	 (2,'技术部',2,'admin','admin','2023-04-03 13:04:47','2023-04-03 13:04:47','0',1),
	 (3,'市场部',3,'admin','admin','2023-04-03 13:04:47','2023-04-03 13:04:47','0',1),
	 (4,'销售部',4,'admin','admin','2023-04-03 13:04:47','2023-04-03 13:04:47','0',1),
	 (5,'财务部',5,'admin','admin','2023-04-03 13:04:47','2023-04-03 13:04:47','0',1),
	 (6,'人事行政部',6,'admin','admin','2023-04-03 13:04:47','2023-04-03 13:53:36','1',1),
	 (7,'研发部',7,'admin','admin','2023-04-03 13:04:47','2023-04-03 13:04:47','0',2),
	 (8,'UI设计部',11,'admin','admin','2023-04-03 13:04:47','2023-04-03 13:04:47','0',7),
	 (9,'产品部',12,'admin','admin','2023-04-03 13:04:47','2023-04-03 13:04:47','0',2),
	 (10,'渠道部',13,'admin','admin','2023-04-03 13:04:47','2023-04-03 13:04:47','0',3);
INSERT INTO public.sys_dept (dept_id,"name",sort_order,create_by,update_by,create_time,update_time,del_flag,parent_id) VALUES
	 (11,'推广部',14,'admin','admin','2023-04-03 13:04:47','2023-04-03 13:04:47','0',3),
	 (12,'客服部',15,'admin','admin','2023-04-03 13:04:47','2023-04-03 13:04:47','0',4),
	 (13,'财务会计部',16,'admin','admin','2023-04-03 13:04:47','2023-04-03 13:04:47','0',5),
	 (14,'审计风控部',17,'admin','admin','2023-04-03 13:04:47','2023-04-03 14:06:57','0',5);
INSERT INTO public.sys_dict (id,dict_type,description,create_by,update_by,create_time,update_time,remarks,system_flag,del_flag) VALUES
	 (1,'log_type','日志类型',' ',' ','2019-03-19 11:06:44','2019-03-19 11:06:44','异常、正常','1','0'),
	 (2,'social_type','社交登录',' ',' ','2019-03-19 11:09:44','2019-03-19 11:09:44','微信、QQ','1','0'),
	 (3,'job_type','定时任务类型',' ',' ','2019-03-19 11:22:21','2019-03-19 11:22:21','quartz','1','0'),
	 (4,'job_status','定时任务状态',' ',' ','2019-03-19 11:24:57','2019-03-19 11:24:57','发布状态、运行状态','1','0'),
	 (5,'job_execute_status','定时任务执行状态',' ',' ','2019-03-19 11:26:15','2019-03-19 11:26:15','正常、异常','1','0'),
	 (6,'misfire_policy','定时任务错失执行策略',' ',' ','2019-03-19 11:27:19','2019-03-19 11:27:19','周期','1','0'),
	 (7,'gender','性别',' ',' ','2019-03-27 13:44:06','2019-03-27 13:44:06','微信用户性别','1','0'),
	 (8,'subscribe','订阅状态',' ',' ','2019-03-27 13:48:33','2019-03-27 13:48:33','公众号订阅状态','1','0'),
	 (9,'response_type','回复',' ',' ','2019-03-28 21:29:21','2019-03-28 21:29:21','微信消息是否已回复','1','0'),
	 (10,'param_type','参数配置',' ',' ','2019-04-29 18:20:47','2019-04-29 18:20:47','检索、原文、报表、安全、文档、消息、其他','1','0');
INSERT INTO public.sys_dict (id,dict_type,description,create_by,update_by,create_time,update_time,remarks,system_flag,del_flag) VALUES
	 (11,'status_type','租户状态',' ',' ','2019-05-15 16:31:08','2019-05-15 16:31:08','租户状态','1','0'),
	 (12,'dict_type','字典类型',' ',' ','2019-05-16 14:16:20','2019-05-16 14:20:16','系统类不能修改','1','0'),
	 (13,'channel_type','支付类型',' ',' ','2019-05-16 14:16:20','2019-05-16 14:20:16','系统类不能修改','1','0'),
	 (14,'grant_types','授权类型',' ',' ','2019-08-13 07:34:10','2019-08-13 07:34:10',NULL,'1','0'),
	 (15,'style_type','前端风格',' ',' ','2020-02-07 03:49:28','2020-02-07 03:50:40','0-Avue 1-element','1','0'),
	 (16,'captcha_flag_types','验证码开关',' ',' ','2020-11-18 06:53:25','2020-11-18 06:53:25','是否校验验证码','1','0'),
	 (17,'enc_flag_types','前端密码加密',' ',' ','2020-11-18 06:54:44','2020-11-18 06:54:44','前端密码是否加密传输','1','0'),
	 (18,'lock_flag','用户状态','admin',' ','2023-02-01 16:55:31',NULL,NULL,'1','0'),
	 (19,'ds_config_type','数据连接类型','admin',' ','2023-02-06 18:36:59',NULL,NULL,'1','0'),
	 (20,'common_status','通用状态','admin',' ','2023-02-09 11:02:08',NULL,NULL,'1','0');
INSERT INTO public.sys_dict (id,dict_type,description,create_by,update_by,create_time,update_time,remarks,system_flag,del_flag) VALUES
	 (21,'app_social_type','app社交登录','admin',' ','2023-02-10 11:11:06',NULL,'app社交登录','1','0'),
	 (22,'yes_no_type','是否','admin',' ','2023-02-20 23:25:04',NULL,NULL,'1','0'),
	 (23,'repType','微信消息类型','admin',' ','2023-02-24 15:08:25',NULL,NULL,'0','0'),
	 (24,'leave_status','请假状态','admin',' ','2023-03-02 22:50:15',NULL,NULL,'0','0'),
	 (25,'schedule_type','日程类型','admin',' ','2023-03-06 14:49:18',NULL,NULL,'0','0'),
	 (26,'schedule_status','日程状态','admin',' ','2023-03-06 14:52:57',NULL,NULL,'0','0'),
	 (27,'ds_type','代码生成器支持的数据库类型','admin',' ','2023-03-12 09:57:59',NULL,NULL,'1','0');
INSERT INTO public.sys_dict_item (id,dict_id,item_value,"label",dict_type,description,sort_order,create_by,update_by,create_time,update_time,remarks,del_flag) VALUES
	 (1,1,'9','异常','log_type','日志异常',1,' ',' ','2019-03-19 11:08:59','2019-03-25 12:49:13','','0'),
	 (2,1,'0','正常','log_type','日志正常',0,' ',' ','2019-03-19 11:09:17','2019-03-25 12:49:18','','0'),
	 (3,2,'WX','微信','social_type','微信登录',0,' ',' ','2019-03-19 11:10:02','2019-03-25 12:49:36','','0'),
	 (4,2,'QQ','QQ','social_type','QQ登录',1,' ',' ','2019-03-19 11:10:14','2019-03-25 12:49:36','','0'),
	 (5,3,'1','java类','job_type','java类',1,' ',' ','2019-03-19 11:22:37','2019-03-25 12:49:36','','0'),
	 (6,3,'2','spring bean','job_type','spring bean容器实例',2,' ',' ','2019-03-19 11:23:05','2019-03-25 12:49:36','','0'),
	 (7,3,'9','其他','job_type','其他类型',9,' ',' ','2019-03-19 11:23:31','2019-03-25 12:49:36','','0'),
	 (8,3,'3','Rest 调用','job_type','Rest 调用',3,' ',' ','2019-03-19 11:23:57','2019-03-25 12:49:36','','0'),
	 (9,3,'4','jar','job_type','jar类型',4,' ',' ','2019-03-19 11:24:20','2019-03-25 12:49:36','','0'),
	 (10,4,'1','未发布','job_status','未发布',1,' ',' ','2019-03-19 11:25:18','2019-03-25 12:49:36','','0');
INSERT INTO public.sys_dict_item (id,dict_id,item_value,"label",dict_type,description,sort_order,create_by,update_by,create_time,update_time,remarks,del_flag) VALUES
	 (11,4,'2','运行中','job_status','运行中',2,' ',' ','2019-03-19 11:25:31','2019-03-25 12:49:36','','0'),
	 (12,4,'3','暂停','job_status','暂停',3,' ',' ','2019-03-19 11:25:42','2019-03-25 12:49:36','','0'),
	 (13,5,'0','正常','job_execute_status','正常',0,' ',' ','2019-03-19 11:26:27','2019-03-25 12:49:36','','0'),
	 (14,5,'1','异常','job_execute_status','异常',1,' ',' ','2019-03-19 11:26:41','2019-03-25 12:49:36','','0'),
	 (15,6,'1','错失周期立即执行','misfire_policy','错失周期立即执行',1,' ',' ','2019-03-19 11:27:45','2019-03-25 12:49:36','','0'),
	 (16,6,'2','错失周期执行一次','misfire_policy','错失周期执行一次',2,' ',' ','2019-03-19 11:27:57','2019-03-25 12:49:36','','0'),
	 (17,6,'3','下周期执行','misfire_policy','下周期执行',3,' ',' ','2019-03-19 11:28:08','2019-03-25 12:49:36','','0'),
	 (18,7,'1','男','gender','微信-男',0,' ',' ','2019-03-27 13:45:13','2019-03-27 13:45:13','微信-男','0'),
	 (19,7,'2','女','gender','女-微信',1,' ',' ','2019-03-27 13:45:34','2019-03-27 13:45:34','女-微信','0'),
	 (20,7,'0','未知','gender','x性别未知',3,' ',' ','2019-03-27 13:45:57','2019-03-27 13:45:57','x性别未知','0');
INSERT INTO public.sys_dict_item (id,dict_id,item_value,"label",dict_type,description,sort_order,create_by,update_by,create_time,update_time,remarks,del_flag) VALUES
	 (21,8,'0','未关注','subscribe','公众号-未关注',0,' ',' ','2019-03-27 13:49:07','2019-03-27 13:49:07','公众号-未关注','0'),
	 (22,8,'1','已关注','subscribe','公众号-已关注',1,' ',' ','2019-03-27 13:49:26','2019-03-27 13:49:26','公众号-已关注','0'),
	 (23,9,'0','未回复','response_type','微信消息-未回复',0,' ',' ','2019-03-28 21:29:47','2019-03-28 21:29:47','微信消息-未回复','0'),
	 (24,9,'1','已回复','response_type','微信消息-已回复',1,' ',' ','2019-03-28 21:30:08','2019-03-28 21:30:08','微信消息-已回复','0'),
	 (25,10,'1','检索','param_type','检索',0,' ',' ','2019-04-29 18:22:17','2019-04-29 18:22:17','检索','0'),
	 (26,10,'2','原文','param_type','原文',0,' ',' ','2019-04-29 18:22:27','2019-04-29 18:22:27','原文','0'),
	 (27,10,'3','报表','param_type','报表',0,' ',' ','2019-04-29 18:22:36','2019-04-29 18:22:36','报表','0'),
	 (28,10,'4','安全','param_type','安全',0,' ',' ','2019-04-29 18:22:46','2019-04-29 18:22:46','安全','0'),
	 (29,10,'5','文档','param_type','文档',0,' ',' ','2019-04-29 18:22:56','2019-04-29 18:22:56','文档','0'),
	 (30,10,'6','消息','param_type','消息',0,' ',' ','2019-04-29 18:23:05','2019-04-29 18:23:05','消息','0');
INSERT INTO public.sys_dict_item (id,dict_id,item_value,"label",dict_type,description,sort_order,create_by,update_by,create_time,update_time,remarks,del_flag) VALUES
	 (31,10,'9','其他','param_type','其他',0,' ',' ','2019-04-29 18:23:16','2019-04-29 18:23:16','其他','0'),
	 (32,10,'0','默认','param_type','默认',0,' ',' ','2019-04-29 18:23:30','2019-04-29 18:23:30','默认','0'),
	 (33,11,'0','正常','status_type','状态正常',0,' ',' ','2019-05-15 16:31:34','2019-05-16 22:30:46','状态正常','0'),
	 (34,11,'9','冻结','status_type','状态冻结',1,' ',' ','2019-05-15 16:31:56','2019-05-16 22:30:50','状态冻结','0'),
	 (35,12,'1','系统类','dict_type','系统类字典',0,' ',' ','2019-05-16 14:20:40','2019-05-16 14:20:40','不能修改删除','0'),
	 (36,12,'0','业务类','dict_type','业务类字典',0,' ',' ','2019-05-16 14:20:59','2019-05-16 14:20:59','可以修改','0'),
	 (37,2,'GITEE','码云','social_type','码云',2,' ',' ','2019-06-28 09:59:12','2019-06-28 09:59:12','码云','0'),
	 (38,2,'OSC','开源中国','social_type','开源中国登录',2,' ',' ','2019-06-28 10:04:32','2019-06-28 10:04:32','','0'),
	 (39,14,'password','密码模式','grant_types','支持oauth密码模式',0,' ',' ','2019-08-13 07:35:28','2019-08-13 07:35:28',NULL,'0'),
	 (40,14,'authorization_code','授权码模式','grant_types','oauth2 授权码模式',1,' ',' ','2019-08-13 07:36:07','2019-08-13 07:36:07',NULL,'0');
INSERT INTO public.sys_dict_item (id,dict_id,item_value,"label",dict_type,description,sort_order,create_by,update_by,create_time,update_time,remarks,del_flag) VALUES
	 (41,14,'client_credentials','客户端模式','grant_types','oauth2 客户端模式',2,' ',' ','2019-08-13 07:36:30','2019-08-13 07:36:30',NULL,'0'),
	 (42,14,'refresh_token','刷新模式','grant_types','oauth2 刷新token',3,' ',' ','2019-08-13 07:36:54','2019-08-13 07:36:54',NULL,'0'),
	 (43,14,'implicit','简化模式','grant_types','oauth2 简化模式',4,' ',' ','2019-08-13 07:39:32','2019-08-13 07:39:32',NULL,'0'),
	 (44,15,'0','Avue','style_type','Avue风格',0,' ',' ','2020-02-07 03:52:52','2020-02-07 03:52:52','','0'),
	 (45,15,'1','element','style_type','element-ui',1,' ',' ','2020-02-07 03:53:12','2020-02-07 03:53:12','','0'),
	 (46,16,'0','关','captcha_flag_types','不校验验证码',0,' ',' ','2020-11-18 06:53:58','2020-11-18 06:53:58','不校验验证码 -0','0'),
	 (47,16,'1','开','captcha_flag_types','校验验证码',1,' ',' ','2020-11-18 06:54:15','2020-11-18 06:54:15','不校验验证码-1','0'),
	 (48,17,'0','否','enc_flag_types','不加密',0,' ',' ','2020-11-18 06:55:31','2020-11-18 06:55:31','不加密-0','0'),
	 (49,17,'1','是','enc_flag_types','加密',1,' ',' ','2020-11-18 06:55:51','2020-11-18 06:55:51','加密-1','0'),
	 (50,13,'MERGE_PAY','聚合支付','channel_type','聚合支付',1,' ',' ','2019-05-30 19:08:08','2019-06-18 13:51:53','聚合支付','0');
INSERT INTO public.sys_dict_item (id,dict_id,item_value,"label",dict_type,description,sort_order,create_by,update_by,create_time,update_time,remarks,del_flag) VALUES
	 (51,2,'CAS','CAS登录','social_type','CAS 单点登录系统',3,' ',' ','2022-02-18 13:56:25','2022-02-18 13:56:28',NULL,'0'),
	 (52,2,'DINGTALK','钉钉','social_type','钉钉',3,' ',' ','2022-02-18 13:56:25','2022-02-18 13:56:28',NULL,'0'),
	 (53,2,'WEIXIN_CP','企业微信','social_type','企业微信',3,' ',' ','2022-02-18 13:56:25','2022-02-18 13:56:28',NULL,'0'),
	 (54,15,'2','APP','style_type','uview风格',1,' ',' ','2020-02-07 03:53:12','2020-02-07 03:53:12','','0'),
	 (55,13,'ALIPAY_WAP','支付宝支付','channel_type','支付宝支付',1,' ',' ','2019-05-30 19:08:08','2019-06-18 13:51:53','聚合支付','0'),
	 (56,13,'WEIXIN_MP','微信支付','channel_type','微信支付',1,' ',' ','2019-05-30 19:08:08','2019-06-18 13:51:53','聚合支付','0'),
	 (57,14,'mobile','mobile','grant_types','移动端登录',5,'admin',' ','2023-01-29 17:21:42',NULL,NULL,'0'),
	 (58,18,'0','有效','lock_flag','有效',0,'admin',' ','2023-02-01 16:56:00',NULL,NULL,'0'),
	 (59,18,'9','禁用','lock_flag','禁用',1,'admin',' ','2023-02-01 16:56:09',NULL,NULL,'0'),
	 (60,15,'4','vue3','style_type','element-plus',4,'admin',' ','2023-02-06 13:52:43',NULL,NULL,'0');
INSERT INTO public.sys_dict_item (id,dict_id,item_value,"label",dict_type,description,sort_order,create_by,update_by,create_time,update_time,remarks,del_flag) VALUES
	 (61,19,'0','主机','ds_config_type','主机',0,'admin',' ','2023-02-06 18:37:23',NULL,NULL,'0'),
	 (62,19,'1','JDBC','ds_config_type','jdbc',2,'admin',' ','2023-02-06 18:37:34',NULL,NULL,'0'),
	 (63,20,'false','否','common_status','否',1,'admin',' ','2023-02-09 11:02:39',NULL,NULL,'0'),
	 (64,20,'true','是','common_status','是',2,'admin',' ','2023-02-09 11:02:52',NULL,NULL,'0'),
	 (65,21,'MINI','小程序','app_social_type','小程序登录',0,'admin',' ','2023-02-10 11:11:41',NULL,NULL,'0'),
	 (66,22,'0','否','yes_no_type','0',0,'admin',' ','2023-02-20 23:35:23',NULL,'0','0'),
	 (67,22,'1','是','yes_no_type','1',0,'admin',' ','2023-02-20 23:35:37',NULL,'1','0'),
	 (69,23,'text','文本','repType','文本',0,'admin',' ','2023-02-24 15:08:45',NULL,NULL,'0'),
	 (70,23,'image','图片','repType','图片',0,'admin',' ','2023-02-24 15:08:56',NULL,NULL,'0'),
	 (71,23,'voice','语音','repType','语音',0,'admin',' ','2023-02-24 15:09:08',NULL,NULL,'0');
INSERT INTO public.sys_dict_item (id,dict_id,item_value,"label",dict_type,description,sort_order,create_by,update_by,create_time,update_time,remarks,del_flag) VALUES
	 (72,23,'video','视频','repType','视频',0,'admin',' ','2023-02-24 15:09:18',NULL,NULL,'0'),
	 (73,23,'shortvideo','小视频','repType','小视频',0,'admin',' ','2023-02-24 15:09:29',NULL,NULL,'0'),
	 (74,23,'location','地理位置','repType','地理位置',0,'admin',' ','2023-02-24 15:09:41',NULL,NULL,'0'),
	 (75,23,'link','链接消息','repType','链接消息',0,'admin',' ','2023-02-24 15:09:49',NULL,NULL,'0'),
	 (76,23,'event','事件推送','repType','事件推送',0,'admin',' ','2023-02-24 15:09:57',NULL,NULL,'0'),
	 (77,24,'0','未提交','leave_status','未提交',0,'admin',' ','2023-03-02 22:50:45',NULL,'未提交','0'),
	 (78,24,'1','审批中','leave_status','审批中',0,'admin',' ','2023-03-02 22:50:57',NULL,'审批中','0'),
	 (79,24,'2','完成','leave_status','完成',0,'admin',' ','2023-03-02 22:51:06',NULL,'完成','0'),
	 (80,24,'9','驳回','leave_status','驳回',0,'admin',' ','2023-03-02 22:51:20',NULL,NULL,'0'),
	 (81,25,'record','日程记录','schedule_type','日程记录',0,'admin',' ','2023-03-06 14:50:01',NULL,NULL,'0');
INSERT INTO public.sys_dict_item (id,dict_id,item_value,"label",dict_type,description,sort_order,create_by,update_by,create_time,update_time,remarks,del_flag) VALUES
	 (82,25,'plan','计划','schedule_type','计划类型',0,'admin',' ','2023-03-06 14:50:29',NULL,NULL,'0'),
	 (83,26,'0','计划中','schedule_status','日程状态',0,'admin',' ','2023-03-06 14:53:18',NULL,NULL,'0'),
	 (84,26,'1','已开始','schedule_status','已开始',0,'admin',' ','2023-03-06 14:53:33',NULL,NULL,'0'),
	 (85,26,'3','已结束','schedule_status','已结束',0,'admin',' ','2023-03-06 14:53:41',NULL,NULL,'0'),
	 (86,27,'mysql','mysql','ds_type','mysql',0,'admin',' ','2023-03-12 09:58:11',NULL,NULL,'0');
INSERT INTO public.sys_log (id,log_type,title,service_id,create_by,update_by,create_time,update_time,remote_addr,user_agent,request_uri,"method",params,"time",del_flag,"exception") VALUES
	 (1965269872275484673,'0','登录成功','pig-auth','admin',NULL,'2025-09-09 12:23:45.157677',NULL,'192.168.232.1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36','/oauth2/token','POST','randomStr=%5B170fe4c4-b856-41f8-b82e-dac0a91eded2%5D&code=%5B8%5D&grant_type=%5Bpassword%5D&scope=%5Bserver%5D&username=%5Badmin%5D',1311,'0',NULL);
INSERT INTO public.sys_menu (menu_id,"name",en_name,"permission","path",parent_id,icon,visible,sort_order,keep_alive,embedded,menu_type,create_by,create_time,update_by,update_time,del_flag) VALUES
	 (1000,'权限管理','authorization',NULL,'/admin',-1,'iconfont icon-icon-','1',0,'0','0','0','','2018-09-28 08:29:53','admin','2023-03-12 22:32:52','0'),
	 (1100,'用户管理','user',NULL,'/admin/user/index',1000,'ele-User','1',1,'0','0','0','','2017-11-02 22:24:37','admin','2023-07-05 10:28:22','0'),
	 (1101,'用户新增',NULL,'sys_user_add',NULL,1100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:52:09',' ','2021-05-25 03:12:55','0'),
	 (1102,'用户修改',NULL,'sys_user_edit',NULL,1100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:52:48',' ','2021-05-25 03:12:55','0'),
	 (1103,'用户删除',NULL,'sys_user_del',NULL,1100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:54:01',' ','2021-05-25 03:12:55','0'),
	 (1104,'导入导出',NULL,'sys_user_export',NULL,1100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:54:01',' ','2021-05-25 03:12:55','0'),
	 (1200,'菜单管理','menu',NULL,'/admin/menu/index',1000,'iconfont icon-caidan','1',2,'0','0','0','','2017-11-08 09:57:27','admin','2023-07-05 10:28:17','0'),
	 (1201,'菜单新增',NULL,'sys_menu_add',NULL,1200,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:15:53',' ','2021-05-25 03:12:55','0'),
	 (1202,'菜单修改',NULL,'sys_menu_edit',NULL,1200,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:16:23',' ','2021-05-25 03:12:55','0'),
	 (1203,'菜单删除',NULL,'sys_menu_del',NULL,1200,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:16:43',' ','2021-05-25 03:12:55','0');
INSERT INTO public.sys_menu (menu_id,"name",en_name,"permission","path",parent_id,icon,visible,sort_order,keep_alive,embedded,menu_type,create_by,create_time,update_by,update_time,del_flag) VALUES
	 (1300,'角色管理','role',NULL,'/admin/role/index',1000,'iconfont icon-gerenzhongxin','1',3,'0',NULL,'0','','2017-11-08 10:13:37','admin','2023-07-05 10:28:13','0'),
	 (1301,'角色新增',NULL,'sys_role_add',NULL,1300,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:14:18',' ','2021-05-25 03:12:55','0'),
	 (1302,'角色修改',NULL,'sys_role_edit',NULL,1300,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:14:41',' ','2021-05-25 03:12:55','0'),
	 (1303,'角色删除',NULL,'sys_role_del',NULL,1300,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:14:59',' ','2021-05-25 03:12:55','0'),
	 (1304,'分配权限',NULL,'sys_role_perm',NULL,1300,NULL,'1',1,'0',NULL,'1',' ','2018-04-20 07:22:55',' ','2021-05-25 03:12:55','0'),
	 (1305,'角色导入导出',NULL,'sys_role_export',NULL,1300,NULL,'1',4,'0',NULL,'1',' ','2022-03-26 15:54:34',' ',NULL,'0'),
	 (1400,'部门管理','dept',NULL,'/admin/dept/index',1000,'iconfont icon-zidingyibuju','1',4,'0',NULL,'0','','2018-01-20 13:17:19','admin','2023-07-05 10:28:07','0'),
	 (1401,'部门新增',NULL,'sys_dept_add',NULL,1400,NULL,'1',1,'0',NULL,'1',' ','2018-01-20 14:56:16',' ','2021-05-25 03:12:55','0'),
	 (1402,'部门修改',NULL,'sys_dept_edit',NULL,1400,NULL,'1',1,'0',NULL,'1',' ','2018-01-20 14:56:59',' ','2021-05-25 03:12:55','0'),
	 (1403,'部门删除',NULL,'sys_dept_del',NULL,1400,NULL,'1',1,'0',NULL,'1',' ','2018-01-20 14:57:28',' ','2021-05-25 03:12:55','0');
INSERT INTO public.sys_menu (menu_id,"name",en_name,"permission","path",parent_id,icon,visible,sort_order,keep_alive,embedded,menu_type,create_by,create_time,update_by,update_time,del_flag) VALUES
	 (1600,'岗位管理','post',NULL,'/admin/post/index',1000,'iconfont icon--chaifenhang','1',5,'1','0','0','','2022-03-26 13:04:14','admin','2023-07-05 10:28:03','0'),
	 (1601,'岗位信息查看',NULL,'sys_post_view',NULL,1600,NULL,'1',0,'0',NULL,'1',' ','2022-03-26 13:05:34',' ',NULL,'0'),
	 (1602,'岗位信息新增',NULL,'sys_post_add',NULL,1600,NULL,'1',1,'0',NULL,'1',' ','2022-03-26 13:06:00',' ',NULL,'0'),
	 (1603,'岗位信息修改',NULL,'sys_post_edit',NULL,1600,NULL,'1',2,'0',NULL,'1',' ','2022-03-26 13:06:31',' ','2022-03-26 13:06:38','0'),
	 (1604,'岗位信息删除',NULL,'sys_post_del',NULL,1600,NULL,'1',3,'0',NULL,'1',' ','2022-03-26 13:06:31',' ',NULL,'0'),
	 (1605,'岗位导入导出',NULL,'sys_post_export',NULL,1600,NULL,'1',4,'0',NULL,'1',' ','2022-03-26 13:06:31',' ','2022-03-26 06:32:02','0'),
	 (2000,'系统管理','system',NULL,'/system',-1,'iconfont icon-quanjushezhi_o','1',1,'0',NULL,'0','','2017-11-07 20:56:00','admin','2023-07-05 10:27:58','0'),
	 (2001,'日志管理','log',NULL,'/admin/logs',2000,'ele-Cloudy','1',0,'0','0','0','admin','2023-03-02 12:26:42','admin','2023-07-05 10:27:53','0'),
	 (2100,'操作日志','operation',NULL,'/admin/log/index',2001,'iconfont icon-jinridaiban','1',2,'0','0','0','','2017-11-20 14:06:22','admin','2023-07-05 10:27:49','0'),
	 (2101,'日志删除',NULL,'sys_log_del',NULL,2100,NULL,'1',1,'0',NULL,'1',' ','2017-11-20 20:37:37',' ','2021-05-25 03:12:55','0');
INSERT INTO public.sys_menu (menu_id,"name",en_name,"permission","path",parent_id,icon,visible,sort_order,keep_alive,embedded,menu_type,create_by,create_time,update_by,update_time,del_flag) VALUES
	 (2102,'导入导出',NULL,'sys_log_export',NULL,2100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:54:01',' ','2021-05-25 03:12:55','0'),
	 (2200,'字典管理','dict',NULL,'/admin/dict/index',2000,'iconfont icon-zhongduancanshuchaxun','1',6,'0',NULL,'0','','2017-11-29 11:30:52','admin','2023-07-05 10:27:37','0'),
	 (2201,'字典删除',NULL,'sys_dict_del',NULL,2200,NULL,'1',1,'0',NULL,'1',' ','2017-11-29 11:30:11',' ','2021-05-25 03:12:55','0'),
	 (2202,'字典新增',NULL,'sys_dict_add',NULL,2200,NULL,'1',1,'0',NULL,'1',' ','2018-05-11 22:34:55',' ','2021-05-25 03:12:55','0'),
	 (2203,'字典修改',NULL,'sys_dict_edit',NULL,2200,NULL,'1',1,'0',NULL,'1',' ','2018-05-11 22:36:03',' ','2021-05-25 03:12:55','0'),
	 (2210,'参数管理','parameter',NULL,'/admin/param/index',2000,'iconfont icon-wenducanshu-05','1',7,'1',NULL,'0','','2019-04-29 22:16:50','admin','2023-02-16 15:24:51','0'),
	 (2211,'参数新增',NULL,'sys_syspublicparam_add',NULL,2210,NULL,'1',1,'0',NULL,'1',' ','2019-04-29 22:17:36',' ','2020-03-24 08:57:11','0'),
	 (2212,'参数删除',NULL,'sys_syspublicparam_del',NULL,2210,NULL,'1',1,'0',NULL,'1',' ','2019-04-29 22:17:55',' ','2020-03-24 08:57:12','0'),
	 (2213,'参数编辑',NULL,'sys_syspublicparam_edit',NULL,2210,NULL,'1',1,'0',NULL,'1',' ','2019-04-29 22:18:14',' ','2020-03-24 08:57:13','0'),
	 (2300,'代码生成','code',NULL,'/gen/table/index',9000,'iconfont icon-zhongduancanshu','1',1,'0','0','0','','2018-01-20 13:17:19','admin','2023-02-20 13:54:35','0');
INSERT INTO public.sys_menu (menu_id,"name",en_name,"permission","path",parent_id,icon,visible,sort_order,keep_alive,embedded,menu_type,create_by,create_time,update_by,update_time,del_flag) VALUES
	 (2400,'终端管理','client',NULL,'/admin/client/index',2000,'iconfont icon-gongju','1',9,'1',NULL,'0','','2018-01-20 13:17:19','admin','2023-02-16 15:25:28','0'),
	 (2401,'客户端新增',NULL,'sys_client_add',NULL,2400,'1','1',1,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2021-05-25 03:12:55','0'),
	 (2402,'客户端修改',NULL,'sys_client_edit',NULL,2400,NULL,'1',1,'0',NULL,'1',' ','2018-05-15 21:37:06',' ','2021-05-25 03:12:55','0'),
	 (2403,'客户端删除',NULL,'sys_client_del',NULL,2400,NULL,'1',1,'0',NULL,'1',' ','2018-05-15 21:39:16',' ','2021-05-25 03:12:55','0'),
	 (2600,'令牌管理','token',NULL,'/admin/token/index',2000,'ele-Key','1',11,'0',NULL,'0','','2018-09-04 05:58:41','admin','2023-02-16 15:28:28','0'),
	 (2601,'令牌删除',NULL,'sys_token_del',NULL,2600,NULL,'1',1,'0',NULL,'1',' ','2018-09-04 05:59:50',' ','2020-03-24 08:57:24','0'),
	 (2800,'Quartz管理','quartz',NULL,'/daemon/job-manage/index',2000,'ele-AlarmClock','1',8,'0',NULL,'0','','2018-01-20 13:17:19','admin','2023-02-16 15:25:06','0'),
	 (2810,'任务新增',NULL,'job_sys_job_add',NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:26','0'),
	 (2820,'任务修改',NULL,'job_sys_job_edit',NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:27','0'),
	 (2830,'任务删除',NULL,'job_sys_job_del',NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:28','0');
INSERT INTO public.sys_menu (menu_id,"name",en_name,"permission","path",parent_id,icon,visible,sort_order,keep_alive,embedded,menu_type,create_by,create_time,update_by,update_time,del_flag) VALUES
	 (2840,'任务暂停',NULL,'job_sys_job_shutdown_job',NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:28','0'),
	 (2850,'任务开始',NULL,'job_sys_job_start_job',NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:29','0'),
	 (2860,'任务刷新',NULL,'job_sys_job_refresh_job',NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:30','0'),
	 (2870,'执行任务',NULL,'job_sys_job_run_job',NULL,2800,'1','1',0,'0',NULL,'1',' ','2019-08-08 15:35:18',' ','2020-03-24 08:57:31','0'),
	 (2871,'导出',NULL,'job_sys_job_export',NULL,2800,NULL,'1',0,'0','0','1','admin','2023-03-06 15:26:13',' ',NULL,'0'),
	 (2906,'文件管理','file',NULL,'/admin/file/index',2000,'ele-Files','1',6,'0',NULL,'0','','2019-06-25 12:44:46','admin','2023-02-16 15:24:42','0'),
	 (2907,'删除文件',NULL,'sys_file_del',NULL,2906,NULL,'1',1,'0',NULL,'1',' ','2019-06-25 13:41:41',' ','2020-03-24 08:58:42','0'),
	 (4000,'系统监控','monitor',NULL,'/daemon',-1,'iconfont icon-shuju','1',3,'0','0','0','admin','2023-02-06 20:20:47','admin','2023-02-23 20:01:07','0'),
	 (4001,'文档扩展','doc',NULL,'http://pig-gateway:9999/swagger-ui.html',4000,'iconfont icon-biaodan','1',2,'0','1','0','','2018-06-26 10:50:32','admin','2023-02-23 20:01:29','0'),
	 (4002,'缓存监控','cache',NULL,'/ext/cache',4000,'iconfont icon-shuju','1',1,'0','0','0','admin','2023-05-29 15:12:59','admin','2023-06-06 11:58:41','0');
INSERT INTO public.sys_menu (menu_id,"name",en_name,"permission","path",parent_id,icon,visible,sort_order,keep_alive,embedded,menu_type,create_by,create_time,update_by,update_time,del_flag) VALUES
	 (9000,'开发平台','develop',NULL,'/gen',-1,'iconfont icon-shuxingtu','1',9,'0','0','0','','2019-08-12 09:35:16','admin','2023-07-05 10:25:27','0'),
	 (9005,'数据源管理','datasource',NULL,'/gen/datasource/index',9000,'ele-Coin','1',0,'0',NULL,'0','','2019-08-12 09:42:11','admin','2023-07-05 10:26:56','0'),
	 (9006,'表单设计','Form Design',NULL,'/gen/design/index',9000,'iconfont icon-AIshiyanshi','0',2,'0','0','0','','2019-08-16 10:08:56','admin','2023-02-23 14:06:50','0'),
	 (9007,'生成页面','generation',NULL,'/gen/gener/index',9000,'iconfont icon-tongzhi4','0',0,'0','0','0','admin','2023-02-20 09:58:23','admin','2023-07-05 10:27:06','0'),
	 (9050,'元数据管理','metadata',NULL,'/gen/metadata',9000,'iconfont icon--chaifenhang','1',9,'0','0','0','','2018-07-27 01:13:21','admin','2023-07-05 10:27:13','0'),
	 (9051,'模板管理','template',NULL,'/gen/template/index',9050,'iconfont icon--chaifenhang','1',5,'0','0','0','admin','2023-02-21 11:22:54','admin','2023-07-05 10:27:18','0'),
	 (9052,'查询',NULL,'codegen_template_view',NULL,9051,NULL,'0',0,'0','0','1','admin','2023-02-21 12:33:03','admin','2023-02-21 13:50:54','0'),
	 (9053,'增加',NULL,'codegen_template_add',NULL,9051,NULL,'1',0,'0','0','1','admin','2023-02-21 13:34:10','admin','2023-02-21 13:39:49','0'),
	 (9054,'新增',NULL,'codegen_template_add',NULL,9051,NULL,'0',1,'0','0','1','admin','2023-02-21 13:51:32',' ',NULL,'0'),
	 (9055,'导出',NULL,'codegen_template_export',NULL,9051,NULL,'0',2,'0','0','1','admin','2023-02-21 13:51:58',' ',NULL,'0');
INSERT INTO public.sys_menu (menu_id,"name",en_name,"permission","path",parent_id,icon,visible,sort_order,keep_alive,embedded,menu_type,create_by,create_time,update_by,update_time,del_flag) VALUES
	 (9056,'删除',NULL,'codegen_template_del',NULL,9051,NULL,'0',3,'0','0','1','admin','2023-02-21 13:52:16',' ',NULL,'0'),
	 (9057,'编辑',NULL,'codegen_template_edit',NULL,9051,NULL,'0',4,'0','0','1','admin','2023-02-21 13:52:58',' ',NULL,'0'),
	 (9059,'模板分组','group',NULL,'/gen/group/index',9050,'iconfont icon-shuxingtu','1',6,'0','0','0','admin','2023-02-21 15:06:50','admin','2023-07-05 10:27:22','0'),
	 (9060,'查询',NULL,'codegen_group_view',NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:08:07',' ',NULL,'0'),
	 (9061,'新增',NULL,'codegen_group_add',NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:08:28',' ',NULL,'0'),
	 (9062,'修改',NULL,'codegen_group_edit',NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:08:43',' ',NULL,'0'),
	 (9063,'删除',NULL,'codegen_group_del',NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:09:02',' ',NULL,'0'),
	 (9064,'导出',NULL,'codegen_group_export',NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:09:22',' ',NULL,'0'),
	 (9065,'字段管理','field',NULL,'/gen/field-type/index',9050,'iconfont icon-fuwenben','1',0,'0','0','0','admin','2023-02-23 20:05:09','admin','2023-07-05 10:27:31','0');
INSERT INTO public.sys_oauth_client_details (id,client_id,resource_ids,client_secret,"scope",authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove,del_flag,create_by,update_by,create_time,update_time) VALUES
	 (1,'app',NULL,'app','server','password,refresh_token,authorization_code,client_credentials,mobile','http://localhost:4040/sso1/login,http://localhost:4041/sso1/login,http://localhost:8080/renren-admin/sys/oauth2-sso,http://localhost:8090/sys/oauth2-sso',NULL,43200,2592001,'{"enc_flag":"1","captcha_flag":"1","online_quantity":"1"}','true','0','','admin',NULL,'2023-02-09 13:54:54'),
	 (2,'daemon',NULL,'daemon','server','password,refresh_token',NULL,NULL,43200,2592001,'{"enc_flag":"1","captcha_flag":"1"}','true','0',' ',' ',NULL,NULL),
	 (3,'gen',NULL,'gen','server','password,refresh_token',NULL,NULL,43200,2592001,'{"enc_flag":"1","captcha_flag":"1"}','true','0',' ',' ',NULL,NULL),
	 (4,'mp',NULL,'mp','server','password,refresh_token',NULL,NULL,43200,2592001,'{"enc_flag":"1","captcha_flag":"1"}','true','0',' ',' ',NULL,NULL),
	 (5,'pig',NULL,'pig','server','password,refresh_token,authorization_code,client_credentials,mobile','http://localhost:4040/sso1/login,http://localhost:4041/sso1/login,http://localhost:8080/renren-admin/sys/oauth2-sso,http://localhost:8090/sys/oauth2-sso',NULL,43200,2592001,'{"enc_flag":"1","captcha_flag":"1","online_quantity":"1"}','false','0','','admin',NULL,'2023-03-08 11:32:41'),
	 (6,'test',NULL,'test','server','password,refresh_token',NULL,NULL,43200,2592001,'{ "enc_flag":"1","captcha_flag":"0"}','true','0',' ',' ',NULL,NULL),
	 (7,'social',NULL,'social','server','password,refresh_token,mobile',NULL,NULL,43200,2592001,'{ "enc_flag":"0","captcha_flag":"0"}','true','0',' ',' ',NULL,NULL);
INSERT INTO public.sys_post (post_id,post_code,post_name,post_sort,remark,del_flag,create_time,create_by,update_time,update_by) VALUES
	 (1,'CTO','CTO',0,'CTOOO','0','2022-03-26 13:48:17','','2023-03-08 16:03:35','admin');
INSERT INTO public.sys_public_param (public_id,public_name,public_key,public_value,status,validate_code,create_by,update_by,create_time,update_time,public_type,system_flag,del_flag) VALUES
	 (1,'租户默认来源','TENANT_DEFAULT_ID','1','0','',' ',' ','2020-05-12 04:03:46','2020-06-20 08:56:30','2','0','1'),
	 (2,'租户默认部门名称','TENANT_DEFAULT_DEPTNAME','租户默认部门','0','',' ',' ','2020-05-12 03:36:32',NULL,'2','1','0'),
	 (3,'租户默认账户','TENANT_DEFAULT_USERNAME','admin','0','',' ',' ','2020-05-12 04:05:04',NULL,'2','1','0'),
	 (4,'租户默认密码','TENANT_DEFAULT_PASSWORD','123456','0','',' ',' ','2020-05-12 04:05:24',NULL,'2','1','0'),
	 (5,'租户默认角色编码','TENANT_DEFAULT_ROLECODE','ROLE_ADMIN','0','',' ',' ','2020-05-12 04:05:57',NULL,'2','1','0'),
	 (6,'租户默认角色名称','TENANT_DEFAULT_ROLENAME','租户默认角色','0','',' ',' ','2020-05-12 04:06:19',NULL,'2','1','0'),
	 (7,'表前缀','GEN_TABLE_PREFIX','tb_','0','',' ',' ','2020-05-12 04:23:04',NULL,'9','1','0'),
	 (8,'接口文档不显示的字段','GEN_HIDDEN_COLUMNS','tenant_id','0','',' ',' ','2020-05-12 04:25:19',NULL,'9','1','0'),
	 (9,'注册用户默认角色','USER_DEFAULT_ROLE','GENERAL_USER','0',NULL,' ',' ','2022-03-31 16:52:24',NULL,'2','1','0');
INSERT INTO public.sys_role (role_id,role_name,role_code,role_desc,create_by,update_by,create_time,update_time,del_flag) VALUES
	 (1,'管理员','ROLE_ADMIN','管理员','','admin','2017-10-29 15:45:51','2023-07-07 14:55:07','0'),
	 (2,'普通用户','GENERAL_USER','普通用户','','admin','2022-03-31 17:03:15','2023-04-03 02:28:51','0');
INSERT INTO public.sys_role_menu (role_id,menu_id) VALUES
	 (1,1000),
	 (1,1100),
	 (1,1101),
	 (1,1102),
	 (1,1103),
	 (1,1104),
	 (1,1200),
	 (1,1201),
	 (1,1202),
	 (1,1203);
INSERT INTO public.sys_role_menu (role_id,menu_id) VALUES
	 (1,1300),
	 (1,1301),
	 (1,1302),
	 (1,1303),
	 (1,1304),
	 (1,1305),
	 (1,1400),
	 (1,1401),
	 (1,1402),
	 (1,1403);
INSERT INTO public.sys_role_menu (role_id,menu_id) VALUES
	 (1,1600),
	 (1,1601),
	 (1,1602),
	 (1,1603),
	 (1,1604),
	 (1,1605),
	 (1,2000),
	 (1,2001),
	 (1,2100),
	 (1,2101);
INSERT INTO public.sys_role_menu (role_id,menu_id) VALUES
	 (1,2102),
	 (1,2200),
	 (1,2201),
	 (1,2202),
	 (1,2203),
	 (1,2210),
	 (1,2211),
	 (1,2212),
	 (1,2213),
	 (1,2300);
INSERT INTO public.sys_role_menu (role_id,menu_id) VALUES
	 (1,2400),
	 (1,2401),
	 (1,2402),
	 (1,2403),
	 (1,2600),
	 (1,2601),
	 (1,2800),
	 (1,2810),
	 (1,2820),
	 (1,2830);
INSERT INTO public.sys_role_menu (role_id,menu_id) VALUES
	 (1,2840),
	 (1,2850),
	 (1,2860),
	 (1,2870),
	 (1,2871),
	 (1,2906),
	 (1,2907),
	 (1,4000),
	 (1,4001),
	 (1,4002);
INSERT INTO public.sys_role_menu (role_id,menu_id) VALUES
	 (1,9000),
	 (1,9005),
	 (1,9006),
	 (1,9007),
	 (1,9050),
	 (1,9051),
	 (1,9052),
	 (1,9053),
	 (1,9054),
	 (1,9055);
INSERT INTO public.sys_role_menu (role_id,menu_id) VALUES
	 (1,9056),
	 (1,9057),
	 (1,9059),
	 (1,9060),
	 (1,9061),
	 (1,9062),
	 (1,9063),
	 (1,9064),
	 (1,9065),
	 (2,4000);
INSERT INTO public.sys_role_menu (role_id,menu_id) VALUES
	 (2,4001),
	 (2,4002);
INSERT INTO public.sys_user (user_id,username,"password",salt,phone,avatar,nickname,"name",email,dept_id,create_by,update_by,create_time,update_time,lock_flag,del_flag,wx_openid,mini_openid,qq_openid,gitee_login,osc_id) VALUES
	 (1,'admin','$2a$10$c/Ae0pRjJtMZg3BnvVpO.eIK6WYWVbKTzqgdy3afR7w.vd.xi3Mgy','','17034642999','/admin/sys-file/s3demo/7ff4ca6b7bf446f3a5a13ac016dc21af.png','管理员','管理员','pig4cloud@qq.com',4,' ','admin','2018-04-20 07:15:18','2023-07-07 14:55:40','0','0',NULL,'oBxPy5E-v82xWGsfzZVzkD3wEX64',NULL,'log4j',NULL);
INSERT INTO public.sys_user_post (user_id,post_id) VALUES
	 (1,1);
INSERT INTO public.sys_user_role (user_id,role_id) VALUES
	 (1,1),
	 (1676492190299299842,2);
