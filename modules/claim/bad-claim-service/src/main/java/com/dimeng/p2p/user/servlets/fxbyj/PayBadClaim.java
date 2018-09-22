/*
 * 文 件 名:  PayBadClaim.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年6月14日
 */
package com.dimeng.p2p.user.servlets.fxbyj;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6265;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.common.entities.Safety;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.order.BadClaimAdvanceExecutor;
import com.dimeng.p2p.repeater.claim.SubscribeBadClaimManage;
import com.dimeng.p2p.service.ContractManage;
import com.dimeng.p2p.service.UserInfoManage;
import com.dimeng.p2p.user.servlets.AbstractUserBadClaimServlet;
import com.dimeng.p2p.user.servlets.thread.BadClaimContractPreservationThread;
import com.dimeng.p2p.variables.defines.BadClaimVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 购买不良债权
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月14日]
 */
public class PayBadClaim extends AbstractUserBadClaimServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 5145130403481024875L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
        {
            throw new LogicalException("请不要重复提交请求！");
        }
        ResourceProvider resourceProvider = getResourceProvider();
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        SubscribeBadClaimManage service = serviceSession.getService(SubscribeBadClaimManage.class);
        BadClaimAdvanceExecutor badClaimAdvanceExecutor =
            getResourceProvider().getResource(BadClaimAdvanceExecutor.class);
        ContractManage contractManage = serviceSession.getService(ContractManage.class);
        int blzqId = IntegerParser.parse(request.getParameter("blzqId"));
        
        Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
        Boolean isOpenWsd =
            BooleanParser.parseObject(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        if (isOpenWsd)
        {
            String tranPwd = request.getParameter("tranPwd");
            tranPwd = RSAUtils.decryptStringByJs(tranPwd);
            tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
            if (StringHelper.isEmpty(tranPwd))
            {
                throw new LogicalException("输入正确的交易密码！");
            }
            UserInfoManage safetyManage = serviceSession.getService(UserInfoManage.class);
            Safety sa = safetyManage.get();
            if (!tranPwd.equals(sa.txpassword))
            {
                throw new LogicalException("输入正确的交易密码！");
            }
        }
        //添加不良债权转让订单
        List<Integer> orderIds = service.addOrder(blzqId);
        //债权价值
        BigDecimal creditPrice = service.getCreditPrice(blzqId);
        //逾期天数
        int overdueDays = service.getOverdueDays(blzqId);
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("blzqId", String.valueOf(blzqId));
        params.put("creditPrice", String.valueOf(creditPrice));
        params.put("overdueDays", String.valueOf(overdueDays));
        
        params.put("orderIdNum", String.valueOf(orderIds.size()));
        
        if (orderIds != null)
        {
            for (Integer orderId : orderIds)
            {
                badClaimAdvanceExecutor.submit(orderId, params);
                if (!tg)
                {
                    badClaimAdvanceExecutor.confirm(orderId, params);
                }
            }
            
            //合同保全
            Boolean isAllowBadClaim =
                Boolean.parseBoolean(configureProvider.getProperty(BadClaimVariavle.IS_ALLOW_BADCLAIM_TRANSFER));
            if (isAllowBadClaim)
            {
                T6265 t6265 = contractManage.selectT6265(blzqId);
                if (t6265 != null)
                {
                    int blzqzrId = t6265.F01;
                    //执行合同保全线程
                    BadClaimContractPreservationThread bccpThread = new BadClaimContractPreservationThread(blzqzrId);
                    Thread thread = new Thread(bccpThread);
                    thread.setName("不良债权转让合同保全线程类!");
                    thread.start();
                }
            }
        }
        service.writeFrontLog(FrontLogType.GMBLZQ.getName(), "购买不良债权");
        getController().prompt(request, response, PromptLevel.INFO, "恭喜你，购买成功");
        sendRedirect(request, response, configureProvider.format(URLVariable.USER_BLZQZR) + "?type=2");
        
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        ResourceProvider resourceProvider = getResourceProvider();
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        getResourceProvider().log(throwable);
        if (throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
            sendRedirect(request, response, configureProvider.format(URLVariable.USER_BLZQZR));
        }
        else if (throwable instanceof ParameterException || throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, configureProvider.format(URLVariable.USER_BLZQZR));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
    
}
