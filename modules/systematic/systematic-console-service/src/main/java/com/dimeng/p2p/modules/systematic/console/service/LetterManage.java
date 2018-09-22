package com.dimeng.p2p.modules.systematic.console.service;

import java.io.InputStream;
import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.enums.T7160_F07;
import com.dimeng.p2p.modules.systematic.console.service.entity.Letter;

public abstract interface LetterManage extends Service {

	/**
	 * 
	 * <dl>
	 * 描述： 添加发送站内信信息
	 * </dl>
	 * 
	 * @param sendType
	 *            发送对象
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 * @param userNames
	 *            发送用户
	 * @return {@code int} 自增ID
	 * @throws Throwable
	 */
	public abstract int addLetter(T7160_F07 sendType, String title,
			String content, String... userNames) throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述：根据条件分页查询所有站内信推广记录
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
	 * @return {@link PagingResult}{@code <}{@link Letter}{@code >} 查询结果
	 * @throws Throwable
	 */
	public abstract PagingResult<Letter> serarch(String title, String name,
			String beginTime, String endTime, Paging paging) throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述：根据主键ID查询站内信推广详细信息
	 * </dl>
	 * 
	 * 
	 * @param id
	 *            主键ID
	 * @return {@link Letter} 查询结果
	 * @throws Throwable
	 */
	public abstract Letter get(int id) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述： 导入用户名
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
	 * <li>导入用户名</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param inputStream
	 *            文件读取流
	 * @param charset
	 *            导入编码
	 * @return {@link String}{@code []}用户名列表
	 * @throws Throwable
	 */
	public abstract String[] importUser(InputStream inputStream, String charset)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：导出用户账号
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
	 * <li>导出用户账号</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param outputStream
	 *            文件写入流
	 * @param charset
	 *            导入编码
	 * @param id
	 *            导出的用户账号
	 * @throws Throwable
	 */
	public abstract void export(OutputStream outputStream, String charset,
			int id) throws Throwable;
	/**
     * <查询输入用户名是否存在>
     * @return
     */
	public int getCheckUserId(String userName) throws Throwable;
}
