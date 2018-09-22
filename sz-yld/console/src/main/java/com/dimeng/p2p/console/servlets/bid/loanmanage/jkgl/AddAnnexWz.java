package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.PartFile;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.ImageThread;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6233;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.common.FileUploadUtils;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.modules.bid.console.service.AnnexManage;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.p2p.modules.bid.console.service.entity.WzAnnex;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.PrintWriter;
import java.util.Collection;

@MultipartConfig
public class AddAnnexWz extends AbstractBidServlet
{
    
    private static final long serialVersionUID = -268955794485557959L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        AnnexManage annexManage = serviceSession.getService(AnnexManage.class);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int userId = IntegerParser.parse(request.getParameter("userId"));
        try
        {
            BidManage bidManage = serviceSession.getService(BidManage.class);
            T6230 loan1 = bidManage.get(loanId);
            if (loan1 != null && loan1.F20 != T6230_F20.SQZ)
            {
                sendRedirect(request, response, getController().getURI(request, LoanList.class));
                return;
            }
            UserManage userManage = serviceSession.getService(UserManage.class);
            T6110_F06 userType = userManage.getUserType(userId);
            request.setAttribute("userType", userType);
            T6230 t6230 = bidManage.getBidType(loanId);
            request.setAttribute("t6230", t6230);
            WzAnnex[] t6233s = annexManage.searchFgk(loanId);
            request.setAttribute("t6233s", t6233s);
            forwardView(request, response, getClass());
        }
        catch (Throwable throwable)
        {
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, getController().getURI(request, AddAnnexMsk.class) + "?loanId="
                    + loanId + "&userId=" + userId);
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = null;
        try
        {
            out = response.getWriter();
            AnnexManage annexManage = serviceSession.getService(AnnexManage.class);
            final int loanId = IntegerParser.parse(request.getParameter("loanId"));
            int typeId = IntegerParser.parse(request.getParameter("typeId"));
            if (typeId <= 0)
            {
                out.print("{'num':1,'msg':'参数错误'}");
                return;
            }
            BidManage bidManage = serviceSession.getService(BidManage.class);
            T6230 loan1 = bidManage.get(loanId);
            if (loan1 != null && loan1.F20 != T6230_F20.SQZ)
            {
                out.print("{'num':1,'msg':'不是申请中状态'}");
                return;
            }
            String rtnMsg = "";
            int i = 0;
            Collection<Part> parts = request.getParts();
            final FileStore fileStore = getResourceProvider().getResource(FileStore.class);
            for (final Part part : parts)
            {
                if (part.getContentType() == null)
                {
                    continue;
                }
                String value = part.getHeaders("content-disposition").toString();
                String flag = "=\"";
                String fileName = value.substring(value.lastIndexOf(flag) + 2, value.lastIndexOf("."));
                String fileType = value.substring(value.lastIndexOf(".") + 1, value.length() - 2);
                if (fileName.length() > 60)
                {
                    rtnMsg = "{'num':1,'msg':'" + fileName + "." + fileType + "附件的名字太长。如要上传，请修改文件名！（附件名60个字节或30个汉字。）'}";
                    break;
                }
                if (fileName.contains("=") || fileName.contains(",") || fileName.contains("&"))
                {
                    rtnMsg = "{'num':1,'msg':'" + fileName + "." + fileType + "附件名称不符合要求,不能有符号,请重新上传!'}";
                    break;
                }
                if (!FileUploadUtils.checkFileType(part.getInputStream(), fileType, getResourceProvider()))
                {
                    rtnMsg =
                        "{'num':1,'msg':'不支持" + fileName + "."
                            + String.valueOf(FileUploadUtils.getType(part.getInputStream())).toLowerCase()
                            + "此类文件上传！'}";
                    break;
                }

                String[] fileCodes = fileStore.upload(i, new PartFile(part));
                String fileCode = "";
                if (fileCodes != null)
                {
                    fileCode = fileCodes[i];
                }
                Thread thread = new Thread(new ImageThread(fileCode, IntegerParser.parse(part.getSize())));
                thread.start();
                T6233 t6233 = new T6233();
                t6233.F02 = loanId;
                t6233.F03 = typeId;
                t6233.F04 = fileName;
                t6233.F06 = fileCode;
                annexManage.addFgk(t6233, new PartFile(part));
                rtnMsg = "{'num':0,'msg':'附件已经上传成功！'}";
                i++;
            }
            out.print(rtnMsg);
            
        }
        catch (Exception e)
        {
            out.print("{'num':1,'msg':'" + e.getMessage() + "'}");
        }
        finally
        {
            if (out != null)
            {
                out.close();
            }
        }
        
    }
}
