/*
 * 文 件 名:  ZqzrExtSign.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2017年10月30日
 */
package com.dimeng.p2p.console.servlets.fdd;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFddServlet;
import com.dimeng.p2p.S62.entities.T6260;
import com.dimeng.p2p.modules.bid.user.service.TenderTransferManage;
import com.dimeng.p2p.modules.bid.user.service.ZqzrSignaManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * 债权转让补签
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2017年10月30日]
 */
public class ZqzrExtSign extends AbstractFddServlet
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
        //债权转让申请ID
        String zqzrId = request.getParameter("zqzrSqId");
        logger.info("根据债权转让申请ID进行补签, zqzrSqId =" + zqzrId);
        ZqzrSignaManage zqzrSignaManage = serviceSession.getService(ZqzrSignaManage.class);
        TenderTransferManage transferManage = serviceSession.getService(TenderTransferManage.class);
        int zrId = IntegerParser.parse(zqzrId);
        T6260 t6260 = transferManage.getT6260(zrId);
        if (null != t6260)
        {
            zqzrSignaManage.setZqzrSigna(zrId);
        }
        else
        {
            logger.error("根据债权转让申请ID进行补签,信息不存在 , zqzrSqId =" + zqzrId);
        }
    }
    
}
