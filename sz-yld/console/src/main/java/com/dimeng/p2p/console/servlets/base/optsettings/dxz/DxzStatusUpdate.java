package com.dimeng.p2p.console.servlets.base.optsettings.dxz;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.bid.console.service.DxzBidManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 定向组状态修改
 * @author  zhongsai
 * @version  [V7.0, 2018年2月9日]
 */
@Right(id = "P2P_C_BASE_DXZ_ZT_UPDATE", name = "状态修改", moduleId = "P2P_C_BASE_OPTSETTINGS_DXZTYPE", order = 2)
public class DxzStatusUpdate extends AbstractBaseServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        try
        {
            int dxzId = IntegerParser.parse(request.getParameter("dxzId"));
            String status = request.getParameter("status");
            DxzBidManage dxzBidManage = serviceSession.getService(DxzBidManage.class);
            dxzBidManage.updateT6217Status(dxzId, status);
            out.print("修改成功！");
        }
        catch (Throwable e)
        {
            log(e.getMessage());
            out.print("修改状态失败！");
        }

    }
    

}
