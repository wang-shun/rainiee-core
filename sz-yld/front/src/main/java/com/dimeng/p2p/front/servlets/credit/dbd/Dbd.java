package com.dimeng.p2p.front.servlets.credit.dbd;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S51.enums.T5123_F03;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6120_F05;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6236;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6231_F35;
import com.dimeng.p2p.S62.enums.T6236_F04;
import com.dimeng.p2p.account.front.service.UserInfoManage;
import com.dimeng.p2p.account.front.service.UserManage;
import com.dimeng.p2p.account.front.service.entity.UserInfo;
import com.dimeng.p2p.front.servlets.credit.AbstractCreditServlet;
import com.dimeng.p2p.modules.account.console.service.GrManage;
import com.dimeng.p2p.modules.account.console.service.entity.Rzxx;
import com.dimeng.p2p.modules.base.front.service.CreditLevelManage;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.BusinessManage;
import com.dimeng.p2p.repeater.guarantor.ApplyGuarantorManage;
import com.dimeng.p2p.service.SafetymsgViewManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.DateHelper;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

public class Dbd extends AbstractCreditServlet
{
    
    private static final long serialVersionUID = -3655351845904847543L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        //担保贷必须有担保码传过来
        String gCode = request.getParameter("gCode");
        if (StringHelper.isEmpty(gCode))
        {
            getController().prompt(request, response, PromptLevel.ERROR, "担保人编号不能为空");
            getController().sendRedirect(request, response, getController().getViewURI(request, Dbd.class));
            return;
        }
        BusinessManage manage = serviceSession.getService(BusinessManage.class);
        UserInfoManage mge = serviceSession.getService(UserInfoManage.class);
        
        ResourceProvider resourceProvider = getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        
        // 查询当前用户是否可以新建借款
        
        if (!mge.isSmrz() || !mge.getYhrzxx())
        {
            boolean isOpenWithPsd =
                BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
            String errorMessage = "实名认证，手机号码认证";
            if (isOpenWithPsd)
            {
                errorMessage = "实名认证，手机号码认证，交易密码设置";
            }
            //跳转到实名认证页面
            SafetymsgViewManage safeManage = serviceSession.getService(SafetymsgViewManage.class);
            String info =
                errorMessage + "，前往<a href=\"" + configureProvider.format(safeManage.getSafetymsgView())
                    + "\" class=\"blue\">个人基础信息</a>进行认证。";
            getController().prompt(request, response, PromptLevel.ERROR, info);
            getController().sendRedirect(request, response, getController().getViewURI(request, Dbd.class));
            return;
        }
        
        // 认证项是否通过
        GrManage personalManage = serviceSession.getService(GrManage.class);
        Rzxx[] t6120s = personalManage.getRzxx(serviceSession.getSession().getAccountId());
        StringBuffer rzMsg = new StringBuffer("");
        if (t6120s != null && t6120s.length > 0)
        {
            for (Rzxx rzxx : t6120s)
            {
                if (T5123_F03.S.name().equals(rzxx.mustRz.name()) && !T6120_F05.TG.equals(rzxx.status))
                {
                    rzMsg.append(rzxx.lxmc + ",");
                }
            }
        }
        
        //只有自然人需要认证项
        if (!StringHelper.isEmpty(rzMsg.toString()))
        {
            // 跳转到认证信息页面
            String info =
                "认证信息中的必要认证没有通过，前往<br/><a href=\"" + configureProvider.format(URLVariable.USER_RZXX)
                    + "\" class=\"blue\">认证信息</a>进行认证。";
            getController().prompt(request, response, PromptLevel.ERROR, info);
            getController().sendRedirect(request, response, getController().getViewURI(request, Dbd.class));
            return;
        }
        
        //如果是托管，则应该先开通托管账号
        UserManage userManage = serviceSession.getService(UserManage.class);
        String usrCustId = userManage.getUsrCustId();
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        if (tg && StringHelper.isEmpty(usrCustId))
        {//托管模式未第三方开户则统一跳转到开户引导页
            String openEscrowGuide = configureProvider.format(URLVariable.OPEN_ESCROW_GUIDE);
            String errorMessage = "申请担保贷必须先在第三方注册托管账户，";
            //开户引导页
            String info = errorMessage + "<a id='to_validate' href=\"" + openEscrowGuide + "\" class=\"blue\">立即注册</a>";
            getController().prompt(request, response, PromptLevel.ERROR, info);
            getController().sendRedirect(request, response, getController().getViewURI(request, Dbd.class));
            return;
        }
        
        String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
        
        if (tg && StringHelper.isEmpty(usrCustId))
        {
            //你还没在第三方注册账号
            String info =
                "您还没在第三方托管注册账号，请<a id='to_validate' href=\"" + configureProvider.format(URLVariable.OPEN_ESCROW_GUIDE)
                    + "\" class=\"red\">立即注册</a>";
            getController().prompt(request, response, PromptLevel.ERROR, info);
            getController().sendRedirect(request, response, getController().getViewURI(request, Dbd.class));
            return;
        }
        //双乾托管需要授权还款转账及二次分配转账
        if (tg && "shuangqian".equals(escrow))
        {
            T6119 t6119 = userManage.getUsrAute();
            //获取用户授权情况
            if (t6119 == null || "0".equals(t6119.F04))
            {
                String info =
                    "该用户尚未授权还款转账与二次分配转账，不能发标！<a id='to_validate' style=\"color:red\" href=\""
                        + configureProvider.format(URLVariable.AUTHORIZE_URL) + "\">点击授权</a>";
                getController().prompt(request, response, PromptLevel.ERROR, info);
                getController().sendRedirect(request, response, getController().getViewURI(request, Dbd.class));
                return;
            }
        }
        
        //开启必须邮箱认证时，用户未验证的，提示其到个人基础信息认证
        boolean isMustEmail = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_EMAIL));
        if (isMustEmail && !mge.isEmail())
        {
            SafetymsgViewManage safeManage = serviceSession.getService(SafetymsgViewManage.class);
            String info =
                "申请担保贷必须必须认证邮箱，请您到<a id='to_validate' href=\""
                    + configureProvider.format(safeManage.getSafetymsgView()) + "\" class=\"blue\">个人基础信息</a>设置。";
            getController().prompt(request, response, PromptLevel.ERROR, info);
            getController().sendRedirect(request, response, getController().getViewURI(request, Dbd.class));
            return;
        }
        
        //只有自然人账号能够申请担保贷
        T6110_F06 userType = userManage.getUserType(serviceSession.getSession().getAccountId());
        if (userType == T6110_F06.FZRR)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "非自然人账户不能申请担保贷");
            getController().sendRedirect(request, response, getController().getViewURI(request, Dbd.class));
            return;
        }
        
        if (mge.isYuqi().equals("Y"))
        {
            //当前用户存在逾期不能借款
            getController().prompt(request, response, PromptLevel.ERROR, "您存在逾期借款，请先进行还款");
            getController().sendRedirect(request, response, getController().getViewURI(request, Dbd.class));
            return;
        }
        
        // 通过ID和状态查询，当前ID是否为新建类型，不是则提示以存在不能新建。
        if (mge.isBid())
        {
            //当前用户存在待审核的标不能借款
            getController().prompt(request, response, PromptLevel.ERROR, "您已申请过其他产品，不能再申请此产品");
            getController().sendRedirect(request, response, getController().getViewURI(request, Dbd.class));
            return;
        }
        
        //判断年龄是否符合
        int accountId = serviceSession.getSession().getAccountId();
        UserInfo userInfo = mge.search(accountId);
        int minAge = Integer.parseInt(configureProvider.getProperty(SystemVariable.CREDIT_LOAN_AGE_MIN));
        int maxAge = Integer.parseInt(configureProvider.getProperty(SystemVariable.CREDIT_LOAN_AGE_MAX));
        if (userInfo.age < minAge || userInfo.age > maxAge)
        {
            //当前用户年龄不符合规范
            getController().prompt(request, response, PromptLevel.ERROR, "您的年龄不在申请条件范围之内，不能申请。");
            getController().sendRedirect(request, response, getController().getViewURI(request, Dbd.class));
            return;
        }
        
        //判断是否网签
        boolean isNetSigned = mge.isNetSigned();
        if (!isNetSigned)
        {
            getController().prompt(request,
                response,
                PromptLevel.ERROR,
                "申请担保贷必须先网签合同认证，<br/>请您到<a id='to_validate' href=\""
                    + configureProvider.format(URLVariable.USER_NETSIGN_URL) + "\" class=\"blue\">网签合同</a>设置。");
            forwardView(request, response, getClass());
            return;
        }
        
        //判断该担保码是否存在且是成功状态
        ApplyGuarantorManage applyGuarantorManage = serviceSession.getService(ApplyGuarantorManage.class);
        int guarantorId = applyGuarantorManage.getGuarantId(gCode, true);
        BidManage bidManage = serviceSession.getService(BidManage.class);
        if (guarantorId == 0 || bidManage.isUserHasYQ(guarantorId))
        {
            getController().prompt(request, response, PromptLevel.ERROR, "您所输入的担保人编号错误或该担保人目前暂时不能提供担保业务");
            forwardView(request, response, getClass());
            return;
        }
        UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = userInfoManage.getUserInfo(guarantorId);
        if (T6110_F07.SD == t6110.F07 || T6110_F07.HMD == t6110.F07)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "该担保方已被锁定或已被拉黑");
            forwardView(request, response, getClass());
            return;
        }
        if (guarantorId == serviceSession.getSession().getAccountId())
        {
            getController().prompt(request, response, PromptLevel.ERROR, "不能填写自己的担保编号");
            getController().sendRedirect(request, response, getController().getViewURI(request, Dbd.class));
            return;
        }
        T6230 t6230 = new T6230();
        T6231 t6231 = new T6231();
        t6231.F07 = IntegerParser.parse(request.getParameter("xian"));
        t6231.F08 = request.getParameter("t6231_f08");
        t6231.F09 = request.getParameter("t6231_f09");
        t6231.F16 = request.getParameter("t6231_f16");
        t6230.parse(request);
        CreditLevelManage creditLevelManage = serviceSession.getService(CreditLevelManage.class);
        int xyId = creditLevelManage.getId(userInfoManage.getXyjf(serviceSession.getSession().getAccountId()));
        t6230.F23 = xyId;
        t6230.F11 = T6230_F11.S;
        
        // 本息到期一次付清
        String accDay = request.getParameter("accDay");
        boolean flag = true;
        if (T6230_F10.YCFQ == t6230.F10 && "S".equals(accDay))
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            long curDateInMills = cal.getTimeInMillis();
            cal.add(Calendar.MONTH, 36);
            long upDateInMills = cal.getTimeInMillis();
            int upDays = (int)((upDateInMills - curDateInMills) / DateHelper.DAY_IN_MILLISECONDS);
            int lowDays = IntegerParser.parse(configureProvider.getProperty(SystemVariable.JK_BXDQ_LEAST_DAYS), 1);
            if (t6230.F09 < lowDays || t6230.F09 > upDays)
            {
                getController().prompt(request,
                    response,
                    PromptLevel.WARRING,
                    "本息到期，一次付清(按天计)，借款期限超出有效天数[" + lowDays + "," + upDays + "]");
                flag = false;
            }
            t6231.F21 = T6231_F21.S;
            t6231.F22 = t6230.F09;
            t6230.F09 = 0;
        }
        else
        {
            if (t6230.F09 < 1 || t6230.F09 > 36)
            {
                getController().prompt(request, response, PromptLevel.WARRING, "借款期限超出有效月数[1-36]");
                flag = false;
            }
            t6231.F21 = T6231_F21.F;
            t6231.F22 = 0;
        }
        
        if (!flag)
        {
            // getController().forward(request, response, getController().getViewURI(request, Xjd.class);
            forwardView(request, response, Dbd.class);
            return;
        }
        T6236 t6236 = new T6236();
        t6236.parse(request);
        t6236.F04 = T6236_F04.S;
        t6236.F03 = guarantorId;
        //担保贷
        t6231.F35 = T6231_F35.DBD;
        manage.add(t6230, t6231, t6236);
        getController().prompt(request, response, PromptLevel.INFO, "借款申请提交成功！");
        // 跳转到用户中心
        sendRedirect(request,
            response,
            getResourceProvider().getResource(ConfigureProvider.class).format(URLVariable.USER_JKSQCX));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        ResourceProvider resourceProvider = getResourceProvider();
        resourceProvider.log(throwable);
        getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        if (throwable instanceof AuthenticationException)
        {
            Controller controller = getController();
            controller.redirectLogin(request, response, configureProvider.format(URLVariable.LOGIN));
        }
        else
        {
            getController().forward(request, response, getController().getViewURI(request, Dbd.class));
        }
    }
    
}
