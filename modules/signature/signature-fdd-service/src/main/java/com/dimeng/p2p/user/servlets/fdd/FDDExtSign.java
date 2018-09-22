package com.dimeng.p2p.user.servlets.fdd;

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
import com.dimeng.p2p.signature.fdd.service.FddSignatureServiceV25;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 法大大借款人-手动签章
 * 文  件  名：FDDExtSign.java
 * 版        权：深圳市迪蒙网络科技有限公司
 * 类  描  述：
 * 修  改  人：ZhangXu
 * 修改时间：2016年12月17日
 */
public class FDDExtSign extends AbstractFddServlet
{
    
    private static final long serialVersionUID = 1L;
    
    public static final Logger logger = Logger.getLogger(FDDExtSign.class);
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        logger.info("法大大【手动签章】接口调用---" + getSystemDateTime());
        String bidId = request.getParameter("bidId");
        FddSignatureServiceV25 fddManage = serviceSession.getService(FddSignatureServiceV25.class);
        try
        {
            T6230 t6230 = fddManage.selectT6230(Integer.parseInt(bidId));
            if (T6230_F20.HKZ == t6230.F20)
            {
                logger.info("法大大【手动签章】标id：" + bidId + "借款人id：" + t6230.F02);
                T6273 t6273 = fddManage.selectT6273(t6230.F02, t6230.F01, 0, T6273_F10.JKR);
                if (null != t6273 && !StringHelper.isEmpty(t6273.F09))
                {
                    //根据当前状态调用各自接口处理
                    if (T6273_F15.DSQ == t6273.F15 || T6273_F15.DSC == t6273.F15)
                    {
                        String retUploadDoc = fddManage.uploadFileNoSign(t6230.F02, t6230.F01, 0, T6273_F10.JKR);
                        if ("success".equalsIgnoreCase(retUploadDoc))
                        {
                            //合同上传成功后调用手动签章
                            fddManage.extSign(t6230.F02, t6230.F01, T6273_F10.JKR, response); // 借款人自动签章
                        }
                        //手动签后面通过回调通知来归档和更新信息
                        /*   retUploadDoc = fddManage.contractFiling(t6273.F04);
                           if ("success".equalsIgnoreCase(retUploadDoc))
                           {
                               fddManage.updateT6273ForStatus(T6273_F15.YGD, t6273.F02, t6273.F03, 0, T6273_F10.JKR);
                           }*/
                    }
                    else if (T6273_F15.DQM == t6273.F15)
                    {
                        fddManage.extSign(t6230.F02, t6230.F01, T6273_F10.JKR, response);
                        //手动签后面通过回调通知来归档和更新信息
                        /*fddManage.contractFiling(t6273.F04);
                        fddManage.updateT6273ForStatus(T6273_F15.YGD, t6273.F02, t6273.F03, 0, T6273_F10.JKR);*/
                    }
                    else if (T6273_F15.DGD == t6273.F15)
                    {
                        fddManage.contractFiling(t6273.F04);
                        fddManage.updateT6273ForStatus(T6273_F15.YGD, t6273.F02, t6273.F03, 0, T6273_F10.JKR);
                    }
                    /*   sendRedirect(request,
                           response,
                           getResourceProvider().getResource(ConfigureProvider.class).format(URLVariable.USER_CREDIT));*/
                }
                else
                {
                    logger.info("法大大【手动签章】,标的未生成签章信息,标id：" + bidId + "借款人id：" + t6230.F02);
                    throw new LogicalException("法大大【手动签章】,标的未生成签章信息");
                }
            }
            else
            {
                throw new LogicalException("法大大【手动签章】,标的未放款不能签章");
            }
        }
        catch (Exception e)
        {
            logger.info("手动签章数据处理异常", e);
            throw e;
        }
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        logger.error("", throwable);
        getResourceProvider().log(throwable);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            //  sendRedirect(request, response, getURL(loanId));
        }
        else
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试！");
            // super.onThrowable(request, response, throwable);
            //   sendRedirect(request, response, getURL(loanId));
        }
        sendRedirect(request,
            response,
            getResourceProvider().getResource(ConfigureProvider.class).format(URLVariable.USER_CREDIT));
    }
    
    protected String getURL(int loanId)
        throws IOException
    {
        ResourceProvider resourceProvider = getResourceProvider();
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        StringBuilder url = new StringBuilder(configureProvider.format(URLVariable.USER_CREDIT));
        // url.append(Integer.toString(loanId)).append(resourceProvider.getSystemDefine().getRewriter().getViewSuffix());
        return url.toString();
    }
    
}
