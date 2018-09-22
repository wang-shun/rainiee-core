package com.dimeng.p2p.pay.servlets.fuyou;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.escrow.fuyou.service.WithDrawManage;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 用户提现
 * <富友托管>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月30日]
 */
public class FyouWithdraw extends AbstractFuyouServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("用户提现-开始——IP:" + request.getRemoteAddr());
        // 银行卡ID
        int cardId = IntegerParser.parse(request.getParameter("cardId"));
        // 提现的金额
        final String amtStr = request.getParameter("amount");
        if (StringHelper.isEmpty(amtStr) || cardId <= 0)
        {
            throw new LogicalException("请输入正确的金额!");
        }
        
        // 提现金额
        BigDecimal amount = new BigDecimal(amtStr);
        WithDrawManage withdrawService = serviceSession.getService(WithDrawManage.class);
        // 查询用户的第三方托管账号
        final String login_id = withdrawService.selectT6119();
        if (login_id == null || StringHelper.isEmpty(login_id))
        {
            throw new LogicalException("请先注册第三方托管账号!");
        }
        // 条件过滤及插入订单,并获取订单号
        final String mchnt_txn_ssn = withdrawService.addOrderId(amount, cardId, T6101_F03.WLZH);
        // 构建提现信息
        Map<String, String> map = withdrawService.createWithdrawUrI(login_id, amount, mchnt_txn_ssn);
        // 提现提交地址
        String formUrl = getConfigureProvider().format(FuyouVariable.FUYOU_500003_URL);
        // 向第三方发送请求
        sendHttp(map, formUrl, response, true);
        withdrawService.writeFrontLog(FrontLogType.TX.getName(), "前台提现:" + amount);
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
            getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
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
