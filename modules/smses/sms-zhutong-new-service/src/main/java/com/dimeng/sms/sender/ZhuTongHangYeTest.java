package com.dimeng.sms.sender;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
/**
 * 类型描述：测试
 * @author 胥耀
 * @date 日期：2017-11-25
 */

public class ZhuTongHangYeTest {

	public static void main(String[] args) {
		String url="http://www.api.zthysms.com/sendSms.do";
		String username="puhuibaohy";  //账号
		String password="QuDyB1";  //密码
		String tkey=TimeUtil.getNowTime("yyyyMMddHHmmss");
		String mobile="15019231132";  //发送的手机号
		String content="今天天气不错【迪蒙】";   //内容
		String xh="";
		try {
			content=URLEncoder.encode(content, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		String param="url="+url+"&username="+username+"&password="+MD5Update.getMD5(MD5Update.getMD5(password)+tkey)+"&tkey="+tkey+"&mobile="+mobile+"&content="+content+"&xh="+xh;
		String ret=HttpReq.sendPost(url,param);//定时信息只可为post方式提交
		System.out.println("ret:"+ret);
		System.out.println(param);

	}

}
