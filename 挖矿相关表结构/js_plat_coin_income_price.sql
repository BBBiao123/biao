/*
Navicat MySQL Data Transfer

Source Server         : TEST
Source Server Version : 50725
Source Host           : rm-j6c6mdh161ts3o52bco.mysql.rds.aliyuncs.com:3306
Source Database       : bbex

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-07-30 11:25:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for js_plat_coin_income_price
-- ----------------------------
DROP TABLE IF EXISTS `js_plat_coin_income_price`;
CREATE TABLE `js_plat_coin_income_price` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `create_date` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `price` decimal(32,16) DEFAULT NULL COMMENT '价格',
  `coin_plat_symbol` varchar(45) DEFAULT NULL COMMENT '平台币种',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收益核算对应平台币价格表';
