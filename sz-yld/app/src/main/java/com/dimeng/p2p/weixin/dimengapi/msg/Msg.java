package com.dimeng.p2p.weixin.dimengapi.msg;

import java.io.IOException;
import java.io.OutputStream;

import org.w3c.dom.Document;

/**
 * 抽象消息类 提供各种消息类型字段、头部消息对象以及写入和读取抽象方法
 * 
 * @author yc
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 * */
public abstract class Msg
{
    
    /** 文本消息 */
    public static final String MSG_TYPE_TEXT = "text";
    
    /** 图片消息 */
    public static final String MSG_TYPE_IMAGE = "image";
    
    /** 音乐消息 */
    public static final String MSG_TYPE_MUSIC = "music";
    
    /** 地理位置消息 */
    public static final String MSG_TYPE_LOCATION = "location";
    
    /** 链接消息 */
    public static final String MSG_TYPE_LINK = "link";
    
    /** 图文消息 */
    public static final String MSG_TYPE_IMAGE_TEXT = "news";
    
    /** 事件消息 */
    public static final String MSG_TYPE_EVENT = "event";
    
    /** 语音识别消息 */
    public static final String MSG_TYPE_VOICE = "voice";
    
    /** 视频消息 */
    public static final String MSG_TYPE_VIDEO = "video";
    
    /** 消息头 */
    protected HeadMsg head;
    
    public static final String MSG_TYPE_JSON = "json";
    
    public static final String MSG_TYPE_XML = "xml";
    
    /**
     * 写入消息内容
     * @param document
     */
    public abstract void write(Document document);
    
    /**
     * 读取消息内容
     * @param document
     */
    public abstract void read(Document document);
    
    /**
     * 获取节点文本内容
     * @param document 文档
     * @param element 节点名称
     * @return 内容
     */
    protected String getElementContent(Document document, String element)
    {
        return document.getElementsByTagName(element).item(0).getTextContent();
    }
    
    public HeadMsg getHead()
    {
        return head;
    }
    
    public void setHead(HeadMsg head)
    {
        this.head = head;
    }
    
    public String getToUserName()
    {
        return head.getToUserName();
    }
    
    public void setToUserName(String toUserName)
    {
        head.setToUserName(toUserName);
    }
    
    public String getFromUserName()
    {
        return head.getFromUserName();
    }
    
    public void setFromUserName(String fromUserName)
    {
        head.setFromUserName(fromUserName);
    }
    
    public String getCreateTime()
    {
        return head.getCreateTime();
    }
    
    public void setCreateTime(String createTime)
    {
        head.setCreateTime(createTime);
    }
    
    public <S extends Msg> S getReplayMsg(Class<S> msg)
    {
        S returnMsg = null;
        try
        {
            returnMsg = msg.newInstance();
            returnMsg.setCreateTime(this.getCreateTime());
            returnMsg.setFromUserName(this.getToUserName());
            returnMsg.setToUserName(this.getFromUserName());
        }
        catch (InstantiationException e)
        {
        }
        catch (IllegalAccessException e)
        {
        }
        return returnMsg;
    }
    
    public String getMsgType()
    {
        return MSG_TYPE_XML;
    }
    
    public void write(OutputStream os)
        throws IOException
    {
    };
}
