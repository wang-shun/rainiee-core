package com.dimeng.p2p.console.servlets.account.vipmanage.jgxx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.QyManage;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_ACCOUNT_VIEWJGXX", name = "查看", moduleId = "P2P_C_ACCOUNT_JGXX", order = 1)
public class ViewJgxx extends AbstractAccountServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        QyManage manage = serviceSession.getService(QyManage.class);
        int id = IntegerParser.parse(request.getParameter("id"));
        T6161 entity = manage.getQyxx(id);
        request.setAttribute("info", entity);
        UserManage userManager = serviceSession.getService(UserManage.class);
        T6110_F17 t6110_f17 = userManager.getUserInvestorType(id);
        request.setAttribute("t6110_f17", t6110_f17);
        
        UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = userInfoManage.getUserInfo(id);
        request.setAttribute("t6110_f19", t6110.F19);
        forwardView(request, response, getClass());
    }
    
}
