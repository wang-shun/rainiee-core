/**
 * 
 */
package com.dimeng.p2p.modules.account.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.enums.T5127_F03;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6112;
import com.dimeng.p2p.S61.entities.T6113;
import com.dimeng.p2p.S61.entities.T6121;
import com.dimeng.p2p.S61.entities.T6122;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.entities.T6142;
import com.dimeng.p2p.S61.entities.T6143;
import com.dimeng.p2p.S61.entities.T6145;
import com.dimeng.p2p.S61.enums.T6145_F07;
import com.dimeng.p2p.S71.entities.T7110;
import com.dimeng.p2p.modules.account.console.service.entity.Attestation;
import com.dimeng.p2p.modules.account.console.service.entity.BasicInfo;
import com.dimeng.p2p.modules.account.console.service.entity.Dbxxmx;
import com.dimeng.p2p.modules.account.console.service.entity.Dfxxmx;
import com.dimeng.p2p.modules.account.console.service.entity.Grxx;
import com.dimeng.p2p.modules.account.console.service.entity.LoanRecord;
import com.dimeng.p2p.modules.account.console.service.entity.Rzxx;
import com.dimeng.p2p.modules.account.console.service.entity.T6143EX;
import com.dimeng.p2p.modules.account.console.service.entity.TenderRecord;
import com.dimeng.p2p.modules.account.console.service.query.DbxxmxQuery;
import com.dimeng.p2p.modules.account.console.service.query.DfxxmxQuery;
import com.dimeng.p2p.modules.account.console.service.query.LoanRecordQuery;
import com.dimeng.p2p.modules.account.console.service.query.TenderRecordQuery;

/**
 * 个人用户
 * 
 */
public abstract interface GrManage extends Service
{
    /**
     * 
     * <dt>
     * <dl>
     * 描述：查询所用个人用户列表.
     * </dl>
     * 
     * @param userQuery
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Grxx> search(Grxx userQuery, Paging paging)
        throws Throwable;
    
    /**
     * 分页查询个人用户列表，含视频认证信息
     * @param userQuery
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Grxx> searchGrxx(Grxx userQuery, Paging paging)
        throws Throwable;
    
    /**
     * 
     * 
     * 描述：查询用户详细情况
     * @param userId 用户ID
     * @return {@link BasicInfo} 基本信息   不存在则返回{@code null}
     * @throws Throwable
     */
    public abstract BasicInfo findBasicInfo(int userId)
        throws Throwable;
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述：修改手机号码、邮箱
     * </dl>
     * 
     * @param user
     *            修改信息
     * @throws Throwable
     */
    public abstract void update(T6110 user)
        throws Throwable;
    
    /**
     * 查询用户的账号信息
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract T6110 getT6110(int userId)
        throws Throwable;
    
    /**
     * 查询个人基础信息
     */
    public abstract T6141 getPersonal(int userId)
        throws Throwable;
    
    /**
     * 查询个人学历记录
     */
    public abstract T6142 getXl(int userId)
        throws Throwable;
    
    /**
     * 查询个人工作记录
     */
    public abstract T6143 getWork(int userId)
        throws Throwable;
    
    /**
     * 
     * 描述：修改个人基础信息.
     * 
     * @param t6141
     * @throws Throwable
     */
    public abstract void updatePersonalBase(T6141 t6141)
        throws Throwable;
    
    /**
     * 修改个人学历信息.
     */
    public abstract void updatePersonalXl(T6142 t6142)
        throws Throwable;
    
    /**
     * 修改个人工作信息.
     */
    public abstract void updatePersonalWork(T6143 t6143)
        throws Throwable;
    
    /**
     * 查询个人用户信息.
     */
    public abstract Grxx getUser(int userId)
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
     * 描述：查询借款记录
     * @param loanRecordQuery 查询条件
     * @param paging 分页参数
     * @return {@link PagingResult}{@code <}{@link LoanRecord}{@code >} 分页查询结果,没有结果则返回{@code null}
     * @throws Throwable
     */
    public abstract PagingResult<LoanRecord> findLoanRecord(LoanRecordQuery loanRecordQuery, Paging paging)
        throws Throwable;
    
    /**
     * 描述：查询投资记录
     * @param tenderRecordQuery 查询条件
     * @param paging 分页参数
     * @return {@link PagingResult}{@code <}{@link TenderRecord}{@code >} 分页查询结果,没有结果则返回{@code null}
     * @throws Throwable
     */
    public abstract PagingResult<TenderRecord> findTenderRecord(TenderRecordQuery tenderRecordQuery, Paging paging)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：查询附件
     * </dl>
     * </dt>
     * 
     * @param id
     *            附件ID
     * @throws Throwable
     */
    public abstract T6122 getAttachment(int id)
        throws Throwable;
    
    public abstract T6122 get(int id)
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
     * 通过认证项id得到认证记录
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6121[] rzjl(int id)
        throws Throwable;
    
    /**
     * 通过认证项id得到认证记录
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6121[] rzjl(int id, int yhId)
        throws Throwable;
    
    /**
     * 分页查询客户经理
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract T7110[] getKfjl()
        throws Throwable;
    
    /**
     * 用户视频审核
     * @param accountId
     * @param videoId
     * @param opinion 
     * @param result
     * @throws Throwable
     */
    public abstract void videoExamine(int accountId, int videoId, T6145_F07 result, String opinion)
        throws Throwable;
    
    /**
     * 获取用户待审核的视频
     * @param accountId
     */
    public abstract T6145 getVideo(int accountId)
        throws Throwable;
    
    /**
     * 获取用户上传视频列表
     * @param accountId
     * @return
     * @throws Throwable
     */
    public abstract T6145[] getHistoryVideos(int accountId)
        throws Throwable;
    
    /**
     * 查看单个认证视频
     * @param videoId
     * @return
     * @throws Throwable
     */
    public abstract T6145 getSingleVideo(int videoId)
        throws Throwable;
    
    /**
     * 得到用户的借款等级
     * @return
     * @throws Throwable
     */
    public T5127_F03 getJkdj(int id)
        throws Throwable;
    
    /**
     * 得到用户的投资等级
     * @return
     * @throws Throwable
     */
    public T5127_F03 getTzdj(int id)
        throws Throwable;
    
    /**
     * 用户的借款总金额
     * @return
     * @throws Throwable
     */
    public BigDecimal getJkje(int id)
        throws Throwable;
    
    /**
     * 用户的投资总金额
     * @return
     * @throws Throwable
     */
    public BigDecimal getTzje(int id)
        throws Throwable;
    
    /**
    * 根据用户id得到分页认证信息
    * @param id
    * @return
    * @throws Throwable
    */
    public abstract PagingResult<Rzxx> getRzxx(int id, Paging paging)
        throws Throwable;
    
    /**
     * 导出个人信息列表
     * <功能详细描述>
     * @param users
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public void export(Grxx[] grxxs, OutputStream outputStream, String charset)
        throws Throwable;
    
    /**
     * 得到学历分页信息
     * @param page
     * @param userId
     * @return
     * @throws Throwable
     */
    PagingResult<T6142> seachXlxx(Paging paging, int userId)
        throws Throwable;
    
    /**
     * 得到工作分页信息
     * @param page
     * @return
     * @throws Throwable
     */
    PagingResult<T6143EX> seachGzxx(Paging paging, int userId)
        throws Throwable;
    
    /**
     * 得到房产分页信息
     * @param page
     * @return
     * @throws Throwable
     */
    PagingResult<T6112> seachFcxx(Paging paging, int userId)
        throws Throwable;
    
    /**
     * 得到车产分页信息
     * @param page
     * @return
     * @throws Throwable
     */
    PagingResult<T6113> seachCcxx(Paging paging, int userId)
        throws Throwable;
    
    /**
     * 查询区域名称
     *
     * @param id
     * @return
     * @throws SQLException
     */
    String getRegion(int id)
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
}
