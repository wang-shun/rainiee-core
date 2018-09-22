package com.dimeng.p2p.user.servlets.fuyou;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.escrow.fuyou.entity.OpenEscrowGuideEntity;
import com.dimeng.p2p.escrow.fuyou.service.UserManage;
import com.dimeng.p2p.variables.defines.URLVariable;

/**
 * 
 * 托管引导页管理
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年5月16日]
 */
public class OpenEscrowGuide extends AbstractFuyouServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        UserManage manage = serviceSession.getService(UserManage.class);
        ConfigureProvider configureProvider = this.getResourceProvider().getResource(ConfigureProvider.class);
        /*
         * 防止已经注册第三方的用户通过浏览器直接输入地址进入引导页
         * 判断存在托管帐号就重定向到用户中心页面
         */
        String accountId = manage.selectT6119();
        if (StringUtils.isNotEmpty(accountId))
        {
            sendRedirect(request, response, configureProvider.format(URLVariable.USER_INDEX));
            return;
        }
        OpenEscrowGuideEntity entity = manage.getOpenEscrowGuideEntity();
        request.setAttribute("openEscrowGuideEntity", entity);
        forwardView(request, response, getClass());
        
    }
    
}
