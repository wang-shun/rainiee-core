/*
 * 文 件 名:  ScoreMallIndexService.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月9日
 */
package com.dimeng.p2p.repeater.score;

import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.entities.T6351;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.repeater.score.entity.SearchGoodsCategory;

/**
 * <积分商城首页>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月9日]
 */
public interface MallIndexManage extends Service
{

    /**
     * <获取用户积分>
     * @param userId
     * @param status
     * @return int
     */
    public abstract int getUserScoreNum(Integer userId, YesOrNo status) throws Throwable;
    
    
    /**
     * 获取最近兑换订单  12条记录
     * @param maxNum N条记录
     * @return List<String>
     */
    public abstract List<String> getNewestMallOrderList(Integer maxNum) throws Throwable;
    
    /**
     * 获取平台商品
     * @param searchGoodsCategory
     * @param ajaxPage
     * @return List<T6351>
     */
    public abstract PagingResult<T6351> getT6351List(SearchGoodsCategory searchGoodsCategory,Paging ajaxPage) throws Throwable;
    
}
