/*
 * 文 件 名:  GyLoanRecordsList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月24日
 */
package com.dimeng.p2p.app.servlets.bid.publics;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.bid.domain.GyRecordList;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.p2p.repeater.donation.entity.Donation;
import com.dimeng.p2p.repeater.donation.query.DonationQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 公益标投资记录
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月24日]
 */
public class GyLoanRecordsList extends AbstractAppServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 5454828945371946577L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取页面参数信息
        final int pageIndex = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        
        // 获取公益标ID
        final String bidId = getParameter(request, "bidId");
        final int id = IntegerParser.parse(bidId);
        
        // 封装分页信息
        Paging paging = getPaging(pageIndex, pageSize);
        
        // 查询捐助记录
        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
        PagingResult<Donation> tbList = gyLoanManage.searchTbjl(new DonationQuery()
        {
            @Override
            public int getUserId()
            {
                return 0;
            }
            
            @Override
            public Timestamp getCreateTimeStart()
            {
                return null;
            }
            
            @Override
            public Timestamp getCreateTimeEnd()
            {
                return null;
            }
            
            @Override
            public int getBidId()
            {
                return id;
            }
            
        }, paging);
        
        // 封装返回信息
        List<GyRecordList> gyRecordLists = null;
        if (tbList != null)
        {
            // 判断是否超出记录集
            if (tbList.getPageCount() < LongParser.parse(pageIndex))
            {
                gyRecordLists = new ArrayList<GyRecordList>();
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", gyRecordLists);
                return;
            }
            
            Donation[] donations = tbList.getItems();
            
            if (donations != null && donations.length > 0)
            {
                gyRecordLists = new ArrayList<GyRecordList>();
                SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                
                for (Donation donation : donations)
                {
                    GyRecordList rec = new GyRecordList();
                    // 设置捐赠人姓名
                    rec.setUserName(donation.userName);
                    // 设置捐赠金额
                    rec.setLoanAmount(String.valueOf(donation.F04));
                    // 设置捐赠时间
                    rec.setLocanTime(dateSdf.format(donation.F06));
                    
                    gyRecordLists.add(rec);
                }
            }
            
        }
        
        // 返回页面信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", gyRecordLists);
        return;
        
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
