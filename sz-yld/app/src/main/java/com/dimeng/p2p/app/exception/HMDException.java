package com.dimeng.p2p.app.exception;

import com.dimeng.framework.http.session.authentication.AuthenticationException;

public class HMDException extends AuthenticationException
{
    
    public HMDException()
    {
        super("账号异常,请联系客服！");
    }
    
    public HMDException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
    
    public HMDException(final String message)
    {
        super(message);
    }
    
    public HMDException(final Throwable cause)
    {
        super(cause);
    }
}
