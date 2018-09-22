/*
 * 文 件 名:  CheckGyBid.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月12日
 */
package com.dimeng.p2p.console.servlets.bid.gyjz.gyyw;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.S62.entities.T6247;
import com.dimeng.p2p.S62.enums.T6242_F11;
import com.dimeng.p2p.S62.enums.T6247_F05;
import com.dimeng.p2p.AbstractDonationServlet;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <公益标审核通过>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月12日]
 */
@Right(id = "P2P_C_CHECK_GYBID", name = "审核",moduleId="P2P_C_BID_GYJZ_GYYW",order=4)
public class CheckGyBid extends AbstractDonationServlet
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
        
        if (t6242.F11 != T6242_F11.DSH)
        {
            throw new ParameterException("标状态不是待审核状态,不能审核该公益标");
        }
        gyLoanManage.CheckGyBid(loanId, T6242_F11.DFB, T6242_F11.DSH);
        //添加审核记
        T6247 t6247 = new T6247();
        t6247.F02 = loanId;
        t6247.F03 = serviceSession.getSession().getAccountId();
        t6247.F05 = T6247_F05.YCL;
        //t6247.F06 = "公益标:\""+t6242.F03+"\",审核通过";
        t6247.F06 = "审核通过";
        gyLoanManage.addT6247(t6247);
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
