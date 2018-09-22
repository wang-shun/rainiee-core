package com.dimeng.p2p.user.servlets.capital;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.account.user.service.BankCardManage;
import com.dimeng.p2p.account.user.service.entity.BankCard;
import com.dimeng.p2p.account.user.service.entity.BankDetail;
import com.dimeng.p2p.modules.account.pay.service.UserManage;
import com.dimeng.p2p.modules.account.pay.service.entity.OpenEscrowGuideEntity;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;

/**
 * 
 * 托管引导页管理
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年5月16日]
 */
public class OpenEscrowGuide extends AbstractCapitalServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        this.processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        UserManage manage = serviceSession.getService(UserManage.class);
        BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
        ConfigureProvider configureProvider = this.getResourceProvider().getResource(ConfigureProvider.class);
        /*
         * 防止已经注册第三方的用户通过浏览器直接输入地址进入引导页
         * 判断存在托管帐号就重定向到用户中心页面
         */
        int accountId = serviceSession.getSession().getAccountId();
        T6119 t6119 = manage.selectT6119(accountId);
        //t6119 不为空表示已开户
        if (null != t6119)
        {
            sendRedirect(request, response, configureProvider.format(URLVariable.USER_INDEX));
            return;
        }
        OpenEscrowGuideEntity entity = manage.getOpenEscrowGuideEntity();
        BankCard bcard = bankCardManage.getBankCard(accountId);
        if (null != bcard)
        {
            entity.setBankCard(StringHelper.decode(bcard.BankNumber));
            BankDetail bankDetail = bankCardManage.getBankDetail(bcard.id);
            if (null != bankDetail)
            {
                entity.setBankName(bankDetail.Bankname);
                entity.setBankCode(bankDetail.bankCode);
            }
        }
        request.setAttribute("openEscrowGuideEntity", entity);
        forwardView(request, response, getClass());
        
    }
    
}
