package com.dimeng.p2p.console.servlets.mall.spgl.goodslist;

import java.util.List;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.PartFile;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.entities.T6350;
import com.dimeng.p2p.S63.entities.T6351;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.CommodityCategoryManage;
import com.dimeng.p2p.repeater.score.ScoreCommodityManage;
import com.dimeng.p2p.repeater.score.entity.CommoditySearch;
import com.dimeng.p2p.repeater.score.entity.T6351Ext;
import com.dimeng.p2p.variables.FileType;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 修改商品
 *
 * @author zengzhihua
 * @version [版本号, 2015/12/14]
 */
@MultipartConfig
@Right(id = "P2P_C_MALL_COMMODITY_UPDATE", name = "修改", isMenu = false, moduleId = "P2P_C_MALL_SPGL_SHLB", order = 2)
public class CommodityUpdate extends AbstractMallServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {}
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {}
    
    private String uploadCommLogo(HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession, Part part, String type)
        throws Throwable
    {
		return null;}
    
}