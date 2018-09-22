package com.dimeng.p2p.console.servlets.statistics.zjtj.total;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.systematic.console.service.MoneyStatisticManage;
import com.dimeng.p2p.modules.systematic.console.service.entity.MoneyStatisticalEntity;

@Right(id = "P2P_C_ACCOUNT_FUNDSEXPORT", name = "导出资金统计总览", moduleId = "P2P_C_STATISTICS_ZJTJ_TOTAL")
public class ExportFunds extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        String platformTotalIncome =
            request.getParameter("platformTotalIncome") == null ? "0.00" : request.getParameter("platformTotalIncome");
        //资金统计实体类
        MoneyStatisticalEntity moneyStatistical = new MoneyStatisticalEntity();
        MoneyStatisticManage funds = serviceSession.getService(MoneyStatisticManage.class);
        moneyStatistical.setAccountBalance(funds.accountBalance());
        moneyStatistical.setUsableBalance(funds.usableBalance(T6101_F03.WLZH));
        moneyStatistical.setMargin(funds.usableBalance(T6101_F03.FXBZJZH));
        moneyStatistical.setAmountFrozen(funds.usableBalance(T6101_F03.SDZH));
        moneyStatistical.setOnlinePay(funds.getXscz());
        moneyStatistical.setOfflinePay(funds.getXxcz());
        moneyStatistical.setTxsxf(funds.getTxsxf());
        moneyStatistical.setCzsxf(funds.getCzsxf());
        moneyStatistical.setCjfwf(funds.getPtzhzjtj(FeeCode.CJFWF));
        moneyStatistical.setLcglf(funds.getPtzhzjtj(FeeCode.GLF));
        moneyStatistical.setZqzrsxf(funds.getPtzhzjtj(FeeCode.ZQZR_SXF));
        moneyStatistical.setWyjsxf(funds.getWyj());
        moneyStatistical.setLjtzje(funds.getLjtzje());
        moneyStatistical.setLjtzzsy(funds.getLjtzzsy());
        moneyStatistical.setSbtzzsy(funds.getSbtzzsy());
        moneyStatistical.setZqzrykze(funds.getZqzrykze());
        moneyStatistical.setJkyhzje(funds.getJkyhzje());
        moneyStatistical.setJkzchkz(funds.getJkzchkze());
        moneyStatistical.setJkwhk(funds.getJkwhk());
        moneyStatistical.setJkyqwh(funds.getJkyqwh());
        moneyStatistical.setYqjgdf(funds.getYqjgdf());
        moneyStatistical.setYqjgdfyh(funds.getYqjgdfyh());
        moneyStatistical.setYqptdf(funds.getYqptdf());
        moneyStatistical.setYqptdfyh(funds.getYqptdfyh());
        moneyStatistical.setTodayCharge(funds.getTodayCharge());
        moneyStatistical.setTodayWithdraw(funds.getTodayWithdraw());
        moneyStatistical.setYhtxze(funds.getYhtxze());
        moneyStatistical.setDzrzqze(funds.getDzrzqze());
        moneyStatistical.setZrzzqze(funds.getZrzzqze());
        moneyStatistical.setYzrzqze(funds.getYzrzqze());
        funds.export(platformTotalIncome, moneyStatistical, response.getOutputStream(), "");
    }
    
}
