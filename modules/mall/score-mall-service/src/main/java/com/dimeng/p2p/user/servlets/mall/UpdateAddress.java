package com.dimeng.p2p.user.servlets.mall;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.p2p.repeater.score.query.HarvestAddressQuery;
import com.dimeng.p2p.user.servlets.AbstractMallServlet;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateAddress extends AbstractMallServlet
{
    
    private static final long serialVersionUID = -2632876248730429710L;

    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {}
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }

}
