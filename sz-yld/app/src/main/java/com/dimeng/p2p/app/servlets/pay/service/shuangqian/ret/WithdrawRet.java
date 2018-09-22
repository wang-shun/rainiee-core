/*package com.dimeng.p2p.app.servlets.pay.service.shuangqian.ret;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.escrow.shuangqian.entity.WithdrawEntity;
import com.dimeng.p2p.escrow.shuangqian.service.WithdrawManage;

*//**
 * 
 * 第三方提现页面回调接口
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月12日]
 *//*
public class WithdrawRet extends AbstractShuangqianServlet
{
    *//**
     * 注释内容
     *//*
    private static final long serialVersionUID = -3124466632913584446L;
    
    @SuppressWarnings("deprecation")
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        WithdrawManage manage = serviceSession.getService(WithdrawManage.class);
        WithdrawEntity entity = manage.withdrawReturnDecoder(request);
        String enRetUrl = request.getParameter("retUrl");
        
        String url = enRetUrl + "?";
        if ("88".equals(entity.resultCode))
        {
            url += "code=000000&description=success";
        }
        else if ("90".equals(entity.resultCode))
        {
            url +=
                "code=000000&description="
                    + URLEncoder.encode(new String(Base64.encode("提现申请成功，请耐心等待！".getBytes("UTF-8")), "UTF-8"));
        }
        else if ("05".equals(entity.resultCode))//卡信息错误时提示封装
        {
            url +=
                "code=000004&description="
                    + URLEncoder.encode(new String(
                        Base64.encode("很抱歉，提现申请失败！提示：请确认该卡开户归属银行，归属省市等信息是否有误？".getBytes("UTF-8")), "UTF-8"));
        }
        else
        {
            url +=
                "code=000004&description="
                    + URLEncoder.encode(new String(Base64.encode(entity.message.getBytes("UTF-8")), "UTF-8"));
            
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