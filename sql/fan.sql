DROP TABLE IF EXISTS `fans`;
CREATE TABLE `fans` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) DEFAULT NULL COMMENT '�û�ID',
  `fansUid` bigint(20) DEFAULT NULL COMMENT '��˿ID',
  `fansNickname` varchar(150) DEFAULT NULL COMMENT '��˿�û���',
  `fansFollow` int(11) DEFAULT NULL COMMENT '��˿��ע��',
  `fansFans` int(11) DEFAULT NULL COMMENT '��˿��˿��',
  `fansWeibo` int(11) DEFAULT NULL COMMENT '��˿΢����',

  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
