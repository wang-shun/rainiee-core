package com.dimeng.p2p.pay.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.order.MallChangeExecutor;
import com.dimeng.p2p.repeater.score.MallChangeManage;
import com.dimeng.p2p.repeater.score.entity.OrderGoods;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

/**
 * 
 * 换购商品
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月22日]
 */
public class Buy extends AbstractPayServlet
{
    private static final long serialVersionUID = -1332501050421123427L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        response.setContentType("text/html;charset=utf-8");
        MallChangeManage manage = serviceSession.getService(MallChangeManage.class);
        
        String type = request.getParameter("payType");
        String jsonStr = request.getParameter("goodsList");
        String address = request.getParameter("addressId");
        int addressId = StringHelper.isEmpty(address) ? 0 : Integer.parseInt(address);
        String tranPwd = request.getParameter("password");
        final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        boolean isTg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        if (isOpenWithPsd)
        {
            tranPwd = RSAUtils.decryptStringByJs(tranPwd);
            tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
            if (StringHelper.isEmpty(tranPwd))
            {
                throw new LogicalException("请输入正确的交易密码！");
            }
            String txpassword = manage.selectTranPwd();
            if (!tranPwd.equals(txpassword))
            {
                throw new LogicalException("请输入正确的交易密码！");
            }
        }
        
        JSONArray array = JSONArray.fromObject(jsonStr);
        List<OrderGoods> orders = (List<OrderGoods>)array.toCollection(array, OrderGoods.class);
        if ("score".equals(type))
        {
            manage.toChangeByScore(orders, type, addressId);
        }
        else
        {
            MallChangeExecutor executor = getResourceProvider().getResource(MallChangeExecutor.class);
            int orderId = manage.toChangeByBalance(orders, type, addressId);
            executor.submit(orderId, null);
            executor.confirm(orderId, null);
        }
        PrintWriter out = response.getWriter();
        String msg = "score".equals(type) ? "兑换成功！" : "购买成功！";
        out.write("[{result:1,msg:'" + msg + "'}]");
        out.close();
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable);
        PrintWriter out = response.getWriter();
        if (throwable instanceof ParameterException || throwable instanceof AuthenticationException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
            out.write("[{result:0,msg:'" + throwable.getMessage() + "'}]");
        }
        else if (throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            out.write("[{result:0,msg:'" + throwable.getMessage() + "'}]");
        }
        else
        {
            super.onThrowable(request, response, throwable);
            out.write("[{result:0,msg:'" + throwable.getMessage() + "'}]");
        }
        out.close();
    }
}