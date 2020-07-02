package com.wechat.demo.wechat;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

/**
 * @author tanghh
 */
@Component
public class QiWeiXinParamesUtil {

    /**
     * encodingAESKey
     */
    public static String encodingAESKey = "*****";
    /**
     * encodingAESKey
     */
    public static String token = "***";
    /**
     * 企业ID（yes）+
     */
    public static String corpId = "*****";

    /**
     * 通讯录秘钥（通讯录同步yes）
     */
    public static String contactsSecret = "****";

    /**
     * 应用的凭证密钥(yes)
     */
    public static String agentSecret = "g6lk7FIJrzSr***8zriBPcYSDsA64AQ4YmOs";

    /**
     * 企业应用的id，整型。可在应用的设置页面查看(yes)项目测试（ebo0.2版本）
     */
    public static int agentId = 1000049;

    /**
     * 获取access_token的url
     */
    public final static String access_token_url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={corpId}&corpsecret={corpsecret}";



}
