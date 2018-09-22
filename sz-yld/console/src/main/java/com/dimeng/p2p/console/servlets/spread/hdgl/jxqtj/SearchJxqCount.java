/*
 * 文 件 名:  SearchJxqtj.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月8日
 */
package com.dimeng.p2p.console.servlets.spread.hdgl.jxqtj;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.activity.console.service.ActivityCountManage;
import com.dimeng.p2p.modules.activity.console.service.entity.ActivityCountEntity;
import com.dimeng.p2p.modules.activity.console.service.entity.JxqCountTotalInfo;
import com.dimeng.p2p.modules.activity.console.service.query.ActivityCountQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 功能描述：后台 加息券统计
 * 详情描述： 1. 根据查询条件分页展示加息券统计结果列表
 *        2. 加息券数量统计：未使用加息券数量统计，已使用加息券数量统计
 * @author  xiaoqi
 * @version  [v3.1.2, 2015年10月8日]
 */
@Right(id = "P2P_C_SPREAD_HDGL_JXQTJ", isMenu = true, name = "加息券统计", moduleId = "P2P_C_SPREAD_HDGL_JXQTJ", order = 0)
public class SearchJxqCount extends AbstractSpreadServlet
{
    private static final long serialVersionUID = -4043812664245222625L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
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
        ActivityCountQuery query = new ActivityCountQuery()
        {
            
            @Override
            public String userName()
            {
                return request.getParameter("userName");
            }
            
            @Override
            public Timestamp useDateEnd()
            {
                return TimestampParser.parse(request.getParameter("useDateEnd"));
            }
            
            @Override
            public Timestamp useDateBegin()
            {
                return TimestampParser.parse(request.getParameter("useDateBegin"));
            }
            
            @Override
            public String status()
            {
                return request.getParameter("status");
            }
            
            @Override
            public Timestamp presentDateEnd()
            {
                return TimestampParser.parse(request.getParameter("presentDateEnd"));
            }
            
            @Override
            public Timestamp presentDateBegin()
            {
                return TimestampParser.parse(request.getParameter("presentDateBegin"));
            }
            
            @Override
            public Timestamp outOfDateEnd()
            {
                return TimestampParser.parse(request.getParameter("outOfDateEnd"));
            }
            
            @Override
            public Timestamp outOfDateBegin()
            {
                return TimestampParser.parse(request.getParameter("outOfDateBegin"));
            }
            
            @Override
            public String loanNum()
            {
                return request.getParameter("loanNum");
            }
            
            @Override
            public String activityNum()
            {
                return request.getParameter("activityNum");
            }
        };
        PagingResult<ActivityCountEntity> result = manage.searchActivityCount(query, paging, T6340_F03.interest.name());
        JxqCountTotalInfo info = manage.getJxqCountTotalInfo();
        request.setAttribute("result", result);
        request.setAttribute("info", info);
        forwardView(request, response, getClass());
    }
    
}
