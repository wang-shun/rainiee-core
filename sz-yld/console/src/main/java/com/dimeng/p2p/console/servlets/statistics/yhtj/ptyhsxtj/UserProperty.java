/*
 * 文 件 名:  UserProperty.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年6月7日
 */
package com.dimeng.p2p.console.servlets.statistics.yhtj.ptyhsxtj;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.statistics.AbstractStatisticsServlet;
import com.dimeng.p2p.modules.statistics.console.service.UserDataManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.AgeDistributionEntity;
import com.dimeng.p2p.modules.statistics.console.service.entity.InvestmentLoanEntity;

/**
 * <平台用户属性统计>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月7日]
 */
@Right(id = "P2P_C_STATISTICS_USERPROPERTY", name = "平台用户属性统计", moduleId = "P2P_C_STATISTICS_YHTJ_YHSXTJ")
public class UserProperty extends AbstractStatisticsServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        UserDataManage manage = serviceSession.getService(UserDataManage.class);
        //投资/借款用户分布
        InvestmentLoanEntity investmentLoanEntity = manage.getInvestmentLoanData();
        //平台用户年龄分布
        AgeDistributionEntity[] userAgeEntity = manage.getUserAgeData();
        //平台用户性别分布
        AgeDistributionEntity[] userSexEntity = manage.getUserSexData();
        //平台用户注册终端分布
        AgeDistributionEntity[] userRegisterSourceEntity = manage.getUserRegisterSourceData();
        //平台投资终端分布
        AgeDistributionEntity[] userInvestSourceEntity = manage.getUserInvestSourceData();
        request.setAttribute("investmentLoanEntity", investmentLoanEntity);
        request.setAttribute("userAgeEntity", userAgeEntity);
        request.setAttribute("userSexEntity", userSexEntity);
        request.setAttribute("userRegisterSourceEntity", userRegisterSourceEntity);
        request.setAttribute("userInvestSourceEntity", userInvestSourceEntity);
        forwardView(request, response, getClass());
    }
}
