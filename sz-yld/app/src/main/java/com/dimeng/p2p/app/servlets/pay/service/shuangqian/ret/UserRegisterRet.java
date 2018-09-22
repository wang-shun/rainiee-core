/*package com.dimeng.p2p.app.servlets.pay.service.shuangqian.ret;

import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.escrow.shuangqian.entity.SqAuthorize;
import com.dimeng.p2p.escrow.shuangqian.entity.UserRegisterEntity;
import com.dimeng.p2p.escrow.shuangqian.service.BidManage;
import com.dimeng.p2p.escrow.shuangqian.service.UserManage;

*//**
 * 
 * 第三方用户注册回调
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月11日]
 *//*
public class UserRegisterRet extends AbstractShuangqianServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @SuppressWarnings("deprecation")
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        UserManage manage = serviceSession.getService(UserManage.class);
        UserRegisterEntity entity = manage.registerReturnDecoder(request);
        BidManage bidManage = serviceSession.getService(BidManage.class);
        final String enRetUrl = request.getParameter("retUrl");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        String location = "";
        if (entity != null && ("88".equals(entity.resultCode) || "16".equals(entity.resultCode)))
        {
            try
            {
                String usrCustId = bidManage.getUserCustId();//当前投标人第三方账号
                
                String returnURL =
                    getSiteDomain("/pay/service/shuangqian/ret/authorizeRet.htm" + "?retUrl=" + Config.authorizeRetUrl);
                SqAuthorize authorize = new SqAuthorize();
                authorize.setMoneymoremoreId(usrCustId);
                authorize.setAuthorizeTypeOpen("123");
                location = bidManage.createAuthorizeFormAPP(authorize, returnURL);
                PrintWriter writer = response.getWriter();
                writer.print(location);
                writer.flush();
                writer.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            String url = enRetUrl + "?";
            
            url +=
                "code=000004&description="
                    + URLEncoder.encode(new String(Base64.encode(entity.message.getBytes("UTF-8")), "UTF-8"));
            
            response.sendRedirect(url);
        }
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}*/