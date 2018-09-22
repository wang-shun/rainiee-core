package com.dimeng.p2p.app.servlets;

/**
 * 需要会话Servlet
 * 
 */
public abstract class AbstractSecureServlet extends AbstractAppBaseServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected boolean mustAuthenticated()
    {
        return true;
    }
    
}
