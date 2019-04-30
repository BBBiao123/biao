/*
Navicat MySQL Data Transfer

Source Server         : local2
Source Server Version : 50724
Source Host           : 127.0.0.1:3306
Source Database       : bbex

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-04-30 10:26:22
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='余币宝收益明细表';
