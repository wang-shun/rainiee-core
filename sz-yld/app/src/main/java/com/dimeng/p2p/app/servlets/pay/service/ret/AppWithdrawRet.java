package com.dimeng.p2p.app.servlets.pay.service.ret;

/**
 * 
 * 提现回调 
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月9日]
 */
public class AppWithdrawRet extends AbstractRetServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 373485876591729764L;
    
    @Override
    protected String getRetName()
    {
        return "提现";
    }
}
