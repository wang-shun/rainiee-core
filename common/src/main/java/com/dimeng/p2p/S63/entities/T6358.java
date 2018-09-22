package com.dimeng.p2p.S63.entities;
import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;
/**
 * 
 * 平台商城购物车
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月16日]
 */
public class T6358 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 主键Id
     */
    public int F01;
    /**
     * 用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 商品ID,参考T6351.F01
     */
    public int F03;

    /** 
     * 数量
     */
    public int F04;

    /** 
     * 修改时间
     */
    public Timestamp F05;

}
