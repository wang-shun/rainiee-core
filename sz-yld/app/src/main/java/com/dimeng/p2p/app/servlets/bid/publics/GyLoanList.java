/*
 * 文 件 名:  GyLoanList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月24日
 */
package com.dimeng.p2p.app.servlets.bid.publics;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.enums.T6242_F11;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.bid.domain.Gyinfo;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.p2p.repeater.donation.entity.GyLoan;
import com.dimeng.p2p.repeater.donation.query.GyLoanQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 公益标列表
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月24日]
 */
public class GyLoanList extends AbstractAppServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -7616016104794974897L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取页面参数信息
        final int pageIndex = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        
        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
        
        // 封装分页信息
        Paging paging = getPaging(pageIndex, pageSize);
        
        // 查询公益标信息
        PagingResult<GyLoan> gyList = gyLoanManage.search4front(new GyLoanQuery()
        {
            @Override
            public T6242_F11 getStatus()
            {
                return null;
            }
            
            @Override
            public String getName()
            {
                return null;
            }
            
            @Override
            public String getLoanTitle()
            {
                return null;
            }
            
            @Override
            public String getGyName()
            {
                return null;
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
            public String getBidNo()
            {
                return null;
            }
        }, paging);
        
        List<Gyinfo> gyLoans = null;
        if (gyList != null)
        {
            // 判断是否超出记录集
            if (gyList.getPageCount() < LongParser.parse(pageIndex))
            {
                gyLoans = new ArrayList<Gyinfo>();
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", gyLoans);
                return;
            }
            
            // 查询公益标列表
            GyLoan[] loanList = gyList.getItems();
            
            if (null != loanList && loanList.length > 0)
            {
                gyLoans = new ArrayList<Gyinfo>();
                for (GyLoan gyLoan : loanList)
                {
                    Gyinfo gyinfo = new Gyinfo();
                    // 公益标ID
                    gyinfo.setBidId(gyLoan.t6242.F01);
                    
                    // 公益标标题
                    gyinfo.setLoanName(gyLoan.t6242.F03);
                    
                    // 公益方
                    gyinfo.setOrganisers(gyLoan.t6242.F22);
                    
                    // 项目金额
                    gyinfo.setLoanAmount(Formater.formatAmount(gyLoan.t6242.F05));
                    
                    // 项目进度(百分比)
                    gyinfo.setProgress(gyLoan.perCent);
                    
                    // 公益标状态
                    gyinfo.setStatus(gyLoan.t6242.F11.name());
                    
                    // 是否已结束
                    gyinfo.setTimeEnd(gyLoan.isTimeEnd);
                    
                    // 状态中文描述
                    gyinfo.setStatusCn(gyLoan.t6242.F11.getChineseName());
                    
                    gyLoans.add(gyinfo);
                }
            }
            
        }
        
        // 返回页面信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", gyLoans);
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
