package com.wechat.demo.wechat;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author luojie on 2018/8/27
 */
public class SendRequest {

    /**
     * 发送GET请求
     *
     * @param url
     * @return
     */
    public static JSONObject sendGet(String url) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        JSONObject jsonObject = null;
        StringBuffer sb = new StringBuffer();
        BufferedReader in = null;
        try {
            String urlName = url;
            URL realUrl = new URL(urlName);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            //针对群晖NAS请求，加一个Cookie
            if (session.getAttribute("sid") != null) {
                conn.addRequestProperty("Cookie", "id=" + session.getAttribute("sid"));
            }
            conn.setConnectTimeout(10000);
            // 建立实际的连接
            conn.connect();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            jsonObject = JSON.parseObject(sb.toString());
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
        } finally {
            // 使用finally块来关闭输入流
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                System.out.println("关闭流异常");
            }
        }
        return jsonObject;
    }


    /**
     * 发送get 请求，比如定时任务调用的就是请求
     *
     * @param url
     * @return
     */
    public static JSONObject sendGet2(String url) {
        JSONObject jsonObject = null;
        StringBuffer sb = new StringBuffer();
        BufferedReader in = null;
        try {
            String urlName = url;
            URL realUrl = new URL(urlName);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setConnectTimeout(10000);
            // 建立实际的连接
            conn.connect();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            jsonObject = JSON.parseObject(sb.toString());
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            // 使用finally块来关闭输入流
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                System.out.println("关闭流异常");
            }
        }
        return jsonObject;
    }


    /**
     * 发送post请求(返回json)
     *
     * @param url
     * @param param
     * @return
     */
    public static JSONObject sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        JSONObject jsonObject = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
//            conn.addRequestProperty("Cookie", "stay_login=1 smid=DumpWzWQSaLmKlFY1PgAtURdV_u3W3beoei96zsXkdSABwjVCRrnnNBsnH1wGWI0-VIflgvMaZAfli9H2NGtJg id=EtEWf1XZRLIwk1770NZN047804");//设置获取的cookie
            // 获取URLConnection对象对应的输出流（设置请求编码为UTF-8）
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 获取请求返回数据（设置返回数据编码为UTF-8）
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            jsonObject = JSONObject.parseObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return jsonObject;
    }

    /**
     * 发送post请求(返回json)
     *
     * @param url
     * @param param
     * @return
     */
    public static String sendPost2String(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuffer sb = new StringBuffer();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            System.out.println("[POST请求]向地址：" + url + " 发送数据：" + param + " 发生错误!");
        } finally {// 使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                System.out.println("关闭流异常");
            }
        }
        return sb.toString();
    }

    /**
     * 获取在session中保存的cookie
     *
     * @param request
     * @return
     */
    private static String getSessionIncookie(HttpServletRequest request) {
        String back = "";
        HttpSession session = request.getSession();

        String verCode = (String) session.getAttribute("verCode");
        String JSESSIONID = (String) session.getAttribute("JSESSIONID");

        System.out.println(verCode);
        System.out.println(JSESSIONID);

        if (verCode != null) {
            back = verCode;
        }

        if (JSESSIONID != null) {
            if (!Strings.isNullOrEmpty(back)) {
                back += " ";
            }
            back += JSESSIONID;
        }

        System.out.println(back);
        return back;
    }


}
