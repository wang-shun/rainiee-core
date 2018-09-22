package com.dimeng.p2p.app.servlets.user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.account.user.service.entity.CapitalLs;
import com.dimeng.p2p.app.service.BusinessManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.TranRecord;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 
 * 交易记录
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月27日]
 */
public class TranRecordList extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5342532436154784017L;
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取页面参数
        final int pageIndex = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        
        // 查询往来账户信息
        BusinessManage manage = serviceSession.getService(BusinessManage.class);
        PagingResult<CapitalLs> tradingRecords = manage.mobileSearchLs(getPaging(pageIndex, pageSize));
        
        // 判断异常信息
        List<TranRecord> tranRecords = new ArrayList<TranRecord>();
        if (tradingRecords == null || (tradingRecords.getPageCount() < LongParser.parse(pageIndex)))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", tranRecords);
            return;
        }
        
        // 获取往来账户信息
        CapitalLs[] capitals = tradingRecords.getItems();
        
        if (capitals != null && capitals.length > 0)
        {
            // 查询账户类型
            for (CapitalLs cap : capitals)
            {
                TranRecord tr = new TranRecord();
                
                // 设置交易类型
                tr.setTranType(cap.tradeType);
                
                // 设置交易时间
                tr.setTranTime(cap.F18);
                
                // 收入
                tr.setInAmount(cap.F06.setScale(2, BigDecimal.ROUND_HALF_UP));
                
                // 支出
                tr.setExpAmount(cap.F07.setScale(2, BigDecimal.ROUND_HALF_UP));
                
                // 设置交易金额
                tr.setRevAmount(cap.F08.setScale(2, BigDecimal.ROUND_HALF_UP));
                
                // 备注
                tr.setRemark(cap.F09);
                tranRecords.add(tr);
            }
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", tranRecords);
        return;
    }
}
