package com.dimeng.p2p.console.servlets.base.optsettings.dxz;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.bid.console.service.DxzBidManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 验证定向组 id 与 名称是否存在
 * @author  zhongsai
 * @version  [V7.0, 2018年2月8日]
 */
public class CheckDxzInfo extends AbstractBaseServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        //验证类型：dxzId:定向组ID; dxzName:定向组名称
        String checkType = request.getParameter("checkType");
        //验证值
        String dxzIdOrName = request.getParameter("dxzIdOrName");
        String typeName = "dxzId".equals(checkType) ? "定向组ID" : "定向组名称";
        StringBuilder sb = new StringBuilder();
        if (StringHelper.isEmpty(dxzIdOrName))
        {
            sb.append("[{'num':00,'msg':'");
            sb.append(typeName + "不能为空。" + "'}]");
            out.write(sb.toString());
            return;
        }

        DxzBidManage dxzBidManage = serviceSession.getService(DxzBidManage.class);
        boolean isExist =
            dxzBidManage.isExist(checkType, dxzIdOrName, IntegerParser.parse(request.getParameter("F01")));
        if (isExist)
        {
            sb.append("[{'num':00,'msg':'");
            sb.append(typeName + "已经存在，请重新输入。" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        sb.append("[{'num':01,'msg':'");
        sb.append("sussess" + "'}]");
        out.write(sb.toString());
    }
}