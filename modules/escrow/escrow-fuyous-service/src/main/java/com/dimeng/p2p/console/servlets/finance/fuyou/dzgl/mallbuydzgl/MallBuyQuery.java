package com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.mallbuydzgl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.AbstractDzglServlet;
import com.dimeng.p2p.escrow.fuyou.service.QueryManage;
import com.dimeng.p2p.order.MallChangeExecutor;

/**
 * 
 * 商品购买订单查询
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年05月12日]
 */
@Right(id = "P2P_C_FINANCE_SPGM_QUERY", moduleId = "P2P_C_FUYOU_SPGM_DZGL", order = 1, name = "商品购买对账操作")
public class MallBuyQuery extends AbstractDzglServlet
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
        logger.info("商品购买对账——IP:" + request.getRemoteAddr());
        final int orderId = Integer.parseInt(request.getParameter("orderNo"));
        QueryManage manage = serviceSession.getService(QueryManage.class);
        T6501 t6501 = manage.selectT6501(orderId);
        if (t6501 == null)
        {
            throw new LogicalException("订单不存在!");
        }
        // 对账订单必须大于5分钟[防止订单未提交第三方面对账]
        String f04 = String.valueOf(t6501.F04);
        if (compareDate(f04, 300000))
        {
            processRequest(request, response, "5分钟内订单请稍后对账!");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        boolean flag = manage.selectFuyou(serviceSession, t6501, params);
        switch (t6501.F02)
        {
        //商品购买
            case 50003:
                MallChangeExecutor executor = getResourceProvider().getResource(MallChangeExecutor.class);
                manage.bid50003(serviceSession, t6501, params, flag, executor);
                processRequest(request, response, "SUCCESS");
                break;
            default:
                processRequest(request, response, "对账类型不符合!");
                break;
        }
    }
    
    /**
     * 时间
     * <功能详细描述>
     * @param t6501_F04
     * @param time
     * @return
     * @throws ParseException
     */
    public boolean compareDate(String t6501_F04, int time)
        throws ParseException
    {
        Date now = new Date();
        Date d2 = new Date(now.getTime() - time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = simpleDateFormat.parse(t6501_F04);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        int result = c1.compareTo(c2);
        if (result >= 0)
            return true;
        else
            return false;
    }
}
