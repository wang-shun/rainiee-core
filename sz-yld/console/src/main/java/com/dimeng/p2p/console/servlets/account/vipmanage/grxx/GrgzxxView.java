/*
 * 文 件 名:  GrgzxxView.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2015年11月10日
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
import com.dimeng.p2p.modules.account.console.service.entity.T6143EX;
import com.dimeng.util.parser.IntegerParser;

/**
 * <个人工作信息>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年11月10日]
 */
@Right(id = "P2P_C_ACCOUNT_GRVIEW", name ="查看", moduleId = "P2P_C_ACCOUNT_GRXX",order=1)
public class GrgzxxView extends AbstractAccountServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2171539447761371253L;
    
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
        
        GrManage manage = serviceSession.getService(GrManage.class);
        int userId = IntegerParser.parse(request.getParameter("userId"));
        BasicInfo basicInfo = manage.findBasicInfo(userId);
        if (basicInfo == null)
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
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
        PagingResult<T6143EX> resultGzxx = manage.seachGzxx(paging, userId);
        request.setAttribute("basicInfo", basicInfo);
        request.setAttribute("resultGzxx", resultGzxx);
        forwardView(request, response, getClass());
    }
    
}
