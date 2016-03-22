/*
SQLyog Ultimate v8.32 
MySQL - 5.5.30 : Database - sina_weibo
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`sina_weibo` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `sina_weibo`;

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `weiboID` varchar(16) NOT NULL,
  `poster` varchar(64) NOT NULL,
  `content` varchar(4096) NOT NULL,
  `postTime` datetime NOT NULL,
  `fetchTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `follow` */

DROP TABLE IF EXISTS `follow`;

CREATE TABLE `follow` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `followUid` bigint(20) DEFAULT NULL COMMENT '关注ID',
  `forwardNickname` varchar(150) DEFAULT NULL COMMENT '关注者用户名',
  `followFollow` int(11) DEFAULT NULL COMMENT '关注者关注数',
  `followFans` int(11) DEFAULT NULL COMMENT '关注者粉丝数',
  `followWeibo` int(11) DEFAULT NULL COMMENT '关注者微博数',
  `followAddr` varchar(20) DEFAULT NULL COMMENT '关注者地址',
	`followCert` varchar(1024) DEFAULT NULL COMMENT '关注者认证',
	`followFrom` varchar(20) DEFAULT NULL COMMENT '通过什么关注',		
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `accountID` bigint(16) NOT NULL,
  `nickName` varchar(64) DEFAULT NULL,
  `domain` varchar(256) DEFAULT NULL COMMENT '域名地址',
  `addr` varchar(20) DEFAULT NULL COMMENT '用户地址',
  `certification` varchar(512) DEFAULT NULL COMMENT '个人认证',
  `gender` varchar(4) DEFAULT NULL COMMENT '性别',
  `blogAddr` varchar(512) DEFAULT NULL COMMENT '博客地址',
  `birthday` varchar(32) DEFAULT NULL COMMENT '生日',
  `regtime` varchar(32) DEFAULT NULL COMMENT '注册时间',
  `brief` varchar(1024) DEFAULT NULL COMMENT '简介',
  `company` varchar(512) DEFAULT NULL COMMENT '工作信息',
  `label` varchar(1024) DEFAULT NULL,
  `educationback` varchar(512) DEFAULT NULL,
  `grade` varchar(128) DEFAULT NULL,
  `bagde` varchar(512) DEFAULT NULL COMMENT '勋章',
  `followNum` int(11) DEFAULT NULL,
  `followList` varchar(512) DEFAULT NULL,
  `fansNum` int(11) DEFAULT NULL,
  `fansList` varchar(512) DEFAULT NULL,
  `publishNum` int(11) DEFAULT NULL,
  `publishList` varchar(512) DEFAULT NULL,
  `others` varchar(1024) DEFAULT NULL,
  `vipLevel` int(11) DEFAULT '1',
  `vipType` int(11) DEFAULT '0' COMMENT '1-个人认证；2-企业认证；3-达人',
  `isFetched` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已经爬虫过了',
  `ad` varchar(64) DEFAULT NULL,
  `an` int(11) DEFAULT '0',
  `bi` varchar(512) DEFAULT NULL,
  `ci` varchar(512) DEFAULT NULL,
  `cnt` int(11) DEFAULT NULL,
  `de` varchar(512) DEFAULT NULL,
  `ei` varchar(512) DEFAULT NULL,
  `fn` int(11) DEFAULT '0',
  PRIMARY KEY (`id`,`accountID`)
) ENGINE=InnoDB AUTO_INCREMENT=3001 DEFAULT CHARSET=utf8;

/*Table structure for table `weibo` */

DROP TABLE IF EXISTS `weibo`;

CREATE TABLE `weibo` (
  `accountID` varchar(64) NOT NULL,
  `weiboID` varchar(16) NOT NULL,
  `content` varchar(4096) NOT NULL,
  `postTime` datetime NOT NULL,
  `fetchTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `isCommentFetched` tinyint(1) NOT NULL DEFAULT '0',
  `isRepostFetched` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`weiboID`),
  KEY `isCommentFetched` (`isCommentFetched`,`isRepostFetched`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `fans`;
CREATE TABLE `fans` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `fansUid` bigint(20) DEFAULT NULL COMMENT '粉丝ID',
  `fansNickname` varchar(150) DEFAULT NULL COMMENT '粉丝用户名',
  `fansFollow` int(11) DEFAULT NULL COMMENT '粉丝关注数',
  `fansFans` int(11) DEFAULT NULL COMMENT '粉丝粉丝数',
  `fansWeibo` int(11) DEFAULT NULL COMMENT '粉丝微博数',
  `fansAddr` varchar(100) DEFAULT NULL COMMENT '粉丝地址',
	`fansCert` varchar(1000) DEFAULT NULL COMMENT '粉丝认证',
	`fansFrom` varchar(20) DEFAULT NULL COMMENT '通过什么关注',	

  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `content`;
CREATE TABLE `content` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `mid` bigint(20) DEFAULT NULL COMMENT '微博ID',
  `content` varchar(500) DEFAULT '' COMMENT '微博内容',
  `isForward` tinyint(1) DEFAULT NULL COMMENT '0:原创,1:转发',
  `forward` int(11) DEFAULT NULL COMMENT '转发数量',
  `favorite` int(11) DEFAULT NULL COMMENT '收藏数量',
  `comment` int(11) DEFAULT NULL COMMENT '评论数量',
  `heart` int(11) DEFAULT NULL COMMENT '称赞数量',
  `addTime` bigint(20) DEFAULT NULL COMMENT '发布时间',
  `comeFrom` varchar(150) DEFAULT NULL COMMENT '来源',
  `href` varchar(255) DEFAULT NULL COMMENT '微博访问地址',
  `pictureNum` int(11) DEFAULT NULL COMMENT '图片的数量',
  `pictureHerf` varchar(2048) DEFAULT NULL COMMENT '图片的地址',
  `forwardUid` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `forwardUserName` varchar(150) DEFAULT NULL COMMENT '用户名',
  `forwardContent` varchar(500) DEFAULT '' COMMENT '微博内容',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `image`;
CREATE TABLE `image` (
  `img_id` int(11) NOT NULL,
  `img_name` varchar(255) NOT NULL DEFAULT '',
  `img_data` longblob,
  `in_out_door` int(11) DEFAULT NULL,
  `personNum` int(11) DEFAULT NULL,
  PRIMARY KEY (`img_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
