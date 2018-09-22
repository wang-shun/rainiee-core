package com.dimeng.p2p.modules.systematic.console.service;

import java.io.InputStream;
import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S60.entities.T6010;
import com.dimeng.p2p.S71.enums.T7164_F07;
import com.dimeng.p2p.modules.systematic.console.service.entity.Email;

public abstract interface EmailManage extends Service {
	/**
	 * 
	 * <dl>
	 * 描述：添加发送邮件推广信息
	 * </dl>
	 * 
	 * 
	 * 
	 * @param sendType
	 *            发送对象
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 * @param emails
	 *            邮箱账号列表
	 * @return {@code int} 自增ID
	 * @throws Throwable
	 */
	public abstract int addEmail(T7164_F07 sendType, String title,
			String content, String... emails) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述： 查询所有用户邮箱账号
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>无</li>
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
	 * <li>查询{@link T6010}表</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link String}对应{@code T6010.F05}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @return {@link String}{@code []}查询结果
	 * @throws Throwable
	 */
	public String[] getUserEmails() throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述：根据条件分页查询所有邮件推广记录
	 * </dl>
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
	 * @return {@link PagingResult}{@code <}{@link Email}{@code >} 查询结果
	 * @throws Throwable
	 */
	public abstract PagingResult<Email> search(String title, String name,
			String beginTime, String endTime, Paging paging) throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述：根据主键ID查询邮件推广详细信息
	 * </dl>
	 * 
	 * 
	 * @param id
	 *            主键ID
	 * @return {@link Email} 查询结果
	 * @throws Throwable
	 */
	public abstract Email get(int id) throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述：导入邮箱账号
	 * </dl>
	 * 
	 * 
	 * @param inputStream
	 *            文件读取流
	 * @param charset
	 *            导入编码
	 * @return {@link String}{@code []}邮箱账号列表
	 * @throws Throwable
	 */
	public abstract String[] importEmail(InputStream inputStream, String charset)
			throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据邮件推广ID导出邮箱账号
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
	 * <li>导出邮箱账号</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param outputStream
	 *            输出流
	 * @param charset
	 *            编码格式
	 * @param id
	 *            邮件推广ID
	 * @throws Throwable
	 */
	public abstract void export(OutputStream outputStream, String charset,
			int id) throws Throwable;
	
	/**
     * <查询输入邮箱地址是否存在>
     * @return
     */
	public int getCheckUserId(String email) throws Throwable;
}
