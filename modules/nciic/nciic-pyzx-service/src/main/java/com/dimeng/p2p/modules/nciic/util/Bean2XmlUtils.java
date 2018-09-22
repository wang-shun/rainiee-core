/**
 * 
 */
package com.dimeng.p2p.modules.nciic.util;

import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.dimeng.p2p.modules.nciic.entity.IdentifierAttr;
import com.dimeng.p2p.modules.nciic.entity.PoliceCheckInfo;

/**
 * xml转换对像
 */
public class Bean2XmlUtils
{
    private static final String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    
    /**
     * bean 转 xml
     * @param obj bean对象
     * @param cls bean类型
     * @return
     * @throws JAXBException
     */
    public static <T> String bean2xml(Object obj, Class<T> cls)
        throws JAXBException
    {
        if (obj != null && cls != null)
        {
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(cls);
            Marshaller mar = context.createMarshaller();
            //mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            mar.setProperty(Marshaller.JAXB_FRAGMENT, true);
            writer.append(header);
            mar.marshal(obj, writer);
            return writer.toString();
        }
        return null;
    }
    
    /**
     * 
     * @param obj
     * @param cls 
     * @param isFrame 是否需要生成xml头
     * @return
     * @throws JAXBException
     */
    public static <T> String bean2xml(Object obj, Class<T> cls, boolean isFrame)
        throws JAXBException
    {
        if (isFrame == true)
        {
            if (obj != null && cls != null)
            {
                StringWriter writer = new StringWriter();
                JAXBContext context = JAXBContext.newInstance(cls);
                Marshaller mar = context.createMarshaller();
                mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                mar.setProperty(Marshaller.JAXB_FRAGMENT, true);
                mar.marshal(obj, writer);
                return writer.toString();
            }
            return null;
        }
        else
        {
            return bean2xml(obj, cls);
        }
        
    }
    
    /**
     * xml 转 bean
     * @param xmlStr xml内容
     * @param cls bean类型
     * @return
     * @throws JAXBException
     */
    @SuppressWarnings("unchecked")
    public static <T> T xml2bean(String xmlStr, Class<T> cls)
        throws JAXBException
    {
        JAXBContext context = JAXBContext.newInstance(cls);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T)unmarshaller.unmarshal(new StringReader(xmlStr));
    }
    
    /**
     * xml 转 bean
     * @param reader
     * @param cls bean类型
     * @return
     * @throws JAXBException
     */
    @SuppressWarnings("unchecked")
    public static <T> T xml2bean(Reader reader, Class<T> cls)
        throws JAXBException
    {
        JAXBContext context = JAXBContext.newInstance(cls);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T)unmarshaller.unmarshal(reader);
    }
    
    public static void main(String[] args)
    {
        try
        {
            PoliceCheckInfo info = xml2bean(new FileReader("E:/wsxml/pyzxtrue.xml"), PoliceCheckInfo.class);
            System.out.println(info.toString());
            for (IdentifierAttr row : info.item)
            {
                System.out.print(row.toString());
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private final static String xmlDataTrue = "";
}
