package com.weibo.content;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.weibo.Config;
import com.weibo.util.FileUtil;
import com.weibo.util.SimpleWebClient;
import com.weibo.util.WebPage;
import com.weibo.util.WeiboDB;

public class ContentCrawler {
    /** 日志对象. */
    private static Logger logger = Logger.getLogger(ContentCrawler.class);
    /**
     * 抓取某一个的所有微博列表.
     * @param cookie 登录后的Cookie
     * @param uid 待抓取的用户id
     * @param preUid 待抓取的用户id前缀
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @param conn 数据库对象
     */
    public static void content(String cookie, String uid, String preUid,
                               String startDate, String endDate, Connection conn) {
        //搜索的是有图原创的微博
    	String pageURL = "http://weibo.com/p/" + preUid + uid + "/home?"
                       + "is_search=1&visible=0&is_ori=1&is_pic=1"
                       + "&start_time=" + startDate + "&end_time=" + endDate 
                       + "&is_tag=0&profile_ftype=1&page=";
        //搜索所有的微博
    	String allweibo = "http://weibo.com/p/"+ preUid + uid +"/home?is_search=1"
                       +"&visible=0&is_ori=1&is_pic=1&is_video=1&is_music=1&is_forward=1&is_text=1"
                       + "&start_time=" + startDate + "&end_time=" + endDate
                       +"&is_tag=0&profile_ftype=1&page=";
        logger.info(pageURL);
        WebPage webPage = null;
        String value = "";        
        ArrayList<MsgInfo> msgList = new ArrayList<MsgInfo>();
        ArrayList<MsgInfo> onePageMsgList = new ArrayList<MsgInfo>();
        SimpleWebClient simpleClient = new SimpleWebClient();
        MsgInfo firstMsg = new MsgInfo();
        MsgInfo secondMsg = new MsgInfo();
        Mbloglist mbloglist = new Mbloglist();
        msgList.add(new MsgInfo());
        for (int indexPage = 1; msgList.size() > 0; indexPage++) {
            if (indexPage % 10 == 0) {
                try {
                    Thread.sleep(3 * Config.THREAD_SLEEP);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            msgList.clear();
            try {
                webPage = simpleClient.getPageContent(pageURL + indexPage, cookie);
                if (webPage != null) {
                	logger.info("=====解析图片微博======");
                    //FileUtil.writerFile("data" + File.separator + uid, indexPage + "_1" + ".htm",
                    //                    "UTF-8", webPage.getHtml());
                    value = ContentParser.getWeiboJS(webPage.getHtml());
                    //System.out.println("here!" + value);
                    msgList.clear();
                    msgList = ContentParser.parserWeibo(uid, value, 1);
                    value = "";
                    try {
                        Thread.sleep(Config.THREAD_SLEEP);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //System.out.println(msgList.size());
                    if (msgList != null && msgList.size() > 0) {
                        onePageMsgList.addAll(msgList);
                        WeiboDB.insertWeiboToDB(msgList, conn);
                        //System.out.println("===" + onePageMsgList.size());
                        // 页面Ajax取得第二部分内容
                        firstMsg = msgList.get(0);
                        secondMsg =  msgList.get(msgList.size() - 1);
                        mbloglist.setUid(uid);
                        mbloglist.setEnd_id(firstMsg.getMid());
                        mbloglist.setMax_id(secondMsg.getMid());
                        mbloglist.setCount(15);
                        mbloglist.set__rnd(System.currentTimeMillis() / 1000);
                        mbloglist.set_k(System.currentTimeMillis());
                        mbloglist.setPage(indexPage);
                        mbloglist.setPagebar(0);
                        mbloglist.setPre_page(indexPage);
                        mbloglist.setStartDate(startDate);
                        mbloglist.setEndDate(endDate);
                        msgList.clear();
                        logger.debug(mbloglist.toString());
                        value = ContentParser.getAxajWeibo(simpleClient, mbloglist.toString(), cookie,
                                uid, indexPage, 2);
                        //System.out.println("===========================json:===============================");
                        //System.out.println(value);
                        msgList = ContentParser.parserAxajWeibo(uid, value, 2);
                        onePageMsgList.addAll(msgList);
                        WeiboDB.insertWeiboToDB(msgList, conn);
                        //System.out.println("===" + onePageMsgList.size());
                        // 页面Ajax取得第三部分内容
                        secondMsg =  msgList.get(0);
                        mbloglist.setMax_id(secondMsg.getMid());
                        mbloglist.setCount(15);
                        mbloglist.set__rnd(System.currentTimeMillis() / 1000);
                        mbloglist.set_k(System.currentTimeMillis());
                        mbloglist.setPage(indexPage);
                        mbloglist.setPagebar(1);
                        mbloglist.setPre_page(indexPage);
                        msgList.clear();
                        logger.debug(mbloglist.toString());
                        value = ContentParser.getAxajWeibo(simpleClient, mbloglist.toString(), cookie,
                                uid, indexPage, 3);
                        msgList = ContentParser.parserAxajWeibo(uid, value, 3);
                        //System.out.println("json:1========================");
                        //System.out.println(value);
                        onePageMsgList.addAll(msgList);
                        WeiboDB.insertWeiboToDB(msgList, conn);
                        //System.out.println("===" + onePageMsgList.size());
                    }
                    //System.out.println("===" + onePageMsgList.size());
                    //WeiboDB.insertWeiboToDB(onePageMsgList, conn);
                    onePageMsgList.clear();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
        }
    }
}
