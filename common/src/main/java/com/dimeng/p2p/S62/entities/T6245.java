/*
 * 文 件 名:  T6245.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月9日
 */
package com.dimeng.p2p.S62.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6245_F05;

import java.sql.Timestamp;

/**
 * 公益标进展信息表
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月9日]
 */
public class T6245 extends AbstractEntity
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    /**
     * 自增ID
     */
   public int F01 ;
   /**
    * 创建用户ID,参考T7110.F01
    */
   public int F02 ;
   /**
    * 公益项目ID ,T6242.F01
    */
   public int F03 ;
   /**
    * 主题标题
    */
   public String  F04 ;
   /**
    * 是否发布,S:是;F:否;
    */
   public T6245_F05 F05;
   /**
    * 简要介绍
    */
    public String  F06 ;
    /**
     * 发布时间
     */
    public Timestamp F07 ;
    /**
     * 标题时间
     */
    public Timestamp F08 ;
    /**
     * 查看更多,外链地址
     */
   public String F09 ;
    
}
