package com.dimeng.p2p.modules.base.console.service.entity;

import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.p2p.S50.enums.T5012_F03;
/**
 * 客服信息
 *
 */
public interface CustomerService {

	/**
	 * 客户名称.
	 * 
	 * @return {@link String}
	 */
	public String getName();

	/**
	 * 客服类型.
	 * 
	 * @return {@link T5012_F03}
	 */
	public T5012_F03 getType();

	/*** 在线代码
	 * 
	 * @return {@link String}
	 */
	public String getNumber();

	 /*** 客服号.
     * 
     * @return {@link String}
     */
    public String getQy();
	
	/**
	 * 排序值.
	 * 
	 * @return {@code int}
	 */
	public int getSortIndex();

	/**
	 * 封面图片.
	 * 
	 * @return {@link UploadFile}
	 * @throws Throwable
	 */
	public UploadFile getImage() throws Throwable;
}
