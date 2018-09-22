package com.dimeng.p2p.console.servlets.mall.ddgl.dsh;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S63.entities.T6359;
import com.dimeng.p2p.S63.entities.T6360;
import com.dimeng.p2p.S63.enums.T6359_F08;
import com.dimeng.p2p.S63.enums.T6360_F03;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.ScoreOrderManage;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 待审核订单列表
 *
 * @author pengwei
 * @version [版本号, 2015/12/29]
 */
/*@Right(id = "P2P_C_MALL_SIPPINGORDER", name = "审核",moduleId="P2P_C_MALL_DDGL_DSH",order=1)*/
public class SippingOrder extends AbstractMallServlet{
    private static final long serialVersionUID = 1L;

    @Override
    protected void processGet(HttpServletRequest request,
                              HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        processPost(request, response, serviceSession);
    }

    @Override
    protected void processPost(final HttpServletRequest request,
                               final HttpServletResponse response,
                               final ServiceSession serviceSession) throws Throwable {}
}