/*
 * 文 件 名:  ExportStaticsDH.java
 * 版    权:  © 2014 DM. All rights reserved.
 * 描    述:  体验金统计-待还-导出
 * 修 改 人:  linxiaolin
 * 修改时间:  2015/3/10
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.dimeng.p2p.console.servlets.spread.hdgl.tyjjstj;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.account.console.experience.service.ExperienceStatisticsManage;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceStatistics;
import com.dimeng.p2p.modules.account.console.experience.service.query.ExperienceStatisticsQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

/**
 * 体验金统计-已还-导出
 * @author linxiaolin
 * @version [1.0, 2015/3/20]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Right(id = "P2P_C_SPREAD_TYJTJ_EXPORT", name = "导出体验金结算统计", moduleId = "P2P_C_SPREAD_HDGL_TYJJSTJ", order = 3)
public class ExportStaticsYH extends AbstractSpreadServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
        ExperienceStatisticsManage experienceManage = serviceSession
                .getService(ExperienceStatisticsManage.class);
        PagingResult<ExperienceStatistics> result = experienceManage.searchYh(
                new ExperienceStatisticsQuery() {

                    @Override
                    public String userName() {
                        return request.getParameter("userName");
                    }

                    @Override
                    public String bid() {
                        return request.getParameter("bid");
                    }

                    @Override
                    public Timestamp repaymentStartTime() {
                        return TimestampParser.parse(request.getParameter("repaymentStartTime"));
                    }

                    @Override
                    public Timestamp repaymentEndTime() {
                        return TimestampParser.parse(request.getParameter("repaymentEndTime"));
                    }

                }, new Paging() {

                    @Override
                    public int getSize() {
                        return Integer.MAX_VALUE;
                    }

                    @Override
                    public int getCurrentPage() {
                        return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
                    }
                });
		response.setHeader("Content-disposition", "attachment;filename="
				+ new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
		response.setContentType("application/csv");
        experienceManage.exportexportYH(result.getItems(), response.getOutputStream());
	}
}
