/*package com.dimeng.p2p.app.servlets.pay.service.fuyou.ret;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.pay.service.fuyou.AbstractFuyouServlet;
import com.dimeng.p2p.app.servlets.pay.service.fuyou.service.ChargeManage;
import com.google.gson.Gson;

*//**
 * 
 * 富友，充值返回地址
 * <功能详细描述>
 * 
 * @author  suwei
 * @version  [版本号, 2016年03月02日]
 *//*
public class ChargeRet extends AbstractFuyouServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        response.setContentType("text/html");
        response.setCharacterEncoding(charSet);
        logger.info("APP充值-响应返回——IP:" + request.getRemoteAddr());
        Gson gson = new Gson();
        String jsonData = gson.toJson(reversRequest(request));
        logger.info("APP充值-返回数据：" + jsonData);
        String url = getSiteDomain(Config.chargeRetUrl);
        // 分析从对方传回来的数据
        ChargeManage chargeManage = serviceSession.getService(ChargeManage.class);
        @SuppressWarnings("unchecked")
        Map<String, String> retMap = gson.fromJson(jsonData, Map.class);
        boolean flag = chargeManage.chargeRetDecode(retMap);
        if (!flag)
        {
            url += "?code=000004&description=" + URLEncoder.encode(new String(Base64.encode(retMap.get("resp_desc").getBytes("UTF-8")), "UTF-8"));
        }
        else if (flag && "0000".equals(retMap.get("resp_code")))
        {
        	url += "?code=000000&description=success";
        	logger.info("APP富友托管充值成功！");
        }
        else
        {
        	url += "?code=000004&description=" + URLEncoder.encode(new String(Base64.encode(retMap.get("resp_desc").getBytes("UTF-8")), "UTF-8"));
        	logger.info("APP富友托管充值失败！");
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