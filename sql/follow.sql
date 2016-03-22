DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `followUid` bigint(20) DEFAULT NULL COMMENT '关注ID',
  `forwardNickname` varchar(150) DEFAULT NULL COMMENT '关注者用户名',
  `followFollow` int(11) DEFAULT NULL COMMENT '关注者关注数',
  `followFans` int(11) DEFAULT NULL COMMENT '关注者粉丝数',
  `followWeibo` int(11) DEFAULT NULL COMMENT '关注者微博数',

  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
