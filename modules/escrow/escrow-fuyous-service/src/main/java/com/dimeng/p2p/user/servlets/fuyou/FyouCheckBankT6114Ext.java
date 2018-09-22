package com.dimeng.p2p.user.servlets.fuyou;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.escrow.fuyou.service.BankManage;

/**
 * 
 * 查询用户申请更换银行卡记录
 * 
 * @author lingyuanjie
 * @version [版本号, 2016年4月15日]
 */
public class FyouCheckBankT6114Ext extends AbstractFuyouServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        response.setContentType("text/html;charset=" + getResourceProvider().getCharset());
        logger.info("查询用户申请更换银行卡记录");
        BankManage bankManage = serviceSession.getService(BankManage.class);
        boolean isResult = bankManage.selectT6114Ext();
        response.getWriter().println(isResult);
        response.getWriter().flush();
    }
    
}
