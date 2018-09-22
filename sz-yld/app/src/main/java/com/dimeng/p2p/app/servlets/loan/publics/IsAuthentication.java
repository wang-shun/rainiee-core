/*
 * 文 件 名:  IsAuthentication.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月23日
 */
package com.dimeng.p2p.app.servlets.loan.publics;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S51.enums.T5123_F03;
import com.dimeng.p2p.S61.enums.T6120_F05;
import com.dimeng.p2p.account.front.service.UserInfoManage;
import com.dimeng.p2p.account.front.service.entity.UserInfo;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.account.console.service.GrManage;
import com.dimeng.p2p.modules.account.console.service.entity.Rzxx;
import com.dimeng.util.StringHelper;

/**
 * 信用贷-用户是否认证安全信息，年龄是否合法
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年02月29日]
 */
public class IsAuthentication extends AbstractSecureServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -6038142946026843552L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
    	UserInfoManage mge = serviceSession.getService(UserInfoManage.class);
    	
        GrManage personalManage = serviceSession.getService(GrManage.class);
        
        final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        
        // 读取资源文件
        ResourceProvider resourceProvider = getResourceProvider();
        
        // 读取session信息
        final Session session = resourceProvider.getResource(SessionManager.class).getSession(request, response);
        
        final int accountId = session.getAccountId();

        //封装返回给页面信息
        Map<String, Object> isAuthMap = new HashMap<String, Object>();
        //年龄是否在范围内
        boolean ageIsLegal = false;
        //必要认证是否认证
        boolean isAuth = false;
        //是否有逾期借款
        boolean isYuqi = false;
        //是否申请过此产品
        boolean isBid = false;
        //实名认证
        boolean isSmrz = false;
        
        //年龄是否符合
        UserInfo userInfo = mge.search(accountId);
        //申请信用贷最小年龄
        int minAge = 22;
        //申请信用贷最大年龄
        int maxAge = 55;
        if (userInfo.age >= minAge && userInfo.age <= maxAge) 
        {
        	//年龄符合
        	ageIsLegal = true;
        }
        
        //根据用户id得到用户的认证信息以及需要认证的信息。
        Rzxx[] t6120s = personalManage.getRzxx(serviceSession.getSession().getAccountId());
        String rzMsg = "";
        if (t6120s != null && t6120s.length > 0) 
        {
            for (Rzxx rzxx : t6120s) 
            {
                if (T5123_F03.S.name().equals(rzxx.mustRz.name()) && !T6120_F05.TG.equals(rzxx.status)) 
                {
                    rzMsg = rzMsg + rzxx.lxmc + ",";
                }
            }
        }

        //只有自然人需要认证项
        if (StringHelper.isEmpty(rzMsg)) 
        {
            isAuth = true;
        }
        if (!mge.isBid())
        {
            isBid = true;
        }
        if (!mge.isYuqi().equals("Y"))
        {
            isYuqi = true;
        }
        
        if (mge.isSmrz() && mge.getYhrzxx()) 
        {
            isSmrz = true;
        }
        
        isAuthMap.put("ageIsLegal", ageIsLegal);
        isAuthMap.put("isAuth", isAuth);
        isAuthMap.put("isBid", isBid);
        isAuthMap.put("isYuqi", isYuqi);
        isAuthMap.put("isSmrz", isSmrz);
        // 封装页面信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", isAuthMap);
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
