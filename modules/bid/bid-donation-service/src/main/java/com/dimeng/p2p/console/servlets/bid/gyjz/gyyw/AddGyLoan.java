/*
 * 文 件 名:  AddGyLoan.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月10日
 */
package com.dimeng.p2p.console.servlets.bid.gyjz.gyyw;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.AbstractDonationServlet;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * <添加公益标>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月10日]
 */
@Right(id = "P2P_C_BID_ADD_GYBID", name = "新增", moduleId = "P2P_C_BID_GYJZ_GYYW", order = 1)
public class AddGyLoan extends AbstractDonationServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        // int userId = IntegerParser.parse(request.getParameter("userId"));
        // int loanId = IntegerParser.parse(request.getParameter("loanId"));
        //UserManage userManage = serviceSession.getService(UserManage.class);
        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
        // T6110_F06 userType = userManage.getUserType(userId);
        //T6230 t6230 = bidManage.getBidType(loanId);
        //T6234[] t6234s = bidManage.searchBidDyw(loanId);
        // request.setAttribute("t6230", t6230);
        T6211[] t6211s = gyLoanManage.getBidType();
        request.setAttribute("t6211s", t6211s);
        // request.setAttribute("userType", userType);
        //request.setAttribute("t6234s", t6234s);
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
        {
            throw new LogicalException("请不要重复提交请求！");
        }
        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int userId = IntegerParser.parse(request.getParameter("userId"));
        //ResourceProvider resourceProvider = getResourceProvider();
        try
        {
            T6242 t6242 = new T6242();
            //默认用户是平台账户
            userId = gyLoanManage.getPTID();
            
            if (userId <= 0)
            {
                throw new ParameterException("平台账号不存在，请联系管理员");
            }
            //if (tg && !userManage.isTg(userId)) {
            //   throw new ParameterException("该用户没用注册第三方托管账号(平台账户)");
            // }
            
            t6242.parse(request);
            //创建用户
            t6242.F02 = serviceSession.getSession().getAccountId();
            //借款账户,默认为平台账户
            t6242.F23 = userId;
            
            // 项目金额和起捐金额不能为0
            if (t6242.F06.compareTo(new BigDecimal(0)) < 0 || t6242.F05.compareTo(new BigDecimal(0)) < 0)
            {
                
                getController().prompt(request, response, PromptLevel.WARRING, " 项目金额和起捐金额不能小于0");
                processGet(request, response, serviceSession);
                return;
            }
            
            // 项目金额和起捐金额不能为0
            if (StringHelper.isEmpty(t6242.F03) || StringHelper.isEmpty(t6242.F22))
            {
                
                getController().prompt(request, response, PromptLevel.WARRING, " 公益标题或公益机构不能为空");
                processGet(request, response, serviceSession);
                return;
            }
            
            // 最低起捐金额不能大于项目金额
            if (t6242.F06.compareTo(t6242.F05) > 0)
            {
                
                getController().prompt(request, response, PromptLevel.WARRING, "最低起捐金额不能大于项目金额");
                processGet(request, response, serviceSession);
                return;
            }
            int id = 0;
            if (loanId > 0)
            {
                gyLoanManage.update(t6242);
            }
            else
            {
                id = gyLoanManage.add(t6242);
            }
            loanId = loanId == 0 ? id : loanId;
            request.setAttribute("loanId", loanId);
            request.setAttribute("F21", t6242.F21);
            //0: 保存,1:保存并继续
            int flag = IntegerParser.parse(request.getParameter("flag"));
            if (flag == 1)
            {
                // 跳转到倡议书信息页面
                sendRedirect(request, response, getController().getURI(request, GyLoanCys.class) + "?loanId=" + loanId
                    + "&userId=" + userId);
            }
            else
            {
                // getController().prompt(request, response, PromptLevel.WARRING,
                // "保存成功");
                // 跳转到倡议书信息页面
                sendRedirect(request, response, getController().getURI(request, GyLoanList.class));
            }
        }
        catch (Throwable throwable)
        {
            logger.error(throwable, throwable);
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
