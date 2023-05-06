package com.github.binarywang.demo.wx.mp.handler;

import com.github.binarywang.demo.wx.mp.builder.TextBuilder;
import com.github.binarywang.demo.wx.mp.utils.JsonUtils;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Component
public class MsgHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }

        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        try {
            if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
                && weixinService.getKefuService().kfOnlineList()
                .getKfOnlineList().size() > 0) {
                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser()).build();
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        //解析淘口令
        if (StringUtils.startsWithAny(wxMessage.getContent(), "￥")) {
            String content = wxMessage.getContent();
            String[] split = content.split("￥");
            String tk = split[1];
            String url = "https://api.open.21ds.cn/apiv1/taoke/getTbkTpwdInfo?apkey=1b2c3d4e5f6g7h8i&tpwd=" + tk;
//            String s = JsonUtils.(url);
//            String coupon_click_url = JsonUtils.getJsonString(s, "data", "coupon_click_url");
//            String coupon_info = JsonUtils.getJsonString(s, "data", "coupon_info");
//            String coupon_remain_count = JsonUtils.getJsonString(s, "data", "coupon_remain_count");
//            String coupon_total_count = JsonUtils.getJsonString(s, "data", "coupon_total_count");
//            String coupon_start_time = JsonUtils.getJsonString(s, "data", "coupon_start_time");
//            String coupon_end_time = JsonUtils.getJsonString(s, "data", "coupon_end_time");
//            String coupon_amount = JsonUtils.getJsonString(s, "data", "coupon_amount");
//            String coupon_share_url = JsonUtils.getJsonString(s, "data", "coupon_share_url");
//            String coupon_short_url = JsonUtils.getJsonString(s, "data", "coupon_short_url");
//            String coupon_tpwd = JsonUtils.getJsonString(s, "data", "coupon_tpwd");
//            String coupon_tpwd_url = JsonUtils.getJsonString(s, "data", "coupon_tpwd_url");
//            String item_id = JsonUtils.getJsonString(s, "data", "item_id");
//            String item_url = JsonUtils.getJsonString(s, "data", "item_url");
//            String max_commission_rate = JsonUtils.getJsonString(s, "data", "max_commission_rate");
//            String max_commission_rate_time = JsonUtils.getJsonString(s, "data", "max_commission_rate_time");
//            String min_commission_rate = JsonUtils.getJsonString(s, "data", "min_commission_rate");
//            String min_commission_rate_time = JsonUtils.getJsonString(s, "data", "min_commission_rate_time");
//            String short_url = JsonUtils.getJsonString(s, "data", "short_url");
//            String tao_id = JsonUtils.getJsonString(s, "data",)
        }

        //TODO 组装回复消息
        String content = "收到信息内容：" + JsonUtils.toJson(wxMessage);

        return new TextBuilder().build(content, wxMessage, weixinService);

    }

}
