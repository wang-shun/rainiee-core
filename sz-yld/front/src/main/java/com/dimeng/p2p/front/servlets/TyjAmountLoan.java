package com.dimeng.p2p.front.servlets;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.util.parser.BooleanParser;
import org.apache.log4j.Logger;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6103;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.account.user.service.MyExperienceManage;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.util.StringHelper;

/**
 * 计算投资时，追加体验金获得的收益 体验金的利息，就是金额*月利率*收益期，或者金额*日利率*收益期 收效期,如果体验金和标的收益期取小
 *
 * @author luoyi
 * @version [3.0, 2015年4月29日]
 */
public class TyjAmountLoan extends AbstractFrontServlet
{
    
    private static final long serialVersionUID = -2988720874765286778L;
    
    private static final int DECIMAL_SCALE = 9;

    private static final Logger logger = Logger.getLogger(TyjAmountLoan.class);
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    /**
     * 追加体验金获得的收益
     *
     * @author 创 建 人: luoyi
     * @author 创 建 时 间 : 2015年4月29日 下午2:14:34
     */
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 第一步:查询标相关信息
        int biaoId = 0;
        String tempBiaoId = request.getParameter("biaoId");
        if (!StringHelper.isEmpty(tempBiaoId))
        {
            biaoId = Integer.parseInt(tempBiaoId);
        }
        // 根据标的id，查询标的信息
        T6230 t6230 = null;
        BidManage bidManage = serviceSession.getService(BidManage.class);
        if (biaoId > 0)
        {
            t6230 = bidManage.findT6230ByF01(biaoId);
        }
        
        if (null == t6230)
        {
            response.getWriter().flush();
            return;
        }

        BigDecimal yqTyjAmount = new BigDecimal("0");// 所获得体验金利益
        try
        {
            // 第二步:根据标的id，查找标的扩展信息
            // 先拿T6231信息判断是否按月，按月取T6230.F09，按天取T6231.F22
            T6231 t6231 = bidManage.findT6231ByF01(t6230.F01);
            // 第三步:查询有效体验金
            MyExperienceManage myExperienceManage = serviceSession.getService(MyExperienceManage.class);
            // 根据当前登录用户,取得有效的体验金信息
            int accountId = serviceSession.getSession().getAccountId();
            List<T6103> myTyjList = myExperienceManage.findMyTyjList(accountId);
            
            int days = 0; // 借款天数
            int exper = 0; // 体验金收益数
            T6231_F21 dayOrMonth = T6231_F21.F; // 借款方式
            if (null != t6231.F21)
            {
                dayOrMonth = t6231.F21;
            }
            if (t6231.F22 > 0)
            {
                days = t6231.F22;
            }
            // 数据库当前日期
            final Date currentDate = bidManage.getCurrentDate();
            // 日利率
            BigDecimal dayRate = t6230.F06.divide(new BigDecimal(360), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
            // 月利率
            BigDecimal monthRate = t6230.F06.divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);

            int expDays = 0;
            for (T6103 t6103 : myTyjList)
            {
                Calendar dayCalendar = Calendar.getInstance();
                Calendar monthCalendar = Calendar.getInstance();
                dayCalendar.setTime(currentDate);
                monthCalendar.setTime(currentDate);
                // 按天计算还款计划
                if (T6231_F21.S == dayOrMonth)
                {
                    dayCalendar.add(Calendar.DATE, days);
                    if(!BooleanParser.parse(t6103.F16)) {
                        expDays = t6103.F07;
                    }else{
                        expDays = t6103.F07*30;
                    }
                    monthCalendar.add(Calendar.DATE,expDays);
                    if (dayCalendar.compareTo(monthCalendar) > 0) {
                        // 借款周期大于收益期，按照收益期返还利息
                        exper = expDays;
                    }else {
                        // 借款周期小于收益期，按照借款周期来算
                        exper = days;
                    }
                    yqTyjAmount = yqTyjAmount.add(t6103.F03.multiply(dayRate).multiply(new BigDecimal(exper)));
                } else {
                    if(BooleanParser.parse(t6103.F16)){
                        exper = t6230.F09 > t6103.F07 ? t6103.F07 : t6230.F09;
                        //体验金投资有效收益计算方式 按月
                        yqTyjAmount = yqTyjAmount.add(t6103.F03.multiply(monthRate).multiply(new BigDecimal(exper)));
                    }else{
                        //体验金投资有效收益计算方式 按天
                        int t6230_F09 = t6230.F09*30;
                        if(t6230_F09 > t6103.F07){
                            yqTyjAmount = yqTyjAmount.add(t6103.F03.multiply(dayRate).multiply(new BigDecimal(t6103.F07)));
                        } else{
                            yqTyjAmount = yqTyjAmount.add(t6103.F03.multiply(dayRate).multiply(new BigDecimal(t6230_F09)));
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e,e);
        }
        
        response.getWriter().println(yqTyjAmount);
        response.getWriter().flush();
        
    }
    
}
