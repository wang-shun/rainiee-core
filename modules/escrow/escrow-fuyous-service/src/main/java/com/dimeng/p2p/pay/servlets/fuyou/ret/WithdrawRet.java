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
import com.dimeng.p2p.escrow.fuyou.service.WithDrawManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.google.gson.Gson;

/**
 * 
 * 用户提现返回通知
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月30日]
 */
public class WithdrawRet extends AbstractFuyouServlet
{
    
    /**
     * 用户提现返回结果通知类
     */
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
        String noticeMessage = null;
        if (jsonData.length() < 5)
        {
            noticeMessage = "欢迎光临！";
            createNoticeMessagePage(getConfigureProvider().format(URLVariable.INDEX), noticeMessage, response);
            return;
        }
        WithDrawManage withDrawManage = serviceSession.getService(WithDrawManage.class);
        @SuppressWarnings("unchecked")
        Map<String, String> params = gs.fromJson(jsonData, Map.class);
        // 验签
        boolean flag = withDrawManage.withdrawReturnDecoder(params);
        if (flag)
        {
            // 如果验证签名通过，并且返回成功,则通知前台
            if (FuyouRespCode.JYCG.getRespCode().equals(params.get("resp_code")))
            {
                //                getController().prompt(request, response, PromptLevel.INFO, "提现成功");
                //                sendRedirect(request, response, configureProvider.format(URLVariable.USER_WITHDRAW));
                noticeMessage = "提现成功!";
            }
            else
            {
                //                getController().prompt(request, response, PromptLevel.INFO, BackCodeInfo.info(params.get("resp_code")));
                //                sendRedirect(request, response, configureProvider.format(URLVariable.USER_WITHDRAW));
                //                return;
                noticeMessage = BackCodeInfo.info(params.get("resp_code"));
            }
        }
        else
        {
            this.log("验签失败-返回信息：" + BackCodeInfo.info(params.get("resp_code")));
            //            getController().prompt(request, response, PromptLevel.INFO, "验证签名失败");
            //            sendRedirect(request, response, configureProvider.format(URLVariable.USER_WITHDRAW));
            noticeMessage = "验证签名失败!";
        }
        createNoticeMessagePage(getConfigureProvider().format(URLVariable.USER_WITHDRAW), noticeMessage, response);
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable);
        String retUrl = request.getHeader("Referer");
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
