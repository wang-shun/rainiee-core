/*
 * 文 件 名:  GyLoanBid.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月25日
 */
package com.dimeng.p2p.app.servlets.bid;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.account.user.service.TxManage;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.bid.domain.Prefix;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
//import com.dimeng.p2p.escrow.shuangqian.service.UserManage;
import com.dimeng.p2p.modules.bid.pay.service.DonationService;
import com.dimeng.p2p.order.DonationExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 公益标投资
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月25日]
 */
public class GyLoanBid extends AbstractSecureServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 8982822757890348239L;
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
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
        
        // 获取页面参数
        final BigDecimal amount = BigDecimalParser.parse(getParameter(request, "amount"));
        final int loanId = IntegerParser.parse(getParameter(request, "loanId"));
        String tranPwd = getParameter(request, "tranPwd");
        
        ResourceProvider resourceProvider = getResourceProvider();
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        boolean isOpenWsd = BooleanParser.parseObject(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        if (isOpenWsd)
        {
        	// 交易密码处理
            TxManage txManage = serviceSession.getService(TxManage.class);
            // 判断交易密码是否为空
            if (StringHelper.isEmpty(tranPwd)) 
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.TRANPASSWORD_ERROR, "请输入交易密码！");
                return;
			}
            
            if (!txManage.checkWithdrawPassword(tranPwd))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.TRANPASSWORD_ERROR, "请输入正确的交易密码！");
                return;
            }
            tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
        }
        
        if (tg)
        {
            // 判断是否为双乾托管，如果为双乾托管，则需要校验是否二次授权
            final String prefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
            
            // 当配置为空时，需要提示用户
            if (StringHelper.isEmpty(prefix))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.PREFIX_IS_EMPTY, "请先配置托管方式！");
            }
            
//            if (Prefix.SHUANGQIAN.name().equals(prefix.toUpperCase(Locale.ENGLISH)))
//            {
//                // 获取用户授权情况
//                UserManage userManage = serviceSession.getService(UserManage.class);
//                T6119 t6119 = userManage.selectT6119();
//                if (StringHelper.isEmpty(t6119.F04))
//                {
//                    String url = getSiteDomain("/pay/service/shuangqian/authorize.htm");
//                    Map<String, String> map = new HashMap<String, String>();
//                    map.put("url", url);
//                    
//                    setReturnMsg(request,
//                        response,
//                        ExceptionCode.UNAUTHORIZED_EXCEPTION,
//                        "您尚未授权还款转账与二次分配转账，不能操作，点击确定跳转到授权页面！",
//                        map);
//                    return;
//                }
//            }
            
            String location = getSiteDomain("/bid/gyLoanBidProxy.htm?amount=" + amount + "&loanId=" + loanId);
            
            Map<String, String> map = new HashMap<String, String>();
            map.put("url", location);
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", map);
            return;
        } else {
            DonationService tenderManage = serviceSession.getService(DonationService.class);
            final int orderId = tenderManage.bid(loanId, amount, tranPwd);
            DonationExecutor executor = getResourceProvider().getResource(DonationExecutor.class);
            executor.submit(orderId, null);
            executor.confirm(orderId, null);
            
            // 返回页面信息
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "success");
            return;
        }
    }
    
}
