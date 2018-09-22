package com.dimeng.p2p.console.servlets.ebq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.AbstractServlet;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6230;
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
 * 垫付、三方、四方保全合同查看
 * @author dengwenwu
 *
 */
public class Index extends AbstractServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int creditId = IntegerParser.parse(request.getParameter("id"));
        BidManage bidManage = serviceSession.getService(BidManage.class);
        
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
		ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
		String IS_SAVE_BID_CONTRACT = configureProvider.getProperty(SystemVariable.IS_SAVE_BID_CONTRACT);
		
		//是否垫付
        String isDf = request.getParameter("df");
        //如果是垫付，跳到垫付协议页面
        if (!StringHelper.isEmpty(isDf))
        {
        	if("true".equals(IS_SAVE_BID_CONTRACT)){//开启了保全，跳转到保全界面
    			PreservationManager preservationManager = serviceSession.getService(PreservationManager.class);
    			String url = preservationManager.getContractPreservationUrl(Integer.parseInt(request.getParameter("dfuid")), Integer.parseInt(request.getParameter("bid")));
    			if(null != url){
    				sendRedirect(request, response, url);
    				return;
    			}
    		}
        	
            forward(request, response, configureProvider.getProperty(URLVariable.CONSOLE_DF_URL));
            return;
        }
        
		if("true".equals(IS_SAVE_BID_CONTRACT)){//开启了保全，跳转到保全界面
			PreservationManager preservationManager = serviceSession.getService(PreservationManager.class);
			Bdxq bdxq = bidManage.get(creditId);
			String url = preservationManager.getContractPreservationUrl(bdxq.F02, creditId);
			if(null != url){
				sendRedirect(request, response, url);
				return;
			}
		}
		
        T6230 bdxq = bidManage.get(creditId);
        if (bdxq.F11 == T6230_F11.S)
        {
            forward(request, response, configureProvider.getProperty(URLVariable.CONSOLE_SIFANG_URL));
        }
        else
        {
            forward(request, response, configureProvider.getProperty(URLVariable.CONSOLE_SANFANG_URL));
        }
        
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
}
