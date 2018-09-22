/*
 * 文 件 名:  viewExperienceDetail.java
 * 版    权:  © 2014 DM. All rights reserved.
 * 描    述:  体验金详情列表-查看
 * 修 改 人:  linxiaolin
 * 修改时间:  2015/3/19
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.dimeng.p2p.console.servlets.spread.hdgl.tyjxq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.account.console.experience.service.ExperienceDetailManage;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceProfit;
import com.dimeng.util.parser.IntegerParser;

/**
 * 体验金详情列表-查看
 * @author linxiaolin
 * @version [1.0, 2015/3/19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Right(id = "P2P_C_SPREAD_TYJQX_VIEW", name = "体验金详情列表查看", moduleId = "P2P_C_SPREAD_HDGL_TYJXQ")
public class ViewExperienceDetail extends AbstractSpreadServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			final HttpServletResponse response,
			final ServiceSession serviceSession) throws Throwable {
        ExperienceDetailManage manage = serviceSession.getService(ExperienceDetailManage.class);
        String id = request.getParameter("id");
        int userId = IntegerParser.parse(request.getParameter("userId"));
        PagingResult<ExperienceProfit> list = manage.get(id, userId, new Paging()
        {
            @Override
            public int getSize() {
                return 10;
            }

            @Override
            public int getCurrentPage() {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        });
		request.setAttribute("list", list);
		forwardView(request, response, getClass());
	}

}
