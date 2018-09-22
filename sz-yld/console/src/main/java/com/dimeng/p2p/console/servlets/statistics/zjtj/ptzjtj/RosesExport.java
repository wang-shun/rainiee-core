/*
 * 文 件 名:  RosesExport.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月7日
 */
package com.dimeng.p2p.console.servlets.statistics.zjtj.ptzjtj;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.statistics.AbstractStatisticsServlet;
import com.dimeng.p2p.modules.statistics.console.service.FundsManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.QuarterFunds;
import com.dimeng.util.parser.IntegerParser;

/**
 * <月度统计导出>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月7日]
 */
@Right(id = "P2P_C_STATISTICS_ROSESEXPORT", name = "月度统计导出", moduleId = "P2P_C_STATISTICS_ZJTJ_PT", order = 4)
public class RosesExport extends AbstractStatisticsServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int year = IntegerParser.parse(request.getParameter("year"));
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        FundsManage manage = serviceSession.getService(FundsManage.class);
        QuarterFunds[] quarterFunds = manage.getRosesStatistics(year);
        manage.exportRosesStatistics(quarterFunds, response.getOutputStream(), "GBK");
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
}
