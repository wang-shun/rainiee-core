package com.dimeng.p2p.escrow.fuyou.util;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class PayUtil
{
    /**
     * 金额格式化——HSP
     * <单位分>
     * @param money
     * @return
     */
    public static String getAmt(BigDecimal money)
    {
        DecimalFormat format = new DecimalFormat("0.00");
        String amt = format.format(money);
        if ("0.00".equals(amt))
        {
            return "0";
        }
        return formatAmt(amt);
    }
    
    /**
     * 金额格式化（单位：分）
     * 
     * @作者：何石平 （20151012）
     * @param amt
     *            金额
     * @return String 格式化后
     */
    private static String formatAmt(String amt)
    {
        int i = amt.indexOf(".");
        if (i > 0)
        {
            String z = amt.substring(0, i);
            String f = amt.substring(i + 1);
            if (f.length() > 2)
            {
                f = f.substring(0, 2);
            }
            else if (f.length() == 0)
            {
                f = f + "00";
            }
            else if (f.length() == 1)
            {
                f = f + "0";
            }
            amt = z + f;
        }
        else
        {
            amt = amt + "00";
        }
        return amt;
    }
    
    /**
     * 把XML转成对象
     *
     * @param xmlStr
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public static <T> T xmlStringToBean(String xmlStr, Class<T> cls)
    {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        xstream.autodetectAnnotations(true);
        return (T)xstream.fromXML(xmlStr);
    }
    
    /** JAXB实现xml与java bean互转
     * @param xml
     * @param c
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T converyToJavaBean(String xml, Class<T> c)
    {
        T t = null;
        try
        {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T)unmarshaller.unmarshal(new StringReader(xml));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return t;
    }
    
    public static void main(String[] args)
    {
        
        try
        {
            /* String xmlStr =
                "<plain><resp_code>0000</resp_code><desc_code>成功</desc_code><mchnt_txn_ssn>YHXX15238477207617516</mchnt_txn_ssn><login_id>13362002020</login_id><mchnt_cd>0002900F0096235</mchnt_cd><txn_ssn>YHKK15238449500443040</txn_ssn><bank_nm>中国银行深圳市分行</bank_nm><card_no>6216612000002945671</card_no><examine_st>0</examine_st><remark></remark></plain>";
            QueryBankReq req = xmlStringToBean(xmlStr, QueryBankReq.class);
            System.out.println(req);*/
            String str =
                "<ap><plain><resp_code>0000</resp_code><desc_code>成功</desc_code><mchnt_txn_ssn>YHXX152361404849465562</mchnt_txn_ssn><login_id>13168094798</login_id><mchnt_cd>0005840F0959282</mchnt_cd><txn_ssn>YHKK152282429327323161</txn_ssn><bank_nm>招商银行深圳振华支行</bank_nm><card_no>6214837832719375</card_no><examine_st>2</examine_st><remark>你好，请提交新卡签约小票与新卡+身份证+新卡授权书合照，谢谢</remark></plain><signature>PESrlc8y5usPeGSm7Lzu9X7eCPt9vHdS8aMeKuDJvSq9t4fY1YkT7aoIUbawHGsgFbJC1XbrAuAvR5Lwd+eVaTJ0gD9HiG9NiO8qABje6hlLabrfNjKKfLShPZbYeIwJRHrCSEx05r92qF24uwcXvHri0/NgnFhLmED+K3zG9/A=</signature></ap>";
            Map<String, Object> xmlMap = XmlHelper.xmlToMap(str);
            /*Gson gson = new Gson();
            QueryBankReq queryReq = gson.fromJson("<desc_code>成功</desc_code>", QueryBankReq.class);*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
