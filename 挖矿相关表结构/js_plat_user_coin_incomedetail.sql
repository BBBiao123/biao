/*
Navicat MySQL Data Transfer

Source Server         : TEST
Source Server Version : 50725
Source Host           : rm-j6c6mdh161ts3o52bco.mysql.rds.aliyuncs.com:3306
Source Database       : bbex

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-07-06 19:39:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for js_plat_user_coin_incomedetail
-- ----------------------------
DROP TABLE IF EXISTS `js_plat_user_coin_incomedetail`;
CREATE TABLE `js_plat_user_coin_incomedetail` (
  `id` varchar(64) NOT NULL COMMENT '主键ID',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `coin_plat_symbol` varchar(45) DEFAULT NULL COMMENT '平台币种',
  `coin_symbol` varchar(45) DEFAULT NULL COMMENT '币种',
  `income_date` timestamp NULL DEFAULT NULL COMMENT '收益日期',
  `detail_income` decimal(32,16) DEFAULT NULL COMMENT '收益',
  `detail_reward` decimal(32,16) DEFAULT NULL COMMENT '奖励',
  `create_date` timestamp NULL DEFAULT NULL COMMENT '创建日期',
  `update_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改者',
  `version` bigint(20) DEFAULT NULL COMMENT '版本',
  `statics_income` decimal(32,16) DEFAULT NULL,
  `team_record` decimal(32,16) DEFAULT NULL COMMENT '团队业绩',
  `team_community_record` decimal(32,16) DEFAULT NULL,
  `team_level` int(4) DEFAULT NULL COMMENT '等级',
  `community_statics_income` decimal(32,16) DEFAULT NULL COMMENT '社区静态收益',
  `node_number` int(10) DEFAULT NULL COMMENT '直推节点个数',
  `reality_statics_income` decimal(32,16) DEFAULT NULL COMMENT '实际静态收益',
  `community_manage_reward` decimal(32,16) DEFAULT NULL COMMENT '社区管理奖',
  `equality_reward` decimal(32,16) DEFAULT NULL COMMENT '平级奖励',
  `dynamics_income` decimal(32,16) DEFAULT NULL COMMENT '动态收益',
  `refer_id` varchar(64) DEFAULT NULL COMMENT '上级ID',
  `sum_revenue` decimal(32,16) DEFAULT NULL COMMENT '总收益',
  `valid_num` bigint(32) DEFAULT NULL,
  `community_sum_manage_reward` decimal(32,16) DEFAULT NULL COMMENT '总的社区管理奖',
  `team_coin_record` decimal(32,16) DEFAULT NULL COMMENT '社区总额-最大值',
  `level_difference_reward` decimal(32,16) DEFAULT NULL COMMENT '级差奖',
  `one_level_income` decimal(32,16) DEFAULT NULL COMMENT '一级邀请静态收益',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='余币宝收益明细表';
