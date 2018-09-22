/*
 * 文 件 名:  FddSignAutoBid.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2017年10月24日
 */
package com.dimeng.p2p.console.servlets.fdd;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFddServlet;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6273;
import com.dimeng.p2p.S62.enums.T6273_F07;
import com.dimeng.p2p.S62.enums.T6273_F15;
import com.dimeng.p2p.signature.fdd.service.FddSignatureServiceV25;
import com.dimeng.p2p.signature.fdd.threads.FddContractPreservationFormationThread;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 根据标ID进行补签
 * 前台页面不显示，用于补录标未生成文档的情况
 * 
 * @author  guomianyun
 * @version  [版本号, 2017年10月24日]
 */
public class FddSignAutoBid extends AbstractFddServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
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
        //标ID
        String bid = request.getParameter("bid");
        
        FddSignatureServiceV25 contractPreservationManage = serviceSession.getService(FddSignatureServiceV25.class);
        int bidId = IntegerParser.parse(bid);
        T6230 t6230 = contractPreservationManage.selectT6230(bidId);
        if (null != t6230)
        {
            List<T6273> t6273s = contractPreservationManage.selectT6273ByDidId(bidId);
            // 本地文件是否生成T6273.F09不为空表示生成了
            boolean isPath = false;
            // 是否有已签名
            boolean isYQ = false;
            if (null != t6273s)
            {
                for (T6273 t6273 : t6273s)
                {
                    if (StringHelper.isEmpty(t6273.F09))
                    {
                        isPath = true;
                    }
                    // 如果有已签,不能删除重新签名
                    if (t6273.F07.name().equals(T6273_F07.YQ) || t6273.F15.name().equals(T6273_F15.DGD.name())
                        || t6273.F15.name().equals(T6273_F15.YGD.name()))
                    {
                        isYQ = true;
                    }
                }
                //如果文档地址有未生成和未到法大大签名(签名状态是未签)
                if (isPath && !isYQ)
                {
                    //先删除标下面的签名信息，下面才能重新生成
                    contractPreservationManage.deleteT6273byBidId(bidId);
                }
            }
            //为空，或生成的本地合同未成功 
            if ((null == t6273s || t6273s.size() <= 0) || isPath)
            {
                contractPreservationManage.insertT6273(bidId);
                //调用法大大电子签章
                Thread thread = new Thread(new FddContractPreservationFormationThread(bidId));
                thread.setName("生成借款合同线程类!");
                thread.start();
            }
            else
            {
                logger.info("根据标ID进行补签,标的已存在签名的PDF文件记录 , bid =" + bid);
            }
        }
        else
        {
            logger.info("根据标ID进行补签,标信息不存在 , bid =" + bid);
        }
    }
    
}
