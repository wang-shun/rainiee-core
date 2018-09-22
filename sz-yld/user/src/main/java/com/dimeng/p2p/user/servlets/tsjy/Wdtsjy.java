package com.dimeng.p2p.user.servlets.tsjy;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6195_EXT;
import com.dimeng.p2p.account.user.service.TzjyManage;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.user.servlets.letter.AbstractLetterServlet;
import com.dimeng.p2p.variables.defines.SiteSwitchVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Wdtsjy extends AbstractLetterServlet {

	private static final long serialVersionUID = 1L;
	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        //开关判断，为false，则不显示我要吐槽
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        boolean advice_complain_switch = BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.ADVICE_COMPLAIN_SWITCH));
        if (!advice_complain_switch) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        TzjyManage manage = serviceSession.getService(TzjyManage.class);

		PagingResult<T6195_EXT> t6195_EXTs = manage.search(serviceSession.getSession().getAccountId(),new Paging() {

			@Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter("currentPage"));
            }

            @Override
            public int getSize()
            {
                return IntegerParser.parse(request.getParameter("pageSize"));
            }
		});
		String pageStr = rendPaging(t6195_EXTs);
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", t6195_EXTs.getPageCount());
        jsonMap.put("t6195_EXTs", t6195_EXTs.getItems());
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
	}
}
