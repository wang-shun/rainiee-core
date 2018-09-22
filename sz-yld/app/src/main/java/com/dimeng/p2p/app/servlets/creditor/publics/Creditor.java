package com.dimeng.p2p.app.servlets.creditor.publics;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5017;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6147;
import com.dimeng.p2p.S61.entities.T6148;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6148_F02;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6216;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6231_F27;
import com.dimeng.p2p.account.front.service.UserInfoManage;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.creditor.domain.CreditorInfo;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.common.enums.TermType;
import com.dimeng.p2p.modules.base.front.service.TermManage;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.TransferManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.front.service.entity.Dbxx;
import com.dimeng.p2p.modules.bid.front.service.entity.Mbxx;
import com.dimeng.p2p.modules.bid.front.service.entity.Zqzqlb;
import com.dimeng.p2p.modules.bid.front.service.entity.ZqzrEntity;
import com.dimeng.p2p.variables.defines.RegulatoryPolicyVariavle;
import com.dimeng.util.Formater;
import com.dimeng.util.parser.IntegerParser;

/**
 * 债券的详情
 * 
 * @author tanhui
 *
 */
public class Creditor extends AbstractAppServlet
{
    
    private static final long serialVersionUID = 1L;
    
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
        
        TransferManage service = serviceSession.getService(TransferManage.class);
        BidManage bidManage = serviceSession.getService(BidManage.class);
        
        final String creditorId = getParameter(request, "creditorId");
        final String bidId = getParameter(request, "bidId");
        int id = IntegerParser.parse(creditorId);
        int bId = IntegerParser.parse(bidId);
        
        if (id == 0 || bId == 0)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "参数错误！");
            return;
        }
        
        ZqzrEntity zqzr = service.getZqzrxx(id);
        Bdxq bdxq = bidManage.get(bId);
        if (bdxq == null || zqzr == null)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.NO_DATA_ERROR, "没有查出数据");
            return;
        }
        if (bdxq.F20 == T6230_F20.YJQ || bdxq.F20 == T6230_F20.HKZ || bdxq.F20 == T6230_F20.YDF)
        {
            bdxq.F05 = bdxq.F05.subtract(bdxq.F07);
            bdxq.proess = 1;
            bdxq.F07 = new BigDecimal(0);
        }
        T6231 t6231 = bidManage.getExtra(bId);
        // 标的投资记录
        T6250[] t6250 = bidManage.getRecord(bId);
        // 借款用户信息
        UserInfoManage userManage = serviceSession.getService(UserInfoManage.class);
        T6110 userInfo = userManage.getUserInfo(bdxq.F02);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
        CreditorInfo creditorInfo = new CreditorInfo();
        creditorInfo.setUserId(userInfo.F01);
        creditorInfo.setType(userInfo.F06 == T6110_F06.FZRR ? 1 : 2);
        creditorInfo.setAmount(String.valueOf(bdxq.F05));
        creditorInfo.setPeopleNum(t6250 != null ? t6250.length : 0);
        creditorInfo.setBidTitle(bdxq.F03);
        creditorInfo.setAlrAmount(String.valueOf(bdxq.F05.subtract(bdxq.F07)));
        creditorInfo.setIsDay(t6231.F21.name());
        if (T6231_F21.S == t6231.F21)
        {
            creditorInfo.setCycle(t6231.F22);
        }
        else
        {
            creditorInfo.setCycle(bdxq.F09);
        }
        creditorInfo.setTerm(bdxq.F08);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(t6231.F12);
        if (t6231.F21 == T6231_F21.S)
        {
            calendar.add(Calendar.DAY_OF_MONTH, t6231.F22);
        }
        else
        {
            calendar.add(Calendar.MONTH, bdxq.F09);
        }
        calendar.add(Calendar.DAY_OF_MONTH, bdxq.F19);
        creditorInfo.setEndTime(dateSdf.format(calendar.getTime()));
        
        creditorInfo.setPublishTime(sdf.format(bdxq.F22));
        
        if (t6231.F06 != null)
        {
            creditorInfo.setHkDate(dateSdf.format(t6231.F06));
        }
        
        // 获取待还本息
        if (bdxq.F20 == T6230_F20.HKZ || bdxq.F20 == T6230_F20.YDF)
        {
            Mbxx mbxx = bidManage.getMbxx(bId);
            creditorInfo.setDhjeAmount(Formater.formatAmount(mbxx.dhje));
            creditorInfo.setDays(t6231.F03);
        }
        
        // 若为奖励标
        if (T6231_F27.S== t6231.F27)
        {
            creditorInfo.setJlRate(String.valueOf(t6231.F28 != null ? t6231.F28.setScale(4,
                BigDecimal.ROUND_HALF_UP) : 0));
        }
        
        // 获取标的类型
        T6211[] t6211s = bidManage.getBidType();
        for (T6211 t6211 : t6211s)
        {
            if (t6211.F01 == bdxq.F04)
            {
                creditorInfo.setFinancialType(t6211.F02);
            }
        }
        creditorInfo.setIsDanBao(bdxq.F11 != null ? bdxq.F11.name() : "F");
        Dbxx dbxx = bidManage.getDB(bId);
        creditorInfo.setGuarantee(dbxx == null ? "" : dbxx.F06);
        creditorInfo.setGuaSch(bdxq.F12.getChineseName());
        creditorInfo.setPaymentType(bdxq.F10.getChineseName());
        creditorInfo.setRate(String.valueOf(String.valueOf(bdxq.F06 != null ? bdxq.F06.setScale(4,
            BigDecimal.ROUND_HALF_UP) : 0)));
        creditorInfo.setRemainAmount(String.valueOf(bdxq.F07));
        
        creditorInfo.setSalePrice(String.valueOf(zqzr.F12));
        
        // 标产品是否增加投资限制
        final Boolean isinvestLimit =
            Boolean.parseBoolean(getConfigureProvider().getProperty(RegulatoryPolicyVariavle.IS_INVEST_LIMIT));
        creditorInfo.setIsinvestLimit(isinvestLimit);
        
        // 投资人条件
        final T6216 bidProduct = bidManage.getProductById(bdxq.F32);
        creditorInfo.setRiskLevel(bidProduct.F18.getChineseName());
       
        
        // 风险等级是否匹配(默认匹配)
        boolean isRiskMatch = true;
        String userRiskLevel = null;
        
        // 读取资源文件
        ResourceProvider resourceProvider = getResourceProvider();
        
        // 读取session信息
        final Session session = resourceProvider.getResource(SessionManager.class).getSession(request, response);
        
        // 判断session中登录标识 |登录之后可查看用户可用积分、余额
        if (session != null && session.isAuthenticated())
        {
        	final T6148 t6148 = bidManage.getT6148(bidProduct.F18.name());
        	final T6147 t6147 = userManage.getT6147();
       
            
            // 投资用户风险等级小于标产品风险等级
            if ((t6147 == null && t6148.F02 != T6148_F02.BSX) || (t6147 != null && t6147.F03 < t6148.F03))
            {
                isRiskMatch = false;
            }
            userRiskLevel = t6147 == null ? T6148_F02.BSX.getChineseName() : t6147.F04.getChineseName();
        }
        
        
        // 是否显示风险投资
        final Boolean isOpenRisk =
            Boolean.parseBoolean(getConfigureProvider().getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
        creditorInfo.setOpenRisk(isOpenRisk);
        
        
        //是否显示风险邀请函
    	boolean isShowFXTS = false;
    	TermManage termManage = serviceSession.getService(TermManage.class);
    	T5017 fxtsh = termManage.get(TermType.FXTSH);
    	if (fxtsh != null)
    	{
    		isShowFXTS = true;
    	}
    	creditorInfo.setIsShowFXTS(isShowFXTS);
       
        creditorInfo.setRiskMatch(isRiskMatch);
        creditorInfo.setUserRiskLevel(userRiskLevel);
        
        int creId = IntegerParser.parse(getParameter(request, "creId"));
        Zqzqlb zqInfo = bidManage.getZqzrXq(creId);
        creditorInfo.setDsjeAmount(String.valueOf(zqInfo.dsbx));
        creditorInfo.setRemainCircle(String.valueOf(zqInfo.F23) + "/" + String.valueOf(zqInfo.F22));
        creditorInfo.setStatus(String.valueOf(zqInfo.F06));
        creditorInfo.setCreditorVal(String.valueOf(zqInfo.F03));
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", creditorInfo);
        return;
    }
}
