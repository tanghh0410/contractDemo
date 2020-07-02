package com.wechat.demo.controller;

import com.wechat.demo.qiWechat.WXBizMsgCrypt;
import com.wechat.demo.service.WechatService;
import com.wechat.demo.wechat.QiWeiXinParamesUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author tanghh
 * @Date 2020/7/2 16:24
 */
@RestController
public class WechatController {
    private Logger logger = LoggerFactory.getLogger(WechatController.class);
    @Autowired
    private WechatService wechatService;
    /**
     * 企业微信消息接收事件（用于通讯录同步或消息接收）
     * 接口作用：  企业微信发生通讯录变更事件要调用这个接口，无需手动调用，企业微信后台有配置这个接口名字
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "1.4 企业微信接收通讯录变更事件")
    @RequestMapping(value = "/changeNews")
    public void changeNews(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.1企业微信接收消息服务器配置的代码部分
        // 微信加密签名
        String msg_signature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        logger.warn("微信加密签名,msg_signature:"+msg_signature);
        logger.warn("时间戳,timestamp:"+timestamp);
        logger.warn("随机数,nonce:"+nonce);
        logger.warn("随机字符串,echostr:"+echostr);
        PrintWriter out = response.getWriter();
        // 通过检验msg_signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        String result = null;
        WXBizMsgCrypt wxcpt = null;
        try {
            wxcpt = new WXBizMsgCrypt(QiWeiXinParamesUtil.token, QiWeiXinParamesUtil.encodingAESKey, QiWeiXinParamesUtil.corpId);
            result = wxcpt.VerifyURL(msg_signature, timestamp, nonce, echostr);

            //更新成员，部门信息
//            wechatService.getEncryptRespMessage(request);
            if (result == null) {
                result = QiWeiXinParamesUtil.token;
            }
            out.print(result);
            out.close();
            out = null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("changeNews接口报错", e);
        }
    }
}
