/*package com.dimeng.p2p.app.servlets.pay.service.shuangqian;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.escrow.shuangqian.entity.SqAuthorize;
import com.dimeng.p2p.escrow.shuangqian.service.BidManage;

*//**
 * 
 * 双乾托管二次授权
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月11日]
 *//*
public class Authorize extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        BidManage bidManage = serviceSession.getService(BidManage.class);
        String userId = request.getParameter("userId");
        String location = "";
        String usrCust = "";
        if (userId != null)
        {
            usrCust = bidManage.getUserCustId(Integer.parseInt(userId));
        }
        else
        {
            usrCust = bidManage.getUserCustId();//当前投标人第三方账号
        }
        if (usrCust == null)
        {
            throw new LogicalException("您尚未开通第三方账户，请前往用户中心充值注册！");
        }
        String returnURL =
            getSiteDomain("/pay/service/shuangqian/ret/authorizeRet.htm" + "?retUrl=" + Config.authorizeRetUrl);
        SqAuthorize authorize = new SqAuthorize();
        authorize.setMoneymoremoreId(usrCust);
        authorize.setAuthorizeTypeOpen("123");
        location = bidManage.createAuthorizeFormAPP(authorize, returnURL);
        PrintWriter writer = response.getWriter();
        writer.print(location);
        writer.flush();
        writer.close();
    }
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
    
}*/