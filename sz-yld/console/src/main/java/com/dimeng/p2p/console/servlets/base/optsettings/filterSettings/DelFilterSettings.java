package com.dimeng.p2p.console.servlets.base.optsettings.filterSettings;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.base.console.service.FilterSettingsManage;
import com.dimeng.util.parser.IntegerParser;

public class DelFilterSettings extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        FilterSettingsManage filterSettings = serviceSession.getService(FilterSettingsManage.class);
        PrintWriter out = response.getWriter();
        int NHSYids = IntegerParser.parse(request.getParameter("NHSYids"));
        int RZJDids = IntegerParser.parse(request.getParameter("RZJDids"));
        int JKQXids = IntegerParser.parse(request.getParameter("JKQXids"));
        filterSettings.delFilterSettings(NHSYids);
        filterSettings.delFilterSettings(RZJDids);
        filterSettings.delFilterSettings(JKQXids);
        
        StringBuilder sb = new StringBuilder();
        sb.append("[{'num':1,'msg':'");
        sb.append("sussess" + "'}]");
        out.write(sb.toString());
        return;
    }
    
}
