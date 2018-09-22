package com.dimeng.p2p.user.servlets.financing.zdtb;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.modules.bid.user.service.ZdtbManage;
import com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet;
import com.dimeng.util.StringHelper;
/**
 * 
 * 检查是否存在相同期限的自动投资规则
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月13日]
 */
public class ExistRule extends AbstractFinancingServlet
{
    private static final long serialVersionUID = 8383649446527084817L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int start =
            StringHelper.isEmpty(request.getParameter("start")) ? 0 : Integer.parseInt(request.getParameter("start"));
        
        int end =
            StringHelper.isEmpty(request.getParameter("end")) ? 0 : Integer.parseInt(request.getParameter("end"));
        ZdtbManage zdtbManage = serviceSession.getService(ZdtbManage.class);
        int result = zdtbManage.queryExist(start,end);
        PrintWriter out = response.getWriter();
        out.print("{result:" + result + "}");
        out.close();
    }
}
