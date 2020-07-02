package com.wechat.demo.qiWechat;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理图片   Base64编码的相互转换
 * @Author: tanghh18
 * @Date: 2019/7/5 17:20
 */
public class ImageUtil {
    public static void main(String[]args){
        String strImg = getImageStr("Z:\\水印\\2.bmp");
        System.out.println(strImg);
    }
    /**
     * 根据图片地址转换为base64编码字符串
     * @param imgFile
     * @return
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    /**
     * 将base64编码字符串转换为图片
     * @param imgStr
     * @param path
     * @return
     */
    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null)
            return false;
        // 解密
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] b = decoder.decode(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * JAVA 利用正则表达式截取img标签（封装成方法*便于调用）
     * @param htmlStr
     * @return
     */
    public static List<String> getImgSrc(String htmlStr) {
        String img = "";
        Pattern patternImage;
        Matcher matcherImage;
        List<String> pictures = new ArrayList<String>();
        String regexImage = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        patternImage = Pattern.compile(regexImage, Pattern.CASE_INSENSITIVE);
        matcherImage = patternImage.matcher(htmlStr);
        while (matcherImage.find()) {
            img = img + "," + matcherImage.group();
            String imageRule = "src\\s*=\\s*\"?(.*?)(\"|>|\\s+)";
            Matcher m = Pattern.compile(imageRule).matcher(img);
            while (m.find()) {
                pictures.add(m.group(1));
            }
        }
        return pictures;
    }

    /**
     * 将fastdfs 服务器上的图片下载到本地
     * @param urlString
     * @param filename
     * @param savePath
     * @throws Exception
     */
    public static void download(String urlString, String filename,String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5*1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
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
     * 获取图片后缀
     * @param imagePath
     * @return
     */
    public static String getPictureSuffix(String imagePath){
        if (imagePath == null || imagePath.indexOf(".") == -1){
            //如果图片地址为null或者地址中没有"."就返回""
            return "";
        }
        return imagePath.substring(imagePath.lastIndexOf(".") + 1).
                trim().toLowerCase();
    }

    /**
     * 获取图片名字
     * @param imgUrl
     * @return
     */
    public static String getImageName(String imgUrl) {
        if (imgUrl == null) {
            return null;
        }
        String[] strs = imgUrl.split("/");
        return strs[strs.length - 1];
    }
}
