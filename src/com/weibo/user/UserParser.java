/**   
* @Title: UserParser.java
* @Package com.weibo.user
* @Description: TODO
* @author Huaigui   
* @date 2015年11月26日 下午8:43:24
* @version V1.0   
*/
package com.weibo.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Strings;
import com.opera.core.systems.scope.protos.EcmascriptProtos.Value;
import com.sun.accessibility.internal.resources.accessibility;
import com.sun.corba.se.spi.servicecontext.UEInfoServiceContext;
import com.weibo.user.UserInfo;
import com.weibo.util.EmojiFilter;


/**
* @ClassName: UserParser
* @Description: 解析用户信息类
* @author Huaigui
* @date 2015年11月26日 下午8:43:24
* 
*/
public class UserParser {
	private static Logger logger = Logger.getLogger(UserParser.class);
	

	/** 
	* @Title: parserUser 
	* @Description: 解析用户信息
	* @param uid 用户id
	* @param user 用户网页信息
	* @return UserInfo 用户信息列表
	* @throws 
	*/ 
	public static UserInfo parserUser(String id, String user) {
        Document doc = Jsoup.parse(user, "UTF-8");
        UserInfo userinfo = new UserInfo();
	    //System.out.println(doc);
        //提取认证信息
        if(doc.select("div[class=pf_intro]").size()>0){
        	String pf_intro = doc.select("div[class=pf_intro]").text();
        	pf_intro = EmojiFilter.filterEmoji(pf_intro);
        	userinfo.setCertification(pf_intro);
        }
        Elements elementUserInfo = doc.select("div[class=WB_cardwrap S_bg2]").select("div[class=PCD_text_b PCD_text_b2]");//选取用户基本信息模块
		//System.out.println(elementUserInfo.size());
		userinfo.setID(id);
		if(elementUserInfo!=null && !elementUserInfo.isEmpty()){
			
			for (int j = 0; j <elementUserInfo.size(); j++) {
				Elements Li_clearfix = elementUserInfo.get(j).select("li[class=li_1 clearfix]");
				//System.out.println("--------------------");
				for (int i = 0; i < Li_clearfix.size(); i++) {
					Element clearfix = Li_clearfix.get(i);
					String title = clearfix.select("span[class=pt_title S_txt2]").text();
					//System.out.print(clearfix.select("span[class=pt_title S_txt2]").text());
					switch (title) {
					case "昵称：":
						//System.out.print(clearfix.select("span[class=pt_title S_txt2]").text());
						//System.out.println(clearfix.select("span[class=pt_detail]").text());
						userinfo.setNickname(clearfix.select("span[class=pt_detail]").text());
						break;
					case "所在地：":
						//System.out.print(clearfix.select("span[class=pt_title S_txt2]").text());
						//System.out.println(clearfix.select("span[class=pt_detail]").text());
						userinfo.setAddr(clearfix.select("span[class=pt_detail]").text());
						break;
					case "性别：":
						//System.out.print(clearfix.select("span[class=pt_title S_txt2]").text());
						//System.out.println(clearfix.select("span[class=pt_detail]").text());
						userinfo.setSex(clearfix.select("span[class=pt_detail]").text());
						break;
					case "博客：":
						//System.out.print(clearfix.select("span[class=pt_title S_txt2]").text());
						//System.out.println(clearfix.select("a").attr("href"));
						userinfo.setBlogAddr(clearfix.select("a").attr("href"));
						break;
					case "个性域名：":
						//System.out.print(clearfix.select("span[class=pt_title S_txt2]").text());
						//System.out.println(clearfix.select("a").attr("href"));
						userinfo.setDomainhacks(clearfix.select("a").attr("href"));
						break;
					case "简介：":
						//System.out.print(clearfix.select("span[class=pt_title S_txt2]").text());
						//System.out.println(clearfix.select("span[class=pt_detail]").text());
						String brief = clearfix.select("span[class=pt_detail]").text();
			        	brief = EmojiFilter.filterEmoji(brief);
						userinfo.setBrief(brief);
						break;
					case "注册时间：":
						//System.out.print(clearfix.select("span[class=pt_title S_txt2]").text());
						//System.out.println(clearfix.select("span[class=pt_detail]").text());
						userinfo.setRegTime(clearfix.select("span[class=pt_detail]").text());
						break;
					case "生日：":
						//System.out.print(clearfix.select("span[class=pt_title S_txt2]").text());
						//System.out.println(clearfix.select("span[class=pt_detail]").text());
						userinfo.setBirthday(clearfix.select("span[class=pt_detail]").text());
						break;
					case "标签：":
						//System.out.print(clearfix.select("span[class=pt_title S_txt2]").text());
						//System.out.println(clearfix.select("span[class=pt_detail]").text());
						userinfo.setLabel(clearfix.select("span[class=pt_detail]").select("a[target=_blank]").text());
						//System.out.println(clearfix.select("span[class=pt_detail]").select("a[target=_blank]").text());
						break;
					case "大学：":
					case "高中：":
					case "小学：":
					case "初中：":
					case "中专技校：":
						//System.out.print(clearfix.select("span[class=pt_title S_txt2]").text());
						//System.out.println(clearfix.select("span[class=pt_detail]").text());
						userinfo.setEducationBack(clearfix.select("span[class=pt_detail]").text());
						break;
					case "公司：":
						//System.out.print(clearfix.select("span[class=pt_title S_txt2]").text());
						//System.out.println(clearfix.select("span[class=pt_detail]").text());
						userinfo.setCompany(clearfix.select("span[class=pt_detail]").text());
						break;
					default:
						System.out.print("未定义的信息：");
						System.out.print(clearfix.select("span[class=pt_title S_txt2]").text());
						System.out.println(clearfix.select("span[class=pt_detail]").text());
						userinfo.setothers(clearfix.select("span[class=pt_title S_txt2]").text()+clearfix.select("span[class=pt_detail]").text());
						break;
					}
				}
			}		
		}
		Elements person_details = doc.select("div[class=WB_cardwrap S_bg2]");
		for (int i = 0; i < person_details.size(); i++) {
			Element person_detail=person_details.get(i);
			//System.out.println(person_detail.select("h2").text());
			switch (person_detail.select("h2").text()) {
			case "等级信息":
				Elements level_info = person_detail.select("p[class=level_info]").select("span[class=info]");
				//System.out.println(level_info.text());
				userinfo.setGrade(level_info.text());
				break;
			case "信用信息":
				
				break;
			case "勋章信息":
				Elements bagde_item = person_detail.select("li[class=bagde_item]");
				for (int j = 0; j < bagde_item.size(); j++) {
					userinfo.setBagde(bagde_item.get(j).select("a").attr("title"));	
				}
				break;
			case "简介":
				userinfo.setBrief(person_detail.select("p").text());;
				break;

			default:
				break;
			}
		}
		//选择follow关注列表的的li
		//System.out.println(divElements.select("table.tb_counter").select("td.S_line1"));
		//选择关注,粉丝等信息框
	    Elements tb_conter = doc.select("div[class=WB_cardwrap S_bg2]").select("table.tb_counter").select("td.S_line1");
		if (tb_conter != null && !tb_conter.isEmpty()) {
	    	int divSize = tb_conter.size();
	    	//System.out.println(divSize);
	    	for (int index = 0; index < divSize; index++) {
	    		Element element = tb_conter.get(index);
	    		switch (element.select("Span.S_txt2").text()) {
				case "关注":
					//System.out.println(element.select("strong").text());
					//System.out.println(element.select("a").attr("href"));
					userinfo.setFollowNum(element.select("strong").text());
					userinfo.setFollowList(element.select("a").attr("href"));
					break;
				case "粉丝":
					//System.out.println(element.select("strong").text());
					//System.out.println(element.select("a").attr("href"));
					userinfo.setFansNum(element.select("strong").text());
					userinfo.setFansList(element.select("a").attr("href"));
					break;
				case "微博":
					//System.out.println(element.select("strong").text());
					//System.out.println(element.select("a").attr("href"));
					userinfo.setPublishNum(element.select("strong").text());
					userinfo.setPublishList(element.select("a").attr("href"));
					break;
				default:
					break;
				}
	    	}
		}
		
		
		
		
		return userinfo;
    }
	
	
	/** 
	* @Title: getUserJS 
	* @Description: 获取用户JS网页
	* @param content
	* @return   
	* @return String
	* @throws 
	*/ 
	public static String getUserJS(String content) {
		String[] array = content.split("\\\n");
        String value = "";
        String uservalue= "";
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                //System.out.println(i + "\t" + array[i]);
                if (array[i].startsWith("<script>FM.view({\"ns\":\"\"")||array[i].startsWith("<script>FM.view({\"ns\":\"pl.header.head.index")) {
                    value = array[i];
                    int index = value.indexOf("\"html\":\"");
                    if (index > 0) {
                        value = value.substring(index + 7, value.length() - 12); //截头去尾
                        value = value.replaceAll("(\\\\t|\\\\n|\\\\r)",  "");
                        value = value.replaceAll("\\\\/", "/").replaceAll("\\\\", "");
                        uservalue = uservalue + value;                        
                    }
                }
            }
        }
        if(uservalue.length()>0)
        	 logger.info("=====用户信息 JS: Ok!======");
        return uservalue;
    }
}
