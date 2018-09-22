/*
 * 文 件 名:  UnUserRewardList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月9日
 */
package com.dimeng.p2p.app.servlets.bid.publics;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6103;
import com.dimeng.p2p.S63.entities.T6342;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S63.enums.T6342_F04;
import com.dimeng.p2p.account.user.service.MyExperienceManage;
import com.dimeng.p2p.account.user.service.MyRewardManage;
import com.dimeng.p2p.account.user.service.entity.MyRewardRecod;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.RewardInfo;
import com.dimeng.p2p.modules.bid.pay.service.TenderManage;
import com.dimeng.util.Formater;
import com.dimeng.util.parser.IntegerParser;

/**
 * 未使用奖励列表
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月9日]
 */
public class UnUseAwardList extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 8720014534408257263L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
     // 获取标ID
        final int id = IntegerParser.parse(getParameter(request, "id"));
        
        // 判断此标是否使用过体验金
        TenderManage tenderManage = serviceSession.getService(TenderManage.class);
        T6103 t6103 = tenderManage.getT6103(id, 0);
        
        MyRewardManage myRewardManage = serviceSession.getService(MyRewardManage.class);
        // 用户活动,此活动是否已投资过
        T6342 t6342 = myRewardManage.getT6342(id);
        
        Map<String, Object> map = new HashMap<String, Object>();
        if (null == t6103 && null == t6342)
        {
            // 获取体验金金额
            MyExperienceManage experienceManage = serviceSession.getService(MyExperienceManage.class);
            final String experAmonut = Formater.formatAmount(experienceManage.getExperienceAmount());
            
            // 获取未使用红包列表
            List<RewardInfo> redPkgList = getRewardInfo(serviceSession, T6340_F03.redpacket);
            
            // 获取未使用加息券列表
            List<RewardInfo> jxqList = getRewardInfo(serviceSession, T6340_F03.interest);
            
            // 封装详情消息
            map.put("experAmonut", experAmonut);
            map.put("redPkgList", redPkgList);
            map.put("jxqList", jxqList);
        }
        
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", map);
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    /**
     * 获取未使用的红包或者加息券列表
     * 
     * @param serviceSession 上下文session
     * @param type 类型
     * @return 未使用列表
     * @throws Throwable 异常信息
     */
    private List<RewardInfo> getRewardInfo(final ServiceSession serviceSession, final T6340_F03 type)
        throws Throwable
    {
        MyRewardManage myRewardManage = serviceSession.getService(MyRewardManage.class);
        // 封装查询参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("status", T6342_F04.WSY);
        
        // 查询红包或者加息券列表
        PagingResult<MyRewardRecod> result = myRewardManage.searchMyReward(params, getPaging(1, Integer.MAX_VALUE));
        
        List<RewardInfo> rewardInfo = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (null != result)
        {
            MyRewardRecod[] myRewardRecods = result.getItems();
            
            if (null != myRewardRecods && myRewardRecods.length > 0)
            {
                rewardInfo = new ArrayList<RewardInfo>();
                for (MyRewardRecod record : myRewardRecods)
                {
                    RewardInfo info = new RewardInfo();
                    
                    // 红包或者加息券的ID
                    info.setId(record.F01);
                    
                    // 红包的金额
                    info.setAmount(String.valueOf(record.value.setScale(2, BigDecimal.ROUND_HALF_UP)));
                    
                    // 加息券赠送规则
                    info.setQuota(String.valueOf(record.quota.setScale(2, BigDecimal.ROUND_HALF_UP)));
                    
                    // 加息券利率
                    info.setRate(String.valueOf(record.value.setScale(4, BigDecimal.ROUND_HALF_UP)));
                    
                    // 使用规则
                    info.setInvestUseRule(String.valueOf(record.investUseRule.setScale(2, BigDecimal.ROUND_HALF_UP)));
                    
                    // 使用状态
                    info.setStatus(record.F04.name());
                    
                    // 过期时间
                    info.setTimeOut(sdf.format(record.F08));
                    
                    // 活动类型
                    info.setType(T6340_F04.parse(record.type).getChineseName());
                    
                    rewardInfo.add(info);
                }
            }
        }
        
        return rewardInfo;
    }
    
}
