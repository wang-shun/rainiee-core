/*
 * 文 件 名:  ContractPdfFormationExecutor.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月22日
 */
package com.dimeng.p2p.order;

import java.io.File;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.util.StringHelper;

/**
 * <一句话功能简述> 合同保全生成pdf文件
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [7.2.0, 2016年6月22日]
 */
@ResourceAnnotation
public class PdfFormationExecutor extends AbstractPdfFormationExecutor
{
    
    /** 
     * <默认构造函数>
     */
    public PdfFormationExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return PdfFormationExecutor.class;
    }
    
    @Override
    public String convertHtml2Pdf(String htmlPath, String contextPath, String charsetName)
        throws Throwable
    {
        logger.info("convertHtml2Pdf 空实现");
        return null;
    }
    
    @Override
    public File getFile(String fileType, String subffix, String xyNo)
        throws Throwable
    {
        Calendar now = Calendar.getInstance();
        if (StringHelper.isEmpty(fileType))
        {
            throw new ParameterException("Parameter [fileType] must not null!");
        }
        if (StringHelper.isEmpty(subffix))
        {
            throw new ParameterException("Parameter [subffix] must not null!");
        }
        if (StringHelper.isEmpty(xyNo))
        {
            throw new ParameterException("Parameter [xyNo] must not null!");
        }
        StringBuilder uri = new StringBuilder();
        uri.append(fileType)
            .append(separatorChar)
            .append(now.get(Calendar.YEAR))
            .append(separatorChar)
            .append((now.get(Calendar.MONTH) + 1))
            .append(separatorChar)
            .append(now.get(Calendar.DAY_OF_MONTH))
            .append(separatorChar)
            .append(xyNo);
        File file = new File(getHome(), uri.toString());
        if (!file.exists() && !file.isDirectory())
        {
            file.mkdirs();
        }
        uri.setLength(0);
        uri.append(System.currentTimeMillis());
        uri.append(generateRandom()).append(".").append(subffix);
        return new File(file, uri.toString());
    }
    
    /**
     * <一句话功能简述> 生成3位随机数
     * <功能详细描述> 在高并发情况下文件名加随机数
     * @return
     */
    private int generateRandom()
    {
        Random rand = new Random();
        return (rand.nextInt(900) + 100);
    }
    
    @Override
    public String createHTML(Map<String, Object> valueMap, String fileType, String name, String content,
        String charset, String xyNo)
        throws Throwable
    {
        logger.info("createHTML 方法空实现");
        return null;
    }
    
}
