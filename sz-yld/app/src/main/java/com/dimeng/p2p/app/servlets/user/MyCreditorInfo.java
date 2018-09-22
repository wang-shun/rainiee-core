/*
 * 文 件 名:  MyCreditorInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月26日
 */
package com.dimeng.p2p.app.servlets.user;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.MyZqEntity;
import com.dimeng.p2p.modules.bid.user.service.WdzqManage;
import com.dimeng.p2p.modules.bid.user.service.entity.Tbzdzq;
import com.dimeng.p2p.modules.bid.user.service.entity.ZqxxEntity;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 我的债权列表
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月26日]
 */
public class MyCreditorInfo extends AbstractSecureServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 6377495048699042899L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 查询类型
        final String type = getParameter(request, "type");
        final int currentPage = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        
        // 我的债权接口管理类
        WdzqManage wdzqManage = serviceSession.getService(WdzqManage.class);
        
        // 分页信息
        final Paging paging = getPaging(currentPage, pageSize);
        
        // 获取债权列表
        List<MyZqEntity> myZqEntitys = null;
        
        // 回款中的债权
        if ("hkz".equals(type))
        {
            // 获取债权列表
            myZqEntitys = getHkzEntity(wdzqManage, paging);
        }
        // 投资中
        else if ("tbz".equals(type))
        {
            // 获取投资中列表
            myZqEntitys = getTbzEntitys(wdzqManage, paging);
        }
        // 已结清
        else if ("yjq".equals(type))
        {
            // 获取已结清列表
            myZqEntitys = getYjqEntitys(wdzqManage, paging);
        }
        
        // 返回页面列表
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", myZqEntitys);
        return;
    }
    
    /**
     * 获取回款信息
     * 
     * @param wdzqManage 债权的服务接口
     * @param paging 分页信息
     * @return 回款信息
     * @throws Throwable 异常信息
     */
    private List<MyZqEntity> getHkzEntity(final WdzqManage wdzqManage, final Paging paging)
        throws Throwable
    {
        //得到查询结果
        PagingResult<ZqxxEntity> result = wdzqManage.getRecoverAssests(paging);
        
        List<MyZqEntity> hkzEntitys = null;
        if (null != result)
        {
            // 判断是否超出记录集
            ZqxxEntity[] zqxxEntitys = null;
            
            if (result.getPageCount() >= LongParser.parse(paging.getCurrentPage()))
            {
                zqxxEntitys = result.getItems();
            }
            
            if (null != zqxxEntitys && zqxxEntitys.length > 0)
            {
                // 债权持有多少天后可转让
                final int cyDay = IntegerParser.parse(getConfigureProvider().getProperty(SystemVariable.ZQZR_CY_DAY));
                //债权价值比例设置： 常量值为区间（比例下限-比例上限），数值最大保留两位小数。   0-0表示不限
                final String zqzrRate = getConfigureProvider().getProperty(SystemVariable.ZQZRBL);
                String[] zqzrRates = {"",""};
                if (zqzrRate.contains("-"))
                {
                    zqzrRates = zqzrRate.split("-");
                }
                else if (zqzrRate.contains("—"))
                {
                    zqzrRates = zqzrRate.split("—");
                }
                
                //比例下限
                String zqzrRatesOne =  zqzrRates[0];
                //比例上限
                String zqzrRatesTwo = zqzrRates[1];
                
                hkzEntitys = new ArrayList<MyZqEntity>();
                for (ZqxxEntity zqxxEntity : zqxxEntitys)
                {
                    MyZqEntity hkzEntity = new MyZqEntity();
                    // 标ID
                    hkzEntity.setBidId(zqxxEntity.F02);
                    
                    // 借款标题
                    hkzEntity.setBidName(zqxxEntity.F11);
                    
                    // 债权ID
                    hkzEntity.setZqId(zqxxEntity.zqid);
                    
                    // 债权编码
                    hkzEntity.setCreditorId(zqxxEntity.F01);
                    
                    // 购买价格
                    final BigDecimal gmjg = zqxxEntity.F04.setScale(2, BigDecimal.ROUND_HALF_UP);
                    hkzEntity.setGmjg(gmjg);
                    
                    // 原始债权金额(持有债权金额)
                    final BigDecimal sourcePrice = zqxxEntity.F06.setScale(2, BigDecimal.ROUND_HALF_UP);
                    hkzEntity.setSourceZqPrice(sourcePrice);
                    
                    // 年化利率
                    final BigDecimal nhl =
                        zqxxEntity.F14.multiply(new BigDecimal(100)).setScale(4, BigDecimal.ROUND_HALF_UP);
                    hkzEntity.setNhl(nhl);
                    
                    // 加息利率
                    if (zqxxEntity.jxl != new BigDecimal(0))
                    {
                        final BigDecimal jxl = zqxxEntity.jxl.setScale(4, BigDecimal.ROUND_HALF_UP);
                        hkzEntity.setJxl(jxl);
                    }
                    
                    // 待收本息
                    final BigDecimal dsbx = zqxxEntity.dsbx.setScale(2, BigDecimal.ROUND_HALF_UP);
                    hkzEntity.setDsbx(dsbx);
                    
                    // 剩余期数
                    hkzEntity.setSyqs(zqxxEntity.syqs);
                    
                    // 还款期数
                    hkzEntity.setHkqs(zqxxEntity.hkqs);
                    
                    // 下个还款日
                    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    hkzEntity.setXghkr(zqxxEntity.xghkr != null ? sdf.format(zqxxEntity.xghkr) : "");
                    
                    // 状态
                    hkzEntity.setStatus(zqxxEntity.F28.getChineseName());
                    
                    // 转让费率
                    final BigDecimal transferRate =
                        BigDecimalParser.parse(getConfigureProvider().getProperty(SystemVariable.ZQZRGLF_RATE))
                            .setScale(4, BigDecimal.ROUND_HALF_UP);
                    hkzEntity.setTransferRate(transferRate);
                    
                    // 是否正在转让
                    hkzEntity.setIsTransfered(zqxxEntity.F07.name());
                    
                    // 判断债券是否可以转让
                    Calendar rightNow = Calendar.getInstance();
                    rightNow.setTime(new Date());
                    
                    Calendar createTime = Calendar.getInstance();
                    createTime.setTime(zqxxEntity.F08);
                    createTime.add(Calendar.DAY_OF_YEAR, cyDay);
                    
                    if (createTime.compareTo(rightNow) <= 0)
                    {
                        hkzEntity.setCanTrans(true);
                    }
                    
                    hkzEntity.setZqzrRateMin(zqzrRatesOne);
                    hkzEntity.setZqzrRateMax(zqzrRatesTwo);
                    
                    hkzEntitys.add(hkzEntity);
                }
            }
        }
        
        return hkzEntitys;
    }
    
    /**
     * 获取投资中列表
     * 
     * @param wdzqManage 债权的服务接口
     * @param paging 分页信息
     * @return 回款信息
     * @throws Throwable 异常信息
     */
    private List<MyZqEntity> getTbzEntitys(final WdzqManage wdzqManage, final Paging paging)
        throws Throwable
    {
        // 得到查询结果
        PagingResult<Tbzdzq> result = wdzqManage.getLoanAssests(paging);
        
        List<MyZqEntity> tbzEntitys = null;
        if (null != result)
        {
            // 判断是否超出记录集
            Tbzdzq[] tbzdzqs = null;
            
            if (result.getPageCount() >= LongParser.parse(paging.getCurrentPage()))
            {
                tbzdzqs = result.getItems();
            }
            
            if (null != tbzdzqs && tbzdzqs.length > 0)
            {
                tbzEntitys = new ArrayList<MyZqEntity>();
                for (Tbzdzq tbzdzq : tbzdzqs)
                {
                    MyZqEntity tbzEntity = new MyZqEntity();
                    // 标ID
                    tbzEntity.setBidId(tbzdzq.F15);
                    
                    // 借款标题
                    tbzEntity.setBidName(tbzdzq.F02);
                    
                    // 借款周期
                    int jkzq = tbzdzq.F08;
                    
                    // 是否按天借款
                    if (tbzdzq.F21.name().equals(T6231_F21.S.name()))
                    {
                        // 按天借款标识
                        tbzEntity.setDay(true);
                        
                        // 借款天数
                        jkzq = tbzdzq.F22;
                    }
                    tbzEntity.setJkzq(jkzq);
                    tbzEntity.setStatus(tbzdzq.F09.name());
                    // 进度
                    final BigDecimal process =
                        (tbzdzq.F04.subtract(tbzdzq.F06)).divide(tbzdzq.F04, 5, BigDecimal.ROUND_HALF_UP)
                            .multiply(new BigDecimal(100));
                    if (process.compareTo(new BigDecimal(0)) == 1 && process.compareTo(new BigDecimal(1)) == -1)
                    {
                        tbzEntity.setProcess(new BigDecimal(1));
                    }
                    else
                    {
                        tbzEntity.setProcess(process.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_UP));
                    }
                    
                    // 标编号
                    tbzEntity.setCreditorId(tbzdzq.F16);
                    
                    // 购买价格
                    final BigDecimal gmjg = tbzdzq.F13.setScale(2, BigDecimal.ROUND_HALF_UP);
                    tbzEntity.setGmjg(gmjg);
                    
                    // 年化利率
                    final BigDecimal nhl =
                        tbzdzq.F05.multiply(new BigDecimal(100)).setScale(4, BigDecimal.ROUND_HALF_UP);
                    tbzEntity.setNhl(nhl);
                    
                    // 加息利率
                    if (tbzdzq.jxl != new BigDecimal(0))
                    {
                        final BigDecimal jxl = tbzdzq.jxl.setScale(4, BigDecimal.ROUND_HALF_UP);
                        tbzEntity.setJxl(jxl);
                    }
                    
                    // 剩余时间
                    tbzEntity.setSurTime(tbzdzq.surTime);
                    
                    tbzEntitys.add(tbzEntity);
                }
            }
        }
        
        return tbzEntitys;
    }
    
    /**
     * 获取已结清列表
     * 
     * @param wdzqManage 债权的服务接口
     * @param paging 分页信息
     * @return 回款信息
     * @throws Throwable 异常信息
     */
    private List<MyZqEntity> getYjqEntitys(final WdzqManage wdzqManage, final Paging paging)
        throws Throwable
    {
        //得到查询结果
        PagingResult<ZqxxEntity> result = wdzqManage.getSettleAssests(paging);
        
        List<MyZqEntity> yjqEntitys = null;
        if (null != result)
        {
            // 判断是否超出记录集
            ZqxxEntity[] zqxxEntitys = null;
            
            if (result.getPageCount() >= LongParser.parse(paging.getCurrentPage()))
            {
                zqxxEntitys = result.getItems();
            }
            
            if (null != zqxxEntitys && zqxxEntitys.length > 0)
            {
                yjqEntitys = new ArrayList<MyZqEntity>();
                for (ZqxxEntity zqxxEntity : zqxxEntitys)
                {
                    MyZqEntity yjqEntity = new MyZqEntity();
                    // 标ID
                    yjqEntity.setBidId(zqxxEntity.F02);
                    
                    // 借款标题
                    yjqEntity.setBidName(zqxxEntity.F11);
                    
                    // 债权编码
                    yjqEntity.setCreditorId(zqxxEntity.F01);
                    
                    // 购买金额
                    final BigDecimal gmjg = zqxxEntity.F04.setScale(2, BigDecimal.ROUND_HALF_UP);
                    yjqEntity.setGmjg(gmjg);
                    
                    // 年化利率
                    final BigDecimal nhl =
                        zqxxEntity.F14.multiply(new BigDecimal(100)).setScale(4, BigDecimal.ROUND_HALF_UP);
                    yjqEntity.setNhl(nhl);
                    
                    // 加息利率
                    if (zqxxEntity.jxl != new BigDecimal(0))
                    {
                        final BigDecimal jxl = zqxxEntity.jxl.setScale(4, BigDecimal.ROUND_HALF_UP);
                        yjqEntity.setJxl(jxl);
                    }
                    
                    // 已赚金额
                    final BigDecimal yzje = zqxxEntity.yzje.setScale(2, BigDecimal.ROUND_HALF_UP);
                    yjqEntity.setYzje(yzje);
                    
                    // 结算时间
                    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    yjqEntity.setJqsj(zqxxEntity.jqsj != null ? sdf.format(zqxxEntity.jqsj) : "");
                    
                    // 状态
                    yjqEntity.setStatus(zqxxEntity.F28.getChineseName());
                    
                    yjqEntitys.add(yjqEntity);
                }
            }
        }
        
        return yjqEntitys;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
}
