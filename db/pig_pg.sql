-- public.gen_datasource_conf definition

-- Drop table

-- DROP TABLE public.gen_datasource_conf;

CREATE TABLE public.gen_datasource_conf (
	id int8 NOT NULL,
	"name" varchar(64) NULL,
	url varchar(255) NULL,
	username varchar(64) NULL,
	"password" varchar(64) NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NULL,
	ds_type varchar(64) NULL,
	conf_type bpchar(1) NULL,
	ds_name varchar(64) NULL,
	"instance" varchar(64) NULL,
	port int4 NULL,
	host varchar(128) NULL,
	CONSTRAINT gen_datasource_conf_pkey PRIMARY KEY (id)
);


-- public.gen_field_type definition

-- Drop table

-- DROP TABLE public.gen_field_type;

CREATE TABLE public.gen_field_type (
	id int8 NOT NULL,
	column_type varchar(200) NULL,
	attr_type varchar(200) NULL,
	package_name varchar(200) NULL,
	create_time timestamp NULL,
	create_by varchar(64) NULL,
	update_time timestamp NULL,
	update_by varchar(64) NULL,
	del_flag bpchar(1) NULL,
	CONSTRAINT gen_field_type_pkey PRIMARY KEY (id)
);
CREATE UNIQUE INDEX column_type ON public.gen_field_type USING btree (column_type);


-- public.gen_group definition

-- Drop table

-- DROP TABLE public.gen_group;

CREATE TABLE public.gen_group (
	id int8 NOT NULL,
	group_name varchar(255) NULL,
	group_desc varchar(255) NULL,
	create_by varchar(64) NOT NULL,
	update_by varchar(64) NOT NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NULL,
	CONSTRAINT gen_group_pkey PRIMARY KEY (id)
);


-- public.gen_table definition

-- Drop table

-- DROP TABLE public.gen_table;

CREATE TABLE public.gen_table (
	id int8 NOT NULL,
	table_name varchar(200) NULL,
	class_name varchar(200) NULL,
	db_type varchar(200) NULL,
	table_comment varchar(200) NULL,
	author varchar(200) NULL,
	email varchar(200) NULL,
	package_name varchar(200) NULL,
	"version" varchar(200) NULL,
	i18n bpchar(1) NULL,
	"style" int8 NULL,
	child_table_name varchar(200) NULL,
	main_field varchar(200) NULL,
	child_field varchar(200) NULL,
	generator_type bpchar(1) NULL,
	backend_path varchar(500) NULL,
	frontend_path varchar(500) NULL,
	module_name varchar(200) NULL,
	function_name varchar(200) NULL,
	form_layout int2 NULL,
	ds_name varchar(200) NULL,
	baseclass_id int8 NULL,
	create_time timestamp NULL,
	CONSTRAINT gen_table_pkey PRIMARY KEY (id)
);
CREATE UNIQUE INDEX table_name ON public.gen_table USING btree (table_name, ds_name);


-- public.gen_table_column definition

-- Drop table

-- DROP TABLE public.gen_table_column;

CREATE TABLE public.gen_table_column (
	id int8 NOT NULL,
	ds_name varchar(200) NULL,
	table_name varchar(200) NULL,
	field_name varchar(200) NULL,
	field_type varchar(200) NULL,
	field_comment varchar(200) NULL,
	attr_name varchar(200) NULL,
	attr_type varchar(200) NULL,
	package_name varchar(200) NULL,
	sort int4 NULL,
	auto_fill varchar(20) NULL,
	primary_pk bpchar(1) NULL,
	base_field bpchar(1) NULL,
	form_item bpchar(1) NULL,
	form_required bpchar(1) NULL,
	form_type varchar(200) NULL,
	form_validator varchar(200) NULL,
	grid_item bpchar(1) NULL,
	grid_sort bpchar(1) NULL,
	query_item bpchar(1) NULL,
	query_type varchar(200) NULL,
	query_form_type varchar(200) NULL,
	field_dict varchar(200) NULL,
	CONSTRAINT gen_table_column_pkey PRIMARY KEY (id)
);


-- public.gen_template definition

-- Drop table

-- DROP TABLE public.gen_template;

CREATE TABLE public.gen_template (
	id int8 NOT NULL,
	template_name varchar(255) NOT NULL,
	generator_path varchar(255) NOT NULL,
	template_desc varchar(255) NOT NULL,
	template_code text NOT NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NOT NULL,
	create_by varchar(64) NOT NULL,
	update_by varchar(64) NOT NULL,
	CONSTRAINT gen_template_pkey PRIMARY KEY (id)
);


-- public.gen_template_group definition

-- Drop table

-- DROP TABLE public.gen_template_group;

CREATE TABLE public.gen_template_group (
	group_id int8 NOT NULL,
	template_id int8 NOT NULL,
	CONSTRAINT gen_template_group_pkey PRIMARY KEY (group_id, template_id)
);


-- public.qrtz_calendars definition

-- Drop table

-- DROP TABLE public.qrtz_calendars;

CREATE TABLE public.qrtz_calendars (
	"SCHED_NAME" varchar(120) NOT NULL,
	"CALENDAR_NAME" varchar(200) NOT NULL,
	"CALENDAR" bytea NOT NULL,
	CONSTRAINT qrtz_calendars_pkey PRIMARY KEY ("SCHED_NAME", "CALENDAR_NAME")
);


-- public.qrtz_fired_triggers definition

-- Drop table

-- DROP TABLE public.qrtz_fired_triggers;

CREATE TABLE public.qrtz_fired_triggers (
	"SCHED_NAME" varchar(120) NOT NULL,
	"ENTRY_ID" varchar(95) NOT NULL,
	"TRIGGER_NAME" varchar(200) NOT NULL,
	"TRIGGER_GROUP" varchar(200) NOT NULL,
	"INSTANCE_NAME" varchar(200) NOT NULL,
	"FIRED_TIME" int8 NOT NULL,
	"SCHED_TIME" int8 NOT NULL,
	"PRIORITY" int4 NOT NULL,
	"STATE" varchar(16) NOT NULL,
	"JOB_NAME" varchar(200) NULL,
	"JOB_GROUP" varchar(200) NULL,
	"IS_NONCONCURRENT" varchar(1) NULL,
	"REQUESTS_RECOVERY" varchar(1) NULL,
	CONSTRAINT qrtz_fired_triggers_pkey PRIMARY KEY ("SCHED_NAME", "ENTRY_ID")
);


-- public.qrtz_job_details definition

-- Drop table

-- DROP TABLE public.qrtz_job_details;

CREATE TABLE public.qrtz_job_details (
	"SCHED_NAME" varchar(120) NOT NULL,
	"JOB_NAME" varchar(200) NOT NULL,
	"JOB_GROUP" varchar(200) NOT NULL,
	"DESCRIPTION" varchar(250) NULL,
	"JOB_CLASS_NAME" varchar(250) NOT NULL,
	"IS_DURABLE" varchar(1) NOT NULL,
	"IS_NONCONCURRENT" varchar(1) NOT NULL,
	"IS_UPDATE_DATA" varchar(1) NOT NULL,
	"REQUESTS_RECOVERY" varchar(1) NOT NULL,
	"JOB_DATA" bytea NULL,
	CONSTRAINT qrtz_job_details_pkey PRIMARY KEY ("SCHED_NAME", "JOB_NAME", "JOB_GROUP")
);


-- public.qrtz_locks definition

-- Drop table

-- DROP TABLE public.qrtz_locks;

CREATE TABLE public.qrtz_locks (
	"SCHED_NAME" varchar(120) NOT NULL,
	"LOCK_NAME" varchar(40) NOT NULL,
	CONSTRAINT qrtz_locks_pkey PRIMARY KEY ("SCHED_NAME", "LOCK_NAME")
);


-- public.qrtz_paused_trigger_grps definition

-- Drop table

-- DROP TABLE public.qrtz_paused_trigger_grps;

CREATE TABLE public.qrtz_paused_trigger_grps (
	"SCHED_NAME" varchar(120) NOT NULL,
	"TRIGGER_GROUP" varchar(200) NOT NULL,
	CONSTRAINT qrtz_paused_trigger_grps_pkey PRIMARY KEY ("SCHED_NAME", "TRIGGER_GROUP")
);


-- public.qrtz_scheduler_state definition

-- Drop table

-- DROP TABLE public.qrtz_scheduler_state;

CREATE TABLE public.qrtz_scheduler_state (
	"SCHED_NAME" varchar(120) NOT NULL,
	"INSTANCE_NAME" varchar(200) NOT NULL,
	"LAST_CHECKIN_TIME" int8 NOT NULL,
	"CHECKIN_INTERVAL" int8 NOT NULL,
	CONSTRAINT qrtz_scheduler_state_pkey PRIMARY KEY ("SCHED_NAME", "INSTANCE_NAME")
);


-- public.sys_dept definition

-- Drop table

-- DROP TABLE public.sys_dept;

CREATE TABLE public.sys_dept (
	dept_id int8 NOT NULL,
	"name" varchar(50) NULL,
	sort_order int4 NOT NULL,
	create_by varchar(64) NOT NULL,
	update_by varchar(64) NOT NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NULL,
	parent_id int8 NULL,
	CONSTRAINT sys_dept_pkey PRIMARY KEY (dept_id)
);


-- public.sys_dict definition

-- Drop table

-- DROP TABLE public.sys_dict;

CREATE TABLE public.sys_dict (
	id int8 NOT NULL,
	dict_type varchar(100) NULL,
	description varchar(100) NULL,
	create_by varchar(64) NOT NULL,
	update_by varchar(64) NOT NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	remarks varchar(255) NULL,
	system_flag bpchar(1) NULL,
	del_flag bpchar(1) NULL,
	CONSTRAINT sys_dict_pkey PRIMARY KEY (id)
);
CREATE INDEX sys_dict_del_flag ON public.sys_dict USING btree (del_flag);


-- public.sys_dict_item definition

-- Drop table

-- DROP TABLE public.sys_dict_item;

CREATE TABLE public.sys_dict_item (
	id int8 NOT NULL,
	dict_id int8 NOT NULL,
	item_value varchar(100) NULL,
	"label" varchar(100) NULL,
	dict_type varchar(100) NULL,
	description varchar(100) NULL,
	sort_order int4 NOT NULL,
	create_by varchar(64) NOT NULL,
	update_by varchar(64) NOT NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	remarks varchar(255) NULL,
	del_flag bpchar(1) NULL,
	CONSTRAINT sys_dict_item_pkey PRIMARY KEY (id)
);
CREATE INDEX sys_dict_item_del_flag ON public.sys_dict_item USING btree (del_flag);
CREATE INDEX sys_dict_label ON public.sys_dict_item USING btree (label);
CREATE INDEX sys_dict_value ON public.sys_dict_item USING btree (item_value);


-- public.sys_file definition

-- Drop table

-- DROP TABLE public.sys_file;

CREATE TABLE public.sys_file (
	id int8 NOT NULL,
	file_name varchar(100) NULL,
	bucket_name varchar(200) NULL,
	original varchar(100) NULL,
	"type" varchar(50) NULL,
	file_size int8 NULL,
	create_by varchar(64) NOT NULL,
	update_by varchar(64) NOT NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NULL,
	CONSTRAINT sys_file_pkey PRIMARY KEY (id)
);


-- public.sys_job definition

-- Drop table

-- DROP TABLE public.sys_job;

CREATE TABLE public.sys_job (
	job_id int8 NOT NULL,
	job_name varchar(64) NOT NULL,
	job_group varchar(64) NOT NULL,
	job_order bpchar(1) NULL,
	job_type bpchar(1) NOT NULL,
	execute_path varchar(500) NULL,
	class_name varchar(500) NULL,
	method_name varchar(500) NULL,
	method_params_value varchar(2000) NULL,
	cron_expression varchar(255) NULL,
	misfire_policy varchar(20) NULL,
	job_tenant_type bpchar(1) NULL,
	job_status bpchar(1) NULL,
	job_execute_status bpchar(1) NULL,
	create_by varchar(64) NULL,
	create_time timestamp NOT NULL,
	update_by varchar(64) NULL,
	update_time timestamp NOT NULL,
	start_time timestamp NULL,
	previous_time timestamp NULL,
	next_time timestamp NULL,
	remark varchar(500) NULL,
	CONSTRAINT sys_job_pkey PRIMARY KEY (job_id)
);
CREATE UNIQUE INDEX job_name_group_idx ON public.sys_job USING btree (job_name, job_group);


-- public.sys_job_log definition

-- Drop table

-- DROP TABLE public.sys_job_log;

CREATE TABLE public.sys_job_log (
	job_log_id int8 NOT NULL,
	job_id int8 NOT NULL,
	job_name varchar(64) NULL,
	job_group varchar(64) NULL,
	job_order bpchar(1) NULL,
	job_type bpchar(1) NOT NULL,
	execute_path varchar(500) NULL,
	class_name varchar(500) NULL,
	method_name varchar(500) NULL,
	method_params_value varchar(2000) NULL,
	cron_expression varchar(255) NULL,
	job_message varchar(500) NULL,
	job_log_status bpchar(1) NULL,
	execute_time varchar(30) NULL,
	exception_info varchar(2000) NULL,
	create_time timestamp NOT NULL,
	CONSTRAINT sys_job_log_pkey PRIMARY KEY (job_log_id)
);


-- public.sys_log definition

-- Drop table

-- DROP TABLE public.sys_log;

CREATE TABLE public.sys_log (
	id int8 NOT NULL,
	log_type bpchar(1) NULL,
	title varchar(255) NULL,
	service_id varchar(32) NULL,
	create_by varchar(64) NULL,
	update_by varchar(64) NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	remote_addr varchar(255) NULL,
	user_agent varchar(1000) NULL,
	request_uri varchar(255) NULL,
	"method" varchar(10) NULL,
	params text NULL,
	"time" int8 NULL,
	del_flag bpchar(1) NULL,
	"exception" text NULL,
	CONSTRAINT sys_log_pkey PRIMARY KEY (id)
);
CREATE INDEX sys_log_create_date ON public.sys_log USING btree (create_time);
CREATE INDEX sys_log_request_uri ON public.sys_log USING btree (request_uri);
CREATE INDEX sys_log_type ON public.sys_log USING btree (log_type);


-- public.sys_menu definition

-- Drop table

-- DROP TABLE public.sys_menu;

CREATE TABLE public.sys_menu (
	menu_id int8 NOT NULL,
	"name" varchar(32) NULL,
	en_name varchar(128) NULL,
	"permission" varchar(32) NULL,
	"path" varchar(128) NULL,
	parent_id int8 NULL,
	icon varchar(64) NULL,
	visible bpchar(1) NULL,
	sort_order int4 NULL,
	keep_alive bpchar(1) NULL,
	embedded bpchar(1) NULL,
	menu_type bpchar(1) NULL,
	create_by varchar(64) NOT NULL,
	create_time timestamp NULL,
	update_by varchar(64) NOT NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NULL,
	CONSTRAINT sys_menu_pkey PRIMARY KEY (menu_id)
);


-- public.sys_oauth_client_details definition

-- Drop table

-- DROP TABLE public.sys_oauth_client_details;

CREATE TABLE public.sys_oauth_client_details (
	id int8 NOT NULL,
	client_id varchar(32) NOT NULL,
	resource_ids varchar(256) NULL,
	client_secret varchar(256) NULL,
	"scope" varchar(256) NULL,
	authorized_grant_types varchar(256) NULL,
	web_server_redirect_uri varchar(256) NULL,
	authorities varchar(256) NULL,
	access_token_validity int4 NULL,
	refresh_token_validity int4 NULL,
	additional_information varchar(4096) NULL,
	autoapprove varchar(256) NULL,
	del_flag bpchar(1) NULL,
	create_by varchar(64) NOT NULL,
	update_by varchar(64) NOT NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	CONSTRAINT sys_oauth_client_details_pkey PRIMARY KEY (id)
);


-- public.sys_post definition

-- Drop table

-- DROP TABLE public.sys_post;

CREATE TABLE public.sys_post (
	post_id int8 NOT NULL,
	post_code varchar(64) NOT NULL,
	post_name varchar(50) NOT NULL,
	post_sort int4 NOT NULL,
	remark varchar(500) NULL,
	del_flag bpchar(1) NOT NULL,
	create_time timestamp NULL,
	create_by varchar(64) NOT NULL,
	update_time timestamp NULL,
	update_by varchar(64) NOT NULL,
	CONSTRAINT sys_post_pkey PRIMARY KEY (post_id)
);


-- public.sys_public_param definition

-- Drop table

-- DROP TABLE public.sys_public_param;

CREATE TABLE public.sys_public_param (
	public_id int8 NOT NULL,
	public_name varchar(128) NULL,
	public_key varchar(128) NULL,
	public_value varchar(128) NULL,
	status bpchar(1) NULL,
	validate_code varchar(64) NULL,
	create_by varchar(64) NOT NULL,
	update_by varchar(64) NOT NULL,
	create_time timestamp NOT NULL,
	update_time timestamp NULL,
	public_type bpchar(1) NULL,
	system_flag bpchar(1) NULL,
	del_flag bpchar(1) NULL,
	CONSTRAINT sys_public_param_pkey PRIMARY KEY (public_id)
);


-- public.sys_role definition

-- Drop table

-- DROP TABLE public.sys_role;

CREATE TABLE public.sys_role (
	role_id int8 NOT NULL,
	role_name varchar(64) NULL,
	role_code varchar(64) NULL,
	role_desc varchar(255) NULL,
	create_by varchar(64) NOT NULL,
	update_by varchar(64) NOT NULL,
	create_time timestamp NOT NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NULL,
	CONSTRAINT sys_role_pkey PRIMARY KEY (role_id)
);
CREATE INDEX role_idx1_role_code ON public.sys_role USING btree (role_code);


-- public.sys_role_menu definition

-- Drop table

-- DROP TABLE public.sys_role_menu;

CREATE TABLE public.sys_role_menu (
	role_id int8 NOT NULL,
	menu_id int8 NOT NULL,
	CONSTRAINT sys_role_menu_pkey PRIMARY KEY (role_id, menu_id)
);


-- public.sys_user definition

-- Drop table

-- DROP TABLE public.sys_user;

CREATE TABLE public.sys_user (
	user_id int8 NOT NULL,
	username varchar(64) NULL,
	"password" varchar(255) NULL,
	salt varchar(255) NULL,
	phone varchar(20) NULL,
	avatar varchar(255) NULL,
	nickname varchar(64) NULL,
	"name" varchar(64) NULL,
	email varchar(128) NULL,
	dept_id int8 NULL,
	create_by varchar(64) NOT NULL,
	update_by varchar(64) NOT NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	lock_flag bpchar(1) NULL,
	del_flag bpchar(1) NULL,
	wx_openid varchar(32) NULL,
	mini_openid varchar(32) NULL,
	qq_openid varchar(32) NULL,
	gitee_login varchar(100) NULL,
	osc_id varchar(100) NULL,
	CONSTRAINT sys_user_pkey PRIMARY KEY (user_id)
);
CREATE INDEX user_idx1_username ON public.sys_user USING btree (username);
CREATE INDEX user_qq_openid ON public.sys_user USING btree (qq_openid);
CREATE INDEX user_wx_openid ON public.sys_user USING btree (wx_openid);


-- public.sys_user_post definition

-- Drop table

-- DROP TABLE public.sys_user_post;

CREATE TABLE public.sys_user_post (
	user_id int8 NOT NULL,
	post_id int8 NOT NULL,
	CONSTRAINT sys_user_post_pkey PRIMARY KEY (user_id, post_id)
);


-- public.sys_user_role definition

-- Drop table

-- DROP TABLE public.sys_user_role;

CREATE TABLE public.sys_user_role (
	user_id int8 NOT NULL,
	role_id int8 NOT NULL,
	CONSTRAINT sys_user_role_pkey PRIMARY KEY (user_id, role_id)
);


-- public.qrtz_triggers definition

-- Drop table

-- DROP TABLE public.qrtz_triggers;

CREATE TABLE public.qrtz_triggers (
	"SCHED_NAME" varchar(120) NOT NULL,
	"TRIGGER_NAME" varchar(200) NOT NULL,
	"TRIGGER_GROUP" varchar(200) NOT NULL,
	"JOB_NAME" varchar(200) NOT NULL,
	"JOB_GROUP" varchar(200) NOT NULL,
	"DESCRIPTION" varchar(250) NULL,
	"NEXT_FIRE_TIME" int8 NULL,
	"PREV_FIRE_TIME" int8 NULL,
	"PRIORITY" int4 NULL,
	"TRIGGER_STATE" varchar(16) NOT NULL,
	"TRIGGER_TYPE" varchar(8) NOT NULL,
	"START_TIME" int8 NOT NULL,
	"END_TIME" int8 NULL,
	"CALENDAR_NAME" varchar(200) NULL,
	"MISFIRE_INSTR" int2 NULL,
	"JOB_DATA" bytea NULL,
	CONSTRAINT qrtz_triggers_pkey PRIMARY KEY ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP"),
	CONSTRAINT qrtz_triggers_ibfk_1 FOREIGN KEY ("SCHED_NAME","JOB_NAME","JOB_GROUP") REFERENCES public.qrtz_job_details("SCHED_NAME","JOB_NAME","JOB_GROUP")
);
CREATE INDEX "SCHED_NAME" ON public.qrtz_triggers USING btree ("SCHED_NAME", "JOB_NAME", "JOB_GROUP");


-- public.qrtz_blob_triggers definition

-- Drop table

-- DROP TABLE public.qrtz_blob_triggers;

CREATE TABLE public.qrtz_blob_triggers (
	"SCHED_NAME" varchar(120) NOT NULL,
	"TRIGGER_NAME" varchar(200) NOT NULL,
	"TRIGGER_GROUP" varchar(200) NOT NULL,
	"BLOB_DATA" bytea NULL,
	CONSTRAINT qrtz_blob_triggers_pkey PRIMARY KEY ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP"),
	CONSTRAINT qrtz_blob_triggers_ibfk_1 FOREIGN KEY ("SCHED_NAME","TRIGGER_NAME","TRIGGER_GROUP") REFERENCES public.qrtz_triggers("SCHED_NAME","TRIGGER_NAME","TRIGGER_GROUP")
);


-- public.qrtz_cron_triggers definition

-- Drop table

-- DROP TABLE public.qrtz_cron_triggers;

CREATE TABLE public.qrtz_cron_triggers (
	"SCHED_NAME" varchar(120) NOT NULL,
	"TRIGGER_NAME" varchar(200) NOT NULL,
	"TRIGGER_GROUP" varchar(200) NOT NULL,
	"CRON_EXPRESSION" varchar(200) NOT NULL,
	"TIME_ZONE_ID" varchar(80) NULL,
	CONSTRAINT qrtz_cron_triggers_pkey PRIMARY KEY ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP"),
	CONSTRAINT qrtz_cron_triggers_ibfk_1 FOREIGN KEY ("SCHED_NAME","TRIGGER_NAME","TRIGGER_GROUP") REFERENCES public.qrtz_triggers("SCHED_NAME","TRIGGER_NAME","TRIGGER_GROUP")
);


-- public.qrtz_simple_triggers definition

-- Drop table

-- DROP TABLE public.qrtz_simple_triggers;

CREATE TABLE public.qrtz_simple_triggers (
	"SCHED_NAME" varchar(120) NOT NULL,
	"TRIGGER_NAME" varchar(200) NOT NULL,
	"TRIGGER_GROUP" varchar(200) NOT NULL,
	"REPEAT_COUNT" int8 NOT NULL,
	"REPEAT_INTERVAL" int8 NOT NULL,
	"TIMES_TRIGGERED" int8 NOT NULL,
	CONSTRAINT qrtz_simple_triggers_pkey PRIMARY KEY ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP"),
	CONSTRAINT qrtz_simple_triggers_ibfk_1 FOREIGN KEY ("SCHED_NAME","TRIGGER_NAME","TRIGGER_GROUP") REFERENCES public.qrtz_triggers("SCHED_NAME","TRIGGER_NAME","TRIGGER_GROUP")
);


-- public.qrtz_simprop_triggers definition

-- Drop table

-- DROP TABLE public.qrtz_simprop_triggers;

CREATE TABLE public.qrtz_simprop_triggers (
	"SCHED_NAME" varchar(120) NOT NULL,
	"TRIGGER_NAME" varchar(200) NOT NULL,
	"TRIGGER_GROUP" varchar(200) NOT NULL,
	"STR_PROP_1" varchar(512) NULL,
	"STR_PROP_2" varchar(512) NULL,
	"STR_PROP_3" varchar(512) NULL,
	"INT_PROP_1" int4 NULL,
	"INT_PROP_2" int4 NULL,
	"LONG_PROP_1" int8 NULL,
	"LONG_PROP_2" int8 NULL,
	"DEC_PROP_1" numeric(13, 4) NULL,
	"DEC_PROP_2" numeric(13, 4) NULL,
	"BOOL_PROP_1" varchar(1) NULL,
	"BOOL_PROP_2" varchar(1) NULL,
	CONSTRAINT qrtz_simprop_triggers_pkey PRIMARY KEY ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP"),
	CONSTRAINT qrtz_simprop_triggers_ibfk_1 FOREIGN KEY ("SCHED_NAME","TRIGGER_NAME","TRIGGER_GROUP") REFERENCES public.qrtz_triggers("SCHED_NAME","TRIGGER_NAME","TRIGGER_GROUP")
);