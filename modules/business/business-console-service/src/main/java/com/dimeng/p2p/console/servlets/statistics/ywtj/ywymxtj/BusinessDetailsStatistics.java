package com.dimeng.p2p.console.servlets.statistics.ywtj.ywymxtj;

import com.dimeng.framework.http.entity.RoleBean;
import com.dimeng.framework.http.service.RoleManage;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.AbstractBuisnessServlet;
import com.dimeng.p2p.repeater.business.SysBusinessManage;
import com.dimeng.p2p.repeater.business.entity.Performance;
import com.dimeng.p2p.repeater.business.entity.Results;
import com.dimeng.p2p.repeater.business.query.ResultsQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;
/**
 * 
 * 业务员业绩明细
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月2日]
 */
@Right(id = "P2P_C_STATISTICS_BUSINESSDETAILS", name = "业务员业绩明细", moduleId = "P2P_C_STATISTICS_YWTJ_YWYMXTJ", order = 0)
public class BusinessDetailsStatistics extends AbstractBuisnessServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@SuppressWarnings(value="unchecked")
	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		
	    SysBusinessManage sysBusinessManage = serviceSession.getService(SysBusinessManage.class);
        RoleManage roleManage = serviceSession.getService(RoleManage.class);
        Performance performance=sysBusinessManage.findPerformances();
        
        ResultsQuery query = new ResultsQuery()
        {
            
            @Override
            public String getEmployNum()
            {
                return request.getParameter("employNum");
            }
            
            @Override
            public Timestamp getCreateTimeStart()
            {
                return TimestampParser.parse(request.getParameter("createTimeStart"));
            }
            
            @Override
            public Timestamp getCreateTimeEnd()
            {
                return TimestampParser.parse(request.getParameter("createTimeEnd"));
            }
            
            @Override
            public String getName()
            {
                return request.getParameter("userName");
            }
            
            @Override
            public String getProject()
            {
                return request.getParameter("project");
            }
            
            @Override
            public String getUserType()
            {
                return request.getParameter("zhlx");
            }
            
            @Override
            public String getNamelevel()
            {
                return request.getParameter("nameLevel");
            }

            @Override
            public String getCustomName()
            {
                return request.getParameter("customName");
            }

            /**
             * 一级客户姓名
             *
             * @return
             */
            @Override
            public String getUserNameLevel() {
                return request.getParameter("belongLevel");
            }

            /**
             * 客户姓名
             *
             * @return
             */
            @Override
            public String getCustomRealName() {
                return request.getParameter("customRealName");
            }

            /**
             * 交易类型
             *
             * @return
             */
            @Override
            public String getTradeType() {
                return request.getParameter("tradeType");
            }
        };
        
        Paging paging = new Paging()
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
        };
        PagingResult<RoleBean> roles = roleManage.search(null, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return Integer.MAX_VALUE;
            }
            
            @Override
            public int getCurrentPage()
            {
                return 1;
            }
        });
        request.setAttribute("roles", roles.getItems());
        Map<String, Object> result = sysBusinessManage.searchAll(query, paging);
        PagingResult<Results> pagItem = (PagingResult<Results>)result.get("pagItems");
        request.setAttribute("performance", performance);
        request.setAttribute("result", pagItem);
		request.setAttribute("loanTotalAmount", result.get("loanTotalAmount"));
		request.setAttribute("investTotalAmount", result.get("investTotalAmount"));
        request.setAttribute("chargeTotalAmount", result.get("chargeTotalAmount"));
        request.setAttribute("withdrawTotalAmount", result.get("withdrawTotalAmount"));
		forwardView(request, response, BusinessDetailsStatistics.class);
	}
	@Override
	protected void onThrowable(HttpServletRequest request,
			HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		super.onThrowable(request, response, throwable);
	}
}
