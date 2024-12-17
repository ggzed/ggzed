/*
 Navicat Premium Data Transfer

 Source Server         : yf.wiki
 Source Server Type    : MySQL
 Source Server Version : 50744
 Source Host           : 8.137.57.215:3306
 Source Schema         : vue3_springboot3_admin

 Target Server Type    : MySQL
 Target Server Version : 50744
 File Encoding         : 65001

 Date: 16/12/2024 21:09:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
                              `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `function_author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '作者',
                              `function_notes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '方法注释',
                              `table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库表名',
                              `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '数据库表描述',
                              `class_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类名(根据数据库表名生成)',
                              `log_type` tinyint(1) NULL DEFAULT 1 COMMENT '是否记录日志(0:不记录日志,1:记录Aop注解日志)',
                              `gen_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '生成类型(1:单表,2:树形结构)',
                              `boot_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'mybatis-plus' COMMENT 'Springboot生成类型',
                              `front_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'vue_hook' COMMENT '前端生成类型',
                              `package_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '生成包名',
                              `module_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '模块名（外层包名）',
                              `business_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务名（前端 : 内层包名 , 后端并不使用）',
                              `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                              `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '生成代码-数据库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gen_table_fields
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_fields`;
CREATE TABLE `gen_table_fields`  (
                                     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                     `table_id` int(11) NOT NULL COMMENT '所属表ID',
                                     `column_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库列名',
                                     `column_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库类型',
                                     `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '数据库字段描述',
                                     `java_field` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'JAVA字段名',
                                     `java_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'JAVA类型',
                                     `ts_field` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ts名',
                                     `ts_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ts类型',
                                     `is_pk` tinyint(1) NULL DEFAULT 0 COMMENT '是否主键（1:是,0:否）',
                                     `is_increment` tinyint(1) NULL DEFAULT 0 COMMENT '是否自增（1:是,0:否）',
                                     `is_required` tinyint(1) NULL DEFAULT 0 COMMENT '是否必填（1:是,0:否）',
                                     `is_insert_edit` tinyint(1) NULL DEFAULT 0 COMMENT '是否为插入/修改字段（1:是,0:否）',
                                     `is_show` tinyint(1) NULL DEFAULT 0 COMMENT '是否展示字段（1:是,0:否）',
                                     `is_query` tinyint(1) NULL DEFAULT 0 COMMENT '是否查询字段（1:是,0:否）',
                                     `query_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
                                     `html_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
                                     `dict_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
                                     `sort` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '排序',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 251 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '生成代码-数据库表字段' ROW_FORMAT = Dynamic;

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
                                   INDEX `idx_sender_receiver_time`(`sender_id`, `receiver_id`, `create_time`) USING BTREE COMMENT '聚合索引  sender  receiver time ',
                                   INDEX `idx_receiver_sender_time`(`receiver_id`, `sender_id`, `create_time`) USING BTREE COMMENT '聚合索引 receiver  sender time '
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'websocket消息记录表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 177 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (129, '研发部门', 0, '0', 0, 1, '2024-04-19 10:34:39', '2024-07-26 16:42:26');
INSERT INTO `sys_dept` VALUES (130, '市场部门', 0, '0', 0, 1, '2024-04-19 10:34:39', '2024-06-07 11:26:18');
INSERT INTO `sys_dept` VALUES (131, '行政部门', 0, '0', 0, 1, '2024-04-19 10:34:39', '2024-06-07 11:26:22');
INSERT INTO `sys_dept` VALUES (132, '运营部门', 0, '0', 0, 1, '2024-04-19 10:34:39', '2024-06-07 11:26:25');
INSERT INTO `sys_dept` VALUES (133, '产品部门', 0, '0', 0, 1, '2024-04-19 10:34:39', '2024-06-07 11:26:40');
INSERT INTO `sys_dept` VALUES (134, '综合部门', 0, '0', 0, 1, '2024-04-19 10:34:39', '2024-06-07 11:26:45');
INSERT INTO `sys_dept` VALUES (135, '人力资源部门', 0, '0', 0, 1, '2024-04-19 10:34:39', '2024-06-07 11:26:50');
INSERT INTO `sys_dept` VALUES (136, '财务部门', 0, '0', 0, 1, '2024-04-19 10:34:39', '2024-06-07 11:26:53');
INSERT INTO `sys_dept` VALUES (137, '技术部门', 129, '0,129', 0, 1, '2024-04-19 10:34:39', '2024-07-26 16:40:47');
INSERT INTO `sys_dept` VALUES (138, '市场营销部门', 130, '0,130', 0, 1, '2024-04-19 10:34:39', '2024-04-19 10:34:39');
INSERT INTO `sys_dept` VALUES (139, '运营管理部门', 132, '0,132', 0, 1, '2024-04-19 10:34:39', '2024-04-19 10:34:39');
INSERT INTO `sys_dept` VALUES (140, '产品研发部门', 133, '0,133', 0, 1, '2024-04-19 10:34:39', '2024-04-19 10:34:39');
INSERT INTO `sys_dept` VALUES (141, '客户服务部门', 132, '0,132', 0, 1, '2024-04-19 10:34:39', '2024-04-19 10:34:39');
INSERT INTO `sys_dept` VALUES (142, '行政办公室', 131, '0,131', 0, 1, '2024-04-19 10:34:39', '2024-04-19 10:34:39');
INSERT INTO `sys_dept` VALUES (143, '市场推广部门', 130, '0,130', 0, 1, '2024-04-19 10:34:39', '2024-04-19 10:34:39');
INSERT INTO `sys_dept` VALUES (144, '销售部门', 130, '0,130', 0, 1, '2024-04-19 10:34:39', '2024-04-19 10:34:39');
INSERT INTO `sys_dept` VALUES (145, '采购部门', 132, '0,132', 0, 1, '2024-04-19 10:34:39', '2024-04-19 10:34:39');
INSERT INTO `sys_dept` VALUES (146, '项目管理部门', 132, '0,132', 0, 1, '2024-04-19 10:34:39', '2024-04-19 10:34:39');
INSERT INTO `sys_dept` VALUES (147, '用户体验部门', 133, '0,133', 0, 1, '2024-04-19 10:34:39', '2024-04-19 10:34:39');
INSERT INTO `sys_dept` VALUES (148, '研究部门', 129, '0,129', 0, 1, '2024-04-19 10:34:39', '2024-07-26 16:40:47');
INSERT INTO `sys_dept` VALUES (149, '质量管理部门', 133, '0,133', 0, 1, '2024-04-19 10:34:39', '2024-04-19 10:34:39');
INSERT INTO `sys_dept` VALUES (150, '安全部门', 132, '0,132', 0, 1, '2024-04-19 10:34:39', '2024-04-19 10:34:39');

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
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 'menu', '目录', '1', 0, 1, 1, '【目录】', '2024-07-30 07:25:25', 128, '2024-07-30 07:25:25', 128);
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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '菜单', 'menu', 1, '菜单类型', '2024-07-30 07:25:25', '2024-07-31 00:33:11');
INSERT INTO `sys_dict_type` VALUES (2, '性别', 'gender', 1, '性别', '2024-07-30 07:25:25', '2024-07-30 07:25:25');
INSERT INTO `sys_dict_type` VALUES (3, '数据权限', 'data_permission', 1, '数据权限', '2024-07-30 07:25:25', '2024-07-30 07:25:25');
INSERT INTO `sys_dict_type` VALUES (4, '业务', 'business', 1, '操作日志中定义的业务类型', '2024-07-30 07:25:25', '2024-07-30 07:25:25');
INSERT INTO `sys_dict_type` VALUES (5, '客户端', 'operator_type', 1, '操作日志中定义的操作日志客户端', '2024-07-30 07:25:25', '2024-07-30 07:25:25');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
                             `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单主键',
                             `parent_id` int(11) NOT NULL COMMENT '父菜单ID',
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
) ENGINE = InnoDB AUTO_INCREMENT = 221 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (128, 0, '0', '系统管理', 1, '/system', 'Layout', '', 0, 0, 0, NULL, 'system', 1, 0, '/system/user', '2024-04-18 09:12:21', '2024-08-02 22:12:04');
INSERT INTO `sys_menu` VALUES (129, 128, '0,128', '用户管理', 2, 'user', 'system/user/index', 'system:user:list', 0, 1, NULL, 0, 'user', 1, 1, NULL, '2024-04-18 09:12:21', '2024-05-22 18:39:42');
INSERT INTO `sys_menu` VALUES (130, 129, '0,128,129', '用户新增', 4, '', NULL, 'system:user:save', NULL, NULL, NULL, NULL, '', NULL, 1, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (131, 129, '0,128,129', '用户删除', 4, '', NULL, 'system:user:delete', NULL, NULL, NULL, NULL, '', NULL, 2, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (132, 129, '0,128,129', '用户修改', 4, '', NULL, 'system:user:update', NULL, NULL, NULL, NULL, '', NULL, 3, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (133, 129, '0,128,129', '用户信息导入', 4, '', NULL, 'system:user:import', NULL, NULL, NULL, NULL, '', NULL, 4, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (134, 129, '0,128,129', '用户信息导出', 4, '', NULL, 'system:user:export', NULL, NULL, NULL, NULL, '', NULL, 5, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (136, 128, '0,128', '角色管理', 2, 'role', 'system/role/index', 'system:role:list', 0, 1, NULL, NULL, 'role', 1, 1, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
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
INSERT INTO `sys_menu` VALUES (160, 128, '0,128', '部门管理', 2, 'dept', 'system/dept/index', 'system:dept:list', 0, 1, NULL, NULL, 'dept', 1, 1, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (161, 160, '0,128,160', '部门新增', 4, '', NULL, 'system:dept:save', NULL, NULL, NULL, NULL, '', NULL, 1, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (162, 160, '0,128,160', '部门删除', 4, '', NULL, 'system:dept:delete', NULL, NULL, NULL, NULL, '', NULL, 2, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (163, 160, '0,128,160', '部门修改', 4, '', NULL, 'system:dept:update', NULL, NULL, NULL, NULL, '', NULL, 3, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (164, 160, '0,128,160', '部门信息导入', 4, '', NULL, 'system:dept:import', NULL, NULL, NULL, NULL, '', NULL, 4, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (165, 160, '0,128,160', '部门信息导出', 4, '', NULL, 'system:dept:export', NULL, NULL, NULL, NULL, '', NULL, 5, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (166, 128, '0,128', '字典管理', 2, 'dict', 'system/dict/index', 'system:dict-type:list', 0, 1, NULL, NULL, 'dict', 1, 1, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (167, 166, '0,128,166', '字典新增', 4, '', NULL, 'system:dict-type:save', NULL, NULL, NULL, NULL, '', NULL, 1, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (168, 166, '0,128,166', '字典删除', 4, '', NULL, 'system:dict-type:delete', NULL, NULL, NULL, NULL, '', NULL, 2, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (169, 166, '0,128,166', '字典修改', 4, '', NULL, 'system:dict-type:update', NULL, NULL, NULL, NULL, '', NULL, 3, NULL, '2024-04-18 09:12:21', '2024-04-18 09:12:21');
INSERT INTO `sys_menu` VALUES (172, 0, '0', 'AI助手', 1, '/ai', 'Layout', '', 0, 1, 1, NULL, 'ai-assistant', NULL, 3, '/ai/easy-chat', '2024-05-22 13:24:01', '2024-08-02 19:09:06');
INSERT INTO `sys_menu` VALUES (173, 172, '0,172', 'EasyChat', 2, 'easy-chat', 'ai/easy-chat/index', 'ai:easy_chat:list', 0, 1, 0, NULL, 'EasyChat', NULL, 1, NULL, '2024-05-22 13:28:58', '2024-05-22 13:44:04');
INSERT INTO `sys_menu` VALUES (174, 0, '0', '生成代码助手', 1, '/generate', 'Layout', '', 0, 1, 1, NULL, 'code-assistant', NULL, 3, '/generate/gen-code', '2024-05-22 14:12:52', '2024-08-02 19:08:49');
INSERT INTO `sys_menu` VALUES (175, 174, '0,174', '基础CRUD生成', 2, 'gen-code', 'generate/gen-code/index', 'generate:gen:list', 0, 1, 0, NULL, 'gen-code', NULL, 1, NULL, '2024-05-22 14:15:13', '2024-07-27 15:13:41');
INSERT INTO `sys_menu` VALUES (176, 174, '0,174', '低代码开发', 2, 'low-code', 'generate/low-code/index', 'generate:low:list', 0, 1, 0, NULL, 'low-code', NULL, 1, NULL, '2024-05-22 14:16:54', '2024-07-27 15:13:29');
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
INSERT INTO `sys_menu` VALUES (196, 194, '0,194', '图片NSFW检测', 2, 'nsfw', 'demo/nsfw/index', 'demo:nsfw:list', 0, 1, 0, 0, 'nsfw', NULL, 1, NULL, '2024-07-21 10:46:46', '2024-08-28 15:30:05');
INSERT INTO `sys_menu` VALUES (197, 194, '0,194', '图片物品检测', 2, 'object-search', 'demo/object-search/index', 'demo:object-search:list', 0, 1, 0, 0, 'image-detection', NULL, 1, NULL, '2024-07-21 11:00:42', '2024-07-21 11:00:42');
INSERT INTO `sys_menu` VALUES (198, 194, '0,194', 'ffmpeg', 2, 'ffmpeg', 'demo/ffmpeg/index', 'demo:ffmpeg:list', 0, 1, 0, 0, 'ffmpeg', NULL, 1, NULL, '2024-07-21 11:05:20', '2024-07-21 11:45:06');
INSERT INTO `sys_menu` VALUES (199, 194, '0,194', 'markdown', 2, 'markdown', 'demo/markdown/index', 'demo:markdown:list', 0, 1, 0, 0, 'markdown', NULL, 1, NULL, '2024-07-21 11:44:40', '2024-07-21 11:44:40');
INSERT INTO `sys_menu` VALUES (200, 209, '0,209', '帮助文档', 2, 'help', 'doc/help/index', 'doc:help:list', 0, 1, 0, 0, 'help-documentation', NULL, 1, NULL, '2024-07-21 11:51:42', '2024-08-02 21:22:21');
INSERT INTO `sys_menu` VALUES (201, 194, '0,194', 'ocr', 2, 'ocr', 'demo/ocr/index', 'demo:ocr:list', 0, 1, 0, 0, 'ocr', NULL, 1, NULL, '2024-07-21 14:31:36', '2024-07-21 14:31:58');
INSERT INTO `sys_menu` VALUES (202, 128, '0,128', '字典详情数据', 2, 'dict-detail/:id', 'system/dict/detail/index', 'system:dict-data:list', 1, 1, 0, 0, 'dict', NULL, 1, NULL, '2024-07-28 16:43:04', '2024-07-28 17:16:55');
INSERT INTO `sys_menu` VALUES (203, 166, '0,128,166', '字典信息导入', 4, '', '', 'system:dict-type:import', 1, 0, 0, 0, '', NULL, 4, NULL, '2024-07-28 16:44:45', '2024-07-28 16:44:53');
INSERT INTO `sys_menu` VALUES (204, 166, '0,128,166', '字典信息导出', 4, '', '', 'system:dict-type:export', 1, 0, 0, 0, '', NULL, 5, NULL, '2024-07-28 16:45:21', '2024-07-28 16:45:35');
INSERT INTO `sys_menu` VALUES (205, 202, '0,128,202', '字典详情数据新增', 4, '', '', 'system:dict-data:save', 1, 0, 0, 0, '', NULL, 1, NULL, '2024-07-28 16:47:34', '2024-07-28 17:16:55');
INSERT INTO `sys_menu` VALUES (206, 202, '0,128,202', '字典详情数据修改', 4, '', '', 'system:dict-data:update', 1, 0, 0, 0, '', NULL, 2, NULL, '2024-07-28 16:48:17', '2024-07-28 17:16:55');
INSERT INTO `sys_menu` VALUES (207, 202, '0,128,202', '字典详情数据删除', 4, '', '', 'system:dict-data:delete', 1, 0, 0, 0, '', NULL, 3, NULL, '2024-07-28 16:48:44', '2024-07-28 17:16:55');
INSERT INTO `sys_menu` VALUES (208, 194, '0,194', 'echarts', 2, 'echarts', 'demo/echarts/index', 'demo:echarts:list', 0, 1, 0, 0, 'echarts', NULL, 1, NULL, '2024-07-31 20:03:16', '2024-07-31 20:03:16');
INSERT INTO `sys_menu` VALUES (209, 0, '0', '平台文档', 1, '/doc', 'Layout', '', 0, 1, 0, 0, 'doc', NULL, 1, NULL, '2024-08-02 21:12:41', '2024-08-02 21:22:08');
INSERT INTO `sys_menu` VALUES (211, 209, '0,209', '组件文档', 2, 'component', 'doc/component/index', 'doc:component:list', 0, 1, 0, 0, 'component', NULL, 3, NULL, '2024-08-02 21:23:16', '2024-08-02 21:23:30');
INSERT INTO `sys_menu` VALUES (212, 209, '0,209', '稀土掘金', 3, 'https://juejin.cn/post/7424399072532447244', '', NULL, 0, 1, 0, 0, 'juejin', NULL, 1, NULL, '2024-08-02 21:47:41', '2024-12-16 21:01:34');
INSERT INTO `sys_menu` VALUES (213, 209, '0,209', 'hooks文档', 2, 'hook', 'doc/hook/index', 'doc:hook:list', 0, 1, 0, 0, 'help-documentation', NULL, 1, NULL, '2024-08-03 22:02:21', '2024-08-03 22:02:36');
INSERT INTO `sys_menu` VALUES (214, 148, '0,181,148', '删除操作日志信息', 4, '', '', 'monitor:operation-log:delete', 1, 0, 0, 0, '', NULL, 1, NULL, '2024-08-05 15:18:28', '2024-08-05 15:18:28');
INSERT INTO `sys_menu` VALUES (215, 136, '0,128,136', '角色授权菜单', 4, '', '', 'system:role:permission', 1, 0, 0, 0, '', NULL, 0, NULL, '2024-08-11 14:32:54', '2024-08-11 14:33:00');
INSERT INTO `sys_menu` VALUES (216, 194, '0,194', '生成二维码', 2, 'qr', 'demo/qr/index', 'demo:qr:list', 0, 1, 0, 0, 'qr', NULL, 1, NULL, '2024-08-28 18:56:41', '2024-08-28 18:56:41');
INSERT INTO `sys_menu` VALUES (217, 194, '0,194', '节日头像', 2, 'festival-avatar', 'demo/festival-avatar/index', 'demo:festival-avatar:list', 0, 1, 0, 0, 'festival-avatar', NULL, 1, NULL, '2024-09-28 14:50:28', '2024-09-28 14:50:28');
INSERT INTO `sys_menu` VALUES (218, 182, '0,181,182', '踢出在线用户', 4, '', '', 'monitor:online-user:kick-out', 1, 0, 0, 0, '', NULL, 1, NULL, '2024-09-05 11:00:44', '2024-09-05 11:50:48');
INSERT INTO `sys_menu` VALUES (219, 194, '0,194', '动态开关逻辑', 2, 'dynamic-logic-switch', 'demo/dynamic-logic-switch/index', 'demo:dynamic-logic-switch:list', 0, 1, 0, 0, 'system', NULL, 1, NULL, '2024-11-28 09:45:00', '2024-11-28 09:45:00');
INSERT INTO `sys_menu` VALUES (220, 194, '0,194', 'markdown展示', 2, 'markdown-detail/:id', 'demo/markdown/detail/index', 'demo:markdown-detail:list', 1, 1, 0, 0, 'markdown', NULL, 1, NULL, '2024-11-28 09:49:10', '2024-11-28 09:49:51');

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
) ENGINE = InnoDB AUTO_INCREMENT = 148 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (128, '管理员', 'ADMIN', 0, 1, 0, '2024-04-18 17:46:02', '2024-06-11 23:43:01');
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
                                  `role_id` int(11) NOT NULL COMMENT '角色ID',
                                  `menu_id` int(11) NOT NULL COMMENT '菜单ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (128, 128);
INSERT INTO `sys_role_menu` VALUES (128, 129);
INSERT INTO `sys_role_menu` VALUES (128, 130);
INSERT INTO `sys_role_menu` VALUES (128, 131);
INSERT INTO `sys_role_menu` VALUES (128, 132);
INSERT INTO `sys_role_menu` VALUES (128, 133);
INSERT INTO `sys_role_menu` VALUES (128, 134);
INSERT INTO `sys_role_menu` VALUES (128, 136);
INSERT INTO `sys_role_menu` VALUES (128, 137);
INSERT INTO `sys_role_menu` VALUES (128, 138);
INSERT INTO `sys_role_menu` VALUES (128, 139);
INSERT INTO `sys_role_menu` VALUES (128, 140);
INSERT INTO `sys_role_menu` VALUES (128, 141);
INSERT INTO `sys_role_menu` VALUES (128, 142);
INSERT INTO `sys_role_menu` VALUES (128, 143);
INSERT INTO `sys_role_menu` VALUES (128, 144);
INSERT INTO `sys_role_menu` VALUES (128, 145);
INSERT INTO `sys_role_menu` VALUES (128, 146);
INSERT INTO `sys_role_menu` VALUES (128, 147);
INSERT INTO `sys_role_menu` VALUES (128, 148);
INSERT INTO `sys_role_menu` VALUES (128, 154);
INSERT INTO `sys_role_menu` VALUES (128, 156);
INSERT INTO `sys_role_menu` VALUES (128, 157);
INSERT INTO `sys_role_menu` VALUES (128, 160);
INSERT INTO `sys_role_menu` VALUES (128, 161);
INSERT INTO `sys_role_menu` VALUES (128, 162);
INSERT INTO `sys_role_menu` VALUES (128, 163);
INSERT INTO `sys_role_menu` VALUES (128, 164);
INSERT INTO `sys_role_menu` VALUES (128, 165);
INSERT INTO `sys_role_menu` VALUES (128, 166);
INSERT INTO `sys_role_menu` VALUES (128, 167);
INSERT INTO `sys_role_menu` VALUES (128, 168);
INSERT INTO `sys_role_menu` VALUES (128, 169);
INSERT INTO `sys_role_menu` VALUES (128, 172);
INSERT INTO `sys_role_menu` VALUES (128, 173);
INSERT INTO `sys_role_menu` VALUES (128, 174);
INSERT INTO `sys_role_menu` VALUES (128, 175);
INSERT INTO `sys_role_menu` VALUES (128, 176);
INSERT INTO `sys_role_menu` VALUES (128, 177);
INSERT INTO `sys_role_menu` VALUES (128, 178);
INSERT INTO `sys_role_menu` VALUES (128, 179);
INSERT INTO `sys_role_menu` VALUES (128, 180);
INSERT INTO `sys_role_menu` VALUES (128, 181);
INSERT INTO `sys_role_menu` VALUES (128, 182);
INSERT INTO `sys_role_menu` VALUES (128, 183);
INSERT INTO `sys_role_menu` VALUES (128, 184);
INSERT INTO `sys_role_menu` VALUES (128, 185);
INSERT INTO `sys_role_menu` VALUES (128, 187);
INSERT INTO `sys_role_menu` VALUES (128, 188);
INSERT INTO `sys_role_menu` VALUES (128, 189);
INSERT INTO `sys_role_menu` VALUES (128, 190);
INSERT INTO `sys_role_menu` VALUES (128, 194);
INSERT INTO `sys_role_menu` VALUES (128, 196);
INSERT INTO `sys_role_menu` VALUES (128, 197);
INSERT INTO `sys_role_menu` VALUES (128, 198);
INSERT INTO `sys_role_menu` VALUES (128, 199);
INSERT INTO `sys_role_menu` VALUES (128, 200);
INSERT INTO `sys_role_menu` VALUES (128, 201);
INSERT INTO `sys_role_menu` VALUES (128, 202);
INSERT INTO `sys_role_menu` VALUES (128, 203);
INSERT INTO `sys_role_menu` VALUES (128, 204);
INSERT INTO `sys_role_menu` VALUES (128, 205);
INSERT INTO `sys_role_menu` VALUES (128, 206);
INSERT INTO `sys_role_menu` VALUES (128, 207);
INSERT INTO `sys_role_menu` VALUES (128, 208);
INSERT INTO `sys_role_menu` VALUES (128, 209);
INSERT INTO `sys_role_menu` VALUES (128, 211);
INSERT INTO `sys_role_menu` VALUES (128, 212);
INSERT INTO `sys_role_menu` VALUES (128, 213);
INSERT INTO `sys_role_menu` VALUES (128, 214);
INSERT INTO `sys_role_menu` VALUES (128, 215);
INSERT INTO `sys_role_menu` VALUES (138, 128);
INSERT INTO `sys_role_menu` VALUES (138, 129);
INSERT INTO `sys_role_menu` VALUES (138, 194);
INSERT INTO `sys_role_menu` VALUES (138, 196);
INSERT INTO `sys_role_menu` VALUES (138, 197);
INSERT INTO `sys_role_menu` VALUES (138, 198);
INSERT INTO `sys_role_menu` VALUES (138, 199);
INSERT INTO `sys_role_menu` VALUES (138, 136);
INSERT INTO `sys_role_menu` VALUES (138, 200);
INSERT INTO `sys_role_menu` VALUES (138, 201);
INSERT INTO `sys_role_menu` VALUES (138, 202);
INSERT INTO `sys_role_menu` VALUES (138, 142);
INSERT INTO `sys_role_menu` VALUES (138, 208);
INSERT INTO `sys_role_menu` VALUES (138, 209);
INSERT INTO `sys_role_menu` VALUES (138, 211);
INSERT INTO `sys_role_menu` VALUES (138, 148);
INSERT INTO `sys_role_menu` VALUES (138, 212);
INSERT INTO `sys_role_menu` VALUES (138, 213);
INSERT INTO `sys_role_menu` VALUES (138, 154);
INSERT INTO `sys_role_menu` VALUES (138, 160);
INSERT INTO `sys_role_menu` VALUES (138, 166);
INSERT INTO `sys_role_menu` VALUES (138, 172);
INSERT INTO `sys_role_menu` VALUES (138, 173);
INSERT INTO `sys_role_menu` VALUES (138, 174);
INSERT INTO `sys_role_menu` VALUES (138, 175);
INSERT INTO `sys_role_menu` VALUES (138, 176);
INSERT INTO `sys_role_menu` VALUES (138, 177);
INSERT INTO `sys_role_menu` VALUES (138, 178);
INSERT INTO `sys_role_menu` VALUES (138, 179);
INSERT INTO `sys_role_menu` VALUES (138, 180);
INSERT INTO `sys_role_menu` VALUES (138, 181);
INSERT INTO `sys_role_menu` VALUES (138, 187);
INSERT INTO `sys_role_menu` VALUES (138, 188);
INSERT INTO `sys_role_menu` VALUES (138, 189);
INSERT INTO `sys_role_menu` VALUES (138, 190);
INSERT INTO `sys_role_menu` VALUES (128, 216);
INSERT INTO `sys_role_menu` VALUES (138, 216);
INSERT INTO `sys_role_menu` VALUES (138, 182);
INSERT INTO `sys_role_menu` VALUES (138, 183);
INSERT INTO `sys_role_menu` VALUES (138, 184);
INSERT INTO `sys_role_menu` VALUES (128, 217);
INSERT INTO `sys_role_menu` VALUES (138, 217);
INSERT INTO `sys_role_menu` VALUES (128, 219);
INSERT INTO `sys_role_menu` VALUES (128, 220);
INSERT INTO `sys_role_menu` VALUES (138, 219);
INSERT INTO `sys_role_menu` VALUES (138, 220);
INSERT INTO `sys_role_menu` VALUES (128, 218);

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
INSERT INTO `sys_user` VALUES (128, 'admin', '系统管理员', 1, '$2a$10$oRkVVqCLRJKCiqtxHRm7u.b7FaK9i6Tfbj9GlojryIZaGNuKVKyB6', 129, '', '18222222281', 1, '', 0, '2024-05-12 19:51:39', '2024-09-01 17:23:08');
INSERT INTO `sys_user` VALUES (129, 'demo_admin', '演示管理员', 1, '$2a$10$OsKkgnscTSbAvFe9ueZ5huVOhA7wm8LN4tmlAF/OmA/IEAYfGY0DW', 147, '', NULL, 1, NULL, 0, '2024-09-15 14:19:38', '2024-09-15 14:19:38');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
                                  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
                                  `role_id` int(11) NOT NULL COMMENT '角色ID',
                                  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (128, 128);
INSERT INTO `sys_user_role` VALUES (129, 138);

SET FOREIGN_KEY_CHECKS = 1;
