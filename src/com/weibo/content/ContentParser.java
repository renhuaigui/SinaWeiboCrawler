package com.weibo.content;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.weibo.util.EmojiFilter;
import com.weibo.util.FileUtil;
import com.weibo.util.SimpleWebClient;
import com.weibo.util.WebPage;
import com.weibo.util.WeiboDB;

public class ContentParser {
    /** 页面字符集. */
    private static String charset = "UTF-8";
    /** 日志对象. */
    private static Logger logger = Logger.getLogger(ContentParser.class);
    /** 页面中动态取内容的地址. */
    private static String mbloglistPrefix = "http://weibo.com/aj/mblog/mbloglist?";
    /**
     * 解析HTML中的微博内容.
     * @param weibo
     * @param part 当前页的第几部分,1-3
     * @return
     */
    public static ArrayList<MsgInfo> parserWeibo(String uid, String weibo, int part) {
        ArrayList<MsgInfo> msgList = new ArrayList<MsgInfo>();
        Document doc = Jsoup.parse(weibo, "UTF-8");
        
        Elements divElements = doc.select("div[action-type=feed_list_item]");
        if (divElements != null && !divElements.isEmpty()) {
            int divSize = divElements.size();
            for (int index = 0; index < divSize; index++) {
                Element element = null;
                try {
                    MsgInfo msg = new MsgInfo();
                    element = divElements.get(index);
                    //System.out.println(element);
                    msg.setUserId(uid);
                    msg.setMid(element.attr("mid"));
                    if ("1".equals(element.attr("isForward"))) {//如果是转发的微博
                        msg.setIsforward(element.attr("isforward"));
                        continue;//转发微博不分析
                       /*********************************
                        *  // 解析转发后微博内容
                        String content = element.select("div[node-type=feed_list_content]").text();
                        logger.debug(content);
                        //System.out.println(content);
                        if (part == 1 && content.startsWith("n ")) {
                            content = content.substring(2);
                        }
                        msg.setContent(content);
                        // 赞     转发     收藏  评论
                        // <div class="WB_func clearfix">
                        Elements webDetailElements = element.select("div.WB_detail");
                        if (webDetailElements != null && webDetailElements.size() > 0) {
                            if (webDetailElements.get(0).children() != null
                            && webDetailElements.get(0).children().size() >= 2) {
                                webDetailElements = webDetailElements.get(0).children().get(2).children();
                            }
                        }
                        Elements commentElements = webDetailElements.select("div.WB_handle");
                        if (commentElements != null && commentElements.size() >= 1) {
                            // a title="赞
                            String heart = commentElements.select("a[title=赞]").text();
                            // 每部分js不同
                            // action-type="feed_list_forward
                            String forward;
                            // action-type="feed_list_favorite
                            String favorite;
                            // action-type="feed_list_comment
                            String comment;
                            if (part == 1) {
                                forward = commentElements.select("a[action-type=fl_forward]").text();
                                favorite = commentElements.select("a[action-type=fl_favorite]").text();
                                comment = commentElements.select("a[action-type=fl_comment]").text();
                            } else {
                                forward = commentElements.select("a[action-type=feed_list_forward]").text();
                                favorite = commentElements.select("a[action-type=feed_list_favorite]").text();
                                comment = commentElements.select("a[action-type=feed_list_comment]").text();
                            }
                            logger.debug(heart + "\t" + forward + "\t" + favorite + "\t" + comment);
                            msg.setHeart(heart);
                            msg.setForward(forward);
                            msg.setFavorite(favorite);
                            msg.setComment(comment);
                        }
                        Elements webFromElements = webDetailElements.select("div.WB_from");
                        if (webFromElements != null && webFromElements.size() > 0) {
                            // 微博发布时间
                            // node-type="feed_list_item_date" class="S_link2 WB_time"
                            String date = webFromElements.select("a[node-type=feed_list_item_date]").text();
                            String dateString = webFromElements.select("a[node-type=feed_list_item_date]").attr("date");
                            String href = webFromElements.select("a[node-type=feed_list_item_date]").attr("href");
                            logger.debug(dateString + "\t" + date + "\t" + href);
                            msg.setDate(date);
                            msg.setDateString(dateString);
                            msg.setHref(href);
                            // 微博发布方式
                            // class="S_link2">iPad客户端</a>
                            String feedComeFrom = webFromElements.select("a[rel=nofollow]").text();
                            logger.debug(feedComeFrom);
                            msg.setComeFrom(feedComeFrom);
                            //msgList.add(msg);
                            logger.debug("\r\n" + msg);
                        }
                        // 如果是转发的微博，解析转发微博的原文和转发后的微博评论
                        // <div node-type="feed_list_forwardContent" class="WB_media_expand SW_fun2 S_line1 S_bg1">
                        // 解析转发微博内容
                        //MsgInfo forwardMsg = new MsgInfo();
                        Elements forwardElements = element.select("div[node-type=feed_list_forwardContent]");
                        if (forwardElements.size() > 0) {
                            // 用户信息
                            String authorUrl = forwardElements.select("div.WB_info > a").first().attr("href");
                            String authorName = forwardElements.select("div.WB_info > a").first().attr("title");
                            // usercard="id=1494720324"
                            String userId = element.select("div.WB_info > a").first().attr("usercard");
                            if (userId != null && userId.indexOf("=") > 0) {
                                userId = userId.substring(userId.indexOf("=") + 1);
                            }
                            msg.setForwardUid(userId);
                            //logger.debug(userId + "\t" + authorUrl + "\t" + authorName);
                            msg.setForwardAuthor(authorName);
                            //forwardMsg.setAuthorUrl(authorUrl);
                            // 微博内容
                            // <div node-type="feed_list_reason" class="WB_text">
                            content = forwardElements.select("div[node-type=feed_list_reason]").text();
                            if (part == 1 && content.startsWith("n ")) {
                                content = content.substring(2);
                            }
                            logger.debug(content);
                            msg.setForwardContent(content);
                            // <div mid="3574220405120054" class="WB_handle">
                            String mid = forwardElements.select("div.WB_handle").attr("mid").toString();
                            //forwardMsg.setMid(mid);
                            // 赞     转发     评论
                            // action-type="feed_list_like"
                            String heart = forwardElements.select("a[action-type=feed_list_like]").text();
                            // <a href="/1642330703/zv8OkltXw?type=repost" class="S_func4">转发(9)</a>
                            String forward = "0"; //forwardElements.select("a.S_func4").first().text();
                            // <a class="S_func4" href="/1642330703/zv8OkltXw">评论(3)</a>
                            String comment = "0"; //forwardElements.select("a.S_func4").get(1).text();
                            logger.debug(heart + "\t" + forward + "\t" + comment);
                            //forwardMsg.setHeart(heart);
                            //forwardMsg.setForward(forward);
                            //forwardMsg.setComment(comment);
                            // 微博发布时间
                            String date = forwardElements.select("a[node-type=feed_list_item_date]").text();
                            String dateString = forwardElements.select("a[node-type=feed_list_item_date]").attr("date");
                            String href = forwardElements.select("a[node-type=feed_list_item_date]").attr("href");
                            logger.debug(dateString + "\t" + date + "\t" + href);
                            //forwardMsg.setDate(date);
                            //forwardMsg.setDateString(dateString);
                            //forwardMsg.setHref(href);
                            // 微博发布方式
                            String feedComeFrom = forwardElements.select("a[rel=nofollow]").text();
                            logger.debug(feedComeFrom);
                            //forwardMsg.setComeFrom(feed_come_from);
                            //logger.debug("\r\n" + forwardMsg);
                        }
                        msgList.add(msg);  */
                    } 
                    
                    /***********************************************************
                     * 解析原创微博
                     * ***/
                    else { //+++++=====原创微博=
                        /*
                        if(element.select("div.WB_info > a").size() > 2) {
                            // 用户信息
                            String authorUrl = element.select("div.WB_info > a").first().attr("href");
                            String authorName = element.select("div.WB_info > a").first().attr("title");
                            // usercard="id=1494720324"
                            String userId = element.select("div.WB_info > a").first().attr("usercard");
                            if(userId != null && userId.indexOf("=") > 0)
                            {
                                userId = userId.substring(userId.indexOf("=") + 1);
                            }
                            logger.debug(userId + "\t" + authorUrl + "\t" + authorName);
                            msg.setUserId(userId);
                            msg.setAuthorName(authorName);
                            msg.setAuthorUrl(authorUrl);
                            // 微博认证
                            Elements identityTags = element.select("div.WB_info").first().select("i[class]");
                            if (!identityTags.isEmpty()) {
                                String identity = identityTags.first().attr("title");
                                logger.debug(identity);
                                msg.setIdentity(identity);
                            }
                        }
                        // 地理信息
                        // <div class="map_data">
                        if(element.select("div.map_data").size() > 1) {
                            msg.setMapData(element.select("div.map_data").text());
                        }
                        */
                        // 微博内容
                        // <div node-type="feed_list_content" class="WB_text">
                        String content = element.select("div.WB_text").text();
                        if (part == 1 && content.startsWith("n ")) {
                            content = content.substring(2);
                        }
                        content = EmojiFilter.filterEmoji(content);
                        logger.debug(content);
                        msg.setContent(content);
                        // a title="赞
                        String heart = element.select("a[title=赞]").text();
                        // 转发     收藏     评论
                        // 每部分js不同
                        // action-type="feed_list_forward
                        String forward;
                        // action-type="feed_list_favorite
                        String favorite;
                        // action-type="feed_list_comment
                        String comment;
                        if (part == 1) {
                            forward = element.select("a[action-type=fl_forward]").text();
                            favorite = element.select("a[action-type=fl_favorite]").text();
                            comment = element.select("a[action-type=fl_comment]").text();
                        } else {
                            forward = element.select("a[action-type=feed_list_forward]").text();
                            favorite = element.select("a[action-type=feed_list_favorite]").text();
                            comment = element.select("a[action-type=feed_list_comment]").text();
                        }
                        logger.debug(heart + "\t" + forward + "\t" + favorite + "\t" + comment);
                        msg.setHeart(heart);
                        msg.setForward(forward);
                        msg.setFavorite(favorite);
                        msg.setComment(comment);
                        
                        //图片
                        Elements image;
                        if((image=element.select("img[class=bigcursor]")).size()>0){//一种图片的表达格式
                        	//<img  class="bigcursor" node-type="feed_list_media_bgimg" src="http://ww2.sinaimg.cn/thumbnail/668f794ejw1el42jbqqvtj20xc18g45t.jpg" alt=""/>
                        	msg.setPictureNum(String.valueOf(image.size()));
                        	for (int i = 0; i < image.size(); i++) {
								String imgHerf = image.get(i).select("img").attr("src");
								imgHerf = imgHerf.replaceAll("(thumbnail|square|bmiddle)","mw1024");
								msg.setPictureHerf(imgHerf);
							}
                        }
                        else if ((image=element.select("img[action-type=fl_pics]")).size()>0) {//另一种图片表达格式
							// <img src="http://ww3.sinaimg.cn/square/81309c56jw1eyrkj4dlbbj20rr15ojxb.jpg" action-data="pic_id=81309c56jw1eyrkj4dlbbj20rr15ojxb" action-type="fl_pics" suda-uatrack="key=tblog_newimage_feed&amp;value=image_feed_unfold:3917750302866861:81309c56jw1eyrkj4dlbbj20rr15ojxb">
                        	msg.setPictureNum(String.valueOf(image.size()));
                        	for (int i = 0; i < image.size(); i++) {
								String imgHerf = image.get(i).select("img").attr("src");
								imgHerf = imgHerf.replaceAll("(thumbnail|square|bmiddle)","mw1024");
								msg.setPictureHerf(imgHerf);
                        	}
                        }
                        
                        
                        // 微博发布时间
                        // node-type="feed_list_item_date" class="S_link2 WB_time"
                        String date = element.select("a[node-type=feed_list_item_date]").text();
                        String dateString = element.select("a[node-type=feed_list_item_date]").attr("title");
                        String href = element.select("a[node-type=feed_list_item_date]").attr("href");
                        logger.debug(dateString + "\t" + date + "\t" + href);
                        msg.setDate(date);
                        msg.setDateString(dateString);
                        msg.setHref(href);
                        // 微博发布方式
                        // class="S_link2">iPad客户端</a>
                        String feedComeFrom = element.select("a[rel=nofollow]").text();
                        logger.debug(feedComeFrom);
                        msg.setComeFrom(feedComeFrom);
                        msgList.add(msg);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.debug(element);
                    continue;
                }
            }
            if (msgList != null && msgList.size() > 0) {
                for (MsgInfo msg: msgList) {
                    logger.debug(msg);
                }
            }
            logger.debug(msgList.size());
        }
        return msgList;
    }
    /**
     * 解析Axaj.
     * @param weibo
     * @param part
     * @return
     */
    public static ArrayList<MsgInfo> parserAxajWeibo(String uid, String weibo, int part) {
        ArrayList<MsgInfo> msgList = new ArrayList<MsgInfo>();
        Map m = new Gson().fromJson(weibo, Map.class);
        String data = m.get("data").toString();
        logger.info("====第"+part+"部分内容====");
        //System.out.println(data);
        msgList = parserWeibo(uid, data, part);
        return msgList;
    }
    /**
     * 获取微博JS.
     * @param content
     * @return
     */
    public static String getWeiboJS(String content) {
        String[] array = content.split("\n");
        String value = "";
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                //System.out.println(i + "\t" + array[i]);
                if (array[i].startsWith("<script>FM.view({\"ns\":\"pl.content.homeFeed.index\"")) {
                    value = array[i];
                    int index = value.indexOf("\"html\":\"");
                    if (index > 0) {
                        value = value.substring(index + 7, value.length() - 12); //截头去尾
                        value = value.replaceAll("(\\\\t|\\\\n|\\\\r)",  "");
                        value = value.replaceAll("\\\\", "");
                        logger.info("+++微博JS OK！+++++");
                    }
                }
            }
        }
        return value;
    }
    /**
     * 获取第二、三部分Axaj.
     * @param simpleClient
     * @param mbloglist
     * @param cookie
     * @param uid
     * @param pageNumber
     * @param indexPage
     * @return
     */
    public static String getAxajWeibo(SimpleWebClient simpleClient,String mbloglist, String cookie, 
                                      String uid, int pageNumber, int indexPage) {
        String axaj = "";
        WebPage webPage = null;
        if (mbloglist != null && !"".equals(mbloglist)) {
            webPage = simpleClient.getPageContent(mbloglistPrefix + mbloglist, cookie);
            if (webPage != null) {
                logger.debug("++++获取微博 "+pageNumber+1+" 部分+++++");
                axaj = webPage.getHtml();
                //FileUtil.writerFile("data" + File.separator + uid, pageNumber + "_" + indexPage + ".htm",
                //                    "UTF-8", webPage.getHtml());
            }
        }
        return axaj;
    }
}
