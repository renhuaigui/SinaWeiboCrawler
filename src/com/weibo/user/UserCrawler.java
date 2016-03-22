/**   
* @Title: UserCrawler.java
* @Package com.weibo.user
* @Description: TODO
* @author Huaigui   
* @date 2015年11月30日 下午6:09:58
* @version V1.0   
*/
package com.weibo.user;

import java.sql.Connection;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import com.weibo.follow.FollowCrawler;
import com.weibo.follow.FollowParser;
import com.weibo.util.SimpleWebClient;
import com.weibo.util.WebPage;
import com.weibo.util.WeiboDB;

/**
* @ClassName: UserCrawler
* @Description: 用户信息爬去类
* @author Huaigui
* @date 2015年11月30日 下午6:09:58
* 
*/
public class UserCrawler {
	/**
	* @Fields logger :日志对象
	*/
	private static Logger logger = Logger.getLogger(UserCrawler.class);
	
	/** 
	* @Title: userinfo 
	* @Description: 爬去用户信息
	* @param cookie
	* @param uid
	* @param preUid
	* @param conn
	* @return   
	* @return UserInfo
	* @throws 
	*/ 
	public static UserInfo userinfo(String cookie, String uid, String preUid, Connection conn) {
        String pageURL = "http://weibo.com/p/" + preUid + uid + "/info?mod=pedit_more";
        WebPage webPage = null;
        String value = "";
        UserInfo userinfo = new UserInfo();
        logger.info(pageURL);
        SimpleWebClient simpleClient = new SimpleWebClient();
        try {
        	webPage = simpleClient.getPageContent(pageURL, cookie);
        	if (webPage != null) {
        		//logger.debug(webPage.getHtml());
        		//FileUtil.writerFile("data" + File.separator + uid, "follow_" + indexPage + ".htm",
        		//                    "UTF-8", webPage.getHtml());
        		logger.info("=============解析用户信息=================");
        		value = UserParser.getUserJS(webPage.getHtml());
        		userinfo = UserParser.parserUser(uid,value);
                value = "";
                if((userinfo.getSex() != "")||(userinfo.getNickname() != "")){
                	WeiboDB.insertUserInfoToDB(userinfo, conn); 
                }
                 
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userinfo;
    }
}
