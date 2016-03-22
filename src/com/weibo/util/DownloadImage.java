/**   
* @Title: DownloadImage.java
* @Package com.weibo.util
* @Description: TODO
* @author Huaigui   
* @date 2015年12月11日 下午5:07:36
* @version V1.0   
*/
package com.weibo.util;

/**
* @ClassName: DownloadImage
* @Description: TODO(这里用一句话描述这个类的作用)
* @author Huaigui
* @date 2015年12月11日 下午5:07:36
* 
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.net.URL;  
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import java.sql.*;
import com.weibo.Config;  
  
  
public class DownloadImage {  

	private static Logger logger = Logger.getLogger(DownloadImage.class);

      
 
    /** 
    * @Title: download 
    * @Description: 下载图片并存入本地文件中
    * @param urlString 图片地址
    * @param filename 图片存储文件名
    * @param savePath 路径
    * @throws Exception   
    * @return void
    * @throws 
    */ 
    public static void download(String urlString, String filename,String savePath) throws Exception {  
        // 构造URL  
        URL url = new URL(urlString);  
        // 打开连接  
        URLConnection con = url.openConnection();  
        //设置请求超时为20s  
        con.setConnectTimeout(5*1000);  
        // 输入流  
        InputStream is=null;
        int i=0;
        while (is == null && i<5){
        	try {
        		is= con.getInputStream();
        		i++;
        	} catch (Exception e) {
        		System.out.println("获取图片内容不成功！重新下载");
        	} 
        }   
        byte[] bs = new byte[1024];  
        // 读取到的数据长度  
        int len;  
        // 输出的文件流
       File sf=new File(savePath);  
       if(!sf.exists()){  
           sf.mkdirs();  
       }  
       OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);  
        // 开始读取  
        while ((len = is.read(bs)) != -1) {  
          os.write(bs, 0, len);  
        }  
        // 完毕，关闭所有链接  
        os.close();  
        is.close(); 
    }   
    
    
    /** 
     * @param args 
     * @throws Exception  
     */     
    public static void main(String[] args) throws Exception {  
        // TODO Auto-generated method stub  
        //download("http://ui.51bi.com/opt/siteimg/images/fanbei0923/Mid_07.jpg", "51bi.gif","c:\\image\\");  
    	Config config = new Config();//获取配置文件
    	String path;
    	String [] pictureHerf;
    	String name;
    	String dbpath;
		String ImgPath = config.IMG_STORE_PATH;
    	Connection weiboDB = null;
        try {
            weiboDB = WeiboDB.getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "select * from content where Id <= 182510 and downloard=0";
        PreparedStatement pst = weiboDB.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        //pst.setString(1, "30");
        try {
			ResultSet rs = pst.executeQuery();
			int i=0;
			while (rs.next()) {
				pictureHerf = rs.getString("pictureHerf").split(",");
				System.out.println("正在下载第"+rs.getInt("Id")+"个       共："+pictureHerf.length+"张");
				for (int j = 0; j < pictureHerf.length; j++) {
					path = rs.getString("uid");
					name = pictureHerf[j].substring(pictureHerf[j].lastIndexOf("/")+1);
					//返回最后一个符号为*‘/’后字符串变量中的所有字符，不包裹此自身符号/  ，即提取图片名
					logger.info("正在下载："+pictureHerf[j]);
					download(pictureHerf[j], name, ImgPath+"/"+path);
					dbpath = ImgPath+"/"+path+"/"+name;
					dbpath = dbpath.replace("/", "\\");
					WeiboDB.insertImage(rs.getString("uid"),dbpath, weiboDB);	
				}
				rs.updateInt("downloard", 1);
				rs.updateRow();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        
        try {
            weiboDB.close(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }  
    
}  
