/*
 * 文 件 名:  EditBankCard.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年12月19日
 */
package com.dimeng.p2p.console.servlets.finance.ptyhkgl;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.enums.BankCardStatus;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.account.console.service.BankCardManage;
import com.dimeng.p2p.modules.account.console.service.query.BankCardQuery;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <编辑银行卡信息>
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年12月19日]
 */
@Right(id = "P2P_C_FINANCE_EDITBANKCARD", name = "编辑平台银行卡")
public class EditBankCard extends AbstractFinanceServlet {

	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = -671861279249924435L;
	@Override
	protected void processPost(final HttpServletRequest request,
							   HttpServletResponse response, final ServiceSession serviceSession)
			throws Throwable {
		BankCardManage  bankCardManage = serviceSession.getService(BankCardManage.class);

		BankCardQuery query = new BankCardQuery() {
			@Override
			public String getSubbranch() {
				return request.getParameter("subbranch");
			}

			@Override
			public String getStatus() {
				return BankCardStatus.QY.name();
			}

			@Override
			public String getCity() {
				return request.getParameter("xian");
			}

			@Override
			public String getBankNumber() {
				return null;
			}

			@Override
			public int getBankId() {
				return IntegerParser.parse(request.getParameter("bankname"));
			}

			@Override
			public int getAcount() {
				return serviceSession.getSession().getAccountId();
			}

			@Override
			public String getUserName()
			{
				return request.getParameter("userName");
			}

			@Override
			public int getType()
			{
				return Integer.parseInt(request.getParameter("type"));
			}
		};
		//启用银行卡
		int id = IntegerParser.parse(request.getParameter("id"));
		bankCardManage.update(id, query);
		request.setAttribute("close", "close");
		getController().forwardView(request, response, getClass());
	}
	
}
