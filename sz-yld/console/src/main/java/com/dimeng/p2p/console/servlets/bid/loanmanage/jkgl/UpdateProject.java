package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import java.util.Calendar;
import java.util.Date;

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
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6216;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6238;
import com.dimeng.p2p.S62.enums.T6216_F04;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F28;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6231_F27;
import com.dimeng.p2p.S62.enums.T6231_F33;
import com.dimeng.p2p.S62.enums.T6231_F36;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.modules.base.console.service.ProductManage;
import com.dimeng.p2p.modules.base.console.service.query.ProductQuery;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.DateHelper;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;

@MultipartConfig
@Right(id = "P2P_C_LOAN_UPDATEPROJECT", name = "修改", moduleId = "P2P_C_BID_JKGL_LOANMANAGE", order = 3)
public class UpdateProject extends AbstractBidServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -268955794485557959L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        final BidManage bidManage = serviceSession.getService(BidManage.class);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int userId = IntegerParser.parse(request.getParameter("userId"));
        String productIdStr = request.getParameter("productId");
        UserManage userManage = serviceSession.getService(UserManage.class);
        String userName = userManage.getUserNameById(userId);
        request.setAttribute("userName", userName);
        T6211[] t6211s = bidManage.getBidType();
        request.setAttribute("t6211s", t6211s);
        T6110_F06 userType = userManage.getUserType(userId);
        request.setAttribute("userType", userType);
        T6230 loan = bidManage.get(loanId);
        T6231 t6231 = bidManage.getExtra(loanId);
        request.setAttribute("loan", loan);
        request.setAttribute("t6231", t6231);
        T6230 t6230 = bidManage.getBidType(loanId);
        T6238 t6238 = bidManage.getBidFl(loanId);
        request.setAttribute("t6238", t6238);
        request.setAttribute("t6230", t6230);
        
        ProductManage productManage = serviceSession.getService(ProductManage.class);
        Paging paging = new Paging()
        {
            @Override
            public int getSize()
            {
                return 100;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        };
        PagingResult<T6216> result = productManage.search(new ProductQuery()
        {
            @Override
            public T6216_F04 getStatus()
            {
                return T6216_F04.QY;
            }
            
            @Override
            public String getProductName()
            {
                return "";
            }
            
            @Override
            public String getBidType()
            {
                
                return "";
            }
            
            @Override
            public String getRepaymentWay()
            {
                return "";
            }
        }, paging);
        
        int productId = 0;
        if (StringHelper.isEmpty(productIdStr))
        {
            productId = t6230.F32;
        }
        else
        {
            productId = Integer.parseInt(productIdStr);
        }
        //当第一次修改薪金贷时，产品ID和标的.F32都为空时，取产品列表的第一个对象
        T6216 model = null;
        if (productId == 0)
        {
            model = result.getItems()[0];
        }
        else
        {
            model = productManage.get(productId);
        }
        if (StringHelper.isEmpty(productIdStr))
        {
            model.F12 = t6238.F02;
            model.F13 = t6238.F03;
            model.F14 = t6238.F04;
            //model.F15 = t6231.F25;
        }
        request.setAttribute("product", model); //产品信息
        request.setAttribute("productResult", result);
        super.processGet(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        //产品下拉列表改变事件：选择产品，重新带出产品规则
        String submitType = request.getParameter("submitType");
        if ("processGet".equalsIgnoreCase(submitType))
        {
            processGet(request, response, serviceSession);
            return;
        }
        
        ResourceProvider resourceProvider = getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int userId = IntegerParser.parse(request.getParameter("userId"));
        final BidManage bidManage = serviceSession.getService(BidManage.class);
        T6230 loan1 = bidManage.get(loanId);
        if (loan1 != null && loan1.F20 != T6230_F20.SQZ)
        {
            getController().prompt(request, response, PromptLevel.WARRING, "不是申请中状态");
            sendRedirect(request, response, getController().getURI(request, LoanList.class));
            return;
        }
        UserManage userManage = serviceSession.getService(UserManage.class);
        String productId = request.getParameter("productId");
        try
        {
            T6110_F06 userType = userManage.getUserType(userId);
            T6230 t6230 = new T6230();
            t6230.parse(request);
            t6230.F32 = IntegerParser.parse(productId);
            t6230.F01 = loanId;
            t6230.F02 = userId;
            T6231 t6231 = new T6231();
            t6231.F07 = IntegerParser.parse(request.getParameter("xian"));
            t6231.F08 = request.getParameter("jkyt");
            t6231.F09 = request.getParameter("jkms");
            t6231.F16 = request.getParameter("hkly");
            t6231.F25 = BigDecimalParser.parse(request.getParameter("zxtze"));
            t6231.F26 = BigDecimalParser.parse(request.getParameter("zdtze"));
            t6231.F28 = BigDecimalParser.parse(request.getParameter("jlll"));
            T6231_F33 f33 = T6231_F33.PC_APP;
            switch (request.getParameter("terminal"))
            {
                case "PC":
                    f33 = T6231_F33.PC;
                    break;
                case "APP":
                    f33 = T6231_F33.APP;
                    break;
            }
            t6231.F33 = f33;
            //            t6231.F36 = StringHelper.isEmpty(request.getParameter("rewardUsedType")) ? T6231_F36.ALL : T6231_F36.parse(request.getParameter("rewardUsedType"));
            t6231.F36 = T6231_F36.NONE;
            T6238 t6238 = new T6238();
            t6238.F02 = BigDecimalParser.parse(request.getParameter("cjfwfl"));
            t6238.F03 = BigDecimalParser.parse(request.getParameter("tzglfl"));
            t6238.F04 = BigDecimalParser.parse(request.getParameter("yqflfl"));
            String bidFlag = request.getParameter("bidFlag"); //是否为奖励标或新手标
            if ("xsb".equals(bidFlag))
            {
                t6230.xsb = T6230_F28.S;
            }
            else if ("jlb".equals(bidFlag))
            {
                t6231.F27 = T6231_F27.S;
            }
            else
            {
                t6231.F36 = T6231_F36.SINGLE;
            }
            
            if (t6231.F26.compareTo(t6231.F25) == -1)
            {
                getController().prompt(request, response, PromptLevel.WARRING, "最小投资金额不能大于最大投资额");
                processGet(request, response, serviceSession);
                return;
            }
            if (t6230.F05.compareTo(t6231.F26) == -1)
            {
                getController().prompt(request, response, PromptLevel.WARRING, "最大投资金额不能大于借款金额");
                processGet(request, response, serviceSession);
                return;
            }
            T6110_F10 t6110_F10 = userManage.getDb(userId);
            if (userType == T6110_F06.FZRR && t6110_F10 == T6110_F10.S)
            {
                throw new LogicalException("机构不允许借款");
            }
            // 本息到期一次付清
            String accDay = request.getParameter("accDay");
            if (T6230_F10.YCFQ == t6230.F10 && "S".equals(accDay))
            {
                /*if (t6230.F09 < 30) {
                	getController().prompt(request, response,
                			PromptLevel.WARRING, "本息到期，一次付清(按天计)，借款期限不能小于30天");
                	processGet(request, response, serviceSession);
                	return;
                }*/
                
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                long curDateInMills = cal.getTimeInMillis();
                cal.add(Calendar.MONTH, 36);
                long upDateInMills = cal.getTimeInMillis();
                int upDays = (int)((upDateInMills - curDateInMills) / DateHelper.DAY_IN_MILLISECONDS);
                int lowDays = IntegerParser.parse(configureProvider.getProperty(SystemVariable.JK_BXDQ_LEAST_DAYS), 1);
                if (t6230.F09 < lowDays || t6230.F09 > upDays)
                {
                    getController().prompt(request,
                        response,
                        PromptLevel.WARRING,
                        "本息到期，一次付清(按天计)，借款期限超出有效天数[" + lowDays + "," + upDays + "]");
                    processGet(request, response, serviceSession);
                    return;
                }
                t6231.F21 = T6231_F21.S;
                t6231.F22 = t6230.F09;
                t6230.F09 = 0;
            }
            else
            {
                if (t6230.F09 < 1 || t6230.F09 > 36)
                {
                    getController().prompt(request, response, PromptLevel.WARRING, "借款期限超出有效月数[1-36]");
                    processGet(request, response, serviceSession);
                    return;
                }
                t6231.F21 = T6231_F21.F;
                t6231.F22 = 0;
            }
            
            //标的图片
            Part part = request.getPart("image");
            UploadFile uploadFile = null;
            if (!(part == null || part.getContentType() == null || part.getSize() == 0))
            {
                String content = part.getHeaders("content-disposition").toString();
                String fileType = content.substring(content.lastIndexOf(".") + 1, content.length() - 2);
                if (!("jpg".equalsIgnoreCase(fileType) || "png".equalsIgnoreCase(fileType)))
                {
                    getController().prompt(request, response, PromptLevel.WARRING, "图片格式不正确，仅支持jpg、png格式。");
                    processGet(request, response, serviceSession);
                    return;
                }
                if (part.getSize() >= 1024 * 1024)
                {
                    getController().prompt(request, response, PromptLevel.WARRING, "图片大小不正确，不能大于1M。");
                    processGet(request, response, serviceSession);
                    return;
                }
                
                uploadFile = new PartFile(part);
            }
            t6230.F21 = request.getParameter("pcimage");
            bidManage.update(t6230, t6231, t6238, uploadFile);
            T6230 loan = bidManage.get(loanId);
            request.setAttribute("loan", loan);
            request.setAttribute("loanId", loanId);
            int flag = IntegerParser.parse(request.getParameter("flag"));
            if (flag == 1 && userType == T6110_F06.ZRR)
            {
                // 跳转到个人信息页面
                sendRedirect(request, response, getController().getURI(request, AddUserInfoXq.class) + "?loanId="
                    + loanId + "&userId=" + userId);
            }
            else if (flag == 1 && userType == T6110_F06.FZRR)
            {
                sendRedirect(request, response, getController().getURI(request, AddEnterpriseXm.class) + "?loanId="
                    + loanId + "&userId=" + userId);
            }
            else
            {
                getController().prompt(request, response, PromptLevel.INFO, "保存成功");
                sendRedirect(request, response, getController().getURI(request, UpdateProject.class) + "?loanId="
                    + loanId + "&userId=" + userId);
            }
        }
        catch (Throwable throwable)
        {
            if (throwable instanceof ParameterException || throwable instanceof LogicalException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                processGet(request, response, serviceSession);
            }
            else
            {
                getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
                processGet(request, response, serviceSession);
            }
        }
    }
}
