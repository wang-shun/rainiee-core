package com.dimeng.p2p.app.servlets.bid.publics;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5017;
import com.dimeng.p2p.S50.entities.T5019;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6163;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6237;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.account.front.service.EnterpriseInfoManage;
import com.dimeng.p2p.account.front.service.UserInfoManage;
import com.dimeng.p2p.account.front.service.entity.Enterprise;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.bid.domain.Dyinfo;
import com.dimeng.p2p.app.servlets.bid.domain.Dysx;
import com.dimeng.p2p.app.servlets.bid.domain.QyFinance;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.common.enums.TermType;
import com.dimeng.p2p.modules.base.front.service.DistrictManage;
import com.dimeng.p2p.modules.base.front.service.TermManage;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdylx;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdysx;
import com.dimeng.p2p.modules.bid.front.service.entity.Dbxx;
import com.dimeng.p2p.variables.defines.RegulatoryPolicyVariavle;
import com.dimeng.util.parser.IntegerParser;

/**
 * 借款方的信息
 * @author tanhui
 *
 */
public class BidItem extends AbstractAppServlet
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
        logger.info("|BidItem-in|");
        final String bidId = getParameter(request, "bidId");
        int id = IntegerParser.parse(bidId);
        
        if (id == 0)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "参数错误！");
            return;
        }
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
        //标信息
        BidManage bidManage = serviceSession.getService(BidManage.class);
        EnterpriseInfoManage eim = serviceSession.getService(EnterpriseInfoManage.class);
        Bdxq bdxq = bidManage.get(id);
        if (bdxq == null)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.NO_DATA_ERROR, "没有查出数据");
            return;
        }
        
        UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
        //标的扩展信息
        T6231 t6231 = bidManage.getExtra(bdxq.F01);
        //用户信息
        T6110 t6110 = userInfoManage.getUserInfo(bdxq.F02);
        
        if (bdxq.F20 == T6230_F20.YJQ || bdxq.F20 == T6230_F20.HKZ || bdxq.F20 == T6230_F20.YDF)
        {
            bdxq.F05 = bdxq.F05.subtract(bdxq.F07);
        }
        
        com.dimeng.p2p.app.servlets.bid.domain.BidItem bidItem = new com.dimeng.p2p.app.servlets.bid.domain.BidItem();
        String qyName = "";
        if (t6110.F06 == T6110_F06.ZRR)
        {//个人借款
            if (t6110.F02 != null && t6110.F02.length() > 4)
            {
                qyName = t6110.F02.substring(0, 4) + "***";
            }
            else
            {
                qyName = t6110.F02;
            }
            bidItem.setQyName(qyName);
            bidItem.setIsQy("F");
        }
        else
        {//企业借款
         //企业信息
            Enterprise enterprise = eim.get(bdxq.F02);
            if (enterprise.F04 != null && enterprise.F04.length() > 4)
            {
                qyName = "***" + enterprise.F04.substring(enterprise.F04.length() - 4, enterprise.F04.length());
            }
            else
            {
                qyName = enterprise.F04;
            }
            
            bidItem.setIsQy("S");
            bidItem.setQyName(qyName);
            bidItem.setRegYear(String.valueOf(enterprise.F07));
            bidItem.setRegAmount(String.valueOf(enterprise.F08));
            bidItem.setEarnAmount(String.valueOf(enterprise.F14));
            bidItem.setCash(String.valueOf(enterprise.F15));
            bidItem.setBusiness(enterprise.F09 == null ? "" : enterprise.F09);
            bidItem.setOperation(enterprise.F17 == null ? "" : enterprise.F17);
            bidItem.setComplaints(enterprise.F18 == null ? "" : enterprise.F18);
            bidItem.setCredit(enterprise.F19 == null ? "" : enterprise.F19);
            
            EnterpriseInfoManage enterpriseInfo = serviceSession.getService(EnterpriseInfoManage.class);
            T6163[] t6163s = enterpriseInfo.search(bdxq.F02);
            List<QyFinance> qyFinances = new ArrayList<QyFinance>();
            if (t6163s != null && t6163s.length > 0)
            {
                for (T6163 t6163 : t6163s)
                {
                    QyFinance qyFinance = new QyFinance();
                    qyFinance.setNetAssets(String.valueOf(t6163.F07));
                    qyFinance.setProfit(String.valueOf(t6163.F05));
                    qyFinance.setRemark(t6163.F08);
                    qyFinance.setRevenue(String.valueOf(t6163.F03));
                    qyFinance.setTotalAssets(String.valueOf(t6163.F06));
                    qyFinance.setYears(String.valueOf(t6163.F02));
                    qyFinances.add(qyFinance);
                }
            }
            bidItem.setQyFinanceList(qyFinances);
        }
        
        DistrictManage districtManage = serviceSession.getService(DistrictManage.class);
        
        bidItem.setAmount(String.valueOf(bdxq.F05));
        bidItem.setRepayDate(t6231.F06 != null ? dateSdf.format(t6231.F06) : "");
        bidItem.setRate(String.valueOf(bdxq.F06 != null ? bdxq.F06.setScale(4, BigDecimal.ROUND_HALF_UP) : 0));
        bidItem.setEndDate(bdxq.jsTime != null ? dateSdf.format(bdxq.jsTime) : "");
        bidItem.setBidUse(t6231.F08);
        bidItem.setRepaySource(t6231.F16);
        bidItem.setDesc(getImgContent(t6231.F09));// 处理图片
        T5019 t5019 = districtManage.getShengName(t6231.F07);
        if (t5019 != null)
        {
            if (t5019.F08 != null)
            {
                bidItem.setArea(t5019.F06 + " " + t5019.F07 + " " + t5019.F08);
            }
            else
            {
                bidItem.setArea(t5019.F06 + " " + t5019.F07);
            }
        }
        
        // 是否有担保,S:是;F:否;
        bidItem.setIsDd(bdxq.F11.name());
        
        // 风控信息
        if (bdxq.F11 == T6230_F11.S)
        {
            T6237 fkInfos = bidManage.getFk(id);
            Dbxx dbInfos = bidManage.getDB(id);
            if (dbInfos != null)
            {
                bidItem.setDbjg(dbInfos.F06);
                bidItem.setDbdesc(dbInfos.F05);
                bidItem.setDbinfo(getImgContent(dbInfos.F07));
            }
            if (fkInfos != null)
            {
                bidItem.setFkcs(fkInfos.F02);
                bidItem.setFdbinfo(getImgContent(fkInfos.F03));
            }
        }
        
        //抵押物信息
        if (bdxq.F13 == T6230_F13.S)
        {
            Bdylx dyxxs = bidManage.getDylb(id);
            List<Dyinfo> dys = new ArrayList<Dyinfo>();
            Dyinfo dy = new Dyinfo();
            dy.setDyName(getImgContent(dyxxs.F04));
            Bdysx[] dysxs = bidManage.getDysx(dyxxs.F01);
            if (dysxs != null && dysxs.length > 0)
            {
                List<Dysx> sxs = new ArrayList<Dysx>();
                for (Bdysx bdysx : dysxs)
                {
                    Dysx sx = new Dysx();
                    sx.setDxsxName(bdysx.dxsxName);
                    sx.setDxsxVal(bdysx.F04);
                    sxs.add(sx);
                }
                dy.setDysxs(sxs);
            }
            dys.add(dy);
            bidItem.setDys(dys);
        }
        
        // 用户信用等级
        final String xyLevel = userInfoManage.getXyLevel(bdxq.F02);
        bidItem.setXyLevel(xyLevel);
        
        // 是否显示风险投资
        final Boolean isOpenRisk =
            Boolean.parseBoolean(getConfigureProvider().getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
        bidItem.setOpenRisk(isOpenRisk);
        
        //是否显示风险邀请函
    	boolean isShowFXTS = false;
    	TermManage termManage = serviceSession.getService(TermManage.class);
    	T5017 fxtsh = termManage.get(TermType.FXTSH);
    	if (fxtsh != null)
    	{
    		isShowFXTS = true;
    	}
    	bidItem.setIsShowFXTS(isShowFXTS);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", bidItem);
        return;
    }
}
