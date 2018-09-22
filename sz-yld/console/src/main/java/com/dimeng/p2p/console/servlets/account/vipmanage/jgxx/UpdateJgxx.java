package com.dimeng.p2p.console.servlets.account.vipmanage.jgxx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.dbzz.UpdateDbzz;
import com.dimeng.p2p.modules.account.console.service.QyManage;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.modules.nciic.service.INciicManageService;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.nciic.NciicVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_ACCOUNT_UPDATEJGXX", name = "修改", moduleId = "P2P_C_ACCOUNT_JGXX", order = 2)
public class UpdateJgxx extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        QyManage manager = serviceSession.getService(QyManage.class);
        int userId = IntegerParser.parse(request.getParameter("id"));
        T6161 t6161 = manager.getQyxx(userId);
        if (t6161 == null)
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("t6161", t6161);
        UserManage userManager = serviceSession.getService(UserManage.class);
        UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
        T6110_F17 t6110_f17 = userManager.getUserInvestorType(userId);
        T6110 t6110 = userInfoManage.getUserInfo(userId);
        request.setAttribute("t6110_f17", t6110_f17);
        request.setAttribute("t6110_f19", t6110.F19);
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        try
        {
            QyManage manage = serviceSession.getService(QyManage.class);
            int id = IntegerParser.parse(request.getParameter("id"));
            T6161 entity = new T6161();
            entity.parse(request);
            
            ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
            String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
            //易宝特殊处理，判断
            if (null != ESCROW_PREFIX && !ESCROW_PREFIX.isEmpty() && "yeepay".equalsIgnoreCase(ESCROW_PREFIX))
            {
                if (StringHelper.isEmpty(entity.F21))
                {
                    throw new ParameterException("开户银行许可证不能为空");
                }
            }
            
            if (!StringHelper.isEmpty(entity.F11) && !StringHelper.isEmpty(entity.F12))
            {
                INciicManageService nic = serviceSession.getService(INciicManageService.class);
                
                boolean enable = BooleanParser.parse(configureProvider.getProperty(NciicVariable.ENABLE));
                boolean is;
                //不启用实名认证
                if (!enable)
                {
                    is = true;
                }
                else
                {
                    is = nic.check(entity.F12, entity.F11, "PC", 0);
                }
                if (is)
                {
                    entity.F01 = id;
                    T6110_F17 t6110_f17 = EnumParser.parse(T6110_F17.class, request.getParameter("isInvestor"));
                    if (t6110_f17 == null)
                    {
                        t6110_f17 = T6110_F17.F;
                    }
                    T6110_F19 t6110_f19 = EnumParser.parse(T6110_F19.class, request.getParameter("isBuyBadClaim"));
                    if (t6110_f19 == null)
                    {
                        t6110_f19 = T6110_F19.F;
                    }
                    manage.updateQyxx(entity, t6110_f17, t6110_f19);
                    if ("submit".equals(request.getParameter("entryType")))
                    {
                        sendRedirect(request, response, getController().getURI(request, JgList.class));
                        return;
                    }
                    sendRedirect(request, response, getController().getURI(request, UpdateDbzz.class) + "?id=" + id);
                }
                else
                {
                    throw new ParameterException("实名认证失败");
                }
            }
            
        }
        catch (Exception e)
        {
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            processGet(request, response, serviceSession);
        }
    }
    
}
