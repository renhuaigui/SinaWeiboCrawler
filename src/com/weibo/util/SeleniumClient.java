package com.weibo.util;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class SeleniumClient {
    public static String GetCookie() throws InterruptedException {
        //WebDriver webDriver = new FirefoxDriver();
        WebDriver webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //webDriver.get("http://weibo.com/p/1002061729332983/weibo?page=1");
        webDriver.get("http://top.weibo.com/newtop/influencedetail?second=29&depart=1");
       
        Thread.sleep(20000);
        Set<Cookie> cookies = webDriver.manage().getCookies();
        String cookieStr = "";
        for (Cookie cookie : cookies) {
            cookieStr += cookie.getName() + "=" + cookie.getValue() + "; ";
        }
        System.out.println("Title : " + webDriver.getTitle());
        System.out.println("Cookie : " + cookieStr);
        //退出，关闭浏览器
        //webDriver.quit();
        return cookieStr;
    }
    public static void main(final String[] args) throws InterruptedException {
        System.out.println(GetCookie());
    }
}
