package com.dimeng.p2p.console.servlets.account.vipmanage.zhgl;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.console.servlets.bid.csgl.hmd.HmdList;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_ACCOUNT_BLACK", name = "拉黑[取消拉黑]",moduleId= "P2P_C_ACCOUNT_ZHGL",order=8)
public class UnBlack extends AbstractAccountServlet {
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
	    
	    try
        {
	        UserManage manage = serviceSession.getService(UserManage.class);
	        int id = IntegerParser.parse(request.getParameter("userId"));
	        String blacklistDesc = request.getParameter("blacklistDesc");
	        manage.unBlack(id, blacklistDesc);
        }
        catch (SQLException e)
        {
            getController().prompt(request, response, PromptLevel.ERROR,
                    "系统繁忙，请您稍后再试");
        }catch (LogicalException e)
        {
            logger.error(e, e);
            getController().prompt(request, response, PromptLevel.WARRING,
                e.getMessage());
        }catch (ParameterException e)
        {
            getController().prompt(request, response, PromptLevel.WARRING,
                e.getMessage());
        }finally {
            if (StringHelper.isEmpty(request.getParameter("pageFlg")))
            {
                sendRedirect(request, response, getController().getURI(request, HmdList.class));
            }
            else
            {
                sendRedirect(request, response, getController().getURI(request, ZhList.class));
            }
        }
		
	}
	
}
