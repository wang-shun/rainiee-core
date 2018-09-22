package com.dimeng.p2p.user.servlets.account;

import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.PartFile;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6120;
import com.dimeng.p2p.S61.enums.T6120_F05;
import com.dimeng.p2p.account.user.service.RzxxManage;
import com.dimeng.p2p.common.FileUploadUtils;
import com.dimeng.util.StringHelper;

@MultipartConfig
public class Rzsc extends AbstractAccountServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        RzxxManage manage = serviceSession.getService(RzxxManage.class);
        int rzID = Integer.parseInt(request.getParameter("id"));
        String isMulti = request.getParameter("isMulti");
        Collection<Part> parts = request.getParts();
        String rtnMsg = "";
        int i = 0;
        int rzjlId = 0;
        FileStore fileStore = getResourceProvider().getResource(FileStore.class);
        T6120 t6120 = null;
        for (final Part part : parts)
        {
            if (StringHelper.isEmpty(part.getContentType()))
            {
                continue;
            }
            String[] fileCodes = fileStore.upload(i, new PartFile(part));
            String fileCode = "";
            if (fileCodes != null)
            {
                fileCode = fileCodes[i];
            }
            String content = part.getHeaders("content-disposition").toString();
            String fileType = content.substring(content.lastIndexOf(".") + 1, content.length() - 2);
            if (!FileUploadUtils.checkFileType(part.getInputStream(), fileType, getResourceProvider()))
            {
                rtnMsg = "不支持" + String.valueOf(FileUploadUtils.getType(part.getInputStream())) + "此类文件上传！";
                break;
            }
            if ("yes".equalsIgnoreCase(isMulti))
            {
                t6120 = manage.getRzxx(rzID);
                rzjlId = t6120.F07;
                if (t6120.F05 != T6120_F05.DSH)
                {
                    rzjlId = manage.insertRzjl(rzID);
                }
                
                manage.insertFile(rzjlId, fileCode, new PartFile(part));
            }
            else
            {
                manage.uploadFile(rzID, fileCode, new PartFile(part));
            }
            rtnMsg = "1";
            i++;
        }
        
        PrintWriter out = response.getWriter();
        out.print(rtnMsg);
        out.close();
    }
    
}
