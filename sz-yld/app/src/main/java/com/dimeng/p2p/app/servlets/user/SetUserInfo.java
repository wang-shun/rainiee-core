package com.dimeng.p2p.app.servlets.user;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.account.user.service.IndexManage;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.account.user.service.entity.UserBaseInfo;
import com.dimeng.p2p.app.service.BusinessManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.common.enums.AttestationState;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.signature.fdd.service.FddSignatureServiceV25;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.p2p.variables.defines.RealNameAuthVarivale;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.nciic.NciicVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

/**
 * 用户信息
 * @author tanhui
 */
public class SetUserInfo extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        //实名认证
        String name = getParameter(request, "name");
        String idcard = getParameter(request, "idCard");
        
        // 身份证号小写x转换成大写X
        idcard = idcard.toUpperCase(Locale.ENGLISH);
        
        Safety safety = safetyManage.get();
        if (!StringHelper.isEmpty(safety.isIdCard))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.IDCARD_NAME_EXIST_ERRROR, "您的账号已通过实名认证，请不要重复认证!");
            return;
        }
        
        // 判断修改实名认证信息时，该身份证是否正在注册第三方账户
        if (safetyManage.isAuthingUpdate())
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "正在第三方注册账户，不能修改实名认证信息!");
            return;
        }
        
        // 判断是否需要实名认证
        final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        boolean enable = BooleanParser.parse(configureProvider.getProperty(NciicVariable.ENABLE));
        
        // 不启用实名认证，直接返回true
        boolean is = false;
        
        // 获取来源
        final String source = getAgentType(request);
        
        if (!enable)
        {
            is = true;
        }
        else if (!StringHelper.isEmpty(idcard) && !StringHelper.isEmpty(name))
        {
            /*NciicManage nic = serviceSession.getService(NciicManage.class);
            is = nic.check(idcard, name, source, serviceSession.getSession().getAccountId());*/
        }
        else
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "参数错误！");
            return;
        }
        
        // 实名认证通过
        if (is)
        {
            if (!safetyManage.isIdcard(idcard))
            {
                safetyManage.updateNameSFZRR(name, idcard, AttestationState.YYZ.name(), T6110_F06.ZRR);
                
                // 新用户生日当天注册并进行实名认证后直接送生日登录红包和加息券
                Session session =
                    getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
                ActivityCommon activityCommon = serviceSession.getService(ActivityCommon.class);
                int userId = session.getAccountId();
                activityCommon.sendActivity(userId, T6340_F03.redpacket.name(), T6340_F04.birthday.name());
                activityCommon.sendActivity(userId, T6340_F03.interest.name(), T6340_F04.birthday.name());
                activityCommon.sendActivity();
                
                boolean is_realNameAuth =
                    Boolean.parseBoolean(configureProvider.getProperty(RealNameAuthVarivale.IS_REALNAMEAUTH));
                if (is_realNameAuth)
                {
                    // 实名认证通过时间
                    BusinessManage businessManage = serviceSession.getService(BusinessManage.class);
                    businessManage.updateT6198F06(source);
                }
                
                boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
                if (is_mall)
                {
                    // 实名认证赠送积分
                    SetScoreManage setScoreManage = serviceSession.getService(SetScoreManage.class);
                    setScoreManage.giveScore(null, T6106_F05.realname, null);
                }
                //法大大电子签章- CA证书申请
                FddSignatureServiceV25 fdd = serviceSession.getService(FddSignatureServiceV25.class);
                fdd.syncPersonAuto(userId);
            }
            else
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.IDCARD_NAME_EXIST_ERRROR, "该身份证已认证过");
                return;
            }
        }
        else
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.IDCARD_NAME_ERRROR, "实名认证失败");
            return;
        }
        
        //是否全部认证
        boolean isAllVerify = true;
        IndexManage manage = serviceSession.getService(IndexManage.class);
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
        if (!userBaseInfo.realName)
        {
            isAllVerify = false;
        }
        if (!userBaseInfo.phone)
        {
            isAllVerify = false;
        }
        if (!userBaseInfo.email)
        {
            isAllVerify = false;
        }
        if (!tg && !userBaseInfo.withdrawPsw)
        {
            isAllVerify = false;
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", isAllVerify);
        return;
    }
}
