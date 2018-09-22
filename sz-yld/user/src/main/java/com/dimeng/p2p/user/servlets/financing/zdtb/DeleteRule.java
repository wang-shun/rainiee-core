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
 * 删除自动投资规则
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月14日]
 */
public class DeleteRule extends AbstractFinancingServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int id = StringHelper.isEmpty(request.getParameter("id")) ? 0 : Integer.parseInt(request.getParameter("id"));
        if (id <= 0)
        {
            throw new IllegalArgumentException("该规则不存在");
        }
        ZdtbManage manage = serviceSession.getService(ZdtbManage.class);
        PrintWriter out = response.getWriter();
        try
        {          
        	manage.delete(id);
            
            out.print("{result:'success'}");
            out.close();
        }
        catch (Throwable e)
        {

            logger.error(e, e);
            out.print("{result:'faild'}");
            out.close();
        }
    }
}
