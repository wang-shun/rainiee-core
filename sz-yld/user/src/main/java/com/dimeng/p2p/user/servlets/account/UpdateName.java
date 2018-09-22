package com.dimeng.p2p.user.servlets.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.common.enums.AttestationState;
import com.dimeng.p2p.modules.nciic.service.INciicManageService;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.p2p.variables.defines.RealNameAuthVarivale;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.nciic.NciicVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

public class UpdateName extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    private static final int[] WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    
    private static final char CHECK_CODE[] = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        String idcard = request.getParameter("idCard");
        String name = request.getParameter("name");
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        if (StringHelper.isEmpty(name))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append("姓名不能为空!" + "'}]");
            out.write(sb.toString());
            return;
        }
        String mtest = "^[\u4e00-\u9fa5]{2,}$";
        name = name.trim();
        if (!name.matches(mtest))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append("请输入合法的姓名!" + "'}]");
            out.write(sb.toString());
            return;
        }
        if (StringHelper.isEmpty(idcard))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':00,'msg':'");
            sb.append("身份证号码不能为空!" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        idcard = idcard.trim();
        if (!isValidId(idcard))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append("无效的身份证号!" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        if (name.length() > 20)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append("姓名最多为20个字符!" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int born = Integer.parseInt(idcard.substring(6, 10));
        if ((year - born) < 18)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append("必须年满18周岁!" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        // 判断修改实名认证信息时，该身份证是否正在注册第三方账户
        if (safetyManage.isAuthingUpdate())
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append("正在第三方注册账户，不能修改实名认证信息!" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        Safety safety = safetyManage.get();
        if (!StringHelper.isEmpty(safety.isIdCard))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append("您的账号已通过实名认证，请不要重复认证!" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        INciicManageService nic = serviceSession.getService(INciicManageService.class);
        idcard = idcard.toUpperCase();
        
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        
        boolean enable = BooleanParser.parse(configureProvider.getProperty(NciicVariable.ENABLE));
        boolean is;
        //不启用实名认证
        if (!enable)
        {
            is = true;
        }
        else
        {
            int userId = serviceSession.getSession().getAccountId();
            int number = Integer.parseInt(configureProvider.getProperty(SystemVariable.SM_RZCS));
            //该用户当天超过实名认证错误次数
            if (safetyManage.isMoreThanErrorCount(userId, number))
            {
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':03,'msg':'");
                sb.append("您今天实名认证太频繁，请明天再试!" + "'}]");
                out.write(sb.toString());
                return;
            }
            is = nic.check(idcard, name, "PC", userId);
        }
        if (is)
        {
            if (T6110_F06.FZRR.name().equals(safety.userType))
            {
                safetyManage.updateNameForFZRR(name, idcard, AttestationState.YYZ.name());
                
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':01,'msg':'");
                sb.append("sussess" + "'}]");
                out.write(sb.toString());
                return;
            }
            
            if (!safetyManage.isIdcard(idcard, T6110_F06.ZRR))
            {
                safetyManage.updateNameSFZRR(name, idcard, AttestationState.YYZ.name(), T6110_F06.ZRR);
                // getController().prompt(request, response, PromptLevel.ERROR,
                // "实名认证成功");
                
                String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
                if (StringHelper.isEmpty(escrow)
                    || (!"yeepay".equals(escrow.toLowerCase()) && !"shuangqian".equals(escrow.toLowerCase())))
                {
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
                        //实名认证通过时间
                        safetyManage.updateT6198F06(serviceSession.getSession().getAccountId());
                    }
                    
                    boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
                    if (is_mall)
                    {
                        // 实名认证赠送积分
                        SetScoreManage setScoreManage = serviceSession.getService(SetScoreManage.class);
                        setScoreManage.giveScore(null, T6106_F05.realname, null);
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':01,'msg':'");
                sb.append("sussess" + "'}]");
                out.write(sb.toString());
                return;
            }
            else
            {
                // getController().prompt(request, response, PromptLevel.ERROR,
                // "该身份证已认证过！");
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':00,'msg':'");
                sb.append("该身份证已认证过!" + "'}]");
                out.write(sb.toString());
                return;
            }
        }
        else
        {
            // getController().prompt(request, response, PromptLevel.ERROR,
            // "实名认证失败");
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':00,'msg':'");
            sb.append("实名认证失败!" + "'}]");
            out.write(sb.toString());
            return;
        }
        
    }
    
    /**
     * 校验身份证号是否合法.
     * 
     * @param id
     *            身份证号码
     * @return {@code boolean} 是否合法
     * @throws Throwable
     */
    private boolean isValidId(String id)
        throws Throwable
    {
        if (StringHelper.isEmpty(id) || id.length() != 18)
        {
            return false;
        }
        
        int sum = 0;
        int num;
        for (int index = 0; index < 17; index++)
        {
            num = id.charAt(index) - '0';
            if (num < 0 || num > 9)
            {
                return false;
            }
            sum += num * WEIGHT[index];
        }
        return CHECK_CODE[sum % 11] == Character.toUpperCase(id.charAt(17));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();
        ResourceProvider resourceProvider = getResourceProvider();
        resourceProvider.log(throwable);
        String errorInfo = throwable.getMessage();
        getController().prompt(request, response, PromptLevel.ERROR, errorInfo);
        /*if (throwable instanceof AuthenticationException) {
        	
        	 * Controller controller = getController();
        	 * controller.redirectLogin(request, response,
        	 * controller.getViewURI(request, Login.class));
        	 
        	StringBuilder sb = new StringBuilder();
        	sb.append("[{'num':00,'msg':'");
        	sb.append(errorInfo);
        	sb.append("'}]");
        	out.write(sb.toString());
        	return;
        } else {
        	StringBuilder sb = new StringBuilder();
        	sb.append("[{'num':00,'msg':'");
        	sb.append(errorInfo);
            sb.append("'}]");
        	out.write(sb.toString());
        	return;
        	// sendRedirect(request, response,
        	// getController().getViewURI(request, Safetymsg.class));
        }*/
        StringBuilder sb = new StringBuilder();
        sb.append("[{'num':00,'msg':'");
        sb.append(errorInfo);
        sb.append("'}]");
        out.write(sb.toString());
    }
}
