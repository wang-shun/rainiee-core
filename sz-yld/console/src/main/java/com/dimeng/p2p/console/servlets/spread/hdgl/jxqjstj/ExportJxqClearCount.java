/*
 * 文 件 名:  ExportJxqClearCount.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月10日
 */
package com.dimeng.p2p.console.servlets.spread.hdgl.jxqjstj;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.activity.console.service.ActivityCountManage;
import com.dimeng.p2p.modules.activity.console.service.entity.JxqClearCountEntity;
import com.dimeng.p2p.modules.activity.console.service.query.JxqClearCountQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 功能描述：导出根据条件查询的[待付|已付]加息券结算统计列表 详细描述： 1. 导出根据条件查询的待付加息券结算统计列表 2.
 * 导出根据条件查询的已付加息券结算统计列表
 * 
 * @author xiaoqi
 * @version [v3.1.2, 2015年10月10日]
 */
@Right(id = "P2P_C_SPREAD_HDGL_EXPORTJXQJSTJ", name = "导出加息券结算统计列表", moduleId = "P2P_C_SPREAD_HDGL_JXQJSTJ")
public class ExportJxqClearCount extends AbstractSpreadServlet {
	private static final long serialVersionUID = -5138046322148700619L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		ActivityCountManage manage = serviceSession
				.getService(ActivityCountManage.class);
		JxqClearCountQuery query = new JxqClearCountQuery() {
			@Override
			public String userName() {
				return request.getParameter("userName");
			}

			@Override
			public String loanNum() {
				return request.getParameter("loanNum");
			}

			@Override
			public Timestamp startTime() {
				return TimestampParser.parse(request.getParameter("startTime"));
			}

			@Override
			public Timestamp endTime() {
				return TimestampParser.parse(request.getParameter("endTime"));
			}

			@Override
			public String status() {
				return request.getParameter("status");
			}
		};
		JxqClearCountEntity[] list = manage.exportJxqClearCountList(query);
		response.setHeader("Content-disposition", "attachment;filename="
				+ new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
		response.setContentType("application/csv");
		if (list == null) {
			return;
		}
		try (BufferedWriter out = new BufferedWriter(
            new OutputStreamWriter(response.getOutputStream(), "GBK"))) {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("标的ID");
            writer.write("用户名");
            writer.write("用户姓名");
            writer.write("加息利率");
            if ("DF".equals(query.status())) {
                writer.write("应付加息奖励(元)");
                writer.write("应付时间");
            } else {
                writer.write("已付加息奖励(元)");
                writer.write("已付时间");
            }
            writer.newLine();
            int index = 1;
            for (JxqClearCountEntity jcce : list) {
                writer.write(index++);
                writer.write(jcce.loanNum);
                writer.write(jcce.userName);
                writer.write(jcce.realName);
                writer.write(Formater.formatAmount(jcce.interestRate) + "%");
                writer.write(Formater.formatAmount(jcce.rewardAmount));
                writer.write(DateParser.format(jcce.payTime));
                writer.newLine();
            }
        }
	}
}
