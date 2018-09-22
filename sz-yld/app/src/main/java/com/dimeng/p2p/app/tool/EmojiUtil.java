/*
 * 文 件 名:  EmojiUtil.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月12日
 */
package com.dimeng.p2p.app.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表情处理工具类
 * <功能详细描述>
 * 
 * @author  suwei
 * @version  [版本号, 2016年6月12日]
 */
public final class EmojiUtil
{
    /*
     *单例设计模式（保证类的对象在内存中只有一个）
     *1、把类的构造函数私有
     *2、自己创建一个类的对象
     *3、对外提供一个公共的方法，返回类的对象
     */
    private EmojiUtil(){}
    
    private static EmojiUtil instance = new EmojiUtil();
    
    /**
     * 返回类的对象
     * @return
     */
    public static EmojiUtil getInstance()
    {
        return instance;
    }
    
    /**
     * 匹配输入的文本内容是否含有emoji表情
     * <功能详细描述>
     * @param source
     * @return true/false
     */
    public boolean isContainEmoji(final String source) 
    { 
        if (source != null)
        {
            final Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            final Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find())
            {
                return true;
            }
        }
        return false; 
    }
}
