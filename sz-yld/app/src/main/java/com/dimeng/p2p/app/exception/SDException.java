package com.dimeng.p2p.app.exception;

import com.dimeng.framework.http.session.authentication.AuthenticationException;

public class SDException extends AuthenticationException
{
    
    public SDException()
    {
        super("用户已被锁定，如有疑问请联系客服工作人员！");
    }
    
    public SDException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
    
    public SDException(final String message)
    {
        super(message);
    }
    
    public SDException(final Throwable cause)
    {
        super(cause);
    }
}
