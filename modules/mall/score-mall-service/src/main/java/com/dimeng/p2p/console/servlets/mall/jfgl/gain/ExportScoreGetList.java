package com.dimeng.p2p.console.servlets.mall.jfgl.gain;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6106;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.UserScoreManage;
import com.dimeng.p2p.repeater.score.entity.UserScoreExt;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@Right(id = "P2P_C_MALL_JFHQJLDC", name = "导出", moduleId = "P2P_C_MALL_JFGL_JFHQJL", order = 1)
public class ExportScoreGetList extends AbstractMallServlet
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
