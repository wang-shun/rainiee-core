package com.dimeng.p2p.console.servlets.mall.jfgl.gain;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.util.StringHelper;

/**
 * 积分获取记录列表
 *
 * @author pengwei
 * @version [版本号, 2015/12/15]
 */
@Right(id = "P2P_C_MALL_JFCZ", isMenu = false, name = "积分充值", moduleId = "P2P_C_MALL_JFGL_JFHQJL", order = 0)
public class ChargeScore extends AbstractMallServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {}
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {}
}