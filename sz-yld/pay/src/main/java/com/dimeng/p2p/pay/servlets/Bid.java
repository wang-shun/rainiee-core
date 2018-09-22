package com.dimeng.p2p.pay.servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.p2p.modules.bid.queue.service.QueueManage;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S65.enums.T6504_F07;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.modules.bid.pay.service.TenderManage;
import com.dimeng.p2p.order.TenderOrderExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 投资
 */
public class Bid extends AbstractPayServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        QueueManage queueManage = serviceSession.getService(QueueManage.class);
        queueManage.subscribeRedisMsg();
        int userId = serviceSession.getSession().getAccountId();
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
        {
            //throw new LogicalException("请不要重复提交请求！");
            queueManage.sendWebSocketInfo(String.valueOf(userId), "请不要重复提交请求！");
            return;
        }


        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        String userReward = request.getParameter("userReward") == null ? "" : request.getParameter("userReward");
        String useType = request.getParameter("useType") == null ? "" : request.getParameter("useType");
        String hbRule = request.getParameter("hbRule") == null ? "" : request.getParameter("hbRule");
        String jxqRule = request.getParameter("jxqRule") == null ? "" : request.getParameter("jxqRule");
        String usedExp = request.getParameter("usedExp") == null ? "" : request.getParameter("usedExp");
        final BigDecimal amount = BigDecimalParser.parse(request.getParameter("amount"));
        final BigDecimal jkje = BigDecimalParser.parse(request.getParameter("jkje"));
        String tranPwd = request.getParameter("tranPwd") == null ? "" : request.getParameter("tranPwd");
        String myRewardType = request.getParameter("myRewardType") == null ? "" : request.getParameter("myRewardType");

        if ("ALL".equals(useType)) {
            userReward = "isUsed";
            myRewardType = "ALL";
        }

        Map<String, Object> parmMap = new HashMap<String, Object>();
        parmMap.put("source", T6504_F07.PC.name());
        parmMap.put("usedExp", usedExp);
        parmMap.put("userReward", userReward);
        parmMap.put("myRewardType", myRewardType);
        parmMap.put("hbRule", hbRule);
        parmMap.put("jxqRule", jxqRule);
        parmMap.put("usedExp", usedExp);
        parmMap.put("loanId", loanId);
        parmMap.put("amount", amount);
        parmMap.put("tranPwd", tranPwd);
        parmMap.put("jkje", jkje);
        parmMap.put("userId", userId);

  /*      try {
            //队列投标
            String retMsg = queueManage.insertBidQueue(parmMap, serviceSession);
            if(retMsg.length()>0){
                queueManage.sendWebSocketInfo(String.valueOf(userId), retMsg);
            }

            sendRedirect(request, response, getURL(loanId));
            return;
        }catch (Exception e){*/
            //正常投标流程
            normalBid(request, response, serviceSession, loanId, userReward, useType, hbRule, jxqRule, usedExp, amount, tranPwd, myRewardType);
     /*   }*/
    }

    private void normalBid(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession, int loanId, String userReward, String useType, String hbRule, String jxqRule, String usedExp, BigDecimal amount, String tranPwd, String myRewardType) throws Throwable {
        TenderManage tenderManage = serviceSession.getService(TenderManage.class);
        final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        if (isOpenWithPsd)
        {
            if (StringHelper.isEmpty(tranPwd))
            {
                throw new LogicalException("交易密码不能为空");
            }
            tranPwd = RSAUtils.decryptStringByJs(tranPwd);
            tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
        }

        if("ALL".equals(useType))
        {
            userReward = "isUsed";
            myRewardType = "ALL";
        }
        Map<String, String> parMap = new HashMap<String, String>();
        parMap.put("source", T6504_F07.PC.name());
        parMap.put("usedExp", usedExp);
        Map<String, String> rtnMap =
            tenderManage.bid(loanId, amount, userReward, tranPwd, myRewardType, hbRule, jxqRule, parMap);
        rtnMap.put("userReward", userReward);
        rtnMap.put("myRewardType", myRewardType);
        rtnMap.put("hbRule", hbRule);
        rtnMap.put("jxqRule", jxqRule);
        rtnMap.put("usedExp", usedExp);
        TenderOrderExecutor executor = getResourceProvider().getResource(TenderOrderExecutor.class);
        //Controller controller = serviceSession.getController();
        //String ip = controller.getRemoteAddr(request);
        //余额投资订单
        executor.submit(IntegerParser.parse(rtnMap.get("orderId")), rtnMap);
        //确认订单
        executor.confirm(IntegerParser.parse(rtnMap.get("orderId")), rtnMap);
        tenderManage.writeFrontLog(FrontLogType.SDTB.getName(), "前台手动投资");
        getController().prompt(request, response, PromptLevel.INFO, "恭喜您，投资成功。");
        sendRedirect(request, response, getURL(loanId));
    }

    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        if (throwable instanceof ParameterException || throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
            sendRedirect(request, response, getURL(loanId));
        }
        else if (throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getURL(loanId));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
    
    protected String getURL(int loanId)
        throws IOException
    {
        ResourceProvider resourceProvider = getResourceProvider();
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        StringBuilder url = new StringBuilder(configureProvider.format(URLVariable.FINANCING_SBTZ_XQ));
        url.append(Integer.toString(loanId)).append(resourceProvider.getSystemDefine().getRewriter().getViewSuffix());
        return url.toString();
    }
    
}
