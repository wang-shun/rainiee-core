package com.dimeng.p2p.modules.nciic.util;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

//import com.dimeng.common.util.FileUtil;

public class JSONUtil
{
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    protected static final Logger logger = Logger.getLogger(JSONUtil.class);
    
    /**
     * ���ط�null��json��
     * @param obj
     * @param clearNullProperty
     * @return
     */
    public static String toJSON(Object obj)
    {
        try
        {
            String propertyStr = mapper.writeValueAsString(obj);
            propertyStr = propertyStr.replaceAll("\"[a-zA-Z0-9]+\":null(,)?", "").replaceAll(",([\\]\\}])", "$1");
            return propertyStr;
        }
        catch (Exception e)
        {
            logger.error(e, e);
            return null;
        }
    }
    
    public static <T> T toObject(String json, Class<T> valueType)
    {
        try
        {
            return mapper.readValue(json, valueType);
        }
        catch (Exception e)
        {
            logger.error(e, e);
            return null;
        }
    }
    
    public static <T> T toObject(InputStream ins, Class<T> valueType)
    {
        try
        {
            return mapper.readValue(ins, valueType);
        }
        catch (Exception e)
        {
            logger.error(e, e);
            return null;
        }
    }
    
    /*public static void main(String[] args) throws Exception {
    	
    	String json = "[{\"id\":0,\"memberId\":0,\"ip\":null,\"interfaceName\":\"getProductCountByCompanyId\",\"visitTime\":1371439883910,\"elapse\":2,\"result\":1,\"errMsg\":\"\"}, "+
    	" {\"id\":0,\"memberId\":0,\"ip\":null,\"interfaceName\":\"getProductCountByCompanyId\",\"visitTime\":1371439883910,\"elapse\":2,\"result\":1,\"errMsg\":\"\"}]";
    	com.dimeng.b2b.app.entity.AppVisitLog list = toObject(json, com.dimeng.b2b.app.entity.AppVisitLog[].class);
    	
    	
    	String text = FileUtil.readFromFile("C:\\json.txt");
    	com.dimeng.b2b.app.entity.Product[] arr = toObject(text, com.dimeng.b2b.app.entity.Product[].class);
    	for (Product p : arr)
    		System.out.println(p.getName());
    	
    	URL url = new URL("http://localhost:8080/app/m?action=getCompanyById&companyId=1000002");
    	com.dimeng.b2b.app.entity.ResultObject ro = toObject(url.openStream(), 
    		com.dimeng.b2b.app.entity.ResultObject.class);
    	if (ro.getStatus().equals("OK")) {
    		String valuetype = ro.getType();
    		if (valuetype != null) {
    			com.dimeng.b2b.app.entity.Company company = toObject(ro.getValue(), 
    				com.dimeng.b2b.app.entity.Company.class);
    			System.out.println(company.getTelephone());
    		}
    	} else {
    		System.out.println(ro.getMessage());
    	}
    	
    	url = new URL("http://192.168.0.238:8080/app/m?action=getProductById&prodId=2834510");
    	//product = toObject(url.openStream(), com.dimeng.b2b.app.entity.Product.class);
    	//System.out.println(product.getName());
    }*/
    
    public static void main(String args[])
        throws Exception
    {
        
    }
    
}