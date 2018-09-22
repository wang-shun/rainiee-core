package com.dimeng.p2p.console.servlets.account.vipmanage.zhgl;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S50.entities.T5019;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.entities.T6162;
import com.dimeng.p2p.S61.entities.T6164;
import com.dimeng.p2p.S61.entities.T6166;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.account.user.service.BankCardManage;
import com.dimeng.p2p.account.user.service.entity.Bank;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.QyManage;
import com.dimeng.p2p.modules.account.console.service.ZhglManage;
import com.dimeng.p2p.modules.base.console.service.DistrictManage;
import com.dimeng.p2p.modules.nciic.service.INciicManageService;
import com.dimeng.p2p.service.AuditFileSftpManage;
import com.dimeng.p2p.service.PtAccountManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.nciic.NciicVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

@MultipartConfig
@Right(id = "P2P_C_ACCOUNT_ADDQY", name = "新增企业账号", moduleId = "P2P_C_ACCOUNT_ZHGL", order = 2)
public class AddQy extends AbstractAccountServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		ConfigureProvider configureProvider = getResourceProvider()
				.getResource(ConfigureProvider.class);
		String ESCROW_PREFIX = configureProvider
				.getProperty(SystemVariable.ESCROW_PREFIX);
		// 新浪存管 获取银行卡编码信息和省级信息
		if (null != ESCROW_PREFIX && !ESCROW_PREFIX.isEmpty()
				&& "sina".equalsIgnoreCase(ESCROW_PREFIX)) {
			BankCardManage bankCardManage = serviceSession
					.getService(BankCardManage.class);
			Bank[] bank = bankCardManage.getBank();
			request.setAttribute("bank", bank);

			DistrictManage districtManage = serviceSession
					.getService(DistrictManage.class);
			T5019[] shengs = districtManage.getSheng();
			request.setAttribute("shengs", shengs);
		}
		forwardView(request, response, getClass());
	}

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {

		ZhglManage manage = serviceSession.getService(ZhglManage.class);
		T6161 entity = new T6161();
		entity.parse(request);
		if (!StringHelper.isEmpty(entity.F11)
				&& !StringHelper.isEmpty(entity.F12)) {
			INciicManageService nic = serviceSession
					.getService(INciicManageService.class);
			ConfigureProvider configureProvider = getResourceProvider()
					.getResource(ConfigureProvider.class);
			boolean enable = BooleanParser.parse(configureProvider
					.getProperty(NciicVariable.ENABLE));
			boolean is;
			// 不启用实名认证
			if (!enable) {
				is = true;
			} else {
				is = nic.check(entity.F12, entity.F11, "PC", 0);
			}
			if (is) {
				String ESCROW_PREFIX = configureProvider
						.getProperty(SystemVariable.ESCROW_PREFIX);
				T6166 t6166 = null;
				if (null != ESCROW_PREFIX && !ESCROW_PREFIX.isEmpty()
						&& "sina".equalsIgnoreCase(ESCROW_PREFIX)) {
					t6166 = this.uploadFile(request, response, serviceSession);
				}

				// 注册前判断平台账号是否存在，否，则增加平台账号
				PtAccountManage ptAccountManage = serviceSession
						.getService(PtAccountManage.class);
				ptAccountManage.addPtAccount();

				String realName = request.getParameter("realName");
				String lxTel = request.getParameter("lxTel");
				String email = request.getParameter("email");
				String mobile = request.getParameter("mobile");
				String temp = entity.F12.toUpperCase();
				entity.F12 = temp.substring(0, 2) + "***************";
				entity.F13 = StringHelper.encode(temp);
				T6110_F17 t6110_f17 = EnumParser.parse(T6110_F17.class,
						request.getParameter("isInvestor"));
				if (t6110_f17 == null) {
					t6110_f17 = T6110_F17.F;
				}
				int userId = manage.addQy(entity, realName, lxTel, mobile,
						email, t6110_f17);

				// 新浪存管 继续保存 企业简介 企业地址 银行卡信息
				if (null != ESCROW_PREFIX && !ESCROW_PREFIX.isEmpty()
						&& "sina".equalsIgnoreCase(ESCROW_PREFIX)) {
					t6166.F01 = userId;
					this.sinaQyInfoSave(request, response, serviceSession,
							t6166);
				}

				getController().prompt(request, response, PromptLevel.INFO,
						"<span class=\"f18\">企业账号添加成功，初始登录密码为：888888</span>");
				processGet(request, response, serviceSession);
			} else {
				throw new ParameterException("实名认证失败");
			}
		}
	}

	@Override
	protected void onThrowable(HttpServletRequest request,
			HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
		forwardView(request, response, getClass());
	}

	private void sinaQyInfoSave(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession,
			final T6166 t6166) throws Throwable {
		QyManage manage = serviceSession.getService(QyManage.class);
		T6162 t6162 = new T6162();
		t6162.F01 = t6166.F01;
		t6162.F02 = request.getParameter("summary");
		manage.updateJscl(t6162);

		String email = request.getParameter("email");
		T6164 t6164 = new T6164();
		t6164.F01 = t6166.F01;
		t6164.F02 = IntegerParser.parse(request.getParameter("xian"));
		t6164.F03 = request.getParameter("address");
		t6164.F04 = request.getParameter("lxTel");
		t6164.F06 = request.getParameter("mobile");
		t6164.F07 = request.getParameter("realName");
		manage.updateLxxx(t6164, email);

		T6114 t6114 = new T6114();
		t6114.F02 = t6166.F01;
		t6114.F03 = IntegerParser.parse(request.getParameter("bankname"));
		t6114.F04 = IntegerParser.parse(request.getParameter("bankxian"));
		t6114.F05 = request.getParameter("subbranch");
		t6114.F07 = request.getParameter("banknumber").replaceAll("\\s", "")
				.replaceAll(" ", "");
		t6114.F11 = request.getParameter("realName");
		t6114.F12 = 2;

		AuditFileSftpManage auditFileSftpManage = serviceSession
				.getService(AuditFileSftpManage.class);
		auditFileSftpManage.insertT6166(t6166, t6114);
	}

	private T6166 uploadFile(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		AuditFileSftpManage manage = serviceSession
				.getService(AuditFileSftpManage.class);
		Part part = request.getPart("file_path");// 文件路径

		String fileName = this.getFileName(part);
		String fileUrl = this.getFileUrl(fileName);
		part.write(fileUrl);
		return manage.auditFileUpload(fileUrl, fileName);
	}

	private String getFileName(Part part) throws IOException {
		String cd = part.getHeader("Content-Disposition");
		String[] cds = cd.split(";");
		String filename = cds[2].substring(cds[2].indexOf("=") + 1)
				.substring(cds[2].lastIndexOf("//") + 1).replace("\"", "");
		String ext = filename.substring(filename.lastIndexOf(".") + 1);
		if (!"zip".equalsIgnoreCase(ext)) {
			throw new LogicalException("压缩文件格式不正确，仅支持zip格式。");
		}
		return filename;
	}

	private String getFileUrl(String filename) throws IOException {
		String path = "E:\\qualificationFile" + File.separator;

		File myFolderPath = new File(path);
		File myFilePath = new File(path + filename);
		try {
			if (!myFolderPath.exists()) {
				myFolderPath.mkdir();
			}
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path + filename;
	}

}
