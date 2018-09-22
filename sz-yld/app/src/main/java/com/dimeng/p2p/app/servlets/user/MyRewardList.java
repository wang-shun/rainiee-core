/*
 * 文 件 名:  MyHbList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月23日
 */
package com.dimeng.p2p.app.servlets.user;

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
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.account.user.service.MyRewardManage;
import com.dimeng.p2p.account.user.service.entity.MyRewardRecod;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.RewardInfo;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 我的奖励列表
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月23日]
 */
public class MyRewardList extends AbstractSecureServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3928059244572149491L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取请求参数信息
        final int currentPage = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        
        // 奖励类型  1: 红包  2: 加息券
        final String type = getParameter(request, "type");
        
        // 查询红包列表
        MyRewardManage service = serviceSession.getService(MyRewardManage.class);
        // 参数
        Map<String, Object> params = new HashMap<String, Object>();
        
        // 设置查询类型
        if ("1".equals(type))
        {
            params.put("type", T6340_F03.redpacket.name());
        }
        else if ("2".equals(type))
        {
            params.put("type", T6340_F03.interest.name());
        }
        else
        {
            // 返回参数错误
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "参数错误！");
            return;
        }
        
        PagingResult<MyRewardRecod> result = service.searchMyReward(params, getPaging(currentPage, pageSize));
        
        List<RewardInfo> rewardInfo = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (null != result)
        {
            // 判断是否超出记录集
            MyRewardRecod[] myRewardRecods = null;
            
            if (result.getPageCount() >= LongParser.parse(currentPage))
            {
                myRewardRecods = result.getItems();
            }
            
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
        
        // 封装页面信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", rewardInfo);
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
