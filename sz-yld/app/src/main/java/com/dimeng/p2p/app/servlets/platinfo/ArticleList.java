package com.dimeng.p2p.app.servlets.platinfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5011;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.domain.Article;
import com.dimeng.p2p.modules.base.front.service.ArticleManage;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 
 * 资讯列表
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月24日]
 */
public class ArticleList extends AbstractAppServlet
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
        // 获取分页信息
        final int pageIndex = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        
        // 获取文章类型
        final String type = getParameter(request, "type");
        
        // 查询资讯信息  资讯类型参考T5011.F02字段说明
        ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
        
        // 获取文章类型枚举值
        final T5011_F02 t5011_F02 = getType(type);
        
        // 查询文章列表
        PagingResult<T5011> results = articleManage.search(t5011_F02, getPaging(pageIndex, pageSize));
        
        List<Article> articleList = new ArrayList<Article>();
        if (results.getPageCount() < LongParser.parse(pageIndex))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", articleList);
            return;
        }
        
        T5011[] articles = results.getItems();
        if (articles != null && articles.length > 0)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (T5011 article : articles)
            {
                Article art = new Article();
                art.setId(article.F01);
                art.setTitle(article.F06);
                art.setReleaseTime(article.F12 == null ? "" : sdf.format(article.F12));
                art.setDesc(article.F08);
                articleList.add(art);
            }
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", articleList);
        return;
    }
    
    /**
     * 取文章类型
     * 
     * @param type 需查询文章类型
     * @return 枚举类型
     */
    private T5011_F02 getType(final String type)
    {
        for (T5011_F02 value : T5011_F02.values())
        {
            if (value.name().equals(type))
            {
                return value;
            }
        }
        
        return T5011_F02.WDHYZX;
    }
}
