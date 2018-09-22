package com.dimeng.p2p.S63.enums;
import com.dimeng.util.StringHelper;
/**
 * 
 * 订单状态
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月17日]
 */
public enum T6359_F08{

    /**
     * 待审核
     */
    pendding("待审核"), 
    /**
     * 待发货
     */
    pass("待发货"), 
    /**
     * 审核不通过
     */
    nopass("审核不通过"),
    /**
     * 已发货
     */
    sended("已发货"), 
    /**
     * 已退货
     */
    returned("已退货"),
    /**
     * 申请退款
     */
    refunding("申请退款中"),
    /**
     * 拒绝退款
     */
    norefund("拒绝退款"),
    /**
     * 已退款
     */
    refund("已退款");

    protected final String chineseName;

    private T6359_F08(String chineseName){
        this.chineseName = chineseName;
    }
    /**
     * 获取中文名称.
     *
     * @return {@link String}
     */
    public String getChineseName() {
        return chineseName;
    }
    /**
     * 解析字符串.
     *
     * @return {@link T6359_F08}
     */
    public static final T6359_F08 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6359_F08.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
