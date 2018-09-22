package com.dimeng.p2p.app.servlets.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.account.user.service.LetterManage;
import com.dimeng.p2p.account.user.service.entity.LetterEntity;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.Letter;
import com.dimeng.p2p.app.servlets.user.domain.LetterReturnCode;
import com.dimeng.p2p.common.enums.LetterStatus;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 站内信息
 * @author tanhui
 */
public class LetterList extends AbstractSecureServlet
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
        
        LetterReturnCode lrc = new LetterReturnCode();
        lrc.setCode(ExceptionCode.SUCCESS);
        lrc.setDescription("success");
        
        LetterManage letterManage = serviceSession.getService(LetterManage.class);
        final int pageIndex = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        String status = getParameter(request, "status");
        LetterStatus letterStatus = null;
        if (!StringHelper.isEmpty(status))
        {
            letterStatus = LetterStatus.valueOf(status);
        }
        
        Paging paging = new Paging()
        {
            
            @Override
            public int getSize()
            {
                return pageSize == 0 ? PAGESIZE : pageSize;
            }
            
            @Override
            public int getCurrentPage()
            {
                return pageIndex;
            }
        };
        
        int unRead = letterManage.getUnReadCount();
        int count = letterManage.getCount();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PagingResult<LetterEntity> result = letterManage.search(letterStatus, paging);
        
        List<Letter> letterList = new ArrayList<Letter>();
        
        if (result != null && result.getItems() != null)
        {
            if (result.getPageCount() < LongParser.parse(pageIndex))
            {
                lrc.setCode(ExceptionCode.SUCCESS);
                lrc.setDescription("success");
                lrc.setData(letterList);
                returnHandle(request, response, lrc);
                return;
            }
            
            LetterEntity[] letters = result.getItems();
            for (LetterEntity le : letters)
            {
                Letter l = new Letter();
                l.setId(le.id);
                l.setContent(le.content);
                l.setSendTime(le.sendTime != null ? sdf.format(le.sendTime) : "");
                l.setStatus(String.valueOf(le.status));
                l.setTitle(le.title);
                letterList.add(l);
            }
        }
        
        lrc.setCount(count);
        lrc.setUnRead(unRead);
        lrc.setData(letterList);
        lrc.setCode(ExceptionCode.SUCCESS);
        returnHandle(request, response, lrc);
        return;
        
    }
}
