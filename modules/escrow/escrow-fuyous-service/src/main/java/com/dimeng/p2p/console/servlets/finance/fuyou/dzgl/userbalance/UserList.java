package com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.userbalance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.AbstractDzglServlet;
import com.dimeng.p2p.escrow.fuyou.entity.console.T6110_FY;
import com.dimeng.p2p.escrow.fuyou.service.UserAcctQueryManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 用户余额
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月3日]
 */
@Right(id = "P2P_C_FUYOU_USER_BALANCE_QUERY", moduleId = "P2P_C_FUYOU_USER_BALANCE", order = 0, name = "用户余额查询")
public class UserList extends AbstractDzglServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        final int pageNum = IntegerParser.parse(request.getParameter(PAGING_CURRENT));
        final String name = request.getParameter("userName");
        String userTag = request.getParameter("userTag");
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
                if (pageNum > 0)
                {
                    return pageNum;
                }
                return 0;
            }
        };
        UserAcctQueryManage manage = serviceSession.getService(UserAcctQueryManage.class);
        PagingResult<T6110_FY> pagingResult = manage.selectUserList(serviceSession, name, paging, userTag);
        request.setAttribute("pagingResult", pagingResult);
        request.setAttribute("userTag", userTag);
        forward(request, response, getController().getViewURI(request, getClass()));
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
