/*
 * 文 件 名:  GetUserBidRank.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月21日
 */
package com.dimeng.p2p.app.servlets.platinfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S70.entities.T7050;
import com.dimeng.p2p.S70.entities.T7051;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.platinfo.domain.BidRankInfo;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;

/**
 * 用户投资排行榜
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月21日]
 */
public class GetUserBidRank extends AbstractAppServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -6108720160549537497L;
    
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
        // 获取投资排行榜类型  1:按周 2:按月 3:按年
        final String type = getParameter(request, "type");
        
        // 校验排行榜类型是否为空
        if (StringHelper.isEmpty(type))
        {
            // 返回排行榜类型错误
            setReturnMsg(request, response, ExceptionCode.RANK_TYPE_ERROR, "排行榜类型错误！");
            return;
        }
        
        BidManage bidManage = serviceSession.getService(BidManage.class);
        
        List<BidRankInfo> bidRankInfos = new ArrayList<BidRankInfo>();
        if ("1".equals(type))
        {
            // 按周排行 取前10条记录
            T7050[] t7050s = bidManage.getUserBidRankForWeek(10);
            
            if (t7050s != null && t7050s.length > 0)
            {
                for (int i = 0; i < t7050s.length; i++)
                {
                    // 封装排行榜信息
                    BidRankInfo bidRankInfo = getBidRankInfo(i + 1, t7050s[i].F02, t7050s[i].F03);
                    bidRankInfos.add(bidRankInfo);
                }
            }
        }
        else if ("2".equals(type))
        {
            // 按月排行 取前10条记录
            T7051[] t7051s = bidManage.getUserBidRankForMonth(10);
            getBidRankInfos(bidRankInfos, t7051s);
        }
        else if ("3".equals(type))
        {
            // 按年排行 取前10条记录
            T7051[] records = bidManage.getUserBidRankForYear(10);
            getBidRankInfos(bidRankInfos, records);
        }
        
        // 返回排行榜列表信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", bidRankInfos);
    }
    
    /**
     * 无鉴权请求
     */
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
    
    /**
     * 隐藏用户的账号名称
     * 
     * @param userName 用户账号
     * @return 隐藏的用户名称
     */
    private String hideUserName(String userName)
    {
        if (userName != null && userName.length() > 4)
        {
            userName = userName.substring(0, 4).concat("***");
        }
        
        return userName;
    }
    
    /**
     * 封装排行榜信息
     * 
     * @param rankId 排行榜ID
     * @param userName 用户账号信息
     * @param amount 投资金额
     * @return 排行榜信息
     */
    private BidRankInfo getBidRankInfo(final int rankId, String userName, BigDecimal amount)
    {
        // 封装排行榜信息
        BidRankInfo bidRankInfo = new BidRankInfo();
        bidRankInfo.setRankId(rankId);
        bidRankInfo.setUserName(hideUserName(userName));
        bidRankInfo.setAmount(Formater.formatAmount(amount));
        
        return bidRankInfo;
    }
    
    /**
     * 封装排行榜列表信息
     * 
     * @param bidRankInfos 排行榜列表
     * @param t7051s 按月/年 获取的排行榜信息
     */
    private void getBidRankInfos(List<BidRankInfo> bidRankInfos, final T7051[] t7051s)
    {
        if (t7051s != null && t7051s.length > 0)
        {
            for (int i = 0; i < t7051s.length; i++)
            {
                // 封装排行榜信息
                BidRankInfo bidRankInfo = getBidRankInfo(i + 1, t7051s[i].F02, t7051s[i].F03);
                bidRankInfos.add(bidRankInfo);
            }
        }
    }
    
}
