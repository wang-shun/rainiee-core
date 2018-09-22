/*
 * 文 件 名:  CancelTransfer.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月10日
 */
package com.dimeng.p2p.app.servlets.creditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.user.service.ZqzrManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * 取消债券转让
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月10日]
 */
public class CancelTransfer extends AbstractSecureServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2014319775024429764L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 债券ID
        final int creditorId = IntegerParser.parse(getParameter(request, "creditorId"));
        
        // 取消债券操作
        ZqzrManage finacingManage = serviceSession.getService(ZqzrManage.class);
        finacingManage.cancel(creditorId);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!");
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
