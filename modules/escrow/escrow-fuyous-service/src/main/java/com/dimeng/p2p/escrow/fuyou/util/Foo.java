/*
 * 文 件 名:  Foo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  beiweiyuan
 * 修改时间:  2017年7月11日
 */
package com.dimeng.p2p.escrow.fuyou.util;

import java.util.Map;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  beiweiyuan
 * @version  [版本号, 2017年7月11日]
 */
public class Foo
{
    
    /** <一句话功能简述>
     * <功能详细描述>
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)
        throws Exception
    {
        String testString = "[A-0000]";
        System.out.println(testString.matches("\\[A-0000\\]"));
        String content = "";
        content = testString.replaceAll("\\[", "\\\\[");
        content = content.replaceAll("\\]", "\\\\]");
        System.out.println(testString + " ----------- " + content);
        System.out.println("result ***************** " + testString.replaceFirst(content, ""));
        
        String xmlString =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ap><plain><resp_code>0000</resp_code><mchnt_cd>0001430F0387865</mchnt_cd><mchnt_txn_ssn>CTCX149975858126521</mchnt_txn_ssn><busi_tp>PW11</busi_tp><total_number>1</total_number><results><result><ext_tp></ext_tp><txn_date>20170711</txn_date><txn_time>094124</txn_time><mchnt_ssn>YHCZ149973739254519044</mchnt_ssn><txn_amt>100000</txn_amt><fuiou_acct_no>8118110130797979</fuiou_acct_no><cust_no>13383061838</cust_no><artif_nm>赵明恒</artif_nm><remark></remark><txn_rsp_cd>0000</txn_rsp_cd><rsp_cd_desc>成功[A-0000]</rsp_cd_desc></result></results></plain><signature>fXdo8ZOXN4cOLNlqEi/HU1RYYagTCwxhKJhodOLasGFqK/i6Eg1iUcSxUUonumplWYpBHaIrer262zxnHjzpzlrgYsveSEt+z/nNbC24d79IB9E4s2F2SHWasghSNalTVZ+/Bwqh4ySyDcgox7OR6HJ4QaV6Hrq478rr58d1IFM=</signature></ap>";
        System.out.println(xmlString);
        //        xmlString = xmlString.replaceAll("\\[", "【");
        //        xmlString = xmlString.replaceAll("\\]", "】");
        System.out.println(xmlString);
        Map<String, Object> map = XmlHelper.xmlToMap(xmlString);
        System.out.println(map);
    }
    
}
