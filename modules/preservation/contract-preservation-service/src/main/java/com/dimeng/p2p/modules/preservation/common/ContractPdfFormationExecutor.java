/*
 * 文 件 名:  ContractPdfFormationExecutor.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月22日
 */
package com.dimeng.p2p.modules.preservation.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import com.dimeng.framework.resource.AchieveVersion;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.order.PdfFormationExecutor;
import com.dimeng.util.StringHelper;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.ReadingProcessor;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.ImageProvider;
import com.itextpdf.tool.xml.pipeline.html.NoImageProviderException;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * <一句话功能简述> 合同保全生成pdf文件
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [7.2.0, 2016年6月22日]
 */
@AchieveVersion(version = 20160625)
@ResourceAnnotation
public class ContractPdfFormationExecutor extends PdfFormationExecutor
{
    
    /** 
     * <默认构造函数>
     */
    public ContractPdfFormationExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public String convertHtml2Pdf(String htmlPath, final String contextPath, final String charsetName)
        throws Throwable
    {
        if (StringHelper.isEmpty(htmlPath))
        {
            logger.info("Parameter [htmlPath] is must not empty!");
            throw new ParameterException("Parameter [htmlPath] is must not empty!");
        }
        //PDF生成路径
        String pdfPath = htmlPath.replace(".html", ".pdf");
        XMLWorker worker = null;
        try (OutputStream outputStream = new FileOutputStream(pdfPath))
        {
            Document document = new Document(PageSize.A4, 30, 30, 30, 30);
            document.setMargins(30, 30, 30, 30);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            writer.setInitialLeading(12.5f);
            document.open();
            
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(new CssAppliersImpl(new XMLWorkerFontProvider()
            {
                @Override
                public Font getFont(String fontname, String encoding, float size, final int style)
                {
                    if (fontname == null)
                    {
                        fontname = "宋体";
                    }
                    return super.getFont(fontname, encoding, size, style);
                }
            }))
            {
                @Override
                public HtmlPipelineContext clone()
                    throws CloneNotSupportedException
                {
                    HtmlPipelineContext context = super.clone();
                    try
                    {
                        ImageProvider imageProvider = this.getImageProvider();
                        context.setImageProvider(imageProvider);
                    }
                    catch (NoImageProviderException e)
                    {
                    }
                    return context;
                }
            };
            
            htmlContext.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
            CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
            cssResolver.setFileRetrieve(new FileRetrieve()
            {
                @Override
                public void processFromStream(InputStream in, ReadingProcessor processor)
                    throws IOException
                {
                    try (InputStreamReader reader = new InputStreamReader(in, charsetName))
                    {
                        int i = -1;
                        while (-1 != (i = reader.read()))
                        {
                            processor.process(i);
                        }
                    }
                }
                
                @Override
                public void processFromHref(String href, ReadingProcessor processor)
                    throws IOException
                {
                    if (StringHelper.isEmpty(contextPath))
                    {
                        return;
                    }
                    href = contextPath + href;
                    URL url = new URL(href);
                    try (InputStreamReader reader = new InputStreamReader(url.openStream(), charsetName))
                    {
                        int i = -1;
                        while (-1 != (i = reader.read()))
                        {
                            processor.process(i);
                        }
                    }
                }
            });
            
            HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, writer));
            Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
            worker = new XMLWorker(pipeline, true);
            XMLParser parser = new XMLParser(true, worker, Charset.forName(charsetName));
            try (InputStream inputStream = new FileInputStream(htmlPath))
            {
                parser.parse(inputStream, Charset.forName(charsetName));
            }
            document.close();
        }
        finally
        {
            if (worker != null)
            {
                worker.close();
            }
        }
        //生成pdf文件后删除HTML文件
        File file = new File(htmlPath);
        if (file.exists())
        {
            file.delete();
        }
        logger.info("pdf文档地址：" + pdfPath);
        return pdfPath;
    }
    
    @Override
    public String createHTML(Map<String, Object> valueMap, String fileType, String name, String content,
        String charset, String xyNo)
        throws Throwable
    {
        fileType = StringHelper.isEmpty(fileType) ? "contract" : fileType;
        File file = getFile(fileType, "html", xyNo);
        //logger.info(file.getPath());
        Configuration cfg = new Configuration();
        Template template = new Template(name, content, cfg);
        try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset)))
        {
            template.process(valueMap, out);
        }
        return file.getPath();
    }
    
}
