/*
 * 文 件 名:  BidRecordsList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月24日
 */
package com.dimeng.p2p.app.servlets.bid.publics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.service.BusinessManage;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.front.service.entity.BidRecordInfo;
import com.dimeng.util.parser.IntegerParser;

/**
 * 投资记录
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月24日]
 */
public class BidRecordsList extends AbstractAppServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3493700097617934387L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取标ID
        final String bidId = getParameter(request, "bidId");
        int id = IntegerParser.parse(bidId);
        
        if (id == 0)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "参数错误！");
            return;
        }
        
        BusinessManage bidManage = serviceSession.getService(BusinessManage.class);
        
        // 根据标ID查询投资记录
        BidRecordInfo[] bidRecords = bidManage.getBidRecordList(id);
        
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", bidRecords);
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
