/*
 * 文 件 名:  WxLoginHelper.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  suwei
 * 修改时间:  2016年6月14日
 */
package com.dimeng.p2p.app.servlets.platinfo.wechat;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.p2p.app.servlets.platinfo.Login;
import com.dimeng.p2p.common.entities.ThirdPartyUser;
import com.dimeng.util.StringHelper;

/**
* <第三方微信登录帮助类>
* @author  suwei
* @version  [版本号, 2016年6月14日]
*/

public class WxLoginHelper
{
    /**
    * 获取微信用户登录用户的Token和用户openId
    * <功能详细描述>
    * @param url
    * @param code
    * @param host
    * @return
    * @throws Exception
    */
    public static final Map<String, String> getWxTokenAndOpenid(Controller controller, HttpServletRequest request, HttpServletResponse response, String appid, String secret, String code)
        throws Exception
    {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
        url = url + "?appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";
        Map<String, String> map = new HashMap<String, String>();
        // 获取令牌
        try
        {
            String tokenRes = HttpUtil.httpClientGet(url);
            if (tokenRes != null && tokenRes.indexOf("access_token") > -1)
            {
                Map<String, String> tokenMap = toMap(tokenRes);
                map.put("access_token", tokenMap.get("access_token"));
                map.put("refresh_token", tokenMap.get("refresh_token"));
                // 获取微信用户的唯一标识openid
                map.put("openId", tokenMap.get("openid"));
            }
            else
            {
                throw new IllegalArgumentException("THIRDPARTY.LOGIN.NOTOKEN");
            }
        }
        catch (Exception e)
        {
            controller.forward(request, response, controller.getURI(request, Login.class));
        }
        
        return map;
    }
    
    /**
    * 将格式为s1&s2&s3...的字符串转化成Map集合
    * 
    * @param str
    * @return
    */
    private static final Map<String, String> toMap(String str)
    {
        Map<String, String> map = new HashMap<String, String>();
        String[] strs = str.split("&");
        for (int i = 0; i < strs.length; i++)
        {
            String[] ss = strs[i].split("=");
            map.put(ss[0], ss[1]);
        }
        return map;
    }
    
    /**
    * 获取微信登录的用户信息
    * <功能详细描述>
    * @param token
    * @param openid
    * @return
    * @throws Exception
    */
    public static ThirdPartyUser getWxUserinfo(String token, String openid)
        throws Exception
    {
        ThirdPartyUser user = new ThirdPartyUser();
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + openid;
        String res = HttpUtil.httpClientGet(url);
        JSONObject json = JSONObject.fromObject(res);
        if (json.getString("errcode") == null)
        {
            user.openid = json.getString("openid");
            user.nickName = json.getString("nickname");
            String img = json.getString("headimgurl");
            if (!StringHelper.isEmpty(img))
            {
                user.headImgUrl = img;
            }
            
            //普通用户性别，1为男性，2为女性
            user.gender = "1".equals(json.getString("sex")) ? "1" : "2";
        }
        else
        {
            throw new IllegalArgumentException(json.getString("errmsg"));
        }
        return user;
    }
    
}
