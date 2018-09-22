/*
 * 文 件 名:  GyBidZf.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月10日
 */
package com.dimeng.p2p.console.servlets.bid.gyjz.gyyw;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.S62.enums.T6242_F11;
import com.dimeng.p2p.AbstractDonationServlet;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <公益标的作废>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月10日]
 */
@Right(id = "P2P_C_BID_GYJZ_GYJZZF", name = "作废",moduleId="P2P_C_BID_GYJZ_GYYW",order=6)
public class GyBidZf extends AbstractDonationServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        
        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
        
        T6242 t6242 = gyLoanManage.get(loanId);
        if (t6242 == null)
        {
            throw new ParameterException("标信息不存在,请重新查看");
        }
        //捐助中和已捐赠的标不能作废
        if (t6242.F11 == T6242_F11.JKZ || t6242.F11 == T6242_F11.YJZ)
        {
            throw new ParameterException("公益标状态是已捐助或捐款中状态,不能作废该公益标");
        }
        
        gyLoanManage.CheckGyBid(loanId, T6242_F11.YZF, T6242_F11.DSH);
        sendRedirect(request, response, getController().getURI(request, GyLoanList.class));
        
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getController().getURI(request, GyLoanList.class));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
    
}
