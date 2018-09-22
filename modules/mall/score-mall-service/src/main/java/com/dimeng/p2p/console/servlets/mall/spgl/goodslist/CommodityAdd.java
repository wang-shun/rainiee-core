package com.dimeng.p2p.console.servlets.mall.spgl.goodslist;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.PartFile;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S63.entities.T6350;
import com.dimeng.p2p.S63.entities.T6351;
import com.dimeng.p2p.S63.enums.T6350_F04;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.CommodityCategoryManage;
import com.dimeng.p2p.repeater.score.ScoreCommodityManage;
import com.dimeng.p2p.variables.FileType;
import com.dimeng.util.StringHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 添加商品
 *
 * @author zengzhihua
 * @version [版本号, 2015/12/14]
 */
@MultipartConfig
@Right(id = "P2P_C_MALL_COMMODITY_ADD", name = "新增商品", isMenu = false,moduleId="P2P_C_MALL_SPGL_SHLB",order=1)
public class CommodityAdd extends AbstractMallServlet{
    private static final long serialVersionUID = 1L;

    @Override
    protected void processGet(HttpServletRequest request,
                              HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {}

    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
                               ServiceSession serviceSession) throws Throwable {}

    private String uploadCommLogo(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession, Part part, String type) throws Throwable {
		return type;}


}