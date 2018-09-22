package com.dimeng.p2p.preservations.ebao.service;

import com.dimeng.framework.service.Service;

/**
 * 保全
 * @author God
 *
 */
public interface PreservationManager extends Service
{
	/**
	 * 合同保全
	 */
    public void contractPreservation(int preservationId) throws Throwable;
    
    /**
	 * 协议保全
	 */
    public void agreementPreservation(int preservationId) throws Throwable;
    
    /**
     * 获取借款合同保全地址
     * @param userId 用ID
     * @param bidId 标ID
     * @throws Throwable
     */
    public String getContractPreservationUrl(int userId, int bidId) throws Throwable;
    
    /**
     * 获取协议保全地址
     * @param userId 用户ID
     * @param agreeCode 协议编码
     * @throws Throwable
     */
    public String getAgreementPreservationUrl(int userId, int agreeCode) throws Throwable;
    
    /**
     * 获取合同保全地址
     * @param id id
     * @throws Throwable
     */
    public String getContractPreservationUrlById(int id) throws Throwable;
    
    /**
     * 获取债权转让合同保全地址
     * @param zqId 债权
     * @param userID 用户ID
     * @throws Throwable
     */
    public String getZqzrContractPreservationUrl(int zqId, int userId) throws Throwable;
    
    /**
     * 获取不良债权转让合同保全地址
     * @param zqId 债权ID
     * @param blzqId 不良债权Id
     * @throws Throwable
     */
    public String getBlzqzrContractPreservationUrl(int zqId, int blzqId, int userId) throws Throwable;
    
    /**
     * 获取协议保全地址
     * @param id id
     * @param userId 用户ID
     * @throws Throwable
     */
    public String getAgreementPreservationUrlById(int id, int userId) throws Throwable;
}
