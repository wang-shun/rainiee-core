package com.dimeng.p2p.pay.servlets.fdd.ret;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6273;
import com.dimeng.p2p.S62.enums.T6273_F10;
import com.dimeng.p2p.S62.enums.T6273_F15;
import com.dimeng.p2p.signature.fdd.enums.FDDReturnStatus;
import com.dimeng.p2p.signature.fdd.service.FddSignatureServiceV25;

/**
 * 手动签署合同-回调通知处理
 * 文  件  名：FDDExtSignNotify.java
 * 版        权：深圳市迪蒙网络科技有限公司
 * 类  描  述：
 * 修  改  人：ZhangXu
 * 修改时间：2016年12月18日
 */
public class FDDExtSignNotify extends AbstractFDDServletRet
{
    
    private static final long serialVersionUID = 1L;
    
    public static final Logger logger = Logger.getLogger(FDDExtSignNotify.class);
    
    @Override
    protected String handlerResult(HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        doPrintWriter(response, "success", false);
        logger.info("---法大大【手动合同签署】异步回调通知---" + getSystemDateTime());
        
        logger.info("---法大大【手动合同签署】异步回调通知---" + request.getQueryString());
        //接口返回结果参数
        String result_code = request.getParameter("result_code");//签章结果代码 3000（签章成功） 3001(签章失败)
        String result_desc = request.getParameter("result_desc");//签章结果描述 签章结果描述信息
        String download_url = request.getParameter("download_url");//签章后文档下载地址，也可直接展示给客户，供其下载签章后的文档留存，或由接入平台自行下载保存
        String viewpdf_url = request.getParameter("viewpdf_url");//在线查看已签文档的地址，URL提供客户直接在线查看已签署合同文件
        String transaction_id = request.getParameter("transaction_id"); //签章请求交易号
        String timestamp = request.getParameter("timestamp");
        String msg_digest = request.getParameter("msg_digest");
        
        StringBuffer buf = new StringBuffer().append("result_code").append(result_code);
        buf.append("result_desc").append(result_desc);
        buf.append("download_url").append(download_url);
        buf.append("viewpdf_url").append(viewpdf_url);
        buf.append("transaction_id").append(transaction_id);
        buf.append("timestamp").append(timestamp);
        buf.append("msg_digest").append(msg_digest);
        
        logger.info("---法大大【手动合同签署】异步回调通知信息 = " + buf.toString());
        String[] transIds = transaction_id.split("_");
        int bidId = Integer.parseInt(transIds[1]);
        int userId = Integer.parseInt(transIds[2]);
        FddSignatureServiceV25 fddManage = serviceSession.getService(FddSignatureServiceV25.class);
        if (FDDReturnStatus.SIGN_SUCCESS.code().equals(result_code))
        {
            logger.info("---法大大【手动合同签署】：" + result_desc);
            
            // 借款 人 签平台章,orderId=0
            fddManage.extsignAutoPT(userId, bidId, 0, T6273_F10.JKR);
            //根据条件更新签章状态-待归档
            fddManage.updateT6273ForStatus(T6273_F15.DGD, userId, bidId, 0, T6273_F10.JKR);
            fddManage.updateT6273ForSign(transaction_id, userId, bidId, 0, T6273_F10.JKR);
            
            //插入文档地址供查看或下载合同
            fddManage.updateT6273ForUrl(userId, bidId, 0, T6273_F10.JKR, viewpdf_url, download_url);
            T6273 t6273 = fddManage.selectT6273(userId, bidId, 0, T6273_F10.JKR);
            //合同归档
            String resultStr = fddManage.contractFiling(t6273.F04);
            if (!StringUtils.isEmpty(resultStr))
            {
                //归档成功:则改状态为“已归档”，整个电子签章流程结束
                if ("success".equals(resultStr))
                {
                    fddManage.updateT6273ForStatus(T6273_F15.YGD, userId, bidId, 0, T6273_F10.JKR);
                }
            }
        }
        else
        {
            logger.info("---法大大【手动合同签署】返回：" + result_code + "：" + result_desc);
            throw new Exception("借款人合同签署失败，原因：" + result_desc);
        }
        
        return null;
    }
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
    
}
