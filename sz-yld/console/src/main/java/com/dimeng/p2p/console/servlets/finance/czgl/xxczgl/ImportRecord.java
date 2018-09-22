package com.dimeng.p2p.console.servlets.finance.czgl.xxczgl;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.config.ConsoleConst;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.account.console.service.OfflineChargeManage;
import com.dimeng.p2p.modules.account.console.service.entity.OfflineChargeImportRecord;
import com.dimeng.util.StringHelper;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;

@Right(id = "P2P_C_FINANCE_XXCZGLLIST_IMPORT", name = "批量导入", moduleId="P2P_C_FINANCE_CZGL_XXCZGL", order = 4)
@MultipartConfig
public class ImportRecord extends AbstractSpreadServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		OfflineChargeManage offlineChargeManage = serviceSession.getService(OfflineChargeManage.class);
		Part part = request.getPart("file");
		if (!StringHelper.isEmpty(part.getContentType())
				&& (part.getContentType().equals(ConsoleConst.CVS_STR)
						|| part.getContentType().equals(ConsoleConst.TXT_STR) 
						|| part.getContentType().equals(ConsoleConst.CVS_JS_STR))) {
			List<OfflineChargeImportRecord> list = offlineChargeManage.batchImportRecord(part.getInputStream(),part.getContentType());
			if(null != list && list.size() > 0) {
				request.setAttribute("errorRecords", JSONArray.fromObject(list).toString());
				prompt(request, response, PromptLevel.WARRING, "导入失败");
			}
			else
			{
				prompt(request, response, PromptLevel.INFO, "导入成功");
			}


		}else{
			logger.info("文件格式不正确，仅支持CSV、TXT格式");
		    throw new ParameterException("文件格式不正确，仅支持CSV、TXT格式");
		}
        forward(request, response, getController().getURI(request, XxczglList.class));
		//sendRedirect(request,response,getController().getURI(request, XxczglList.class));
	}

	@Override
	protected void onThrowable(HttpServletRequest request,
			HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
	    prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
        forward(request, response, getController().getURI(request, XxczglList.class));
	}
}