package com.dimeng.p2p.modules.financing.front.service.query;

import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.common.enums.CreditTerm;
import com.dimeng.p2p.common.enums.InvestType;

public interface CreditAssignmentQuery
{
    /**
     * 描述：标的类型 信用认证标xyrzb,实地认证标sdrzb,担保标jgdbb
     * 
     * @param type
     * @throws Throwable
     * 
     */
    public abstract InvestType[] getType();
    
    /**
     * 
     * 描述：剩余期限 6个月以下：1 6-12个月：2 12-24个月：3 24个月以上：4
     * 
     * @param type
     * @throws Throwable
     * 
     */
    public abstract CreditTerm[] getTerm();
    
    /**
     * 认证等级
     * 
     * @param type
     * @throws Throwable
     * 
     */
    public abstract CreditLevel[] getCreditLevel();
}
