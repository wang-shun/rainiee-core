package com.dimeng.p2p.modules.nciic.util;

import com.dimeng.p2p.modules.nciic.entity.PoliceCheckInfo;

/**
 * 读取XML元素
 * 
 * @author xiaoyaocai
 * 
 */
public class DocumentUtil
{
    
    public static String readStringNode(String xmlData, String node)
        throws Throwable
    {
        StringBuffer result = new StringBuffer();
        result.append(xmlData.substring(xmlData.indexOf("<" + node + ">") + node.length() + 2,
            xmlData.indexOf("</" + node + ">")));
        return result.toString();
    }
    
    public static String readStringNodeAll(String xmlData, String node)
        throws Throwable
    {
        StringBuffer result = new StringBuffer();
        result.append(xmlData.substring(xmlData.indexOf(node) - 2, xmlData.indexOf("</" + node + ">") + node.length()
            + 4));
        return result.toString();
    }
    
    public static void main(String[] args)
    {
        try
        {
            String xmlDataTrue = "";
            String ba64 = readStringNode(xmlDataTrue, "returnValue");
            System.out.println(ba64);
            Base64 base64 = new Base64();
            byte[] re = base64.decode(ba64);
            String xml = new CompressStringUtil().decompress(re);
            System.out.println(xml);
            String str = readStringNodeAll(xml, "policeCheckInfo");
            PoliceCheckInfo result = Bean2XmlUtils.xml2bean(str, PoliceCheckInfo.class);
            System.out.println("result:" + result);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
    
}
