package com.dimeng.p2p.weixin.dimengapi.msg;

import java.text.SimpleDateFormat;
import java.util.Date; 

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dimeng.p2p.weixin.dimengapi.WXXmlElementName;

/**
 * 微信消息头
 * 
 * @author yc
 * @version 1.0
 * */
public class HeadMsg {
	// 开发者微信号
	private String toUserName;
	// 发送方帐号（一个OpenID）
	private String fromUserName;
	// 消息创建时间 （整型）
	private String createTime;
	// 消息类型：text\image\
	private String msgType;
	

	
	/**
	 * 一般由程序内部调用，开发者不用调用
	 * */
	public HeadMsg() { 
	    final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		this.createTime = sdf.format(new Date());//初始化创建时间
	}

	public void write(Element root, Document document) {
		Element toUserNameElement = document
				.createElement(WXXmlElementName.TO_USER_NAME);
		toUserNameElement.setTextContent(this.toUserName);
		Element fromUserNameElement = document
				.createElement(WXXmlElementName.FROM_USER_NAME);
		fromUserNameElement.setTextContent(this.fromUserName);
		Element createTimeElement = document
				.createElement(WXXmlElementName.CREATE_TIME);
		createTimeElement.setTextContent(this.createTime);
		Element msgTypeElement = document
				.createElement(WXXmlElementName.MSG_TYPE);
		msgTypeElement.setTextContent(this.msgType);

		root.appendChild(toUserNameElement);
		root.appendChild(fromUserNameElement);
		root.appendChild(createTimeElement);
		root.appendChild(msgTypeElement);
	}

	public void read(Document document) {
		this.toUserName = document
				.getElementsByTagName(WXXmlElementName.TO_USER_NAME).item(0)
				.getTextContent();
		this.fromUserName = document
				.getElementsByTagName(WXXmlElementName.FROM_USER_NAME).item(0)
				.getTextContent();
		this.createTime = document
				.getElementsByTagName(WXXmlElementName.CREATE_TIME).item(0)
				.getTextContent();
		this.msgType = document.getElementsByTagName(WXXmlElementName.MSG_TYPE)
				.item(0).getTextContent();
		
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

 
	
}
