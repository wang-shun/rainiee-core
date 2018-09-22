package com.dimeng.p2p.modules.nciic.entity;

import java.util.List;

/*
 * 文 件 名:  QueryListEntity.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  YINKE
 * 修改时间:  2015年10月20日
 */
public class QueryListEntity
{
    private List<QueryEntity> query;
    
    public List<QueryEntity> getQuery()
    {
        return query;
    }
    
    public void setQuery(List<QueryEntity> query)
    {
        this.query = query;
    }
}
