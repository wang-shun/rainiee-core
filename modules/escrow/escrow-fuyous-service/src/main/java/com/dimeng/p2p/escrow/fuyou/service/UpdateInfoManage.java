package com.dimeng.p2p.escrow.fuyou.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.escrow.fuyou.entity.ComUpdate;
import com.dimeng.p2p.escrow.fuyou.entity.UserUpdateEntity;

public interface UpdateInfoManage extends Service {

	/**
	 * 个人用户信息更新请求解析
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	public abstract  UserUpdateEntity userUpdateReqDecode(
			HttpServletRequest request) throws Throwable ;
	
	
	/**
	 * 法人信息更新请求解析
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	public abstract ComUpdate  legalPerUpdateReqDecode(
	HttpServletRequest request) throws Throwable ;
	
	/**
	 * 根据用户ID更新银行卡信息
	 * @param userId
	 * @param capAcntNo
	 * @param city_id
	 * @param parent_bank_id
	 * @param bank_nm
	 * @throws Throwable
	 */
	public void updateInfo(String mobile_no,String capAcntNo,String city_id,
			String parent_bank_id,String bank_nm,String cust_name,int isPersion) throws Throwable ;
	
	/**
	 * 加密得到签名字符串
	 * @param map
	 * @return
	 * @throws Throwable
	 */
	public String asynQuery(HashMap<String, String> map) throws Throwable;
	
	/**
	 * 查询卡号是否成成设置
	 * @param userIdStr
	 * @param capAcntNo
	 * @return
	 * @throws Throwable
	 */
	public boolean setCardNumSuccessed(String userIdStr, String capAcntNo,String city_id,
			String parent_bank_id,String bank_nm )throws Throwable;
}
