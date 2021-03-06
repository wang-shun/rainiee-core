package com.dimeng.p2p.console.servlets.bid.csgl.dhklb;

import java.sql.Date;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.bid.console.service.CollectionManage;
import com.dimeng.p2p.modules.bid.console.service.entity.Less30;
import com.dimeng.p2p.modules.bid.console.service.query.Greater30Query;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.SQLDateParser;

@Right(id = "P2P_C_BUSI_CSGL_EXPORT", name = "导出",moduleId="P2P_C_BID_CSGL_DHKLB",order=4)
public class Dcyzyq extends AbstractSystemServlet {
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
        CollectionManage collectionManage = serviceSession
				.getService(CollectionManage.class);
		response.setHeader("Content-disposition", "attachment;filename="
				+ new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
		response.setContentType("application/csv");
        PagingResult<Less30> greater30s = collectionManage.Greater30Search(new Greater30Query()
        {
            
            @Override
            public String getUserName()
            {
                return request.getParameter("userName");
            }
            
            @Override
            public String getLoanRecordTitle()
            {
                return request.getParameter("loanRecordTitle");
            }
            
            @Override
            public Date getCreateTimeStart()
            {
                return SQLDateParser.parse(request.getParameter("createTimeStart"));
            }
            
            @Override
            public Date getCreateTimeEnd()
            {
                return SQLDateParser.parse(request.getParameter("createTimeEnd"));
            }
        }, new Paging()
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
        collectionManage.exportGreater30(greater30s.getItems(),
				response.getOutputStream(), "");
	}

}
