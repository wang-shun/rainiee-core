package com.dimeng.p2p.pay.servlets.fuyou.ret;

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
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.service.PhoneManage;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.google.gson.Gson;

/**
 * 
 * 手机号修改响应地址
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月5日]
 */
public class PhoneRet extends AbstractFuyouServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("手机号码修改-响应返回——IP:" + request.getRemoteAddr());
        Gson gson = new Gson();
        String jsonData = gson.toJson(reversRequest(request));
        logger.info("手机号码修改-返回数据：" + jsonData);
        if (jsonData.length() < 5)
        {
            createNoticeMessagePage(getConfigureProvider().format(URLVariable.INDEX), "欢迎光临！", response);
            return;
        }
        PhoneManage phoneManage = serviceSession.getService(PhoneManage.class);
        @SuppressWarnings("unchecked")
        Map<String, String> params = gson.fromJson(jsonData, Map.class);
        boolean flag = phoneManage.phoneRetDecode(params);
        if (!flag)
        {
            logger.info("手机号更失败-验签失败");
            getController().prompt(request, response, PromptLevel.ERROR, "验证签名失败！");
            sendRedirect(request, response, getConfigureProvider().format(URLVariable.USER_BASES));
            return;
        }
        if (FuyouRespCode.JYCG.getRespCode().equals(params.get("resp_code")))
        {
           phoneManage.updatePhone(params.get("new_mobile"));
           getController().prompt(request, response, PromptLevel.INFO, "手机号码更换成功！");
           sendRedirect(request, response, getConfigureProvider().format(URLVariable.USER_BASES)+"?userBasesFlag=1");
           return;
        }
        
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable);
        String retUrl = getConfigureProvider().format(URLVariable.USER_BASES);
        if (throwable instanceof ParameterException || throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试!");
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
