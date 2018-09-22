/*
 * 文 件 名:  QuerySignSatus.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2017年10月24日
 */
package com.dimeng.p2p.console.servlets.fdd;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.AbstractFddServlet;
import com.dimeng.p2p.S62.entities.T6273;
import com.dimeng.p2p.S62.enums.T6273_F15;
import com.dimeng.p2p.signature.fdd.service.FddSignatureServiceV25;

/**
 * 签署状态查询
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2017年10月24日]
 */
public class QuerySignSatus extends AbstractFddServlet
{
    
    private static final long serialVersionUID = 1L;
    
    public static final Logger logger = Logger.getLogger(FDDExtSignAuto.class);
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        logger.info("法大大【签署状态查询】，接口调用---" + getSystemDateTime());
        String qmId = request.getParameter("qmId");
        try
        {
            FddSignatureServiceV25 fddManage = serviceSession.getService(FddSignatureServiceV25.class);
            T6273 t6273 = fddManage.selectT6273(Integer.parseInt(qmId));
            T6273_F15 status = t6273.F15; //当前记录的签名状态
            logger.info("法大大【签署状态查询】id：" + t6273.F01 + "，签章当前状态：" + status.getChineseName());
            if (t6273 == null)
            {
                throw new LogicalException("未查到信息");
            }
            fddManage.querySignStatus(Integer.parseInt(qmId));
            /*int orderId = 0;
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
            }*/
            
        }
        catch (Throwable e)
        {
            logger.error("自动签章对账数据处理异常", e);
            throw e;
        }
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        logger.error("", throwable);
        //int loanId = IntegerParser.parse(request.getParameter("loanId"));
        if (throwable instanceof ParameterException || throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试！");
            // sendRedirect(request, response, getURL(loanId));
        }
        else if (throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            //  sendRedirect(request, response, getURL(loanId));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
    
    /* protected String getURL(int loanId)
         throws IOException
     {
         ResourceProvider resourceProvider = getResourceProvider();
         final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
         StringBuilder url = new StringBuilder(configureProvider.format(URLVariable.FINANCING_SBTZ_XQ));
         url.append(Integer.toString(loanId)).append(resourceProvider.getSystemDefine().getRewriter().getViewSuffix());
         return url.toString();
     }*/
    
}
