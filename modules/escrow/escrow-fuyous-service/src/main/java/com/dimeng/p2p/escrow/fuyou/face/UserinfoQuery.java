package com.dimeng.p2p.escrow.fuyou.face;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryEntity;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.service.UserManage;
import com.dimeng.p2p.escrow.fuyou.util.HttpClientHandler;
import com.dimeng.p2p.escrow.fuyou.util.XmlHelper;
import com.google.gson.Gson;

/** 
 * 描述:用户信息查询
 * 作者:wangshaohua 
 * 创建时间：2015年5月4日
 */
public class UserinfoQuery
{
    
    private static Log logger = LogFactory.getLog(UserinfoQuery.class);
    
    /** 
     * 描述:用户信息查询，并返回结果
     * 作者:wangshaohua 
     * 创建时间：2015年5月4日
     * @param entity
     * @param serverSession
     * @return
     */
    public UserQueryResponseEntity userinfoQuery(UserQueryEntity entity, ServiceSession serverSession, String formUrl)
        throws Throwable
    {
        UserManage userManage = serverSession.getService(UserManage.class);
        //构建查询参数实体
        userManage.queryUserInfo(entity);
        // 请求富友地址，并获取返回结果
        Gson gs = new Gson();
        @SuppressWarnings("unchecked")
        String xmlpost = HttpClientHandler.doPost(gs.fromJson(gs.toJson(entity), Map.class), formUrl);
        logger.info("xmlpost == " + xmlpost);
        // 获取plain值
        String plain = XmlHelper.getPlain(xmlpost);
        //将返回的xml参数进行解析
        HashMap<String, Object> xmlMap = (HashMap<String, Object>)XmlHelper.xmlToMap(xmlpost);
        // 把解析的结果封装成实体对象
        UserQueryResponseEntity userQueryResponseEntity = userManage.userQueryReturnDecoder(xmlMap, plain);
        return userQueryResponseEntity;
    }
}
