/*
Navicat MySQL Data Transfer

Source Server         : TEST
Source Server Version : 50725
Source Host           : rm-j6c6mdh161ts3o52bco.mysql.rds.aliyuncs.com:3306
Source Database       : bbex

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-07-18 23:15:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for js_plat_user_income_incomedetail
-- ----------------------------
DROP TABLE IF EXISTS `js_plat_user_income_incomedetail`;
CREATE TABLE `js_plat_user_income_incomedetail` (
  `id` varchar(64) NOT NULL COMMENT '主键ID',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `coin_plat_symbol` varchar(45) DEFAULT NULL COMMENT '平台币种',
  `coin_symbol` varchar(45) DEFAULT NULL COMMENT '币种',
  `income_date` timestamp NULL DEFAULT NULL COMMENT '收益日期',
  `detail_reward` decimal(32,16) DEFAULT NULL COMMENT '奖励',
  `create_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改者',
  `version` bigint(20) DEFAULT NULL COMMENT '版本',
  `reward_type` varchar(64) DEFAULT NULL COMMENT '奖励类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='挖矿部落按类型收益明细表';
