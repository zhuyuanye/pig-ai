-- PostgreSQL版本的Pig-AI数据库结构
-- 基于MySQL版本转换为PostgreSQL兼容语法

-- 删除数据库（如果存在）
-- DROP DATABASE IF EXISTS pig;

-- 创建数据库
-- CREATE DATABASE pig WITH ENCODING 'UTF8' LC_COLLATE='C' LC_CTYPE='C';

-- 连接到数据库
-- \c pig;

-- 设置搜索路径
SET search_path TO public;

-- 删除已存在的表
DROP TABLE IF EXISTS sys_dept CASCADE;
DROP TABLE IF EXISTS sys_dict CASCADE;
DROP TABLE IF EXISTS sys_dict_item CASCADE;
DROP TABLE IF EXISTS sys_file CASCADE;
DROP TABLE IF EXISTS sys_log CASCADE;
DROP TABLE IF EXISTS sys_menu CASCADE;
DROP TABLE IF EXISTS sys_role CASCADE;
DROP TABLE IF EXISTS sys_role_dept CASCADE;
DROP TABLE IF EXISTS sys_role_menu CASCADE;
DROP TABLE IF EXISTS sys_user CASCADE;
DROP TABLE IF EXISTS sys_user_role CASCADE;
DROP TABLE IF EXISTS sys_client CASCADE;
DROP TABLE IF EXISTS sys_public_param CASCADE;
DROP TABLE IF EXISTS sys_token CASCADE;
DROP TABLE IF EXISTS sys_tenant CASCADE;
DROP TABLE IF EXISTS sys_tenant_menu CASCADE;
DROP TABLE IF EXISTS sys_datasource_conf CASCADE;
DROP TABLE IF EXISTS sys_job CASCADE;
DROP TABLE IF EXISTS sys_job_log CASCADE;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
CREATE TABLE sys_dept (
    dept_id BIGINT NOT NULL,
    name VARCHAR(50) DEFAULT NULL,
    sort_order INTEGER NOT NULL DEFAULT 0,
    create_by VARCHAR(64) NOT NULL DEFAULT ' ',
    update_by VARCHAR(64) NOT NULL DEFAULT ' ',
    create_time TIMESTAMP DEFAULT NULL,
    update_time TIMESTAMP DEFAULT NULL,
    del_flag CHAR(1) DEFAULT '0',
    parent_id BIGINT DEFAULT NULL,
    PRIMARY KEY (dept_id)
);

COMMENT ON TABLE sys_dept IS '部门管理';
COMMENT ON COLUMN sys_dept.dept_id IS '部门ID';
COMMENT ON COLUMN sys_dept.name IS '部门名称';
COMMENT ON COLUMN sys_dept.sort_order IS '排序';
COMMENT ON COLUMN sys_dept.create_by IS '创建人';
COMMENT ON COLUMN sys_dept.update_by IS '修改人';
COMMENT ON COLUMN sys_dept.create_time IS '创建时间';
COMMENT ON COLUMN sys_dept.update_time IS '修改时间';
COMMENT ON COLUMN sys_dept.del_flag IS '删除标志';
COMMENT ON COLUMN sys_dept.parent_id IS '父级部门ID';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO sys_dept VALUES (1, '总裁办', 1, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:07:49', '0', 0);
INSERT INTO sys_dept VALUES (2, '技术部', 2, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 1);
INSERT INTO sys_dept VALUES (3, '市场部', 3, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 1);
INSERT INTO sys_dept VALUES (4, '销售部', 4, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 1);
INSERT INTO sys_dept VALUES (5, '财务部', 5, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 1);
INSERT INTO sys_dept VALUES (6, '人事行政部', 6, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:53:36', '1', 1);
INSERT INTO sys_dept VALUES (7, '研发部', 7, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 2);
INSERT INTO sys_dept VALUES (8, 'UI设计部', 11, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 7);
INSERT INTO sys_dept VALUES (9, '产品部', 12, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 2);
INSERT INTO sys_dept VALUES (10, '渠道部', 13, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 3);
INSERT INTO sys_dept VALUES (11, '推广部', 14, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 3);
INSERT INTO sys_dept VALUES (12, '客服部', 15, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 4);
INSERT INTO sys_dept VALUES (13, '财务会计部', 16, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 5);
INSERT INTO sys_dept VALUES (14, '审计风控部', 17, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 14:06:57', '0', 5);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
CREATE TABLE sys_dict (
    id BIGINT NOT NULL,
    dict_type VARCHAR(100) DEFAULT NULL,
    description VARCHAR(100) DEFAULT NULL,
    create_by VARCHAR(64) NOT NULL DEFAULT ' ',
    update_by VARCHAR(64) NOT NULL DEFAULT ' ',
    create_time TIMESTAMP DEFAULT NULL,
    update_time TIMESTAMP DEFAULT NULL,
    remarks VARCHAR(255) DEFAULT NULL,
    system_flag CHAR(1) DEFAULT '0',
    del_flag CHAR(1) DEFAULT '0',
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_dict IS '字典表';
COMMENT ON COLUMN sys_dict.id IS '编号';
COMMENT ON COLUMN sys_dict.dict_type IS '字典类型';
COMMENT ON COLUMN sys_dict.description IS '描述';
COMMENT ON COLUMN sys_dict.create_by IS '创建人';
COMMENT ON COLUMN sys_dict.update_by IS '修改人';
COMMENT ON COLUMN sys_dict.create_time IS '创建时间';
COMMENT ON COLUMN sys_dict.update_time IS '更新时间';
COMMENT ON COLUMN sys_dict.remarks IS '备注信息';
COMMENT ON COLUMN sys_dict.system_flag IS '系统标志';
COMMENT ON COLUMN sys_dict.del_flag IS '删除标志';

-- 创建索引
CREATE INDEX sys_dict_del_flag ON sys_dict(del_flag);

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO sys_dict VALUES (1, 'log_type', '日志类型', ' ', ' ', '2019-03-19 11:06:44', '2019-03-19 11:06:44', '异常、正常', '1', '0');
INSERT INTO sys_dict VALUES (2, 'social_type', '社交登录', ' ', ' ', '2019-03-19 11:09:44', '2019-03-19 11:09:44', '微信、QQ', '1', '0');
INSERT INTO sys_dict VALUES (3, 'job_type', '定时任务类型', ' ', ' ', '2019-03-19 11:22:21', '2019-03-19 11:22:21', 'quartz', '1', '0');
INSERT INTO sys_dict VALUES (4, 'job_status', '定时任务状态', ' ', ' ', '2019-03-19 11:24:57', '2019-03-19 11:24:57', '发布状态、运行状态', '1', '0');
INSERT INTO sys_dict VALUES (5, 'job_execute_status', '定时任务执行状态', ' ', ' ', '2019-03-19 11:26:15', '2019-03-19 11:26:15', '正常、异常', '1', '0');
INSERT INTO sys_dict VALUES (6, 'misfire_policy', '定时任务错失执行策略', ' ', ' ', '2019-03-19 11:27:19', '2019-03-19 11:27:19', '周期', '1', '0');
INSERT INTO sys_dict VALUES (7, 'gender', '性别', ' ', ' ', '2019-03-27 13:44:06', '2019-03-27 13:44:06', '微信用户性别', '1', '0');
INSERT INTO sys_dict VALUES (8, 'subscribe', '订阅状态', ' ', ' ', '2019-03-27 13:48:33', '2019-03-27 13:48:33', '公众号订阅状态', '1', '0');
INSERT INTO sys_dict VALUES (9, 'response_type', '回复', ' ', ' ', '2019-03-28 21:29:21', '2019-03-28 21:29:21', '微信消息是否已回复', '1', '0');
INSERT INTO sys_dict VALUES (10, 'param_type', '参数配置', ' ', ' ', '2019-04-29 18:20:47', '2019-04-29 18:20:47', '检索、原文、报表、安全、文档、消息、其他', '1', '0');
INSERT INTO sys_dict VALUES (11, 'status_type', '租户状态', ' ', ' ', '2019-05-15 16:31:08', '2019-05-15 16:31:08', '租户状态', '1', '0');
INSERT INTO sys_dict VALUES (12, 'dict_type', '字典类型', ' ', ' ', '2019-05-16 14:16:20', '2019-05-16 14:20:16', '系统类不能修改', '1', '0');
INSERT INTO sys_dict VALUES (13, 'channel_type', '支付类型', ' ', ' ', '2019-05-16 14:16:20', '2019-05-16 14:20:16', '系统类不能修改', '1', '0');
INSERT INTO sys_dict VALUES (14, 'grant_types', '授权类型', ' ', ' ', '2019-08-13 07:34:10', '2019-08-13 07:34:10', NULL, '1', '0');
INSERT INTO sys_dict VALUES (15, 'style_type', '前端风格', ' ', ' ', '2020-02-07 03:49:28', '2020-02-07 03:50:40', '0-Avue 1-element', '1', '0');
INSERT INTO sys_dict VALUES (16, 'captcha_flag_types', '验证码开关', ' ', ' ', '2020-11-18 06:53:25', '2020-11-18 06:53:25', '是否校验验证码', '1', '0');
INSERT INTO sys_dict VALUES (17, 'enc_flag_types', '前端密码加密', ' ', ' ', '2020-11-18 06:54:44', '2020-11-18 06:54:44', '前端密码是否加密传输', '1', '0');
INSERT INTO sys_dict VALUES (18, 'lock_flag', '用户状态', 'admin', ' ', '2023-02-01 16:55:31', NULL, NULL, '1', '0');
INSERT INTO sys_dict VALUES (19, 'ds_config_type', '数据连接类型', 'admin', ' ', '2023-02-06 18:36:59', NULL, NULL, '1', '0');
INSERT INTO sys_dict VALUES (20, 'common_status', '通用状态', 'admin', ' ', '2023-02-09 11:02:08', NULL, NULL, '1', '0');
INSERT INTO sys_dict VALUES (21, 'app_social_type', 'app社交登录', 'admin', ' ', '2023-02-10 11:11:06', NULL, 'app社交登录', '1', '0');
INSERT INTO sys_dict VALUES (22, 'yes_no_type', '是否', 'admin', ' ', '2023-02-20 23:25:04', NULL, NULL, '1', '0');
INSERT INTO sys_dict VALUES (23, 'repType', '微信消息类型', 'admin', ' ', '2023-02-24 15:08:25', NULL, NULL, '0', '0');
INSERT INTO sys_dict VALUES (24, 'leave_status', '请假状态', 'admin', ' ', '2023-03-02 22:50:15', NULL, NULL, '0', '0');
INSERT INTO sys_dict VALUES (25, 'schedule_type', '日程类型', 'admin', ' ', '2023-03-06 14:49:18', NULL, NULL, '0', '0');
INSERT INTO sys_dict VALUES (26, 'schedule_status', '日程状态', 'admin', ' ', '2023-03-06 14:52:57', NULL, NULL, '0', '0');
INSERT INTO sys_dict VALUES (27, 'ds_type', '代码生成器支持的数据库类型', 'admin', ' ', '2023-03-12 09:57:59', NULL, NULL, '1', '0');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
CREATE TABLE sys_user (
    user_id BIGINT NOT NULL,
    username VARCHAR(64) NOT NULL,
    password VARCHAR(255) NOT NULL,
    salt VARCHAR(255) DEFAULT NULL,
    phone VARCHAR(20) DEFAULT NULL,
    avatar VARCHAR(255) DEFAULT NULL,
    dept_id BIGINT DEFAULT NULL,
    lock_flag CHAR(1) DEFAULT '0',
    del_flag CHAR(1) DEFAULT '0',
    create_time TIMESTAMP DEFAULT NULL,
    update_time TIMESTAMP DEFAULT NULL,
    create_by VARCHAR(64) DEFAULT NULL,
    update_by VARCHAR(64) DEFAULT NULL,
    PRIMARY KEY (user_id),
    UNIQUE (username)
);

COMMENT ON TABLE sys_user IS '用户表';
COMMENT ON COLUMN sys_user.user_id IS '主键ID';
COMMENT ON COLUMN sys_user.username IS '用户名';
COMMENT ON COLUMN sys_user.password IS '密码';
COMMENT ON COLUMN sys_user.salt IS '随机盐';
COMMENT ON COLUMN sys_user.phone IS '简介';
COMMENT ON COLUMN sys_user.avatar IS '头像';
COMMENT ON COLUMN sys_user.dept_id IS '部门ID';
COMMENT ON COLUMN sys_user.lock_flag IS '锁定标记';
COMMENT ON COLUMN sys_user.del_flag IS '删除标记';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
COMMENT ON COLUMN sys_user.create_by IS '创建者';
COMMENT ON COLUMN sys_user.update_by IS '更新者';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO sys_user VALUES (1, 'admin', '$2a$10$RpFJjxYiXdEsAGnWp/8fsOetMuOON96Ntk/Ym2M/RKRyU0GZseaDC', NULL, '17034642888', '', 1, '0', '0', '2018-04-20 07:15:18', '2019-01-31 14:29:07', '', 'admin');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
CREATE TABLE sys_role (
    role_id BIGINT NOT NULL,
    role_name VARCHAR(64) NOT NULL,
    role_code VARCHAR(64) NOT NULL,
    role_desc VARCHAR(255) DEFAULT NULL,
    ds_type CHAR(1) DEFAULT '2',
    ds_scope VARCHAR(255) DEFAULT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT NULL,
    del_flag CHAR(1) DEFAULT '0',
    tenant_id BIGINT DEFAULT 0,
    PRIMARY KEY (role_id),
    UNIQUE (role_code)
);

COMMENT ON TABLE sys_role IS '系统角色表';
COMMENT ON COLUMN sys_role.role_id IS '角色ID';
COMMENT ON COLUMN sys_role.role_name IS '角色名称';
COMMENT ON COLUMN sys_role.role_code IS '角色编码';
COMMENT ON COLUMN sys_role.role_desc IS '角色描述';
COMMENT ON COLUMN sys_role.ds_type IS '数据权限类型';
COMMENT ON COLUMN sys_role.ds_scope IS '数据权限范围';
COMMENT ON COLUMN sys_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_role.update_time IS '更新时间';
COMMENT ON COLUMN sys_role.del_flag IS '删除标识';
COMMENT ON COLUMN sys_role.tenant_id IS '租户ID';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO sys_role VALUES (1, '管理员', 'ROLE_ADMIN', '管理员', '2', NULL, '2017-10-29 15:45:51', '2018-12-26 14:09:11', '0', 0);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
CREATE TABLE sys_menu (
    menu_id BIGINT NOT NULL,
    name VARCHAR(32) NOT NULL,
    permission VARCHAR(32) DEFAULT NULL,
    path VARCHAR(128) DEFAULT NULL,
    parent_id BIGINT DEFAULT NULL,
    icon VARCHAR(32) DEFAULT NULL,
    sort_order INTEGER DEFAULT NULL,
    keep_alive CHAR(1) DEFAULT NULL,
    type CHAR(1) DEFAULT NULL,
    create_time TIMESTAMP DEFAULT NULL,
    update_time TIMESTAMP DEFAULT NULL,
    del_flag CHAR(1) DEFAULT '0',
    PRIMARY KEY (menu_id)
);

COMMENT ON TABLE sys_menu IS '菜单权限表';
COMMENT ON COLUMN sys_menu.menu_id IS '菜单ID';
COMMENT ON COLUMN sys_menu.name IS '菜单名称';
COMMENT ON COLUMN sys_menu.permission IS '菜单权限标识';
COMMENT ON COLUMN sys_menu.path IS '前端URL';
COMMENT ON COLUMN sys_menu.parent_id IS '父菜单ID';
COMMENT ON COLUMN sys_menu.icon IS '图标';
COMMENT ON COLUMN sys_menu.sort_order IS '排序值';
COMMENT ON COLUMN sys_menu.keep_alive IS '0-开启，1- 关闭';
COMMENT ON COLUMN sys_menu.type IS '菜单类型';
COMMENT ON COLUMN sys_menu.create_time IS '创建时间';
COMMENT ON COLUMN sys_menu.update_time IS '更新时间';
COMMENT ON COLUMN sys_menu.del_flag IS '删除标识';

-- 创建索引
CREATE INDEX sys_menu_parent_id ON sys_menu(parent_id);

-- 这里只插入部分关键菜单，完整菜单数据请参考原MySQL文件
INSERT INTO sys_menu VALUES (1000, '权限管理', NULL, '/admin', -1, 'icon-quanxianguanli', 8, '1', '0', '2018-09-28 08:29:53', '2020-03-11 23:58:18', '0');
INSERT INTO sys_menu VALUES (1100, '用户管理', NULL, '/admin/user/index', 1000, 'icon-yonghuguanli', 9, '1', '1', '2017-11-02 22:24:37', '2020-03-12 00:04:40', '0');
INSERT INTO sys_menu VALUES (1101, '用户新增', NULL, NULL, 1100, '1', 1, '0', NULL, '1', '2018-05-15 21:35:18', '2019-05-25 06:48:34', '0');
INSERT INTO sys_menu VALUES (1102, '用户修改', NULL, NULL, 1100, '1', 1, '0', NULL, '1', '2018-05-15 21:35:18', '2019-05-25 06:48:34', '0');
INSERT INTO sys_menu VALUES (1103, '用户删除', NULL, NULL, 1100, '1', 1, '0', NULL, '1', '2018-05-15 21:35:18', '2019-05-25 06:48:34', '0');
INSERT INTO sys_menu VALUES (1200, '菜单管理', NULL, '/admin/menu/index', 1000, 'icon-caidanguanli', 10, '1', '1', '2017-11-08 09:29:26', '2020-03-12 00:04:40', '0');
INSERT INTO sys_menu VALUES (1201, '菜单新增', NULL, NULL, 1200, '1', 1, '0', NULL, '1', '2018-05-15 21:35:18', '2019-05-25 06:48:34', '0');
INSERT INTO sys_menu VALUES (1202, '菜单修改', NULL, NULL, 1200, '1', 1, '0', NULL, '1', '2018-05-15 21:35:18', '2019-05-25 06:48:34', '0');
INSERT INTO sys_menu VALUES (1203, '菜单删除', NULL, NULL, 1200, '1', 1, '0', NULL, '1', '2018-05-15 21:35:18', '2019-05-25 06:48:34', '0');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

COMMENT ON TABLE sys_user_role IS '用户角色表';
COMMENT ON COLUMN sys_user_role.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_role.role_id IS '角色ID';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO sys_user_role VALUES (1, 1);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, menu_id)
);

COMMENT ON TABLE sys_role_menu IS '角色菜单表';
COMMENT ON COLUMN sys_role_menu.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_menu.menu_id IS '菜单ID';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO sys_role_menu VALUES (1, 1000);
INSERT INTO sys_role_menu VALUES (1, 1100);
INSERT INTO sys_role_menu VALUES (1, 1101);
INSERT INTO sys_role_menu VALUES (1, 1102);
INSERT INTO sys_role_menu VALUES (1, 1103);
INSERT INTO sys_role_menu VALUES (1, 1200);
INSERT INTO sys_role_menu VALUES (1, 1201);
INSERT INTO sys_role_menu VALUES (1, 1202);
INSERT INTO sys_role_menu VALUES (1, 1203);

-- 创建序列用于自增ID
CREATE SEQUENCE IF NOT EXISTS sys_dept_seq START 15;
CREATE SEQUENCE IF NOT EXISTS sys_dict_seq START 28;
CREATE SEQUENCE IF NOT EXISTS sys_user_seq START 2;
CREATE SEQUENCE IF NOT EXISTS sys_role_seq START 2;
CREATE SEQUENCE IF NOT EXISTS sys_menu_seq START 3000;

-- 设置序列的默认值
ALTER TABLE sys_dept ALTER COLUMN dept_id SET DEFAULT nextval('sys_dept_seq');
ALTER TABLE sys_dict ALTER COLUMN id SET DEFAULT nextval('sys_dict_seq');
ALTER TABLE sys_user ALTER COLUMN user_id SET DEFAULT nextval('sys_user_seq');
ALTER TABLE sys_role ALTER COLUMN role_id SET DEFAULT nextval('sys_role_seq');
ALTER TABLE sys_menu ALTER COLUMN menu_id SET DEFAULT nextval('sys_menu_seq');

COMMIT;
