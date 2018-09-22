package com.dimeng.p2p.modules.systematic.console.service;

import java.io.InputStream;
import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.enums.T7162_F06;
import com.dimeng.p2p.modules.systematic.console.service.entity.Sms;

public abstract interface SmsManage extends Service {
	/**
	 * <dl>
	 * 描述： 添加发送短信信息
	 * </dl>
	 * 
	 * @param sendType
	 *            发送对象
	 * @param content
	 *            发送内容
	 * @param mobiles
	 *            发送手机号码
	 * @return {@code int} 自增ID
	 * @throws Throwable
	 */
	public abstract int addSms(T7162_F06 sendType, String content,
			String... mobiles) throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述：查询所有手机号码
	 * </dl>
	 * 
	 * @return {@link String}{@code []} 手机号码列表
	 * @throws Throwable
	 */
	public String[] getUserMobiles() throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述：根据条件分页查询所有短信推广记录
	 * </dl>
	 * 
	 * 
	 * @param title
	 *            标题
	 * @param name
	 *            管理员
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param paging
	 *            分页信息
	 * @return {@link PagingResult}{@code <}{@link Sms}{@code >} 查询结果
	 * @throws Throwable
	 */
	public abstract PagingResult<Sms> serarch(String name, String beginTime,
			String endTime, Paging paging) throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述： 根据主键ID查询短信推广详细信息
	 * </dl>
	 * 
	 * 
	 * @param id
	 *            主键ID
	 * @return {@link Sms} 查询结果
	 * @throws Throwable
	 */
	public abstract Sms get(int id) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：导入手机号码
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code inputStream == null }为空 则抛出参数异常 读取文件流不存在.</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>如果{@code charset}为空 则赋值为GBK</li>
	 * <li>导入手机号码</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param inputStream
	 *            文件读取流
	 * @param charset
	 *            导入编码
	 * @return {@link String}{@code []} 手机号码列表
	 * @throws Throwable
	 */
	public abstract String[] importMobile(InputStream inputStream,
			String charset) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据短信推广ID导出手机号
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code outputStream == null} 则抛出参数异常 写入文件流不存在.</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>如果{@code charset}为空 则赋值为GBK</li>
	 * <li>{@code getSendMobile(id)}导出邮箱账号</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * 
	 * 
	 * @param outputStream
	 *            文件写入流
	 * @param charset
	 *            导入编码
	 * @param id
	 *            短信推广ID
	 * @throws Throwable
	 */
	public abstract void export(OutputStream outputStream, String charset,
			int id) throws Throwable;
	
	/**
     * <查询输入手机号码是否存在>
     * @return
     */
	public int getCheckUserId(String mobile) throws Throwable;
}
