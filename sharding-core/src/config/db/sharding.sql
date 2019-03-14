/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : sharding_0000 sharding_0001 sharding_0002 sharding_0003

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-03-14 11:53:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_sharding_0000
-- ----------------------------
DROP TABLE IF EXISTS `t_sharding_0000`;
CREATE TABLE `t_sharding_0000` (
  `id` varchar(64) NOT NULL,
  `money` int(10) NOT NULL DEFAULT '0',
  `status` int(2) NOT NULL DEFAULT '0',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sharding_0001
-- ----------------------------
DROP TABLE IF EXISTS `t_sharding_0001`;
CREATE TABLE `t_sharding_0001` (
  `id` varchar(64) NOT NULL,
  `money` int(10) NOT NULL DEFAULT '0',
  `status` int(2) NOT NULL DEFAULT '0',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sharding_0002
-- ----------------------------
DROP TABLE IF EXISTS `t_sharding_0002`;
CREATE TABLE `t_sharding_0002` (
  `id` varchar(64) NOT NULL,
  `money` int(10) NOT NULL DEFAULT '0',
  `status` int(2) NOT NULL DEFAULT '0',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sharding_0003
-- ----------------------------
DROP TABLE IF EXISTS `t_sharding_0003`;
CREATE TABLE `t_sharding_0003` (
  `id` varchar(64) NOT NULL,
  `money` int(10) NOT NULL DEFAULT '0',
  `status` int(2) NOT NULL DEFAULT '0',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
