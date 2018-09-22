package com.dimeng.p2p.escrow.fuyou.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.dimeng.util.StringHelper;

/**
 * xml帮助类
 * 
 * @author chentongbing
 * @date 2014年9月10日
 */

public class XmlHelper
{
    protected static final Logger logger = Logger.getLogger(XmlHelper.class);
    
    /**
     * 解析xml字符串
     * 
     * @param 传入的xml格式的字符串
     * @return map
     */
    public static Map<String, String> xmlElements(String xmlDoc)
    {
        Map<String, String> xmlMap = new HashMap<String, String>();
        // 创建一个新的字符串
        StringReader read = new StringReader(xmlDoc);
        // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        // 创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        try
        {
            // 通过输入源构造一个Document
            Document doc = sb.build(source);
            // 取的根元素
            Element root = doc.getRootElement();
            // 得到根元素所有子元素的集合
            @SuppressWarnings("rawtypes")
            List jiedian = root.getChildren();
            
            // 获得XML中的命名空间（XML中未定义可不写）
            // Namespace ns = root.getNamespace();
            Element et = null;
            
            // 保存子元素的名称
            List<String> childrenName = new ArrayList<String>();
            
            for (int i = 0; i < jiedian.size(); i++)
            {
                
                et = (Element)jiedian.get(i);
                
                @SuppressWarnings("rawtypes")
                List zjiedian = et.getChildren();
                
                // 没有子节点，只有自己的
                if (zjiedian.size() == 0)
                {
                    childrenName.add(et.getName());
                    xmlMap.put(et.getName(), et.getValue());
                }
                else
                {
                    // 有子节点的
                    for (int j = 0; j < zjiedian.size(); j++)
                    {
                        Element xet = (Element)zjiedian.get(j);
                        childrenName.add(xet.getName());
                        xmlMap.put(xet.getName(), xet.getValue());
                    }
                }
            }
        }
        catch (JDOMException e)
        {
            logger.error(e, e);
        }
        catch (IOException e)
        {
            logger.error(e, e);
        }
        return xmlMap;
    }
    
    /**
     * 描述:将xml转换为map（这个方法的算法有些耗性能，后期需要修改） 作者:wangshaohua 创建时间：2015年5月4日
     * 
     * @param xmlString
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map<String, Object> xmlToMap(String xmlString)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringHelper.isEmpty(xmlString))
        {
            return null;
        }
        
        int remarkStartIndex = xmlString.indexOf("<remark>");
        int remarkEndIndex = xmlString.indexOf("</remark>");
        if (remarkStartIndex > 0 && remarkEndIndex > 0)
        {
            String remarkVal = xmlString.substring(remarkStartIndex + "<remark>".length(), remarkEndIndex);
            map.put("remark", remarkVal);
            xmlString = xmlString.replace(remarkVal, "");
        }
        
        List<String> list = null;
        xmlString = xmlString.replaceAll("\\[", "【");
        xmlString = xmlString.replaceAll("\\]", "】");
        while (true)
        {
            // 第一个以<的位置
            int startIndex = xmlString.indexOf("<");
            // 第一个>的位置
            int endIndex = xmlString.indexOf(">");
            // 获取第一个节点标签的内容
            String strName = xmlString.substring(startIndex + 1, endIndex);
            int lastIndex = xmlString.indexOf("/" + strName);
            // 判断第一个节点是否有尾节点，如果没有，则这个节点的内容可以都删除
            if (lastIndex < 0)
            {
                xmlString = xmlString.substring(endIndex + 1, xmlString.length());
                continue;
            }
            // 获取标签之间节点的内容
            String content = xmlString.substring(endIndex + 1, lastIndex - 1);
            // 判断当前节点是否有子节点如果有子节点,将这个节点删除，但并不删除里面的子节点
            if (content.indexOf("<") >= 0 || content.indexOf("</") >= 0)
            {
                xmlString = xmlString.replace("<" + strName + ">", "");
                xmlString = xmlString.replace("</" + strName + ">", "");
                continue;
            }
            // 如果没有子节点，则执行map中的操作
            Object mapValue = map.get(strName);
            // 如果当前节点没有放入过此节点元素，则可直接放入
            if (mapValue == null)
            {
                map.put(strName, content);
            }
            else
            {
                // 判断当前节点内容有木有 被多次放入到map中，如果没有，则建一个list表放入进去
                if (mapValue.getClass().getName().equals("java.lang.String"))
                {
                    list = new ArrayList<String>();
                    list.add((String)mapValue);
                    list.add(content);
                    map.put(strName, list);
                }
                else
                {
                    ((List)mapValue).add(content);
                }
            }
            if (xmlString.length() < 2 || strName.equals("signature"))
            {
                break;
            }
            xmlString = xmlString.replaceFirst("<" + strName + ">" + content + "</" + strName + ">", "");
        }
        return map;
    }
    
    // 获取palin数据
    public static String getPlain(String xmlpost)
    {
        int start = xmlpost.indexOf("<plain>");
        int end = xmlpost.indexOf("</plain>");
        return xmlpost.substring(start, end + 8);
    }
    
    /**
     * 响应XML构建
     * <HSP>
     * @param param
     * @return String
     * @throws Throwable
     *//*
       public String createXml(Map<String, String> param)
        throws Throwable
       {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<ap>");
        builder.append("<plain>");
        builder.append("<resp_code>" + param.get("resp_code") + "</resp_code>");
        builder.append("<mchnt_cd>" + param.get("mchnt_cd") + "</mchnt_cd>");
        builder.append("<mchnt_txn_ssn>" + param.get("mchnt_txn_ssn") + "</mchnt_txn_ssn>");
        builder.append("</plain>");
        builder.append("<signature>" + param.get("signature") + "</signature>");
        builder.append("</ap>");
        return builder.toString();
       }
       */
    
    public static void main(String[] args)
    {
        String xmlpost =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ap><plain><resp_code>0000</resp_code><mchnt_cd>0005840F0959282</mchnt_cd><mchnt_txn_ssn>YHXX152032410061018683</mchnt_txn_ssn><results><result><mobile_no>18620289900</mobile_no><cust_nm>佛山邮差驿站科技有限公司</cust_nm><certif_id>430681198410157914</certif_id><email>280029220@qq.com</email><city_id>5880</city_id><parent_bank_id>0314</parent_bank_id><bank_nm>佛山顺德农村商业银行股份有限公司</bank_nm><capAcntNo>80020000011081569</capAcntNo><card_pwd_verify_st></card_pwd_verify_st><id_nm_verify_st></id_nm_verify_st><contract_st></contract_st><user_st>1</user_st></result></results></plain><signature>hXxn8+YW+mHTqzbOV7YfALgc26ogEQGrJjqADZJ12SLpQJLwerFntf34BSFnb6mCQcyrxIIs0Ef5zcamynnRDN4JuTSvc+eQy3psv4egAssbG5lBjRKusgDhkEPbyO8I0p4etCN7HKGbOElqnjqsd6thVV7KM1Ca0fdTj8kRMAM=</signature></ap>";
        
        HashMap<String, Object> xmlMap = (HashMap<String, Object>)XmlHelper.xmlToMap(xmlpost);
        
        System.out.println(xmlMap.get("parent_bank_id"));
    }
}