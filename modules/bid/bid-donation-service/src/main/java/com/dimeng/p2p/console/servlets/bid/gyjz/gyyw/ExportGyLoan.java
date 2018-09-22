/*
 * 文 件 名:  ExportGyLoan.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月12日
 */
package com.dimeng.p2p.console.servlets.bid.gyjz.gyyw;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.enums.T6242_F11;
import com.dimeng.p2p.AbstractDonationServlet;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.p2p.repeater.donation.entity.GyLoan;
import com.dimeng.p2p.repeater.donation.query.GyLoanQuery;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;

/**
 * <导出公益标信息>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月12日]
 */
@Right(id = "P2P_C_BID_GYJZ_EXPORT", name = "导出",moduleId="P2P_C_BID_GYJZ_GYYW",order=7)
public class ExportGyLoan extends AbstractDonationServlet
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
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);

        // T6211[] t6211s = bidManage.getBidType();
         PagingResult<GyLoan> result = gyLoanManage.search(
                 new GyLoanQuery() {

                     @Override
                     public String getName() {
                         return request.getParameter("userName");
                     }

                     @Override
                     public T6242_F11 getStatus() {
                         return EnumParser.parse(T6242_F11.class,
                                 request.getParameter("status"));
                     }

                     @Override
                     public Timestamp getCreateTimeStart() {
                         Date date = DateParser.parse(request
                                 .getParameter("createTimeStart"));
                         return date == null ? null : new Timestamp(date
                                 .getTime());
                     }

                     @Override
                     public Timestamp getCreateTimeEnd() {
                         Date date = DateParser.parse(request
                                 .getParameter("createTimeEnd"));
                         return date == null ? null : new Timestamp(date
                                 .getTime());
                     }

                     @Override
                     public String getLoanTitle()
                     {
                         return request.getParameter("loanTitle");
                     }

                     @Override
                     public String getGyName()
                     {
                         return request.getParameter("gyName");
                     }

                     @Override
                     public String getBidNo()
                     {
                         return request.getParameter("loanNo");
                     }
                 }, new Paging() {

                     @Override
                     public int getSize() {
                         return Integer.MAX_VALUE;
                     }

                     @Override
                     public int getCurrentPage() {
                         return IntegerParser.parse(request
                                 .getParameter(PAGING_CURRENT));
                     }
                 });
         gyLoanManage.exportGyLoan(result.getItems(), response.getOutputStream(), "");
    }
    
}
