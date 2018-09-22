/*
 * 文 件 名:  AddCys.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月10日
 */
package com.dimeng.p2p.console.servlets.bid.gyjz.gyyw;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.AbstractDonationServlet;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.S62.entities.T6243;
import com.dimeng.p2p.S62.enums.T6242_F11;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.p2p.repeater.donation.entity.Article;
import com.dimeng.p2p.repeater.donation.entity.GyLoan;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * <添加公益标倡议书>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月10日]
 */
public class GyLoanCys extends AbstractDonationServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        //final BidManage bidManage = serviceSession.getService(BidManage.class);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int userId = IntegerParser.parse(request.getParameter("userId"));
        //公益标业务管理
        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
        UserManage userManage = serviceSession.getService(UserManage.class);
        
        T6110_F06 userType = userManage.getUserType(userId);
        request.setAttribute("userType", userType);
        GyLoan info = gyLoanManage.getInfo(loanId);
        request.setAttribute("loan", info);
        request.setAttribute("userId", userId);
        request.setAttribute("content", info.t6243 != null ? info.t6243.F02 : "");
        
        super.processGet(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        //ResourceProvider resourceProvider = getResourceProvider();
        //ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        
        //UserManage userManage = serviceSession.getService(UserManage.class);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int userId = IntegerParser.parse(request.getParameter("userId"));
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
        {
            throw new LogicalException("请不要重复提交请求！");
        }
        //serviceSession.openTransactions();
        
        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
        
        //            Boolean tg = BooleanParser.parseObject(configureProvider
        //                    .getProperty(SystemVariable.SFZJTG));
        try
        {
            
            Article article = new Article()
            {
                
                @Override
                public String getTitle()
                {
                    return T5011_F02.ZXNS.getChineseName();
                }
                
                @Override
                public String getSummary()
                {
                    return request.getParameter("summary");
                }
                
                @Override
                public String getSource()
                {
                    return request.getParameter("source");
                }
                
                @Override
                public int getSortIndex()
                {
                    return IntegerParser.parse(request.getParameter("sortIndex"));
                }
                
                @Override
                public ArticlePublishStatus getPublishStatus()
                {
                    return ArticlePublishStatus.YFB;
                }
                
                @Override
                public UploadFile getImage()
                    throws Throwable
                {
                    /*Part part = request.getPart("image");
                    if (part == null || part.getContentType() == null
                            || part.getSize() == 0) {
                        return null;
                    }
                    return new PartFile(part);*/
                    return null;
                }
                
                @Override
                public String getContent()
                {
                    return getResourceProvider().getResource(FileStore.class).encode(request.getParameter("content"));
                }
                
                @Override
                public Timestamp publishTime()
                {
                    
                    return TimestampParser.parse(request.getParameter("publishTime"));
                }
            };
            
            T6243 t6243 = new T6243();
            t6243.F02 = article.getContent();
            //默认用户是平台账户
            userId = gyLoanManage.getPTID();
            
            if (userId <= 0)
            {
                throw new ParameterException("平台账号不存在，请联系管理员");
            }
            //if (tg && !userManage.isTg(userId)) {
            //    throw new ParameterException("该用户没用注册第三方托管账号(平台账户)");
            //}
            
            // 最低起捐金额不能大于项目金额
            if (StringHelper.isEmpty(t6243.F02))
            {
                
                getController().prompt(request, response, PromptLevel.WARRING, "倡议书内容不能为空");
                processGet(request, response, serviceSession);
                return;
            }
            T6242 t6242 = gyLoanManage.get(loanId);
            if (t6242 == null)
            {
                getController().prompt(request, response, PromptLevel.WARRING, "公益标信息不存在,请重新查看");
                // 跳转到倡议书信息页面
                sendRedirect(request, response, getController().getURI(request, GyLoanList.class));
                return;
            }
            T6243 exist = gyLoanManage.getT6243(loanId);
            t6243.F01 = loanId;
            if (exist == null)
            {
                gyLoanManage.addT6243(t6243, article);
            }
            else
            {
                gyLoanManage.updateT6243(t6243, article);
            }
            
            request.setAttribute("loanId", loanId);
            request.setAttribute("userId", userId);
            //0: 保存,1:保存并继续
            int flag = IntegerParser.parse(request.getParameter("flag"));
            if (flag == 1)
            {
                if (t6242.F11 == T6242_F11.SQZ)
                {
                    //更新标状态为待审核
                    gyLoanManage.CheckGyBid(loanId, T6242_F11.DSH, T6242_F11.SQZ);
                }
                // 跳转到倡议书信息页面
                sendRedirect(request, response, getController().getURI(request, GyLoanList.class));
                // + "?loanId=" + loanId + "&userId=" + userId);
            }
            else
            {
                getController().prompt(request, response, PromptLevel.INFO, "保存成功");
                processGet(request, response, serviceSession);
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
                super.onThrowable(request, response, throwable);
            }
        }
    }
    
}
