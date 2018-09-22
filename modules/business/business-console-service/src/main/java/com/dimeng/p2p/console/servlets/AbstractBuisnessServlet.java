/*
 * 文 件 名:  AbstractBuisnessServlet
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/4/23
 */
package com.dimeng.p2p.console.servlets;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.http.servlet.AbstractServlet;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/4/23]
 */
public class AbstractBuisnessServlet extends AbstractServlet {
    protected static final Logger logger = Logger.getLogger(AbstractBuisnessServlet.class);
    private static final long serialVersionUID = 1L;
    public static final Producer COMMON_KAPTCHA_PRODUCER;
    protected static final SecureRandom RANDOM = new SecureRandom();
    public static final String PAGING_CURRENT = "paging.current";

    static {
        ImageIO.setUseCache(false);
        final Properties props = new Properties();
        props.setProperty("kaptcha.image.height", "70");
        props.put("kaptcha.border", "no");
        props.put("kaptcha.textproducer.font.color", "black");
        props.put("kaptcha.textproducer.char.space", "5");
        COMMON_KAPTCHA_PRODUCER = new Config(props).getProducerImpl();
    }

    public static void showKaptcha(Producer producer, String verifyCode,
                                   HttpServletResponse response) {
        response.setContentType("image/jpeg");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control",
                "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        try {
            final BufferedImage bi = producer.createImage(verifyCode);
            ImageIO.write(bi, "jpeg", response.getOutputStream());
        } catch (IOException e) {
            logger.error(e, e);
        }
    }

    public static void showKaptcha(Producer producer, String verifyCode,
                                   PageContext pageContext) {
        HttpServletResponse response = (HttpServletResponse) pageContext
                .getResponse();
        response.setContentType("image/jpeg");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control",
                "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        try {
            final BufferedImage bi = producer.createImage(verifyCode);
            ImageIO.write(bi, "jpeg", response.getOutputStream());
            JspWriter out = pageContext.getOut();
            out = pageContext.pushBody();
            out.clear();
        } catch (IOException e) {
        }
    }

    public static void rendPagingResult(JspWriter out, PagingResult<?> result)
            throws IOException {
        if (out == null || result == null) {
            return;
        }
        String id = '_' + Long.toHexString(RANDOM.nextLong());
        String idText = '_' + Long.toHexString(RANDOM.nextLong());

        String func = '_' + Long.toHexString(RANDOM.nextLong());
        int currentPage = result.getCurrentPage();
        out.println("<input type='hidden' name='" + PAGING_CURRENT + "' id='" + id + "' value='" + currentPage + "'/>");
        out.println("<script type='text/javascript'>");
        out.println("<!--");
        out.print("function ");
        out.print(func);
        out.print("(p){var _cur=document.getElementById('");
        out.print(id);
        out.println("');if(_cur){if(p){_cur.value=p;}_cur.form.submit();}}");

        // 设置文本框点击事件
        out.println("function enterPress(e) {");
        out.println("var e = e || window.event; ");
        out.println("if(e.keyCode == 13){");
        out.println("pageSubmit();");
        out.println("}");
        out.println("}");

        // 输入页面提交方法
        out.println("function pageSubmit() {");
        out.println("var page = document.getElementById('" + idText + "').value;");
        out.println("var re =  /^[1-9]+[0-9]*]*$/ // 判断正整数 //判断字符串是否为数字/^[0-9]+.?[0-9]*$/;");
        out.println("if(!re.test(page)){");
        out.println("return;");
        out.println("}");
        out.println("document.getElementById('" + id + "').value=page;");
        out.println("document.getElementById('" + id + "').form.submit();");
        out.println("}");

        out.println("//-->");
        out.println("</script>");

        out.print("<div class=\"paging clearfix pt20\">");
        out.print("<div class=\"page-number\">");
        if (currentPage == 1 && result.getPageCount() > 1) {
            out.print("<a href=\"javascript:void(0)\" class=\"next\"><</a>");
        }
        if (currentPage > 1) {
			/*out.print("<a href='javascript:");
			out.print(func);
			out.print("(");
			out.print(1);
            out.print(")' class='number'>1</a>");*/

            out.print("<a href='javascript:");
            out.print(func);
            out.print("(");
            out.print(currentPage - 1);
            out.print(")' class='next'><</a>");
        }
        if (result.getPageCount() > 1) {
            int total = 1;
            final int max = 5;
            int index = result.getPageCount() - currentPage;
            if (index > 2) {
                index = 2;
            } else {
                index = index <= 0 ? (max - 1) : (max - index - 1);
            }
            int i;
            for (i = (currentPage - index); i <= result.getPageCount()
                    && total <= max; i++) {
                if (i <= 0) {
                    continue;
                }
                if (currentPage == i) {
                    out.print("<a href='javascript:void(0)' class='number selected'>");
                    out.print(i);
                    out.print("</a>");
                } else {
                    out.print("<a href='javascript:");
                    out.print(func);
                    out.print("(");
                    out.print(i);
                    out.print(")' class='number'>");
                    out.print(i);
                    out.print("</a>");
                }
                total++;
            }
            int idx = 0;
            if (i < result.getPageCount()) {
                out.print("...");
                idx = result.getPageCount() - 1;
                if (i <= idx) {
                    out.print("<a href='javascript:");
                    out.print(func);
                    out.print("(");
                    out.print(idx);
                    out.print(")' class='number'>");
                    out.print(idx);
                    out.print("</a>");
                    i++;
                }
                idx++;
                if (i <= idx) {
                    out.print("<a href='javascript:");
                    out.print(func);
                    out.print("(");
                    out.print(idx);
                    out.print(")' class='number'>");
                    out.print(idx);
                    out.print("</a>");
                    i++;
                }
            }
            if(i > 5 && i==result.getPageCount() && idx==0){
                out.print("...");
                out.print("<a href='javascript:");
                out.print(func);
                out.print("(");
                out.print(i);
                out.print(")' class='number'>");
                out.print(i);
                out.print("</a>");
            }
        }

        if (currentPage < result.getPageCount()) {
            out.print("<a href='javascript:");
            out.print(func);
            out.print("(");
            out.print(currentPage + 1);
            out.print(")' class='next'>></a>");

			/*out.print("<a href='javascript:");
            out.print(func);
			out.print("(");
			out.print(result.getPageCount());
            out.print(")' class='page-link next'>尾页</a>");*/

        }
        if (currentPage == result.getPageCount() && result.getPageCount() > 1) {
            out.print("<a href=\"javascript:void(0)\" class='next'>></a>");
        }

        if (result.getPageCount() > 1)
        {
            out.println("第<input type='text' id='" + idText + "' class='border ml5 mr5 w30 pl5 pr5' name='enterPage' onkeypress='enterPress(event)'  value=''/>页");
            out.println("<a href='javascript:pageSubmit()' class='number button radius-6'>GO</a>");
        }
        out.print("</div>");
        out.print("<div class='page-size fr gray6 mr20 lh30'>");
        out.print("<span data-bind=\"text: formatedItemCount\">总共");
        out.print("<span class=\"main-color pl5 pr5\">");
        out.print(result.getItemCount());
        out.print("</span>条记录");
        out.print("</span>");
        out.print("</div>");
        out.print("</div>");

    }

    @Override
    protected void processGet(HttpServletRequest request,
                              HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        forwardView(request, response, getClass());
    }

    @Override
    protected void processPost(HttpServletRequest request,
                               HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        forwardView(request, response, getClass());
    }

    @Override
    protected void onThrowable(HttpServletRequest request,
                               HttpServletResponse response, Throwable throwable)
            throws ServletException, IOException {
        getResourceProvider().log(throwable);
        prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
        if (throwable instanceof AuthenticationException) {
            Controller controller = getController();
            controller.redirectLogin(request, response,
                    getResourceProvider().getResource(ConfigureProvider.class).format(URLVariable.CONSOLE_LOGIN));
        } else {
            forwardView(request, response, getClass());
        }
    }

    protected void demo(final Envionment envionment) throws IOException {
        Configuration cfg = new Configuration();
        Map<String, Object> valueMap = new HashMap<String, Object>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Object get(Object key) {
                Object object = super.get(key);
                if (object == null) {
                    return envionment == null ? null : envionment.get(key
                            .toString());
                }
                return object;
            }
        };

        valueMap.put("abc", "123");
        valueMap.put("investers", new T6251[0]);
        Template template = new Template("模版名称", "协议模版内容${SYSTEM.ASADF}", cfg);
        try {
            template.process(valueMap, new OutputStreamWriter(System.out,
                    "utf-8"));
        } catch (TemplateException e) {
            throw new IOException(e);
        }

    }

    /**
     * ajax分页信息返回字符串
     * @param paging
     * @return
     * @throws Throwable
     */
    protected String rendPaging(PagingResult<?> paging)
            throws Throwable
    {
        StringBuffer rtnPageStr = new StringBuffer();
        int currentPage = paging.getCurrentPage();

        rtnPageStr.append("<div class='page-number'>总共");
        rtnPageStr.append("<span class='total main-color ml5 mr5'>");
        rtnPageStr.append(paging.getItemCount());
        rtnPageStr.append("</span>条记录 &nbsp;");
        if (currentPage == 1 && paging.getPageCount() > 1)
        {
            rtnPageStr.append("<a href=\"javascript:void(0);\" class='disabled prev'>&lt;</a>");
        }
        if (currentPage > 1)
        {
            rtnPageStr.append("<a href='javascript:void(0);' class='number prev'>&lt;</a>");
        }
        if (paging.getPageCount() > 1)
        {
            int total = 1;
            final int max = 5;
            int index = paging.getPageCount() - currentPage;
            if (index > 2)
            {
                index = 2;
            }
            else
            {
                index = index <= 0 ? (max - 1) : (max - index - 1);
            }
            int i;
            for (i = (currentPage - index); i <= paging.getPageCount() && total <= max; i++)
            {
                if (i <= 0)
                {
                    continue;
                }
                if (currentPage == i)
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='number selected'>");
                    rtnPageStr.append(i);
                    rtnPageStr.append("</a>");
                }
                else
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='number'>");
                    rtnPageStr.append(i);
                    rtnPageStr.append("</a>");
                }
                total++;
            }
            if (i < paging.getPageCount())
            {
                rtnPageStr.append("<span>...</span>");
                int idx = paging.getPageCount() - 2;
                if (i <= idx)
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='number'>");
                    rtnPageStr.append(idx);
                    rtnPageStr.append("</a>");
                }
                idx++;
                if (i <= idx)
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='number'>");
                    rtnPageStr.append(idx);
                    rtnPageStr.append("</a>");
                }
            }
        }
        if (currentPage < paging.getPageCount())
        {
            rtnPageStr.append("<a href='javascript:void(0);' class='number next'>&gt;</a>");
        }
        if (currentPage == paging.getPageCount() && paging.getPageCount() > 1)
        {
            rtnPageStr.append("<a href='javascript:void(0);' class=' disabled'>&gt;</a>");
        }

        if(paging.getPageCount() > 1){
            rtnPageStr.append("到<input type=\"text\"  id=\"goPage\" class=\"page_input border ml5 mr5 w20 pl5 pr5\" maxlength=\"7\">页<input type=\"button\" style='cursor: pointer;' class=\"btn_ok number selected blue ml10\" value=\"确定\" onclick=\"pageSubmit(this);\" />");
        }

        rtnPageStr.append("</div>");

        return rtnPageStr.toString();
    }
}
