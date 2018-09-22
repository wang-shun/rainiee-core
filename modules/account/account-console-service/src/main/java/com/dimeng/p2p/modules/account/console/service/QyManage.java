/**
 * 
 */
package com.dimeng.p2p.modules.account.console.service;

import java.sql.SQLException;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5020;
import com.dimeng.p2p.S61.entities.T6112;
import com.dimeng.p2p.S61.entities.T6113;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.entities.T6121;
import com.dimeng.p2p.S61.entities.T6122;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.entities.T6162;
import com.dimeng.p2p.S61.entities.T6163;
import com.dimeng.p2p.S61.entities.T6164;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.modules.account.console.service.entity.Attestation;
import com.dimeng.p2p.modules.account.console.service.entity.Cwzk;
import com.dimeng.p2p.modules.account.console.service.entity.Dbxxmx;
import com.dimeng.p2p.modules.account.console.service.entity.Dfxxmx;
import com.dimeng.p2p.modules.account.console.service.entity.LoanRecord;
import com.dimeng.p2p.modules.account.console.service.entity.Rzxx;
import com.dimeng.p2p.modules.account.console.service.entity.XyrzTotal;
import com.dimeng.p2p.modules.account.console.service.query.DbxxmxQuery;
import com.dimeng.p2p.modules.account.console.service.query.DfxxmxQuery;
import com.dimeng.p2p.modules.account.console.service.query.LoanRecordQuery;

/**
 * 企业用户
 * 
 */
public abstract interface QyManage extends Service
{
    
    /**
     * 根据企业id得到企业信息
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6161 getQyxx(int id)
        throws Throwable;
    
    /**
     * 修改企业信息
     * @param entity
     * @throws Throwable
     */
    public abstract void updateQyxx(T6161 entity, T6110_F17 t6110_f17, T6110_F19 t6110_f19)
        throws Throwable;
    
    /**
     * 修改企业信息(中小企业)
     * @param entity
     * @throws Throwable
     */
    public abstract void updateQyxxZxq(T6161 entity)
        throws Throwable;
    
    /**
     * 根据企业id得到企业介绍资料
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6162 getJscl(int id)
        throws Throwable;
    
    /**
     * 修改企业介绍资料
     * @param entity
     * @throws Throwable
     */
    public abstract void updateJscl(T6162 entity)
        throws Throwable;
    
    /**
     * 根据企业id得到企业联系信息
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6164 getLxxx(int id)
        throws Throwable;
    
    /**
     * 修改企业联系信息
     * @param entity
     * @throws Throwable
     */
    public abstract void updateLxxx(T6164 entity, String email)
        throws Throwable;
    
    /**
     * 新增房产信息
     * @param entity 房产信息
     * @throws Throwable
     */
    public abstract int addFcxx(T6112 entity)
        throws Throwable;
    
    /**
     * 通过ID得到房产信息
     * @param id 房产信息ID
     * @return
     * @throws Throwable
     */
    public abstract T6112 getFcxx(int id)
        throws Throwable;
    
    /**
     * 修改房产信息
     * @param entity
     * @throws Throwable
     */
    public abstract void updateFcxx(T6112 entity)
        throws Throwable;
    
    /**
     * 得到房产分页信息
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<T6112> seachFcxx(int id, Paging paging)
        throws Throwable;
    
    /**
     * 新增车产信息
     * @param entity 车产信息
     * @throws Throwable
     */
    public abstract int addCcxx(T6113 entity)
        throws Throwable;
    
    /**
     * 通过ID得到车产信息
     * @param id 车产信息ID
     * @return
     * @throws Throwable
     */
    public abstract T6113 getCcxx(int id)
        throws Throwable;
    
    /**
     * 修改车产信息
     * @param entity 车产信息
     * @throws Throwable
     */
    public abstract void updateCcxx(T6113 entity)
        throws Throwable;
    
    /**
     * 得到车产分页信息
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<T6113> seachCcxx(int id, Paging paging)
        throws Throwable;
    
    /**
     * 查询区域名称
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    public abstract String getRegion(int id)
        throws Throwable;
    
    /**
     * 根据id得到机构担保资质信息
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract String getDbzz(int id)
        throws Throwable;
    
    /**
     * 更新担保资质
     * @param entitys
     * @throws Throwable
     */
    public abstract void updateDbzz(String content, int id)
        throws Throwable;
    
    /**
     * 更新财务状况
     * @param entitys
     * @throws Throwable
     */
    public abstract void updateCwzk(Cwzk cwzk)
        throws Throwable;
    
    /**
     * 根据用户ID得到财务信息
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6163[] getCwzk(int id)
        throws Throwable;
    
    /**
     * 得到银行列表
     */
    public abstract T5020[] getBank()
        throws Throwable;
    
    /**
     * 得到对公账号
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6114 getDgzh(int id)
        throws Throwable;
    
    /**
     * 更新对公账号
     */
    public abstract void updateDgzh(T6114 entity)
        throws Throwable;
    
    /**
     * 根据id得到企业名称
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract String getName(int id)
        throws Throwable;
    
    /**
     * 检查手机号码是否存在
     * @param phone
     * @return
     * @throws Throwable
     */
    public abstract boolean isPhoneExist(String phone, int id)
        throws Throwable;
    
    /**
     * 根据企业id得到企业借款记录
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<LoanRecord> getJkxx(LoanRecordQuery loanRecordQuery, Paging paging)
        throws Throwable;
    
    /**
     * 获取法人邮箱地址 <功能详细描述>
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract String getEmail(int id)
        throws Throwable;
    
    /**
     * 得到担保机构分页信息
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Dbxxmx> seachDbjl(DbxxmxQuery dbxxmxQuery, Paging paging)
        throws Throwable;
    
    /**
     * 得到垫付记录分页信息
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Dfxxmx> seachDfjl(DfxxmxQuery dfxxmxQuery, Paging paging)
        throws Throwable;
    
    /** 
     * 获取信用认证数量
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract XyrzTotal getXyrzTotal(int userId)
        throws Throwable;
    
    /**
     * 描述：查询必要认证
     * @param userId 用户ID
     * @return {@link Attestation}{@code []} 返回信息认证列表  不存在则返回{@code null}
     * @throws Throwable
     */
    public abstract Attestation[] needAttestation(int userId)
        throws Throwable;
    
    /**
     * 描述：查询可选认证
     * @param userId 用户ID
     * @return {@link Attestation}{@code []} 返回信息认证列表  不存在则返回{@code null}
     * @throws Throwable
     */
    public abstract Attestation[] notNeedAttestation(int userId)
        throws Throwable;
    
    /** 
     * 查询附件
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6122 getAttachment(int id)
        throws Throwable;
    
    /**
     * 认证信息通过
     * @param id
     * @param userId
     * @throws Throwable
     */
    public abstract void rztg(int id, String desc)
        throws Throwable;
    
    /**
     * 认证信息不通过
     * @param id
     * @param userId
     * @throws Throwable
     */
    public abstract void rzbtg(int id, String desc)
        throws Throwable;
    
    /**
     * 根据用户id得到认证信息
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract Rzxx[] getRzxx(int id)
        throws Throwable;
    
    /** 
     * 根据认证记录id得到认证图片url
     * @param rzjlId
     * @return
     * @throws Throwable
     */
    public abstract String[] getFileCodes(int rzjlId)
        throws Throwable;
    
    /**
     * 通过认证项id得到认证记录
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6121[] rzjl(int id, int yhId)
        throws Throwable;
}
