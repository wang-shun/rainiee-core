package com.dimeng.p2p.console.servlets.account.vipmanage.zhgl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.ZhglManage;
import com.dimeng.p2p.modules.nciic.service.INciicManageService;
import com.dimeng.p2p.service.PtAccountManage;
import com.dimeng.p2p.variables.defines.nciic.NciicVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.EnumParser;

@Right(id = "P2P_C_ACCOUNT_ADDJG", name = "新增机构账号", moduleId = "P2P_C_ACCOUNT_ZHGL", order = 3)
public class AddJg extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        ZhglManage manage = serviceSession.getService(ZhglManage.class);
        T6161 entity = new T6161();
        entity.parse(request);
        if (!StringHelper.isEmpty(entity.F11) && !StringHelper.isEmpty(entity.F12))
        {
            INciicManageService nic = serviceSession.getService(INciicManageService.class);
            ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
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
                //注册前判断平台账号是否存在，否，则增加平台账号
                PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
                ptAccountManage.addPtAccount();
                
                String dbjgms = request.getParameter("dbjgms");
                String realName = request.getParameter("realName");
                String lxTel = request.getParameter("lxTel");
                String mobile = request.getParameter("mobile");
                String email = request.getParameter("email");
                String temp = entity.F12.toUpperCase();
                entity.F12 = temp.substring(0, 2) + "***************";
                entity.F13 = StringHelper.encode(temp);
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
                manage.addJg(entity,
                    dbjgms,
                    request.getParameter("jgjs"),
                    realName,
                    lxTel,
                    mobile,
                    email,
                    t6110_f17,
                    t6110_f19);
                getController().prompt(request,
                    response,
                    PromptLevel.INFO,
                    "<span class=\"f18\">机构账号添加成功，初始登录密码为：888888</span>");
                processGet(request, response, serviceSession);
            }
            else
            {
                throw new ParameterException("实名认证失败");
            }
        }
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
        forwardView(request, response, getClass());
    }
    
}
