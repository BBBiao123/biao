/*
Navicat MySQL Data Transfer

Source Server         : TEST
Source Server Version : 50725
Source Host           : rm-j6c6mdh161ts3o52bco.mysql.rds.aliyuncs.com:3306
Source Database       : bbex

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-07-06 19:38:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for js_plat_user_coin_balancechange
-- ----------------------------
DROP TABLE IF EXISTS `js_plat_user_coin_balancechange`;
CREATE TABLE `js_plat_user_coin_balancechange` (
  `id` varchar(64) NOT NULL COMMENT '主键id',
  `create_date` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户id',
  `coin_plat_symbol` varchar(45) DEFAULT NULL COMMENT '平台币种',
  `coin_symbol` varchar(45) DEFAULT NULL COMMENT '币种',
  `update_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(64) DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `flag` smallint(6) DEFAULT NULL COMMENT '转入转出标识',
  `coin_num` decimal(32,16) DEFAULT NULL COMMENT '数量',
  `mobile` varchar(11) DEFAULT NULL COMMENT '电话',
  `mail` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `accumul_income` decimal(32,16) DEFAULT NULL COMMENT '累计收益',
  `take_out_date` timestamp NULL DEFAULT NULL COMMENT '取出时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='刷单宝转入转出明细';
