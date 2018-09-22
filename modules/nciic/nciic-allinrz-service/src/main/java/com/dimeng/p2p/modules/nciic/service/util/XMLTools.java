package com.dimeng.p2p.modules.nciic.service.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.allinpay.XmlTools;
import com.dimeng.p2p.modules.nciic.service.entity.INFO;
import com.dimeng.util.StringHelper;

public class XMLTools
{
    protected static final Logger logger = Logger.getLogger(XMLTools.class);
    
    /**
     * 向通联支付发送报文，并获取返回报文的结果
     * @param xml          发送内容
     * @param flag         是否去掉签名 true：去掉签名  false：加上签名
     * @param url          提交地址
     * @param prvkeyPath   私钥路径
     * @param pubkeyPath   公钥路径
     * @param pfxPassword  私钥密码
     * @return
     */
    public static String sendToTlt(String xml, boolean flag, String url, String prvkeyPath, String pubkeyPath,
        String pfxPassword)
    {
        try
        {
            if (!flag)
            {
                xml = signMsg(xml, prvkeyPath, pfxPassword);
            }
            else
            {
                xml = xml.replaceAll("<SIGNED_MSG></SIGNED_MSG>", "");
            }
            return sendXml(xml, url, pubkeyPath, flag);
        }
        catch (Exception e)
        {
            logger.error(e.toString(), e);
            if (e.getCause() instanceof ConnectException || e instanceof ConnectException)
            {
                logger.error("请求链接中断，如果是支付请求，请做交易结果查询，以确认该笔交易是否已被通联受理，避免重复交易");
            }
        }
        return "";
    }
    
    /**
     * 获取报文签名
     * @param xml             xml报文内容
     * @param prvkeyPath      私钥路径
     * @param prvkeyPassword  私钥密码
     * @return
     * @throws Exception
     */
    private static String signMsg(String xml, String prvkeyPath, String prvkeyPassword)
        throws Exception
    {
        xml = XmlTools.signMsg(xml, prvkeyPath, prvkeyPassword, false);
        return xml;
    }
    
    /**
     * 向通联支付发送报文
     * @param xml           xml报文
     * @param url           提交的接口地址
     * @param pubkeyPath    公钥路径
     * @param isFront
     * @return
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    private static String sendXml(String xml, String url, String pubkeyPath, boolean isFront)
        throws UnsupportedEncodingException, Exception
    {
        logger.info("======================发送报文======================：\n" + xml);
        String resp = XmlTools.send(url, xml);
        logger.info("======================响应内容======================");
        boolean flag = verifyMsg(resp, pubkeyPath, isFront);
        if (flag)
        {
            logger.info("响应内容验证通过");
        }
        else
        {
            logger.info("响应内容验证不通过");
        }
        return resp;
    }
    
    /**
     * 验证签名
     * @param msg       验证内容
     * @param cer       公钥路径
     * @param isFront 
     * @return
     * @throws Exception
     */
    private static boolean verifyMsg(String msg, String cer, boolean isFront)
        throws Exception
    {
        boolean flag = XmlTools.verifySign(msg, cer, false, isFront);
        logger.info("验签结果[" + flag + "]");
        return flag;
    }
    
    /**作者：王少华
     * 实体对象转换xml字符串
     * @param thisObj           实体对象
     * @param firstStr          指定首节点（如果没有可以留空）
     * @param isFirstCharUp     节点属性是否首字母大写
     * @return
     */
    public static String objToXml(Object thisObj, String firstStr, boolean isFirstCharUp)
    {
        Class c;
        StringBuilder sb = new StringBuilder();
        //根节点
        Map<String, Object> nodeMap = new HashMap<String, Object>();
        try
        {
            //根据对象获取类
            c = Class.forName(thisObj.getClass().getName());
            //获取类中的所有方法
            Method[] m = c.getMethods();
            for (int i = 0; i < m.length; i++)
            {
                //获取方法名称
                String method = m[i].getName();
                if (method.startsWith("get"))
                {
                    //属性值
                    Object value = m[i].invoke(thisObj);
                    if (value == null)
                    {
                        continue;
                    }
                    //去除本身类属性
                    if (value.getClass().getName().equals("java.lang.Class"))
                    {
                        continue;
                    }
                    //将方法名称的前3个字符去掉；
                    String key = method.substring(3);
                    //成员变量的首字母是否要大写
                    if (isFirstCharUp)
                    {
                        key = key.substring(0, 1).toUpperCase() + key.substring(1);
                    }
                    else
                    {
                        key = key.substring(0, 1).toLowerCase() + key.substring(1);
                    }
                    //将属性名称以及属性值放入
                    nodeMap.put(key, value);
                }
            }
            for (Map.Entry<String, Object> entry : nodeMap.entrySet())
            {
                String key = entry.getKey().toString();
                Object value = nodeMap.get(key);
                //判断属性值是不是字符串类型,如果是的，直接放入xml字符串中
                if (value != null && value.getClass().equals(String.class))
                {
                    sb.append("<" + key + ">" + value + "</" + key + ">");
                }
                if (value != null && !value.getClass().equals(String.class))
                {
                    sb.append("<" + key + ">");
                    //对象的类名称
                    String sbKey = value.getClass().getName();
                    //获取这个对象的类
                    Class d = Class.forName(sbKey);
                    //获取这个对象的所有方法
                    Method[] n = d.getMethods();
                    for (int j = 0; j < n.length; j++)
                    {
                        //获取方法的方法名称
                        String method_ = n[j].getName();
                        if (method_.startsWith("get"))
                        {
                            //获取方法的属性值
                            Object value_ = n[j].invoke(value);
                            if (value_ == null)
                            {
                                continue;
                            }
                            //去除本身类属性
                            if (value_.getClass().getName().equals("java.lang.Class"))
                            {
                                continue;
                            }
                            //将方法名称的前3个字符去掉；
                            String key_ = method_.substring(3);
                            if (isFirstCharUp)
                            {
                                //转换大写截取得到属性名称
                                key_ = key_.substring(0, 1).toUpperCase() + key_.substring(1);
                            }
                            else
                            {
                                key_ = key_.substring(0, 1).toLowerCase() + key_.substring(1);
                            }
                            sb.append("<" + key_ + ">" + value_ + "</" + key_ + ">");
                        }
                    }
                    sb.append("</" + key + ">");
                }
                
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        String resultStr = sb.toString();
        //判断是否制指定了第一个节点
        if (firstStr != null)
        {
            
            int index = resultStr.indexOf("<" + firstStr + ">");
            if (index >= 0)
            {
                int endIndex = resultStr.indexOf("</" + firstStr + ">");
                resultStr =
                    resultStr.substring(index, endIndex + firstStr.length() + 3) + resultStr.substring(0, index)
                        + resultStr.substring(endIndex + firstStr.length() + 3, resultStr.length());
            }
        }
        return resultStr;
    }
    
    /**作者：王少华
     * 实体对象转换为Map对象
     * @param thisObj
     * @return
     */
    public static Map objToMap(Object thisObj)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Class c;
        try
        {
            c = Class.forName(thisObj.getClass().getName());
            Method[] m = c.getMethods();
            for (int i = 0; i < m.length; i++)
            {
                String method = m[i].getName();
                if (method.startsWith("get"))
                {
                    //属性值
                    Object value = m[i].invoke(thisObj);
                    if (value == null)
                    {
                        continue;
                    }
                    //去除本身类属性
                    if (value.getClass().getName().equals("java.lang.Class"))
                    {
                        continue;
                    }
                    //将方法名称的前3个字符去掉；
                    String key = method.substring(3);
                    key = key.substring(0, 1).toLowerCase() + key.substring(1);
                    map.put(key, value);
                    System.out.println(key);
                    //判断如果不是字符串，再放一遍
                    if (!value.getClass().equals(String.class))
                    {
                        //对象的类名称
                        String sbKey = value.getClass().getName();
                        //获取这个对象的类
                        Class d = Class.forName(sbKey);
                        //获取这个对象的所有方法
                        Method[] n = d.getMethods();
                        for (int j = 0; j < n.length; j++)
                        {
                            //获取方法的方法名称
                            String method_ = n[j].getName();
                            if (method_.startsWith("get"))
                            {
                                //获取方法的属性值
                                Object value_ = n[j].invoke(value);
                                if (value_ == null)
                                {
                                    continue;
                                }
                                //去除本身类属性
                                if (value_.getClass().getName().equals("java.lang.Class"))
                                {
                                    continue;
                                }
                                //将方法名称的前3个字符去掉；
                                String key_ = method_.substring(3);
                                //转换小写截取得到属性名称
                                key_ = key_.substring(0, 1).toLowerCase() + key_.substring(1);
                                //放入map中
                                map.put(sbKey + "," + key_, value_);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return map;
    }
    
    /**
     * XML字符串转换为Map
     * @param xmlString
     * @return
     */
    public static Map<String, Object> xmlToMap(String xmlString)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringHelper.isEmpty(xmlString))
        {
            return null;
        }
        List<String> list = null;
        while (true)
        {
            //第一个以<的位置
            int startIndex = xmlString.indexOf("<");
            //第一个>的位置
            int endIndex = xmlString.indexOf(">");
            //获取第一个节点标签的内容
            String strName = xmlString.substring(startIndex + 1, endIndex);
            int lastIndex = xmlString.indexOf("/" + strName);
            //判断第一个节点是否有尾节点，如果没有，则这个节点的内容可以都删除
            if (lastIndex < 0)
            {
                xmlString = xmlString.substring(endIndex + 1, xmlString.length());
                continue;
            }
            //获取标签之间节点的内容
            String content = xmlString.substring(endIndex + 1, lastIndex - 1);
            //判断当前节点是否有子节点如果有子节点,将这个节点删除，但并不删除里面的子节点
            if (content.indexOf("<") >= 0 || content.indexOf("</") >= 0)
            {
                xmlString = xmlString.replace("<" + strName + ">", "");
                xmlString = xmlString.replace("</" + strName + ">", "");
                continue;
            }
            //如果没有子节点，则执行map中的操作
            Object mapValue = map.get(strName);
            //如果当前节点没有放入过此节点元素，则可直接放入
            if (mapValue == null)
            {
                map.put(strName, content);
            }
            else
            {
                //判断当前节点内容有木有 被多次放入到map中，如果没有，则建一个list表放入进去
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
            xmlString = xmlString.replaceFirst("<" + strName + ">" + content + "</" + strName + ">", "");
            if (xmlString.length() < 2)
            {
                break;
            }
        }
        return map;
    }
    
    /**作者：王少华
     * 将xml转换为实体对象
     * @param xml 
     * @param o
     * @return
     * @throws Exception
     */
    public static Object xmlTOEntity(String xml, Object o)
        throws Exception
    {
        
        //得到对象的类型  
        Class cla = o.getClass();
        // 得到类中的所有属性集合 
        Field[] fs = cla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++)
        {
            Field f = fs[i];
            //设置属性为可以访问
            f.setAccessible(true);
            //得到此属性的类型名称
            String type = f.getType().toString();
            //如果是字符串类型，直接设值
            if (type.endsWith("String"))
            {
                //属性名称
                String attrName = f.getName();
                //在xml中查询此节点的值
                int index = xml.indexOf("<" + attrName + ">");
                int endIndex = xml.indexOf("</" + attrName + ">");
                if (index < 0 || endIndex < 0)
                {
                    continue;
                }
                String str = xml.substring(index + attrName.length() + 2, endIndex);
                //给属性设值 
                f.set(o, str);
            }
            else
            {
                /**
                 * 说明此节点还有子节点
                 */
                //属性的类型
                Class cla_ = f.getType();
                //属性名称(节点名称 或者类名称) ，因为xml字符串是大写，所以这里转换成了大写。
                String nopeNameStr = f.getName().toUpperCase();
                int subIndex = xml.indexOf("<" + nopeNameStr + ">");
                int subEndIndex = xml.indexOf("</" + nopeNameStr + ">");
                if (subIndex < 0 || subEndIndex < 0)
                {
                    continue;
                }
                //截取掉其他节点的内容
                String xmlSub = xml.substring(subIndex + nopeNameStr.length() + 2, subEndIndex);
                //创建此类型的实例
                Object d = f.getType().newInstance();
                Field[] fs_ = cla_.getDeclaredFields();
                for (int j = 0; j < fs_.length; j++)
                {
                    Field f_ = fs_[j];
                    //设置属性为可以访问
                    f_.setAccessible(true);
                    String type_ = f_.getType().toString();//得到此属性的类型  
                    //如果是字符串类型，直接设值
                    if (type_.endsWith("String"))
                    {
                        //属性名称
                        String attrName_ = f_.getName();
                        //在xml中查询此节点的值
                        int index_ = xmlSub.indexOf("<" + attrName_ + ">");
                        int endIndex_ = xmlSub.indexOf("</" + attrName_ + ">");
                        if (index_ < 0 || endIndex_ <= 0)
                        {
                            continue;
                        }
                        String str_ = xmlSub.substring(index_ + attrName_.length() + 2, endIndex_);
                        //给属性设值
                        f_.set(d, str_);
                    }
                }
                f.set(o, d);
            }
        }
        return o;
    }
    
    public static void main(String[] args)
        throws Exception
    {
        String xml =
            "<?xml version=\"1.0\" encoding=\"GBK\"?><AIPG><INFO><TRX_CODE>220001</TRX_CODE><VERSION>03</VERSION><DATA_TYPE>2</DATA_TYPE>"
                + "<REQ_SN>2006040000004452015071712445414371082947688934</REQ_SN><RET_CODE>0000</RET_CODE><ERR_MSG>提交成功</ERR_MSG></INFO><VALIDRET>"
                + "<RET_CODE>3999</RET_CODE><ERR_MSG>验证失败</ERR_MSG></VALIDRET></AIPG>";
        xmlTOEntity(xml, new INFO());
        
    }
    
    /**
     * 获取随机数
     * @return
     */
    public static String getSerialNumberSuffix()
    {
        Random random = new Random(new java.util.Date().getTime());
        return String.valueOf(new java.util.Date().getTime()) + Math.abs(random.nextInt(9999));
    }
}
