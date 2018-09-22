package com.dimeng.p2p.app.servlets.loan;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6282;
import com.dimeng.p2p.S62.enums.T6282_F15;
import com.dimeng.p2p.S62.enums.T6282_F16;
import com.dimeng.p2p.S62.enums.T6282_F17;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.tool.EmojiUtil;
import com.dimeng.p2p.modules.bid.front.service.BidWillManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 申请个人贷款接口
 * @author suwei
 * @version  [5.0.1, 2016年01月19日]
 *
 */
public class ApplyGrLoan extends AbstractAppServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5688988073227892828L;

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
		final String contact = getParameter(request, "contact");					// 联系人
		final String phoneNum = getParameter(request, "phoneNum");					// 手机号码
		final String loanType = getParameter(request, "loanType");					// 借款类型
		final String fundTerm = getParameter(request, "fundTerm");					// 预期筹款期限
		final String loanDescription = getParameter(request, "loanDescription");	// 借款描述 
		
		// 判断参数是否正确
        if (StringHelper.isEmpty(contact) || StringHelper.isEmpty(phoneNum) 
        		|| StringHelper.isEmpty(getParameter(request, "xian")) || StringHelper.isEmpty(getParameter(request, "loanType")) 
        		|| StringHelper.isEmpty(getParameter(request, "loanAmount")) || StringHelper.isEmpty(getParameter(request, "loanTerm")) 
        		|| StringHelper.isEmpty(loanDescription))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "参数错误！");
            return;
        }
        
        if (EmojiUtil.getInstance().isContainEmoji(contact) || EmojiUtil.getInstance().isContainEmoji(loanDescription))
        {
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "请不要输入表情符号！");
            return;
        }
        
        final int xian = IntegerParser.parse(getParameter(request, "xian"));						// 所在城市
        final BigDecimal loanAmount = BigDecimalParser.parse(getParameter(request, "loanAmount"));	// 借款金额
        final int loanTerm = IntegerParser.parse(getParameter(request, "loanTerm"));				// 借款期限
        
        BidWillManage manage = serviceSession.getService(BidWillManage.class);
		T6282 t6282 = new T6282();
		
		// t6282填值
		t6282.F03 = contact;
		t6282.F04 = phoneNum;
		t6282.F06 = loanAmount;
		t6282.F08 = xian;
		t6282.F09 = fundTerm;
		t6282.F10 = loanDescription;
		t6282.F19 = loanTerm;
		
		String [] loanTypes = loanType.split(",");
		if ("S".equals(loanTypes[0])) 
		{
			t6282.F15 = T6282_F15.S;
		}
		if ("S".equals(loanTypes[1])) 
		{
			t6282.F16 = T6282_F16.S;
		}
		if ("S".equals(loanTypes[2])) 
		{
			t6282.F17 = T6282_F17.S;
		}
		
		manage.add(t6282);
		
		setReturnMsg(request, response, ExceptionCode.SUCCESS, "提交成功！我们将尽快和您联系");
        return;
    }
}
