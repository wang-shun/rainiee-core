/*
 * 文 件 名:  MySpread.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月26日
 */
package com.dimeng.p2p.app.servlets.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.account.user.service.SpreadManage;
import com.dimeng.p2p.account.user.service.entity.SpreadEntity;
import com.dimeng.p2p.account.user.service.entity.SpreadTotle;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.MySpreadInfo;
import com.dimeng.p2p.app.servlets.user.domain.SpreadInfo;
import com.dimeng.p2p.variables.defines.SystemVariable;

/**
 * 推广信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月26日]
 */
public class MySpread extends AbstractSecureServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5315802261058917683L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        MySpreadInfo info = new MySpreadInfo();
        // 获取推广信息接口服务类对象
        SpreadManage spreadManage = serviceSession.getService(SpreadManage.class);
        
        // 获取我的邀请码(推广码)
        String tgm = spreadManage.getMyyqNo();
        info.setTgm(tgm);
        
        // 获取推广信息
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        String msg = configureProvider.getProperty(SystemVariable.APPTGWB);
        msg = msg.replaceAll("xxx", tgm);
        String wzmc = configureProvider.getProperty(SystemVariable.SITE_NAME);
        String wzdz = configureProvider.getProperty(SystemVariable.SITE_DOMAIN);
        msg = msg.replaceAll("SITE_NAME", wzmc);
        msg = msg.replaceAll("SITE_DOMAIN", wzdz);
        info.setMsg(msg);
        
        // 好友最低充值下限
        final String czjs = configureProvider.getProperty(SystemVariable.TG_YXCZJS);
        info.setCzjs(czjs);
        
        // 每次邀请成功奖励
        final String tghl = configureProvider.getProperty(SystemVariable.TG_YXTGJL);
        info.setTghl(tghl);
        
        // 每月推荐上限
        final String tgsx = configureProvider.getProperty(SystemVariable.TG_YXTGSX);
        info.setTgsx(tgsx);
        
        // 好友投资下限
        final String tzcs = configureProvider.getProperty(SystemVariable.TG_TZJS);
        info.setTzcs(tzcs);
        
        // 投资奖励
        final String tzjl = configureProvider.getProperty(SystemVariable.TG_TZJL);
        info.setTzjl(tzjl);
        
        // 推广统计
        SpreadTotle spreadTotle = spreadManage.search();
        info.setSpreadTotle(spreadTotle);
        
        // 获取推荐列表
        PagingResult<SpreadEntity> pgList = spreadManage.getAllReward(getPaging(0, 10));
        
        if (pgList != null)
        {
            SpreadEntity[] entitys = pgList.getItems();
            List<SpreadInfo> spreadInfos = null;
            
            if (null != entitys && entitys.length > 0)
            {
                spreadInfos = new ArrayList<SpreadInfo>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (SpreadEntity entity : entitys)
                {
                    SpreadInfo spreadInfo = new SpreadInfo();
                    spreadInfo.setUserName(entity.userName);
                    spreadInfo.setRegisterTime(sdf.format(entity.zcTime));
                    
                    spreadInfos.add(spreadInfo);
                }
            }
            info.setSpreadEntitys(spreadInfos);
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", info);
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
