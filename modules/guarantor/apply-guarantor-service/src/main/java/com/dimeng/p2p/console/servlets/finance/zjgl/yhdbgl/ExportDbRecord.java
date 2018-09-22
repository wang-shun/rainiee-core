package com.dimeng.p2p.console.servlets.finance.zjgl.yhdbgl;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.finance.AbstractGuarantorServlet;
import com.dimeng.p2p.repeater.guarantor.ApplyGuarantorManage;
import com.dimeng.p2p.repeater.guarantor.entity.DbRecordEntity;
import com.dimeng.p2p.repeater.guarantor.query.DbRecordQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_FINANCE_DBRECORD_EXPORT", name = "导出额度明细", moduleId = "P2P_C_FINANCE_ZJGL_YHDBGL", order = 6)
public class ExportDbRecord extends AbstractGuarantorServlet
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
        ApplyGuarantorManage manage = serviceSession.getService(ApplyGuarantorManage.class);
        PagingResult<DbRecordEntity> result = manage.searchAmountTrandRecords(new DbRecordQuery()
        {
            
            @Override
            public int getType()
            {
                return IntegerParser.parse(request.getParameter("type"));
            }
            
            @Override
            public Timestamp getStartPayTime()
            {
                return TimestampParser.parse(request.getParameter("startPayTime"));
            }
            
            @Override
            public Timestamp getEndPayTime()
            {
                return TimestampParser.parse(request.getParameter("endPayTime") + "23:59:59");
            }
            
            @Override
            public int getUserId()
            {
                return IntegerParser.parse(request.getParameter("id"));
            }
        }, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return Integer.MAX_VALUE;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        });
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "GBK")))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("发生时间");
            writer.write("类型明细");
            writer.write("收入(元)");
            writer.write("支出(元)");
            writer.write("结余(元)");
            writer.write("备注");
            writer.newLine();
            int index = 1;
            if (result != null)
            {
                for (DbRecordEntity entity : result.getItems())
                {
                    if (entity == null)
                    {
                        continue;
                    }
                    writer.write(index++);
                    writer.write(DateTimeParser.format(entity.F04, "yyyy-MM-dd HH:mm") + "\t");
                    writer.write(entity.type);
                    writer.write(Formater.formatAmount(entity.F05));
                    writer.write(Formater.formatAmount(entity.F06));
                    writer.write(Formater.formatAmount(entity.F07));
                    writer.write(entity.F08);
                    writer.newLine();
                }
            }
        }
    }
}
