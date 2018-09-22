package com.dimeng.p2p.console.servlets.bid.csgl.hmd;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;

@Right(id = "P2P_C_BUSI_CS_HMDVIEW", name = "黑名单详情", moduleId = "P2P_C_BID_CSGL_HMD", order = 1)
public class Hmdxq extends AbstractBidServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
    }
    
}
