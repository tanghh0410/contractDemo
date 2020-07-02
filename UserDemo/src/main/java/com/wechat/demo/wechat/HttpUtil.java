package com.wechat.demo.wechat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class HttpUtil {
    public static String httpUrlConnect(String httpUrl, String params,
                                        String method) throws Exception {

        URL url = new URL(httpUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url
                .openConnection();
        urlConnection.setRequestProperty("accept", "*/*");
        urlConnection.setRequestProperty("connection", "Keep-Alive");
        urlConnection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod(method);
        urlConnection.connect();
        PrintWriter out = null;
        BufferedReader in = null;
        if (null != params && !"".equals(params)) {
            out = new PrintWriter(new OutputStreamWriter(
                    urlConnection.getOutputStream(), "utf-8"));
            out.print(params);
            out.flush();
        }
        in = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream(), "utf-8"));
        String line;
        String result = "";
        while ((line = in.readLine()) != null) {
            result += line;
        }
        return result;

    }

    /**
     * 根据字符串json数据解析access_token
     *
     * @param jsonStr
     * @return map
     */
    public static Map<String, Object> getAccessTokenByJsonStr(String jsonStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject jsonObj = new JSONObject(jsonStr);
        if (jsonObj.has("access_token")) {
            map.put("access_token", jsonObj.get("access_token"));
        }
        if (jsonObj.has("expires_in")) {
            map.put("expires_in", jsonObj.get("expires_in"));
        }
        return map;
    }


}
