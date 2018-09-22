package com.dimeng.p2p.user.servlets.ebq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.AbstractServlet;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.preservations.ebao.service.PreservationManager;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.parser.IntegerParser;

/**
 * 债权转让合同查看
 * @author dengwenwu
 *
 */
public class ZqzrPreservationView extends AbstractServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		int id = IntegerParser.parse(request.getParameter("id"));

		
		ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
		ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
		
		//是否开启保全
		String IS_SAVE_TRANSFER_CONTRACT = configureProvider.getProperty(SystemVariable.IS_SAVE_TRANSFER_CONTRACT);
		if("true".equals(IS_SAVE_TRANSFER_CONTRACT)){//开启了保全，跳转到保全界面
			PreservationManager preservationManager = serviceSession.getService(PreservationManager.class);
			String url = preservationManager.getZqzrContractPreservationUrl(id, serviceSession.getSession().getAccountId());//获取合同保全后的地址
			if(null != url){
				sendRedirect(request, response, url);
				return;
			}
		}
		
		sendRedirect(request, response, configureProvider.format(URLVariable.USER_WBQ_ZQZR) + "?id=" + id);
	}
	
	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}
}
