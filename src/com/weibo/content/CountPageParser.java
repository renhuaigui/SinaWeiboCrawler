package com.weibo.content;

import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;

public class CountPageParser {
    /** 日志对象. */
    private static Logger logger = Logger.getLogger(CountPageParser.class);
    /**
     * 取得微博总分页数.
     * @param json json
     * @return countPage 页数
     */
    public static String getCountPage(final String json) {
        Map m = new Gson().fromJson(json, Map.class);
        String data = m.get("data").toString();
        logger.debug(data);
        Document doc = Jsoup.parse(data, "UTF-8");
        String countPage = doc.select("a[action-type=feed_list_page_more]").attr("action-data");
        logger.debug(countPage);
        countPage = countPage.substring(countPage.lastIndexOf("=") + 1);
        logger.debug("countPage = " + countPage);
        return countPage;
    }
}
