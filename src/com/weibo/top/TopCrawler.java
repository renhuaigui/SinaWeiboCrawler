package com.weibo.top;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.weibo.Config;
import com.weibo.content.ContentCrawler;
import com.weibo.follow.FollowInfo;
import com.weibo.util.GetPid;
import com.weibo.util.SimpleWebClient;
import com.weibo.util.WebPage;
import com.weibo.util.WeiboDB;

public class TopCrawler {
    /** 日志对象. */
    private static Logger logger = Logger.getLogger(TopCrawler.class);
    
    public static String getAxajTop(SimpleWebClient simpleClient, String cookie, long timeday) {
        
        //System.out.println(timeday);
        String axaj = "";
        WebPage webPage = null;
        webPage = simpleClient.getPageContent("http://top.weibo.com/newtop/ajax/influencedetailpage?type=month&key=1&second=29&month="+ timeday +"&depart=1&influtype=1&typedate=&timeday=" + timeday + "&pagecount=&__rnd=" + (System.currentTimeMillis() / 1000), cookie);
        if (webPage != null) {
            //logger.debug(webPage.getHtml());
            axaj = webPage.getHtml();
        }
        return axaj;
    }
    
    public static void top(String cookie) {
        
        /*
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setTimeout(50000);
        webClient.getCookieManager().setCookiesEnabled(true);

        final HtmlPage htmlPage = webClient.getPage("http://top.weibo.com/newtop/influencedetail?second=29&depart=1");
        //System.out.println("=1="+htmlPage.getTitleText());
        //System.out.println("=2="+htmlPage.getWebResponse().getContentAsString());
        //System.out.println("=3="+htmlPage.asText());
        */
        
        String pageURL = "http://top.weibo.com/newtop/influencedetail?second=29&depart=1";
        WebPage webPage = null;
        SimpleWebClient simpleClient = new SimpleWebClient();
        try {
            Date dat=new Date();
            dat.setDate(18);
            dat.setMonth(6);
            long timeday=dat.getTime()/1000;
            webPage = simpleClient.getPageContent(pageURL, cookie);
            //System.out.println(webPage.getHtml().toString());
            String value = getAxajTop(simpleClient, cookie, timeday);
            System.out.println(value);
            if (webPage != null) {
                //logger.debug(webPage.getHtml());
                //FileUtil.writerFile("data" + File.separator + uid, "follow_" + indexPage + ".htm",
                //                    "UTF-8", webPage.getHtml());
                //System.out.println(webPage.getHtml());
                parserTop(webPage.getHtml(),"体育","全国",07);
                //WeiboDB.insertFollowToDB(onePageFollowList, conn);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    public static ArrayList<TopInfo> parserTop(String top, String cat, String area, int month) {
        ArrayList<TopInfo> topList = new ArrayList<TopInfo>();
        Document doc = Jsoup.parse(top, "UTF-8");
        Elements divElements = doc.select("dl.list").select("dt");
        System.out.println(divElements.size());
        if (divElements != null && !divElements.isEmpty()) {
            int divSize = divElements.size();
            for (int index = 0, no = 1; index < divSize; index++) {
                Element element = divElements.get(index);
                TopInfo topInfo = new TopInfo();
                //System.out.println(element.toString());
                //System.out.println("----------------");
                //if (element.select("span.key_name").select("a").attr("uid") == "0")
                 //   continue;
                topInfo.setCat(cat);
                topInfo.setArea(area);
                topInfo.setMonth(month);
                topInfo.setTop(no);
                topInfo.setUid(element.select("span.key_name").select("a").attr("uid"));
                topInfo.setNick(element.select("span.key_name").text());
                topInfo.setInflu(element.select("span.num").text());
                topInfo.setDescrip(element.select("span.appro").text());
                if (topInfo.getUid() == "0"||topInfo.getUid() ==null||topInfo.getUid()=="")
                    continue;
                no++;
                //followInfo.setFollowUid(element.select("a[class=W_f14 S_func1]").attr("usercard").substring(3));
                //followInfo.setFollowNickname(element.select("a[class=W_f14 S_func1]").text());
                //followInfo.setFollowFollow(element.select("div.connect").select("a[target=_blank]").get(0).text());
                //followInfo.setFollowFans(element.select("div.connect").select("a[target=_blank]").get(1).text());
                //followInfo.setFollowWeibo(element.select("div.connect").select("a[target=_blank]").get(2).text());
                //System.out.println("================");
                topList.add(topInfo);
            }
        }
        return topList;
    }
    
    public static int insertTopToDB(ArrayList<TopInfo> topList, Connection conn) {
        String insertSQL = "INSERT INTO `toplist` (`category`, `area`, `month`, `top`, `uid`,"
                         + "`nick`, `influence`, `description`)"
                         + "VALUES (?,?,?,?,?,?,?,?);";
        if (topList != null && topList.size() > 0) {
            try {
                int parameterIndex = 1;
                PreparedStatement pst = conn.prepareStatement(insertSQL);
                for (int i = 0; i < topList.size(); i++) {
                    parameterIndex = 1;
                    TopInfo top = topList.get(i);
                    pst.setString(parameterIndex++, top.getCat());
                    pst.setString(parameterIndex++, top.getArea());
                    pst.setLong(parameterIndex++, top.getMonth());
                    pst.setLong(parameterIndex++, top.getTop());
                    pst.setLong(parameterIndex++, WeiboDB.stringToLong(top.getUid()));
                    pst.setString(parameterIndex++, top.getNick());
                    pst.setLong(parameterIndex++, WeiboDB.stringToLong(top.getInflu()));
                    pst.setString(parameterIndex++, top.getDescrip());
                    logger.debug(pst.toString());
                    pst.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
  
    public static void main(String[] args) {
        
        String path = "C://Users//cisl//workspace//SimpleWeiboCrawler//4";
        File file = new File(path);
        File[] array = file.listFiles();
        System.out.println(array.length);
        for(int i = 0; i < array.length; i++){
            String [] arrayList = array[i].getName().substring(0, array[i].getName().length()-4).split("-");
            if (arrayList.length==3) {
                arrayList[1] = arrayList[2]; 
            }
            //System.out.println(array[i].getPath());
            
            String retData = null;
            BufferedReader br = null;
            StringBuffer buffer = null;
            try{
                buffer = new StringBuffer();
                InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(array[i].getPath())),"utf-8");
                br = new BufferedReader(isr); 
                int s;
                while((s = br.read())!=-1){
                    buffer.append((char)s);
                }
            retData = buffer.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            Connection weiboDB = null;
            try {
                weiboDB = WeiboDB.getConnection();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //System.out.println("===" + retData  + "===");
            insertTopToDB(parserTop(retData, arrayList[0], arrayList[1], 4),weiboDB);
            
        }
     
        //top(Config.cookie);
    }
    
}
