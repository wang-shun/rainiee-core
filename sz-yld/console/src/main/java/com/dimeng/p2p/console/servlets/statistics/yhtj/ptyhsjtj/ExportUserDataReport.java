package com.dimeng.p2p.console.servlets.statistics.yhtj.ptyhsjtj;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S70.entities.T7052;
import com.dimeng.p2p.console.servlets.statistics.AbstractStatisticsServlet;
import com.dimeng.p2p.modules.statistics.console.service.UserDataManage;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * 导出平台用户数据
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2016年06月07日]
 */
@Right(id = "P2P_C_STATISTICS_USERDATA_EXPORT", name = "导出", moduleId = "P2P_C_STATISTICS_YHTJ_YHSJTJ", order = 2)
public class ExportUserDataReport extends AbstractStatisticsServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        response.setHeader("Content-disposition",
                "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        UserDataManage manage = serviceSession.getService(UserDataManage.class);
        Date startDate = TimestampParser.parse(request.getParameter("startDate"));
        Date endDate = TimestampParser.parse(request.getParameter("endDate"));
        PagingResult<T7052> pagingResult = manage.searchUsersOpData(new Paging() {
            @Override
            public int getCurrentPage() {
                return IntegerParser.parse(request.getParameter(AbstractStatisticsServlet.PAGING_CURRENT));
            }

            @Override
            public int getSize() {
                return Integer.MAX_VALUE;
            }
        },startDate,endDate);
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "GBK")))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("日期");
            writer.write("注册用户数");
            writer.write("PC端注册用户数");
            writer.write("APP端注册用户数");
            writer.write("微信端注册用户数");
            writer.write("后台注册用户数");
            writer.write("登录用户数");
            writer.write("充值用户数");
            writer.write("提现用户数");
            writer.write("投资用户数");
            writer.write("借款用户数");
            writer.newLine();
            int i = 0;
            if (pagingResult != null)
            {
                for (T7052 t7052 : pagingResult.getItems())
                {
                    i++;
                    writer.write(i);
                    writer.write(t7052.F01);
                    writer.write(t7052.F02);
                    writer.write(t7052.F03);
                    writer.write(t7052.F04);
                    writer.write(t7052.F05);
                    writer.write(t7052.F06);
                    writer.write(t7052.F07);
                    writer.write(t7052.F08);
                    writer.write(t7052.F09);
                    writer.write(t7052.F10);
                    writer.write(t7052.F11);
                    writer.newLine();
                }
            }
        }
        
    }
}
