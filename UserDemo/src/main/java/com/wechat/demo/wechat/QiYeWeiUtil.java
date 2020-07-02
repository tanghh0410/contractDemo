package com.wechat.demo.wechat;

import com.wechat.demo.qiWechat.AesException;
import com.wechat.demo.qiWechat.WXBizMsgCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author tanghh
 * @Date: 2019/4/17
 */
public class QiYeWeiUtil {
    private static Logger logger = LoggerFactory.getLogger(QiYeWeiUtil.class);
    // 微信加密签名
    private String msg_signature;
    // 时间戳
    private String timestamp;
    // 随机数
    private String nonce;

    /**
     * 1.企业微信上传素材的请求方法
     *
     * @param
     * @return
     */
    public static String httpRequest(String requestUrl, File file) {
        StringBuffer buffer = new StringBuffer();

        try {
            //1.建立连接
            URL url = new URL(requestUrl);
            //打开链接
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

            //1.1输入输出设置
            httpUrlConn.setDoInput(true);
            httpUrlConn.setDoOutput(true);
            // post方式不能使用缓存
            httpUrlConn.setUseCaches(false);
            //1.2设置请求头信息
            httpUrlConn.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConn.setRequestProperty("Charset", "UTF-8");
            //1.3设置边界
            String BOUNDARY = "----------" + System.currentTimeMillis();
            httpUrlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            // 请求正文信息
            // 第一部分：
            //2.将文件头输出到微信服务器
            StringBuilder sb = new StringBuilder();
            // 必须多两道线
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" + file.length()
                    + "\";filename=\"" + file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            byte[] head = sb.toString().getBytes("utf-8");
            // 获得输出流
            OutputStream outputStream = new DataOutputStream(httpUrlConn.getOutputStream());
            // 将表头写入输出流中：输出表头
            outputStream.write(head);

            //3.将文件正文部分输出到微信服务器
            // 把文件以流文件的方式 写入到微信服务器中
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                outputStream.write(bufferOut, 0, bytes);
            }
            in.close();
            //4.将结尾部分输出到微信服务器
            // 定义最后数据分隔线
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
            outputStream.write(foot);
            outputStream.flush();
            outputStream.close();


            //5.将微信服务器返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();


        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 2.发送https请求之获取临时素材
     *
     * @param requestUrl
     * @param savePath   文件的保存路径，此时还缺一个扩展名
     * @return
     * @throws Exception
     */
    public static File getFile(String requestUrl, String savePath) throws Exception {
        //String path=System.getProperty("user.dir")+"/img//1.png";
        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager[] tm = {new MyX509TrustManager()};
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();

        URL url = new URL(requestUrl);
        HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
        httpUrlConn.setSSLSocketFactory(ssf);

        httpUrlConn.setDoOutput(true);
        httpUrlConn.setDoInput(true);
        httpUrlConn.setUseCaches(false);
        // 设置请求方式（GET/POST）
        httpUrlConn.setRequestMethod("GET");

        httpUrlConn.connect();

        //获取文件扩展名
        String ext = getExt(httpUrlConn.getContentType());
        savePath = savePath + ext;
        System.out.println("savePath" + savePath);
        //下载文件到f文件
        File file = new File(savePath);


        // 获取微信返回的输入流
        InputStream in = httpUrlConn.getInputStream();

        //输出流，将微信返回的输入流内容写到文件中
        FileOutputStream out = new FileOutputStream(file);

        int length = 100 * 1024;
        //存储文件内容
        byte[] byteBuffer = new byte[length];

        int byteread = 0;
        int bytesum = 0;
        //字节数 文件大小
        while ((byteread = in.read(byteBuffer)) != -1) {
            bytesum += byteread;
            out.write(byteBuffer, 0, byteread);

        }
        System.out.println("bytesum: " + bytesum);

        in.close();
        // 释放资源
        out.close();
        in = null;
        out = null;

        httpUrlConn.disconnect();


        return file;
    }

    private static String getExt(String contentType) {
        if ("image/jpeg".equals(contentType)) {
            return ".jpg";
        } else if ("image/png".equals(contentType)) {
            return ".png";
        } else if ("image/gif".equals(contentType)) {
            return ".gif";
        }

        return null;
    }


    /**
     * @param request
     * @return String   消息明文
     * @desc ：2.从request中获取消息明文
     */
    public String getDecryptMsg(HttpServletRequest request) throws IOException {
        // 密文，对应POST请求的数据
        String postData = "";
        // 明文，解密之后的结果
        String result = "";
        // 微信加密签名
        this.msg_signature = request.getParameter("msg_signature");
        // 时间戳
        this.timestamp = request.getParameter("timestamp");
        // 随机数
        this.nonce = request.getParameter("nonce");

        try {
            //1.获取加密的请求消息：使用输入流获得加密请求消息postData
            ServletInputStream in = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            //作为输出字符串的临时串，用于判断是否读取完毕
            String tempStr = "";
            while (null != (tempStr = reader.readLine())) {
                postData += tempStr;
            }

            //2.获取消息明文：对加密的请求消息进行解密获得明文
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(QiWeiXinParamesUtil.token, QiWeiXinParamesUtil.encodingAESKey, QiWeiXinParamesUtil.corpId);
            result = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, postData);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (AesException e) {
            e.printStackTrace();
        }

        return result;
    }

}
