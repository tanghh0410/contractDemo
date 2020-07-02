package com.wechat.demo.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author tanghh
 * @Date 2020/7/2 16:27
 */
public interface WechatService {
    /**
     * 解析企业微信接收过来的消息
     * @param request
     */
    void getEncryptRespMessage(HttpServletRequest request);

}
