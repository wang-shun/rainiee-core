package com.dimeng.p2p.app.servlets.bid.publics;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5017;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6147;
import com.dimeng.p2p.S61.entities.T6148;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6148_F02;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6216;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F28;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6231_F27;
import com.dimeng.p2p.S62.enums.T6231_F29;
import com.dimeng.p2p.account.front.service.UserInfoManage;
//import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.bid.domain.BidInfo;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.common.enums.TermType;
import com.dimeng.p2p.modules.base.front.service.TermManage;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.front.service.entity.Dbxx;
import com.dimeng.p2p.modules.bid.front.service.entity.Mbxx;
import com.dimeng.p2p.variables.defines.RegulatoryPolicyVariavle;
import com.dimeng.util.Formater;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 标的详情
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月1日]
 */
public class Bid extends AbstractAppServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2709739925131971213L;
    
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
        BidManage bidManage = serviceSession.getService(BidManage.class);
        UserInfoManage userManage = serviceSession.getService(UserInfoManage.class);
        
        // 获取标的ID
        final String bidId = getParameter(request, "bidId");
        int id = IntegerParser.parse(bidId);
        
        if (id == 0)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "参数错误！");
            return;
        }
        
        // 获取标的详情
        Bdxq bdxq = bidManage.get(id);
        if (bdxq == null)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.NO_DATA_ERROR, "没有查出数据");
            return;
        }
        
        // 获取标的扩展信息
        T6231 t6231 = bidManage.getExtra(id);
        
        // 标的投资记录
        T6250[] t6250 = bidManage.getRecord(id);
        
        // 借款用户信息
        T6110 userInfo = userManage.getUserInfo(bdxq.F02);
        
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BidInfo bidInfo = new BidInfo();
        // 用户ID
        bidInfo.setUserId(userInfo.F01);
        // 用户类型
        bidInfo.setType(userInfo.F06 == T6110_F06.FZRR ? 1 : 2);
        
        // 发布时间
        bidInfo.setPublicDate(null != bdxq.F22 ? timeSdf.format(bdxq.F22) : "");
        
        // 如果标已结束(已结清、还款中、已垫付，则需要修改标可投金额)
        if (bdxq.F20 == T6230_F20.YJQ || bdxq.F20 == T6230_F20.HKZ || bdxq.F20 == T6230_F20.YDF)
        {
            // 标的金额 = 借款金额 -可投金额
            bdxq.F05 = bdxq.F05.subtract(bdxq.F07);
            
            // 进度为已完成
            bdxq.proess = 1;
            
            // 可投金额为0
            bdxq.F07 = new BigDecimal(0);
        }
        
        // 设置是否为新手标
        if (bdxq.xsb == T6230_F28.S)
        {
            bidInfo.setIsXsb("true");
        }
        
        // 设置是否为奖励标
        if (t6231.F27 == T6231_F27.S)
        {
            bidInfo.setIsJlb("true");
            bidInfo.setJlRate(Formater.formatRate(t6231.F28));
        }
        
        // 设置是否为推荐标
        if (t6231.F29 == T6231_F29.S)
        {
            bidInfo.setIsTjb("true");
        }
        
        // 设置标的属性
        if (bdxq.F11 == T6230_F11.S)
        {
            bidInfo.setFlag("保");
        }
        else if (bdxq.F13 == T6230_F13.S)
        {
            bidInfo.setFlag("抵");
        }
        else if (bdxq.F14 == T6230_F14.S)
        {
            bidInfo.setFlag("实");
        }
        else if (bdxq.F11 == T6230_F11.F && bdxq.F13 == T6230_F13.F && bdxq.F14 == T6230_F14.F)
        {
            bidInfo.setFlag("信");
        }
        
        // 标的可借款金额
        bidInfo.setAmount(String.valueOf(bdxq.F05));
        
        // 已投资人数
        bidInfo.setPeopleNum(t6250 != null ? t6250.length : 0);
        
        // 标的标题
        bidInfo.setBidTitle(bdxq.F03);
        
        // 已投金额
        bidInfo.setAlrAmount(String.valueOf(bdxq.F05.subtract(bdxq.F07)));
        
        // 是否是按天结算
        bidInfo.setIsDay(t6231.F21.name());
        
        // 借款周期
        if (T6231_F21.S == t6231.F21)
        {
            bidInfo.setCycle(t6231.F22);
        }
        else
        {
            bidInfo.setCycle(bdxq.F09);
        }
        
        // 借款期限
        bidInfo.setTerm(bdxq.F08);
        
        // 投资截止时间
        bidInfo.setEndTime(timeSdf.format(bdxq.jsTime));
        if (t6231.F12 != null)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(t6231.F12);
            if (t6231.F21 == T6231_F21.S)
            {
                calendar.add(Calendar.DAY_OF_MONTH, t6231.F22);
            }
            else
            {
                calendar.add(Calendar.MONTH, bdxq.F09);
            }
            calendar.add(Calendar.DAY_OF_MONTH, bdxq.F19);
            bidInfo.setEndTime(t6231.F13 != null ? dateSdf.format(t6231.F13) : dateSdf.format(calendar.getTime()));
        }
        
        // 还款日期
        if (t6231.F06 != null)
        {
            bidInfo.setHkDate(dateSdf.format(t6231.F06));
        }
        
        // 获取待还本息
        if (bdxq.F20 == T6230_F20.HKZ)
        {
            Mbxx mbxx = bidManage.getMbxx(id);
            bidInfo.setDhjeAmount(Formater.formatAmount(mbxx.dhje));
            bidInfo.setDays(t6231.F03);
        }
        
        // 标的发布时间
        bidInfo.setPublishTime(bdxq.F22 != null ? dateSdf.format(bdxq.F22) : "");
        
        // 获取标的类型
        T6211[] t6211s = bidManage.getBidType();
        for (T6211 t6211 : t6211s)
        {
            if (t6211.F01 == bdxq.F04)
            {
                bidInfo.setFinancialType(t6211.F02);
            }
        }
        
        // 获取标的担保信息
        Dbxx dbxx = bidManage.getDB(id);
        
        // 担保机构名称
        bidInfo.setGuarantee(dbxx == null ? "" : dbxx.F06);
        
        // 担保方案,BXQEDB:本息全额担保;BJQEDB:本金全额担保;
        bidInfo.setGuaSch(bdxq.F12.getChineseName());
        
        // 还款方式,DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清;DEBJ:等额本金;
        bidInfo.setPaymentType(bdxq.F10.getChineseName());
        
        // 标的年化利率
        bidInfo.setRate(String.valueOf(String.valueOf(bdxq.F06 != null ? bdxq.F06.setScale(4, BigDecimal.ROUND_HALF_UP)
            : 0)));
        
        // 可投金额
        bidInfo.setRemainAmount(String.valueOf(bdxq.F07));
        
        // 标的状态
        bidInfo.setStatus(String.valueOf(bdxq.F20));
        // if(bdxq.F20 == T6230_F20.TBZ && bdxq.jsTime.after(new Date())){
        // bidInfo.setStatus(String.valueOf(T6230_F20.DFK));
        // }
        
        // 是否有担保标识
        bidInfo.setIsDanBao(bdxq.F11 != null ? bdxq.F11.name() : "F");
        
        // 最小投资金额
        bidInfo.setMinBidingAmount(Formater.formatAmount(t6231.F25));
        
        // 最大投资金额
        bidInfo.setMaxBidingAmount(Formater.formatAmount(t6231.F26));
        
        // 标产品是否增加投资限制
        final Boolean isinvestLimit =
            Boolean.parseBoolean(getConfigureProvider().getProperty(RegulatoryPolicyVariavle.IS_INVEST_LIMIT));
        bidInfo.setIsinvestLimit(isinvestLimit);
        
        final Boolean isOpenRisk =
                Boolean.parseBoolean(getConfigureProvider().getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
        bidInfo.setOpenRisk(isOpenRisk);
        
        // 投资人条件
        final T6216 bidProduct = bidManage.getProductById(bdxq.F32);
        bidInfo.setRiskLevel(bidProduct.F18.getChineseName());
        
        // 读取session信息
        final Session session = getResourceProvider().getResource(SessionManager.class).getSession(request, response);
        
        // 判断session中登录标识
        if (session != null && session.isAuthenticated())
        {
            final T6147 t6147 = userManage.getT6147();
            final T6148 t6148 = bidManage.getT6148(bidProduct.F18.name());
            
            // 风险等级是否匹配(默认匹配)
            boolean isRiskMatch = true;
            String userRiskLevel = null;
            
            // 投资用户风险等级小于标产品风险等级
            if ((t6147 == null && t6148.F02 != T6148_F02.BSX) || (t6147 != null && t6147.F03 < t6148.F03))
            {
                isRiskMatch = false;
            }
            userRiskLevel = t6147 == null ? T6148_F02.BSX.getChineseName() : t6147.F04.getChineseName();
            bidInfo.setRiskMatch(isRiskMatch);
            bidInfo.setUserRiskLevel(userRiskLevel);
        }
        
        //是否显示风险邀请函
    	boolean isShowFXTS = false;
    	TermManage termManage = serviceSession.getService(TermManage.class);
    	T5017 fxtsh = termManage.get(TermType.FXTSH);
    	if (fxtsh != null)
    	{
    		isShowFXTS = true;
    	}
    	bidInfo.setIsShowFXTS(isShowFXTS);
    	
    	// 筹款时间
        Calendar calendar = Calendar.getInstance();
        BidManage investManage = serviceSession.getService(BidManage.class);
        Timestamp timemp = investManage.getCurrentTimestamp();
        long sytime = bdxq.jsTime.getTime() - timemp.getTime();
        if (bdxq.F20 == T6230_F20.TBZ && sytime <=0)
        {
            long hm = 1000*3600*24;
            long time = calendar.getTimeInMillis()-bdxq.F22.getTime();
            long day = time/hm;
            long hour = (time-day*hm)/(1000*3600);
            long min = (time-day*hm-hour*1000*3600)/(1000*60);
            long ss = (time-day*hm-hour*1000*3600-min*1000*60)/(1000);
            if (!Formater.formatAmount(bdxq.F07).equals(Formater.formatAmount(0.00)))
            {
                ss = 0;
            }
            StringBuffer sb = new StringBuffer();
            bidInfo.setFundDate((sb.append(day).append("天").append(hour).append("时").append(min).append("分").append(ss).append("秒")).toString());
        }
        else if (bdxq.F20 == T6230_F20.DFK)
        {
            long hm = 1000*3600*24;
            long time = t6231.F11.getTime()-bdxq.F22.getTime();
            long day = time/hm;
            long hour = (time-day*hm)/(1000*3600);
            long min = (time-day*hm-hour*1000*3600)/(1000*60);
            long ss = (time-day*hm-hour*1000*3600-min*1000*60)/(1000);
            if (!Formater.formatAmount(bdxq.F07).equals(Formater.formatAmount(0.00)))
            {
                ss = 0;
            }
            
            StringBuffer sb = new StringBuffer();
            bidInfo.setFundDate((sb.append(day).append("天").append(hour).append("时").append(min).append("分").append(ss).append("秒")).toString());
        }
        // 还清时间
        else if (bdxq.F20 == T6230_F20.YJQ)
        {
            bidInfo.setFundDate(DateParser.format(t6231.F13));
        }
        // 垫付时间
        else if (bdxq.F20 == T6230_F20.YDF)
        {
            bidInfo.setFundDate(DateParser.format(t6231.F14));
        }
        
        // 数据库当前时间
        bidInfo.setTimemp(timemp.getTime());
        
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", bidInfo);
        return;
    }
}
