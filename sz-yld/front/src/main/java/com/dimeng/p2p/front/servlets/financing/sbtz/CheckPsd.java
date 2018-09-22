package com.dimeng.p2p.front.servlets.financing.sbtz;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.front.service.PasswordManage;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.front.servlets.AbstractFrontServlet;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.IntegerParser;

public class CheckPsd extends AbstractFrontServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = null;
        try
        {
            out = response.getWriter();
            PasswordManage passwordManage = serviceSession.getService(PasswordManage.class);
            String password = RSAUtils.decryptStringByJs(request.getParameter("password"));
            int id = IntegerParser.parse(request.getParameter("id"));
            String jypsd = passwordManage.getJyPassword(id);
            password = UnixCrypt.crypt(password, DigestUtils.sha256Hex(password));
            
            ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
            int maxCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_INPUT));
            int count = passwordManage.psdInputCount();
            if (count >= maxCount)
            {
                out.write("您今日交易密码输入错误已到最大次数，请改日再试!");
                return;
            }
            if (!password.equals(jypsd))
            {
                passwordManage.addInputCount();
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
                out.write(errorMsg);
                return;
            }
            out.write("01");
        }
        catch (Exception e)
        {
            
            out.write(e.getMessage());
        }
        finally
        {
            if (out != null)
            {
                out.close();
            }
        }
    }
}
