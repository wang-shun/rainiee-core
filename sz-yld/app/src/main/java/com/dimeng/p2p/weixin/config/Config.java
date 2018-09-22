package com.dimeng.p2p.weixin.config;

import org.apache.commons.lang.StringUtils;

import com.dimeng.p2p.weixin.dimengapi.msg.EventMsg;
import com.google.gson.JsonObject;

public class Config
{
    // JS鉴权时的随机数，可根据项目不同修改配置
    public static final String JSAPI_NONCESTR = "EAFDASFEFADG";
    
    public static String getWeixinAccessTokenUrl(String APPID, String SECRET)
    {
        return "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID + "&secret="
            + SECRET;
    }
    
    public final static String WEIXIN_ACCESS_JSTICKET_URL =
        "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=";
    
    //    public final static String WEIXIN_WAP_INDEX="http://www.kyygo.com/DimengApp/weixin/dev/";
    public enum MENU_ITEM
    {
        BIND(MENU_TYPE.CLICK, "bind", "绑定用户"), 
        UNBIND(MENU_TYPE.CLICK, "unbind", "解除绑定"),
        USERINFO(MENU_TYPE.CLICK, "userInfo", "用户信息"),
        NONE(null, null, null);
        
        private MENU_TYPE type;
        
        private String url;
        
        private String key;
        
        private String displayName;
        
        private MENU_ITEM(MENU_TYPE type, String value, String displayName)
        {
            if (type == MENU_TYPE.VIEW)
            {
                this.url = value;
            }
            else
            {
                this.key = value;
            }
            this.displayName = displayName;
        }
        
        public MENU_ITEM getByKey(String key)
        {
            if (StringUtils.isNotEmpty(key))
            {
                for (MENU_ITEM ins : MENU_ITEM.values())
                {
                    if (key.equals(ins.key))
                    {
                        return ins;
                    }
                }
            }
            return NONE;
        }
        
        public void write(JsonObject json)
        {
            json.addProperty("name", displayName);
            json.addProperty("type", type.toString());
            if (StringUtils.isNotBlank(url))
            {
                json.addProperty("url", url);
            }
            if (StringUtils.isNotBlank(key))
            {
                json.addProperty("key", key);
            }
        }
    }
    
    public enum MENU_TYPE
    {
        CLICK("click"), VIEW("view"), TEXT("text"), GETTOKEN("getToken"), GETJSTICKET("getJsTicket"), LOCATION(
            "location"), IMAGE("image"), VALIDATION("validation"), SUBSCRIBE(EventMsg.SUBSCRIBE), UNSUBSCRIBE(
            EventMsg.UNSUBSCRIBE);
        private String name;
        
        private MENU_TYPE(String name)
        {
            this.name = name;
        }
        
        @Override
        public String toString()
        {
            return name;
        }
        
        public MENU_TYPE getByName(String name)
        {
            if (StringUtils.isNotEmpty(name))
            {
                for (MENU_TYPE ins : MENU_TYPE.values())
                {
                    if (name.equals(ins.name))
                    {
                        return ins;
                    }
                }
            }
            return null;
        }
    }
    
    public static String getResourceUrl(String requestUrl, String contentPath)
    {
        return requestUrl.substring(0, requestUrl.lastIndexOf(contentPath) + contentPath.length()) + "/weixin/";
    }
}
