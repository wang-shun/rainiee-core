package com.dimeng.p2p.console.servlets.account.vipmanage.zhgl;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.common.enums.SysAccountStatus;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.ZhglManage;
import com.dimeng.p2p.repeater.business.SysBusinessManage;
import com.dimeng.p2p.repeater.business.query.BusinessUserQuery;
import com.dimeng.p2p.service.PtAccountManage;
import com.dimeng.p2p.variables.defines.BusinessVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@Right(id = "P2P_C_ACCOUNT_ADDGR", name = "新增个人账号", moduleId = "P2P_C_ACCOUNT_ZHGL", order = 1)
public class AddGr extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
        if (is_business)
        {
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
        try
        {
            ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
            
            String accountNameEx =
                configureProvider.getProperty(SystemVariable.NEW_USERNAME_REGEX).substring(1,
                    configureProvider.getProperty(SystemVariable.NEW_USERNAME_REGEX).length() - 1);
            String accountName = request.getParameter("userName");
            String employNum = request.getParameter("employNum");
            if (!RSAUtils.decryptStringByJs(accountName).matches(accountNameEx))
            {
                getController().prompt(request,
                    response,
                    PromptLevel.INFO,
                    configureProvider.getProperty(SystemVariable.USERNAME_REGEX_CONTENT));
                processGet(request, response, serviceSession);
                return;
            }
            // 校验密码是否匹配正则规则
            String regEx =
                configureProvider.getProperty(SystemVariable.NEW_PASSWORD_REGEX).substring(1,
                    configureProvider.getProperty(SystemVariable.NEW_PASSWORD_REGEX).length() - 1);
            
            String password = request.getParameter("password");
            if (!RSAUtils.decryptStringByJs(password).matches(regEx))
            {
                getController().prompt(request,
                    response,
                    PromptLevel.WARRING,
                    configureProvider.getProperty(SystemVariable.PASSWORD_REGEX_CONTENT));
                processGet(request, response, serviceSession);
                return;
            }
            //注册前判断平台账号是否存在，否，则增加平台账号
            PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
            ptAccountManage.addPtAccount();
            
            ZhglManage manage = serviceSession.getService(ZhglManage.class);
            manage.addGr(accountName, password, employNum);
            sendRedirect(request, response, getController().getURI(request, ZhList.class));
        }
        catch (Throwable e)
        {
            if (e instanceof ParameterException || e instanceof LogicalException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, e.getMessage());
                processGet(request, response, serviceSession);
            }
            else
            {
                super.onThrowable(request, response, e);
            }
        }
        
    }
    
}
