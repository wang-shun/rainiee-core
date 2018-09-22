/*
 * 文 件 名:  OperationData.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月23日
 */
package com.dimeng.p2p.app.servlets.platinfo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6196;
import com.dimeng.p2p.S61.entities.T6197;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.entity.IndexStatic;
import com.dimeng.p2p.repeater.policy.OperateDataManage;
import com.dimeng.p2p.repeater.policy.entity.AgeDistributionEntity;
import com.dimeng.p2p.repeater.policy.entity.InvestmentLoanEntity;
import com.dimeng.p2p.repeater.policy.entity.PlatformRiskControlEntity;
import com.dimeng.p2p.repeater.policy.entity.VolumeTimeLimit;
import com.dimeng.p2p.repeater.policy.entity.VolumeType;

/**
 * 平台运营数据统计
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月23日]
 */
public class OperationData extends AbstractAppServlet
{
    private static final long serialVersionUID = 7827584078909570569L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        // 投资统计数据
        final BidManage bidManage = serviceSession.getService(BidManage.class);
        IndexStatic indexStatic = bidManage.getIndexStatic();
        
        // 平台统计数据
        OperateDataManage manage = serviceSession.getService(OperateDataManage.class);
        final T6196 t6196 = manage.getT6196();
        
        // 投资借款用户分布
        final InvestmentLoanEntity investmentLoanEntity = manage.getInvestmentLoanData();
        
        List<T6197> t6197s = manage.getT6197List();
        BigDecimal[] totals = manage.getTotalInvestAmount();
        
        // 平台风险管控
        PlatformRiskControlEntity prce = manage.getPlatformRCE();
        
        Map<String, Object> statics = new HashMap<String, Object>();
        // 累计投资金额
        statics.put("totalInvestAmount", indexStatic.rzzje.add(t6196.F02));
        
        // 累计赚取收益
        statics.put("totalEarnAmount", indexStatic.yhzsy.add(t6196.F04));
        
        // 累计代偿金额
        statics.put("totalCompensatoryAmount", t6196.F23.add(prce.totalAdvancedAmount));
        
        // 累计成交笔数
        int tradeCount = manage.getTradeCount();
        tradeCount = tradeCount + t6196.F05;
        statics.put("tradeCount", tradeCount);
        
        // 投资人
        //statics.put("totalInvestment", investmentLoanEntity.totalInvestment + t6196.F06);
        statics.put("totalInvestment", indexStatic.yhjys.add(new BigDecimal(t6196.F06)));
        
        // 借款人
        statics.put("totalLoan", investmentLoanEntity.totalLoan + t6196.F07);
        
        // 借款逾期率
        statics.put("loanOverdueBalanceRate", String.valueOf(prce.loanOverdueBalanceRate.add(t6196.F27)));
        
        // 借款坏账率
        statics.put("loanBadDebtRate", String.valueOf(prce.loanBadDebtRate.add(t6196.F28)));
        
        // 最大单户借款余额占比
        statics.put("maxUserLoanBalanceProportion", String.valueOf(prce.maxUserLoanBalanceProportion.add(t6196.F24)));
        
        // 最大10户借款余额占比
        statics.put("maxTenUsersLoanBalancePropertion",
            String.valueOf(prce.maxTenUsersLoanBalancePropertion.add(t6196.F25)));
        
        List<String> times = null;
        List<BigDecimal> totalInvests = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        if (null != t6197s && t6197s.size() > 0)
        {
            times = new ArrayList<String>();
            totalInvests = new ArrayList<BigDecimal>();
            
            // 只需获取最近半年的投资记录数据
            for (int i = 6; i < t6197s.size(); i++)
            {
                // 累计金额
                BigDecimal totalInvestAmount = t6197s.get(t6197s.size() - 1 - i).F02.add(totals[t6197s.size() - i - 1]);
                totalInvests.add(totalInvestAmount);
                
                // 统计时间
                String time = sdf.format(t6197s.get(t6197s.size()-1-i).F03);
                times.add(time);
            }
        }
        statics.put("totalInvests", totalInvests);
        statics.put("times", times);
        
        // 累积注册总人数
        int totalRegister = manage.getRegisterUser() + t6196.F03;
        statics.put("totalRegister", totalRegister);
        
        // 平台用户年龄分布
        AgeDistributionEntity[] ageDistributions=manage.getAgeRanageData();
        List<Integer> ageData = new ArrayList<Integer>();
        ageData.add(t6196.F08 + ageDistributions[0].number);
        ageData.add(t6196.F09 + ageDistributions[1].number);
        ageData.add(t6196.F10 + ageDistributions[2].number);
        ageData.add(t6196.F11 + ageDistributions[3].number);
        ageData.add(t6196.F12 + ageDistributions[4].number);
        List<String> ageType = new ArrayList<String>();
        ageType.add("90后");
        ageType.add("80后");
        ageType.add("70后");
        ageType.add("60后");
        ageType.add("其他");
        statics.put("ageData", ageData);
        statics.put("ageType", ageType);
        
        // 项目期限分布
        VolumeTimeLimit[] volumeTimeLimits =manage.getVolumeTimeLimits();
        List<BigDecimal> timeLimtsData = new ArrayList<BigDecimal>();
        timeLimtsData.add(t6196.F13.add(volumeTimeLimits[0].amount));
        timeLimtsData.add(t6196.F14.add(volumeTimeLimits[1].amount));
        timeLimtsData.add(t6196.F15.add(volumeTimeLimits[2].amount));
        timeLimtsData.add(t6196.F16.add(volumeTimeLimits[3].amount));
        timeLimtsData.add(t6196.F17.add(volumeTimeLimits[4].amount));
        timeLimtsData.add(t6196.F18.add(volumeTimeLimits[5].amount));
        List<String> timeLimtsType = new ArrayList<String>();
        timeLimtsType.add("0-3个月");
        timeLimtsType.add("3-6个月");
        timeLimtsType.add("6-9个月");
        timeLimtsType.add("9-12个月");
        timeLimtsType.add("12-24个月");
        timeLimtsType.add("24个月以上");
        statics.put("timeLimtsData", timeLimtsData);
        statics.put("timeLimtsType", timeLimtsType);
        
        // 项目类型分布
        VolumeType[] volumeTypes =manage.getVolumeTypes();
        List<BigDecimal> projectTypeData = new ArrayList<BigDecimal>();
        projectTypeData.add(t6196.F19.add(volumeTypes[0].amount));
        projectTypeData.add(t6196.F20.add(volumeTypes[1].amount));
        projectTypeData.add(t6196.F21.add(volumeTypes[2].amount));
        projectTypeData.add(t6196.F22.add(volumeTypes[3].amount));
        List<String> projectTypeType = new ArrayList<String>();
        projectTypeType.add("机构担保标");
        projectTypeType.add("抵押认证标");
        projectTypeType.add("实地认证标");
        projectTypeType.add("信用认证标");
        statics.put("projectTypeData", projectTypeData);
        statics.put("projectTypeType", projectTypeType);
        
        // 借款逾期金额
        statics.put("loanOverdueBalanceAmount", String.valueOf(t6196.F26.add(prce.loanOverdueBalanceAmount)));
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", statics);
        return;
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
