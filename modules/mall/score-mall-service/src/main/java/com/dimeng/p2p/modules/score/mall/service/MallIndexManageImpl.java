/*
 * 文 件 名:  ScoreMallIndexManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月9日
 */
package com.dimeng.p2p.modules.score.mall.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.entities.T6351;
import com.dimeng.p2p.S63.entities.T6353;
import com.dimeng.p2p.S63.enums.T6351_F11;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.repeater.score.MallIndexManage;
import com.dimeng.p2p.repeater.score.entity.SearchGoodsCategory;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * <平台商城首页>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月9日]
 */
public class MallIndexManageImpl extends AbstractMallService implements MallIndexManage
{
    
    public MallIndexManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    /**
     * <获取用户积分>
     * @param userId
     * @param status
     * @return int
     */
    @Override
    public int getUserScoreNum(Integer userId, YesOrNo status)
        throws Throwable
    {

        return 0;
    }
    
    /**
     * 获取最近兑换订单  12条记录
     * @param maxNum n条记录
     * @return List<String>
     */
    @Override
    public List<String> getNewestMallOrderList(Integer maxNum)
        throws Throwable
    {
        ArrayList<String> list = null;
        return list;
    }
    
    /**
     * 获取平台商品
     * @param searchGoodsCategory
     * @param ajaxPage
     * @return List<T6351>
     */
    @Override
    public PagingResult<T6351> getT6351List(SearchGoodsCategory searchGoodsCategory, Paging ajaxPage)
        throws Throwable
    {
		return null;}
    
    /**
     * 获取积分商品
     * @param id
     * @return T6353
     */
    private T6353 getT6353(Integer id)
        throws Throwable
    {
		return null;}
    
    /**
     * 获取商品成交笔数
     * @param id 商品id
     * @return 
     */
    /*
    private Integer queryBuyGoodsTimes(int id,Connection connection)
    {
     try
     {
         return select(connection,
             new ItemParser<Integer>()
             {
                 @Override
                 public Integer parse(ResultSet rs)
                     throws SQLException
                 {
                     if (rs.next())
                     {
                         return rs.getInt(1);
                     }
                     return 0;
                 }
             },"SELECT COUNT(T6359.F01) FROM S63.T6359 WHERE T6359.F03=?",id);
     }
     catch (Exception e)
     {
         logger.error(e.getMessage());
         return 0;
     }
     
    }*/
    
}
