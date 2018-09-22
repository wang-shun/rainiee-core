package com.dimeng.p2p.console.servlets.mall.ddgl.dfh;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.ScoreOrderManage;
import com.dimeng.p2p.repeater.score.entity.OperationLog;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderDetailRecord;

/**
 * 查看订单详情
 *
 */
@Right(id = "P2P_C_MALL_DFHSCOREORDERLIST", isMenu = true, name = "待发货", moduleId = "P2P_C_MALL_DDGL_DFH", order = 0)
public class DfhScoreOrderDetails extends AbstractMallServlet
{
    
    private static final long serialVersionUID = -4958563159932274595L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
    {}
    
}
