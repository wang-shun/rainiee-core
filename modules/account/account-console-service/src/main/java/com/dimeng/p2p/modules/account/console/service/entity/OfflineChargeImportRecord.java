/*
 * 文 件 名:  OfflineChargeImportRecord
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  线下充值批量导入记录
 * 修 改 人:  heluzhu
 * 修改时间: 2016/8/20
 */
package com.dimeng.p2p.modules.account.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 线下充值批量导入记录
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/8/20]
 */
public class OfflineChargeImportRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 金额
     */
    private String amount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用户ID
     */
    private int userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
