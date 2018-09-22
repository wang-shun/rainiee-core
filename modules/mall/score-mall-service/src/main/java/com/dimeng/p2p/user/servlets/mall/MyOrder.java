package com.dimeng.p2p.user.servlets.mall;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.p2p.repeater.score.entity.MyOrderInfoExt;
import com.dimeng.p2p.user.servlets.AbstractMallServlet;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MyOrder extends AbstractMallServlet
{
	
	private static final long serialVersionUID = -2632876248730429710L;

    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {}
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
