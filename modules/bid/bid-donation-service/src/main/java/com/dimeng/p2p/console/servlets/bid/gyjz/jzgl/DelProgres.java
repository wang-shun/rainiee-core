/*
 * 文 件 名:  DelProgres.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 */
package com.dimeng.p2p.console.servlets.bid.gyjz.jzgl;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6245;
import com.dimeng.p2p.AbstractDonationServlet;
import com.dimeng.p2p.repeater.donation.GyLoanProgresManage;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <删除动态>
 * <功能详细描述>
 * 
 */
@Right(id = "P2P_C_BID_VIEW_PROGRES",  name = "进展管理[按钮]",moduleId="P2P_C_BID_GYJZ_JZGL",order=2)
public class DelProgres extends AbstractDonationServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        processPost(request, response, serviceSession);
    }

    @Override
    protected void processPost(final HttpServletRequest request,
            final HttpServletResponse response,
            final ServiceSession serviceSession) throws Throwable {
       
        GyLoanProgresManage bidManage = serviceSession.getService(GyLoanProgresManage.class);
       //GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
       int loanId = IntegerParser.parse(request.getParameter("loanId"));
       int userId = IntegerParser.parse(request.getParameter("userId"));
       int pId = IntegerParser.parse(request.getParameter("pId"));
       try{
       T6245 t6245 = bidManage.get(pId);
       if(null != t6245)
       {
           bidManage.delete(pId, loanId);
       }
           
//       getController().prompt(
//           request,
//           response,
//           PromptLevel.WARRING,
//           "公益标信息不存在,请重新查看");
       // 跳转到倡议书信息页面
       sendRedirect(request, response,
               getController().getURI(request, ViewProgres.class)+ "?loanId=" + loanId + "&userId=" + userId);
       }
       catch (Throwable throwable) {
           logger.error(throwable, throwable);
           if (throwable instanceof ParameterException
                   || throwable instanceof LogicalException) {
               getController().prompt(request, response, PromptLevel.WARRING,
                       throwable.getMessage());
               sendRedirect(request, response,
                   getController().getURI(request, ViewProgres.class)+ "?loanId=" + loanId + "&userId=" + userId);
           } else {
               super.onThrowable(request, response, throwable);
           }
       }
    }
    
}
