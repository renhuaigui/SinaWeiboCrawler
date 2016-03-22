package com.weibo.util;

import org.apache.log4j.Logger;

import com.weibo.Config;

public class GetPid {
    /** 日志对象. */
    private static Logger logger = Logger.getLogger(GetPid.class);
    /**
     * 根据uid获取前缀pid.
     * @param uid
     * @param cookie
     * @return
     */
    public static String getPid(String uid, String cookie) {
        String value = null;
        String pageURL = "http://weibo.com/u/" + uid;
        SimpleWebClient simpleClient = new SimpleWebClient();
        WebPage webPage = simpleClient.getPageContent(pageURL, cookie);
        if (webPage != null && webPage.getHtml().length() > 100) {
            value = webPage.getHtml();
            int begin = value.indexOf("$CONFIG['page_id']=");
            value = value.substring(begin + 20, begin + 26);
        }
        if (value != null && value.startsWith("10")) {
            return value;
        } else {
            return "100505";
        }
    }
    public static void main(String[] args) {
        String rst = getPid("1496852380", Config.cookie);
        System.out.println(rst);
    }
}

