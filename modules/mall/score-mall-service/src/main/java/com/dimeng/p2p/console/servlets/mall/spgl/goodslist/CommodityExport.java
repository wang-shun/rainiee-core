package com.dimeng.p2p.console.servlets.mall.spgl.goodslist;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.ScoreCommodityManage;
import com.dimeng.p2p.repeater.score.entity.CommoditySearch;
import com.dimeng.p2p.repeater.score.entity.T6351Ext;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@Right(id = "P2P_C_MALL_COMMODITY_EXPORT", name = "导出",moduleId="P2P_C_MALL_SPGL_SHLB",order=3)
public class CommodityExport extends AbstractMallServlet {
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
			final ServiceSession serviceSession) throws Throwable {}

}
