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
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.service.ChargeManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.google.gson.Gson;

/**
 * 
 * 富友，充值返回地址
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月20日]
 */
public class ChargeRet extends AbstractFuyouServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        response.setContentType("text/html");
        response.setCharacterEncoding(charSet);
        logger.info("充值-响应返回——IP:" + request.getRemoteAddr());
        Gson gson = new Gson();
        String jsonData = gson.toJson(reversRequest(request));
        logger.info("充值-返回数据：" + jsonData);
        String noticeMessage = null;
        if (jsonData.length() < 5)
        {
            noticeMessage = "欢迎光临！";
            createNoticeMessagePage(getConfigureProvider().format(URLVariable.INDEX), noticeMessage, response);
            return;
        }
        // 分析从对方传回来的数据
        ChargeManage chargeManage = serviceSession.getService(ChargeManage.class);
        @SuppressWarnings("unchecked")
        Map<String, String> retMap = gson.fromJson(jsonData, Map.class);
        //根据流水号查充值类型
        T6502 t6502 = chargeManage.selectT6502BySsn(retMap.get("mchnt_txn_ssn"));
        boolean flag = chargeManage.chargeRetDecode(retMap,t6502);
        if (!flag)
        {
            noticeMessage = BackCodeInfo.info(retMap.get("resp_code")) + "-验签失败!";
        }
        if (flag && FuyouRespCode.JYCG.getRespCode().equals(retMap.get("resp_code")))
        {
            noticeMessage = "充值成功!";
        }
        else
        {
            noticeMessage = BackCodeInfo.info(retMap.get("resp_code"))+ "-签名失败!";
        }
        getController().prompt(request, response, PromptLevel.INFO, noticeMessage);
        sendRedirect(request, response, getConfigureProvider().format(URLVariable.USER_TRADINGRECORD));
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
