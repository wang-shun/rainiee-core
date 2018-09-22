package com.dimeng.p2p.console.servlets.base.bussettings.product;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.base.console.service.ProductManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.IntegerParser;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CheckProduct extends AbstractBaseServlet
{

    private static final long serialVersionUID = 1L;

    @Override
    protected void processGet(HttpServletRequest request,
        HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {

        super.processGet(request, response, serviceSession);
    }

    @Override
    protected void processPost(final HttpServletRequest request,
        HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        ResourceProvider resourceProvider = getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider
            .getResource(ConfigureProvider.class);
        Integer allowProductCount =
            IntegerParser.parse(configureProvider.getProperty(SystemVariable.ALLOW_PRODUCT_COUNT));

        ProductManage productManage = serviceSession.getService(ProductManage.class);
        int count = productManage.getValidProdcutsCount();
        //封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("success", allowProductCount > count);
        jsonMap.put("allowCount", allowProductCount);
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }

}
