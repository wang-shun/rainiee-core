package com.dimeng.p2p.console.servlets.mall.ddgl.dsh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S63.entities.T6359;
import com.dimeng.p2p.S63.entities.T6360;
import com.dimeng.p2p.S63.enums.T6359_F08;
import com.dimeng.p2p.S63.enums.T6360_F03;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.order.MallRefundExecutor;
import com.dimeng.p2p.repeater.score.ScoreOrderManage;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderDetailRecord;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 待审核订单列表
 *
 * @author beiweiyuan
 * @version [版本号, 2015/12/14]
 */
@Right(id = "P2P_C_MALL_APPROVEORDER", name = "审核[批量审核]", moduleId = "P2P_C_MALL_DDGL_DSH", order = 3)
public class ApproveOrder extends AbstractMallServlet
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
}