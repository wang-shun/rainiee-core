/*
 * 文 件 名:  GyLoanProgres.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月13日
 */
package com.dimeng.p2p.console.servlets.bid.gyjz.jzgl;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.enums.T6242_F11;
import com.dimeng.p2p.AbstractDonationServlet;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.p2p.repeater.donation.GyLoanProgresManage;
import com.dimeng.p2p.repeater.donation.entity.BidProgres;
import com.dimeng.p2p.repeater.donation.entity.GyLoan;
import com.dimeng.p2p.repeater.donation.query.ProgresQuery;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;

/**
 * <查看公益标的进展列表信息>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月13日]
 */
public class GyLoanProgres extends AbstractDonationServlet
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
        GyLoanProgresManage progresManage = serviceSession.getService(GyLoanProgresManage.class);
        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
       // T6211[] t6211s = bidManage.getBidType();
        PagingResult<BidProgres> loanList = progresManage.search(
                new ProgresQuery() {

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

                    @Override
                    public int getBidId()
                    {
                        return  IntegerParser.parse(request.getParameter("loanId"));
                    }

                    @Override
                    public Timestamp getComTimeStart()
                    {
                        return null;
                    }

                    @Override
                    public Timestamp getComTimeEnd()
                    {
                        return null;
                    }

                    @Override
                    public String getSysName()
                    {
                        return null;
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
        request.setAttribute("loan", gyLoan);
        
        request.setAttribute("loanList", loanList);
        request.setAttribute("loanId", request.getParameter("loanId"));
        request.setAttribute("userId", request.getParameter("userId"));

        forwardView(request, response, getClass());
    }
    
}
