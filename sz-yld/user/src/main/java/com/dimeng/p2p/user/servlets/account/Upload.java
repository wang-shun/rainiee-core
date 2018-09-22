package com.dimeng.p2p.user.servlets.account;

import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dimeng.framework.http.upload.PartFile;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.UserBaseManage;
import com.dimeng.p2p.common.FileUploadUtils;

@MultipartConfig
public class Upload extends AbstractAccountServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		Collection<Part> parts = request.getParts();
        String rtnMsg = "";
		if (parts != null) {
			for (Part part : parts) {
				if (part == null || part.getContentType() == null
						|| part.getSize() == 0) {
					continue;
				}
                String content = part.getHeaders("content-disposition").toString();
                String fileType = content.substring(content.lastIndexOf(".") + 1, content.length() - 2);
                if (!FileUploadUtils.checkFileType(part.getInputStream(), fileType, getResourceProvider()))
                {
                    rtnMsg = "不支持" + String.valueOf(FileUploadUtils.getType(part.getInputStream())) + "此类文件上传！";
                    break;
                }
				UserBaseManage userBaseManage = serviceSession
						.getService(UserBaseManage.class);
				userBaseManage.upload(new PartFile(part));
                rtnMsg = "1";
				break;
			}
		}
		
        PrintWriter out = response.getWriter();
        out.print(rtnMsg);
        out.close();
	}
}
