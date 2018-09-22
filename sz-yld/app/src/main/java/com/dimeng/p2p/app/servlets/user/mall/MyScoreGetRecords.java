/*
 * 文 件 名:  UserScoreRecords.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月28日
 */
package com.dimeng.p2p.app.servlets.user.mall;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6106;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.mall.GetUserScoreInfo;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 用户积分获取记录
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月28日]
 */
public class MyScoreGetRecords extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 647069847939919002L;
    
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
        PagingResult<T6106> resultList = userCenterScore.getUserScoreList(paging);
        
        List<GetUserScoreInfo> getUserScoreInfos = null;
        if (null != resultList)
        {
            T6106[] t6106 = null;
            if (null != resultList && null != resultList.getItems())
            {
                // 判断是否超出记录集
                if (resultList.getPageCount() < LongParser.parse(pageIndex))
                {
                    getUserScoreInfos = new ArrayList<GetUserScoreInfo>();
                    
                    // 封装返回信息
                    setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", getUserScoreInfos);
                    return;
                }
                
                t6106 = resultList.getItems();
                getUserScoreInfos = new ArrayList<GetUserScoreInfo>();
            }

            List<T6106> t6106List = new ArrayList<T6106>();
            if (null != t6106 && t6106.length > 0)
            {
                t6106List = Arrays.asList(t6106);
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (T6106 t6 :t6106List){
                GetUserScoreInfo info = new GetUserScoreInfo();
                
                info.setGetDate(sdf.format(t6.F04));
                info.setGetScore(t6.F03);
                info.setGetType(t6.F05.getChineseName());
                
                getUserScoreInfos.add(info);
            }
        }
        
        // 用户积分获取详情
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", getUserScoreInfos);
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
