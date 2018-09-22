/*package com.dimeng.p2p.app.servlets.pay.service.shuangqian.ret;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.escrow.shuangqian.entity.ChargeEntity;
import com.dimeng.p2p.escrow.shuangqian.service.ChargeManage;

*//**
 * 
 * 第三方充值页面回调地址
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月12日]
 *//*
public class ChargeRet extends AbstractShuangqianServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @SuppressWarnings("deprecation")
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        ChargeManage sqManage = serviceSession.getService(ChargeManage.class);
        ChargeEntity entity = sqManage.chargeRetDecode(request);
        String enRetUrl = request.getParameter("retUrl");
        
        String url = enRetUrl + "?";
        String content = "";
        if (entity != null)
        {
            if ("88".equals(entity.resultCode))
            {
                url += "code=000000&description=success";
            }
            else if ("03".equals(entity.resultCode))
            {
                content = "该订单在第三方支付平台已取消，无法继续支付，请重新充值！";
                url +=
                    "code=000004&description="
                        + URLEncoder.encode(new String(Base64.encode(content.getBytes("UTF-8")), "UTF-8"));
            }
            else
            {
                url +=
                    "code=000004&description="
                        + URLEncoder.encode(new String(Base64.encode(entity.message.getBytes("UTF-8")), "UTF-8"));
                
            }
        }
        else
        {
            content = "验签失败";
            url +=
                "code=000004&description="
                    + URLEncoder.encode(new String(Base64.encode(content.getBytes("UTF-8")), "UTF-8"));
        }
        response.sendRedirect(url);
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}*/