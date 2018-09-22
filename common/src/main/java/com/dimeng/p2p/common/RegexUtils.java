package com.dimeng.p2p.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dimeng.util.StringHelper;

public class RegexUtils {
	
	/**
	 * 帐号正则表达式
	 */
	public static final String  USER_NAME_REG = "^[a-zA-Z]([\\w]{5,17})$";
	
	/**
	 * 密码正则表达式
	 */
	public static final String  PASSWORD_REG = ".{6,20}";
	
	/**
	 * 邀请码正则表达式
	 */
	public static final String  INVITATION_REG = "^[A-Za-z0-9]{0,20}$";
	
    /**
     * 短信验证码正则表达式
     */
    public static final String VERIFECODE_REG = "^[0-9]{0,6}$";
    
    /**
     * 身份证格式CODE
     */
    private static final char CHECK_CODE[] = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    
    private static final int[] WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    
    /** 
     * 手机号验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */
    public static boolean isMobile(String str)
    {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号  
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
    
    /** 
     * 电话号码验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */
    public static boolean isPhone(String str)
    {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-?[0-9]{5,10}$"); // 验证带区号的  
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的  
        if (str.length() > 9)
        {
            m = p1.matcher(str);
            b = m.matches();
        }
        else
        {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }
    
    /**
     * 匹配Luhn算法：可用于检测银行卡卡号
     * @param cardNo
     * @return
     */
    public static boolean matchLuhn(String cardNo)
    {
        int[] cardNoArr = new int[cardNo.length()];
        for (int i = 0; i < cardNo.length(); i++)
        {
            cardNoArr[i] = Integer.valueOf(String.valueOf(cardNo.charAt(i)));
        }
        for (int i = cardNoArr.length - 2; i >= 0; i -= 2)
        {
            cardNoArr[i] <<= 1;
            cardNoArr[i] = cardNoArr[i] / 10 + cardNoArr[i] % 10;
        }
        int sum = 0;
        for (int i = 0; i < cardNoArr.length; i++)
        {
            sum += cardNoArr[i];
        }
        return sum % 10 == 0;
    }
    
    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * 
      * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId)
    {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
            || !nonCheckCodeCardId.matches("\\d+"))
        {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++)
        {
            int k = chs[i] - '0';
            if (j % 2 == 0)
            {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }
    
    /**
     * 校验银行卡卡号
     * 
      * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId)
    {
        if (cardId == null || cardId.trim().length() < 16)
        {
            return false;
        }
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N')
        {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }
    
    /**
     * 校验身份证号是否合法.
     * 
     * @param id
     *            身份证号码
     * @return {@code boolean} 是否合法
     * @throws Throwable
     */
    public static boolean isValidId(String id)
        throws Throwable
    {
        if (StringHelper.isEmpty(id) || id.length() != 18)
        {
            return false;
        }
        
        int sum = 0;
        int num;
        for (int index = 0; index < 17; index++)
        {
            num = id.charAt(index) - '0';
            if (num < 0 || num > 9)
            {
                return false;
            }
            sum += num * WEIGHT[index];
        }
        return CHECK_CODE[sum % 11] == Character.toUpperCase(id.charAt(17));
    }
}
