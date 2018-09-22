/*
 * 文 件 名:  AddProgres.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月10日
 */
package com.dimeng.p2p.console.servlets.bid.gyjz.jzgl;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.S62.entities.T6245;
import com.dimeng.p2p.AbstractDonationServlet;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.p2p.repeater.donation.GyLoanProgresManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <添加公益标进展>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月10日]
 */
@Right(id = "P2P_C_BID_VIEW_PROGRES",  name = "进展管理[按钮]",moduleId="P2P_C_BID_GYJZ_JZGL",order=2)
public class AddProgres extends AbstractDonationServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        super.processGet(request, response, serviceSession);
      //  processPost(request, response, serviceSession);
        //forwardView(request, response, getClass());
    }

    @Override
    protected void processPost(final HttpServletRequest request,
            final HttpServletResponse response,
            final ServiceSession serviceSession) throws Throwable {
        GyLoanProgresManage bidManage = serviceSession.getService(GyLoanProgresManage.class);
       GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
       int loanId = IntegerParser.parse(request.getParameter("loanId"));
       int userId = IntegerParser.parse(request.getParameter("userId"));
       try {
           T6242 t6242 = gyLoanManage.get(loanId);
           if(null == t6242)
           {
             getController().prompt(request,response,PromptLevel.WARRING,"公益标信息不存在,请重新查看");
             // 跳转到信息页面
             sendRedirect(request, response,
                 getController().getURI(request, AddProgres.class)+ "?loanId=" + loanId + "&userId=" + userId);
                return;
           }
           T6245 t6245 = new T6245();
           t6245.parse(request);
           t6245.F02 = serviceSession.getSession().getAccountId();
           t6245.F03 = loanId;
           if(StringHelper.isEmpty(t6245.F04) || StringHelper.isEmpty(t6245.F06) || null ==t6245.F08)
           {
                getController().prompt(request, response, PromptLevel.WARRING, "主题标题,标题时间或介绍不能为空");
               // 跳转到信息页面
               forwardView(request, response, getClass());
                return;
           }
            //去掉时分秒，再比较。
            Date date = new Date(t6242.F13.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String sDate = sdf.format(date);
            if (t6245.F08.compareTo(sdf.parse(sDate)) < 0)
            {
                getController().prompt(request, response, PromptLevel.WARRING, "标题时间不能小于公益标发布时间！");
                // 跳转到信息页面
                forwardView(request, response, getClass());
                return;
            }
           bidManage.add(t6245);
           // 跳转到倡议书信息页面
           sendRedirect(request, response,
                   getController().getURI(request, ViewProgres.class)+ "?loanId=" + loanId + "&userId=" + userId);
       } catch (Throwable throwable) {
           logger.error(throwable, throwable);
           if (throwable instanceof ParameterException
                   || throwable instanceof LogicalException) {
               getController().prompt(request, response, PromptLevel.WARRING,
                       throwable.getMessage());
               processGet(request, response, serviceSession);
           } else {
               super.onThrowable(request, response, throwable);
           }
       }
    }
    
}
