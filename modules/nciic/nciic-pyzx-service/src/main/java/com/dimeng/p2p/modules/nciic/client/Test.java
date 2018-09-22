package com.dimeng.p2p.modules.nciic.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;

import org.codehaus.xfire.client.Client;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dimeng.p2p.modules.nciic.util.Base64;
import com.dimeng.p2p.modules.nciic.util.CompressStringUtil;
import com.dimeng.p2p.modules.nciic.util.DocumentUtil;

public class Test
{
    
    public static String soapUrl = "";
    
    private static String userName = "";
    
    private static String passWord = "";
    
    public static void main(String args[])
        throws Exception
    {
        String s =
            "m00Am2GYuRBA0x3uqijhk9kIF5TJ0twA2Ht82/QMyYYzXtqdzB8JEHagW067yVsthKZ1W58WukKW\\r\\ngwj5NC5pZWmwHB7KCOB7X3RBwUc+k3fqAoKdaEFg3ZlNUttj+dhy93prcC7Jnr5n3GRaY9SVoxO6\\r\\nUnc4XHWke4rt9JVNoII\\u003d\\r\\n";
        System.out.println(s.replace("\\r\\n", "").replace("\\u003d", "="));
        System.out.println(new String(
            "m00Am2GYuRBA0x3uqijhk9kIF5TJ0twA2Ht82/QMyYYzXtqdzB8JEHagW067yVsthKZ1W58WukKW\\r\\ngwj5NC5pZWmwHB7KCOB7X3RBwUc+k3fqAoKdaEFg3ZlNUttj+dhy93prcC7Jnr5n3GRaY9SVoxO6\\r\\nUnc4XHWke4rt9JVNoII\\u003d\\r\\n".getBytes("Unicode"),
            "UTF-8"));
    }
    
    public static String decodeUnicode(final String dataStr)
    {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1)
        {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1)
            {
                charStr = dataStr.substring(start + 2, dataStr.length());
            }
            else
            {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char)Integer.parseInt(charStr, 16); // 16进制parse整形字符串。                    
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }
    
    private void testwsQuery()
    {
        try
        {
            
            StringBuffer queryInfo = new StringBuffer("");
            BufferedReader br = new BufferedReader(new FileReader("E:/wsquery.xml"));
            String str = br.readLine();
            while (str != null)
            {
                queryInfo.append(str);
                str = br.readLine();
                
            }
            
            br.close();
            System.out.println("prams:" + queryInfo);
            Client client = new Client(new URL("http://www.pycredit.com:9001/services/WebServiceSingleQuery?wsdl"));
            Object[] results =
                client.invoke("queryReport", new Object[] {"wstzwsquery", "{MD5}6zAp7CvxugS3XjexLprBmQ==", queryInfo,
                    "xml"});
            
            if (results[0] instanceof String)
            {
                System.out.println("resut:" + results[0].toString());
            }
            else if (results[0] instanceof org.w3c.dom.Document)
            {
                org.w3c.dom.Document doc = (org.w3c.dom.Document)results[0];
                
                Element element = doc.getDocumentElement();
                NodeList children = element.getChildNodes();
                Node node = children.item(0);
                System.out.println("result content:" + node.getNodeValue());
                
                String ba64 = DocumentUtil.readStringNode(node.getNodeValue(), "returnValue");
                System.out.println(ba64);
                Base64 base64 = new Base64();
                byte[] re = base64.decode(ba64);
                String xml = new CompressStringUtil().decompress(re);
                System.out.println(xml);
            }
            
        }
        catch (Throwable e)
        {
            System.out.println("(Exception :" + e.getMessage());
        }
        
    }
}
