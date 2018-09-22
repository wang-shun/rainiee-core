/*
 * 文 件 名:  SearchTyjgl.java
 * 版    权:  © 2014 DM. All rights reserved.
 * 描    述:  体验金详情列表
 * 修 改 人:  linxiaolin
 * 修改时间:  2015/3/19
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.dimeng.p2p.console.servlets.spread.hdgl.tyjxq;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.account.console.experience.service.ExperienceDetailManage;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceTotalInfo;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceTotalList;
import com.dimeng.p2p.modules.account.console.experience.service.query.ExperienceDetailQuery;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 体验金详情管理
 * @author linxiaolin
 * @version [1.0, 2015/2/10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Right(id = "P2P_C_SPREAD_TYJGL_SEARCH", isMenu=true, name = "体验金详情列表", moduleId = "P2P_C_SPREAD_HDGL_TYJXQ", order = 0)
public class SearchTyjgl extends AbstractSpreadServlet{

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
        ExperienceDetailManage manage = serviceSession.getService(ExperienceDetailManage.class);
		Paging paging = new Paging() {
			
			@Override
			public int getSize() {
				return 10;
			}
			
			@Override
			public int getCurrentPage() {
				return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
			}
		};

        ExperienceDetailQuery query = new ExperienceDetailQuery() {
			
			@Override
			public String userName() {
				return request.getParameter("userName");
			}

            @Override
            public String bid() {
                return request.getParameter("bid");
            }

            @Override
            public Timestamp invalidStartTime() {
                return TimestampParser.parse(request.getParameter("invalidStartTime"));
            }

            @Override
            public Timestamp invalidEndTime() {
                return TimestampParser.parse(request.getParameter("invalidEndTime"));
            }

            @Override
            public T6103_F06 status() {
                return EnumParser.parse(T6103_F06.class, request.getParameter("status"));
            }

            @Override
            public Timestamp lixiStartTime() {
                return TimestampParser.parse(request.getParameter("lixiStartTime"));
            }

            @Override
            public Timestamp lixiEndTime() {
                return TimestampParser.parse(request.getParameter("lixiEndTime"));
            }
        };
        //列表
		PagingResult<ExperienceTotalList> result = manage.searchTotalList(query, paging);
        ExperienceTotalList searchTotalAmount = manage.searchTotalAmount(query);

        //统计
		ExperienceTotalInfo totalInfo = manage.getExperienceTotal();
		request.setAttribute("result", result);
		request.setAttribute("totalInfo", totalInfo);
        request.setAttribute("searchTotalAmount", searchTotalAmount);
		forwardView(request, response, getClass());
	}
}
