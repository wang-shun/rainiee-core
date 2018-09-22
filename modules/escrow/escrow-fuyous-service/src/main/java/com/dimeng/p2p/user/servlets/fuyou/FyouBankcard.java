package com.dimeng.p2p.user.servlets.fuyou;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.common.enums.BankCardStatus;
import com.dimeng.p2p.escrow.fuyou.entity.BankCardQuery;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 跳
 * 
 * @author  heshiping
 * @version  [版本号, 2016年3月3日]
 */
public class FyouBankcard extends AbstractFuyouServlet{
	private static final long serialVersionUID = 1L;
	
	
	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, final ServiceSession serviceSession)
			throws Throwable {
		
		@SuppressWarnings("unused")
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
		request.setAttribute("close", "close");
		getController().forwardView(request, response, FyouBankcard.class);
	}
}
