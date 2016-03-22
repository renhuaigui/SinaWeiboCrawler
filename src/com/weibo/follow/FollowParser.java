package com.weibo.follow;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.weibo.util.EmojiFilter;

public class FollowParser {
    /** 日志对象. */
    private static Logger logger = Logger.getLogger(FollowParser.class);
    /**
     * 解析关注或粉丝.
     * @param uid
     * @param follow
     * @return
     */
    public static ArrayList<FollowInfo> parserFollow(String uid, String follow) {
        ArrayList<FollowInfo> followList = new ArrayList<FollowInfo>();
        Document doc = Jsoup.parse(follow, "UTF-8");

        //Elements divElements = doc.select("div.con_left");
        Elements divElements = doc.select("div.follow_inner").select("dl");//选择follow关注列表的的li
        //System.out.println(divElements.size());
        if (divElements != null && !divElements.isEmpty()) {
            int divSize = divElements.size();
            for (int index = 0; index < divSize; index++) {
                Element element = divElements.get(index);
                FollowInfo followInfo = new FollowInfo();
                //System.out.println("----------------");
	    		//System.out.print("FollowUid: ");
	    		//System.out.println(element.select("a[class=S_txt1]").attr("href"));
	    		//System.out.println(element.select("a[class=S_txt1]").attr("usercard").substring(3));
	    		//System.out.print("Nickname: ");
	    		//System.out.println(element.select("a[class=S_txt1]").text());
	    		//System.out.print("关注数: ");
	    		//System.out.println(element.select("div.info_connect").select("a[target=_blank]").get(0).text());
	    		//System.out.print("粉丝数: ");
	    		//System.out.println(element.select("div.info_connect").select("a[target=_blank]").get(1).text());
	    		//System.out.print("微博数: ");
	    		//System.out.println(element.select("div.info_connect").select("a[target=_blank]").get(2).text());
	    		//System.out.print("地址: ");
	    		//System.out.println(element.select("div.info_add").select("span").text());
	    		//System.out.print("认证: ");
	    		//System.out.println(element.select("div.info_intro").select("span").text());
	    		//System.out.print("通过: ");
	    		//System.out.println(element.select("div.info_from").text());
	    		//System.out.println("================");
                followInfo.setUid(uid);
                if(element.select("a[class=S_txt1]").attr("usercard").length()>0)
                	followInfo.setFollowUid(element.select("a[class=S_txt1]").attr("usercard").substring(3));//关注ID
                if(element.select("a[class=S_txt1]").text().length()>0)
                	followInfo.setFollowNickname(element.select("a[class=S_txt1]").text());//关注的昵称
                if(element.select("div[class=info_connect]").select("a[target=_blank]").size()>1){
                	followInfo.setFollowFollow(element.select("div[class=info_connect]").select("a[target=_blank]").get(0).text());
                	followInfo.setFollowFans(element.select("div[class=info_connect]").select("a[target=_blank]").get(1).text());
                	followInfo.setFollowWeibo(element.select("div[class=info_connect]").select("a[target=_blank]").get(2).text());
                }
                if(element.select("div[class=info_add]").select("span").text().length()>0)
                	followInfo.setFollowAddr(element.select("div[class=info_add]").select("span").text());
                if(element.select("div[class=info_intro]").select("span").size()>0){
                    String info_intro = element.select("div[class=info_intro]").select("span").text();
                	info_intro = EmojiFilter.filterEmoji(info_intro);//过滤特殊字符
                    followInfo.setFollowCert(info_intro);
                }
                if(element.select("div[class=info_from]").text().length()>0)
                	followInfo.setFollowFrom(element.select("div[class=info_from]").text());
                //System.out.println("================");
                followList.add(followInfo);
            }
        }
        return followList;
    }
    /**
     * 获取关注或粉丝JS.
     * @param content
     * @return
     */
    public static String getFollowJS(String content) {
        String array[] = content.split("\n");
        String value = "";
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                if (array[i].startsWith("<script>FM.view({\"ns\":\"pl.content.followTab.index\"")) {
                    value = array[i];
                    int index = value.indexOf("\"html\":\"");
                    if (index > 0) {
                        value = value.substring(index + 7, value.length() - 12); //12
                        value = value.replaceAll("(\\\\t|\\\\n|\\\\r)",  "");
                        value = value.replaceAll("\\\\", "");
                       
                    }
                }
            }
            logger.debug("=====解析关注/粉丝信息 Ok!=======");
        }
        return value;
    }
}
