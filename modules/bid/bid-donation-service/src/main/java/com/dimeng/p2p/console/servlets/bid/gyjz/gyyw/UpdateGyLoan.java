/*
 * 文 件 名:  UpdateGyLoan.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月10日
 */
package com.dimeng.p2p.console.servlets.bid.gyjz.gyyw;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.AbstractDonationServlet;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <更新公益标>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月10日]
 */
@Right(id = "P2P_C_UPDATE_GYBID",  name = "修改",moduleId="P2P_C_BID_GYJZ_GYYW",order=3)
public class UpdateGyLoan extends AbstractDonationServlet
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
       
        //公益标业务管理
        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
        int userId = gyLoanManage.getPTID();//IntegerParser.parse(request.getParameter("userId"));
        UserManage userManage = serviceSession.getService(UserManage.class);
        
        T6110_F06 userType = userManage.getUserType(userId);
        request.setAttribute("userType", userType);
        T6242 t6242 = gyLoanManage.get(loanId);
        request.setAttribute("loan", t6242);
        request.setAttribute("userId", userId);
        T6211[] t6211s = gyLoanManage.getBidType();
        
        request.setAttribute("t6211s", t6211s);
        super.processGet(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
       // ResourceProvider resourceProvider = getResourceProvider();
       /* ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);*/
        /*final BidManage bidManage = serviceSession.getService(BidManage.class);
        UserManage userManage = serviceSession.getService(UserManage.class);*/
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int userId = IntegerParser.parse(request.getParameter("userId"));
        if(!FormToken.verify(serviceSession.getSession(), 
            request.getParameter(FormToken.parameterName()))) {
            throw new LogicalException("请不要重复提交请求！");
        }
            //serviceSession.openTransactions();
         
            GyLoanManage gyLoanManage = serviceSession
                    .getService(GyLoanManage.class);
            
           /* Boolean tg = BooleanParser.parseObject(configureProvider
                    .getProperty(SystemVariable.SFZJTG));*/
            try {
                T6242 t6242 = new T6242();
                //默认用户是平台账户
                  userId = gyLoanManage.getPTID();
               
                if (userId <= 0) {
                    throw new ParameterException("平台账号不存在，请联系管理员");
                }
                //平台账号没有托管号
//                if (tg && !userManage.isTg(userId)) {
//                    throw new ParameterException("该用户没用注册第三方托管账号(平台账户)");
//                }
        
                t6242.parse(request);
               
                // 最低起捐金额不能大于项目金额
                if (t6242.F06.compareTo(t6242.F05)  > 0) {
                    
                        getController().prompt(
                                request,
                                response,
                                PromptLevel.WARRING,
                                "最低起捐金额不能大于项目金额");
                        processGet(request, response, serviceSession);
                        return;
                } 
               T6242 extist = gyLoanManage.get(loanId);
               if(null==extist)
               {
                   getController().prompt( request,response,PromptLevel.WARRING,
                       "公益标信息不存在,不能修改");
                   processGet(request, response, serviceSession);
                   return;
               }
               //不是创建用户不能修改
//               if(extist.F02 != serviceSession.getSession().getAccountId())
//               {
//                   getController().prompt( request,response,PromptLevel.WARRING,
//                       "不是当前用户创建的公益标,不能修改");
//                   processGet(request, response, serviceSession);
//                   return;
//               }
               t6242.F01= loanId;
                gyLoanManage.update(t6242);
               
                request.setAttribute("loanId", loanId);
                request.setAttribute("F21", extist.F21);
                //0: 保存,1:保存并继续
                int flag = IntegerParser.parse(request.getParameter("flag"));
                if (flag == 1 ) {
                    // 跳转到倡议书信息页面
                    sendRedirect(request, response,
                            getController().getURI(request, GyLoanCys.class)
                                    + "?loanId=" + loanId + "&userId=" + userId);
                } else {
                    getController().prompt(request, response, PromptLevel.INFO,
                            "保存成功");
                    processGet(request, response, serviceSession);
                }
            } catch (Throwable throwable) {
                if (throwable instanceof ParameterException
                        || throwable instanceof LogicalException) {
                    getController().prompt(request, response, PromptLevel.WARRING,
                            throwable.getMessage());
                    processGet(request, response, serviceSession);
                } else {
                    super.onThrowable(request, response, throwable);
                }
            }
    }
    
}
