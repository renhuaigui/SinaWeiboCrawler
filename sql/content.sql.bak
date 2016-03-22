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
  `forwardUid` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `forwardUserName` varchar(150) DEFAULT NULL COMMENT '用户名',
  `forwardContent` varchar(500) DEFAULT '' COMMENT '微博内容',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
