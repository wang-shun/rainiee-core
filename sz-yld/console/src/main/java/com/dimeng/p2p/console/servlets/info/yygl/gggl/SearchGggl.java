package com.dimeng.p2p.console.servlets.info.yygl.gggl;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.AdvertisementStatus;
import com.dimeng.p2p.modules.base.console.service.AdvertisementManage;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementRecord;
import com.dimeng.p2p.modules.base.console.service.query.AdvertisementRecordQuery;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_INFO_YYGL_MENU", isMenu = true, name = "运营管理",moduleId="P2P_C_INFO_YYGL",order=0)
public class SearchGggl extends AbstractAdvertiseServlet {

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
		AdvertisementManage advertisementManage = serviceSession
				.getService(AdvertisementManage.class);
		AdvertisementRecordQuery advRecordQuery = new AdvertisementRecordQuery(){

            @Override
            public String getPublisherName()
            {
                return request.getParameter("publisherName");
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
            public Timestamp getUpdateTimeStart()
            {
                return TimestampParser.parse(request.getParameter("updateTimeStart"));
            }

            @Override
            public Timestamp getUpdateTimeEnd()
            {
                return TimestampParser.parse(request.getParameter("updateTimeEnd"));
            }

            @Override
            public String getTitle()
            {
                return request.getParameter("title");
            }

            @Override
            public AdvertisementStatus getStatus()
            {
                return EnumParser.parse(AdvertisementStatus.class, request.getParameter("advertisementStatus"));
            }

            @Override
            public String getAdvType()
            {
                return request.getParameter("advType");
            }
		    
        };
		PagingResult<AdvertisementRecord> result = advertisementManage.search(
advRecordQuery, new Paging()
        {
					@Override
					public int getSize() {
						return 10;
					}

					@Override
					public int getCurrentPage() {
						return IntegerParser.parse(request
								.getParameter(PAGING_CURRENT));
					}
				});
		request.setAttribute("result", result);
		forwardView(request, response, getClass());
	}

}
