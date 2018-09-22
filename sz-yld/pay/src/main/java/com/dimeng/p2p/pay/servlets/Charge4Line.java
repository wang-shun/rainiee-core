package com.dimeng.p2p.pay.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.modules.account.recharge.service.OfflineChargeManage;
import com.dimeng.p2p.modules.bid.pay.service.UserInfoManage;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;

/**
 * 用户中心增加:线下充值
 */
public class Charge4Line extends AbstractPayServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		/**
		 * 后端校验是否已经绑定手机、实名认证、交易密码
		 */
        boolean checkFlag=true;
        String checkMessage = "";
        com.dimeng.p2p.service.UserInfoManage userCommManage = serviceSession.getService(com.dimeng.p2p.service.UserInfoManage.class);
        Map<String, String> retMap = userCommManage.checkAccountInfo();
        checkFlag = BooleanParser.parse(retMap.get("checkFlag"));
        checkMessage = retMap.get("checkMessage");

		UserInfoManage manage = serviceSession.getService(UserInfoManage.class);
		T6110 userInfo = manage.getUserInfo(serviceSession.getSession().getAccountId());

		PrintWriter out = response.getWriter();
		if (!checkFlag) {
            out.write("[{'num':'01','msg':'" + checkMessage + "'}]");
            return;
        }
        OfflineChargeManage chargeManage = serviceSession.getService(OfflineChargeManage.class);
        chargeManage.add(userInfo.F02, BigDecimalParser.parse(request.getParameter("amount")),request.getParameter("remarks"));
        out.write("[{'num':'00','msg':'线下充值申请成功！'}]");
	}
	
	@Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        
        ResourceProvider resourceProvider = getResourceProvider();
        resourceProvider.log(throwable);
        PrintWriter out = response.getWriter();
        StringBuilder sb = new StringBuilder();
        if (throwable instanceof AuthenticationException)
        {
            sb.append("[{'num':'101','msg':'未登录或会话超时,请重新登录'}]");
        }else{
            sb.append("[{'num':'01','msg':'");
            sb.append(throwable.getMessage() + "'}]");
        }
        out.write(sb.toString());
    }
}
