package com.dimeng.p2p.console.servlets.fdd;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.AbstractFddServlet;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6273;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6273_F10;
import com.dimeng.p2p.S62.enums.T6273_F15;
import com.dimeng.p2p.console.servlets.bid.htgl.signagl.SignatureList;
import com.dimeng.p2p.console.servlets.bid.htgl.signagl.YqmSignatureList;
import com.dimeng.p2p.signature.fdd.service.FddSignatureServiceV25;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;

/**
 * 法大大借款人-自动签章对账
 * 文  件  名：FDDExtSign.java
 * 版        权：深圳市迪蒙网络科技有限公司
 * 类  描  述：
 * 修  改  人：ZhangXu
 * 修改时间：2016年12月27日
 */
public class FDDExtSignAuto extends AbstractFddServlet
{
    
    private static final long serialVersionUID = 1L;
    
    public static final Logger logger = Logger.getLogger(FDDExtSignAuto.class);
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Exception
    {
        this.processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Exception
    {
        
        logger.info("法大大【自动签章】后台手动对账，接口调用---" + getSystemDateTime());
        String qmId = request.getParameter("qmId");
        String t = request.getParameter("t");
        try
        {
            FddSignatureServiceV25 fddManage = serviceSession.getService(FddSignatureServiceV25.class);
            T6273 t6273 = fddManage.selectT6273(Integer.parseInt(qmId));
            T6273_F15 status = t6273.F15; //当前记录的签名状态
            logger.info("法大大【自动签章】后台手动对账id：" + t6273.F01 + "，签章当前状态：" + status.getChineseName());
            T6230 t6230 = fddManage.selectT6230(t6273.F03);
            
            if (T6230_F20.HKZ != t6230.F20)
            {
                throw new LogicalException("标的不是还款中状态，不能签名，请先放款");
            }
            int orderId = 0;
            
            //投资人取投资成功id,债权转让取债权转让id
            if (T6273_F10.TZR == t6273.F10)
            {
                orderId = t6273.F14;
            }
            else
            {
                orderId = t6273.F18;
            }
            
            //根据当前状态调用各自接口处理
            if (T6273_F15.DSQ == status || T6273_F15.DSC == status)
            {
                fddManage.uploadFile(t6273.F02, t6273.F03, orderId, t6273.F10);
            }
            else if (T6273_F15.DQM == status)
            {
                fddManage.extsignAuto(t6273.F02, t6273.F03, orderId, t6273.F10);
            }
            else if (T6273_F15.DGD == status)
            {
                fddManage.contractFiling(t6273.F04);
                fddManage.updateT6273ForStatus(T6273_F15.YGD, t6273.F02, t6273.F03, orderId, t6273.F10);
            }
            getController().prompt(request, response, PromptLevel.INFO, "签名成功");
            if (StringHelper.isEmpty(t) && t.equals("yq"))
            {
                sendRedirect(request, response, getController().getURI(request, YqmSignatureList.class));
            }
            else
            {
                sendRedirect(request, response, getController().getURI(request, SignatureList.class));
            }
        }
        catch (Exception e)
        {
            logger.info("自动签章对账数据处理异常", e);
            throw e;
        }
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        logger.error("", throwable);
        //   int loanId = IntegerParser.parse(request.getParameter("loanId"));
        if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getController().getURI(request, SignatureList.class));
        }
        else
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试！");
            sendRedirect(request, response, getController().getURI(request, SignatureList.class));
        }
    }
    
    protected String getURL(int loanId)
        throws IOException
    {
        ResourceProvider resourceProvider = getResourceProvider();
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        StringBuilder url = new StringBuilder(configureProvider.format(URLVariable.FINANCING_SBTZ_XQ));
        url.append(Integer.toString(loanId)).append(resourceProvider.getSystemDefine().getRewriter().getViewSuffix());
        return url.toString();
    }
    
}
