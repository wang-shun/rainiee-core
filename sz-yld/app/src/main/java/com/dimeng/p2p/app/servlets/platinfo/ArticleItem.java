package com.dimeng.p2p.app.servlets.platinfo;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5011;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.domain.Article;
import com.dimeng.p2p.modules.base.front.service.ArticleManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 资讯详情
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月24日]
 */
public class ArticleItem extends AbstractAppServlet
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
        // 查询资讯信息  资讯类型参考T5011.F02字段说明
        final String type = getParameter(request, "type");
        final String id = getParameter(request, "id");
        int aid = IntegerParser.parse(id);
        if (aid <= 0)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "参数错误！");
            return;
        }
        
        // 获取查询类型枚举值
        final T5011_F02 t5011_F02 = getType(type);
        
        // 分享地址
        final String shareUrl = getSiteDomain("/zxdt/" + type.toLowerCase(Locale.ENGLISH) + "/" + aid + ".html");
        
        SimpleDateFormat timeSdf = new SimpleDateFormat("yyyy-MM-dd");
        ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
        T5011 article = articleManage.get(aid, t5011_F02);
        if (article == null)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.NO_DATA_ERROR, "没有查出数据");
            return;
        }
        articleManage.view(article.F01);
        Article art = new Article();
        art.setId(article.F01);
        art.setTitle(article.F06);
        art.setReleaseTime(article.F12 == null ? (article.F13 == null ? "" : timeSdf.format(article.F13)) : timeSdf.format(article.F12));
        art.setDesc(article.F08);
        art.setContent(getImgContent(StringHelper.format(articleManage.getContent(article.F01),
            getResourceProvider().getResource(FileStore.class))));
        art.setShareUrl(shareUrl);
        art.setFrom(StringHelper.isEmpty(article.F07) ? "" : article.F07);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", art);
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
