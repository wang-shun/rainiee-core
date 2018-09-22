package com.dimeng.p2p.app.servlets.pay.service.ret;

/**
 * 
 * 第三方解绑回调
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月9日]
 */
public class AppUnBindCardRet extends AbstractRetServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -8818879308460154858L;
    
    @Override
    protected String getRetName()
    {
        return "银行卡解绑";
    }
}
