/*
 * 文 件 名:  WxLoginHelper.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年5月23日
 */
package com.dimeng.p2p.user.servlets.wechat;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.dimeng.p2p.common.entities.ThirdPartyUser;
import com.dimeng.util.StringHelper;

/**
* <第三方微信登录帮助类>
* @author  xiaoqi
* @version  [版本号, 2016年5月23日]
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
    public static final Map<String, String> getWxTokenAndOpenid(String appid, String secret, String code)
        throws Exception
    {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
        url = url + "?appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";
        Map<String, String> map = new HashMap<String, String>();
        // 获取令牌
        String tokenRes = HttpUtil.httpClientGet(url);
        JSONObject json = JSONObject.fromObject(tokenRes);
        if (StringHelper.isEmpty(json.getString("errcode")))
        {
            map.put("access_token", json.getString("access_token"));
            map.put("refresh_token", json.getString("refresh_token"));
            // 获取微信用户的唯一标识openid
            map.put("openId", json.getString("openid"));
        }
        else
        {
            throw new IllegalArgumentException("THIRDPARTY.LOGIN.NOTOKEN");
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
        if (StringHelper.isEmpty(json.getString("errcode")))
        {
            user.openid = json.getString("openid");
            user.nickName = json.getString("nickname");
            String img = json.getString("headimgurl");
            if (!StringHelper.isEmpty(img))
            {
                user.headImgUrl = img;
            }
            String sex = json.getString("sex");
            //普通用户性别，1为男性，2为女性
            if ("1".equals(sex))
            {
                user.gender = "1";
            }
            else
            {
                user.gender = "2";
            }
        }
        else
        {
            throw new IllegalArgumentException(json.getString("errmsg"));
        }
        return user;
    }
    
    public static void main(String[] args)
    {
        String jsonStr =
            "{\"openid\":\"OPENID\",\"nickname\":\"NICKNAME\",\"sex\":1,"
                + "\"province\":\"PROVINCE\",\"city\":\"CITY\",\"country\":\"COUNTRY\","
                + "\"headimgurl\": \"http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0\","
                + "\"privilege\":[\"PRIVILEGE1\", \"PRIVILEGE2\"],\"unionid\": \"o6_bmasdasdsad6_2sgVt7hMZOPfL\"}";
        JSONObject json = JSONObject.fromObject(jsonStr);
        System.out.println(json.getString("nickname"));
        System.out.println(json.getString("openid"));
        System.out.println(json.getString("unionid"));
        System.out.println(json.getString("headimgurl"));
    }
    
}
