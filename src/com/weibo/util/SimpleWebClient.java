package com.weibo.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

import com.weibo.Config;


public class SimpleWebClient {
    /** 日志对象. */
    private static Logger logger = Logger.getLogger(SimpleWebClient.class);
    /**
     * 根据URL,下载网站的页面.
     * @param pageURL
     * @param cookie
     * @return WebPage
     */
    public WebPage getPageContent(String pageURL, String cookie) {
        WebPage webPage = null;
        try {
            //休眠10秒钟
            Thread.currentThread().sleep(Config.THREAD_SLEEP);
            //页面的编码
            String contentEncoding = "";
            //页面类型
            String contentType = "";
            //页面的大小
            int contentLength = -1;
            //数据字节流
            byte []data = null;
            //数据读取的位置
            int bytesRead = 0;
            int offset = 0;
            URL url = new URL(pageURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);

            //设置请求的参数
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) "
                                  + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,"
                                  + "application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.8");
            //在HTTP压缩传输中启用启用Gzip的方法：发送HTTP请求的时候在HTTP头中增加： Accept-Encoding:gzip,deflate
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
            conn.setRequestProperty("Accept-Charset", "gb2312,utf-8;q=0.7,*;q=0.7");
            // weibo cookie
            if (cookie != null && !"".equals(cookie)) {
                conn.setRequestProperty("Cookie", cookie);
            }
            //设置链接超时
            conn.setConnectTimeout(10 * 60 * 1000);
            conn.setReadTimeout(10 * 60 * 1000);

            conn.connect();
  
            /*
            //显示返回的请求的Header信息.
            for (int i = 1;; i++) {
                String header = conn.getHeaderField(i);
                if (header == null) {
                    break;
                }
                logger.debug(conn.getHeaderFieldKey(i) + " : " + header);
            }*/
            //从Header头部查找字符编码
            contentType = conn.getContentType();
            //System.out.println("conn.getContentType() " + conn.getContentType());
            //System.out.println("conn.getContentType() " + conn.getHeaderField("Contetnt-Type"));
            if (contentType != null && (contentType.toLowerCase().indexOf("charset") != -1))
            {
                   contentType = contentType.toLowerCase();
                try {
                    contentEncoding = contentType.substring(contentType.indexOf("charset=")
                                    + "charset=".length());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            contentLength = conn.getContentLength();
            int responseCode = conn.getResponseCode();
            logger.info(pageURL + "\t" + responseCode);
            //System.out.println("contentLength = " +contentLength);
            //如果有Conent-Length字段,就按指定的大小读取数据；但是如果用“gzip”压缩传送，就不能Conent-Length长度以为准。
            if (contentLength != -1) {
                InputStream in = null;
                //判断数据是否压缩?
                if (conn.getContentEncoding() != null && conn.getContentEncoding().indexOf("gzip") > -1)
                {
                    in = new GZIPInputStream(conn.getInputStream());
                    //把数据暂时保存在内存中
                    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                    data = new byte[4096];
                    int read = 0;
                    while ((read = in.read(data)) != -1) {
                        arrayOutputStream.write(data, 0, read);
                    }
                    data = arrayOutputStream.toByteArray();
                    arrayOutputStream.close();
                } else {
                    in = new BufferedInputStream(conn.getInputStream());
                    data = new byte[contentLength];
                    bytesRead = 0;
                    offset = 0;
                    while (offset < contentLength) {
                        bytesRead = in.read(data, offset, data.length - offset);
                        if (bytesRead == -1) {
                            break;
                        }
                        offset += bytesRead;
                    }
                }
                in.close();
                //取得页面字符集
                if (contentEncoding == null || contentEncoding.equals("")) {
                    contentEncoding = getCharset(new String(data, 0, data.length));
                    if (contentEncoding.equalsIgnoreCase("gb2312")) {
                        contentEncoding = "gbk";
                    }
                }
                //关闭链接
                conn.disconnect();
            } else { //如果不知道数据大小,循环读取,直到没有数据.
                InputStream in = null;
                if (conn.getContentEncoding() != null && conn.getContentEncoding().indexOf("gzip") > -1) {
                    in = new GZIPInputStream(conn.getInputStream());
                } else {
                    in = new BufferedInputStream(conn.getInputStream());
                }
                //把数据暂时保存在内存中
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                data = new byte[4096];
                int read = 0;
                while ((read = in.read(data)) != -1) {
                    arrayOutputStream.write(data, 0, read);
                }
                arrayOutputStream.close();
                in.close();

                //关闭链接
                conn.disconnect();
                data = arrayOutputStream.toByteArray();
                //取得页面字符集
                if (contentEncoding.equals("") || contentEncoding == null) {
                    contentEncoding = getCharset(new String(data, 0, 1000));
                    if (contentEncoding.equalsIgnoreCase("gb2312")) {
                        contentEncoding = "gbk";
                    }
                }
            }
            webPage = new WebPage();
            webPage.setHref(pageURL);
            webPage.setContentEncoding(contentEncoding);
            webPage.setContentLength(data.length);
            webPage.setHtml(new String(data, 0, data.length, contentEncoding));

            return webPage;
        } catch (Exception e) {
            e.printStackTrace();
            return webPage;
        }
    }
    /**
     * 取得页面的字符集.
     * @param content 页面开头的一段字符串
     * @return 字符集类型
     */
    public String getCharset(String content) {
        try {
            String contentEncoding = "";
            content = content.toLowerCase();
            if (content != null && content.indexOf("content-type") > -1) {
                contentEncoding = content.substring(content.indexOf("charset="));
                contentEncoding = contentEncoding.substring(contentEncoding.indexOf("charset=") + "charset=".length());
                contentEncoding = contentEncoding.substring(0, contentEncoding.indexOf("\""));
            } else {
                contentEncoding = "gbk";
            }
            return contentEncoding;
        } catch (Exception e) {
            e.printStackTrace();
            return "gbk";
        }
    }
}
