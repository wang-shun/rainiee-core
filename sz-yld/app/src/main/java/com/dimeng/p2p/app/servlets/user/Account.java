package com.dimeng.p2p.app.servlets.user;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6342_F04;
import com.dimeng.p2p.account.user.service.IndexManage;
import com.dimeng.p2p.account.user.service.MyRewardManage;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.UserManage;
import com.dimeng.p2p.account.user.service.entity.TenderAccount;
import com.dimeng.p2p.account.user.service.entity.UserBaseInfo;
import com.dimeng.p2p.app.service.BusinessManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.AccountInfo;
import com.dimeng.p2p.modules.bid.user.service.WdjkManage;
import com.dimeng.p2p.repeater.score.MallChangeManage;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;

/**
 * 
 * 我的账户信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月23日]
 */
public class Account extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2909537510687345938L;
    
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
        
        IndexManage manage = serviceSession.getService(IndexManage.class);
        
        // 未使用加息券
        int unUserJxqCount = getUnUserJxqCount(serviceSession);
        
        // 未使用的红包金额
        String hbJe = getHbAmount(serviceSession);
        
        // 账户信息
        UserBaseInfo userBaseInfo = manage.getUserBaseInfo();
        
        // 登录成功
        int accountId = serviceSession.getSession().getAccountId();
        UserManage userManage = serviceSession.getService(UserManage.class);
        TenderAccount tenderAccount = manage.getTenderAccount();
        String usrCustId = userManage.getUsrCustId();
        
        // 判断用户是否已签到
        UserInfoManage uManage1 = serviceSession.getService(UserInfoManage.class);
        final T6110 t6110 = uManage1.getUserInfo(accountId);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final boolean signed = (t6110.F15 == null ? false : sdf.format(new Date()).equals(sdf.format(t6110.F15)));
        
        final ConfigureProvider configureProvider = getConfigureProvider();
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        
        AccountInfo accountfo = new AccountInfo();
        
        // 账户ID
        accountfo.setId(accountId);
        
        // 用户名称
        accountfo.setUserName(userManage.getAccountName());
        
        // 用户头像
        accountfo.setPhoto(userBaseInfo.photo);
        
        // 账户安全等级
        accountfo.setSafeLevel(userBaseInfo.safeLevel);
        
        // 账户手机号码
        accountfo.setMobileVerified(userBaseInfo.phone);
        
        // 账户实名认证姓名(带*号)
        accountfo.setIdcardVerified(userBaseInfo.realName);
        
        // 账户是否设置交易密码
        accountfo.setWithdrawPsw(userBaseInfo.withdrawPsw);
        
        // 账户是否绑定邮箱标识
        accountfo.setEmailVerified(userBaseInfo.email);
        
        // 投资资产
        BigDecimal tzzc = tenderAccount.yxzc.add(tenderAccount.sbzc);
        
        // 账户金额 = 冻结金额 + 账户余额
        BigDecimal zhje = userBaseInfo.freezeFunds.add(userBaseInfo.balance);
        
        // 账户总金额 = 账户金额 + 待收本金 + 待收利息
        accountfo.setTotalAmount(String.valueOf(zhje.add(manage.getDsbj()).add(manage.getDslx())));
        
        // 冻结金额
        accountfo.setFreezeAmount(String.valueOf(userBaseInfo.freezeFunds));
        
        // 账户净资产 = 账户金额 + 理财资产 - 借款负债
        accountfo.setMerelyAmount(String.valueOf(zhje.add(tzzc).subtract(manage.getLoanAmount())));
        
        // 用户理财资产
        accountfo.setInvestAmount(String.valueOf(tzzc));
        
        // 用户借款负债
        accountfo.setLoanAmount(String.valueOf(manage.getLoanAmount()));
        
        // 用户待还总金额
        accountfo.setAlsoAmount(String.valueOf((manage.getUnpayTotal())));
        
        // 已赚总金额
        accountfo.setEarnAmount(String.valueOf(tenderAccount.yzzje));
        
        // 账户可用余额
        accountfo.setOverAmount(String.valueOf(userBaseInfo.balance));
        
        // 第三方注册标识
        accountfo.setUsrCustId(usrCustId == null ? "" : usrCustId);
        
        // 增加体验金
        accountfo.setExperienceAmount(String.valueOf(userBaseInfo.experienceAmount));
        
        // 未使用加息券张数
        accountfo.setUnUserJxqCount(unUserJxqCount);
        
        // 未使用红包金额
        accountfo.setHbJe(hbJe);
        
        // 是否为托管
        accountfo.setTg(tg);
        
        // 是否已签到
        accountfo.setSigned(signed);
        
        // 用户剩余积分
        UserCenterScoreManage userCenterScore = serviceSession.getService(UserCenterScoreManage.class);
        int usableScore = userCenterScore.getUsableScore();
        accountfo.setUsableScore(usableScore);
        
        WdjkManage wdjkManage = serviceSession.getService(WdjkManage.class);
        // 我的借款
        accountfo.setCountMoney(String.valueOf(wdjkManage.getMyLoanCount().countMoney));
       
        BusinessManage businessManage = serviceSession.getService(BusinessManage.class);
        // 我的投资 = 还款中 + 投资中 + 已结清
        accountfo.setTzCount(businessManage.getHkzZqCount() + businessManage.getTzzZqCount() + businessManage.getYjqZqCount());
        
        // 债权数量 = 转让中 + 已转出 + 已转入
        accountfo.setZqCount(businessManage.getZrzZqCount() + businessManage.getYzcZqCount() + businessManage.getYzrZqCount());
        
        // 我的订单数量
        accountfo.setOrderCount(businessManage.getOrderCount());
        
        // 我的购物车数量
        MallChangeManage mallChangeManage = serviceSession.getService(MallChangeManage.class);
        accountfo.setCarCount(mallChangeManage.queryCarNum());
        
        // 风险保证金
        accountfo.setFxbzj(t6110.F10 == T6110_F10.S ? String.valueOf(userBaseInfo.fxbzj) : "");
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", accountfo);
        return;
    }
    
    /**
     * 获取未使用加息券数量
     * 
     * @param serviceSession 会话session
     * @return 未使用加息券数量
     * @throws Throwable 异常信息
     */
    private int getUnUserJxqCount(final ServiceSession serviceSession)
        throws Throwable
    {
        MyRewardManage myservice = serviceSession.getService(MyRewardManage.class);
        
        Map<String, Object> params = new HashMap<String, Object>();
        
        // 设置查询类型 加息券
        params.put("type", T6340_F03.interest);
        
        // 设置查询状态 未使用
        params.put("useStatus", T6342_F04.WSY);
        
        // 未使用加息券
        int unUserJxqCount = myservice.getJxqCount(params);
        
        return unUserJxqCount;
    }
    
    /**
     * 获取未使用红包金额
     * 
     * @param serviceSession 会话session
     * @return 未使用红包金额
     * @throws Throwable 异常信息
     */
    private String getHbAmount(final ServiceSession serviceSession)
        throws Throwable
    {
        MyRewardManage myservice = serviceSession.getService(MyRewardManage.class);
        
        Map<String, Object> paramses = new HashMap<String, Object>();
        // 设置查询类型 红包
        paramses.put("type", T6340_F03.redpacket);
        
        // 设置查询状态  未使用
        paramses.put("useStatus", T6342_F04.WSY);
        
        // 未使用的红包金额
        BigDecimal hbJe = myservice.getHbAmount(paramses);
        
        return String.valueOf(hbJe);
    }
    
}
