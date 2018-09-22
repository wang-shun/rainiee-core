package com.dimeng.p2p.user.servlets.financing.zdtb;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.enums.T6280_F10;
import com.dimeng.p2p.modules.bid.user.service.ZdtbManage;
import com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet;
import com.dimeng.util.StringHelper;

public class UpdateRule extends AbstractFinancingServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 6455695252942810932L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        
        PrintWriter out = response.getWriter();
        try
        {
            ZdtbManage autoUtilFinacingManage = serviceSession.getService(ZdtbManage.class);
            String status = request.getParameter("status");
            int id =
                StringHelper.isEmpty(request.getParameter("id")) ? 0 : Integer.parseInt(request.getParameter("id"));
            if (T6280_F10.QY.name().equals(status))
            {
                int count = autoUtilFinacingManage.autoBidCountQY(null);
                if (count >= 3)
                {
                    out.print("{result:'exceed'}");
                    out.close();
                    return;
                }
            }
            autoUtilFinacingManage.updateStatus(status, id);
            out.print("{result:'success'}");
            out.close();
        }
        catch (Throwable e)
        {
            
            logger.error(e, e);
            out.print("{result:'faild'}");
            out.close();
        }
        
        //forward(request, response, getController().getURI(request, Index.class));
        
    }
    
}
