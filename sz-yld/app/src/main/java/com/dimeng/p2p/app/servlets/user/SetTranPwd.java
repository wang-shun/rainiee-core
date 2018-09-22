package com.dimeng.p2p.app.servlets.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.IndexManage;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.account.user.service.entity.UserBaseInfo;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

/**
 * 设置交易密码
 * @author tanhui
 */
public class SetTranPwd extends AbstractSecureServlet
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
        
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        Safety sa = safetyManage.get();
        
        String onePwd = getParameter(request, "onePwd");
        String twoPwd = getParameter(request, "twoPwd");
        
        if (sa.username.equals(onePwd))
        {
            setReturnMsg(request, response, ExceptionCode.ACCOUNT_PASSWORD_ERROR, "交易密码与用户名不能相同,请重新输入！");
            return;
        }
        if (StringHelper.isEmpty(onePwd) || StringHelper.isEmpty(twoPwd))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.SAME_LOGINPASSWORD_ERROR, "密码不能为空！");
            return;
        }
        else if (!onePwd.equals(twoPwd))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PASSWORD_NOSAME_ERROR, "两次密码输入不一样！");
            return;
        }
        else
        {
            onePwd = UnixCrypt.crypt(onePwd, DigestUtils.sha256Hex(onePwd));
            if (sa.password.equals(onePwd))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.SAME_LOGINPASSWORD_ERROR, "交易密码不能与登录密码一致！");
                return;
            }
            else if (StringHelper.isEmpty(sa.phoneNumber))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.PHONE_NUMBER_NOEXIST_ERRROR, "请先绑定手机号");
                return;
            }
//            else if (!StringHelper.isEmpty(sa.txpassword))
//            {
//                // 封装返回信息
//                setReturnMsg(request, response, ExceptionCode.TRANPASSWORD_EXIST_ERROR, "交易密码已经设置");
//                return;
//            }
            else
            {
                safetyManage.updateTxpassword(onePwd);
            }
        }
        //是否全部认证
        boolean isAllVerify = true;
        IndexManage manage = serviceSession.getService(IndexManage.class);
        
        ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(request.getServletContext());
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        boolean email = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_EMAIL));
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
        if (!userBaseInfo.email && email)
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
