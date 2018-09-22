package com.dimeng.p2p.app.servlets.platinfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5015;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.domain.Notice;
import com.dimeng.p2p.modules.base.front.service.NoticeManage;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 网站公告列表
 * @author luoxiaoyan
 *
 */
public class NoticeList extends AbstractAppServlet
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
        final int pageIndex = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        
        NoticeManage noticeManage = serviceSession.getService(NoticeManage.class);
        PagingResult<T5015> results = noticeManage.search(new Paging()
        {
            @Override
            public int getCurrentPage()
            {
                return pageIndex;
            }
            
            @Override
            public int getSize()
            {
                return pageSize == 0 ? PAGESIZE : pageSize;
            }
        });
        List<Notice> noticeList = new ArrayList<Notice>();
        if (results.getPageCount() < LongParser.parse(pageIndex))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", noticeList);
            return;
        }
        T5015[] notices = results.getItems();
        if (notices != null && notices.length > 0)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (T5015 notice : notices)
            {
                Notice not = new Notice();
                not.setId(notice.F01);
                not.setTitle(notice.F05);
                not.setReleaseTime(sdf.format(notice.F09));
                not.setDesc(notice.F06);
                not.setType(notice.F02.getChineseName());
                noticeList.add(not);
            }
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", noticeList);
        return;
    }
}
