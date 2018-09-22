/**
 * 
 */
package com.dimeng.p2p.app.servlets.platinfo;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.p2p.S61.entities.T6196;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.entity.IndexStatic;
import com.dimeng.p2p.repeater.policy.OperateDataManage;
import com.dimeng.framework.service.ServiceSession;

/**
 * 投资统计
 * 
 * @author zhoulantao
 *
 */
public class GetIndexStatic extends AbstractAppServlet
{
    
    private static final long serialVersionUID = -108329085121189787L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        final BidManage bidManage = serviceSession.getService(BidManage.class);
        IndexStatic indexStatic = bidManage.getIndexStatic();
        
        // 运行基础数据
        final OperateDataManage manage = serviceSession.getService(OperateDataManage.class);
        T6196 t6196 = manage.getT6196();
        
        // 累计投资金额
        indexStatic.rzzje = indexStatic.rzzje.add(t6196.F02);
        
        // 投资总收益
        indexStatic.yhzsy = indexStatic.yhzsy.add(t6196.F04);
        
        // 累计投资总人数
        indexStatic.yhjys = indexStatic.yhjys.add(new BigDecimal(t6196.F06));
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", indexStatic);
        return;
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
