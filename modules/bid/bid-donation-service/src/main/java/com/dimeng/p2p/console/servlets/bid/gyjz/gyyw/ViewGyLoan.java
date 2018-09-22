/*
 * 文 件 名:  ViewGyLoan.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月10日
 */
package com.dimeng.p2p.console.servlets.bid.gyjz.gyyw;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.AbstractDonationServlet;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.p2p.repeater.donation.entity.GyBidCheck;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月10日]
 */
@Right(id = "P2P_C_VIEW_GYBID",  name = "查看",moduleId="P2P_C_BID_GYJZ_GYYW",order=2)
public class ViewGyLoan extends AbstractDonationServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        //final BidManage bidManage = serviceSession.getService(BidManage.class);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
       
        //公益标业务管理
        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
        int userId = gyLoanManage.getPTID();//IntegerParser.parse(request.getParameter("userId"));
        UserManage userManage = serviceSession.getService(UserManage.class);
        
        T6110_F06 userType = userManage.getUserType(userId);
        request.setAttribute("userType", userType);
        T6242 t6242 = gyLoanManage.get(loanId);
        request.setAttribute("loan", t6242);
        request.setAttribute("userId", userId);
        T6211[] t6211s = gyLoanManage.getBidType();
        //审核记录
        GyBidCheck[] bidChecks = gyLoanManage.getGyBidCheck(loanId);
        request.setAttribute("t6211s", t6211s);
        request.setAttribute("bidChecks", bidChecks);
        super.processGet(request, response, serviceSession);
    }
    
}
