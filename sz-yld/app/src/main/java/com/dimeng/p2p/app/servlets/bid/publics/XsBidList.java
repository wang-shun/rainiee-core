
package com.dimeng.p2p.app.servlets.bid.publics;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F28;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6231_F27;
import com.dimeng.p2p.S62.enums.T6231_F29;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.bid.domain.Bid;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdlb;
import com.dimeng.p2p.modules.bid.front.service.query.QyBidQuery;
import com.dimeng.util.Formater;
/**
 * 
 * 首页—新手标的
 * @author  zhongsai
 * @version  [版本号, 2018年2月6日]
 */
public class XsBidList extends AbstractAppServlet
{
    
    private static final long serialVersionUID = -6253170491455343318L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        BidManage bidManage = serviceSession.getService(BidManage.class);
        // 查询推荐标
        PagingResult<Bdlb> crePagingResult = bidManage.searchXSB(new QyBidQuery() {
			@Override
			public T6230_F20 getStatus() {
				return null;
			}
			
			@Override
			public int getRate() {
				return 0;
			}
			
			@Override
			public int getProductId() {
				return 0;
			}
			
			@Override
			public int getOrder() {
				return 0;
			}
			
			@Override
			public int getJd() {
				return 0;
			}
			
			@Override
			public int bidType() {
				return 0;
			}
		}, getPaging(1, 1));
        Bdlb[] bdlbs = crePagingResult.getItems();
        Bid bidInfo = new Bid();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        //获取标的类型
        T6211[] t6211s = bidManage.getBidType();
        if (bdlbs != null && bdlbs.length > 0)
        {
            for (Bdlb bdlb : bdlbs)
            {
                // 获取标的扩展信息
                T6231 t6231 = bidManage.getExtra(bdlb.F02);
                // 封装标的信息
                bidInfo = getBid(bdlb, t6211s, sdf, t6231);
            }
        }
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", bidInfo);
        return;
    
    }
    
    /**
     * 封装标的信息
     * 
     * @param bdlb 标的列表
     * @param t6211s 理财方式
     * @param sdf 时间格式
     * @return 标的信息
     */
    private Bid getBid(final Bdlb bdlb, final T6211[] t6211s, final SimpleDateFormat sdf, T6231 t6231)
    {
        Bid bid = new Bid();
        
        if (bdlb.F11 == T6230_F20.HKZ || bdlb.F11 == T6230_F20.YJQ || bdlb.F11 == T6230_F20.YDF)
        {
            bdlb.F06 = bdlb.F06.subtract(bdlb.F08);
            bdlb.proess = 1;
            bdlb.F08 = new BigDecimal(0);
        }
        
        // 标ID
        bid.setId(bdlb.F02);
        
        // 募集金额
        bid.setAmount(String.valueOf(bdlb.F06));
        
        // 推荐标名称
        bid.setBidTitle(bdlb.F04);
        
        // 期限
        bid.setIsDay(bdlb.F19.name());
        
        // 时间周期
        if (bdlb.F19 == T6231_F21.S)
        {
            bid.setCycle(bdlb.F20);
        }
        else
        {
            bid.setCycle(bdlb.F10);
        }
        
        // 理财方式
        for (T6211 t6211 : t6211s)
        {
            if (t6211.F01 == bdlb.F05)
            {
                bid.setFinancialType(t6211.F02);
            }
        }
        
        // 标的类型
        if (bdlb.F16 == T6230_F11.S)
        {
            bid.setFlag("保");
        }
        else if (bdlb.F17 == T6230_F13.S)
        {
            bid.setFlag("抵");
        }
        else if (bdlb.F18 == T6230_F14.S)
        {
            bid.setFlag("实");
        }
        else
        {
            bid.setFlag("信");
        }
        
        // 发布时间
        bid.setPublicDate(bdlb.F13 != null ? sdf.format(bdlb.F13) : "");
        
        // 付款方式
        bid.setPaymentType(String.valueOf(bdlb.F21));
        
        // 年化利率
        bid.setRate(String.valueOf(bdlb.F07 != null ? bdlb.F07.setScale(4, BigDecimal.ROUND_HALF_UP) : 0));
        
        // 剩余投资金额
        bid.setRemainAmount(String.valueOf(bdlb.F08));
        
        // 标的进度
        BigDecimal process = new BigDecimal(bdlb.proess).setScale(4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        if (process.compareTo(new BigDecimal(0)) == 1 && process.compareTo(new BigDecimal(1)) == -1)
        {
            bid.setProess(new BigDecimal(1));
        }
        else
        {
            bid.setProess(new BigDecimal(bdlb.proess).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
        }
        
        // 标的状态
        bid.setStatus(String.valueOf(bdlb.F11));
        
        // 筹款期限
        bid.setTerm(bdlb.F09);
        
        // 设置是否为新手标
        if (bdlb.F28 == T6230_F28.S)
        {
            bid.setIsXsb("true");
        }
        
        // 设置是否为奖励标
        if (bdlb.F29 == T6231_F27.S)
        {
            bid.setIsJlb("true");
            bid.setJlRate(bdlb.F30);
        }
        
        // 设置是否为推荐标
        if (bdlb.F33 == T6231_F29.S)
        {
            bid.setIsTjb("true");
        }
        
        // 设置是否为预发布
        if (T6230_F20.YFB.name().equals(bdlb.F11.name()))
        {
            bid.setIsYfb("true");
        }
        
        // 最小投资金额
        bid.setMinBidingAmount(Formater.formatAmount(t6231.F25));
        
        return bid;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
