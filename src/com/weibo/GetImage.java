/**   
* @Title: GetImage.java
* @Package com.weibo
* @Description: TODO
* @author Huaigui   
* @date 2015年12月21日 下午9:51:39
* @version V1.0   
*/
package com.weibo;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;  
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.omg.CORBA.PUBLIC_MEMBER;

import com.weibo.util.DownloadImage;
import com.weibo.util.WeiboDB;


/** 
 * @说明 从网络获取图片到本地 
 * @author 崔素强 
 * @version 1.0 
 * @since 
 */  
public class GetImage extends Thread {  
	/** @Fields ThreadNum : 多线程读写的线程数 */
	public static int ThreadNum = 10;
	/** @Fields lock : 修改线程时实现同步安全 */
	private final static ReentrantLock lock = new ReentrantLock();
	/** @Fields logger : 日志文件  */
	private static Logger logger = Logger.getLogger(DownloadImage.class);
    /** @Fields writePath : 图片存储根目录 */
    private static String writePath; 
    /** @Fields fileName : 图片名称  */
    private static String fileName;
    /** @Fields ImgUrl : 图片地址  */
    private static String ImgUrl;
    /** @Fields flag : 获取图片成功标志 ，0 ：成功 ，1：不成功  */
    /** @Fields undownloard : 没下载成功的图片存档  */
    public static String undownloard = "D:/Sina_weibo/image/undownloard.txt";
    public static int flag=0; // 连接获取标志
  
    /**
    * <p>Title: </p>
    * <p>Description:构造函数 用于多线程 写入存储参数 </p>
    * @param path
    * @param img
    * @param name
    */
    public GetImage(String path,String imgurl,String name){
    	this.ImgUrl = imgurl;
    	this.writePath = path;
    	this.fileName = name;
    }
    /* (非 Javadoc) 
    * <p>Title: run</p> 
    * <p>Description:多线程下载、存储图片 </p>  
    * @see java.lang.Thread#run() 
    */ 
    public void run() {
    	//有多个线程操作，所以变量先保存 避免出错
    	String ImgUrl = this.ImgUrl;
    	String fileName = this.fileName;
    	String writePath = this.writePath;
    	
    	logger.info("线程"+Thread.currentThread().getName()+"正在下载："+ImgUrl);
    	byte[] btImg = getImageFromNetByUrl(ImgUrl);
    	if(null != btImg && btImg.length > 0){  
    		//System.out.println("读取到：" + btImg.length + " 字节");  
    		logger.info("线程"+Thread.currentThread().getName()+"正在写入"+fileName+"磁盘！");	
    		writeImageToDisk(btImg,writePath,fileName); 
    	}
    	else if(flag ==1){
    		flag =0;//设置标志位 是否获取的数据
    		//没抓取到的图片存档
			try {
				FileWriter fw= new FileWriter(undownloard,true);
				BufferedWriter cw=new BufferedWriter(fw);
				cw.write(ImgUrl + " " + writePath + " " +fileName+"\r\n");
				cw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//加锁避免出错
		
		}
    	lock.lock();
		try {
			ThreadNum++;
		} finally {
			lock.unlock();
		}
    }

    
	/** 
     * 将图片写入到磁盘 
     * @param img 图片数据流 
     * @param fileName 文件保存时的名称 
     */  
    public static void writeImageToDisk(byte[] btImg,String writePath,String fileName){  
    	 File sf=new File(writePath);  
         if(!sf.exists()){  
             sf.mkdirs();  
         }  
        
    	try {  
            OutputStream os = new FileOutputStream(sf.getPath()+"\\"+fileName);  
            os.write(btImg);  
            os.flush();  
            os.close();  
            logger.info("线程"+Thread.currentThread().getName()+"将图片"+fileName+"写入到磁盘！");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    /** 
     * 根据地址获得数据的字节流 
     * @param strUrl 网络连接地址 
     * @return 
     */  
    public static byte[] getImageFromNetByUrl(String strUrl){  
        try {  
            URL url = new URL(strUrl);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(5 * 1000);  
            InputStream inStream = null;//通过输入流获取图片数据  
            int i = 0;
            //尝试5次获取图片
            while (inStream == null && i<5){
            	if (i>0)Thread.sleep(1000);//第一次获取不成功  等待一会儿
            	try {
					inStream= conn.getInputStream();
					i++; 
				} catch (Exception e) {
					// TODO: handle exception
				}
            } 
            if(inStream == null && i==5){
            	System.out.println("获取图片内容不成功！重新下载");
            	flag = 1;
            }
            	
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据  
            return btImg;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    /** 
     * 从输入流中获取数据 
     * @param inStream 输入流 
     * @return 
     * @throws Exception 
     */  
    public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while( (len=inStream.read(buffer)) != -1 ){  
            outStream.write(buffer, 0, len);  
        }  
        inStream.close();  
        return outStream.toByteArray();  
    }  
    
    
    /** 
     * 入口地址  调用多线程下载和存储图片
     * @param args 
     * @throws Exception 
     */  
    public static void main(String[] args) throws Exception {  
    	Config config = new Config();//获取配置文件
    	String [] pictureHerf;
    	String ImgPath = config.IMG_STORE_PATH;//图片存储路径
    	  
    	//连接数据库
    	Connection weiboDB = null;
        try {
            weiboDB = WeiboDB.getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        //数据库操作
        String sql = "select * from content where Id <= 182510 and downloard = 0";
        //String sql = "select * from content where Id <= 182510 and downloard = 1";
        PreparedStatement pst = weiboDB.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        
        String name;
        String dbpath;
       
        try {
			ResultSet rs = pst.executeQuery();//查询结果
			int i=0;
			while (rs.next()) {
				pictureHerf = rs.getString("pictureHerf").split(",");
				System.out.println("=============正在下载第"+rs.getInt("Id")+"个       共："+pictureHerf.length+"张");
				
				//取随机数的任意张				
				Random rand = new Random();
				int randNum = rand.nextInt(pictureHerf.length);
			 	name = pictureHerf[randNum].substring(pictureHerf[randNum].lastIndexOf("/")+1);
			 	dbpath = ImgPath+"/"+rs.getString("uid")+"/"+name;
			 	dbpath = dbpath.replace("/", "\\");//获取本地绝对地址
			 	//判断是否能启动新的线程
			 	while(ThreadNum == 0){
			 		  System.out.println("=================无空闲线程，等待一会儿!==============");
			 		  Thread.sleep(1000);
			 	} 
			 	//启动多线程进行写
			 	if(ThreadNum > 0){
			 		//加锁避免出错
			 		lock.lock();
					try {
						ThreadNum--;
					} finally {
						lock.unlock();
					}
			 		
			    	//System.out.println(ThreadNum);
			    	Thread thread = new GetImage(ImgPath+"/"+rs.getString("uid"), pictureHerf[randNum], name);
			     	thread.start();
			     	Thread.sleep(5);//等100毫秒启动线程
			    }
			 	Thread.sleep(100);
			    WeiboDB.insertImage(rs.getString("mid"),dbpath, weiboDB);//地址存入数据库
			    
			 	
			    
			    /****获取全部图片
				 for (int j = 0; j < pictureHerf.length; j++) {
				
					name = pictureHerf[j].substring(pictureHerf[j].lastIndexOf("/")+1);
					//返回最后一个符号为*‘/’后字符串变量中的所有字符，不包裹此自身符号/  ，即提取图片名
					dbpath = ImgPath+"/"+rs.getString("uid")+"/"+name;
				 	dbpath = dbpath.replace("/", "\\");//获取本地绝对地址
				 	//判断是否能启动新的线程
				 	while(ThreadNum == 0){
				 		  System.out.println("=================无空闲线程，等待一会儿!==============");
				 		  Thread.sleep(1000);
				 	} 
				 	//启动多线程进行写
				 	if(ThreadNum > 0){
				    	ThreadNum--; // 启动线程  线程数减1
				    	//System.out.println(ThreadNum);
				    	Thread thread = new GetImage(ImgPath+"/"+rs.getString("uid"), pictureHerf[j], name);
				     	thread.start();
				     	Thread.sleep(5);//等100毫秒启动线程
				    }
				    WeiboDB.insertImage(rs.getString("mid"),dbpath, weiboDB);//地址存入数据库
				    Thread.sleep(100);
				}
				
				 */	
				rs.updateInt("downloard",1);
				rs.updateRow();

			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}       
        //关闭数据库
        try {
            weiboDB.close(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("+————————————————————————————————————————————————+");
        System.out.println("+------------------->| END! |<-------------------+");
        System.out.println("+————————————————————————————————————————————————+");
    }  
} 