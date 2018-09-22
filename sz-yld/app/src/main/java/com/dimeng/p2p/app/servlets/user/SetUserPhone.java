package com.dimeng.p2p.app.servlets.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.IndexManage;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.UserBaseInfo;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;

/**
 * 用户信息
 * @author tanhui
 */
public class SetUserPhone extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(request.getServletContext());
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        //手机号码认证
        String phone = getParameter(request, "phone");
        
        safetyManage.updatePhone(phone);
        
        //是否全部认证
        boolean isAllVerify = true;
        IndexManage manage = serviceSession.getService(IndexManage.class);
        
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        UserBaseInfo userBaseInfo = null;
        if (tg)
        {
            userBaseInfo = manage.getUserBaseInfoTx();
        }
        else
        {
            userBaseInfo = manage.getUserBaseInfo();
        }
        if (!userBaseInfo.realName)
        {
            isAllVerify = false;
        }
        if (!userBaseInfo.phone)
        {
            isAllVerify = false;
        }
        if (!userBaseInfo.email)
        {
            isAllVerify = false;
        }
        if (!tg && !userBaseInfo.withdrawPsw)
        {
            isAllVerify = false;
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", isAllVerify);
        return;
    }
}
