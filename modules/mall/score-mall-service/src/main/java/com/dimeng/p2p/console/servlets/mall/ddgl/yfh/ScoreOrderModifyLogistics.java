package com.dimeng.p2p.console.servlets.mall.ddgl.yfh;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S63.entities.T6359;
import com.dimeng.p2p.S63.entities.T6360;
import com.dimeng.p2p.S63.enums.T6360_F03;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.ScoreOrderManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_MALL_MODIFYLOGISTICS", name = "详情[修改]", moduleId = "P2P_C_MALL_DDGL_YFH", order = 1)
public class ScoreOrderModifyLogistics extends AbstractMallServlet
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
