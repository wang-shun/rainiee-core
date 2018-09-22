package com.dimeng.p2p.console.servlets.app.manage;

import javax.servlet.annotation.MultipartConfig;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.p2p.console.servlets.info.AbstractInformationServlet;

@MultipartConfig
@Right(id = "P2P_C_APP_BBGL_ADD", name = "新增", moduleId = "P2P_C_APP_BBGL", order = 1)
public class AddAppVer extends AbstractInformationServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
}
