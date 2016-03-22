package com.weibo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import com.weibo.content.ContentCrawler;
import com.weibo.follow.FollowCrawler;
import com.weibo.follow.FollowInfo;
import com.weibo.user.UserCrawler;
import com.weibo.user.UserInfo;
import com.weibo.util.GetPid;
import com.weibo.util.SeleniumClient;
import com.weibo.util.WeiboDB;
/**
 * 程序入口.
 */
public class Main {

    /** 
    * @Title: main 
    * @Description: 爬虫入口地址
    * @param args
    * @throws IOException   
    * @return void
    * @throws 
    */ 
    public static void main(final String[] args) throws IOException {
    	Config con = new Config();//获取配置文件
        //判断是否重新登录
    	/*if (con.IS_LOGIN) {
            try {
                con.cookie = SeleniumClient.GetCookie();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        //连接数据库
        Connection weiboDB = null;
        try {
            weiboDB = WeiboDB.getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //获取uid前缀，个人100505 机构100206
        //String pid = GetPid.getPid(Config.UID, Config.cookie);
        //抓取微博内容
        //ContentCrawler.content(Config.cookie, Config.UID, pid,
        //                       Config.START_DATE, Config.END_DATE, weiboDB);
        //抓取一级关注
        //ArrayList<FollowInfo> followList = null;
        //followList = FollowCrawler.follow(Config.cookie, Config.UID, pid, weiboDB);
        //抓取二级关注
        //FollowCrawler.followFollow(Config.cookie, followList, weiboDB);
        //抓取一级粉丝
        //followList.clear();
        //followList = FollowCrawler.fans(Config.cookie, Config.UID, pid, weiboDB);
        //抓取二级粉丝
        //FollowCrawler.fansFans(Config.cookie, followList, weiboDB);
        
        
        ArrayList<String> uidList = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(con.USERLIST));
        String userdate = br.readLine();
        while (userdate != null && !"".equals(userdate)) {
            uidList.add(userdate);
            userdate = br.readLine();
        }
        br.close();
        System.out.println("===" + uidList.size()  + "===");
        
        if (uidList != null && uidList.size() > 0) {
            for (int index = 5975; index < uidList.size(); index++) {
            	try {
            		String uid = uidList.get(index);//用户ID
            		String pid = GetPid.getPid(uid, Config.cookie);//用户ID前缀
            		//System.out.println("===" + pid  + "===");
            		System.out.println("===" + index + "开始===");
            		
            		UserInfo user = UserCrawler.userinfo(Config.cookie, uid, pid, weiboDB);//抓取用户信息
            		if((user.getSex() == "")&&(user.getNickname() == ""))
            			continue;
            		/*Thread.sleep(20000);
            		FollowCrawler.follow(Config.cookie, uid, pid, weiboDB);//抓取用户关注信息
            		Thread.sleep(20000);
            		FollowCrawler.fans(Config.cookie, uid, pid, weiboDB);//抓取用户粉丝信息信息
            		*/
            		//抓取图片微博、
            		Thread.sleep(5000);
            		ContentCrawler.content(Config.cookie, uid, pid, Config.START_DATE, Config.END_DATE, weiboDB);
            		System.out.println("===" + index  + "完成===");
            	} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        
       
        try {
            weiboDB.close(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("+————————————————————————————————————————————————+");
        System.out.println("+------------------->| END! |<-------------------+");
        System.out.println("+————————————————————————————————————————————————+");
    }
}
