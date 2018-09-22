package com.dimeng.p2p.modules.account.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6141_F03;
import com.dimeng.p2p.S61.enums.T6147_F04;

public class Grxx extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID,自增
     */
    public int id;
    
    /**
     * 真实姓名
     */
    public String name;
    
    /**
     * 兴趣类型
     */
    public T6141_F03 xqlx;
    
    /**
     * 身份证号码
     */
    public String sfzh;
    
    /**
     * 用户登录账号
     */
    public String userName;
    
    /**
     * 手机号码
     */
    public String phone;
    
    /**
     * 邮箱
     */
    public String email;
    
    /**
     * 用户状态
     */
    public T6110_F07 status;
    
    /**
     * 客户经理
     */
    public String kfjl;
    
    /**
     * 最后登录时间
     */
    public Timestamp loginTime;
    
    /**
     * 视频审核状态, TG:通过;BTG:不通过
     */
    public String videoExamineStatus;
    
    /**
     * 待审视频
     */
    public boolean toCheckVideo;
    
    /**
     * 是否待审核
     */
    public String dshFlg;
    
    /**
     * 交易密码
     */
    public String jymm;
    
    /**
     * 注册开始时间
     */
    public Timestamp startTime;
    
    /**
     * 注册结束时间
     */
    public Timestamp endTime;
    
    /**
     * 标志
     */
    public String zcType;
    
    /**
     * 注册来源
     */
    public T6110_F08 F08;
    
    /**
     * 业务员工号
     */
    public String employNum;
    
    /**
     * 邀请码
     */
    public String code;
    
    /**
     * 评估等级
     */
    public T6147_F04 riskAssess;
    
    /**
     * 已评估总次数
     */
    public int assessedNum;
}