/*
Navicat MySQL Data Transfer

Source Server         : local2
Source Server Version : 50724
Source Host           : 127.0.0.1:3306
Source Database       : bbex

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-07-05 23:58:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for js_plat_jackpot_income
-- ----------------------------
DROP TABLE IF EXISTS `js_plat_jackpot_income`;
CREATE TABLE `js_plat_jackpot_income` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `all_coin_income` decimal(32,16) DEFAULT NULL COMMENT '奖池金额',
  `coin_symbol` varchar(45) DEFAULT NULL COMMENT '币种',
  `create_date` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `reward_date` timestamp NULL DEFAULT NULL COMMENT '开奖时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='挖矿部落奖池信息表';
