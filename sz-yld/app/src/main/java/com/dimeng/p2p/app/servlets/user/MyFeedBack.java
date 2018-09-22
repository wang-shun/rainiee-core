/*
 * 文 件 名:  MyFeedBack.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月22日
 */
package com.dimeng.p2p.app.servlets.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6195_EXT;
import com.dimeng.p2p.S61.enums.T6195_F06;
import com.dimeng.p2p.account.user.service.TzjyManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.FeedBackInfo;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 我的意见反馈
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月22日]
 */
public class MyFeedBack extends AbstractSecureServlet
{
    private static final long serialVersionUID = 3384309401539128304L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 显示分页信息
        final int pageIndex = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        
        // 查询用户的投诉建议
        final TzjyManage manage = serviceSession.getService(TzjyManage.class);
        final int userId = serviceSession.getSession().getAccountId();
        PagingResult<T6195_EXT> t6195_EXTs = manage.search(userId, getPaging(pageIndex, pageSize));
        
        List<FeedBackInfo> feedBackInfos = new ArrayList<FeedBackInfo>();
        if (null != t6195_EXTs)
        {
            if (t6195_EXTs.getPageCount() < LongParser.parse(pageIndex))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", feedBackInfos);
                return;
            }
            
            T6195_EXT[] tsjys = t6195_EXTs.getItems();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (null != tsjys && tsjys.length > 0)
            {
                for (T6195_EXT ext : tsjys)
                {
                    FeedBackInfo info = new FeedBackInfo();
                    // 反馈信息ID
                    info.setFeedId(ext.F01);
                    
                    // 反馈用户ID
                    info.setUserId(ext.F02);
                    
                    // 反馈内容
                    info.setFeedContent(ext.F03);
                    
                    // 反馈时间
                    info.setCreateTime(format.format(ext.F04));
                    
                    // 是否回复
                    info.setIsBack(T6195_F06.no.name());
                    
                    // 是否回复
                    info.setIsBack(ext.F06.name());
                    
                    // 回复人ID
                    info.setBackUserId(ext.F07);
                    
                    // 回复内容
                    info.setBackContent(ext.F05);
                    
                    // 回复时间
                    //info.setBackTime(null == ext.F09 ? "" : format.format(ext.F09));
                    
                    feedBackInfos.add(info);
                }
            }
        }
        
        // 返回页面
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", feedBackInfos);
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
