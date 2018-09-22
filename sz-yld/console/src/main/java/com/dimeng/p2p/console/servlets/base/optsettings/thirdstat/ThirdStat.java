/*
 * 文 件 名:  ThirdStat.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年11月13日
 */
package com.dimeng.p2p.console.servlets.base.optsettings.thirdstat;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.entity.VariableBean;
import com.dimeng.framework.config.service.VariableManage;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.systematic.console.service.ConstantManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

/**
 * 第三方统计设置 百度统计设置
 * 
 * @author xiaoqi
 * @version [版本号, 2015年11月13日]
 */
@Right(id = "P2P_C_BASE_THIRDSTAT_SET", name = "第三方统计设置", moduleId = "P2P_C_BASE_OPTSETTINGS_THIRDSTAT")
public class ThirdStat extends AbstractBaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request, HttpServletResponse response,
			ServiceSession serviceSession) throws Throwable {
		VariableManage variableManage = serviceSession.getService(VariableManage.class);
		String key = SystemVariable.THIRD_PART_STATISTICS_SETTING.name();
		final VariableBean variableBean = variableManage.get("SYSTEM." + key);
		request.setAttribute("codeValue", variableBean);
		forward(request, response, getController().getViewURI(request, ThirdStat.class));
	}

	@Override
	protected void processPost(HttpServletRequest request, HttpServletResponse response,
			ServiceSession serviceSession) throws Throwable {
		VariableManage variableManage = serviceSession.getService(VariableManage.class);
		ConstantManage constantManage = serviceSession.getService(ConstantManage.class);
		String key = "SYSTEM." + SystemVariable.THIRD_PART_STATISTICS_SETTING.name();
		VariableBean variableBean = variableManage.get(key);
		String value = request.getParameter("codeValue");
		//过滤掉以下字符[alert,prompt,confirm,中文禁止插入数据库]
		String regex[] = new String[]{"alert","prompt","confirm","([^[\u4e00-\u9fa5]]+)"};
		boolean flag=false;
		if(!StringHelper.isEmpty(value)){
			try {
				for (int i = 0; i < regex.length; i++) {
					Pattern pattern = Pattern.compile(regex[i], Pattern.CASE_INSENSITIVE);
					Matcher matcher = pattern.matcher(value);
					if(matcher.find()){
						flag=true;
						break;
					}
				}
				if(!flag){
					variableManage.setProperty(key, value);
					constantManage.addConstantLog(key, variableBean.getDescription(),variableBean.getValue(), value);
					getController().prompt(request, response, PromptLevel.WARRING, "修改成功!");
				}else{
					getController().prompt(request, response, PromptLevel.WARRING, "内容含非法脚本!");
				}
			} catch (Throwable t) {
				getController().prompt(request, response, PromptLevel.WARRING, t.getMessage());
			}
		}else{
			getController().prompt(request, response, PromptLevel.WARRING, "输入框不能为空!");
		}
		processGet(request, response, serviceSession);
	}

	@Override
	protected void onThrowable(HttpServletRequest request, HttpServletResponse response,
			Throwable throwable) throws ServletException, IOException {
		super.onThrowable(request, response, throwable);
	}
}
