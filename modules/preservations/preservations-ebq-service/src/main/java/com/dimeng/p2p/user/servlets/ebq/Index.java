package com.dimeng.p2p.user.servlets.ebq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.AbstractServlet;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.modules.bid.user.service.BidManage;
import com.dimeng.p2p.modules.bid.user.service.entity.Bdxq;
import com.dimeng.p2p.preservations.ebao.service.PreservationManager;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 垫付、三方、四方合同查看
 * @author dengwenwu
 *
 */
public class Index extends AbstractServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		int creditId = IntegerParser.parse(request.getParameter("id"));

		ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
		ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
		
		//是否公益标
		String isGyb = request.getParameter("g");
		//如果是公益标,跳到公益标页面
		if(!StringHelper.isEmpty(isGyb)){
			forward(request, response, configureProvider.getProperty(URLVariable.USER_GYB_URL));
			return;
		}
		
        //是否垫付
        String isDf = request.getParameter("df");
        //如果是垫付，跳到垫付协议页面
        if (!StringHelper.isEmpty(isDf))
        {
        	//是否开启保全
    		String IS_SAVE_ADVANCE_CONTRACT = configureProvider.getProperty(SystemVariable.IS_SAVE_ADVANCE_CONTRACT);
        	if("true".equals(IS_SAVE_ADVANCE_CONTRACT)){//开启了保全，跳转到保全界面
    			PreservationManager preservationManager = serviceSession.getService(PreservationManager.class);
    			String url = preservationManager.getContractPreservationUrl(serviceSession.getSession().getAccountId(), creditId);//获取合同保全后的地址
    			if(null != url){
    				sendRedirect(request, response, url);
    				return;
    			}
    		}
        	
            forward(request, response, configureProvider.getProperty(URLVariable.USER_DF_URL));
            return;
        }

      //是否开启保全
      	String IS_SAVE_BID_CONTRACT = configureProvider.getProperty(SystemVariable.IS_SAVE_BID_CONTRACT);
		BidManage bidManage =	serviceSession.getService(BidManage.class);
		Bdxq bdxq = bidManage.get(creditId);
		if(bdxq==null){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		if("true".equals(IS_SAVE_BID_CONTRACT)){//开启了保全，跳转到保全界面
			PreservationManager preservationManager = serviceSession.getService(PreservationManager.class);
			String url = preservationManager.getContractPreservationUrl(serviceSession.getSession().getAccountId(), creditId);//获取合同保全后的地址
			if(null != url){
				sendRedirect(request, response, url);
				return;
			}
		}
		
		if(bdxq.F11 == T6230_F11.S){
			forward(request, response, configureProvider.getProperty(URLVariable.USER_SIFANG_URL));
		}else{
			forward(request, response, configureProvider.getProperty(URLVariable.USER_SANFANG_URL));
		}
		

	}
	
	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}
}
