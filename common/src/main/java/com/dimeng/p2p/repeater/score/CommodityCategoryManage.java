package com.dimeng.p2p.repeater.score;

import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.entities.T6350;
import com.dimeng.p2p.repeater.score.entity.ScoreComdCategoryExt;
import com.dimeng.p2p.repeater.score.query.CommodityCategorySearch;
/**
 * 积分商城-商品类型管理
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author  zengzhihua
 * @version  [版本号, 2015年9月1日]
 */
public interface CommodityCategoryManage extends Service
{

    /**
     * 查询商品类型列表
     * <功能详细描述>
     * @param sear
     * @param page
     * @return
     */
    public abstract PagingResult<ScoreComdCategoryExt> getScoreComdCategoryExt(CommodityCategorySearch sear, Paging page) throws Throwable;
    
    /**
     * <查询所有符合状态的商品类型列表>
     * @param F04
     * @return List<T6350>
     */
    public abstract List<T6350> getT6350List(String F04) throws Throwable;

    /**
     * 增加商品类型
     * <功能详细描述>
     * @param F02
     * @param F03
     * @param F07
     * @return
     */
    public abstract int addT6350(String F02, String F03,String F07) throws Throwable;

    /**
     * 修改商品类型
     * <功能详细描述>
     * @param t6350
     * @return
     */
    public abstract void updateT6350(T6350 t6350) throws Throwable;
    
    /**
     * 修改商品类型状态
     * <功能详细描述>
     * @param F01
     * @param F07
     * @return
     */
    public abstract void updateT6350_F07(String F01, String F07) throws Throwable;
    
    /**
     * 检查类别编号 和 名称是否唯一
     * <功能详细描述>
     * @param name 
     * @return T6350
     */
    public abstract T6350 getT6350(String name)
        throws Throwable;

}
