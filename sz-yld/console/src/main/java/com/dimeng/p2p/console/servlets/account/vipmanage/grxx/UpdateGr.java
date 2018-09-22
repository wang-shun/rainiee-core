package com.dimeng.p2p.console.servlets.account.vipmanage.grxx;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.common.enums.SysAccountStatus;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.GrManage;
import com.dimeng.p2p.modules.account.console.service.entity.Grxx;
import com.dimeng.p2p.repeater.business.SysBusinessManage;
import com.dimeng.p2p.repeater.business.query.BusinessUserQuery;
import com.dimeng.p2p.variables.defines.BusinessVariavle;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@Right(id = "P2P_C_ACCOUNT_UPDATEGR", name = "修改",moduleId ="P2P_C_ACCOUNT_GRXX",order=2)
public class UpdateGr extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        GrManage manage = serviceSession.getService(GrManage.class);
        int userId = IntegerParser.parse(request.getParameter("userId"));
        Grxx user = manage.getUser(userId);
        if (user == null)
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("user", user);
        
        final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
        if(is_business){
            SysBusinessManage sysBusinessManage = serviceSession.getService(SysBusinessManage.class);
            BusinessUserQuery query = new BusinessUserQuery()
            {

                @Override
                public String getEmployNum() {
                    return null;
                }

                /**
                 * 所属部门
                 *
                 * @return
                 */
                @Override
                public String getDept() {
                    return null;
                }

                @Override
                public SysAccountStatus getStatus()
                {
                    return SysAccountStatus.QY;
                }
                
                @Override
                public int getRoleId()
                {
                    return 0;
                }
                
                @Override
                public String getName()
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
                public String getAccountName()
                {
                    return null;
                }
            };
            request.setAttribute("ywUsers", sysBusinessManage.getEmployUsers(query));
        }
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        T6110 t6110 = new T6110();
        try
        {
            GrManage manage = serviceSession.getService(GrManage.class);
            t6110.F01 = IntegerParser.parse(request.getParameter("userId"));
            t6110.F04 = request.getParameter("msisdn");
            t6110.F05 = request.getParameter("mailbox");
            t6110.F14 = request.getParameter("employNum");
            manage.update(t6110);
            sendRedirect(request, response, getController().getURI(request, GrList.class));
        }
        catch (Exception e)
        {
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            request.setAttribute("T6110", t6110);
            processGet(request, response, serviceSession);
        }
        
    }
    
}
