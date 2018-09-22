package com.dimeng.p2p.app.servlets.creditor.publics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5124;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6231_F27;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.creditor.domain.Creditor;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.TransferManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.front.service.entity.Zqzqlb;
import com.dimeng.p2p.modules.bid.front.service.query.TransferQuery_Order;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 债券列表
 * @author tanhui
 */
public class CreditorList extends AbstractAppServlet
{
    
    private static final long serialVersionUID = 1L;
    
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
        
        TransferManage service = serviceSession.getService(TransferManage.class);
        BidManage bidManage = serviceSession.getService(BidManage.class);
        
        final int pageIndex = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        final String[] term = getParameterValues(request, "term");
        final String[] level = getParameterValues(request, "level");
        final String[] type = getParameterValues(request, "type");
        
        /**
         * 排序规则
         * 
         * 年化利率  41:降序  42:升序
         * 借款金额  71:降序  72:升序
         * 剩余期数  11:降序  12:升序
         */
        final String ord = getParameter(request, "orderId");
        final int orderId = StringHelper.isEmpty(ord) ? 0 : IntegerParser.parse(ord);
        
        PagingResult<Zqzqlb> results = service.search(new TransferQuery_Order()
        {
            @Override
            public T6211[] getType()
            {
                String[] values = type;
                if (values == null || values.length == 0)
                {
                    return null;
                }
                T6211[] types = new T6211[values.length];
                for (int i = 0; i < values.length; i++)
                {
                    types[i] = new T6211();
                    types[i].F01 = IntegerParser.parse(values[i]);
                }
                return types;
            }
            
            @Override
            public T5124[] getCreditLevel()
            {
                String[] values = level;
                
                if (values == null || values.length == 0)
                {
                    return null;
                }
                T5124[] levels = new T5124[values.length];
                for (int i = 0; i < values.length; i++)
                {
                    levels[i] = new T5124();
                    levels[i].F01 = IntegerParser.parse(values[i]);
                }
                return levels;
            }
            
            @Override
            public int getRate()
            {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public int getProductId()
            {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public int getTerm()
            {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public int getOrder()
            {
                return orderId;
            }
        }, getPaging(pageIndex, pageSize));
        
        List<Creditor> zqzqlbList = new ArrayList<Creditor>();
        if (results.getPageCount() < LongParser.parse(pageIndex))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", zqzqlbList);
            return;
        }
        
        //获取标的类型
        T6211[] t6211s = bidManage.getBidType();
        
        Zqzqlb[] zqzqlbs = results.getItems();
        
        if (zqzqlbs != null && zqzqlbs.length > 0)
        {
            for (Zqzqlb zqzqlb : zqzqlbs)
            {
                Creditor creditor = new Creditor();
                creditor.setId(zqzqlb.F25);
                creditor.setCreId(zqzqlb.F01);
                creditor.setBidId(zqzqlb.F24);
                creditor.setCreditorTitle(zqzqlb.F12);
                creditor.setCreditorVal(String.valueOf(zqzqlb.F03));
                creditor.setCycle(zqzqlb.F22);
                creditor.setRemainCycle(zqzqlb.F23);
                creditor.setSalePrice(String.valueOf(zqzqlb.F02));
                creditor.setRevInterest(String.valueOf(zqzqlb.dsbx));
                creditor.setRate(String.valueOf(zqzqlb.F14 != null ? zqzqlb.F14.setScale(4, BigDecimal.ROUND_HALF_UP)
                    : 0));
                
                if (zqzqlb.F19 == T6230_F11.S)
                {
                    creditor.setFlag("保");
                }
                else if (zqzqlb.F20 == T6230_F13.S)
                {
                    creditor.setFlag("抵");
                }
                else if (zqzqlb.F21 == T6230_F14.S)
                {
                    creditor.setFlag("实");
                }
                else
                {
                    creditor.setFlag("信");
                }
                
                for (T6211 t6211 : t6211s)
                {
                    if (t6211.F01 == zqzqlb.F13)
                    {
                        creditor.setFinancialType(t6211.F02);
                    }
                }
                Bdxq bdxq = bidManage.get(zqzqlb.F24);
                creditor.setPaymentType(String.valueOf(bdxq.F10));
                creditor.setStatus(String.valueOf(zqzqlb.F06));
                
                T6231 t6231 = bidManage.getExtra(bdxq.F01);
                // 若为奖励标
                if (T6231_F27.S == t6231.F27)
                {
                    creditor.setJlRate(String.valueOf(t6231.F28 != null ? t6231.F28.setScale(4,
                        BigDecimal.ROUND_HALF_UP) : 0));
                }
                
                // 天标
                if (T6231_F21.S == t6231.F21)
                {
                    creditor.setIsDay(t6231.F21.name());
                    creditor.setDays(String.valueOf(t6231.F22));
                }
                zqzqlbList.add(creditor);
            }
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", zqzqlbList);
        return;
    }
}
