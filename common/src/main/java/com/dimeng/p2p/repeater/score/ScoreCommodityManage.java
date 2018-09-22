package com.dimeng.p2p.repeater.score;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.entities.T6351;
import com.dimeng.p2p.repeater.score.entity.ActivityRule;
import com.dimeng.p2p.repeater.score.entity.CommoditySearch;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderInfoExt;
import com.dimeng.p2p.repeater.score.entity.T6351Ext;


/**
 * 积分商城-商品管理
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  zengzhihua
 * @version  [版本号, 2015年9月1日]
 */
public interface ScoreCommodityManage extends Service
{

    /**
     * 查询商品的数量
     * <功能详细描述>
     * @param sear
     * @return
     */
    public abstract Map<String, Object> getT6351Sum(CommoditySearch sear)throws Throwable;
    
    /**
     * 查询商品列表
     * <功能详细描述>
     * @param sear
     * @param page
     * @return
     */
    public abstract PagingResult<T6351Ext> getCommodityList(CommoditySearch sear, Paging page)throws Throwable;
    
    /**
     * 增加商品
     * <功能详细描述>
     * @param obj
     * @return
     */
    public abstract  int addT6351(T6351 obj)throws Throwable;
    
    /**
     * 修改商品状态
     * <功能详细描述>
     * @param obj
     * @return
     */
    public abstract  int updateT6351(T6351 obj)throws Throwable;
    
    /**
     * 根据商品id查询指定商品对象
     * <功能详细描述>
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6351Ext getCommodityObject(int id)
        throws Throwable;
    
    /**
     * 根据商品id查询指定商品的购买记录列表
     * <功能详细描述>
     * @param id
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<ScoreOrderInfoExt> getCommodityOrderList(int id, Paging page)
        throws Throwable;
    
    /**
     * 根据商品id查询购买记录条数
     * <功能详细描述>
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract int getBuyTotal(int id)
        throws Throwable;

    /**
     * 导出商品列表
     * <功能详细描述>
     * @param
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public abstract void export(PagingResult<T6351Ext> results, OutputStream outputStream, String charset,String state)
            throws Throwable;

    /**
     * 查询活动规则
     * @param jlType
     * @param hdType
     * @return
     * @throws Throwable
     */
    public abstract List<ActivityRule> selectRules(String jlType, String hdType) throws Throwable;

}
