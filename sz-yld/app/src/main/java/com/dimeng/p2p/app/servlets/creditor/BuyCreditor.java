package com.dimeng.p2p.app.servlets.creditor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.account.user.service.IndexManage;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.TxManage;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.account.user.service.entity.UserBaseInfo;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.user.service.TenderTransferManage;
import com.dimeng.p2p.modules.bid.user.service.ZqzrSignaManage;
import com.dimeng.p2p.order.TenderExchangeExecutor;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 购买债券
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月10日]
 */
public class BuyCreditor extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 904758214711922809L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
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
        final int zcbId = IntegerParser.parse(getParameter(request, "creditorId"));
        
        String tranPwd = getParameter(request, "tranPwd");
        
        // 交易密码处理
        IndexManage manage = serviceSession.getService(IndexManage.class);
        UserBaseInfo userBaseInfo = manage.getUserBaseInfo();
        if (!userBaseInfo.withdrawPsw)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.NO_TRANPASSWORD_ERROR, "交易密码未设置！");
            return;
        }
        
        // 交易密码处理
        TxManage txManage = serviceSession.getService(TxManage.class);
        if (!txManage.checkWithdrawPassword(tranPwd))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.TRANPASSWORD_ERROR, "交易密码错误！");
            return;
        }
        
        TenderTransferManage transferManage = serviceSession.getService(TenderTransferManage.class);
        ZqzrSignaManage zqzrSignaManage = serviceSession.getService(ZqzrSignaManage.class);
        int orderId = transferManage.purchase(zcbId);
        TenderExchangeExecutor executor = getResourceProvider().getResource(TenderExchangeExecutor.class);
        executor.submit(orderId, null);
        executor.confirm(orderId, null);
        
        //生成电子签章
        zqzrSignaManage.setZqzrSigna(orderId);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!");
        return;
    }
}
