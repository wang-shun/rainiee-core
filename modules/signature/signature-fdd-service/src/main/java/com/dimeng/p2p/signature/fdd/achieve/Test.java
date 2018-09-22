/*
 * 文 件 名:  Test.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2017年10月25日
 */
package com.dimeng.p2p.signature.fdd.achieve;

import java.util.Calendar;

import com.dimeng.util.StringHelper;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2017年10月25日]
 */
public class Test
{
    
    /** <一句话功能简述>
     * <功能详细描述>
     * @param args
     */
    public static void main(String[] args)
    {
        System.out.println(getHtCode("X201710240002"));
        
        try
        {
            System.out.println(StringHelper.decode("c2ujMTMokjsJ1OZd3pIwCB3UYIt1vgYzx02sdwhLhYA="));
        }
        catch (Throwable e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static String getHtCode(String bidCode)
    {
        //      YDYL--20160001-标的编号-001 年份后的为标的数量递增，后三位为投资人数递增
        //  年份后的数量值四位，最大就是9999，对P2P项目估计数量值合适，对发消费标类型不合适，需重新讨论格式
        StringBuffer htCode = new StringBuffer();
        
        htCode.append("BDF");
        htCode.append("-");
        Calendar ca = Calendar.getInstance();
        htCode.append(ca.get(Calendar.YEAR));
        //或者当前标的数字编号
        
        htCode.append("-");
        htCode.append(bidCode);
        
        htCode.append("-");
        
        int index = 1;
        String recordNo = index + "";
        
        for (int i = 0; i < 4 - recordNo.trim().length(); i++)
        {
            htCode.append("0");
        }
        htCode.append(index);
        System.out.println(htCode);
        return htCode.toString();
    }
}
