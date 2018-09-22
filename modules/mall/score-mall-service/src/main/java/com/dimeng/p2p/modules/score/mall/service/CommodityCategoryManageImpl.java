package com.dimeng.p2p.modules.score.mall.service;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.entities.T6350;
import com.dimeng.p2p.S63.enums.T6350_F04;
import com.dimeng.p2p.S63.enums.T6350_F07;
import com.dimeng.p2p.repeater.score.CommodityCategoryManage;
import com.dimeng.p2p.repeater.score.entity.ScoreComdCategoryExt;
import com.dimeng.p2p.repeater.score.query.CommodityCategorySearch;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 积分商城-商品类型管理
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  zengzhihua
 * @version  [版本号, 2015年9月1日]
 */
public class CommodityCategoryManageImpl extends AbstractMallService implements CommodityCategoryManage
{

    public CommodityCategoryManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }

    /**
     * 查询商品列表
     * <功能详细描述>
     * @param sear
     * @param paging
     * @return
     */
    @Override
    public PagingResult<ScoreComdCategoryExt> getScoreComdCategoryExt(CommodityCategorySearch sear, Paging paging) throws Throwable{
		return null;     
      }

    /**
     * 增加商品类别
     * <功能详细描述>
     * @param F02
     * @param F03
     * @param F07
     * @return
     */
    @Override
    public int addT6350(String F02, String F03,String F07) throws Throwable {
		return 0;
    
    }
    
    /**
     * 修改商品类别
     * <功能详细描述>
     * @param t6350
     * @return
     */
    @Override
    public void updateT6350(T6350 t6350) throws Throwable{
    	
    }
    
    /**
     * 修改商品类别状态
     * <功能详细描述>
     * @param F01
     * @param F04
     * @return
     */
    @Override
    public void updateT6350_F07(String F01, String F04) throws Throwable{}

    /**
     * <查询所有符合状态的商品类型列表>
     * @param F04
     * @return List<T6350>
     */
    @Override
    public List<T6350> getT6350List(String F04)
        throws Throwable
    {
        ArrayList<T6350> list = null;
        return list;
    }

    /**
     * 检查类别编号 和 名称是否唯一
     * <功能详细描述>
     * @param name 
     * @return boolean
     */
    @Override
    public T6350 getT6350(String name)
        throws Throwable
    {
    
        T6350 record = null;
      
        return record;
    }
    
}