package com.dimeng.p2p.console.servlets.info.yygl.kfzx;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.http.upload.PartFile;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S50.enums.T5012_F03;
import com.dimeng.p2p.S50.enums.T5012_F11;
import com.dimeng.p2p.common.FileUploadUtils;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.modules.base.console.service.CustomerServiceManage;
import com.dimeng.p2p.modules.base.console.service.entity.CustomerService;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.IntegerParser;

@MultipartConfig
@Right(id = "P2P_C_INFO_YYGL_MENU", isMenu = true, name = "运营管理", moduleId = "P2P_C_INFO_YYGL", order = 0)
public class AddKfzx extends AbstractKfzxServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        CustomerServiceManage manage = serviceSession.getService(CustomerServiceManage.class);
        ResourceProvider resourceProvider = getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        Integer allowZxkfCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.ALLOW_ZXKF_COUNT));
        int count = manage.getQyCount(T5012_F11.QY);
        if (count >= allowZxkfCount)
        {
            prompt(request, response, PromptLevel.ERROR, "启用的客服数量已达最大限制数：" + allowZxkfCount);
            sendRedirect(request, response, getController().getURI(request, SearchKfzx.class));
        }
        else
        {
            forwardView(request, response, getClass());
        }
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        try
        {
            if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
            {
                throw new LogicalException("请不要重复提交请求！");
            }
            CustomerServiceManage manage = serviceSession.getService(CustomerServiceManage.class);
            
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
                    return request.getParameter("number") + request.getParameter("zxdm");
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
            ResourceProvider resourceProvider = getResourceProvider();
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            Integer allowZxkfCount =
                IntegerParser.parse(configureProvider.getProperty(SystemVariable.ALLOW_ZXKF_COUNT));
            int count = manage.getQyCount(T5012_F11.QY);
            if (count >= allowZxkfCount)
            {
                prompt(request, response, PromptLevel.ERROR, "启用的客服数量已达最大限制数：" + allowZxkfCount + "。");
                sendRedirect(request, response, getController().getURI(request, SearchKfzx.class));
                return;
            }
            else
            {
                manage.add(service);
            }
            sendRedirect(request, response, getController().getURI(request, SearchKfzx.class));
        }
        catch (ParameterException | LogicalException e)
        {
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            processGet(request, response, serviceSession);
        }
        
    }
    
}
