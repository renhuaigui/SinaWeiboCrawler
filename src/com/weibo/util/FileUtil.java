package com.weibo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class FileUtil {
    /**
     * 读取文件内容.
     * @param fileName        文件名称
     * @param charsetName    文件编码
     * @return
     * @throws IOException
     */
    public static String readText(String fileName, String charsetName) throws IOException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader breader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charsetName));
        String line = null;
        while ((line = breader.readLine()) != null) {
            buffer.append(line + "\r\n");
        }
        breader.close();
        return buffer.toString();
    }
    /**
     * 保存页面.
     * @param path        本地路径
     * @param fileName    文件名称
     * @param charset    文件编码
     * @param content    页面内容
     */
    public static void writerFile(String path, String fileName, String charset, String content) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(new File(path + File.separator + fileName));
            OutputStreamWriter out = new OutputStreamWriter(fos, charset);
            out.write(content);
            out.flush();
            out.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
