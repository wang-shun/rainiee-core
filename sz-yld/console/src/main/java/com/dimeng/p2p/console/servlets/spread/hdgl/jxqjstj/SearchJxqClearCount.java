/*
 * 文 件 名:  SearchJxqClearCount.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月9日
 */
package com.dimeng.p2p.console.servlets.spread.hdgl.jxqjstj;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.activity.console.service.ActivityCountManage;
import com.dimeng.p2p.modules.activity.console.service.entity.JxqClearAmountTotalInfo;
import com.dimeng.p2p.modules.activity.console.service.entity.JxqClearCountEntity;
import com.dimeng.p2p.modules.activity.console.service.query.JxqClearCountQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 功能描述：加息券结算统计
 * 详情描述： 1. 加息券结算金额统计[待付加息奖励统计，已付加息奖励统计]
 *        2. 根据条件查询加息券结算统计列表
 *        3. 统计查询的当前页面加息券奖励金额
 * @author  xiaoqi
 * @version  [v3.1.2, 2015年10月9日]
 */
@Right(id = "P2P_C_SPREAD_HDGL_JXQJSTJ", isMenu = true, name = "加息券结算统计", moduleId = "P2P_C_SPREAD_HDGL_JXQJSTJ", order = 0)
public class SearchJxqClearCount extends AbstractSpreadServlet
{
    
    private static final long serialVersionUID = -119694570477678712L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        ActivityCountManage manage = serviceSession.getService(ActivityCountManage.class);
        Paging paging = new Paging()
        {
            @Override
            public int getSize()
            {
                return 10;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        };
        JxqClearCountQuery query = new JxqClearCountQuery()
        {
            @Override
            public String userName()
            {
                return request.getParameter("userName");
            }
            
            @Override
            public String loanNum()
            {
                return request.getParameter("loanNum");
            }
            
            @Override
            public Timestamp startTime()
            {
                return TimestampParser.parse(request.getParameter("fkStartTime"));
            }
            
            @Override
            public Timestamp endTime()
            {
                return TimestampParser.parse(request.getParameter("fkEndTime"));
            }
            
            @Override
            public String status()
            {
                if (request.getParameter("status") == null)
                {
                    return "DF";
                }
                return request.getParameter("status");
            }
        };
        
        String status = request.getParameter("status");
        if (status == null)
        {
            status = "DF";
        }
        PagingResult<JxqClearCountEntity> result = manage.searchJxqClearCount(query, paging);
        JxqClearAmountTotalInfo info = manage.getJxqClearAmountTotalInfo();
        if (info != null)
        {
            info.totalAmount = manage.getConditionOfJxqClearAmount(query);
        }
        request.setAttribute("result", result);
        request.setAttribute("info", info);
        request.setAttribute("status", status);
        request.setAttribute("totalAmount", manage.getConditionOfJxqClearAmount(query));
        forwardView(request, response, getClass());
    }
    
}
