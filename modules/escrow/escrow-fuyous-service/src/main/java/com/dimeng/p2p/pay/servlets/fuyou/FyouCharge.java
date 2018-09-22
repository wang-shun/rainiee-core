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
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.escrow.fuyou.service.ChargeManage;
import com.dimeng.p2p.escrow.fuyou.service.UserManage;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;

/**
 * 
 * 向富友托管充值
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月10日]
 */
public class FyouCharge extends AbstractFuyouServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable {
        logger.info("向富友托管充值,开始——IP:" + request.getRemoteAddr());
        // 充值金额
        UserManage userManage = serviceSession.getService(UserManage.class);
        ChargeManage manage = serviceSession.getService(ChargeManage.class);
        // 获取平信个人信息记录
        T6110 userInfo = userManage.selectT6110();
        
        // 充值金额
        final BigDecimal amount = BigDecimalParser.parse(request.getParameter("amount"));
        String payTpye = request.getParameter("payTpye");
        // 充值手续费<单笔计>
        BigDecimal fee = BigDecimal.ZERO;
        
        String formUrl;
        if ("500001".equals(payTpye)) {
        	logger.info("快速充值");
        	formUrl = trimBlank(getConfigureProvider().format(FuyouVariable.FUYOU_500001_URL));
        	
            fee = amount.multiply(new BigDecimal(getConfigureProvider().format(FuyouVariable.FUYOU_CHAREFEE_500001)));
            BigDecimal minFee = new BigDecimal(getConfigureProvider().format(FuyouVariable.FUYOU_CHAREMINFEE_500001));
            if (fee.compareTo(minFee) <= 0) {
                fee = minFee;
            }
            BigDecimal mixFee = new BigDecimal(getConfigureProvider().format(FuyouVariable.FUYOU_CHAREMAXFEE_500001));
            if (fee.compareTo(minFee) <= 0) {
                fee = mixFee;
            }            
        } else if ("500002".equals(payTpye)) {
        	logger.info("网银充值");
        	formUrl = trimBlank(getConfigureProvider().format(FuyouVariable.FUYOU_500002_URL));
        	// 个人网银充值手续费
        	if (userInfo.F06 == T6110_F06.ZRR) {
	        	fee = amount.multiply(new BigDecimal(getConfigureProvider().format(FuyouVariable.FUYOU_PERSONFEE_500002)));
        	} else {// 企业网银充值手续费
        		fee = new BigDecimal(getConfigureProvider().format(FuyouVariable.FUYOU_CORPFEE_500002));
        	}
        } else  if ("500405".equals(payTpye)) {
        	
        	logger.info("快捷充值"); // payTpye =500405, PC端个人快捷充值
        	formUrl = trimBlank(getConfigureProvider().format(FuyouVariable.FUYOU_500405_URL));
        	// 个人快捷充值手续费
	        fee = amount.multiply(new BigDecimal(getConfigureProvider().format(FuyouVariable.FUYOU_PERSONFEE_500405)));
	        BigDecimal minFee = new BigDecimal(getConfigureProvider().format(FuyouVariable.FUYOU_CHAREMINFEE_500405));
            if (fee.compareTo(minFee) <= 0) {
                fee = minFee;
            }
        } else{  
        	 throw new LogicalException("充值类型错误");
        }
        
        // 富友 —— 用户信息查询
        final ChargeManage fyManage = serviceSession.getService(ChargeManage.class);
        // 用于获取第三方托管账户
        final String userId = userManage.selectT6119();
        if (StringHelper.isEmpty(userId)) {
            throw new LogicalException("请先注册第三方");
        }
        // 添加订单，与充值订单
        final String mchnt_txn_ssn = manage.addOrder(amount,"pc", fee,payTpye);
             
        // 充值信息Map
        Map<String, String> map = fyManage.createChargeUrI(userId, getAmt(amount), mchnt_txn_ssn);
        
        // 向第三方发送请求
        sendHttp(map, formUrl, response, true);
        logger.info("充值信息发送第三方");
        manage.writeFrontLog(FrontLogType.CZ.getName(), "前台用户充值:" + amount);
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable) throws ServletException, IOException {
        getResourceProvider().log(throwable);
        String retUrl = request.getHeader("Referer");
        if (throwable instanceof ParameterException || throwable instanceof SQLException) {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试!");
            sendRedirect(request, response, retUrl);
        } else if (throwable instanceof LogicalException) {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, retUrl);
        } else if (throwable instanceof AuthenticationException) {
            sendRedirect(request, response, retUrl);
        } else {
            super.onThrowable(request, response, throwable);
        }
    }
    
}
