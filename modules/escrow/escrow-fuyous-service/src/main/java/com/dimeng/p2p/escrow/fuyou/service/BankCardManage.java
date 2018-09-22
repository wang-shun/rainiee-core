package com.dimeng.p2p.escrow.fuyou.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.escrow.fuyou.entity.BankCard;

/**
 * 银行卡
 * @author Administrator
 *
 */
public interface BankCardManage extends Service
{
    
    /**
     * 查询添加银行卡信息列表
     * @param acount
     * @return
     * @throws Throwable
     */
    public abstract BankCard[] getBankCars(String status)
        throws Throwable;
    
    /**
     * 查询添加银行卡信息
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract BankCard getBankCar(int id)
        throws Throwable;
    
    /**
     * 查询添加银行卡信息
     * @param cardnumber 银行卡号
     * @return
     * @throws Throwable
     */
    public abstract BankCard getBankCar(String cardnumber)
        throws Throwable;
    
}
