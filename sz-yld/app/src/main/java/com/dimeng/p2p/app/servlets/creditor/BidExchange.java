/*package com.dimeng.p2p.app.servlets.creditor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.account.front.service.UserInfoManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.escrow.shuangqian.service.UserManage;
import com.dimeng.p2p.modules.bid.user.service.TenderTransferManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

*//**
 * 
 * 购买债权(双乾托管)
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月11日]
 *//*
public class BidExchange extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        // 判断用户是否被拉黑或者锁定
        UserInfoManage userInfoMananage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = userInfoMananage.getUserInfo(serviceSession.getSession().getAccountId());
        
        // 用户状态非法
        if (t6110.F07 == T6110_F07.HMD)
        {
            throw new LogicalException("账号异常,请联系客服！");
        }
        
        // 债权ID
        int zcbId = IntegerParser.parse(getParameter(request, "creditorId"));
        
        // 获取用户授权情况
        UserManage userManage = serviceSession.getService(UserManage.class);
        T6119 t6119 = userManage.selectT6119();
        if (StringHelper.isEmpty(t6119.F04))
        {
            String url = getSiteDomain("/pay/shuangqian/authorize.htm");
            
            setReturnMsg(request,
                response,
                ExceptionCode.UNAUTHORIZED_EXCEPTION,
                "您尚未授权还款转账与二次分配转账，不能操作，点击确定跳转到授权页面！",
                url);
            return;
        }
        
        final TenderTransferManage transferManage = serviceSession.getService(TenderTransferManage.class);
        
        // 是否可购买债权
        transferManage.checkPurchase(zcbId);
        
        UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
        String isYuqi = userInfoManage.isYuqi();
        if (isYuqi.equals("Y"))
        {
            setReturnMsg(request, response, ExceptionCode.YU_QI_EXCEPTION, "您有逾期未还的贷款，请先还借款再操作！");
            return;
        }
        
        String location = getSiteDomain("/creditor/bidExchangeProxy.htm?creditorId=" + zcbId);
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", location);
        
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", map);
        return;
    }
}*/