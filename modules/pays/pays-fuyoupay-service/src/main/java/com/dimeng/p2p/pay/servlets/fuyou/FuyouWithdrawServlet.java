package com.dimeng.p2p.pay.servlets.fuyou;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.order.WithdrawExecutor;
import com.dimeng.p2p.pay.service.fuyou.WithdrawService;
import com.dimeng.p2p.pay.service.fuyou.entity.Payforrsp;
import com.dimeng.p2p.pay.service.fuyou.util.Bean2XmlUtils;
import com.dimeng.p2p.pay.service.fuyou.util.HttpClientHandler;
import com.dimeng.p2p.pay.service.fuyou.varibles.FuyouPayVaribles;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

public class FuyouWithdrawServlet extends AbstractFuyouServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName()))) {
            throw new LogicalException("请不要重复提交请求！");
        }
		WithdrawService service = serviceSession.getService(WithdrawService.class);
        T6110 t6110 = service.selectT6110(serviceSession.getSession().getAccountId());
        if (T6110_F07.HMD == t6110.F07) {
            throw new LogicalException("账户已被拉黑，不能进行提现！");
        }
        String withdrawPsd = request.getParameter("withdrawPsd");
        if (StringHelper.isEmpty(request.getParameter("amount"))) {
            throw new LogicalException("提现金额不能为空");
        }
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        if (StringHelper.isEmpty(withdrawPsd) && isOpenWithPsd) {
            throw new LogicalException("交易密码不能为空");
        }
        int cardId = IntegerParser.parse(request.getParameter("cardId"));
        BigDecimal amount = new BigDecimal(request.getParameter("amount"));
        if (isOpenWithPsd) {
            withdrawPsd = RSAUtils.decryptStringByJs(withdrawPsd);
        }        
        
        boolean txkcfs = Boolean.parseBoolean(getConfigureProvider().getProperty(SystemVariable.TXSXF_KCFS));
        BigDecimal poundage = null;
        String pundageWay = getConfigureProvider().getProperty(SystemVariable.WITHDRAW_POUNDAGE_WAY);
        if ("BL".equals(pundageWay)) {
            // 按比例计算
            String _proportion = getConfigureProvider().getProperty(SystemVariable.WITHDRAW_POUNDAGE_PROPORTION);
            if (StringHelper.isEmpty(_proportion) || _proportion.contains("-"))
            {
                throw new LogicalException("系统繁忙，请稍后再试！");
            }
            BigDecimal proportion = new BigDecimal(_proportion);
            poundage = amount.multiply(proportion).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            // 按额度计算[1-50000), [50000, ]
            if (amount.compareTo(new BigDecimal(50000)) < 0) {
                poundage = new BigDecimal(getConfigureProvider().getProperty(SystemVariable.WITHDRAW_POUNDAGE_1_5));
            } else {
                poundage = new BigDecimal(getConfigureProvider().getProperty(SystemVariable.WITHDRAW_POUNDAGE_5_20));
            }
        }
        
        int orderId = service.withdraw(withdrawPsd, amount, cardId, poundage, txkcfs);
//        BigDecimal money = new BigDecimal(getConfigureProvider().format(SystemVariable.WITHDRAW_LIMIT_FUNDS));//BigDecimal.ZERO;
        
        //提现金额小于提现审核所限制金额或所限金额为0就自动提现，不需人工审核
//        service.writeFrontLog(FrontLogType.TX.getName(), "前台提现");
				
		WithdrawExecutor chargeOrderExecutor = getResourceProvider().getResource(WithdrawExecutor.class);
		chargeOrderExecutor.submit(orderId, null);
		        
		BigDecimal money = new BigDecimal(getConfigureProvider().format(SystemVariable.WITHDRAW_LIMIT_FUNDS));
		if (amount.doubleValue() > money.doubleValue() || money.doubleValue() <= 0) {
			service.freezeUserAccount(orderId);
            getController().prompt(request, response, PromptLevel.INFO, "您的提现申请已提交，预计T+1工作日到账，具体以实际到账日期为准！");
        } else {		
        	List<NameValuePair> requestParam = service.getRequestParams(orderId);        
	        String actionUrl = this.getConfigureProvider().format(FuyouPayVaribles.DAIFU_URL);
	        
	        
	        // 发送请求拿到同步返回结果字符串
	        String retString = HttpClientHandler.doPost2(actionUrl, requestParam);
	        logger.info("代付返回信息：" + retString);
	        
	        Payforrsp payforrsp = Bean2XmlUtils.xml2bean(retString, Payforrsp.class);        
	        
	        // 返回结果
	        if ("000000".equals(payforrsp.getRet())) {
	        	service.freezeUserAccount(orderId);
	        	chargeOrderExecutor.confirm(orderId, null);
	            logger.info("代付成功返回，返回码：" + payforrsp.getRet() + "备注：" + payforrsp.getMemo());
	            getController().prompt(request, response, PromptLevel.INFO, "提现成功！");
	        } else {
	        	service.updateT6501(orderId, T6501_F03.SB);
	            logger.info("代付失败，返回码：" + payforrsp.getRet() + "备注：" + payforrsp.getMemo());
	            getController().prompt(request, response, PromptLevel.INFO, "提现失败，原因：" + payforrsp.getMemo());
	        }
        }
        this.sendRedirect(request, response, StringHelper.format(URLVariable.USER_WITHDRAW.getValue(), this.getConfigureProvider()));
	}

	@Override
	protected void onThrowable(HttpServletRequest request,
			HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		logger.error("",throwable);
		if (throwable instanceof AuthenticationException || throwable instanceof LogicalException || throwable instanceof ParameterException) {
			getController().prompt(request, response, PromptLevel.WARRING, "提现失败，原因：" + throwable.getMessage());
			this.sendRedirect(request, response, StringHelper.format(URLVariable.USER_WITHDRAW.getValue(), this.getConfigureProvider()));
		} else {
			getController().prompt(request, response, PromptLevel.WARRING, "提现失败，原因：" + throwable.getLocalizedMessage());
			this.sendRedirect(request, response, StringHelper.format(URLVariable.USER_WITHDRAW.getValue(), this.getConfigureProvider()));
		}
	}
	
}
