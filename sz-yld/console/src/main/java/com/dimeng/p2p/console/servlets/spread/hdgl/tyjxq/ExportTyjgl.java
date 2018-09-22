package com.dimeng.p2p.console.servlets.spread.hdgl.tyjxq;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.account.console.experience.service.ExperienceDetailManage;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceTotalList;
import com.dimeng.p2p.modules.account.console.experience.service.query.ExperienceDetailQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_SPREAD_TYJGL_EXPORT", name = "导出体验金详情列表", moduleId = "P2P_C_SPREAD_HDGL_TYJXQ")
public class ExportTyjgl extends AbstractSpreadServlet
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
        
        ExperienceDetailManage manage = serviceSession.getService(ExperienceDetailManage.class);
        ExperienceDetailQuery query = new ExperienceDetailQuery()
        {
            
            @Override
            public String userName()
            {
                return request.getParameter("userName");
            }
            
            @Override
            public String bid()
            {
                return request.getParameter("bid");
            }
            
            @Override
            public Timestamp invalidStartTime()
            {
                return TimestampParser.parse(request.getParameter("invalidStartTime"));
            }
            
            @Override
            public Timestamp invalidEndTime()
            {
                return TimestampParser.parse(request.getParameter("invalidEndTime"));
            }
            
            @Override
            public T6103_F06 status()
            {
                return EnumParser.parse(T6103_F06.class, request.getParameter("status"));
            }
            
            @Override
            public Timestamp lixiStartTime()
            {
                return TimestampParser.parse(request.getParameter("lixiStartTime"));
            }
            
            @Override
            public Timestamp lixiEndTime()
            {
                return TimestampParser.parse(request.getParameter("lixiEndTime"));
            }
        };
        ExperienceTotalList[] lists = manage.exportsExperienceLists(query);
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        if (lists == null)
        {
            return;
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "GBK")))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("用户名");
            writer.write("姓名");
            writer.write("标编号");
            writer.write("投资金额(元)");
            writer.write("体验金(元)");
            writer.write("年化利率");
            writer.write("项目期限");
            writer.write("状态");
            writer.write("投资时间");
            writer.write("收益(元)");
            writer.write("起息日");
            int i = 1;
            writer.newLine();
            for (ExperienceTotalList list : lists)
            {
                writer.write(i++);
                writer.write(list.userName);
                writer.write(list.userRealName);
                writer.write(list.bid);
                writer.write(Formater.formatAmount(list.investmoney));
                writer.write(Formater.formatAmount(list.experienceMoney));
                writer.write(Formater.formatRate(list.jkNlv));
                if (T6231_F21.S == list.borMethod)
                {
                    writer.write(list.jkDay + "天");
                }
                else
                {
                    writer.write(list.jkTime + "个月");
                }
                writer.write(list.status.getChineseName());
                writer.write(TimestampParser.format(list.tbTime) + "\t");
                writer.write(Formater.formatAmount(list.experienceInterest));
                writer.write(TimestampParser.format(list.interestTime) + "\t");
                writer.newLine();
            }
        }
    }
    
}
