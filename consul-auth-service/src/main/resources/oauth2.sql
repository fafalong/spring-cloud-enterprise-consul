/*
Navicat MySQL Data Transfer

Source Server         : local_mysql
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : oauth2

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2017-04-22 17:58:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for authority
-- ----------------------------
DROP TABLE IF EXISTS `authority`;
CREATE TABLE `authority` (
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of authority
-- ----------------------------
INSERT INTO `authority` VALUES ('ROLE_ADMIN');
INSERT INTO `authority` VALUES ('ROLE_USER');

-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(256) DEFAULT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  `authentication` blob,
  `refresh_token` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_access_token
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_refresh_token
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `username` varchar(50) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(500) DEFAULT NULL,
  `activated` tinyint(1) DEFAULT '0',
  `activationkey` varchar(50) DEFAULT NULL,
  `resetpasswordkey` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('admin', 'admin@mail.me', '$2a$10$MKDtO00YG7tLVTl3vYoxJOQTRSLu3DZkW9VM6g8JY6c4IIUZHNWd6', '1', null, null);
INSERT INTO `user` VALUES ('user', 'user@mail.me', '$2a$10$/EbtUW5.qD1UpjZI4DHc/eg/RjJy/9zb0Tl6c/6r0a.slDtZ6R5iW', '1', null, null);
INSERT INTO `user` VALUES ('rajith', 'rajith@abc.com', '$2a$10$MKDtO00YG7tLVTl3vYoxJOQTRSLu3DZkW9VM6g8JY6c4IIUZHNWd6', '1', null, null);

-- ----------------------------
-- Table structure for user_authority
-- ----------------------------
DROP TABLE IF EXISTS `user_authority`;
CREATE TABLE `user_authority` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY `user_authority_idx_1` (`username`,`authority`),
  KEY `authority` (`authority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_authority
-- ----------------------------
INSERT INTO `user_authority` VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO `user_authority` VALUES ('admin', 'ROLE_USER');
INSERT INTO `user_authority` VALUES ('rajith', 'ROLE_USER');
INSERT INTO `user_authority` VALUES ('user', 'ROLE_USER');


create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);



INSERT INTO `oauth2`.`oauth_client_details` (client_id,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove) VALUES ('client','','$2a$10$q4CIY2VWqx9LwoWT81GkS.A6z4lGJ3PpvVhSLu8YK1IhMBGX9DFRW','read,write','password,refresh_token','','',3600,2592000,'{}','');
INSERT INTO `oauth2`.`oauth_client_details` (client_id,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove) VALUES ('client_code','','$2a$10$e1i5Kt9uAIPVRnwJEU7Y7.lvP9i3JQ0G91i/CgaRyB9WS6XDYy.XK','app','authorization_code','','',null,null,'{}','');
INSERT INTO `oauth2`.`oauth_client_details` (client_id,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove) VALUES ('service-a','','$2a$10$3SO8rSx4kuhTrDYL9ZQ3neoC/8fIEngWfGGJdrsDRV5IHkNzS.mjq','server','client_credentials,refresh_token','','',null,null,'{}','');
INSERT INTO `oauth2`.`oauth_client_details` (client_id,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove) VALUES ('service-b','','$2a$10$QQrTWTkD99p3ad7xoPWaIOWoLXslczx9RIyVEWttv/LnuRjwd7RQS','server','client_credentials,refresh_token','','',null,null,'{}','');

