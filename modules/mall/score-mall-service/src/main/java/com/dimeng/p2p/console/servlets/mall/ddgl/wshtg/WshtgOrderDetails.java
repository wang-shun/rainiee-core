package com.dimeng.p2p.console.servlets.mall.ddgl.wshtg;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.ScoreOrderManage;
import com.dimeng.p2p.repeater.score.entity.OperationLog;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderDetailRecord;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查看订单详情
 *
 */
@Right(id = "P2P_C_MALL_WSHTGORDERDETAIL", name = "详情",moduleId="P2P_C_MALL_DDGL_WSHTG",order=1)
public class WshtgOrderDetails extends AbstractMallServlet
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
