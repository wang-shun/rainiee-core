package com.dimeng.p2p.app.servlets.user;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6147;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6147_F04;
import com.dimeng.p2p.account.front.service.UserManage;
import com.dimeng.p2p.account.user.service.IndexManage;
import com.dimeng.p2p.account.user.service.LetterManage;
import com.dimeng.p2p.account.user.service.SpreadManage;
import com.dimeng.p2p.account.user.service.TxManage;
import com.dimeng.p2p.account.user.service.UserBaseManage;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.entity.BankCard;
import com.dimeng.p2p.account.user.service.entity.UserBase;
import com.dimeng.p2p.account.user.service.entity.UserBaseInfo;
import com.dimeng.p2p.app.service.BusinessManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.UserInfo;
import com.dimeng.p2p.repeater.policy.RiskQuesManage;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.p2p.variables.defines.RegulatoryPolicyVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

/**
 * 用户信息
 * @author tanhui
 *
 */
public class User extends AbstractSecureServlet
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
        
        int accountId = serviceSession.getSession().getAccountId();
        UserBaseManage userBaseManage = serviceSession.getService(UserBaseManage.class);
        UserBase data = userBaseManage.getUserBase();
        IndexManage manage = serviceSession.getService(IndexManage.class);
        
        UserInfo userInfo = new UserInfo();
        UserManage userManage = serviceSession.getService(UserManage.class);
        
        String usrCustId = userManage.getUsrCustId();
        
        // 判断用户是否已签到
        UserInfoManage uManage1 = serviceSession.getService(UserInfoManage.class);
        final T6110 t6110 = uManage1.getUserInfo(accountId);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final boolean signed = (t6110.F15 == null ? false : sdf.format(new Date()).equals(sdf.format(t6110.F15)));
        
        // 用户剩余积分
        UserCenterScoreManage userCenterScore = serviceSession.getService(UserCenterScoreManage.class);
        int usableScore = userCenterScore.getUsableScore();
        userInfo.setUsableScore(usableScore);
        
        ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(request.getServletContext());
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        
        UserBaseInfo userBaseInfo = null;
        if (tg)
        {
            userBaseInfo = manage.getUserBaseInfoTx();
        }
        else
        {
            userBaseInfo = manage.getUserBaseInfo();
        }
        
        //未读消息数量
        LetterManage letterManage = serviceSession.getService(LetterManage.class);
        int letterCount = letterManage.getUnReadCount();
        userInfo.setLetterCount(letterCount);
        //银行卡数量处理
        TxManage txManage = serviceSession.getService(TxManage.class);
        BankCard[] cards = txManage.bankCards();
        userInfo.setBankCount(cards != null ? cards.length : 0);
        
        userInfo.setAccountName(data.userName);
        userInfo.setEmail(!StringHelper.isEmpty(data.emil) ? (data.emil.substring(0, 1) + "*****" + data.emil.split("@")[1])
            : "");
        userInfo.setId(accountId);
        userInfo.setIdCard(data.idCard);
        userInfo.setPhone((data.phoneNumber != null && data.phoneNumber.length() >= 11) ? (data.phoneNumber.substring(0,
            3)
            + "*****" + data.phoneNumber.substring(8, 11))
            : "");
        userInfo.setPhoneNumber(data.phoneNumber);
        userInfo.setRealName(data.name != null ? (data.name.substring(0, 1) + "**") : "");
        userInfo.setName(data.name);
        userInfo.setSafeLevel(userBaseInfo.safeLevel);
        userInfo.setUsrCustId(usrCustId == null ? "" : usrCustId);
        userInfo.setTg(tg);
        
        userInfo.setEmailVerified(userBaseInfo.email);
        userInfo.setIdcardVerified(userBaseInfo.realName);
        userInfo.setMobileVerified(userBaseInfo.phone);
        userInfo.setWithdrawPsw(userBaseInfo.withdrawPsw);
        userInfo.setSigned(signed);
        
        // 增加风险评估类型
        final Boolean isOpenRisk =
            Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
        userInfo.setOpenRisk(isOpenRisk);
        
        T6147 t6147 = null;
        if (isOpenRisk)
        {
            final RiskQuesManage riskQuesManage = serviceSession.getService(RiskQuesManage.class);
            
            // 查询用户风估类型
            t6147 = riskQuesManage.getMyRiskResult();
            
            if (null != t6147)
            {
                // 获取用户等级
                userInfo.setRiskType(t6147.F04.getChineseName());
            }
            else
            {
                // 默认为"保守型(未评估)"
                userInfo.setRiskType(T6147_F04.BSX.getChineseName().concat("(未评估)"));
            }
            
            // 查询用户剩余评估次数
            final int count = riskQuesManage.leftRiskCount();
            userInfo.setRiskTimes(count);
        }
        
        // 判断用户是否被拉黑
        if (t6110.F07 == T6110_F07.HMD)
        {
            userInfo.setHMD(true);
        }
        
        BusinessManage businessManage = serviceSession.getService(BusinessManage.class);
        if (businessManage.getXsbCount(t6110.F01) > 0 || businessManage.getZqzrCount(t6110.F01) > 0)
        {
            userInfo.setXS(false);
        }
        
        // 获取推广信息接口服务类对象
        SpreadManage spreadManage = serviceSession.getService(SpreadManage.class);
        // 获取我的邀请码(推广码)
        userInfo.setTgm(spreadManage.getMyyqNo());
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", userInfo);
        return;
    }
    
}
