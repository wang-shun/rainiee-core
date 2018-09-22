package com.dimeng.p2p.console.servlets.info.yygl.spsc;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.http.upload.PartFile;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.modules.base.console.service.AdvertisementManage;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertSpsc;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertSpscRecord;

@MultipartConfig
@Right(id = "P2P_C_INFO_YYGL_MENU", name = "运营管理",moduleId="P2P_C_INFO_YYGL",order=0)
public class UpdateSpsc extends AbstractSpscServlet {

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		AdvertisementManage manage = serviceSession
				.getService(AdvertisementManage.class);
		AdvertSpscRecord record = manage.searchSpsc(Integer.parseInt(request
				.getParameter("id")));
		if (record == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		request.setAttribute("record", record);
		forwardView(request, response, getClass());
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		try {
			AdvertisementManage advertisementManage = serviceSession
					.getService(AdvertisementManage.class);
			//获取文件信息
			final Part part = request.getPart("file");	
			String h = part.getHeader("content-disposition");
			 String Szie=null;
			 String Format=null;
		   String filename = h.substring(h.lastIndexOf("=") + 2, h.length() - 1);
		    if(!(filename==null||filename.equals(""))){
		    	Format = filename.substring(filename.indexOf(".")+1,filename.length());
		    //装换文件大小
		    long size = part.getSize();
		    DecimalFormat df = new DecimalFormat("#.00"); 
		    if (size < 1024) { 
		    	Szie = df.format((double) size) + "B"; 
		    } else if (size < 1048576) { 
		    	Szie = df.format((double) size / 1024) + "KB"; 
		    } else if (size < 1073741824) { 
		    	Szie = df.format((double) size / 1048576) + "M"; 
		    } else { 
		    	Szie = df.format((double) size / 1073741824) + "G"; 
		    } 
		    }
		    final String fileSize = Szie;
		    final String fileFormat = Format;
		    AdvertSpsc spsc  = new AdvertSpsc() {


				@Override
				public String getTitle() {
					return request.getParameter("title");
				}

				@Override
				public UploadFile getFile() throws IOException,
						ServletException {
					if (part == null || part.getContentType() == null
							|| part.getSize() == 0) {
						return null;
					}
					return new PartFile(part);
				}
				
				@Override
				public String getStatus() {
					return request.getParameter("status");
				}

				@Override
				public int getIsAuto() {
					return Integer.parseInt(request.getParameter("auto"));
				}


				@Override
				public String fileFormat() {
					return fileFormat;
					
				}

				@Override
				public String fileSize() {
					return fileSize;
					
				}

				@Override
				public String getsortIndex() {
					return request.getParameter("sortIndex");
				}
			};

		    advertisementManage.updateSpsc(Integer.parseInt(request.getParameter("id")), spsc,request.getParameter("url"));
			
			  sendRedirect(request, response, getController().getURI(request,
					  SearchSpsc.class));
			 
		} catch (ParameterException | LogicalException e) {
			prompt(request, response, PromptLevel.ERROR, e.getMessage());
			processGet(request, response, serviceSession);
		}
	}
}
