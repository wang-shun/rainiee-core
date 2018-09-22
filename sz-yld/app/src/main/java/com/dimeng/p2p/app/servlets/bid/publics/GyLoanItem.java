/*
 * 文 件 名:  GyLoanItem.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月24日
 */
package com.dimeng.p2p.app.servlets.bid.publics;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.S62.entities.T6243;
import com.dimeng.p2p.S62.enums.T6242_F11;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.bid.domain.BidProgress;
import com.dimeng.p2p.app.servlets.bid.domain.Gyinfo;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.p2p.repeater.donation.GyLoanProgresManage;
import com.dimeng.p2p.repeater.donation.entity.BidProgres;
import com.dimeng.p2p.repeater.donation.entity.GyLoanStatis;
import com.dimeng.p2p.repeater.donation.query.ProgresQuery;
import com.dimeng.util.parser.IntegerParser;

/**
 * 公益标详情
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月24日]
 */
public class GyLoanItem extends AbstractAppServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5774332546898661980L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
        // 获取公益标ID
        final String bidId = getParameter(request, "bidId");
        final int id = IntegerParser.parse(bidId);
        
        // 查询公益标信息
        T6242 creditInfo = gyLoanManage.get(id);
        if (creditInfo == null)
        {
            // 公益标不存在
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        // 公益标详情（统计数据）
        GyLoanStatis gys = gyLoanManage.gyLoanStatistics(id);
        
        // 查询活动是否结束
        final boolean isTimeEnd = getIsTimeEnd(creditInfo);
        
        // 计算进度
        Double jd = (creditInfo.F05.doubleValue() - creditInfo.F07.doubleValue()) / creditInfo.F05.doubleValue();
        
        // 公益标扩展信息
        T6243 cInfo = gyLoanManage.getT6243(id);
        if (cInfo == null)
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        // 获取公益标进度信息
        final List<BidProgress> bidProgres = getBidProgres(serviceSession, id);
        
        // 封装返回消息
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
        Gyinfo gyinfo = new Gyinfo();
        gyinfo.setBidId(id);
        // 公益标标题
        gyinfo.setLoanName(creditInfo.F03);
        // 公益标金额
        gyinfo.setLoanAmount(String.valueOf(creditInfo.F05));
        // 公益方
        gyinfo.setOrganisers(creditInfo.F22);
        // 剩余可投金额
        gyinfo.setRemaindAmount(String.valueOf(creditInfo.F07));
        // 最低起捐金额
        gyinfo.setMinAmount(String.valueOf(creditInfo.F06));
        // 开始时间
        gyinfo.setLoanStartTime(dateSdf.format(creditInfo.F15));
        
        // 设置结束时间
        if (creditInfo.F19 == null)
        {
            Calendar _calendar = Calendar.getInstance();
            
            // 设置活动开始时间
            _calendar.setTime(creditInfo.F13);
            
            // 活动发布时间 + 凑款天数
            _calendar.add(Calendar.DAY_OF_MONTH, creditInfo.F08 - 1);
            
            gyinfo.setLoanEndTime(dateSdf.format(_calendar.getTime()));
        }
        else
        {
            gyinfo.setLoanEndTime(dateSdf.format(creditInfo.F19));
        }
        
        // 进度
        gyinfo.setProgress(jd);
        // 简介
        gyinfo.setIntroduction(getImgContent(creditInfo.F24));
        // 倡导书
        gyinfo.setAdvocacyContent(getImgContent(cInfo.F02));
        // 捐款总人数
        gyinfo.setTotalNum(gys.totalNum);
        // 已捐赠金额
        gyinfo.setDonationsAmount(String.valueOf(gys.donationsAmount));
        // 设置活动是否已结束
        gyinfo.setTimeEnd(isTimeEnd);
        // 活动进度
        gyinfo.setBidProgres(bidProgres);
        
        // 返回页面信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", gyinfo);
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    /**
     * 获取活动是否结束标识
     * 
     * @param creditInfo 活动详情
     * @return 活动是否结束
     */
    private boolean getIsTimeEnd(final T6242 creditInfo)
    {
        // 判断活动是否结束
        boolean isTimeEnd = false;
        Calendar _calendar = Calendar.getInstance();
        if (creditInfo.F19 == null)
        {
            // 设置活动开始时间
            _calendar.setTime(creditInfo.F13);
            
            // 活动发布时间 + 凑款天数
            _calendar.add(Calendar.DAY_OF_MONTH, creditInfo.F08);
            
            // 判断是否等于当前时间，相等则为已结束
            if (Calendar.getInstance().getTime().after(_calendar.getTime()))
            {
                isTimeEnd = true;
            }
        }
        else
        {
            isTimeEnd = true;
        }
        
        return isTimeEnd;
    }
    
    /**
     * 获取公益标投资进度
     * 
     * @param serviceSession 上下文session
     * @param id 公益标ID
     * @return 公益标投资进度
     * @throws Throwable 异常信息
     */
    private List<BidProgress> getBidProgres(final ServiceSession serviceSession, final int id)
        throws Throwable
    {
        GyLoanProgresManage manage = serviceSession.getService(GyLoanProgresManage.class);
        
        // 公益标进展信息
        PagingResult<BidProgres> result = manage.search4front(new ProgresQuery()
        {
            @Override
            public String getSysName()
            {
                return null;
            }
            
            @Override
            public T6242_F11 getStatus()
            {
                return null;
            }
            
            @Override
            public String getLoanTitle()
            {
                return null;
            }
            
            @Override
            public String getGyName()
            {
                return null;
            }
            
            @Override
            public Timestamp getCreateTimeStart()
            {
                return null;
            }
            
            @Override
            public Timestamp getCreateTimeEnd()
            {
                return null;
            }
            
            @Override
            public Timestamp getComTimeStart()
            {
                return null;
            }
            
            @Override
            public Timestamp getComTimeEnd()
            {
                return null;
            }
            
            @Override
            public String getBidNo()
            {
                return null;
            }
            
            @Override
            public int getBidId()
            {
                return id;
            }
        }, getPaging(0, 0));
        
        List<BidProgress> bidProgress = null;
        if (result != null)
        {
            BidProgres[] didProgres = result.getItems();
            
            if (null != didProgres && didProgres.length > 0)
            {
                bidProgress = new ArrayList<BidProgress>();
                SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
                
                for (BidProgres bid : didProgres)
                {
                    BidProgress ress = new BidProgress();
                    // 简介
                    ress.setIntroduction(bid.F06);
                    // 标题时间
                    ress.setTitleTime(dateSdf.format(bid.F08));
                    // 外链地址
                    ress.setMoreUrl(bid.F09);
                    
                    bidProgress.add(ress);
                }
            }
        }
        
        return bidProgress;
    }
    
}
