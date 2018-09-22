/*
 * 文 件 名:  ExportHbCount.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月9日
 */
package com.dimeng.p2p.console.servlets.spread.hdgl.hbtj;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.activity.console.service.ActivityCountManage;
import com.dimeng.p2p.modules.activity.console.service.entity.ActivityCountEntity;
import com.dimeng.p2p.modules.activity.console.service.query.ActivityCountQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 功能描述：导出红包统计列表 详细描述： 1. 默认导出全部的红包列表 2. 导出根据条件查询出的红包统计列表
 * 
 * @author xiaoqi
 * @version [v3.1.2, 2015年10月9日]
 */
@Right(id = "P2P_C_SPREAD_HDGL_EXPORTHBTJ", name = "导出红包统计列表", moduleId = "P2P_C_SPREAD_HDGL_HBTJ")
public class ExportHbCount extends AbstractSpreadServlet {
	private static final long serialVersionUID = -1290911373270350397L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		this.processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		ActivityCountManage manage = serviceSession
				.getService(ActivityCountManage.class);
		ActivityCountQuery query = new ActivityCountQuery() {

			@Override
			public String userName() {
				return request.getParameter("userName");
			}

			@Override
			public Timestamp useDateEnd() {
				return TimestampParser
						.parse(request.getParameter("useDateEnd"));
			}

			@Override
			public Timestamp useDateBegin() {
				return TimestampParser.parse(request
						.getParameter("useDateBegin"));
			}

			@Override
			public String status() {
				return request.getParameter("status");
			}

			@Override
			public Timestamp presentDateEnd() {
				return TimestampParser.parse(request
						.getParameter("presentDateEnd"));
			}

			@Override
			public Timestamp presentDateBegin() {
				return TimestampParser.parse(request
						.getParameter("presentDateBegin"));
			}

			@Override
			public Timestamp outOfDateEnd() {
				return TimestampParser.parse(request
						.getParameter("outOfDateEnd"));
			}

			@Override
			public Timestamp outOfDateBegin() {
				return TimestampParser.parse(request
						.getParameter("outOfDateBegin"));
			}

			@Override
			public String loanNum() {
				return request.getParameter("loanNum");
			}

			@Override
			public String activityNum() {
				return request.getParameter("activityNum");
			}
		};
		ActivityCountEntity[] list = manage.exportActivityCountList(query,
				T6340_F03.redpacket.name());
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
            writer.write("用户名");
            writer.write("用户姓名");
            writer.write("红包金额(元)");
            writer.write("赠送时间");
            writer.write("过期时间");
            writer.write("活动ID");
            writer.write("活动名称");
            writer.write("状态");
            writer.write("使用时间");
            writer.write("标的ID");
            writer.newLine();
            int index = 1;
            for (ActivityCountEntity ace : list) {
                writer.write(index++);
                writer.write(ace.userName);
                writer.write(ace.realName);
                writer.write(Formater.formatAmount(ace.interestRate));
                writer.write(DateTimeParser.format(ace.presentDate,
                        "yyyy-MM-dd HH:mm") + "\t");
                writer.write(DateParser.format(ace.outOfDate));
                writer.write(ace.activityNum);
                writer.write(ace.activityName);
                writer.write(ace.status.getChineseName());
                writer.write(DateTimeParser.format(ace.useDate,
                        "yyyy-MM-dd HH:mm:ss") + "\t");
                writer.write(ace.loanNum);
                writer.newLine();
            }
        }
	}

}
