/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 80016
Source Host           : localhost:3306
Source Database       : ib_company

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2022-04-04 18:26:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `net_asset` double(10,1) DEFAULT NULL,
  `currency` varchar(10) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for conid
-- ----------------------------
DROP TABLE IF EXISTS `conid`;
CREATE TABLE `conid` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `symbol` varchar(20) DEFAULT NULL,
  `conid` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=193 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for gougu_hold
-- ----------------------------
DROP TABLE IF EXISTS `gougu_hold`;
CREATE TABLE `gougu_hold` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL COMMENT '股票代码',
  `old_price` decimal(10,2) DEFAULT NULL,
  `old_date` varchar(20) DEFAULT NULL,
  `new_date` varchar(20) DEFAULT NULL,
  `new_price` decimal(10,2) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for gougu_plan
-- ----------------------------
DROP TABLE IF EXISTS `gougu_plan`;
CREATE TABLE `gougu_plan` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `new_date` varchar(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `action` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hold
-- ----------------------------
DROP TABLE IF EXISTS `hold`;
CREATE TABLE `hold` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `conid` int(11) DEFAULT NULL,
  `symbol` varchar(20) DEFAULT NULL,
  `position` int(10) DEFAULT NULL,
  `market_price` double(10,2) DEFAULT NULL,
  `market_value` double(10,2) DEFAULT NULL,
  `average_cost` double(10,2) DEFAULT NULL,
  `unrealized` double(10,1) DEFAULT NULL,
  `account_name` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for market
-- ----------------------------
DROP TABLE IF EXISTS `market`;
CREATE TABLE `market` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `is_close` int(1) DEFAULT NULL COMMENT '是否闭市,以美股开盘时间所在的中国时间为准',
  `open_time` varchar(10) DEFAULT NULL,
  `close_time` varchar(10) DEFAULT NULL,
  `close_covering` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `close_gougu` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for order_number
-- ----------------------------
DROP TABLE IF EXISTS `order_number`;
CREATE TABLE `order_number` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for trade_plan
-- ----------------------------
DROP TABLE IF EXISTS `trade_plan`;
CREATE TABLE `trade_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `symbol` varchar(20) DEFAULT NULL,
  `conid` int(11) DEFAULT NULL,
  `action` varchar(20) DEFAULT NULL,
  `money` double(10,1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
