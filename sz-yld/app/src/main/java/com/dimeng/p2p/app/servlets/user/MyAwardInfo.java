/*
 * 文 件 名:  MyAwardInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月3日
 */
package com.dimeng.p2p.app.servlets.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6342_F04;
import com.dimeng.p2p.account.user.service.MyRewardManage;
import com.dimeng.p2p.account.user.service.IndexManage;
import com.dimeng.p2p.account.user.service.entity.UserBaseInfo;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.util.Formater;

/**
 * 我的奖励详情
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月3日]
 */
public class MyAwardInfo extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 6573230086157823054L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取体验金金额
        IndexManage manage = serviceSession.getService(IndexManage.class);
        UserBaseInfo userBaseInfo = manage.getUserBaseInfo();
        final String experAmonut = Formater.formatAmount(userBaseInfo.experienceAmount);
        
        // 获取未使用红包个数
        final int totalHbAmount = getUnUseCount(serviceSession, T6340_F03.redpacket);
        
        // 获取未使用加息券个数
        final int totalJxqAmount = getUnUseCount(serviceSession, T6340_F03.interest);
        
        // 封装详情消息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("experAmonut", experAmonut);
        map.put("totalHbAmount", totalHbAmount);
        map.put("totalJxqAmount", totalJxqAmount);
        
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", map);
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    /**
     * 获取未使用的红包或者加息券个数
     * 
     * @param serviceSession 上下文session
     * @param type 类型
     * @return 未使用个数
     * @throws Throwable 异常信息
     */
    private int getUnUseCount(final ServiceSession serviceSession, final T6340_F03 type) throws Throwable
    {
        MyRewardManage myRewardManage = serviceSession.getService(MyRewardManage.class);
        // 封装查询参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("useStatus", T6342_F04.WSY);
        int unUseCount = myRewardManage.getJxqCount(params);
        
        return unUseCount;
    }
    
}
