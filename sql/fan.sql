DROP TABLE IF EXISTS `fans`;
CREATE TABLE `fans` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `fansUid` bigint(20) DEFAULT NULL COMMENT '粉丝ID',
  `fansNickname` varchar(150) DEFAULT NULL COMMENT '粉丝用户名',
  `fansFollow` int(11) DEFAULT NULL COMMENT '粉丝关注数',
  `fansFans` int(11) DEFAULT NULL COMMENT '粉丝粉丝数',
  `fansWeibo` int(11) DEFAULT NULL COMMENT '粉丝微博数',

  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
