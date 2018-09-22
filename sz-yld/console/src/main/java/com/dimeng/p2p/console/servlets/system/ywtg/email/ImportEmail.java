package com.dimeng.p2p.console.servlets.system.ywtg.email;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.config.ConsoleConst;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.systematic.console.service.EmailManage;
import com.dimeng.util.StringHelper;

@MultipartConfig
@Right(id = "P2P_C_SYS_ADDEMAIL", name = "新增", moduleId = "P2P_C_SYS_YWTG_YJTG", order = 1)
public class ImportEmail extends AbstractSystemServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        EmailManage emailManage = serviceSession.getService(EmailManage.class);
        ResourceProvider resourceProvider = getResourceProvider();
        Part part = request.getPart("file");
        if (!StringHelper.isEmpty(part.getContentType())
            && (part.getContentType().equals(ConsoleConst.CVS_STR)
                || part.getContentType().equals(ConsoleConst.TXT_STR) || part.getContentType()
                .equals(ConsoleConst.EXCLE_STR)))
        {
            String[] emails = emailManage.importEmail(part.getInputStream(), resourceProvider.getCharset());
            request.setAttribute("emails", emails);
        }
        else
        {
            prompt(request, response, PromptLevel.WARRING, "支持导入csv、txt格式数据");
        }
        forwardView(request, response, AddEmail.class);
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        super.onThrowable(request, response, throwable);
    }
}
