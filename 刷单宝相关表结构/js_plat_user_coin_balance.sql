/*
Navicat MySQL Data Transfer

Source Server         : local2
Source Server Version : 50724
Source Host           : 127.0.0.1:3306
Source Database       : bbex

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-05-13 17:02:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for js_plat_user_coin_balance
-- ----------------------------
DROP TABLE IF EXISTS `js_plat_user_coin_balance`;
CREATE TABLE `js_plat_user_coin_balance` (
  `id` varchar(64) NOT NULL COMMENT '主键id',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `coin_symbol` varchar(45) DEFAULT NULL COMMENT '币种',
  `coin_balance` decimal(32,16) DEFAULT NULL COMMENT '余额',
  `day_rate` decimal(32,16) DEFAULT NULL COMMENT '日利率',
  `accumul_income` decimal(32,16) DEFAULT NULL COMMENT '累计收益',
  `yesterday_income` decimal(32,16) DEFAULT NULL COMMENT '昨日收益',
  `accumul_reward` decimal(32,16) DEFAULT NULL COMMENT '累计奖励',
  `yesterday_reward` decimal(32,16) DEFAULT NULL COMMENT '昨日奖励',
  `create_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `version` bigint(20) DEFAULT NULL COMMENT '版本',
  `coin_num` decimal(32,16) DEFAULT NULL COMMENT '数量',
  `refer_id` varchar(64) DEFAULT NULL COMMENT '上级ID',
  `yesterday_statics_income` decimal(32,16) DEFAULT NULL COMMENT '昨日静态收益',
  `yesterday_equality_reward` decimal(32,16) DEFAULT NULL COMMENT '昨日平级奖',
  `yesterday_dynamics_income` decimal(32,16) DEFAULT NULL COMMENT '昨日动态收益',
  `yesterday_community_reward` decimal(32,16) DEFAULT NULL COMMENT '昨日社区奖励',
  `team_level` varchar(64) DEFAULT NULL COMMENT '级别',
  `one_invite` bigint(32) DEFAULT NULL COMMENT '一级邀请人数',
  `team_amount` decimal(32,16) DEFAULT NULL COMMENT '团队总额',
  `team_community_amount` decimal(32,16) DEFAULT NULL COMMENT '小区总额',
  `sum_revenue` decimal(32,16) DEFAULT NULL COMMENT '总收入（收益+奖励）',
  `valid_num` bigint(32) DEFAULT NULL COMMENT '有效用户数',
  `yesterday_revenue` decimal(32,16) DEFAULT NULL COMMENT '昨日收入（收益+奖励）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='余币宝用户信息表';
