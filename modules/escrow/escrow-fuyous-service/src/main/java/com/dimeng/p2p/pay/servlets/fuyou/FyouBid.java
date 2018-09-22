package com.dimeng.p2p.pay.servlets.fuyou;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.S65.enums.T6504_F07;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.escrow.fuyou.executor.FYTenderOrderExecutor;
import com.dimeng.p2p.modules.bid.pay.service.TenderManage;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 手动投资
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月13日]
 */
public class FyouBid extends AbstractFuyouServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName()))) {
            throw new LogicalException("请不要重复提交请求！");
        }
        logger.info("手动投标——IP:" + request.getRemoteAddr());
        
        int userId = serviceSession.getSession().getAccountId();
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
        
        if ("ALL".equals(useType))
        {
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
        normalBid(request,
            response,
            serviceSession,
            loanId,
            userReward,
            useType,
            hbRule,
            jxqRule,
            usedExp,
            amount,
            tranPwd,
            myRewardType);
        /* // BidManage 继承 TenderManage - 另重写 其方法
         BidManage tenderManage = serviceSession.getService(BidManage.class);
         // 标ID
         int loanId = IntegerParser.parse(request.getParameter("loanId"));
         // 体验金
         String userReward = request.getParameter("userReward");
         if (!StringHelper.isEmpty(userReward)) {
             //用户体验金-查看用户是否有体验金
             T6103 t6103 = tenderManage.getT6103(loanId,0);
             //用户活动——查询用户是否已使用体验金（一标只能使用一次）
             T6342 t6342 = tenderManage.getT6342(loanId,0);
             if (null != t6103 || null != t6342) {
                 throw new LogicalException("只能使用一次我的奖励投此标!");
             }
         }
         // 投资金额
         final BigDecimal amount = BigDecimalParser.parse(request.getParameter("amount"));
         // 交易密码
         String tranPwd = request.getParameter("tranPwd");
         if (!StringHelper.isEmpty(tranPwd))
         {
             tranPwd = RSAUtils.decryptStringByJs(tranPwd);
             tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
         }
         // 我的体验金
         String myRewardType = request.getParameter("myRewardType");
         // 红包
         String hbRule = request.getParameter("hbRule");
         // 加息卷
         String jxqRule = request.getParameter("jxqRule");
         Map<String, String> parMap = new HashMap<String, String>();
         parMap.put("source", T6504_F07.PC.name());
         // 生成相应订单<保存在map内>
         Map<String, String> rtnMap = tenderManage.bid(loanId, amount, userReward, tranPwd, myRewardType, hbRule, jxqRule, parMap);
         rtnMap.put("userReward", userReward);
         rtnMap.put("myRewardType", myRewardType);
         // 红包
         rtnMap.put("hbRule", hbRule);
         // 加息卷
         rtnMap.put("jxqRule", jxqRule);
         
         // 业务订单执行
         FYTenderOrderExecutor executor = getResourceProvider().getResource(FYTenderOrderExecutor.class);
         //余额投标订单
         int orderId = IntegerParser.parse(rtnMap.get("orderId"));
         // 提交订单
         executor.submit(orderId, rtnMap);
         //确认订单
         executor.confirm(orderId, rtnMap);
         tenderManage.writeFrontLog(FrontLogType.SDTB.getName(), " 手动投资-标Id:" + loanId + "-金额：" + amount);
         logger.info("投标成功—富友预授权成功-订单：" + orderId + " -合同号：" + rtnMap.get("contract_no"));
         getController().prompt(request, response, PromptLevel.INFO, "恭喜您，投资成功!");
         sendRedirect(request, response, getURL(loanId));*/
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable) throws ServletException, IOException {
        getResourceProvider().log(throwable);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        if (throwable instanceof ParameterException || throwable instanceof SQLException) {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试!");
            sendRedirect(request, response, getURL(loanId));
        } else if (throwable instanceof LogicalException) {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getURL(loanId));
        } else {
            super.onThrowable(request, response, throwable);
        }
    }
    
    protected String getURL(int loanId) throws IOException {
        ResourceProvider resourceProvider = getResourceProvider();
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        StringBuilder url = new StringBuilder(configureProvider.format(URLVariable.FINANCING_SBTZ_XQ));
        url.append(Integer.toString(loanId)).append(resourceProvider.getSystemDefine().getRewriter().getViewSuffix());
        return url.toString();
    }
    
    private void normalBid(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession,
        int loanId, String userReward, String useType, String hbRule, String jxqRule, String usedExp,
        BigDecimal amount, String tranPwd, String myRewardType)
        throws Throwable
    {
        TenderManage tenderManage = serviceSession.getService(TenderManage.class);
        final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        if (isOpenWithPsd)
        {
            if (StringHelper.isEmpty(tranPwd))
            {
                throw new LogicalException("交易密码不能为空");
            }
            tranPwd = RSAUtils.decryptStringByJs(tranPwd);
            tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
        }
        
        if ("ALL".equals(useType))
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
        
        // 业务订单执行
        FYTenderOrderExecutor executor = getResourceProvider().getResource(FYTenderOrderExecutor.class);
        //余额投标订单
        int orderId = IntegerParser.parse(rtnMap.get("orderId"));
        // 提交订单
        executor.submit(orderId, rtnMap);
        //确认订单
        executor.confirm(orderId, rtnMap);
        tenderManage.writeFrontLog(FrontLogType.SDTB.getName(), " 手动投资-标Id:" + loanId + "-金额：" + amount);
        logger.info("投标成功—富友预授权成功-订单：" + orderId + " -合同号：" + rtnMap.get("contract_no"));
        getController().prompt(request, response, PromptLevel.INFO, "恭喜您，投资成功!");
        sendRedirect(request, response, getURL(loanId));
        
    }

}
