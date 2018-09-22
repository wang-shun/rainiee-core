/*
 * 文 件 名:  UpdateAssessNum.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年3月9日
 */
package com.dimeng.p2p.console.servlets.account.vipmanage.grxx;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.repeater.policy.RiskAssessManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * <更新评估次数>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年3月9日]
 */
public class UpdateAssessNum extends AbstractAccountServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1247414554895565691L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        try
        {
            int userId = IntegerParser.parse(request.getParameter("userId"));
            RiskAssessManage riskAssessManage = serviceSession.getService(RiskAssessManage.class);
            int num = riskAssessManage.updateT6147F05(userId);
            out.write("{result:'001',message:'" + num + "'}");
        }
        catch (Throwable e)
        {
            out.write("{result:'000',message:'" + e.getMessage() + "'}");
        }
        finally
        {
            out.close();
        }
    }
    
}
