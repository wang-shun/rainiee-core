package com.dimeng.p2p.app.servlets.pay.service.ret;

/**
 * 
 * 第三方回调响应消息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月9日]
 */
public class AppAuthorizeRet extends AbstractRetServlet
{
    private static final long serialVersionUID = 4715914203630944499L;
    
    @Override
    protected String getRetName()
    {
        return "第三方授权";
    }
    
}
