/*
 * 文 件 名:  Deletebankcard.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zengzhihua
 * 修改时间:  2015年12月19日
 */
package com.dimeng.p2p.console.servlets.finance.ptyhkgl;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.account.console.service.BankCardManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
 * <删除平台银行卡>
 * <功能详细描述>
 * 
 * @author  zengzhihua
 * @version  [版本号, 2015年12月23日]
 */
@Right(id = "P2P_C_FINANCE_DELETEBANKCARD", name = "删除平台银行卡")
public class Deletebankcard extends AbstractFinanceServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, final ServiceSession serviceSession)
			throws Throwable {
		BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
		String id = request.getParameter("value");
		if(StringHelper.isEmpty(id)){
			return;
		}
		bankCardManage.delete(IntegerParser.parse(id), T6114_F08.TY.name());
	}
}
