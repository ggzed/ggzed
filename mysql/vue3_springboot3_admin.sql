SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
                              `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `menu_id` int(11) NULL DEFAULT NULL COMMENT '菜单Id',
                              `function_author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '作者',
                              `table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库表名',
                              `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据库表描述',
                              `component_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '前端 component 名(根据数据库表名生成)',
                              `class_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类名(根据数据库表名生成)',
                              `back_end_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'mybatis-plus' COMMENT '后端生成类型',
                              `front_end_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'vue' COMMENT '前端生成类型',
                              `package_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'yf' COMMENT '主包名',
                              `module_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '模块名（外层包名）',
                              `business_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务名（内层包名）',
                              `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                              `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '生成代码-数据库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_fields
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_fields`;
CREATE TABLE `gen_table_fields`  (
                                     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                     `table_id` int(11) NOT NULL COMMENT '所属表ID',
                                     `show_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表格展示列名',
                                     `column_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库列名',
                                     `column_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库类型',
                                     `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '数据库字段描述',
                                     `java_ts_field_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'JAVA字段名',
                                     `java_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'JAVA类型',
                                     `ts_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ts类型',
                                     `is_pk` tinyint(1) NULL DEFAULT 0 COMMENT '是否主键（1:是,0:否）',
                                     `is_increment` tinyint(1) NULL DEFAULT 0 COMMENT '是否自增（1:是,0:否）',
                                     `is_required` tinyint(1) NULL DEFAULT 0 COMMENT '是否必填（1:是,0:否）',
                                     `is_show` tinyint(1) NULL DEFAULT 0 COMMENT '是否展示字段（1:是,0:否）',
                                     `is_query` tinyint(1) NULL DEFAULT 0 COMMENT '是否查询字段（1:是,0:否）',
                                     `is_form` tinyint(1) NULL DEFAULT NULL COMMENT '是否表单字段( 1:是 , 0:否  )',
                                     `query_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '查询方式（等于、不等于、大于、小于、模糊、范围）',
                                     `query_form_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '查询类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
                                     `show_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '展示类型 ( JSON、文本、Markdown、Tag、图片... )',
                                     `save_form_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '表单类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
                                     `dict_type_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
                                     `sort` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '排序',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `idx_gen_table_id`(`table_id`) USING BTREE COMMENT 'gen_table 外键'
) ENGINE = InnoDB AUTO_INCREMENT = 221 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '生成代码-数据库表字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table_fields
-- ----------------------------

-- ----------------------------
-- Table structure for message_notification
-- ----------------------------
DROP TABLE IF EXISTS `message_notification`;
CREATE TABLE `message_notification`  (
                                         `id` bigint(20) NOT NULL COMMENT '主键',
                                         `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息标题',
                                         `sender_id` bigint(20) NOT NULL COMMENT '发送人',
                                         `receiver_id` bigint(20) NOT NULL COMMENT '接收人',
                                         `message_status` tinyint(1) UNSIGNED NOT NULL COMMENT '消息状态(\'未读\', \'已读\', \'已删除\', \'已忽略\')',
                                         `message_template_id` int(11) NOT NULL COMMENT '消息模板ID',
                                         `dynamic_title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '动态标题',
                                         `dynamic_subject` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '动态主题',
                                         `dynamic_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '动态内容',
                                         `scheduled_send_time` datetime(0) NULL DEFAULT NULL COMMENT '预定发送时间 ( 仅用于定时发送 )',
                                         `actual_send_time` datetime(0) NULL DEFAULT NULL COMMENT '实际发送时间',
                                         `push_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '消息推送状态(\'未推送\', \'已推送\', \'推送中\', \'推送失败\')',
                                         `retry_count` tinyint(1) NULL DEFAULT 0 COMMENT '重试次数',
                                         `error_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '发送错误原因',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         INDEX `idx_receiver_id`(`receiver_id`) USING BTREE COMMENT '按接收者 ID 查询通知的索引',
                                         INDEX `idx_message_template_id`(`message_template_id`) USING BTREE COMMENT '按模板 ID 查询的索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息通知' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_notification
-- ----------------------------

-- ----------------------------
-- Table structure for message_template
-- ----------------------------
DROP TABLE IF EXISTS `message_template`;
CREATE TABLE `message_template`  (
                                     `id` int(11) NOT NULL COMMENT '主键',
                                     `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名',
                                     `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息标题',
                                     `subject` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主体',
                                     `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息内容格式',
                                     `display_type` tinyint(1) NULL DEFAULT NULL COMMENT '消息展示类型 ( \'MessageBox\', \'Drawer\', \'Notification\' ) ',
                                     `language` tinyint(1) NULL DEFAULT 0 COMMENT '消息语言（\'en\'）',
                                     `type` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0 COMMENT '模板类型 ( \'System\' , \'SMS\' , \'Email\',\'QQBot\',\'WechatBot\')',
                                     `status` tinyint(1) NULL DEFAULT 1 COMMENT '角色状态(1-正常；0-停用)',
                                     `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标识(0:未删除;1:已删除)',
                                     `create_by` bigint(20) NOT NULL COMMENT '创建人',
                                     `create_time` datetime(0) NOT NULL COMMENT '创建时间',
                                     `update_by` bigint(20) NOT NULL COMMENT '修改人Id',
                                     `update_time` datetime(0) NOT NULL COMMENT '修改时间',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_template
-- ----------------------------

-- ----------------------------
-- Table structure for operate_log
-- ----------------------------
DROP TABLE IF EXISTS `operate_log`;
CREATE TABLE `operate_log`  (
                                `id` bigint(20) NOT NULL COMMENT '日志主键',
                                `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '模块标题',
                                `business_type` tinyint(1) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除 ...）',
                                `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '方法名称',
                                `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求方式',
                                `operator_type` tinyint(1) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
                                `operator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作人员',
                                `operator_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求URL',
                                `operator_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '主机地址',
                                `operator_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作地点',
                                `operator_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求参数',
                                `operator_browser` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器信息',
                                `operator_os` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作系统',
                                `create_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
                                `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '返回参数',
                                `status` tinyint(1) NULL DEFAULT 0 COMMENT '操作状态（1正常 ,0异常）',
                                `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '错误消息',
                                `cost_time` bigint(20) NULL DEFAULT 0 COMMENT '消耗时间',
                                PRIMARY KEY (`id`) USING BTREE,
                                INDEX `idx_log_create_time_desc`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operate_log
-- ----------------------------

-- ----------------------------
-- Table structure for socket_message
-- ----------------------------
DROP TABLE IF EXISTS `socket_message`;
CREATE TABLE `socket_message`  (
                                   `id` bigint(20) NOT NULL COMMENT '主键',
                                   `sender_id` bigint(20) NOT NULL COMMENT '发送消息的用户id',
                                   `receiver_id` bigint(20) NOT NULL COMMENT '接收消息的用户id',
                                   `channel` tinyint(2) NULL DEFAULT NULL COMMENT '消息所在频道(0:系统频道,1:公共频道...)',
                                   `message_provider` tinyint(2) NOT NULL COMMENT '消息提供者(0:user,1:system,2:ai....)',
                                   `service_provider` tinyint(4) NULL DEFAULT NULL COMMENT '服务提供者(0:chat_room,1:data_dashboard...)',
                                   `is_read` tinyint(1) NULL DEFAULT 0 COMMENT '是否读取(1：是 , 0 : 否)',
                                   `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息内容',
                                   `is_send` tinyint(1) NULL DEFAULT 0 COMMENT '是否发送(1: 是，0：否)',
                                   `error_msg` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误信息',
                                   `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '发送时间',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `idx_sender_receiver_time`(`sender_id`, `receiver_id`, `create_time`) USING BTREE COMMENT '聚合索引  sender  receiver time ',
                                   INDEX `idx_receiver_sender_time`(`receiver_id`, `sender_id`, `create_time`) USING BTREE COMMENT '聚合索引 receiver  sender time '
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'websocket消息记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of socket_message
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
                             `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '部门名称',
                             `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '父节点id',
                             `tree_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '父节点id路径',
                             `sort` tinyint(1) NULL DEFAULT 0 COMMENT '显示顺序',
                             `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态(1:正常;0:禁用)',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 'YF-深圳总部', 0, '0', 1, 1, '2024-12-18 23:09:41', '2024-12-18 23:21:37');
INSERT INTO `sys_dept` VALUES (4, '技术研发部', 1, '0,1', 1, 1, '2024-12-18 23:17:41', '2024-12-18 23:17:41');
INSERT INTO `sys_dept` VALUES (5, '技术研发部负责人', 4, '0,1,4', 0, 1, '2024-12-18 23:18:09', '2024-12-18 23:25:12');
INSERT INTO `sys_dept` VALUES (6, '后端开发', 4, '0,1,4', 1, 1, '2024-12-18 23:18:42', '2024-12-18 23:18:56');
INSERT INTO `sys_dept` VALUES (7, '前端开发', 4, '0,1,4', 1, 1, '2024-12-18 23:18:50', '2024-12-18 23:18:50');
INSERT INTO `sys_dept` VALUES (8, '移动端开发', 4, '0,1,4', 1, 1, '2024-12-18 23:19:13', '2024-12-18 23:19:13');
INSERT INTO `sys_dept` VALUES (9, '测试', 4, '0,1,4', 1, 1, '2024-12-18 23:19:22', '2024-12-18 23:19:22');
INSERT INTO `sys_dept` VALUES (10, '运维', 4, '0,1,4', 1, 1, '2024-12-18 23:19:28', '2024-12-18 23:19:28');
INSERT INTO `sys_dept` VALUES (12, '产品与项目部', 1, '0,1', 1, 1, '2024-12-18 23:22:07', '2024-12-18 23:22:07');
INSERT INTO `sys_dept` VALUES (13, '产品经理', 12, '0,1,12', 1, 1, '2024-12-18 23:22:13', '2024-12-18 23:22:13');
INSERT INTO `sys_dept` VALUES (14, '项目经理', 12, '0,1,12', 1, 1, '2024-12-18 23:22:22', '2024-12-18 23:22:22');
INSERT INTO `sys_dept` VALUES (15, 'UI/UX设计师', 12, '0,1,12', 1, 1, '2024-12-18 23:22:30', '2024-12-18 23:22:30');
INSERT INTO `sys_dept` VALUES (16, '市场与销售部', 1, '0,1', 1, 1, '2024-12-18 23:22:42', '2024-12-18 23:22:42');
INSERT INTO `sys_dept` VALUES (17, '产品与项目部负责人', 12, '0,1,12', 0, 1, '2024-12-18 23:22:53', '2024-12-18 23:25:05');
INSERT INTO `sys_dept` VALUES (18, '市场与销售部负责人', 16, '0,1,16', 0, 1, '2024-12-18 23:23:02', '2024-12-18 23:25:20');
INSERT INTO `sys_dept` VALUES (19, '市场经理', 16, '0,1,16', 1, 1, '2024-12-18 23:23:08', '2024-12-18 23:23:08');
INSERT INTO `sys_dept` VALUES (20, '销售经理', 16, '0,1,16', 1, 1, '2024-12-18 23:23:13', '2024-12-18 23:23:13');
INSERT INTO `sys_dept` VALUES (21, '销售代表', 16, '0,1,16', 1, 1, '2024-12-18 23:23:26', '2024-12-18 23:23:26');
INSERT INTO `sys_dept` VALUES (22, '客户经理', 16, '0,1,16', 1, 1, '2024-12-18 23:23:32', '2024-12-18 23:23:32');
INSERT INTO `sys_dept` VALUES (23, '内容营销/新媒体运营', 16, '0,1,16', 1, 1, '2024-12-18 23:23:38', '2024-12-18 23:23:38');
INSERT INTO `sys_dept` VALUES (24, '财务与人力资源部', 1, '0,1', 1, 1, '2024-12-18 23:23:46', '2024-12-18 23:23:46');
INSERT INTO `sys_dept` VALUES (25, '财务经理/会计', 24, '0,1,24', 1, 1, '2024-12-18 23:23:52', '2024-12-18 23:23:52');
INSERT INTO `sys_dept` VALUES (26, '人力资源经理/HR专员', 24, '0,1,24', 1, 1, '2024-12-18 23:23:58', '2024-12-18 23:23:58');
INSERT INTO `sys_dept` VALUES (27, '行政专员（可兼HR）', 24, '0,1,24', 1, 1, '2024-12-18 23:24:07', '2024-12-18 23:24:07');
INSERT INTO `sys_dept` VALUES (28, '数据分析师', 12, '0,1,12', 1, 1, '2024-12-18 23:24:54', '2024-12-18 23:24:54');
INSERT INTO `sys_dept` VALUES (29, '财务与人力资源部负责人', 24, '0,1,24', 0, 1, '2024-12-18 23:25:38', '2024-12-18 23:25:38');
INSERT INTO `sys_dept` VALUES (30, 'YF-成都分公司', 0, '0', 1, 1, '2024-12-18 23:30:52', '2024-12-18 23:30:52');
INSERT INTO `sys_dept` VALUES (31, '用户体验部', 30, '0,30', 1, 1, '2024-12-18 23:31:07', '2024-12-18 23:31:07');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `dict_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型编码',
                                  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典项名称',
                                  `value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典项值',
                                  `sort` tinyint(1) NULL DEFAULT 0 COMMENT '排序',
                                  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(1:正常;0:禁用)',
                                  `defaulted` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认(1:是;0:否)',
                                  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
                                  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人Id',
                                  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人Id',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_dict_type_name`(`dict_type`) USING BTREE COMMENT 'dict_type 外键'
) ENGINE = InnoDB AUTO_INCREMENT = 96 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 'menu', '目录', '1', 0, 1, 1, '【目录】', '2024-07-30 07:25:25', 128, '2025-01-26 16:48:07', 128);
INSERT INTO `sys_dict_data` VALUES (2, 'menu', '菜单', '2', 1, 1, 0, '【菜单】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (3, 'menu', '外链', '3', 2, 1, 0, '【外联】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (4, 'menu', '按钮', '4', 3, 1, 0, '【按钮】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (5, 'gender', '男', '1', 1, 1, 1, '【男】', '2024-07-30 07:25:25', 128, '2024-08-03 22:00:18', 128);
INSERT INTO `sys_dict_data` VALUES (6, 'gender', '女', '2', 2, 1, 0, '【女】', '2024-07-30 07:25:25', 128, '2024-07-31 19:46:41', 128);
INSERT INTO `sys_dict_data` VALUES (7, 'data_permission', '全部数据', '0', 0, 1, 1, '【全部数据】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (8, 'data_permission', '部门及子部门数据', '1', 1, 1, 0, '【部门及子部门数据】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (9, 'data_permission', '本部门数据', '2', 2, 1, 0, '【本部门数据】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (10, 'data_permission', '本人数据', '3', 3, 1, 0, '【本人数据】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (11, 'business', '其他', '0', 0, 1, 1, '【操作日志业务-其他】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (12, 'business', '新增', '1', 0, 1, 0, '【操作日志业务-新增】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (13, 'business', '删除', '2', 0, 1, 0, '【操作日志业务-删除】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (14, 'business', '修改', '3', 0, 1, 0, '【操作日志业务-修改】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (15, 'business', '查询', '4', 0, 1, 0, '【操作日志业务-查询】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (16, 'business', '导出', '5', 0, 1, 0, '【操作日志业务-导出】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (17, 'business', '导入', '6', 0, 1, 0, '【操作日志业务-导入】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (18, 'business', '上传文件', '7', 0, 1, 0, '【操作日志业务-上传文件】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (19, 'business', '登录', '8', 0, 1, 0, '【操作日志业务-登录】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (20, 'business', '登出', '9', 0, 1, 0, '【操作日志业务-登出】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (21, 'business', '踢出', '10', 0, 1, 0, '【操作日志业务-踢出】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (22, 'business', '聊天室', '11', 0, 1, 0, '【操作日志业务-聊天室】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (23, 'business', '测试', '999', 0, 1, 0, '【操作日志业务-测试】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (24, 'operator_type', '其他', '0', 0, 1, 1, '【操作日志客户端类型-其他】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (25, 'operator_type', '后台', '1', 0, 1, 0, '【操作日志客户端类型-后台】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (26, 'operator_type', 'H5', '2', 0, 1, 0, '【操作日志客户端类型-H5端】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (27, 'operator_type', '手机端', '3', 0, 1, 0, '【操作日志客户端类型-手机端】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
INSERT INTO `sys_dict_data` VALUES (28, 'business', '发送邮箱', '12', 0, 1, 0, '【操作日志业务-发送邮箱】', '2024-12-17 14:58:29', 123, '2024-12-17 14:58:35', 128);
INSERT INTO `sys_dict_data` VALUES (32, 'back_end', '生成Boot3和MP', 'SpringBoot3-MybatisPlus', 1, 1, 0, '【代码生成-生成SP3和MP】', '2025-04-20 16:58:07', 128, '2025-04-20 17:21:02', 128);
INSERT INTO `sys_dict_data` VALUES (33, 'front_end', '生成Vue3和Ts', 'Vue3-Typescript', 1, 1, 0, '【代码生成-生成Vue3和Ts】', '2025-04-20 17:20:36', 128, '2025-04-20 17:20:36', 128);
INSERT INTO `sys_dict_data` VALUES (34, 'java_type', 'String', 'String', 1, 1, 0, '【代码生成-String】', '2025-04-21 00:58:03', 128, '2025-04-21 00:58:03', 128);
INSERT INTO `sys_dict_data` VALUES (35, 'java_type', 'byte[]', 'byte[]', 1, 1, 0, '【代码生成-byte[]】', '2025-04-21 00:58:36', 128, '2025-04-21 00:58:36', 128);
INSERT INTO `sys_dict_data` VALUES (36, 'java_type', 'Integer', 'Integer', 1, 1, 0, '【代码生成-Integer】', '2025-04-21 10:43:51', 128, '2025-04-21 10:44:12', 128);
INSERT INTO `sys_dict_data` VALUES (37, 'java_type', 'Long', 'Long', 1, 1, 0, '【代码生成-Long】', '2025-04-21 10:44:29', 128, '2025-04-21 10:44:39', 128);
INSERT INTO `sys_dict_data` VALUES (38, 'java_type', 'Float', 'Float', 1, 1, 0, '【代码生成-Float】', '2025-04-21 11:13:51', 128, '2025-04-21 11:14:04', 128);
INSERT INTO `sys_dict_data` VALUES (39, 'java_type', 'Double', 'Double', 1, 1, 0, '【代码生成-Double】', '2025-04-21 11:14:33', 128, '2025-04-21 11:14:46', 128);
INSERT INTO `sys_dict_data` VALUES (40, 'java_type', 'BigDecimal', 'BigDecimal', 1, 1, 0, '【代码生成-BigDecimal】', '2025-04-21 11:15:25', 128, '2025-04-21 11:15:37', 128);
INSERT INTO `sys_dict_data` VALUES (41, 'java_type', 'LocalDate', 'LocalDate', 1, 1, 0, '【代码生成-LocalDate】', '2025-04-21 11:15:50', 128, '2025-04-21 11:16:07', 128);
INSERT INTO `sys_dict_data` VALUES (42, 'java_type', 'LocalDateTime', 'LocalDateTime', 1, 1, 0, '【代码生成-LocalDateTime】', '2025-04-21 11:16:30', 128, '2025-04-21 11:16:42', 128);
INSERT INTO `sys_dict_data` VALUES (43, 'ts_type', 'string', 'string', 1, 1, 0, '【代码生成-string】', '2025-04-21 11:17:55', 128, '2025-04-21 11:18:11', 128);
INSERT INTO `sys_dict_data` VALUES (44, 'ts_type', 'Uint8Array', 'Uint8Array', 1, 1, 0, '【代码生成-Uint8Array】', '2025-04-21 11:18:26', 128, '2025-04-21 11:18:35', 128);
INSERT INTO `sys_dict_data` VALUES (45, 'ts_type', 'any', 'any', 0, 1, 0, '【代码生成-any】', '2025-04-21 11:19:23', 128, '2025-04-21 11:19:32', 128);
INSERT INTO `sys_dict_data` VALUES (46, 'ts_type', 'number', 'number', 1, 1, 0, '【代码生成-number】', '2025-04-21 11:19:44', 128, '2025-04-21 11:19:52', 128);
INSERT INTO `sys_dict_data` VALUES (47, 'db_type', 'varchar', 'varchar', 1, 1, 0, '【代码生成-varchar】', '2025-04-21 11:22:35', 128, '2025-04-21 11:22:35', 128);
INSERT INTO `sys_dict_data` VALUES (48, 'db_type', 'char', 'char', 1, 1, 0, '【代码生成-char】', '2025-04-21 11:22:50', 128, '2025-04-21 11:23:09', 128);
INSERT INTO `sys_dict_data` VALUES (49, 'db_type', 'blob', 'blob', 1, 1, 0, '【代码生成-blob】', '2025-04-21 11:23:01', 128, '2025-04-21 11:23:25', 128);
INSERT INTO `sys_dict_data` VALUES (50, 'db_type', 'text', 'text', 1, 1, 0, '【代码生成-text】', '2025-04-21 11:23:38', 128, '2025-04-21 11:24:57', 128);
INSERT INTO `sys_dict_data` VALUES (51, 'db_type', 'int', 'int', 1, 1, 0, '【代码生成-int】', '2025-04-21 11:24:20', 128, '2025-04-21 11:25:07', 128);
INSERT INTO `sys_dict_data` VALUES (52, 'db_type', 'tinyint', 'tinyint', 1, 1, 0, '【代码生成-tinyint】', '2025-04-21 11:24:48', 128, '2025-04-21 11:25:18', 128);
INSERT INTO `sys_dict_data` VALUES (53, 'db_type', 'bigint', 'bigint', 1, 1, 0, '【代码生成-bigint】', '2025-04-21 11:25:41', 128, '2025-04-21 11:26:32', 128);
INSERT INTO `sys_dict_data` VALUES (54, 'db_type', 'float', 'float', 1, 1, 0, '【代码生成-float】', '2025-04-21 11:25:58', 128, '2025-04-21 11:26:46', 128);
INSERT INTO `sys_dict_data` VALUES (55, 'db_type', 'double', 'double', 1, 1, 0, '【代码生成-double】', '2025-04-21 11:26:18', 128, '2025-04-21 11:26:56', 128);
INSERT INTO `sys_dict_data` VALUES (56, 'db_type', 'decimal', 'decimal', 1, 1, 0, '【代码生成-decimal】', '2025-04-21 11:27:13', 128, '2025-04-21 11:27:43', 128);
INSERT INTO `sys_dict_data` VALUES (57, 'db_type', 'date', 'date', 1, 1, 0, '【代码生成-date】', '2025-04-21 11:27:24', 128, '2025-04-21 11:28:03', 128);
INSERT INTO `sys_dict_data` VALUES (58, 'db_type', 'datetime', 'datetime', 1, 1, 0, '【代码生成-datetime】', '2025-04-21 11:27:32', 128, '2025-04-21 11:28:09', 128);
INSERT INTO `sys_dict_data` VALUES (59, 'form_type', '输入框', 'input', 1, 1, 0, '【代码生成-输入框】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (60, 'form_type', '数字输入框', 'input_number', 1, 1, 0, '【代码生成-数字输入框】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (61, 'form_type', '密码输入框', 'input_password', 1, 1, 0, '【代码生成-密码输入框】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (62, 'form_type', '下拉框', 'select', 1, 1, 0, '【代码生成-下拉框】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (63, 'form_type', '单选框', 'radio', 1, 1, 0, '【代码生成-单选框】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (64, 'form_type', '日期选择器', 'date', 1, 1, 0, '【代码生成-日期选择器】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (65, 'form_type', '日期时间选择器', 'date_time', 1, 1, 0, '【代码生成-日期时间选择器】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (66, 'form_type', '开关', 'switch', 1, 1, 0, '【代码生成-开关】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (67, 'form_type', '滑块', 'slider', 1, 1, 0, '【代码生成-滑块】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (68, 'form_type', '评分', 'rate', 1, 1, 0, '【代码生成-评分】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (69, 'form_type', '颜色选择器', 'color', 1, 1, 0, '【代码生成-颜色选择器】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (70, 'form_type', '文件上传', 'file', 1, 1, 0, '【代码生成-文件上传】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (71, 'form_type', '图片上传', 'image', 1, 1, 0, '【代码生成-图片上传】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (72, 'form_type', '富文本', 'markdown', 1, 1, 0, '【代码生成-富文本】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (73, 'form_type', '文本域', 'text_area', 1, 1, 0, '【代码生成-文本域】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (74, 'query_form_type', '输入框', 'input', 1, 1, 0, '【查询组件-输入框】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (75, 'query_form_type', '数字输入框', 'input_number', 1, 1, 0, '【查询组件-数字输入框】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (76, 'query_form_type', '单选下拉框', 'select', 1, 1, 0, '【查询组件-单选下拉框】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (77, 'query_form_type', '多选下拉框', 'multi_select', 1, 1, 0, '【查询组件-多选下拉框】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (78, 'query_form_type', '日期时间选择器', 'datetime', 1, 1, 0, '【查询组件-日期时间选择器】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (79, 'query_form_type', '日期选择器', 'date', 1, 1, 0, '【查询组件-日期选择器】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (80, 'show_type', '文本', 'default', 1, 1, 0, '【展示组件-文本】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (81, 'show_type', '特殊文本', 'text', 1, 1, 0, '【展示组件-特殊文本】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (82, 'show_type', '标签', 'tag', 1, 1, 0, '【展示组件-标签】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (83, 'show_type', '图片', 'image', 1, 1, 0, '【展示组件-图片】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (84, 'show_type', '链接', 'href', 1, 1, 0, '【展示组件-链接】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (85, 'show_type', '评分', 'rate', 1, 1, 0, '【展示组件-评分】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (86, 'query_type', '等于', 'eq', 1, 1, 0, '【查询方式-等于】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (87, 'query_type', '模糊匹配', 'like', 1, 1, 0, '【查询方式-模糊匹配】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (88, 'query_type', '左模糊匹配', 'like_left', 1, 1, 0, '【查询方式-左模糊匹配】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (89, 'query_type', '右模糊匹配', 'like_right', 1, 1, 0, '【查询方式-右模糊匹配】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (90, 'query_type', '大于', 'gt', 1, 1, 0, '【查询方式-大于】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (91, 'query_type', '小于', 'lt', 1, 1, 0, '【查询方式-小于】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (92, 'query_type', '大于等于', 'ge', 1, 1, 0, '【查询方式-大于等于】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (93, 'query_type', '小于等于', 'le', 1, 1, 0, '【查询方式-小于等于】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (94, 'query_type', '区间', 'between', 1, 1, 0, '【查询方式-区间】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);
INSERT INTO `sys_dict_data` VALUES (95, 'query_type', '包含', 'in', 1, 1, 0, '【查询方式-包含】', '2025-04-21 11:36:11', 128, '2025-04-21 11:36:11', 128);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
                                  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 ',
                                  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典名称',
                                  `type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典类型',
                                  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态（1正常 0停用）',
                                  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE INDEX `nx_dict_type`(`type`) USING BTREE COMMENT '防止构建同一type'
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '菜单', 'menu', 1, '菜单类型', '2024-07-30 07:25:25', '2024-07-31 00:33:11');
INSERT INTO `sys_dict_type` VALUES (2, '性别', 'gender', 1, '性别', '2024-07-30 07:25:25', '2024-07-30 07:25:25');
INSERT INTO `sys_dict_type` VALUES (3, '数据权限', 'data_permission', 1, '数据权限', '2024-07-30 07:25:25', '2024-07-30 07:25:25');
INSERT INTO `sys_dict_type` VALUES (4, '业务', 'business', 1, '操作日志中定义的业务类型', '2024-07-30 07:25:25', '2024-07-30 07:25:25');
INSERT INTO `sys_dict_type` VALUES (5, '客户端', 'operator_type', 1, '操作日志中定义的操作日志客户端', '2024-07-30 07:25:25', '2024-07-30 07:25:25');
INSERT INTO `sys_dict_type` VALUES (8, '后端生成类型', 'back_end', 1, 'Crud生成-后端生成类型', '2025-04-20 16:56:47', '2025-04-20 16:56:47');
INSERT INTO `sys_dict_type` VALUES (9, '前端生成类型', 'front_end', 1, 'Crud生成-前端生成类型', '2025-04-20 17:10:23', '2025-04-20 17:41:25');
INSERT INTO `sys_dict_type` VALUES (10, 'Java类型', 'java_type', 1, 'Crud生成-Java类型', '2025-04-21 00:54:56', '2025-04-21 00:54:56');
INSERT INTO `sys_dict_type` VALUES (11, 'Type Script类型', 'ts_type', 1, 'Crud生成-Type Script类型', '2025-04-21 00:55:55', '2025-04-21 00:55:55');
INSERT INTO `sys_dict_type` VALUES (12, 'DB字段类型', 'db_type', 1, 'Crud生成-DB字段类型', '2025-04-21 11:21:24', '2025-04-21 11:21:42');
INSERT INTO `sys_dict_type` VALUES (13, '新增/修改表单类型', 'form_type', 1, 'Crud生成-新增/修改表单类型', '2025-04-21 11:29:57', '2025-04-21 11:30:24');
INSERT INTO `sys_dict_type` VALUES (14, '查询表单展示类型', 'query_form_type', 1, 'Crud生成-查询表单展示类型', '2025-04-21 11:31:56', '2025-04-21 11:31:56');
INSERT INTO `sys_dict_type` VALUES (15, '查询算子', 'query_type', 1, 'Crud生成-查询算子', '2025-04-21 11:32:52', '2025-04-21 11:32:52');
INSERT INTO `sys_dict_type` VALUES (16, '表单展示类型', 'show_type', 1, 'Crud-表单展示类型', '2025-04-21 11:33:58', '2025-04-21 11:33:58');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
                             `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单主键',
                             `parent_id` int(11) NULL DEFAULT 0 COMMENT '父菜单ID',
                             `tree_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父节点ID路径',
                             `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单名称',
                             `type` tinyint(1) NOT NULL COMMENT '菜单类型(1:目录；2:菜单；3:外链；4:按钮)',
                             `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由路径(浏览器地址栏路径)',
                             `component` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径(vue页面完整路径，省略.vue后缀)',
                             `permission` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
                             `hidden` tinyint(1) NULL DEFAULT NULL COMMENT '显示状态(0-显示;1-隐藏)',
                             `keep_alive` tinyint(1) NULL DEFAULT NULL COMMENT '【菜单】是否开启页面缓存(1:是 0:否)',
                             `show_single_children` tinyint(1) NULL DEFAULT NULL COMMENT '【目录】是否展示单个子菜单(1:是 0:否)',
                             `affix` tinyint(1) NULL DEFAULT NULL COMMENT '【菜单】是否固定到 TagsView 上面(1:是 0:否)',
                             `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单图标',
                             `breadcrumb` tinyint(1) NULL DEFAULT NULL COMMENT '标题显示在面包屑上(1:是 ,0:否)',
                             `sort` tinyint(1) NULL DEFAULT 0 COMMENT '排序',
                             `redirect` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转路径',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 244 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (128, 0, '0', '系统管理', 1, '/system', 'Layout', '', 0, 0, 0, NULL, 'system', 1, 0, '/system/user', '2024-04-18 09:12:21', '2024-08-02 22:12:04');
INSERT INTO `sys_menu` VALUES (129, 128, '0,128', '用户管理', 2, 'user', 'system/user/index', 'system:user:list', 0, 1, NULL, 0, 'user', 1, 1, NULL, '2024-04-18 09:12:21', '2025-01-17 22:43:15');
INSERT INTO `sys_menu` VALUES (130, 129, '0,128,129', '用户新增', 4, '', NULL, 'system:user:save', NULL, NULL, NULL, NULL, '', NULL, 1, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (131, 129, '0,128,129', '用户删除', 4, '', NULL, 'system:user:delete', NULL, NULL, NULL, NULL, '', NULL, 2, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (132, 129, '0,128,129', '用户修改', 4, '', NULL, 'system:user:update', NULL, NULL, NULL, NULL, '', NULL, 3, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (133, 129, '0,128,129', '用户信息导入', 4, '', NULL, 'system:user:import', NULL, NULL, NULL, NULL, '', NULL, 4, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (134, 129, '0,128,129', '用户信息导出', 4, '', NULL, 'system:user:export', NULL, NULL, NULL, NULL, '', NULL, 5, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (136, 128, '0,128', '角色管理', 2, 'role', 'system/role/index', 'system:role:list', 0, 1, NULL, NULL, 'role', 1, 1, NULL, '2024-04-18 09:12:21', '2025-01-16 23:04:10');
INSERT INTO `sys_menu` VALUES (137, 136, '0,128,136', '角色新增', 4, '', NULL, 'system:role:save', NULL, NULL, NULL, NULL, '', NULL, 1, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (138, 136, '0,128,136', '角色删除', 4, '', NULL, 'system:role:delete', NULL, NULL, NULL, NULL, '', NULL, 2, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (139, 136, '0,128,136', '角色修改', 4, '', NULL, 'system:role:update', NULL, NULL, NULL, NULL, '', NULL, 3, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (140, 136, '0,128,136', '角色信息导入', 4, '', NULL, 'system:role:import', NULL, NULL, NULL, NULL, '', NULL, 4, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (141, 136, '0,128,136', '角色信息导出', 4, '', NULL, 'system:role:export', NULL, NULL, NULL, NULL, '', NULL, 5, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (142, 128, '0,128', '菜单管理', 2, 'menu', 'system/menu/index', 'system:menu:list', 0, 1, 0, 0, 'menu', 1, 1, NULL, '2024-04-18 09:12:21', '2024-05-22 18:58:49');
INSERT INTO `sys_menu` VALUES (143, 142, '0,128,142', '菜单新增', 4, '', NULL, 'system:menu:save', NULL, NULL, NULL, NULL, '', NULL, 1, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (144, 142, '0,128,142', '菜单删除', 4, '', NULL, 'system:menu:delete', NULL, NULL, NULL, NULL, '', NULL, 2, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (145, 142, '0,128,142', '菜单修改', 4, '', NULL, 'system:menu:update', NULL, NULL, NULL, NULL, '', NULL, 3, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (146, 142, '0,128,142', '菜单信息导入', 4, '', NULL, 'system:menu:import', NULL, NULL, NULL, NULL, '', NULL, 4, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (147, 142, '0,128,142', '菜单信息导出', 4, '', NULL, 'system:menu:export', NULL, NULL, NULL, NULL, '', NULL, 5, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (148, 181, '0,181', '操作日志', 2, 'operation-log', 'monitor/operation-log/index', 'monitor:operation-log:list', 0, 1, NULL, NULL, 'operation-log', 1, 1, NULL, '2024-04-18 09:12:21', '2024-07-26 17:04:55');
INSERT INTO `sys_menu` VALUES (154, 128, '0,128', '第三方授权管理', 2, 'oauth', 'system/oauth/index', 'system:oauth:list', 0, 1, NULL, NULL, 'oauth', 1, 1, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (156, 154, '0,128,154', '第三方授权删除', 4, '', NULL, 'system:oauth:delete', NULL, NULL, NULL, NULL, '', NULL, 2, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (157, 154, '0,128,154', '第三方授权修改', 4, '', NULL, 'system:oauth:update', NULL, NULL, NULL, NULL, '', NULL, 3, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (160, 128, '0,128', '部门管理', 2, 'dept', 'system/dept/index', 'system:dept:list', 0, 1, NULL, NULL, 'dept', 1, 1, NULL, '2024-04-18 09:12:21', '2025-01-12 17:53:57');
INSERT INTO `sys_menu` VALUES (161, 160, '0,128,160', '部门新增', 4, '', NULL, 'system:dept:save', NULL, NULL, NULL, NULL, '', NULL, 1, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (162, 160, '0,128,160', '部门删除', 4, '', NULL, 'system:dept:delete', NULL, NULL, NULL, NULL, '', NULL, 2, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (163, 160, '0,128,160', '部门修改', 4, '', NULL, 'system:dept:update', NULL, NULL, NULL, NULL, '', NULL, 3, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (164, 160, '0,128,160', '部门信息导入', 4, '', NULL, 'system:dept:import', NULL, NULL, NULL, NULL, '', NULL, 4, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (165, 160, '0,128,160', '部门信息导出', 4, '', NULL, 'system:dept:export', NULL, NULL, NULL, NULL, '', NULL, 5, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (166, 128, '0,128', '字典类型管理', 2, 'dict-type', 'system/dict-type/index', 'system:dict-type:list', 0, 1, NULL, NULL, 'dict', 1, 1, NULL, '2024-04-18 09:12:21', '2025-01-17 20:21:51');
INSERT INTO `sys_menu` VALUES (167, 166, '0,128,166', '字典新增', 4, '', NULL, 'system:dict-type:save', NULL, NULL, NULL, NULL, '', NULL, 1, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (168, 166, '0,128,166', '字典删除', 4, '', NULL, 'system:dict-type:delete', NULL, NULL, NULL, NULL, '', NULL, 2, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (169, 166, '0,128,166', '字典修改', 4, '', NULL, 'system:dict-type:update', NULL, NULL, NULL, NULL, '', NULL, 3, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (172, 0, '0', 'AI助手', 1, '/ai', 'Layout', '', 0, 1, 1, NULL, 'ai-assistant', NULL, 3, '/ai/easy-chat', '2024-05-22 13:24:01', '2024-08-02 19:09:06');
INSERT INTO `sys_menu` VALUES (173, 172, '0,172', 'EasyChat', 2, 'easy-chat', 'ai/easy-chat/index', 'ai:easy_chat:list', 0, 1, 0, NULL, 'EasyChat', NULL, 1, NULL, '2024-05-22 13:28:58', '2024-05-22 13:44:04');
INSERT INTO `sys_menu` VALUES (174, 0, '0', '代码生成', 1, '/generate', 'Layout', '', 0, 1, 1, NULL, 'code-assistant', NULL, 3, '/generate/gen-code', '2024-05-22 14:12:52', '2025-04-14 15:19:12');
INSERT INTO `sys_menu` VALUES (175, 174, '0,174', 'Crud 生成', 2, 'crud', 'generate/crud/index', 'generate:crud:list', 0, 1, 0, NULL, 'gen-code', NULL, 1, NULL, '2024-05-22 14:15:13', '2025-04-14 17:21:18');
INSERT INTO `sys_menu` VALUES (176, 174, '0,174', '低代码开发', 2, 'low-code', 'generate/low-code/index', 'generate:low:list', 0, 1, 0, NULL, 'low-code', NULL, 1, NULL, '2024-05-22 14:16:54', '2025-04-14 17:56:42');
INSERT INTO `sys_menu` VALUES (177, 172, '0,172', 'ChatGPT4.0', 2, 'chat-gpt', 'ai/chat-gpt/index', 'ai:gpt:list', 0, 1, 0, NULL, 'ChatGPT', NULL, 1, NULL, '2024-05-22 14:18:13', '2024-05-22 14:18:13');
INSERT INTO `sys_menu` VALUES (178, 172, '0,172', 'ollama', 2, 'ollama', 'ai/ollama/index', 'ai:ollama:list', 0, 1, 0, NULL, 'ollama', NULL, 1, NULL, '2024-05-22 14:22:08', '2024-05-22 14:22:08');
INSERT INTO `sys_menu` VALUES (179, 0, '0', 'websocket', 1, '/websocket', 'Layout', '', 0, 0, 1, NULL, 'websocket', NULL, 3, '/websocket/chat-room', '2024-05-22 14:24:41', '2024-08-02 19:09:02');
INSERT INTO `sys_menu` VALUES (180, 179, '0,179', '分布式聊天室', 2, 'chat-room', 'websocket/chat-room/index', 'websocket:chat-room:list', 0, 1, 0, NULL, 'chat-room', NULL, 1, NULL, '2024-05-22 14:25:36', '2024-05-26 20:18:20');
INSERT INTO `sys_menu` VALUES (181, 0, '0', '系统监控', 1, '/monitor', 'Layout', NULL, 0, 1, 1, NULL, 'monitor', NULL, 1, NULL, '2024-05-22 14:29:49', '2024-05-22 14:33:52');
INSERT INTO `sys_menu` VALUES (182, 181, '0,181', '在线用户', 2, 'online-user', 'monitor/online-user/index', 'monitor:online-user:list', 0, 1, 0, NULL, 'online-user', NULL, 1, NULL, '2024-05-22 14:35:07', '2024-05-22 14:48:48');
INSERT INTO `sys_menu` VALUES (183, 181, '0,181', '限流监控', 2, 'rate-limiter', 'monitor/rate-limiter/index', 'monitor:rate-limiter:list', 0, 1, 0, NULL, 'rate-limiter', NULL, 1, NULL, '2024-05-22 14:43:34', '2024-05-22 14:43:34');
INSERT INTO `sys_menu` VALUES (184, 181, '0,181', '系统黑名单', 2, 'black-list', 'monitor/black-list/index', 'monitor:black-list:list', 0, 1, 0, NULL, 'black-list', NULL, 1, NULL, '2024-05-22 14:45:18', '2024-05-22 14:45:18');
INSERT INTO `sys_menu` VALUES (185, 129, '0,128,129', '重置用户名密码', 4, '', NULL, 'system:user:reset-pwd', NULL, 1, 0, NULL, '', NULL, 1, NULL, '2024-05-22 14:54:25', '2024-05-22 14:54:32');
INSERT INTO `sys_menu` VALUES (187, 180, '0,179,180', '私发消息', 4, '', NULL, 'websocket:chat-room:private', NULL, NULL, NULL, NULL, '', NULL, 3, NULL, '2024-05-24 19:05:46', '2024-05-24 19:07:02');
INSERT INTO `sys_menu` VALUES (188, 180, '0,179,180', '群发消息', 4, '', NULL, 'websocket:chat-room:public', NULL, NULL, NULL, NULL, '', NULL, 2, NULL, '2024-05-24 19:06:16', '2024-05-24 19:06:59');
INSERT INTO `sys_menu` VALUES (189, 180, '0,179,180', '发送系统消息', 4, '', NULL, 'websocket:chat-room:system', NULL, NULL, NULL, NULL, '', NULL, 1, NULL, '2024-05-24 19:06:52', '2024-05-24 19:06:52');
INSERT INTO `sys_menu` VALUES (190, 179, '0,179', '消息管理', 2, 'message', 'websocket/message/index', 'websocket:message:list', 1, 0, 0, 0, 'message', NULL, 1, NULL, '2024-05-29 18:31:17', '2024-05-29 18:31:28');
INSERT INTO `sys_menu` VALUES (194, 0, '0', '功能封装演示', 1, '/demo', 'Layout', '', 0, 1, 1, 0, 'demo', NULL, 2, '/demo/file', '2024-06-12 18:28:38', '2024-08-02 19:08:15');
INSERT INTO `sys_menu` VALUES (196, 194, '0,194', '图片检测', 2, 'nsfw', 'demo/nsfw/index', 'demo:nsfw:list', 0, 1, 0, 0, 'nsfw', NULL, 1, NULL, '2024-07-21 10:46:46', '2024-07-21 10:46:46');
INSERT INTO `sys_menu` VALUES (197, 194, '0,194', '图片物品检测', 2, 'object-search', 'demo/object-search/index', 'demo:object-search:list', 0, 1, 0, 0, 'image-detection', NULL, 1, NULL, '2024-07-21 11:00:42', '2024-07-21 11:00:42');
INSERT INTO `sys_menu` VALUES (198, 194, '0,194', 'ffmpeg', 2, 'ffmpeg', 'demo/ffmpeg/index', 'demo:ffmpeg:list', 0, 1, 0, 0, 'ffmpeg', NULL, 1, NULL, '2024-07-21 11:05:20', '2024-07-21 11:45:06');
INSERT INTO `sys_menu` VALUES (199, 194, '0,194', 'markdown', 2, 'markdown', 'demo/markdown/index', 'demo:markdown:list', 0, 1, 0, 0, 'markdown', NULL, 1, NULL, '2024-07-21 11:44:40', '2024-07-21 11:44:40');
INSERT INTO `sys_menu` VALUES (200, 209, '0,209', '开发文档', 2, 'dev', 'doc/dev/index', 'doc:dev:list', 0, 1, 0, 0, 'help-documentation', NULL, 1, NULL, '2024-07-21 11:51:42', '2025-01-27 14:22:11');
INSERT INTO `sys_menu` VALUES (201, 194, '0,194', 'ocr', 2, 'ocr', 'demo/ocr/index', 'demo:ocr:list', 0, 1, 0, 0, 'ocr', NULL, 1, NULL, '2024-07-21 14:31:36', '2024-07-21 14:31:58');
INSERT INTO `sys_menu` VALUES (202, 128, '0,128', '字典详情数据', 2, 'dict-data/:id', 'system/dict-data/index', 'system:dict-data:list', 1, 1, 0, 0, 'dict', NULL, 1, NULL, '2024-07-28 16:43:04', '2025-01-17 20:21:00');
INSERT INTO `sys_menu` VALUES (203, 166, '0,128,166', '字典信息导入', 4, '', '', 'system:dict-type:import', 1, 0, 0, 0, '', NULL, 4, NULL, '2024-07-28 16:44:45', '2024-07-28 16:44:53');
INSERT INTO `sys_menu` VALUES (204, 166, '0,128,166', '字典信息导出', 4, '', '', 'system:dict-type:export', 1, 0, 0, 0, '', NULL, 5, NULL, '2024-07-28 16:45:21', '2024-07-28 16:45:35');
INSERT INTO `sys_menu` VALUES (205, 202, '0,128,202', '字典详情数据新增', 4, '', '', 'system:dict-data:save', 1, 0, 0, 0, '', NULL, 1, NULL, '2024-07-28 16:47:34', '2024-07-28 17:16:55');
INSERT INTO `sys_menu` VALUES (206, 202, '0,128,202', '字典详情数据修改', 4, '', '', 'system:dict-data:update', 1, 0, 0, 0, '', NULL, 2, NULL, '2024-07-28 16:48:17', '2024-07-28 17:16:55');
INSERT INTO `sys_menu` VALUES (207, 202, '0,128,202', '字典详情数据删除', 4, '', '', 'system:dict-data:delete', 1, 0, 0, 0, '', NULL, 3, NULL, '2024-07-28 16:48:44', '2024-07-28 17:16:55');
INSERT INTO `sys_menu` VALUES (208, 194, '0,194', 'echarts', 2, 'echarts', 'demo/echarts/index', 'demo:echarts:list', 0, 1, 0, 0, 'echarts', NULL, 1, NULL, '2024-07-31 20:03:16', '2024-07-31 20:03:16');
INSERT INTO `sys_menu` VALUES (209, 0, '0', '平台文档', 1, '/doc', 'Layout', '', 0, 1, 0, 0, 'doc', NULL, 1, NULL, '2024-08-02 21:12:41', '2024-08-02 21:22:08');
INSERT INTO `sys_menu` VALUES (212, 209, '0,209', '掘金文档', 3, 'https://juejin.cn/post/7424399072532447244', '', NULL, 0, 1, 0, 0, 'juejin', NULL, 1, NULL, '2024-08-02 21:47:41', '2025-01-27 14:22:43');
INSERT INTO `sys_menu` VALUES (214, 148, '0,181,148', '删除操作日志信息', 4, '', '', 'monitor:operation-log:delete', 1, 0, 0, 0, '', NULL, 1, NULL, '2024-08-05 15:18:28', '2024-08-05 15:18:28');
INSERT INTO `sys_menu` VALUES (215, 136, '0,128,136', '角色授权菜单', 4, '', '', 'system:role:permission', 1, 0, 0, 0, '', NULL, 0, NULL, '2024-08-11 14:45:26', '2024-08-11 14:45:31');
INSERT INTO `sys_menu` VALUES (216, 194, '0,194', '生成二维码', 2, 'qr', 'demo/qr/index', 'demo:qr:list', 0, 1, 0, 0, 'qr', NULL, 1, NULL, '2024-08-22 17:56:23', '2024-08-25 15:54:12');
INSERT INTO `sys_menu` VALUES (217, 182, '0,181,182', '踢出在线用户', 4, '', '', 'monitor:online-user:kick-out', 1, 0, 0, 0, '', NULL, 1, NULL, '2024-09-05 11:00:44', '2024-09-05 11:50:48');
INSERT INTO `sys_menu` VALUES (218, 194, '0,194', '节日头像', 2, 'festival-avatar', 'demo/festival-avatar/index', 'demo:festival-avatar:list', 0, 1, 0, 0, 'festival-avatar', NULL, 1, NULL, '2024-09-21 21:48:33', '2024-09-21 21:49:12');
INSERT INTO `sys_menu` VALUES (219, 194, '0,194', '动态开关逻辑', 2, 'dynamic-logic-switch', 'demo/dynamic-logic-switch/index', 'demo:dynamic-logic-switch:list', 0, 1, 0, 0, 'system', NULL, 1, NULL, '2024-11-28 09:45:00', '2024-11-28 09:45:00');
INSERT INTO `sys_menu` VALUES (228, 174, '0,174', 'Crud 生成配置', 2, 'crud-manage/:id', 'generate/crud-manage/index', 'generate:crud:manage', 1, 1, 0, 0, 'gen-code', NULL, 1, NULL, '2025-04-14 17:57:52', '2025-04-19 22:43:56');
INSERT INTO `sys_menu` VALUES (229, 175, '0,174,175', '数据表导入', 4, '', '', 'generate:crud:import', 1, 0, 0, 0, '', NULL, 1, NULL, '2025-04-14 23:50:31', '2025-04-14 23:50:31');
INSERT INTO `sys_menu` VALUES (230, 175, '0,174,175', '生成Zip', 4, '', '', 'generate:crud:zip', 1, 0, 0, 0, '', NULL, 1, NULL, '2025-04-14 23:51:38', '2025-04-17 20:42:17');
INSERT INTO `sys_menu` VALUES (231, 228, '0,174,228', 'Crud 代码阅览', 4, '', '', 'generate:crud:preview', 1, 0, 0, 0, '', NULL, 1, NULL, '2025-04-14 23:52:29', '2025-04-14 23:52:29');
INSERT INTO `sys_menu` VALUES (232, 175, '0,174,175', '删除数据表', 4, '', '', 'generate:crud:delete', 1, 0, 0, 0, '', NULL, 1, NULL, '2025-04-14 23:54:04', '2025-04-14 23:54:04');
INSERT INTO `sys_menu` VALUES (235, 175, '0,174,175', '修改 Crud 配置', 4, '', '', 'generate:crud:update', 1, 0, 0, 0, '', NULL, 1, NULL, '2025-04-19 23:03:33', '2025-04-19 23:03:33');
INSERT INTO `sys_menu` VALUES (242, 228, '0,174,228', '新增菜单', 4, '', '', 'generate:crud:menu', 1, 0, 0, 0, '', NULL, 1, NULL, '2025-04-19 23:24:28', '2025-04-19 23:24:28');
INSERT INTO `sys_menu` VALUES (243, 194, '0,194', '文件上传演示', 2, 'file-upload-demo', 'demo/file-upload-demo/index', 'demo:file-upload:list', 0, 1, 0, 0, 'file-upload', NULL, 1, NULL, '2025-05-09 00:44:25', '2025-05-10 20:32:24');

-- ----------------------------
-- Table structure for sys_oauth
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth`;
CREATE TABLE `sys_oauth`  (
                              `id` bigint(20) NOT NULL COMMENT '主键',
                              `user_id` bigint(20) NOT NULL COMMENT '用户id',
                              `platform_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三方平台名',
                              `platform_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三方平台用户唯一标识',
                              `platform_username` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方平台用户名',
                              `platform_user_avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方平台用户头像地址',
                              `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                              PRIMARY KEY (`id`) USING BTREE,
                              UNIQUE INDEX `uk _oauth_provider_user_id`(`platform_user_id`) USING BTREE COMMENT '唯一索引,provider_user_id',
                              INDEX `idx_oauth_user_id`(`user_id`) USING BTREE COMMENT '普通索引,增加查询效率'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户Oauth信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oauth
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色名称',
                             `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
                             `sort` tinyint(1) NULL DEFAULT NULL COMMENT '显示顺序',
                             `status` tinyint(1) NULL DEFAULT 1 COMMENT '角色状态(1-正常；0-停用)',
                             `data_scope` tinyint(1) NULL DEFAULT NULL COMMENT '数据权限(0-所有数据；1-部门及子部门数据；2-本部门数据；3-本人数据)',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 151 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (128, '管理员', 'ADMIN', 0, 1, 0, '2024-04-18 17:46:02', '2025-05-10 20:15:01');
INSERT INTO `sys_role` VALUES (129, '测试人员', 'TEST', 2, 1, 0, '2024-04-18 17:46:02', '2024-06-11 15:50:49');
INSERT INTO `sys_role` VALUES (130, '运维人员', 'OPERATION', 3, 1, 3, '2024-04-18 17:46:02', '2024-04-18 17:46:07');
INSERT INTO `sys_role` VALUES (131, '销售员', 'SALES', 4, 1, 3, '2024-04-18 17:46:02', '2024-04-18 17:46:07');
INSERT INTO `sys_role` VALUES (132, '客服人员', 'CUSTOMER_SERVICE', 5, 1, 3, '2024-04-18 17:46:02', '2024-04-18 17:46:07');
INSERT INTO `sys_role` VALUES (133, '财务人员', 'FINANCE', 6, 1, 3, '2024-04-18 17:46:02', '2024-04-18 17:46:07');
INSERT INTO `sys_role` VALUES (134, '市场人员', 'MARKETING', 7, 1, 3, '2024-04-18 17:46:02', '2024-04-18 17:46:07');
INSERT INTO `sys_role` VALUES (135, '产品经理', 'PRODUCT_MANAGER', 8, 1, 3, '2024-04-18 17:46:02', '2024-04-18 17:46:07');
INSERT INTO `sys_role` VALUES (136, '项目经理', 'PROJECT_MANAGER', 9, 1, 3, '2024-04-18 17:46:02', '2024-04-18 17:46:07');
INSERT INTO `sys_role` VALUES (137, '人力资源', 'HR', 10, 1, 3, '2024-04-18 17:46:02', '2024-04-18 17:46:07');
INSERT INTO `sys_role` VALUES (138, '用户体验角色', 'USER_EXPERIENCE', 1, 1, 3, '2024-04-18 17:46:02', '2024-04-18 17:46:02');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
                                  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                  `role_id` int(11) NOT NULL COMMENT '角色ID',
                                  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_role_id`(`role_id`) USING BTREE COMMENT 'role_id 外键',
                                  INDEX `idx_menu_id`(`menu_id`) USING BTREE COMMENT 'menu_id 外键'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (4, 138, 128);
INSERT INTO `sys_role_menu` VALUES (5, 138, 129);
INSERT INTO `sys_role_menu` VALUES (6, 138, 194);
INSERT INTO `sys_role_menu` VALUES (7, 138, 196);
INSERT INTO `sys_role_menu` VALUES (8, 138, 197);
INSERT INTO `sys_role_menu` VALUES (9, 138, 198);
INSERT INTO `sys_role_menu` VALUES (10, 138, 199);
INSERT INTO `sys_role_menu` VALUES (11, 138, 136);
INSERT INTO `sys_role_menu` VALUES (12, 138, 200);
INSERT INTO `sys_role_menu` VALUES (13, 138, 201);
INSERT INTO `sys_role_menu` VALUES (14, 138, 202);
INSERT INTO `sys_role_menu` VALUES (15, 138, 142);
INSERT INTO `sys_role_menu` VALUES (16, 138, 208);
INSERT INTO `sys_role_menu` VALUES (17, 138, 209);
INSERT INTO `sys_role_menu` VALUES (18, 138, 148);
INSERT INTO `sys_role_menu` VALUES (19, 138, 212);
INSERT INTO `sys_role_menu` VALUES (20, 138, 154);
INSERT INTO `sys_role_menu` VALUES (21, 138, 160);
INSERT INTO `sys_role_menu` VALUES (22, 138, 166);
INSERT INTO `sys_role_menu` VALUES (23, 138, 172);
INSERT INTO `sys_role_menu` VALUES (24, 138, 173);
INSERT INTO `sys_role_menu` VALUES (25, 138, 174);
INSERT INTO `sys_role_menu` VALUES (26, 138, 175);
INSERT INTO `sys_role_menu` VALUES (27, 138, 176);
INSERT INTO `sys_role_menu` VALUES (28, 138, 177);
INSERT INTO `sys_role_menu` VALUES (29, 138, 178);
INSERT INTO `sys_role_menu` VALUES (30, 138, 179);
INSERT INTO `sys_role_menu` VALUES (31, 138, 180);
INSERT INTO `sys_role_menu` VALUES (32, 138, 181);
INSERT INTO `sys_role_menu` VALUES (33, 138, 182);
INSERT INTO `sys_role_menu` VALUES (34, 138, 183);
INSERT INTO `sys_role_menu` VALUES (35, 138, 184);
INSERT INTO `sys_role_menu` VALUES (36, 138, 187);
INSERT INTO `sys_role_menu` VALUES (37, 138, 188);
INSERT INTO `sys_role_menu` VALUES (38, 138, 189);
INSERT INTO `sys_role_menu` VALUES (39, 138, 190);
INSERT INTO `sys_role_menu` VALUES (40, 138, 216);
INSERT INTO `sys_role_menu` VALUES (41, 138, 218);
INSERT INTO `sys_role_menu` VALUES (42, 138, 219);
INSERT INTO `sys_role_menu` VALUES (43, 138, 228);
INSERT INTO `sys_role_menu` VALUES (44, 138, 229);
INSERT INTO `sys_role_menu` VALUES (45, 138, 230);
INSERT INTO `sys_role_menu` VALUES (46, 138, 231);
INSERT INTO `sys_role_menu` VALUES (47, 138, 232);
INSERT INTO `sys_role_menu` VALUES (48, 138, 235);
INSERT INTO `sys_role_menu` VALUES (49, 138, 243);
INSERT INTO `sys_role_menu` VALUES (50, 138, 242);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                             `id` bigint(20) NOT NULL COMMENT '主键',
                             `username` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
                             `nickname` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
                             `gender` tinyint(1) NULL DEFAULT 0 COMMENT '性别(0:未知,1:男;2:女))',
                             `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
                             `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
                             `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户头像',
                             `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
                             `status` tinyint(1) NULL DEFAULT 1 COMMENT '用户状态((1:正常;0:禁用))',
                             `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
                             `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标识(0:未删除;1:已删除)',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `ux_sys_user_username`(`username`) USING BTREE COMMENT '唯一索引，sys_user表，username属性',
                             UNIQUE INDEX `ux_sys_user_email`(`email`) USING BTREE COMMENT '唯一索引，sys_user表，email属性',
                             UNIQUE INDEX `ux_sys_user_mobile`(`phone_number`) USING BTREE COMMENT '唯一索引，sys_user表，mobile属性'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (128, 'admin', '系统管理员', 1, '$2a$10$bET2vCsRHAkTSfrJpymJsONyy/5OvhPwGOYNgP37gPNNs.khTibHi', 5, NULL, NULL, 1, '', 0, '2024-05-12 19:51:39', '2025-01-27 15:11:32');
INSERT INTO `sys_user` VALUES (129, 'demo_admin', '演示管理员', 1, '$2a$10$uXHIwFoduuwcJsBGN9fV9.9UupJD5BB5AMa5CCK2SPyduXQdHolj2', 31, NULL, NULL, 1, NULL, 0, '2024-09-15 14:19:38', '2025-01-17 23:17:58');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
                                  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
                                  `role_id` int(11) NOT NULL COMMENT '角色ID',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_role_id`(`role_id`) USING BTREE COMMENT 'role_id 外键',
                                  INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT 'user_id 外键'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 128, 128);
INSERT INTO `sys_user_role` VALUES (2, 129, 138);

SET FOREIGN_KEY_CHECKS = 1;
