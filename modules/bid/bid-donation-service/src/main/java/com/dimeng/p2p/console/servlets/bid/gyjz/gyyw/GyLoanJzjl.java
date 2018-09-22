/*
 * 文 件 名:  GyLoanJzjl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月13日
 */
package com.dimeng.p2p.console.servlets.bid.gyjz.gyyw;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.AbstractDonationServlet;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.p2p.repeater.donation.entity.Donation;
import com.dimeng.p2p.repeater.donation.entity.GyLoan;
import com.dimeng.p2p.repeater.donation.entity.GyLoanStatis;
import com.dimeng.p2p.repeater.donation.query.DonationQuery;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;

/**
 * <查看公益标的捐款记录列表信息>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月13日]
 */
public class GyLoanJzjl extends AbstractDonationServlet
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
        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);

       // T6211[] t6211s = bidManage.getBidType();
        PagingResult<Donation> loanList = gyLoanManage.searchTbjl(
                new DonationQuery() {

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
                    public int getBidId()
                    {
                        return  IntegerParser.parse(request.getParameter("loanId"));
                    }

                    @Override
                    public int getUserId()
                    {
                        return 0;
                    }
                }, new Paging() {

                    @Override
                    public int getSize() {
                        return 10;
                    }

                    @Override
                    public int getCurrentPage() {
                        return IntegerParser.parse(request
                                .getParameter(PAGING_CURRENT));
                    }
                });
        GyLoan gyLoan =gyLoanManage.getInfo(IntegerParser.parse(request.getParameter("loanId"))) ;
        
        //统计
        GyLoanStatis statis = gyLoanManage.gyLoanStatistics(IntegerParser.parse(request.getParameter("loanId")));
        
        request.setAttribute("statis", statis);
        request.setAttribute("loan", gyLoan);
        request.setAttribute("loanList", loanList);
        request.setAttribute("loanId", request.getParameter("loanId"));
        request.setAttribute("userId", request.getParameter("userId"));
        forwardView(request, response, getClass());
    }
    
}
