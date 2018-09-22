package com.dimeng.p2p.app.servlets.pay.service.ret;

/**
 * 
 * 第三方绑卡回调
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月9日]
 */
public class AppBindCardRet extends AbstractRetServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3647432747864844013L;

    @Override
    protected String getRetName()
    {
        return "绑定银行卡";
    }
}
