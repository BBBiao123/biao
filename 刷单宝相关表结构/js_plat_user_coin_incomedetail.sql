/*
Navicat MySQL Data Transfer

Source Server         : local2
Source Server Version : 50724
Source Host           : 127.0.0.1:3306
Source Database       : bbex

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-06-17 19:05:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for js_plat_user_coin_incomedetail
-- ----------------------------
DROP TABLE IF EXISTS `js_plat_user_coin_incomedetail`;
CREATE TABLE `js_plat_user_coin_incomedetail` (
  `id` varchar(64) NOT NULL COMMENT '主键ID',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `coin_symbol` varchar(45) DEFAULT NULL COMMENT '币种',
  `income_date` date DEFAULT NULL COMMENT '收益日期',
  `detail_income` decimal(32,16) DEFAULT NULL COMMENT '收益',
  `detail_reward` decimal(32,16) DEFAULT NULL COMMENT '奖励',
  `create_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='余币宝收益明细表';
