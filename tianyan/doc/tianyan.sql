SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `sys_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  `parent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='部门管理';

-- ----------------------------
--  Records of `sys_dept`
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES ('1', '中国支付通集团', null, '2018-01-22 19:00:23', '2018-01-23 12:40:46', '0', '0'), ('3', '客乐芙信息技术有限公司', null, '2018-01-22 19:00:44', '2018-01-23 12:40:56', '0', '1'), ('4', '技术研发部', null, '2018-01-22 19:00:52', '2018-01-23 12:41:11', '0', '3');
COMMIT;

-- ----------------------------
--  Table structure for `sys_dept_relation`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_relation`;
CREATE TABLE `sys_dept_relation` (
  `ancestor` int(11) NOT NULL COMMENT '祖先节点',
  `descendant` int(11) NOT NULL COMMENT '后代节点',
  PRIMARY KEY (`ancestor`,`descendant`),
  KEY `idx1` (`ancestor`),
  KEY `idx2` (`descendant`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Records of `sys_dept_relation`
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept_relation` VALUES ('1', '1'), ('1', '3'), ('1', '4');
COMMIT;

-- ----------------------------
--  Table structure for `sys_dict`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `value` varchar(100) NOT NULL COMMENT '数据值',
  `label` varchar(100) NOT NULL COMMENT '标签名',
  `type` varchar(100) NOT NULL COMMENT '类型',
  `description` varchar(100) NOT NULL COMMENT '描述',
  `sort` decimal(10,0) NOT NULL COMMENT '排序（升序）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_dict_value` (`value`),
  KEY `sys_dict_label` (`label`),
  KEY `sys_dict_del_flag` (`del_flag`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='字典表';

-- ----------------------------
--  Records of `sys_dict`
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` VALUES ('2', '9', '异常', 'log_type', '日志异常', '1', '2017-12-28 13:06:39', '2018-01-06 10:54:41', null, '0'), ('3', '0', '正常', 'log_type', '正常', '1', '2018-05-11 23:52:57', '2018-05-11 23:52:57', '123', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `type` char(1) DEFAULT '1' COMMENT '日志类型',
  `title` varchar(255) DEFAULT '' COMMENT '日志标题',
  `service_id` varchar(32) DEFAULT NULL COMMENT '服务ID',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remote_addr` varchar(255) DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(1000) DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',
  `method` varchar(10) DEFAULT NULL COMMENT '操作方式',
  `params` text COMMENT '操作提交的数据',
  `time` mediumtext COMMENT '执行时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记',
  `exception` text COMMENT '异常信息',
  PRIMARY KEY (`id`),
  KEY `sys_log_create_by` (`create_by`),
  KEY `sys_log_request_uri` (`request_uri`),
  KEY `sys_log_type` (`type`),
  KEY `sys_log_create_date` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='日志表';

-- ----------------------------
--  Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID' FIRST,
  `name` varchar(32) NOT NULL COMMENT '菜单名称',
  `permission` varchar(32) DEFAULT NULL COMMENT '菜单权限标识',
  `path` varchar(128) DEFAULT NULL COMMENT '前端URL',
  `parent_id` int(11) DEFAULT NULL COMMENT '父菜单ID',
  `icon` varchar(32) DEFAULT NULL COMMENT '图标',
  `component` varchar(64) DEFAULT NULL COMMENT 'VUE页面',
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `type` char(1) DEFAULT NULL COMMENT '菜单类型 （0菜单 1按钮）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '0--正常 1--删除',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';

-- ----------------------------
--  Records of `sys_menu`
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES ('1', '系统管理', null, '/admin', '-1', 'icon-xitongguanli', 'Layout', '1', '0', '2017-11-07 20:56:00', '2018-05-14 21:53:22', '0'), ('2', '用户管理', null, 'user', '1', 'icon-yonghuguanli', 'views/admin/user/index', '2', '0', '2017-11-02 22:24:37', '2018-05-14 22:11:35', '0'), ('3', '菜单管理', null, 'menu', '1', 'icon-caidanguanli', 'views/admin/menu/index', '3', '0', '2017-11-08 09:57:27', '2018-05-14 22:11:21', '0'), ('4', '角色管理', null, 'role', '1', 'icon-jiaoseguanli', 'views/admin/role/index', '4', '0', '2017-11-08 10:13:37', '2018-05-14 22:11:19', '0'), ('5', '日志管理', null, 'log', '1', 'icon-rizhiguanli', 'views/admin/log/index', '5', '0', '2017-11-20 14:06:22', '2018-05-14 22:11:18', '0'), ('6', '字典管理', null, 'dict', '1', 'icon-zygl', 'views/admin/dict/index', '6', '0', '2017-11-29 11:30:52', '2018-05-14 22:12:48', '0'), ('7', '部门管理', null, 'dept', '1', 'icon-iconbmgl', 'views/admin/dept/index', '7', '0', '2018-01-20 13:17:19', '2018-05-14 22:11:16', '0'), ('8', '系统监控', null, '', '-1', 'icon-iconbmgl', null, '8', '0', '2018-01-22 12:30:41', '2018-06-24 17:10:11', '0'), ('9', '服务监控', null, 'http://monitor.clf-d.com', '8', 'icon-jiankong', null, '9', '0', '2018-01-23 10:53:33', '2018-06-24 17:10:12', '0'), ('10', 'zipkin监控', null, 'http://zipkin.clf-d.com', '8', 'icon-jiankong', null, '11', '0', '2018-01-23 10:55:18', '2018-06-24 17:10:12', '0'), ('11', 'pinpoint监控', null, 'https://pinpoint.clf-d.com', '8', 'icon-xiazaihuancun', null, '10', '0', '2018-01-25 11:08:52', '2018-06-24 17:10:12', '0'), ('12', '缓存状态', null, 'http://cachecloud.clf-d.com', '8', 'icon-ecs-status', null, '12', '0', '2018-01-23 10:56:11', '2018-06-24 17:10:12', '0'), ('13', 'ELK状态', null, 'http://elk.clf-d.com', '8', 'icon-ecs-status', null, '13', '0', '2018-01-23 10:55:47', '2018-06-24 17:10:12', '0'), ('14', '接口文档', null, 'http://swagger.clf-d.com/swagger-ui.html', '8', 'icon-wendangdocument72', null, '14', '0', '2018-01-23 10:56:43', '2018-06-24 17:10:12', '0'), ('15', '任务监控', null, 'http://task.clf-d.com', '8', 'icon-jiankong', null, '15', '0', '2018-01-23 10:55:18', '2018-06-24 17:10:12', '0'), ('22', '用户新增', 'sys_user_add', null, '2', null, null, null, '1', '2017-11-08 09:52:09', '2017-12-04 16:31:10', '0'), ('23', '用户修改', 'sys_user_edit', null, '2', null, null, null, '1', '2017-11-08 09:52:48', '2018-06-26 21:16:57', '0'), ('24', '用户删除', 'sys_user_del', null, '2', null, null, null, '1', '2017-11-08 09:54:01', '2017-12-04 16:31:18', '0'), ('32', '菜单新增', 'sys_menu_add', null, '3', null, null, null, '1', '2017-11-08 10:15:53', '2018-01-20 14:37:50', '0'), ('33', '菜单修改', 'sys_menu_edit', null, '3', null, null, null, '1', '2017-11-08 10:16:23', '2018-01-20 14:37:56', '0'), ('34', '菜单删除', 'sys_menu_del', null, '3', null, null, null, '1', '2017-11-08 10:16:43', '2018-01-20 14:38:03', '0'), ('42', '角色新增', 'sys_role_add', null, '4', null, null, null, '1', '2017-11-08 10:14:18', '2018-04-20 07:21:38', '0'), ('43', '角色修改', 'sys_role_edit', null, '4', null, null, null, '1', '2017-11-08 10:14:41', '2018-04-20 07:21:50', '0'), ('44', '角色删除', 'sys_role_del', null, '4', null, null, null, '1', '2017-11-08 10:14:59', '2018-04-20 07:22:00', '0'), ('45', '分配权限', 'sys_role_perm', null, '4', null, null, null, '1', '2018-04-20 07:22:55', '2018-04-20 07:24:40', '0'), ('52', '日志删除', 'sys_log_del', null, '5', null, null, null, '1', '2017-11-20 20:37:37', '2017-11-28 17:36:52', '0'), ('62', '字典删除', 'sys_dict_del', null, '6', null, null, null, '1', '2017-11-29 11:30:11', '2018-01-07 15:40:51', '0'), ('63', '字典新增', 'sys_dict_add', null, '6', null, null, null, '1', '2018-05-11 22:34:55', null, '0'), ('64', '字典修改', 'sys_dict_edit', null, '6', null, null, null, '1', '2018-05-11 22:36:03', '2018-06-26 21:17:36', '0'), ('72', '部门新增', 'sys_dept_add', null, '7', null, null, null, '1', '2018-01-20 14:56:16', '2018-01-20 21:17:57', '0'), ('73', '部门修改', 'sys_dept_edit', null, '7', null, null, null, '1', '2018-01-20 14:56:59', '2018-01-20 21:17:59', '0'), ('74', '部门删除', 'sys_dept_del', null, '7', null, null, null, '1', '2018-01-20 14:57:28', '2018-01-20 21:18:05', '0'), ('100', '客户端管理', '', 'client', '1', 'icon-bangzhushouji', 'views/admin/client/index', '10', '0', '2018-01-20 13:17:19', '2018-05-15 21:28:10', '0'), ('101', '客户端新增', 'sys_client_add', null, '100', '1', null, null, '1', '2018-05-15 21:35:18', '2018-05-16 10:35:26', '0'), ('102', '客户端修改', 'sys_client_edit', null, '100', null, null, null, '1', '2018-05-15 21:37:06', '2018-06-26 21:17:17', '0'), ('103', '客户端删除', 'sys_client_del', null, '100', null, null, null, '1', '2018-05-15 21:39:16', '2018-05-15 21:52:34', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_oauth_client_details`
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details` (
  `client_id` varchar(32) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Records of `sys_oauth_client_details`
-- ----------------------------
BEGIN;
INSERT INTO `sys_oauth_client_details` VALUES ('app', null, 'app', 'server', 'password,refresh_token', null, null, null, null, null, 'true'), ('pig', null, 'pig', 'server', 'password,refresh_token', null, null, null, null, null, 'false');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `role_code` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `role_desc` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '删除标识（0-正常,1-删除）',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_idx1_role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Records of `sys_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('1', 'admin', 'ROLE_ADMIN', '超级管理员', '2017-10-29 15:45:51', '2018-04-22 11:40:29', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `role_id` int(20) DEFAULT NULL COMMENT '角色ID',
  `dept_id` int(20) DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色与部门对应关系';

-- ----------------------------
--  Records of `sys_role_dept`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_dept` VALUES ('11', '1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单表';

-- ----------------------------
--  Records of `sys_role_menu`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES ('1', '1'), ('1', '2'), ('1', '3'), ('1', '4'), ('1', '5'), ('1', '6'), ('1', '7'), ('1', '22'), ('1', '23'), ('1', '24'), ('1', '32'), ('1', '33'), ('1', '34'), ('1', '42'), ('1', '43'), ('1', '44'), ('1', '45'), ('1', '52'), ('1', '62'), ('1', '63'), ('1', '64'), ('1', '72'), ('1', '73'), ('1', '74'), ('1', '100'), ('1', '101'), ('1', '102'), ('1', '103'), ('1', '200');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `salt` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '随机盐',
  `phone` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '手机号码',
  `email` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0-正常，1-删除',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_idx1_username` (`username`),
  UNIQUE KEY `user_idx2_phone` (`phone`)
  UNIQUE KEY `user_idx2_phone` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
--  Records of `sys_user`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1', 'admin', '$2a$10$vg5QNHhCknAqevx9vM2s5esllJEzF/pa8VZXtFYHhhOhUcCw/GWyS', null, '15921137075', null, '1', '2018-04-20 07:15:18', '2018-06-26 11:43:22', '0'), ('4', 'tianyan', '$2a$10$vg5QNHhCknAqevx9vM2s5esllJEzF/pa8VZXtFYHhhOhUcCw/GWyS', null, '13837477152', null, '1', '2018-04-22 11:39:07', '2018-05-10 18:01:11', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

-- ----------------------------
--  Records of `sys_user_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES ('1', '1'), ('4', '1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
