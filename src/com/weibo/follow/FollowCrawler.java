package com.weibo.follow;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.weibo.Config;
import com.weibo.util.FileUtil;
import com.weibo.util.GetPid;
import com.weibo.util.SimpleWebClient;
import com.weibo.util.WebPage;
import com.weibo.util.WeiboDB;
import com.weibo.follow.FollowInfo;

public class FollowCrawler {
    /** 日志对象. */
    private static Logger logger = Logger.getLogger(FollowCrawler.class);
    /**
     * 抓取一级关注,返回关注列表.
     * @param cookie
     * @param uid
     * @param preUid
     * @param conn
     * @return
     */
    public static ArrayList<FollowInfo> follow(String cookie, String uid, String preUid, Connection conn) {
        String pageURL = "http://weibo.com/p/" + preUid + uid + "/follow?page=";
        WebPage webPage = null;
        String value = "";
        logger.info(pageURL);
        ArrayList<FollowInfo> followList = new ArrayList<FollowInfo>();
        ArrayList<FollowInfo> onePageFollowList = new ArrayList<FollowInfo>();
        SimpleWebClient simpleClient = new SimpleWebClient();
        onePageFollowList.add(new FollowInfo());
        for (int indexPage = 1; onePageFollowList.size() > 0; indexPage++) {
            onePageFollowList.clear();
            try {
                webPage = simpleClient.getPageContent(pageURL + indexPage, cookie);
                if (webPage != null) {
                    logger.info("=====解析关注信息====");
                    //FileUtil.writerFile("data" + File.separator + uid, "follow_" + indexPage + ".htm",
                    //                    "UTF-8", webPage.getHtml());
                    value = FollowParser.getFollowJS(webPage.getHtml());
                    //System.out.println(value);
                    onePageFollowList = FollowParser.parserFollow(uid, value);
                    value = "";
                    WeiboDB.insertFollowToDB(onePageFollowList, conn);
                    followList.addAll(onePageFollowList);    
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
        }
        return followList;
    }
    /**
     * 抓取一级粉丝,返回粉丝列表.
     * @param cookie
     * @param uid
     * @param preUid
     * @param conn
     * @return
     */
    public static  ArrayList<FollowInfo> fans(String cookie, String uid, String preUid, Connection conn) {
        String pageURL = "http://weibo.com/p/" + preUid + uid
                       + "/follow?pids=Pl_Official_LeftHisRelation__26&relate=fans&page=";
        logger.info(pageURL);
        WebPage webPage = null;
        String value = "";
        ArrayList<FollowInfo> followList = new ArrayList<FollowInfo>();
        ArrayList<FollowInfo> onePageFollowList = new ArrayList<FollowInfo>();
        SimpleWebClient simpleClient = new SimpleWebClient();
        onePageFollowList.add(new FollowInfo());
        for (int indexPage = 1; onePageFollowList.size() > 0; indexPage++) {
            onePageFollowList.clear();
            try {
                webPage = simpleClient.getPageContent(pageURL + indexPage, cookie);
                if (webPage != null) {
                	logger.info("=====解析粉丝信息====");
                    value = FollowParser.getFollowJS(webPage.getHtml());
                    onePageFollowList = FollowParser.parserFollow(uid, value);
                    value = "";
                    WeiboDB.insertFansToDB(onePageFollowList, conn);
                    followList.addAll(onePageFollowList);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
        }
        return followList;
    }
    /**
     * 抓取二级关注.
     * @param cookie
     * @param followList
     * @param conn
     */
    public static void followFollow(String cookie, ArrayList<FollowInfo> followList, Connection conn) {
        if (followList != null &&  followList.size() > 0) {
            for (int index = 0; index < followList.size(); index++) {
                FollowInfo follow = followList.get(index);
                String pid = GetPid.getPid(follow.getFollowUid(), cookie);
                FollowCrawler.follow(cookie, follow.getFollowUid() , pid, conn);
                //System.out.println("===" + (index + 1) + "===");
            }
        }
    }
    /**
     * 抓取二级粉丝.
     * @param cookie
     * @param followList
     * @param conn
     */
    public static void fansFans(String cookie, ArrayList<FollowInfo> followList, Connection conn) {
        if (followList != null &&  followList.size() > 0) {
            for (int index = 0; index < followList.size(); index++) {
                FollowInfo follow = followList.get(index);
                String pid = GetPid.getPid(follow.getFollowUid(), cookie);
                FollowCrawler.fans(cookie, follow.getFollowUid() , pid, conn);
                //考虑到机构粉丝数量比较大，可以只抓个人
                //FollowCrawler.fans(cookie, follow.getFollowUid() , "100505", conn); //只抓个人
            }
        }
    }
}
