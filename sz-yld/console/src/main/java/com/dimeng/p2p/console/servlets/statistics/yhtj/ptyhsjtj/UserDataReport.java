/*
 * 文 件 名:  UserDataReport
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/6/7
 */
package com.dimeng.p2p.console.servlets.statistics.yhtj.ptyhsjtj;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S70.entities.T7052;
import com.dimeng.p2p.console.servlets.statistics.AbstractStatisticsServlet;
import com.dimeng.p2p.modules.statistics.console.service.UserDataManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 平台用户数统计
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/6/7]
 */
@Right(id = "P2P_C_STATISTICS_USERDATA", name = "平台用户数据统计", moduleId = "P2P_C_STATISTICS_YHTJ_YHSJTJ")
public class UserDataReport extends AbstractStatisticsServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        request.setAttribute("startDate", DateParser.format(calendar.getTime()));
        request.setAttribute("endDate", DateParser.format(new Date()));
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        UserDataManage manage = serviceSession.getService(UserDataManage.class);
        request.setAttribute("userData", manage.getUserData());
        Date startDate =
            TimestampParser.parse(StringHelper.isEmpty(request.getParameter("startDate")) ? String.valueOf(request.getAttribute("startDate"))
                : request.getParameter("startDate"));
        Date endDate =
            TimestampParser.parse(StringHelper.isEmpty(request.getParameter("endDate")) ? String.valueOf(request.getAttribute("endDate"))
                : request.getParameter("endDate"));
        PagingResult<T7052> pagingResult = manage.searchUsersOpData(new Paging()
        {
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
            
            @Override
            public int getSize()
            {
                return 10;
            }
        }, startDate, endDate);
        request.setAttribute("pagingResult", pagingResult);
        T7052 totalUserData = manage.searchUsersTotalData(startDate, endDate);
        request.setAttribute("totalUserData", totalUserData);
        forwardView(request, response, getClass());
    }
}
