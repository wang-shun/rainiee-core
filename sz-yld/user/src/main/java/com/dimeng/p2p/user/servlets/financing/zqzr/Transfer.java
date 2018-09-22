package com.dimeng.p2p.user.servlets.financing.zqzr;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.modules.bid.user.service.ZqzrManage;
import com.dimeng.p2p.modules.bid.user.service.query.addTransfer;
import com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

public class Transfer extends AbstractFinancingServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -6826337769817263673L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        final ZqzrManage finacingManage = serviceSession.getService(ZqzrManage.class);
        //final BigDecimal mayMoney = finacingManage.getDslx(IntegerParser.parse(request.getParameter("jkbId")));
        final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        BigDecimal zrValue = BigDecimalParser.parse(request.getParameter("zrValue"));
        BigDecimal zqValue = BigDecimalParser.parse(request.getParameter("zqValue"));
        if (BigDecimal.ZERO.compareTo(zrValue) == 0 || BigDecimal.ZERO.compareTo(zqValue) == 0)
        {
            throw new LogicalException("债权价值或转让价格不能为0");
        }
        BigDecimal zqzrsx = BigDecimal.ZERO;
        BigDecimal zqzrxx = BigDecimal.ZERO;
        try
        {
            String zqzrbl = configureProvider.getProperty(SystemVariable.ZQZRBL);
            String[] zqzrblTemp = zqzrbl.split("-");
            if (zqzrblTemp.length == 2)
            {
                zqzrxx = new BigDecimal(zqzrblTemp[0]);
                zqzrsx = new BigDecimal(zqzrblTemp[1]);
            }
        }
        catch (Exception e)
        {
        }
        
        if (zrValue.compareTo(BigDecimal.valueOf(Math.ceil(zqzrsx.multiply(zqValue).doubleValue()))) > 0
            || zrValue.compareTo(BigDecimal.valueOf(Math.ceil(zqzrxx.multiply(zqValue).doubleValue()))) < 0)
        {
            throw new LogicalException("债权转让的价格区间需在" + Math.ceil(zqzrxx.multiply(zqValue).doubleValue()) + "元-"
                + Math.ceil(zqzrsx.multiply(zqValue).doubleValue()) + "元之间");
        }
        String tranPwd = request.getParameter("tranPwd");
        if (isOpenWithPsd)
        {
            tranPwd = RSAUtils.decryptStringByJs(tranPwd);
            tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
        }
        finacingManage.transfer(new addTransfer()
        {
            @Override
            public BigDecimal getRateMoney()
            {
                return new BigDecimal(configureProvider.getProperty(SystemVariable.ZQZRGLF_RATE));
            }
            
            @Override
            public BigDecimal getTransferValue()
            {
                return BigDecimalParser.parse(request.getParameter("zrValue"));
            }
            
            @Override
            public int getTransferId()
            {
                return IntegerParser.parse(request.getParameter("zqId"));
            }
            
            @Override
            public BigDecimal getBidValue()
            {
                return BigDecimalParser.parse(request.getParameter("zqValue"));
            }
        }, tranPwd);
        finacingManage.writeFrontLog(FrontLogType.ZRZQ.getName(), "前台转出债权");
        sendRedirect(request, response, getController().getViewURI(request, Zqzrz.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable);
        if (throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
            sendRedirect(request, response, getController().getViewURI(request, Zqkzc.class));
        }
        else if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getController().getViewURI(request, Zqkzc.class));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
    
}
