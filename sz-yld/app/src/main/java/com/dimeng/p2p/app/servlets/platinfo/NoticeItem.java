package com.dimeng.p2p.app.servlets.platinfo;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5015;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.domain.Notice;
import com.dimeng.p2p.modules.base.front.service.NoticeManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 文章详情
 * 
 * @author luoxiaoyan
 *
 */
public class NoticeItem extends AbstractAppServlet
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
        final String id = getParameter(request, "id");
        int aid = IntegerParser.parse(id);
        if (aid <= 0)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "参数错误！");
            return;
        }
        SimpleDateFormat timeSdf = new SimpleDateFormat("yyyy-MM-dd");
        NoticeManage noticeManage = serviceSession.getService(NoticeManage.class);
        T5015 notice = noticeManage.get(aid);
        if (notice == null)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.NO_DATA_ERROR, "没有查出数据");
            return;
        }
        noticeManage.view(notice.F01);
        Notice not = new Notice();
        not.setId(notice.F01);
        not.setTitle(notice.F05);
        not.setReleaseTime(timeSdf.format(notice.F09));
        not.setDesc(notice.F06);
        not.setContent(StringHelper.format(notice.F06, getResourceProvider().getResource(FileStore.class)));
        not.setShareUrl(getSiteDomain("/zxdt/wzgg/" + aid + ".html"));
        not.setType(notice.F02.getChineseName());
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", not);
        return;
    }
}
