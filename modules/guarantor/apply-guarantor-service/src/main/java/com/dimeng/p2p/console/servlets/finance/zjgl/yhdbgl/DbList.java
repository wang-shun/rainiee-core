package com.dimeng.p2p.console.servlets.finance.zjgl.yhdbgl;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.finance.AbstractGuarantorServlet;
import com.dimeng.p2p.repeater.guarantor.ApplyGuarantorManage;
import com.dimeng.p2p.repeater.guarantor.entity.GuarantorEntity;
import com.dimeng.p2p.repeater.guarantor.query.GuarantorQuery;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Right(id = "P2P_C_FINANCE_DBLLIST", name = "用户担保管理", moduleId="P2P_C_FINANCE_ZJGL_YHDBGL", order = 0)
public class DbList extends AbstractGuarantorServlet {
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
        final ServiceSession serviceSession)
        throws Throwable
    {
        ApplyGuarantorManage manage = serviceSession.getService(ApplyGuarantorManage.class);
        GuarantorQuery query = new GuarantorQuery() {

            /**
             * 用户名
             *
             * @return
             */
            @Override
            public String getUserName() {
                return request.getParameter("userName");
            }

            /**
             * 姓名/企业名
             *
             * @return
             */
            @Override
            public String getRealName() {
                return request.getParameter("realName");
            }

            /**
             * 担保码
             *
             * @return
             */
            @Override
            public String getCode() {
                return request.getParameter("code");
            }

            /**
             * 用户类型
             *
             * @return
             */
            @Override
            public String getUserType() {
                return request.getParameter("userType");
            }
        };

        PagingResult<GuarantorEntity> result = manage.searchGuarantorInfos(query, new Paging() {
            @Override
            public int getSize() {
                return 10;

            }

            @Override
            public int getCurrentPage() {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));

            }
        });
        request.setAttribute("result", result);
        forwardView(request, response, getClass());
    }
}
