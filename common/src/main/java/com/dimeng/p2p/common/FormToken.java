package com.dimeng.p2p.common;

import java.util.UUID;

import com.dimeng.framework.http.session.Session;
import com.dimeng.p2p.SystemConst;
import com.dimeng.util.StringHelper;

/**
 * 表单令牌
 * @author  XiaoLang 2014 © dimeng.net 
 * @version v3.0
 * @LastModified 
 * 		Created,by XiaoLang 2014年12月23日
 */
public class FormToken implements SystemConst
{
    
    public static boolean verify(Session session, String currentTokenValue)
    {
        if (session == null)
        {
            return false;
        }
        String token = session.getAttribute(parameterName());
        if (StringHelper.isEmpty(currentTokenValue) || !currentTokenValue.equals(token))
        {
            return false;
        }
        session.removeAttribute(parameterName());
        return true;
    }
    
    private static String generate(Session session)
    {
        String token = null;
        if (session == null)
        {
            return token;
        }
        token = UUID.randomUUID().toString();
        session.setAttribute(parameterName(), token);
        return token;
    }
    
    public static String parameterName()
    {
        return TOKEN_NAME;
    }
    
    private static String assignValue(Session session)
    {
        return generate(session);
    }
    
    private static String multiAssignValue(Session session)
    {
        synchronized (session)
        {
            String value = session.getAttribute(parameterName());
            if (value == null)
            {
                value = generate(session);
            }
            return value;
        }
    }

    /**
     * 表单页面TOKEN值生成
     * @param session
     * @return {@code <input type="hidden" name="[TOKEN_NAME]" value="[TOKEN_VALUE]" >}
     */
    public static String hidden(Session session)
    {
        return hidden(session, true);
    }
    
    /**
     * 表单页面TOKEN值生成
     * 
     * @param session
     * @param forceNew
     *      强制生成新的TOKEN值
     * @return {@code <input type="hidden" name="[TOKEN_NAME]" value="[TOKEN_VALUE]" >}
     */
    public static String hidden(Session session, boolean forceNew)
    {
        if (forceNew)
        {
            return "<input type=\"hidden\" name=\"" + parameterName() + "\" value=\"" + assignValue(session) + "\">";
        }
        else
        {
            return "<input type=\"hidden\" name=\"" + parameterName() + "\" value=\"" + multiAssignValue(session)
                + "\">";
        }
    }
    
    /**
     * 链接TOKEN值生成
     * @param session
     * @return {@code [TOKEN_NAME]=[TOKEN_VALUE]}
     */
    public static String link(Session session)
    {
        return link(session, true);
    }
    
    /**
     * 链接TOKEN值生成
     * @param session
     * @param forceNew
     *      强制生成新的TOKEN值
     * @return {@code [TOKEN_NAME]=[TOKEN_VALUE]}
     */
    public static String link(Session session, boolean forceNew)
    {
        if (forceNew)
        {
            return parameterName() + "=" + assignValue(session);
        }
        else
        {
            return parameterName() + "=" + multiAssignValue(session);
        }
    }
}
