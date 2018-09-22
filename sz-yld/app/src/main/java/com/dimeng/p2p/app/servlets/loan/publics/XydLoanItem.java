package com.dimeng.p2p.app.servlets.loan.publics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.loan.domain.LoanType;
import com.dimeng.p2p.app.servlets.loan.domain.RepayWay;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.front.service.BidWillManage;
import com.dimeng.p2p.variables.defines.SystemVariable;

/**
 * 信用贷详情
 * 1、申请条件
 * 2、借款方式
 * 3、必要材料
 * 
 * @author  suwei
 * @version  [5.0.1, 2016年01月19日]
 */
public class XydLoanItem extends AbstractAppServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2309505299023488398L;

	@Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
	
	@Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
		final ConfigureProvider configureProvider =
	            ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
		
		final String rmin = configureProvider.getProperty(SystemVariable.CREDIT_LOAN_RATE_MIN);	// 借款最小年化利率
		final String rmax = configureProvider.getProperty(SystemVariable.CREDIT_LOAN_RATE_MAX);	// 借款最大年化利率
		final String minQx = configureProvider.getProperty(SystemVariable.CREDIT_LOAN_QX_MIN);  //信用贷借款期限最小值
		final String maxQx = configureProvider.getProperty(SystemVariable.CREDIT_LOAN_QX_MAX);  //信用贷借款期限最小值
		 //申请信用贷最小年龄
        int minAge = Integer.parseInt(configureProvider.getProperty(SystemVariable.CREDIT_LOAN_AGE_MIN));
        //申请信用贷最大年龄
        int maxAge = Integer.parseInt(configureProvider.getProperty(SystemVariable.CREDIT_LOAN_AGE_MAX));
		
		List<LoanType> loanTypeList = new ArrayList<LoanType>();
		List<RepayWay> repayWayList = new ArrayList<RepayWay>();
		// 获取所有标类型
		T6211[] result = serviceSession.getService(BidWillManage.class).getBidTypeAll();
		if (result != null) 
		{ 
			LoanType type = null;
			for (T6211 t6211 : result) 
			{
				type = new LoanType();
				type.setType(t6211.F01);
				type.setName(t6211.F02);
				
				loanTypeList.add(type);
			}
		}
		
		// 获取所有还款方式
		for (T6230_F10 f10 : T6230_F10.values())
		{
			RepayWay way = new RepayWay();
			way.setType(f10.name());
			way.setName(f10.getChineseName());
			
			repayWayList.add(way);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rmin", rmin);
		map.put("rmax", rmax);
		map.put("minQx", minQx);
		map.put("maxQx", maxQx);
		map.put("minAge", minAge);
		map.put("maxAge", maxAge);
		map.put("loanTypeList", loanTypeList);
		map.put("repayWayList", repayWayList);
		setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", map);
		return;
    }
}
