/*package com.dimeng.p2p.app.servlets.pay.service.fuyou.ret;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.pay.service.fuyou.AbstractFuyouServlet;
import com.dimeng.p2p.app.servlets.pay.service.fuyou.service.WithDrawManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.google.gson.Gson;

*//**
 * 
 * 用户提现返回通知
 * <功能详细描述>
 * 
 * @author  suwei
 * @version  [版本号, 2016年03月02日]
 *//*
public class WithdrawRet extends AbstractFuyouServlet
{
    
    *//**
     * 用户提现返回结果通知类
     *//*
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("用户提现返回通知——IP:" + request.getRemoteAddr());
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        Gson gs = new Gson();
        String jsonData = gs.toJson(reversRequest(request));
        logger.info("用户提现返回通知数据：" + jsonData);
        String url = getSiteDomain(Config.withdrawRetUrl);
        WithDrawManage withDrawManage = serviceSession.getService(WithDrawManage.class);
        @SuppressWarnings("unchecked")
        Map<String, String> params = gs.fromJson(jsonData, Map.class);
        // 验签
        boolean flag = withDrawManage.withdrawReturnDecoder(params);
        if (flag)
        {
            // 如果验证签名通过，并且返回成功,则通知前台
            if ("0000".equals(params.get("resp_code")))
            {
            	url += "?code=000000&description=success";
            }
            else
            {
                url += "?code=000000&description=" + BackCodeInfo.info(params.get("resp_code"));
            }
        }
        else
        {
            this.log("验签失败-返回信息：" + BackCodeInfo.info(params.get("resp_code")));
            url += "?code=000004&description=验证签名失败";
        }
        // 重定向到用户中心
        sendRedirect(request, response, url);
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable);
        String retUrl = request.getHeader("Referer");
        if (throwable instanceof ParameterException || throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
            sendRedirect(request, response, retUrl);
        }
        else if (throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, retUrl);
        }
        else if (throwable instanceof AuthenticationException)
        {
            sendRedirect(request, response, retUrl);
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
}
*/