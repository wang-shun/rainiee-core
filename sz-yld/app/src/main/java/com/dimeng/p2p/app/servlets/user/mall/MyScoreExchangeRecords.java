/*
 * 文 件 名:  MyScoreExchangeRecords.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月28日
 */
package com.dimeng.p2p.app.servlets.user.mall;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.mall.MyScoreExchangeInfo;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderInfoExt;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 我的积分兑换记录
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月28日]
 */
public class MyScoreExchangeRecords extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -4654581777108618208L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取页面参数
        final int pageIndex = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        
        UserCenterScoreManage userCenterScore = serviceSession.getService(UserCenterScoreManage.class);
        // 分页参数
        Paging paging = getPaging(pageIndex, pageSize);
        PagingResult<ScoreOrderInfoExt> resultList = userCenterScore.queryUsedUScoreList(paging);
        
        ScoreOrderInfoExt[] scoreOrderInfoExt = null;
        List<MyScoreExchangeInfo> scoreOrderInfoExts = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != resultList && null != resultList.getItems())
        {
            // 判断是否超出记录集
            if (resultList.getPageCount() < LongParser.parse(pageIndex))
            {
                scoreOrderInfoExts = new ArrayList<MyScoreExchangeInfo>();
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", scoreOrderInfoExts);
                return;
            }
            
            scoreOrderInfoExt = resultList.getItems();
            scoreOrderInfoExts = new ArrayList<MyScoreExchangeInfo>();
            
            for (ScoreOrderInfoExt info : scoreOrderInfoExt)
            {
                MyScoreExchangeInfo ext = new MyScoreExchangeInfo();
                ext.setComdName(info.comdName);
                ext.setComdNum(info.comdNum);
                ext.setComdScore(info.comdScore);
                ext.setOrderTime(sdf.format(info.F04));
                
                scoreOrderInfoExts.add(ext);
            }
        }
        
        // 用户积分获取详情
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", scoreOrderInfoExts);
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
