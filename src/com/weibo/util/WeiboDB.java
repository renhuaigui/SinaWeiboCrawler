package com.weibo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import com.weibo.Config;
import com.weibo.content.MsgInfo;
import com.weibo.follow.FollowInfo;
import com.weibo.user.UserInfo;


public class WeiboDB {
    /** 日志对象。 */
    private static Logger logger = Logger.getLogger(WeiboDB.class);
    private static String url = "";
    
    /** 
    * @Title: getConnection 
    * @Description: 连接数据库
    * @throws ClassNotFoundException
    * @throws 
    */ 
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");//加载mysql的驱动
        url = "jdbc:mysql://" + Config.HOST + ":" + Config.PORT + "/" + Config.DB_NAME
                              + "?user=" + Config.USER + "&password=" + Config.PASSWORD
                              + "&amp;useUnicode=true&amp;characterEncoding=utf8";
        logger.debug(url);
        return DriverManager.getConnection(url);//获取数据库连接
    }
    /** 
    * @Title: insertUserInfoToDB 
    * @Description: 插入用户信息到数据库user表中
    * @param userinfo
    * @param conn
    * @return   
    * @return int
    * @throws 
    */ 
    public static int insertUserInfoToDB(UserInfo userinfo, Connection conn) {
        String insertSQL = "INSERT INTO `user` (`accountID`, `nickName`, `domain`, `addr`, "
                         + "`certification`, `gender`, `blogAddr`,`birthday`,"
                         + "`regtime`,`brief`, `company`,`grade`,"
                         + "`label`, `educationback`, `followNum`,`followList`,"
                         + "`fansNum`,`fansList`,`publishNum`,`publishList`,`bagde`,`others`,`isFetched`)"
                         + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        if((userinfo != null)&&(userinfo.getSex() != "")){
        	int parameterIndex = 1;
        	try{
        		PreparedStatement pst = conn.prepareStatement(insertSQL);
        		pst.setLong(parameterIndex++, stringToLong(userinfo.getID()));
        		pst.setString(parameterIndex++, userinfo.getNickname());
        		pst.setString(parameterIndex++, userinfo.getDomainhacks());
        		pst.setString(parameterIndex++, userinfo.getAddr());
        		pst.setString(parameterIndex++, userinfo.getCertification());
        		pst.setString(parameterIndex++, userinfo.getSex());
        		pst.setString(parameterIndex++, userinfo.getBlogAddr());
        		pst.setString(parameterIndex++, userinfo.getBirthday());
        		pst.setString(parameterIndex++, userinfo.getRegTime());
        		pst.setString(parameterIndex++, userinfo.getBrief());
        		pst.setString(parameterIndex++, userinfo.getCompany());
        		pst.setString(parameterIndex++, userinfo.getGrade());
        		pst.setString(parameterIndex++, userinfo.getLabel());
        		pst.setString(parameterIndex++, userinfo.getEducationBack());
        		pst.setInt(parameterIndex++, stringToInteger(userinfo.getFollowNum()));
        		pst.setString(parameterIndex++, userinfo.getFollowList());
        		pst.setInt(parameterIndex++, stringToInteger(userinfo.getFansNum()));
        		pst.setString(parameterIndex++, userinfo.getFansList());
        		pst.setInt(parameterIndex++, stringToInteger(userinfo.getPublishNum()));
        		pst.setString(parameterIndex++, userinfo.getPublishList());
        		pst.setString(parameterIndex++, userinfo.getBagde());
        		pst.setString(parameterIndex++, userinfo.getothers());
        		pst.setInt(parameterIndex++, 1);//设置爬虫标记
        		logger.info(pst.toString());
        		try{
                  	pst.execute();
                }catch (SQLException e) {	
                  	logger.debug(pst.toString());
                  	e.printStackTrace(); 	
                };
        	}catch (SQLException e) {
                e.printStackTrace();
            }
        }

        
        return 0;
    }
    /**
     * 插入微博内容到数据库.
     * @param msgList
     * @param conn
     * @return
     */
    public static int insertWeiboToDB(ArrayList<MsgInfo> msgList, Connection conn) {
        String insertSQL = "INSERT INTO `content` (`uid`, `mid`, `content`, `isForward`, "
                         + "`forward`, `favorite`, `comment`,`heart`,"
                         + "`addTime`,`comeFrom`, `href`,"
                         + "`pictureNum`, `pictureHerf`)"
                         + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
        if (msgList != null && msgList.size() > 0) {
            try {
                int parameterIndex = 1;
                PreparedStatement pst = conn.prepareStatement(insertSQL);
                for (int i = 0; i < msgList.size(); i++) {
                    parameterIndex = 1;
                    MsgInfo msg = msgList.get(i);
                    pst.setLong(parameterIndex++, stringToLong(msg.getUserId()));
                    pst.setLong(parameterIndex++, stringToLong(msg.getMid()));
                    pst.setString(parameterIndex++, msg.getContent());
                    pst.setByte(parameterIndex++, stringToByte(msg.getIsforward()));
                    pst.setInt(parameterIndex++, stringToInteger(msg.getForward()));
                    pst.setInt(parameterIndex++, stringToInteger(msg.getFavorite()));
                    pst.setInt(parameterIndex++, stringToInteger(msg.getComment()));
                    pst.setInt(parameterIndex++, stringToInteger(msg.getHeart()));
                    pst.setString(parameterIndex++,msg.getDateString());
                    pst.setString(parameterIndex++, msg.getComeFrom());
                    pst.setString(parameterIndex++, msg.getHref());
                  	pst.setInt(parameterIndex++, stringToInteger(msg.getPictureNum()));
                    pst.setString(parameterIndex++, msg.getPictureHerf());
                    //pst.setLong(parameterIndex++, stringToLong(msg.getForwardUid()));
                    //pst.setString(parameterIndex++, msg.getForwardAuthor());
                    //pst.setString(parameterIndex++, msg.getForwardContent());
                    logger.info(pst.toString());
                    try{
                    	pst.execute();
                    }catch (SQLException e) {	
                    	e.printStackTrace();
                    };
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    /**
     * 插入关注关系到数据库.
     * @param followList
     * @param conn
     * @return
     */
    public static int insertFollowToDB(ArrayList<FollowInfo> followList, Connection conn) {
        String insertSQL = "INSERT INTO `follow` (`uid`, `followUid`, `forwardNickname`,"
                         + "`followFollow`, `followFans`, `followWeibo`,`followAddr`,`followCert`,`followFrom`)"
                         + "VALUES (?,?,?,?,?,?,?,?,?);";
        if (followList != null && followList.size() > 0) {
            try {
                int parameterIndex = 1;
                PreparedStatement pst = conn.prepareStatement(insertSQL);
                for (int i = 0; i < followList.size(); i++) {
                    parameterIndex = 1;
                    FollowInfo follow = followList.get(i);
                    //System.out.println(follow.getUid());
                    pst.setLong(parameterIndex++, stringToLong(follow.getUid()));
                    pst.setLong(parameterIndex++, stringToLong(follow.getFollowUid()));
                    pst.setString(parameterIndex++, follow.getFollowNickname());
                    pst.setInt(parameterIndex++, stringToInteger(follow.getFollowFollow()));
                    pst.setInt(parameterIndex++, stringToInteger(follow.getFollowFans()));
                    pst.setInt(parameterIndex++, stringToInteger(follow.getFollowWeibo()));
                    pst.setString(parameterIndex++, follow.getFollowAddr());
                    pst.setString(parameterIndex++, follow.getFollowCert());
                    pst.setString(parameterIndex++, follow.getFollowFrom()); 
                    logger.info(pst.toString());
                    try{
                    	pst.execute();
                    }catch (SQLException e) {	
                    	e.printStackTrace();
                    };
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    /**
     * 插入粉丝关系到数据库.
     * @param followList
     * @param conn
     * @return
     */
    public static int insertFansToDB(ArrayList<FollowInfo> followList, Connection conn) {
        String insertSQL = "INSERT INTO `fans` (`uid`, `fansUid`, `fansNickname`,"
                         + "`fansFollow`, `fansFans`, `fansWeibo`,`fansAddr`,`fansCert`,`fansFrom`)"
                         + "VALUES (?,?,?,?,?,?,?,?,?);";
        if (followList != null && followList.size() > 0) {
            try {
                int parameterIndex = 1;
                PreparedStatement pst = conn.prepareStatement(insertSQL);
                for (int i = 0; i < followList.size(); i++) {
                    parameterIndex = 1;
                    FollowInfo follow = followList.get(i);
                    pst.setLong(parameterIndex++, stringToLong(follow.getUid()));
                    pst.setLong(parameterIndex++, stringToLong(follow.getFollowUid()));
                    pst.setString(parameterIndex++, follow.getFollowNickname());
                    pst.setInt(parameterIndex++, stringToInteger(follow.getFollowFollow()));
                    pst.setInt(parameterIndex++, stringToInteger(follow.getFollowFans()));
                    pst.setInt(parameterIndex++, stringToInteger(follow.getFollowWeibo()));
                    pst.setString(parameterIndex++, follow.getFollowAddr());
                    pst.setString(parameterIndex++, follow.getFollowCert());
                    pst.setString(parameterIndex++, follow.getFollowFrom());
                    logger.info(pst.toString());
                    try{
                    	pst.execute();
                    }catch (SQLException e) {	
                    	e.printStackTrace();
                    };
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    } 
    
    /** 
    * @Title: insertImage 
    * @Description: 插入图片存储路径信息 
    * @param ImgName 图片的存储路径
    * @param conn
    * @return   
    * @return int
    * @throws 
    */ 
    public static int insertImage(String uid ,String ImgName,Connection conn) {
		String insertImg ="INSERT INTO `image` (`mid`,`img_name`)"
                + "VALUES (?,?);"; 
		try {
			PreparedStatement pst = conn.prepareStatement(insertImg);
			pst.setLong(1, stringToLong(uid));
			pst.setString(2, ImgName);
			logger.info(pst.toString());
			pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
    
    public static long stringToLong(String longString) {
        long id = 0;
        try {
            longString = FindNumber(longString);
            id = Long.parseLong(longString);
        } catch (Exception ex) {
            logger.debug("===================" + longString);
            ex.printStackTrace();
        }
        return id;
    }
    public static int stringToInteger(String integerString) {
        int id = 0;
        try    {
            integerString = FindNumber(integerString);
            id = Integer.parseInt(integerString);
        } catch (Exception ex) {
            logger.debug("===================" + integerString);
            ex.printStackTrace();
        }
        return id;
    }
    public static byte stringToByte(String byteString) {
        byte id = 0;
        try {
            byteString = FindNumber(byteString);
            id = Byte.parseByte(byteString);
        } catch (Exception ex) {
            logger.debug("===================" + byteString);
            ex.printStackTrace();
        }
        return id;
    }
    /**
     * 查找字符串中的数字.
     * @param input
     * @return
     */
    public static String FindNumber(String input) {
        String value = "";
        String regex = "(\\d){0,}";
        Pattern p = Pattern.compile(regex);
        if (input != null && !"".equals(input)) {
            Matcher m = p.matcher(input);
            while (m.find()) {
                value = m.group();
                if (value == null || "".equals(value)) {
                    continue;
                } else {
                    //logger.debug(value);
                    break;
                }
            }
        }
        if (value == null || "".equals(value)) {
            value = "0";
        }
        return value;
    }
    /**
     * @param args
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException
    {
//        WeiboDB weiboDB = new WeiboDB();
//        Connection conn = weiboDB.getConnection();
//        logger.debug(conn);
    }
}
