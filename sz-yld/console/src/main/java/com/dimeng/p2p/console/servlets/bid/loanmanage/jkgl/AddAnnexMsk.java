package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.PartFile;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.ImageThread;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.common.FileUploadUtils;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.modules.bid.console.service.AnnexManage;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.p2p.modules.bid.console.service.entity.MskAnnex;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.PrintWriter;
import java.util.Collection;

@MultipartConfig
public class AddAnnexMsk extends AbstractBidServlet
{
    
    private static final long serialVersionUID = -268955794485557959L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        AnnexManage annexManage = serviceSession.getService(AnnexManage.class);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int userId = IntegerParser.parse(request.getParameter("userId"));
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
        MskAnnex[] t6232s = annexManage.searchGk(loanId);
        request.setAttribute("t6232s", t6232s);
        forwardView(request, response, getClass());
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
            final int loanId = IntegerParser.parse(request.getParameter("loanId"));
            BidManage bidManage = serviceSession.getService(BidManage.class);
            T6230 loan1 = bidManage.get(loanId);
            if (loan1 != null && loan1.F20 != T6230_F20.SQZ)
            {
                out.print("{'num':1,'msg':'不是申请中状态'}");
                return;
            }
            AnnexManage annexManage = serviceSession.getService(AnnexManage.class);
            Collection<Part> parts = request.getParts();
            
            int typeId = IntegerParser.parse(request.getParameter("typeId"));
            if (typeId <= 0)
            {
                out.print("{'num':1,'msg':'参数错误'}");
                return;
            }
            int i = 0;
            String rtnMsg = "";
            FileStore fileStore = getResourceProvider().getResource(FileStore.class);
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
                    rtnMsg =
                        "{'num':1,'msg':'" + fileName + "." + fileType + "附件的名字太长。如要上传，请修改文件名！（附件名60个字节或30个汉字。）" + "'}";
                    break;
                }
                if (fileName.contains("=") || fileName.contains(",") || fileName.contains("&"))
                {
                    rtnMsg = "{'num':1,'msg':'" + fileName + "." + fileType + "附件名称不符合要求,不能有符号,请重新上传!" + "'}";
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
                T6232 t6232 = new T6232();
                t6232.F02 = loanId;
                t6232.F03 = typeId;
                t6232.F04 = fileCode;
                t6232.F05 = fileName;
                annexManage.addGk(t6232, new PartFile(part));
                i++;
                //rtnMsg = fileName + "." + fileType + "附件已经上传成功！";
                rtnMsg = "{'num':0,'msg':'附件已经上传成功！'}";
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
