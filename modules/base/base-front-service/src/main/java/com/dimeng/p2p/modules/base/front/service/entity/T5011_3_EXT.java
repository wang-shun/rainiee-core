package com.dimeng.p2p.modules.base.front.service.entity;

import com.dimeng.p2p.S50.entities.T5011;

/**
 * 运营报告对象
 *
 */
public class T5011_3_EXT extends T5011
{
    
    /**
     * 信息披露附件id
     */
    public int id;
    
    /**
     * 文章ID,参考T5011.F01
     */
    public int articleId;
    
    /**
     * 附件路径
     */
    public String path;
    
    /**
     * 附件大小
     */
    public String size;
    
    /**
     * 附件名称
     */
    public String name;
    
    /**
     * 年份
     */
    public String year;
    
}
