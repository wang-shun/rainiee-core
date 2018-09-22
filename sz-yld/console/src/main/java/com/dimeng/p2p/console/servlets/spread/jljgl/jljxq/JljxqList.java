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
package com.dimeng.p2p.console.servlets.spread.jljgl.jljxq;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.spread.console.service.SpreadManage;
import com.dimeng.p2p.modules.spread.console.service.entity.BonusList;
import com.dimeng.p2p.modules.spread.console.service.entity.BonusQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 奖励金详情
 * @version [1.0, 2015/2/10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Right(id = "P2P_C_SPREAD_JLJGL_JLJGLXQ_LIST", isMenu=true, name = "奖励金详情列表", moduleId = "P2P_C_SPREAD_JLJGL_JLJXQ", order = 0)
public class JljxqList extends AbstractSpreadServlet{

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
		SpreadManage manage = serviceSession.getService(SpreadManage.class);
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

		BonusQuery query = new BonusQuery() {
			
			@Override
			public String loanID() {
				return request.getParameter("loanID");
			}

            @Override
            public String loanUserName() {
                return request.getParameter("loanUserName");
            }

            @Override
            public String investUserName() {
                return request.getParameter("investUserName");
            }
            @Override
            public Timestamp fkStartTime() {
                return TimestampParser.parse(request.getParameter("fkStartTime"));
            }

            @Override
            public Timestamp fkEndTime() {
                return TimestampParser.parse(StringHelper.isEmpty(request.getParameter("fkEndTime")) ? null : request.getParameter("fkEndTime").concat(" 23:59:59"));
            }
        };
        //列表
		PagingResult<BonusList> result = manage.getBonusList(query, paging);
        BonusList getBonusListAmount = manage.getBonusListAmount(query);
        //统计
		BigDecimal total = manage.getBonusSum();
		request.setAttribute("result", result);
		request.setAttribute("total", total);
        request.setAttribute("getBonusListAmount", getBonusListAmount);
		forwardView(request, response, getClass());
	}
}
