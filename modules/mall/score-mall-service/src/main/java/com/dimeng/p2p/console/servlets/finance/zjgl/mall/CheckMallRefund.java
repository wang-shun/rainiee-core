/*
 * 文 件 名:  MallRefundTg.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月28日
 */
package com.dimeng.p2p.console.servlets.finance.zjgl.mall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S63.enums.T6360_F03;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.order.MallRefundExecutor;
import com.dimeng.p2p.repeater.score.ScoreOrderManage;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * <平台商城退款>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月28日]
 */
@Right(id = "P2P_C_FINANCE_MALLREFUNDTG", name = "退款", moduleId = "P2P_C_FINANCE_ZJGL_SCTKGL", order = 1)
public class CheckMallRefund extends AbstractMallServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -8824873015804243028L;
    
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
    
}