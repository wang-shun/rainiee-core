package com.dimeng.p2p.app.servlets;

/**
 * 系统抽象Servlet
 * 
 */
public abstract class AbstractAppServlet extends AbstractAppBaseServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
    
}