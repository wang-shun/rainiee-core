package com.dimeng.p2p.user.servlets.bid;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.OtherLoginException;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 验证交易密码是否正确
 * @author heluzhu
 *
 */
public class CheckTxPwd extends AbstractBidServlet
{
    private static final long serialVersionUID = 904758214711922809L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = null;
        try
        {
            out = response.getWriter();
            ResourceProvider resourceProvider = getResourceProvider();
            final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
            Boolean isOpenWsd =
                BooleanParser.parseObject(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
            if (isOpenWsd)
            {
                String tranPwd = request.getParameter("tranPwd");
                if (StringHelper.isEmpty(tranPwd))
                {
                    out.print("{msg:'交易密码不能为空!',code:'0002'}");
                    return;
                }
                tranPwd = RSAUtils.decryptStringByJs(tranPwd);
                tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
                SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
                Safety sa = safetyManage.get();
                int maxCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_INPUT));
                int count = safetyManage.psdInputCount();
                if (count >= maxCount)
                {
                    out.print("{msg:'您今日交易密码输入错误已到最大次数，请改日再试!',code:'0002'}");
                    return;
                }
                if (!tranPwd.equals(sa.txpassword))
                {
                    safetyManage.addInputCount();
                    String errorMsg = "";
                    if (count + 1 >= maxCount)
                    {
                        errorMsg = "您今日交易密码输入错误已到最大次数，请改日再试!";
                    }
                    else
                    {
                        StringBuilder builder = new StringBuilder("交易密码错误,您最多还可以输入");
                        builder.append(maxCount - (count + 1));
                        builder.append("次");
                        errorMsg = builder.toString();
                    }
                    out.print("{msg:'" + errorMsg + "',code:'0002'}");
                    return;
                }
                out.print("{msg:'验证成功!',code:'0001'}");
            }
        }
        catch (Exception e)
        {
            out.print("{msg:'" + e.getMessage() + "!',code:'0002'}");
        }
        finally
        {
            if (out != null)
            {
                out.close();
            }
        }
        
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
        PrintWriter out = response.getWriter();
        if (throwable instanceof AuthenticationException)
        {
            out.print("{msg:'未登录或会话超时,请重新登录',code:'1001'}");
        }
        else if (throwable instanceof OtherLoginException)
        {
            out.print("{msg:'您的帐号已在另一地点登录，您被迫下线',code:'1002'}");
        }
        else
        {
            out.print("{msg:'系统繁忙！',code:'0002'}");
        }
        out.close();
    }
}