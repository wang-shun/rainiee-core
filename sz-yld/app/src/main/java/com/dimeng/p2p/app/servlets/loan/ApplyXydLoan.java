package com.dimeng.p2p.app.servlets.loan;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.account.front.service.UserInfoManage;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.base.front.service.CreditLevelManage;
import com.dimeng.p2p.modules.bid.front.service.BusinessManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.DateHelper;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 申请信用贷款接口
 * @author suwei
 * @version  [5.0.1, 2016年01月19日]
 *
 */
public class ApplyXydLoan extends AbstractAppServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3579858639252725728L;

	@Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
	
	@Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
		BusinessManage bManage = serviceSession.getService(BusinessManage.class);
        UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
        
        final ConfigureProvider configureProvider =
	            ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        
		final String loanTitle = getParameter(request, "loanTitle");  				// 借款标题
		final String loanWay = getParameter(request, "loanWay");					// 借款用途
		final String repaySource = getParameter(request, "repaySource");			// 还款来源
		final String loanDescription = getParameter(request, "loanDescription");	// 借款描述
		
		// 判断参数是否正确
        if (StringHelper.isEmpty(loanTitle) //|| StringHelper.isEmpty(loanWay) || StringHelper.isEmpty(getParameter(request, "loanType")) 
        		|| StringHelper.isEmpty(getParameter(request, "xian")) || StringHelper.isEmpty(getParameter(request, "repayWay"))
        		|| StringHelper.isEmpty(getParameter(request, "loanAmount")) 
        		|| StringHelper.isEmpty(getParameter(request, "loanTerm")) || StringHelper.isEmpty(loanDescription) 
        		|| StringHelper.isEmpty(repaySource) || StringHelper.isEmpty(getParameter(request, "rate")))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "参数错误！");
            return;
        }
        
        final int loanTerm = IntegerParser.parse(getParameter(request, "loanTerm"));				// 借款期限
        //final int loanType = IntegerParser.parse(getParameter(request, "loanType"));				// 借款标类型
        final BigDecimal loanAmount = BigDecimalParser.parse(getParameter(request, "loanAmount"));	// 借款金额			
        final int fundTerm = IntegerParser.parse(getParameter(request, "fundTerm"));				// 筹款期限
        final T6230_F17 interestWay = T6230_F17.parse(getParameter(request, "interestWay"));		// 付息方式
        final int interestDay = IntegerParser.parse(getParameter(request, "interestDay"));          //付息日
        final int valueDate = IntegerParser.parse(getParameter(request, "valueDate"));              //起息日
        final int xian = IntegerParser.parse(getParameter(request, "xian"));						// 所在城市
        final T6230_F10 repayWay = T6230_F10.parse(getParameter(request, "repayWay"));				// 还款方式
        final BigDecimal rate = BigDecimalParser.parse(getParameter(request, "rate"));				// 年化利率
        
        // 判断用户是否有逾期未还的借款
		String isYuqi = uManage.isYuqi();
        if (isYuqi.equals("Y"))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.YU_QI_EXCEPTION, "您有逾期未还的借款，请先还完借款再操作！");
            return;
        }
        
        // 判断用户是否正在申请、待审核、待发布此产品
        if (uManage.isBid()) 
        {
        	setReturnMsg(request, response, ExceptionCode.ALREADY_LOANED_ERROR, "您已申请过其他产品，不能再申请此产品");
        	return;
		}
        
        T6230 t6230 = new T6230();
        T6231 t6231 = new T6231();
        
        UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
        CreditLevelManage creditLevelManage = serviceSession.getService(CreditLevelManage.class);
        
        // 根据信用积分得到信用等级ID
        int xyId = creditLevelManage.getId(userInfoManage.getXyjf(serviceSession.getSession().getAccountId()));
        
        // t6230填值
        t6230.F03 = loanTitle;
        //t6230.F04 = loanType;
        t6230.F05 = loanAmount;
        t6230.F06 = rate;
        t6230.F08 = fundTerm;
        t6230.F09 = loanTerm;
        t6230.F10 = repayWay;
        
        //T6230_F17 fxfs = EnumParser.parse(T6230_F17.class, configureProvider.getProperty(SystemVariable.FXFS)); // 付息方式，采取后台配置的方式定义
        t6230.F17 = interestWay;
        t6230.F18 = interestDay;
        t6230.F19 = valueDate;
        
        t6230.F23 = xyId;
        
        // t6231填值
        t6231.F07 = xian;
        t6231.F08 = loanWay;
        t6231.F09 = loanDescription;
        t6231.F16 = repaySource;
        
        // 本息到期一次付清
        String accDay = getParameter(request, "accDay");	// 如果还款方式选择本息到期一次付清，则有此参数  S：按天/F：按月
        if (T6230_F10.YCFQ == repayWay && "S".equals(accDay))
        {
        	Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            long curDateInMills = cal.getTimeInMillis();
            cal.add(Calendar.MONTH, 36);
            long upDateInMills = cal.getTimeInMillis();
            int upDays = (int)((upDateInMills - curDateInMills) / DateHelper.DAY_IN_MILLISECONDS);
            int lowDays = IntegerParser.parse(configureProvider.getProperty(SystemVariable.JK_BXDQ_LEAST_DAYS), 1);
            
            if (t6230.F09 < lowDays || t6230.F09 > upDays)
            {
            	setReturnMsg(request, response, ExceptionCode.LOAN_OUT_DATE, "本息到期，一次付清(按天计)，借款期限超出有效天数[" + lowDays + "," + upDays + "]");
            	return;
            }
            t6231.F21 = T6231_F21.S;
            t6231.F22 = t6230.F09;
            t6230.F09 = 0;
        }
        else
        {
        	if (t6230.F09 < 1 || t6230.F09 > 36)
            {
                setReturnMsg(request, response, ExceptionCode.LOAN_OUT_MONTH, "借款期限超出有效月数[1-36]");
                return;
            }
            t6231.F21 = T6231_F21.F;
            t6231.F22 = 0;
        }
        
        bManage.add(t6230, t6231, null);
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "借款申请提交成功！");
        return;
    }
}
