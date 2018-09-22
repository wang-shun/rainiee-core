/*
 * 文 件 名:  ViewListDfjl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年1月8日
 */
package com.dimeng.p2p.console.servlets.account.vipmanage.grxx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.GrManage;
import com.dimeng.p2p.modules.account.console.service.entity.BasicInfo;
import com.dimeng.p2p.modules.account.console.service.entity.Dfxxmx;
import com.dimeng.p2p.modules.account.console.service.query.DfxxmxQuery;
import com.dimeng.util.parser.IntegerParser;

/**
 * <垫付记录>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年1月8日]
 */
@Right(id = "P2P_C_ACCOUNT_GRVIEW", name = "查看", moduleId = "P2P_C_ACCOUNT_GRXX", order = 1)
public class ViewListDfjl extends AbstractAccountServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        DfxxmxQuery dfxxmxQuery = new DfxxmxQuery()
        {
            
            @Override
            public Integer getUserId()
            {
                return IntegerParser.parse(request.getParameter("userId"));
            }
            
            @Override
            public String getJkbh()
            {
                return request.getParameter("jkbh");
            }
            
            @Override
            public String getJkbt()
            {
                return request.getParameter("jkbt");
            }
            
            @Override
            public String getZt()
            {
                return request.getParameter("zt");
            }
        };
        GrManage manage = serviceSession.getService(GrManage.class);
        PagingResult<Dfxxmx> result = manage.seachDfjl(dfxxmxQuery, new Paging()
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
        });
        int userId = IntegerParser.parse(request.getParameter("userId"));
        BasicInfo basicInfo = manage.findBasicInfo(userId);
        if (basicInfo == null)
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("basicInfo", basicInfo);
        request.setAttribute("result", result);
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
