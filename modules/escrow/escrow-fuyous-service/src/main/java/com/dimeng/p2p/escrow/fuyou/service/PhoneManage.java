package com.dimeng.p2p.escrow.fuyou.service;

import java.util.Map;

import com.dimeng.framework.service.Service;

/**
 * 
 * 手机号码修改
 * <富友托管>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月5日]
 */
public interface PhoneManage extends Service {
    
	/**
	 * 组装PC端修改用户手机号码请求参数
	 * @return
	 * @throws Throwable
	 */
    public abstract Map<String, String> createPhoneUri() throws Throwable;
    
    public abstract boolean phoneRetDecode(Map<String, String> params)
        throws Throwable;
    /**
     * 修改手机号
     * @param phoneNumber
     * @throws Throwable
     */
    public abstract void updatePhone(String phoneNumber) throws Throwable;
    
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeFrontLog(String type, String log)
        throws Throwable;
}
