package com.dimeng.p2p.app.servlets.loan;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6281;
import com.dimeng.p2p.S62.enums.T6281_F18;
import com.dimeng.p2p.S62.enums.T6281_F19;
import com.dimeng.p2p.S62.enums.T6281_F20;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.front.service.BidWillManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 申请企业贷款接口
 * @author suwei
 * @version  [5.0.1, 2016年01月19日]
 *
 */
public class ApplyQyLoan extends AbstractAppServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 768874493917862622L;

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
		final String qyName = getParameter(request, "qyName");						// 企业名称
		final String registerNum = getParameter(request, "registerNum");			// 注册号
		final String contact = getParameter(request, "contact");					// 联系人
		final String phoneNum = getParameter(request, "phoneNum");					// 手机号码
		final String companyAddr = getParameter(request, "companyAddr");			// 公司地址
		final String loanType = getParameter(request, "loanType");					// 借款类型
		final String fundTerm = getParameter(request, "fundTerm");					// 筹款期限
		final String loanDescription = getParameter(request, "loanDescription");	// 借款描述 

		// 判断参数是否正确
        if (StringHelper.isEmpty(contact) || StringHelper.isEmpty(phoneNum) 
        		|| StringHelper.isEmpty(qyName) || StringHelper.isEmpty(registerNum) 
        		|| StringHelper.isEmpty(getParameter(request, "xian")) || StringHelper.isEmpty(getParameter(request, "loanType")) 
        		|| StringHelper.isEmpty(getParameter(request, "loanAmount")) || StringHelper.isEmpty(getParameter(request, "loanTerm")) 
        		|| StringHelper.isEmpty(loanDescription) || StringHelper.isEmpty(companyAddr))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "参数错误！");
            return;
        }
        
		final int xian = IntegerParser.parse(getParameter(request, "xian"));						// 所在城市
		final BigDecimal loanAmount = BigDecimalParser.parse(getParameter(request, "loanAmount"));	// 借款金额
        final int loanTerm = IntegerParser.parse(getParameter(request, "loanTerm"));				// 借款期限
		
		BidWillManage manage = serviceSession.getService(BidWillManage.class);
		T6281 t6281 = new T6281();
		
		// t6281填值
		t6281.F03 = qyName;
		t6281.F04 = registerNum;
		t6281.F05 = contact;
		t6281.F06 = phoneNum;
		t6281.F08 = companyAddr;
		t6281.F09 = loanAmount;
		
		String [] loanTypes = loanType.split(",");
		if ("S".equals(loanTypes[0])) 
		{
			t6281.F18 = T6281_F18.S;
		}
		if ("S".equals(loanTypes[1])) 
		{
			t6281.F19 = T6281_F19.S;
		}
		if ("S".equals(loanTypes[2])) 
		{
			t6281.F20 = T6281_F20.S;
		}
		
		t6281.F11 = xian;
		t6281.F12 = fundTerm;
		t6281.F13 = loanDescription;
		t6281.F22 = loanTerm;
		
		manage.add(t6281);
		
		setReturnMsg(request, response, ExceptionCode.SUCCESS, "提交成功！我们将尽快和您联系");
        return;
    }
}
