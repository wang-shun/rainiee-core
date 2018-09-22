package com.dimeng.p2p.modules.bid.user.service;

import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S62.entities.T6272;
import com.dimeng.p2p.modules.bid.user.service.entity.AgreementSign;
import com.dimeng.p2p.modules.bid.user.service.entity.Dzxy;
import com.dimeng.p2p.modules.bid.user.service.entity.Wqxy;

/**
 * 网签协议页面相关接口
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2016年6月17日]
 */
public abstract interface AgreementSignManage extends Service
{
    
    /**
     * 查询协议保全列表
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public AgreementSign[] getAgreementSaveList()
        throws Throwable;
    
    /**
     * 获取网签协议
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract Dzxy getSignContent()
        throws Throwable;
    
    /**
     * 获取用户基本信息
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract Wqxy getUserInfo(int versionNum)
        throws Throwable;
    
    /**
     * 判断用户是否网签协议
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract boolean isNetSign()
        throws Throwable;
    
    /**
     * 判断用户是否保全协议
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract boolean isSaveAgreement()
        throws Throwable;
    
    /**
     * 插入网签数据
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract int insertSignAgreement()
        throws Throwable;
    
    /**
     * 查询网签协议保全里所需参数Map
     * <功能详细描述>
     * @param versionNum
     * @return
     * @throws Throwable
     */
    public abstract Map<String, Object> getValueMap()
        throws Throwable;
    
    /**
     * 插入网签协议内容
     * <功能详细描述>
     * @param t6272
     * @return
     * @throws Throwable
     */
    public abstract int insertAgreementContent(T6272 t6272)
        throws Throwable;
}
