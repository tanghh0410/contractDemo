package com.wechat.demo.service.impl;

import com.wechat.demo.service.WechatService;
import com.wechat.demo.wechat.QiYeWeiUtil;
import com.wechat.demo.wechat.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author tanghh
 * @Date 2020/7/2 16:27
 */
@Service
public class WechatServiceImpl implements WechatService {
    private Logger logger = LoggerFactory.getLogger(WechatServiceImpl.class);

    /**
     * 企业微信成员通讯录变更
     * @param request
     */
    @Override
    public void getEncryptRespMessage(HttpServletRequest request) {
        String xmlMsg = "";
        try {
            //1.解密微信发过来的消息
            QiYeWeiUtil qiYeWeiUtil = new QiYeWeiUtil();
            xmlMsg = qiYeWeiUtil.getDecryptMsg(request);

            //2.解析微信发来的请求,解析xml字符串
            Map<String, String> requestMap = MessageUtil.parseXml(xmlMsg);

            //3.获取请求参数
            //3.1 企业微信CorpID
            String fromUserName = requestMap.get("FromUserName");
            //3.2 成员UserID
            String toUserName = requestMap.get("ToUserName");
            //3.3 消息类型与事件
            String msgType = requestMap.get("MsgType");
            String eventType = requestMap.get("Event");
            String changeType = requestMap.get("ChangeType");
            String eventKey = requestMap.get("EventKey");

            //根据消息类型与事件执行不同的操作
            if ("event".equals(msgType) && "change_contact".equals(eventType)) {
                //新增成员
                if ("create_user".equals(changeType)) {
                    System.out.println("进入新增用户数据");
                }
                //修改成员
                if ("update_user".equals(changeType)) {
                   //自己编写处理的逻辑
                    System.out.println("修改成员");
                }
                //删除成员
                if ("delete_user".equals(changeType)) {
                   //自己编写处理的逻辑
                    System.out.println("修改成员");
                }

                //添加和更新部门
                if ("create_party".equals(changeType) || "update_party".equals(changeType)) {
                   //自己编写处理的逻辑
                    System.out.println("添加和更新部门");
                }

                //删除
                if ("delete_party".equals(changeType)) {
                    //错误删除
                    //自己编写处理的逻辑
                    System.out.println("错误删除");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("通讯录变更事件处理异常!{},{}", xmlMsg, e);
        }
    }
}
