/*package com.dimeng.p2p.modules.shuangqian;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.p2p.escrow.shuangqian.util.RsaHelper;
import com.dimeng.p2p.modules.shuangqian.entity.Identity;
import com.dimeng.p2p.modules.shuangqian.entity.IdentityFailRet;
import com.dimeng.p2p.modules.shuangqian.entity.IdentityMatching;
import com.dimeng.p2p.modules.shuangqian.entity.IdentityMatchingRet;
import com.dimeng.p2p.modules.shuangqian.entity.IdentityRet;
import com.dimeng.p2p.modules.shuangqian.util.Common;
import com.dimeng.p2p.modules.shuangqian.util.HttpClientHandler;
import com.dimeng.p2p.modules.shuangqian.util.MD5;
import com.dimeng.p2p.modules.shuangqian.util.ResultCode;
import com.dimeng.p2p.variables.defines.nciic.ShuangQianVariable;
import com.dimeng.util.parser.BigDecimalParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Client {

	
	public static void main(String [] args)
	{
		String id = "440103198909018361";
		String name = "李高格";
		String res = call(id,name);
		System.out.println("res:"+res);
		
	}
	
	public static String call(String id,String name){
		IdentityMatching entity = new IdentityMatching();
		entity.setIdentificationNo(id);
		entity.setRealName(name);
		
		ConfigureProvider configureProvider = serviceResource
				.getResource(ConfigureProvider.class);
		String actionUrl = configureProvider.format(ShuangQianVariable.SERVICE_VALID_URL);
		String notifyURL = configureProvider.format(ShuangQianVariable.ID_VALID_CALLBACK_URL);
		Gson gs = new Gson();
		IdentityMatchingRet imr = null;
		List<Identity> identityList = new ArrayList<Identity>();
		
		Identity identity = new Identity();
		identity.setRealName(entity.getRealName());
		identity.setIdentificationNo(entity.getIdentificationNo());
		identityList.add(identity);
		
		String IdentityJsonList = Common.JSONEncode(identityList);
		
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("PlatformMoneymoremore", configureProvider.format(com.dimeng.p2p.escrow.shuangqian.variables.ShuangQianVariable.SQ_PLATFORMMONEYMOREMORE));
		params.put("IdentityJsonList", IdentityJsonList);
		String RandomTimeStamp = "";
		if (false) {
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			RandomTimeStamp = Common.getRandomNum(2) + sdf.format(d);
		}
		params.put("RandomTimeStamp", RandomTimeStamp);
		params.put("NotifyURL", notifyURL);
		
		//String dataStr = paramConcat(params);//拼接签名所需字段
		
		IdentityJsonList = Common.UrlEncoder(IdentityJsonList, configureProvider.format(ShuangQianVariable.CHARSET));//发送时需要urlEncoder
		params.put("IdentityJsonList", IdentityJsonList);
		
		String dataStr = singBulid(params);
		
		RsaHelper rsa = RsaHelper.getInstance();
		if (false) {
			dataStr = MD5.getMD5Info(dataStr);
		}
		String SignInfo = rsa.signData(dataStr, configureProvider.format(com.dimeng.p2p.escrow.shuangqian.variables.ShuangQianVariable.SQ_PRIVATEPKCS8_KEY));
		params.put("SignInfo", SignInfo);

		String result = HttpClientHandler.doPost(params, actionUrl);
		Map<String, String> retsultMap = gs.fromJson(result, new TypeToken<Map<String, String>>() {}.getType());
		
		System.out.println("result:"+result);
		System.out.println("resultMap:"+retsultMap.toString());
		
		String platformMoneymoremore = retsultMap.get("PlatformMoneymoremore");
		String identityJsonList = retsultMap.get("IdentityJsonList");
		String identityFailJsonList = retsultMap.get("IdentityFailJsonList");
		String amount = retsultMap.get("Amount");
		String randomTimeStamp = retsultMap.get("RandomTimeStamp");
		String resultCode = retsultMap.get("ResultCode");
		String signInfoRet = retsultMap.get("SignInfo");
		
		identityJsonList = Common.UrlDecoder(identityJsonList, configureProvider.format(ShuangQianVariable.CHARSET));//解码
		identityFailJsonList = Common.UrlDecoder(identityFailJsonList, configureProvider.format(ShuangQianVariable.CHARSET));//解码

		Map<String, String> paramsRet = new LinkedHashMap<String, String>();
		paramsRet.put("platformMoneymoremore", platformMoneymoremore);
		paramsRet.put("identityJsonList", identityJsonList);
		paramsRet.put("identityFailJsonList", identityFailJsonList);
		paramsRet.put("amount", amount);
		paramsRet.put("randomTimeStamp", randomTimeStamp);
		paramsRet.put("resultCode", resultCode);
		
		String str = paramConcat(paramsRet);
		if (verifyByRSA(str, signInfoRet) && "88".equals(resultCode)) {
			List<IdentityRet> irList = gs.fromJson(identityJsonList, new TypeToken<List<IdentityRet>>() {}.getType());
			imr = new IdentityMatchingRet();
			imr.setRealName(Common.UrlDecoder(irList.get(0).getRealName(),configureProvider.format(ShuangQianVariable.CHARSET)));
			imr.setIdentificationNo(irList.get(0).getIdentificationNo());
			if (irList.get(0).getState().equals("0")) {
				imr.setMatchingResult(false);
				imr.setResultCode(ResultCode.FAIL.getCode());
				imr.setMessage(Common.UrlDecoder("匹配失败", configureProvider.format(ShuangQianVariable.CHARSET)));
			} else if (irList.get(0).getState().equals("1")) {
				imr.setMatchingResult(true);
				imr.setResultCode(ResultCode.SUCCESS.getCode());
				imr.setMessage(Common.UrlDecoder("匹配成功", configureProvider.format(ShuangQianVariable.CHARSET)));
			}
			imr.setAmount(BigDecimalParser.parse(amount));
			//String signStr = createMD5SignStr(imr);
			//imr.setSignStr(signStr);
		} else {
			List<IdentityFailRet> irList = gs.fromJson(identityJsonList, new TypeToken<List<IdentityFailRet>>() {}.getType());
			imr = new IdentityMatchingRet();
			imr.setRealName(Common.UrlDecoder(irList.get(0).getRealName(), configureProvider.format(ShuangQianVariable.CHARSET)));
			imr.setIdentificationNo(irList.get(0).getIdentificationNo());
			imr.setMatchingResult(false);
			imr.setResultCode("失败:9999");
			imr.setMessage(retsultMap.get("Message"));
			imr.setAmount(BigDecimalParser.parse(amount));
			//String signStr = createMD5SignStr(imr);
			//imr.setSignStr(signStr);
		}
		return gs.toJson(imr);
	}
}
*/