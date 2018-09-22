package com.dimeng.p2p.pay.servlets.fuyou;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.pay.service.fuyou.WithdrawService;
import com.dimeng.p2p.pay.service.fuyou.util.StringUtils;
import com.dimeng.p2p.pay.service.fuyou.varibles.FuyouPayVaribles;

public class FuyouWithdrawRetServlet extends AbstractFuyouServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		String orderno = StringUtils.nvl(request.getParameter("orderno"));    // 商户请求流水
		String merdt = StringUtils.nvl(request.getParameter("merdt"));        // 原请求日期
		String fuorderno = StringUtils.nvl(request.getParameter("fuorderno"));// 富友流水 否富友生成的原交易流水
		String tpmerdt = StringUtils.nvl(request.getParameter("tpmerdt"));    // 退票日期
		String futporderno = StringUtils.nvl(request.getParameter("futporderno"));// 退票流水退票当日唯一流水
		String accntno = StringUtils.nvl(request.getParameter("accntno"));    // 账号 否银行卡号
		String accntnm = StringUtils.nvl(request.getParameter("accntnm"));    // 账户名称否银行卡持卡人姓名
		String bankno = StringUtils.nvl(request.getParameter("bankno"));      // 总行代码
		String amt = StringUtils.nvl(request.getParameter("amt"));            // 退票金额单位：分
		String state = StringUtils.nvl(request.getParameter("state"));        // 状态 参见交易状态码说明，1 为退票成功
		String result = StringUtils.nvl(request.getParameter("result"));      // 交易结果
		String reason = StringUtils.nvl(request.getParameter("reason"));      // 结果原因
		/**
		 * 校验值否商户号|富友分配给商户的密钥|商户请求流水|原请求日期|账号|退票金额
		 * 用竖线拼接后用MD5 散列后的16 进制文本Md5(merid+”|”+key+”|”+orderno+”|”+merdt+”|”+accntno+”|”+amt)
		 */
//		String mac = StringUtils.nvl(request.getParameter("mac"));
		
		String merId = getConfigureProvider().format(FuyouPayVaribles.DAIFU_MERID); 
		int orderId = Integer.parseInt(orderno.substring(merId.length()));
		
		WithdrawService service = serviceSession.getService(WithdrawService.class);			
		logger.info("代付订单退票返回参数商户请求流水： " + orderno + "，原请求日期： " + merdt
				+ "，富友流水： " + fuorderno
				+ "，退票日期： " + tpmerdt
				+ "，退票流水： " + futporderno
				+ "，账号： " + accntno
				+ "，账户名称： " + accntnm
				+ "，总行代码： " + bankno
				+ "，退票金额： " + amt
				+ "，状态： " + state
				+ "，交易结果： " + result
				+ "，结果原因： " + reason);
		T6501 t6501 = service.selectT6501(orderId);
		if (t6501.F03 != T6501_F03.SB) {
			service.trade(orderId, reason);
			logger.info("提现订单号[" + orderId + "]退票处理成功！");
		} else {
			logger.info("提现订单号[" + orderId + "]退票处理已完成！");
		}
		response.getWriter().write("ok");		
	}

	@Override
	protected boolean mustAuthenticated() {
		return false;
	}

}
