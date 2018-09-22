/*
 * 文 件 名:  CreditAssignmentList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月27日
 */
package com.dimeng.p2p.app.servlets.user;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.CreditorInfo;
import com.dimeng.p2p.modules.bid.user.service.ZqzrManage;
import com.dimeng.p2p.modules.bid.user.service.entity.InSellFinacingExt;
import com.dimeng.p2p.modules.bid.user.service.entity.OutSellFinacing;
import com.dimeng.p2p.modules.bid.user.service.entity.ZrzdzqEntity;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 债权转让列表
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月27日]
 */
public class CreditAssignmentList extends AbstractSecureServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 5639739568508859393L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 查询类型
        final String type = getParameter(request, "type");
        final int currentPage = IntegerParser.parse(request.getParameter("pageIndex"));
        final int pageSize = IntegerParser.parse(request.getParameter("pageSize"));
        
        // 债权转让接口管理类
        final ZqzrManage service = serviceSession.getService(ZqzrManage.class);
        
        // 分页信息
        final Paging paging = getPaging(currentPage, pageSize);
        
        List<CreditorInfo> creditorInfos = null;
        // 转让中
        if ("zrz".equals(type))
        {
            // 获取转让中列表
            creditorInfos = getZrzEntity(service, paging);
        }
        // 已转出
        else if ("yzc".equals(type))
        {
            // 获取已转出列表
            creditorInfos = getYzcEntity(service, paging);
        }
        // 已转入
        else if ("yzr".equals(type))
        {
            // 获取已转入列表
            creditorInfos = getYzrEntity(service, paging);
        }
        
        // 返回页面列表
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", creditorInfos);
        return;
    }
    
    /**
     * 转让中列表
     * 
     * @param service 债权转让接口管理类
     * @param paging 分页信息
     * @return 转让中列表
     * @throws Throwable 异常信息
     */
    private List<CreditorInfo> getZrzEntity(final ZqzrManage service, final Paging paging)
        throws Throwable
    {
        // 查询转让中的债权列表
        PagingResult<ZrzdzqEntity> sfList = service.getSellFinacing(paging);
        
        List<CreditorInfo> zrzEntitys = null;
        if (null != sfList)
        {
            // 判断是否超出记录集
            ZrzdzqEntity[] zrzdzqEntitys = null;
            if (sfList.getPageCount() >= LongParser.parse(paging.getCurrentPage()))
            {
                zrzdzqEntitys = sfList.getItems();
            }
            
            if (null != zrzdzqEntitys && zrzdzqEntitys.length > 0)
            {
                zrzEntitys = new ArrayList<CreditorInfo>();
                for (ZrzdzqEntity zrzdzqEntity : zrzdzqEntitys)
                {
                    CreditorInfo creditorInfo = new CreditorInfo();
                    // 标ID
                    creditorInfo.setBidId(zrzdzqEntity.F01);
                    // 债权编码
                    creditorInfo.setCreditorId(zrzdzqEntity.F09);
                    // 剩余期数
                    creditorInfo.setSubTerm(zrzdzqEntity.F21);
                    // 总期数
                    creditorInfo.setTotalTerm(zrzdzqEntity.F20);
                    // 年化利率
                    creditorInfo.setRate(String.valueOf(zrzdzqEntity.F23));
                    // 债权金额
                    creditorInfo.setCreditorVal(zrzdzqEntity.F14.setScale(2, BigDecimal.ROUND_HALF_UP));
                    // 转让价格
                    creditorInfo.setSalePrice(zrzdzqEntity.F03.setScale(2, BigDecimal.ROUND_HALF_UP));
                    // 状态
                    creditorInfo.setStatus(zrzdzqEntity.F22);
                    
                    zrzEntitys.add(creditorInfo);
                }
            }
        }
        
        return zrzEntitys;
    }
    
    /**
     * 已转出列表
     * 
     * @param service 债权转让接口管理类
     * @param paging 分页信息
     * @return 已转出列表
     * @throws Throwable 异常信息
     */
    private List<CreditorInfo> getYzcEntity(final ZqzrManage service, final Paging paging)
        throws Throwable
    {
        // 查询已转出列表
        PagingResult<OutSellFinacing> osfList = service.getOutSellFinacing(paging);
        
        List<CreditorInfo> yzcEntitys = null;
        if (null != osfList)
        {
            // 判断是否超出记录集
            OutSellFinacing[] outSellFinacings = null;
            if (osfList.getPageCount() >= LongParser.parse(paging.getCurrentPage()))
            {
                outSellFinacings = osfList.getItems();
            }
            
            if (null != outSellFinacings && outSellFinacings.length > 0)
            {
                yzcEntitys = new ArrayList<CreditorInfo>();
                for (OutSellFinacing outSellFinacing : outSellFinacings)
                {
                    CreditorInfo creditorInfo = new CreditorInfo();
                    // 标ID
                    creditorInfo.setBidId(outSellFinacing.F01);
                    
                    // 债权编码
                    creditorInfo.setCreditorId(outSellFinacing.zqNub);
                    
                    // 转让手续费
                    creditorInfo.setTransferPrice(outSellFinacing.F05.setScale(2, BigDecimal.ROUND_HALF_UP));
                    
                    // 转出盈亏
                    creditorInfo.setTurnOutBalance(outSellFinacing.F08.setScale(2, BigDecimal.ROUND_HALF_UP));
                    
                    // 债权金额
                    creditorInfo.setCreditorVal(outSellFinacing.F03.setScale(2, BigDecimal.ROUND_HALF_UP));
                    
                    // 转让价格
                    creditorInfo.setSalePrice(outSellFinacing.F04.setScale(2, BigDecimal.ROUND_HALF_UP));
                    
                    // 转让时间
                    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    creditorInfo.setSaleTime(sdf.format(outSellFinacing.F06));
                    
                    yzcEntitys.add(creditorInfo);
                }
            }
        }
        
        return yzcEntitys;
    }
    
    /**
     * 转入中列表
     * 
     * @param service 债权转让接口管理类
     * @param paging 分页信息
     * @return 转让中列表
     * @throws Throwable 异常信息
     */
    private List<CreditorInfo> getYzrEntity(final ZqzrManage service, final Paging paging)
        throws Throwable
    {
        // 查询转入中的债权列表
        PagingResult<InSellFinacingExt> isfList = service.getInSellFinacingExt(paging);
        
        List<CreditorInfo> yzrEntitys = null;
        if (null != isfList)
        {
            // 判断是否超出记录集
            InSellFinacingExt[] inSellFinacingExts = null;
            if (isfList.getPageCount() >= LongParser.parse(paging.getCurrentPage()))
            {
                inSellFinacingExts = isfList.getItems();
            }
            
            if (null != inSellFinacingExts && inSellFinacingExts.length > 0)
            {
                yzrEntitys = new ArrayList<CreditorInfo>();
                for (InSellFinacingExt inSellFinacingExt : inSellFinacingExts)
                {
                    CreditorInfo creditorInfo = new CreditorInfo();
                    
                    // 标ID
                    creditorInfo.setBidId(inSellFinacingExt.F02);
                    
                    // 剩余期数
                    creditorInfo.setSubTerm(inSellFinacingExt.getSubTerm());
                    
                    // 总期数
                    creditorInfo.setTotalTerm(inSellFinacingExt.getTotalTerm());
                    
                    // 年化利率
                    creditorInfo.setRate(String.valueOf(inSellFinacingExt.getRate().setScale(4,
                        BigDecimal.ROUND_HALF_UP)));
                    
                    // 债权金额
                    creditorInfo.setCreditorVal(inSellFinacingExt.F04.setScale(2, BigDecimal.ROUND_HALF_UP));
                    
                    // 转让价格
                    creditorInfo.setSalePrice(inSellFinacingExt.F05.setScale(2, BigDecimal.ROUND_HALF_UP));
                    
                    // 转入盈亏
                    creditorInfo.setTurnInBalance(inSellFinacingExt.F08.setScale(2, BigDecimal.ROUND_HALF_UP));
                    
                    // 买入时间
                    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    creditorInfo.setBuyingDate(sdf.format(inSellFinacingExt.F07));
                    
                    yzrEntitys.add(creditorInfo);
                }
            }
        }
        
        return yzrEntitys;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
