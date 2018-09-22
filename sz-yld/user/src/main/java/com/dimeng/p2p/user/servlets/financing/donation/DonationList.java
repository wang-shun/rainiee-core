/*
 * 文 件 名:  DonationList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月13日
 */
package com.dimeng.p2p.user.servlets.financing.donation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.servlet.annotation.PagingServlet;
import com.dimeng.framework.http.session.authentication.OtherLoginException;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.p2p.repeater.donation.entity.Donation;
import com.dimeng.p2p.repeater.donation.entity.GyLoanStatis;
import com.dimeng.p2p.repeater.donation.query.DonationQuery;
import com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * <我的公益标列表>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月13日]
 */
@PagingServlet(itemServlet = DonationList.class)
public class DonationList extends AbstractFinancingServlet
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
        GyLoanManage gyBidManage = serviceSession.getService(GyLoanManage.class);
        PagingResult<Donation> boList = gyBidManage.searchTbjl(new DonationQuery()
        {
            @Override
            public int getBidId()
            {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public Timestamp getCreateTimeStart()
            {
                Date ts = DateParser.parse(request.getParameter("timeStart"), "yyyy-MM-dd");
                return ts == null ? null : new Timestamp(ts.getTime());
                // request.setAttribute("timeStart", ts);
                // return ts;
            }
            
            @Override
            public Timestamp getCreateTimeEnd()
            {
                Date ts = DateParser.parse(request.getParameter("timeEnd"), "yyyy-MM-dd");
                request.setAttribute("timeEnd", ts);
                return ts == null ? null : new Timestamp(ts.getTime());
            }
            
            @Override
            public int getUserId()
            {
                return serviceSession.getSession().getAccountId();
            }
        }, new Paging()
        {
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter("currentPage"));
            }
            
            @Override
            public int getSize()
            {
                return IntegerParser.parse(request.getParameter("pageSize"));
            }
        });
        GyLoanStatis statis = gyBidManage.gyLoanStatisticsByUid(serviceSession.getSession().getAccountId());
        
        //封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        String pageStr = rendPaging(boList);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", boList.getPageCount());
        jsonMap.put("donationList", boList.getItems());
        jsonMap.put("statis", statis);
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
        
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (throwable instanceof OtherLoginException)
        {
            jsonMap.put("msg", throwable.getMessage());
            out.print(objectMapper.writeValueAsString(jsonMap));
        }
        out.close();
    }
}
