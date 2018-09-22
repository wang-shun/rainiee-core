package com.dimeng.p2p.app.servlets.bid;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6103;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.enums.T6230_F28;
import com.dimeng.p2p.S62.enums.T6231_F33;
import com.dimeng.p2p.S63.entities.T6342;
import com.dimeng.p2p.account.front.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.TxManage;
import com.dimeng.p2p.app.service.BusinessManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.pay.service.TenderManage;
import com.dimeng.p2p.order.TenderOrderExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 投资
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月9日]
 */
public class BuyBid extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
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
        
        // 获取标ID        
        final String bidId = getParameter(request, "bidId");
        
        // 获取投资金额
        final String amount = getParameter(request, "amount");
        
        // 获取交易密码
        String tranPwd = getParameter(request, "tranPwd");
        
        // 标ID
        final int loanId = IntegerParser.parse(bidId);
        
        // 判断用户是否有逾期未还的借款
        final BigDecimal money = BigDecimalParser.parse(amount);
        final int id = IntegerParser.parse(bidId);
        // 判断投资金额是否为空
        if (StringHelper.isEmpty(bidId) || StringHelper.isEmpty(amount))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "参数错误！");
            return;
        }
        
        String isYuqi = userInfoMananage.isYuqi();
        if (isYuqi.equals("Y"))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.YU_QI_EXCEPTION, "您有逾期未还的借款，请先还借款再操作！");
            return;
        }
        
        // 获取标的扩展信息
        BidManage bidManage = serviceSession.getService(BidManage.class);
        T6231 t6231 = bidManage.getExtra(loanId);
        
        // 判断是否为APP端允许投资的标
        if (t6231.F33 == T6231_F33.PC)
        {
            throw new LogicalException("此项目仅供PC端投资");
        }
        
        // 判断是否能投标
        Bdxq bdxq = bidManage.get(id);
        BusinessManage businessManage = serviceSession.getService(BusinessManage.class);
        if (bdxq.xsb.equals(T6230_F28.S)
            && (businessManage.getXsbCount(t6110.F01) > 0 || businessManage.getZqzrCount(t6110.F01) > 0))
        {
            throw new LogicalException("感谢您的支持！此标为新手标，只有未成功投资过并且没有购买过债权的新用户才可以投标。");
        }
        
        // 获取是否我的奖励   有值则为使用
        final String userReward = getParameter(request, "userReward");
        TenderManage tenderManage = serviceSession.getService(TenderManage.class);
        if (!StringHelper.isEmpty(userReward))
        {
            // 获取用户是否已经使用过体验金
            T6103 t6103 = tenderManage.getT6103(loanId,serviceSession.getSession().getAccountId());
            
            // 获取用户是否已经使用过奖励
            T6342 t6342 = tenderManage.getT6342(loanId,serviceSession.getSession().getAccountId());
            if (null != t6103 || null != t6342)
            {
                throw new LogicalException("只能使用一次我的奖励投此标");
            }
        }
        
        // 加密交易密码
        final ConfigureProvider configureProvider = getConfigureProvider();
        //final boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
        final boolean isOpenWithPsd =
            BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        
        // 当为非托管环境且开放交易密码功能时，需要加密交易密码
        if (isOpenWithPsd)
        {
            // 交易密码处理
            TxManage txManage = serviceSession.getService(TxManage.class);
            if (!txManage.checkWithdrawPassword(tranPwd))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.TRANPASSWORD_ERROR, "请输入正确的交易密码！");
                return;
            }
            tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
        }
        
        // 获取奖励类型   experience:体验金 |hb:红包|jxq:加息券 
        String myRewardType = getParameter(request, "myRewardType");
        System.out.println("奖励类型："+myRewardType);
        // 红包使用规则
        String hbRule = getParameter(request, "hbRule");
        
        // 加息券使用规则
        String jxqRule = getParameter(request, "jxqRule");
        
        // 投资来源
        final String source = getAgentType(request);
        String usedExp ="no";
        if(!StringHelper.isEmpty(myRewardType) && myRewardType.equals("experience")){
        	if(!StringHelper.isEmpty(userReward) && userReward.equals("true")){
        		usedExp ="yes";
        	}
        }
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("source", source);
       
        prams.put("usedExp", usedExp);
        // 调用投资接口
        Map<String, String> rtnMap =
            tenderManage.bid(id, money, userReward, tranPwd, myRewardType, hbRule, jxqRule, prams);
        rtnMap.put("userReward", userReward);
        rtnMap.put("myRewardType", myRewardType);
        rtnMap.put("hbRule", hbRule);
        rtnMap.put("jxqRule", jxqRule);
        rtnMap.put("usedExp", usedExp);
        TenderOrderExecutor executor = getResourceProvider().getResource(TenderOrderExecutor.class);
        
        //余额投资订单
        executor.submit(IntegerParser.parse(rtnMap.get("orderId")), rtnMap);
        
        //确认订单
        executor.confirm(IntegerParser.parse(rtnMap.get("orderId")), rtnMap);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!");
        return;
    }
}
