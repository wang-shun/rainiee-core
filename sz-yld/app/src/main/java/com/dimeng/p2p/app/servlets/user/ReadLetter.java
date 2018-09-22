package com.dimeng.p2p.app.servlets.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.LetterManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.LetterReturnCode;
import com.dimeng.util.parser.IntegerParser;

public class ReadLetter extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        LetterReturnCode lrc = new LetterReturnCode();
        lrc.setCode(ExceptionCode.SUCCESS);
        lrc.setDescription("success");
        
        response.setContentType("text/html;charset=" + getResourceProvider().getCharset());
        LetterManage letterManage = serviceSession.getService(LetterManage.class);
        final String sid = getParameter(request, "id");
        int id = IntegerParser.parse(sid);
        if (id == 0)
        {
            lrc.setCode(ExceptionCode.PARAMETER_ERROR);
            lrc.setDescription("参数错误");
            returnHandle(request, response, lrc);
            return;
        }
        letterManage.updateToRead(id);
        lrc.setDescription("success");
        lrc.setCode(ExceptionCode.SUCCESS);
        returnHandle(request, response, lrc);
        return;
    }
}
