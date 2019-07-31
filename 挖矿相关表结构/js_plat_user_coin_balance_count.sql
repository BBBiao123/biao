/*
Navicat MySQL Data Transfer

Source Server         : TEST
Source Server Version : 50725
Source Host           : rm-j6c6mdh161ts3o52bco.mysql.rds.aliyuncs.com:3306
Source Database       : bbex

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-07-28 21:31:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for js_plat_user_coin_balance_count
-- ----------------------------
DROP TABLE IF EXISTS `js_plat_user_coin_balance_count`;
CREATE TABLE `js_plat_user_coin_balance_count` (
  `id` varchar(64) NOT NULL COMMENT '主键id',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `coin_symbol` varchar(45) DEFAULT NULL COMMENT '币种(资产)',
  `coin_plat_symbol` varchar(45) DEFAULT NULL COMMENT '币种(平台收益)',
  `coin_balance` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '总资产',
  `day_rate` decimal(32,16) DEFAULT NULL COMMENT '日利率',
  `accumul_income` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '累计收益',
  `yesterday_income` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '昨日收益',
  `accumul_reward` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '累计奖励',
  `yesterday_reward` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '昨日奖励',
  `create_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `version` bigint(20) DEFAULT NULL COMMENT '版本',
  `coin_num` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '数量',
  `refer_id` varchar(64) DEFAULT NULL COMMENT '上级ID',
  `yesterday_statics_income` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '昨日静态收益',
  `yesterday_equality_reward` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '昨日平级奖',
  `yesterday_dynamics_income` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '昨日动态收益',
  `yesterday_community_reward` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '昨日社区奖励',
  `team_level` varchar(64) DEFAULT NULL COMMENT '级别',
  `one_invite` bigint(32) DEFAULT NULL COMMENT '一级邀请人数',
  `team_amount` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '团队总额',
  `team_community_amount` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '小区总额',
  `sum_revenue` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '总收入（收益+奖励）',
  `valid_num` bigint(32) DEFAULT NULL COMMENT '有效用户数',
  `yesterday_revenue` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '昨日收入（收益+奖励）',
  `mobile` varchar(11) DEFAULT NULL,
  `mail` varchar(64) DEFAULT NULL,
  `equality_reward` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '平级奖励',
  `community_manage_reward` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '社区管理奖',
  `share_reward` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '分享奖励',
  `scalping_reward` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '刷单奖励',
  `differential_reward` decimal(32,16) DEFAULT '0.0000000000000000' COMMENT '级益奖',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='挖矿收入统计表';
