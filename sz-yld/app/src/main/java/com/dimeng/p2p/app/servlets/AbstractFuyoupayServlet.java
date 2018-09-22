package com.dimeng.p2p.app.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.fuiou.mpay.encrypt.RSAUtils;
import com.fuiou.util.MD5;
/**
 * 资金模块抽象Servlet.
 * 
 */
public abstract class AbstractFuyoupayServlet extends AbstractUserServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 获取签名
	 * @param signStr  签名串
	 * @param signtp   签名类型
	 * @param key      密钥
	 * @return
	 * @throws Exception
	 */
	public static String getSign(String signStr,String signtp,String key) throws  Exception{
		String sign = "";
		if ("md5".equalsIgnoreCase(signtp)) {
			sign = MD5.MD5Encode(signStr);
		} else {
			sign =	RSAUtils.sign(signStr.getBytes("utf-8"), key);
		}
		return sign;
	} 
	protected ConfigureProvider getConfigureProvider()
    {
        return getResourceProvider().getResource(ConfigureProvider.class);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException
        {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            try
            {
                response.setContentType("text/html");
                response.setHeader("Cache-Control", "no-store");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                if ("OK".equals(msg))
                {
                    out.write("OK");
                }
                else
                {
                    out.write(msg);
                }
            }
            finally
            {
                out.close();
            }
        }
}
