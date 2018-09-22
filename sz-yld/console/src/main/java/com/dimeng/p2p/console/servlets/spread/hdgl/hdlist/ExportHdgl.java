package com.dimeng.p2p.console.servlets.spread.hdgl.hdlist;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S63.enums.T6340_F08;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.activity.console.service.ActivityManage;
import com.dimeng.p2p.modules.activity.console.service.entity.ActivityList;
import com.dimeng.p2p.modules.activity.console.service.entity.ActivityQuery;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 
 * 导出活动管理
 * 
 * @author heluzhu
 * @version [版本号, 2015年10月9日]
 */
@Right(id = "P2P_C_SPREAD_HDGL_EXPORTHDGL", name = "导出", moduleId = "P2P_C_SPREAD_HDGL_HDLIST", order = 6)
public class ExportHdgl extends AbstractSpreadServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        
        ActivityManage manage = serviceSession.getService(ActivityManage.class);
        Paging paging = new Paging()
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
        };
        
        ActivityQuery query = new ActivityQuery()
        {
            
            @Override
            public String code()
            {
                
                return request.getParameter("code");
            }
            
            @Override
            public String name()
            {
                return request.getParameter("name");
            }
            
            @Override
            public T6340_F03 jlType()
            {
                return EnumParser.parse(T6340_F03.class, request.getParameter("jlType"));
            }
            
            @Override
            public T6340_F04 hdType()
            {
                return EnumParser.parse(T6340_F04.class, request.getParameter("hdType"));
            }
            
            @Override
            public Timestamp startsTime()
            {
                return TimestampParser.parse(request.getParameter("startsTime"));
            }
            
            @Override
            public Timestamp starteTime()
            {
                return TimestampParser.parse(request.getParameter("starteTime"));
            }
            
            @Override
            public Timestamp endsTime()
            {
                return TimestampParser.parse(request.getParameter("endsTime"));
            }
            
            @Override
            public Timestamp endeTime()
            {
                return TimestampParser.parse(request.getParameter("endeTime"));
            }
            
            @Override
            public T6340_F08 status()
            {
                return EnumParser.parse(T6340_F08.class, request.getParameter("zt"));
            }
            
        };
        
        PagingResult<ActivityList> result = manage.searchActivity(query, paging);
        
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        if (result == null)
        {
            return;
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "GBK")))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("活动ID");
            writer.write("活动名称");
            writer.write("奖励类型");
            writer.write("活动类型");
            writer.write("活动开始时间");
            writer.write("活动结束时间");
            writer.write("总数量（个）");
            writer.write("总金额（元）");
            writer.write("已领取数量（个）");
            writer.write("已领取金额（元）");
            writer.write("活动状态");
            writer.write("操作时间");
            writer.newLine();
            int index = 1;
            for (ActivityList list : result.getItems())
            {
                writer.write(index++);
                writer.write(list.F02);
                writer.write(list.F05);
                writer.write(list.F03.getChineseName());
                writer.write(list.F04.getChineseName());
                writer.write(DateParser.format(list.F06));
                writer.write(DateParser.format(list.F07));
                writer.write(list.F08);
                if (T6340_F03.interest.name().equals(list.F03.name()))
                {
                    writer.write("--");
                }
                else
                {
                    writer.write(list.F10);
                }
                
                writer.write(list.F11);
                
                if (T6340_F03.interest.name().equals(list.F03.name()))
                {
                    writer.write("--");
                }
                else
                {
                    writer.write(list.F10.multiply(new BigDecimal(list.F11)));
                }
                writer.write(list.F12.getChineseName());
                writer.write(DateTimeParser.format(list.F13) + "\t");
                writer.newLine();
            }
        }
    }
    
}
