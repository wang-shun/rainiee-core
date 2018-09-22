/*
 * 文 件 名:  PttzglList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年11月19日
 */
package com.dimeng.p2p.console.servlets.finance.zjgl.pttzgl;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.account.console.service.CheckBalanceManage;
import com.dimeng.p2p.modules.account.console.service.entity.CheckBalanceRecord;
import com.dimeng.p2p.modules.account.console.service.entity.CheckBalanceTotalAmount;
import com.dimeng.p2p.modules.account.console.service.query.CheckBalanceQuery;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 平台调账管理列表
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年11月19日]
 */
@Right(id = "P2P_C_FINANCE_PTTZGLLIST", name = "平台调账管理", moduleId="P2P_C_FINANCE_ZJGL_PTTZGL", order = 0)
public class PttzglList extends AbstractFinanceServlet {

	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request, HttpServletResponse response,
			ServiceSession serviceSession) throws Throwable {
		this.processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request, HttpServletResponse response,
			ServiceSession serviceSession) throws Throwable {
		ResourceProvider resourceProvider = getResourceProvider();
		ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
		Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
		CheckBalanceManage manage=serviceSession.getService(CheckBalanceManage.class);
		CheckBalanceTotalAmount totalAmountEntity=manage.getTotal(tg);
		CheckBalanceQuery query=new CheckBalanceQuery(){

			@Override
			public String getOrderId() {
				return request.getParameter("orderId");
			}

			@Override
			public String getStatus() {
				return request.getParameter("status");
			}
			
			@Override
			public String getType() {
				return request.getParameter("type");
			}

			@Override
			public String getOperationer() {
				return request.getParameter("operationer");
			}

			@Override
			public Timestamp getOptStart() {
				return TimestampParser.parse(request.getParameter("startTime"));
			}

			@Override
			public Timestamp getOptEnd() {
				return TimestampParser.parse(request.getParameter("endTime"));
			}
			
		};
		PagingResult<CheckBalanceRecord> result = manage.search(query, new Paging()
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
        },tg);
		CheckBalanceTotalAmount cbta2=manage.getSelectTotal(query,tg);
        CheckBalanceRecord searchAmount = manage.searchAmount(query, tg);
		request.setAttribute("totalIncomeAmount", totalAmountEntity.totalIncomeAmount);
		request.setAttribute("totalExpendAmount", totalAmountEntity.totalExpendAmount);
		request.setAttribute("result", result);
		request.setAttribute("cbta", cbta2);
        request.setAttribute("searchAmount", searchAmount);
		forwardView(request, response, getClass());
	}

}
