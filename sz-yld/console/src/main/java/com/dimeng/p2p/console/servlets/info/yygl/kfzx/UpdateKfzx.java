package com.dimeng.p2p.console.servlets.info.yygl.kfzx;

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
import com.dimeng.p2p.S50.enums.T5012_F03;
import com.dimeng.p2p.S50.enums.T5012_F11;
import com.dimeng.p2p.common.FileUploadUtils;
import com.dimeng.p2p.modules.base.console.service.CustomerServiceManage;
import com.dimeng.p2p.modules.base.console.service.entity.CustomerService;
import com.dimeng.p2p.modules.base.console.service.entity.CustomerServiceRecord;
import com.dimeng.util.parser.IntegerParser;

@MultipartConfig
@Right(id = "P2P_C_INFO_YYGL_MENU", name = "运营管理", moduleId = "P2P_C_INFO_YYGL", order = 0)
public class UpdateKfzx extends AbstractKfzxServlet
{
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        CustomerServiceManage manage = serviceSession.getService(CustomerServiceManage.class);
        CustomerServiceRecord record = manage.get(Integer.parseInt(request.getParameter("id")));
        if (record == null)
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("record", record);
        forwardView(request, response, getClass());
    }
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        try
        {
            CustomerServiceManage manage = serviceSession.getService(CustomerServiceManage.class);
            String doType = request.getParameter("doType");
            if ("updateStatus".equals(doType))
            {
                //停用、启用
                String id = request.getParameter("id");
                T5012_F11 status = T5012_F11.parse(request.getParameter("status"));
                manage.updateStatus(id, status);
                sendRedirect(request, response, getController().getURI(request, SearchKfzx.class));
                return;
            }
            CustomerService service = new CustomerService()
            {
                
                @Override
                public T5012_F03 getType()
                {
                    return T5012_F03.parse(request.getParameter("type"));
                }
                
                @Override
                public int getSortIndex()
                {
                    return IntegerParser.parse(request.getParameter("sortIndex"));
                }
                
                @Override
                public String getNumber()
                {
                    return request.getParameter("number");
                }
                
                @Override
                public String getName()
                {
                    return request.getParameter("name");
                }
                
                @Override
                public UploadFile getImage()
                    throws Throwable
                {
                    Part part = request.getPart("image");
                    if (part == null || part.getContentType() == null || part.getSize() == 0)
                    {
                        return null;
                    }
                    String content = part.getHeaders("content-disposition").toString();
                    String fileType = content.substring(content.lastIndexOf(".") + 1, content.length() - 2);
                    String fileTypeTemp = String.valueOf(FileUploadUtils.getType(part.getInputStream()));
                    fileType = fileTypeTemp=="null"?fileType:fileTypeTemp;
                    if (!FileUploadUtils.checkFileType(part.getInputStream(), fileType, getResourceProvider()))
                    {
                        String rtnMsg = "不支持" + fileType + "此类文件上传！";
                        throw new ParameterException(rtnMsg);
                    }
                    return new PartFile(part);
                }
                
                @Override
                public String getQy()
                {
                    return request.getParameter("Qy");
                }
            };
            /* ResourceProvider resourceProvider = getResourceProvider();
             ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
             Integer allowZxkfCount =
                 IntegerParser.parse(configureProvider.getProperty(SystemVariable.ALLOW_ZXKF_COUNT));
             int count = manage.getQyCount(T5012_F11.QY);
             if (count >= allowZxkfCount)
             {
                 prompt(request, response, PromptLevel.ERROR, "启用的客服数量已达最大限制数：" + allowZxkfCount);
                 sendRedirect(request, response, getController().getURI(request, SearchKfzx.class));
                 return;
             }
             else
             {
                 manage.update(Integer.parseInt(request.getParameter("id")), service);
             }*/
            manage.update(Integer.parseInt(request.getParameter("id")), service);
            sendRedirect(request, response, getController().getURI(request, SearchKfzx.class));
        }
        catch (ParameterException | LogicalException e)
        {
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            processGet(request, response, serviceSession);
        }
    }
    
}
