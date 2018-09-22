/*
 * 文 件 名:  ExperienceStatics.java
 * 版    权:  © 2014 DM. All rights reserved.
 * 描    述:  体验金统计管理
 * 修 改 人:  linxiaolin
 * 修改时间:  2015/3/19
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.dimeng.p2p.console.servlets.spread.hdgl.tyjjstj;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.account.console.experience.service.ExperienceStatisticsManage;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceStaticTotal;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceStatistics;
import com.dimeng.p2p.modules.account.console.experience.service.query.ExperienceStatisticsQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 体验金统计管理
 * @author linxiaolin
 * @version [1.0, 2015/2/10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Right(id = "P2P_C_SPREAD_TYJGL_STATICS_TYJ", name = "体验金结算统计", moduleId = "P2P_C_SPREAD_HDGL_TYJJSTJ", order = 1)
public class ExperienceStaticsDH extends AbstractSpreadServlet {

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
        throws Throwable
    {
        ExperienceStatisticsManage experienceManage = serviceSession.getService(ExperienceStatisticsManage.class);
        ExperienceStatisticsQuery experienceStatisticsQuery = new ExperienceStatisticsQuery()
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
            public Timestamp repaymentStartTime()
            {
                return TimestampParser.parse(request.getParameter("repaymentStartTime"));
            }
            
            @Override
            public Timestamp repaymentEndTime()
            {
                return TimestampParser.parse(request.getParameter("repaymentEndTime"));
            }
        };
        PagingResult<ExperienceStatistics> result = experienceManage.searchDh(experienceStatisticsQuery, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return 10;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        });
        ExperienceStatistics searchDhAmount = experienceManage.searchDhAmount(experienceStatisticsQuery);
        ExperienceStaticTotal experienceStaticTotal = experienceManage.getExperienceTotal();
        request.setAttribute("result", result);
        request.setAttribute("experienceStaticTotal", experienceStaticTotal);
        request.setAttribute("searchDhAmount", searchDhAmount);
        forwardView(request, response, getClass());
    }
}
