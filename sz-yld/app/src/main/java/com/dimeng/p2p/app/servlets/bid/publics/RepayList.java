package com.dimeng.p2p.app.servlets.bid.publics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6248;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.bid.domain.BidDynamic;
import com.dimeng.p2p.app.servlets.bid.domain.Repay;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.front.service.entity.Hkjllb;
import com.dimeng.util.parser.IntegerParser;

/**
 * 还款计划
 * @author tanhui
 *
 */
public class RepayList extends AbstractAppServlet
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
        logger.info("|RepayList-in|");
        BidManage bidManage = serviceSession.getService(BidManage.class);
        
        final String bidId = getParameter(request, "bidId");
        int id = IntegerParser.parse(bidId);
        
        if (id == 0)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "参数错误！");
            return;
        }
        
        Map<String, Object> repayLists = new HashMap<String, Object>();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Hkjllb[] hkjhlbs = null;
        PagingResult<Hkjllb> result = bidManage.getHks(id, getPaging(1, Integer.MAX_VALUE));
        hkjhlbs = result.getItems();
        
        List<Repay> repayList = new ArrayList<Repay>();
        if (hkjhlbs != null && hkjhlbs.length > 0)
        {
            for (Hkjllb hkjh : hkjhlbs)
            {
                Repay repay = new Repay();
                repay.setAmount(String.valueOf(hkjh.F01));
                repay.setRepayDate(hkjh.F02 != null ? sdf.format(hkjh.F02) : "");
                repay.setRealDate(hkjh.F04 != null ? sdf.format(hkjh.F04) : "");
                repay.setRepayType(hkjh.F05);
                repay.setStatus(hkjh.F03.getChineseName());
                repayList.add(repay);
            }
        }
        
        repayLists.put("repayList", repayList);
        
        // 查看标的详情
        Bdxq bdxq = bidManage.get(id);
        repayLists.put("isXmdt", bdxq.isXmdt);
        
        List<BidDynamic> dynamicInfos = null;
        if (bdxq.isXmdt)
        {
            // 获取项目动态
            PagingResult<T6248> t6248List = bidManage.viewBidProgresList(id, getPaging(1, 50));
            
            if (null != t6248List)
            {
                T6248[] t6248s = t6248List.getItems();
                
                if (null != t6248s && t6248s.length > 0)
                {
                    dynamicInfos = new ArrayList<BidDynamic>();
                    for (T6248 t6248 : t6248s)
                    {
                        BidDynamic dynamicInfo = new BidDynamic();
                        dynamicInfo.setId(t6248.F01);
                        dynamicInfo.setUserId(t6248.F02);
                        dynamicInfo.setBidId(t6248.F03);
                        dynamicInfo.setStatus(t6248.F05.name());
                        dynamicInfo.setTitle(t6248.F04);
                        dynamicInfo.setTitleDate(sdf.format(t6248.F08));
                        dynamicInfo.setPublishTime(sdf.format(t6248.F07));
                        dynamicInfo.setAbout(t6248.F06);
                        dynamicInfo.setSysName(t6248.sysName);
                        dynamicInfo.setMore(t6248.F09);
                        
                        dynamicInfos.add(dynamicInfo);
                    }
                }
            }
        }
        
        repayLists.put("dynamicInfos", dynamicInfos);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", repayLists);
        return;
    }
}
