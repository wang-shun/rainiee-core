/*
 * 文 件 名:  ExportDbList
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/6/14
 */
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
import com.dimeng.p2p.repeater.guarantor.entity.GuarantorEntity;
import com.dimeng.p2p.repeater.guarantor.query.GuarantorQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 用户担保导出
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/6/14]
 */
@Right(id = "P2P_C_FINANCE_EXPORTDB", name = "导出", moduleId = "P2P_C_FINANCE_ZJGL_YHDBGL", order = 4)
public class ExportDbList extends AbstractGuarantorServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        ApplyGuarantorManage manage = serviceSession.getService(ApplyGuarantorManage.class);
        GuarantorQuery query = new GuarantorQuery()
        {
            
            /**
             * 用户名
             *
             * @return
             */
            @Override
            public String getUserName()
            {
                return request.getParameter("userName");
            }
            
            /**
             * 姓名/企业名
             *
             * @return
             */
            @Override
            public String getRealName()
            {
                return request.getParameter("realName");
            }
            
            /**
             * 担保码
             *
             * @return
             */
            @Override
            public String getCode()
            {
                return request.getParameter("code");
            }
            
            /**
             * 用户类型
             *
             * @return
             */
            @Override
            public String getUserType()
            {
                return request.getParameter("userType");
            }
        };
        
        PagingResult<GuarantorEntity> result = manage.searchGuarantorInfos(query, new Paging()
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
            writer.write("用户名");
            writer.write("用户类型");
            writer.write("姓名/企业名");
            writer.write("担保人编号");
            writer.write("担保额度(元)");
            writer.write("状态");
            writer.write("最后更新时间");
            writer.newLine();
            int index = 1;
            for (GuarantorEntity entity : result.getItems())
            {
                if (entity == null)
                {
                    continue;
                }
                writer.write(index++);
                writer.write(entity.userName);
                writer.write(entity.userType);
                writer.write(entity.realName);
                writer.write(entity.code);
                writer.write(Formater.formatAmount(entity.guarantAmount));
                writer.write(entity.status.getChineseName());
                writer.write(DateTimeParser.format(entity.updateTime) + "\t");
                writer.newLine();
            }
        }
    }
}
