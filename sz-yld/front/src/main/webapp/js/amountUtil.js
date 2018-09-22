function chinaCost(numberValue) {
	if(isNaN(numberValue)){
		return "";
	}
	numberValue = new String(Math.round(numberValue * 100)); // 数字金额
	var chineseValue = ""; // 转换后的汉字金额
	var str1 = "零壹贰叁肆伍陆柒捌玖"; // 汉字数字
	var str2 = "万仟佰拾亿仟佰拾万仟佰拾元角分"; // 对应单位
	var len = numberValue.length; // numberValue 的字符串长度
	var ch1; // 数字的汉语读法
	var ch2; // 数字位的汉字读法
	var nZero = 0; // 用来计算连续的零值的个数
	var str3; // 指定位置的数值
	if (len > 15) {
		//alert("超出计算范围");
		return "超出计算范围";
	}
	if (numberValue == 0) {
		chineseValue = "&nbsp;";
		return chineseValue;
	}
	str2 = str2.substr(str2.length - len, len); // 取出对应位数的str2的值
	for (var i = 0; i < len; i++) {
		str3 = parseInt(numberValue.substr(i, 1), 10); // 取出需转换的某一位的值
		if (i != (len - 3) && i != (len - 7) && i != (len - 11)
				&& i != (len - 15)) {
			if (str3 == 0) {
				ch1 = "";
				ch2 = "";
				nZero = nZero + 1;
			} else if (str3 != 0 && nZero != 0) {
				ch1 = "零" + str1.substr(str3, 1);
				ch2 = str2.substr(i, 1);
				nZero = 0;
			} else {
				ch1 = str1.substr(str3, 1);
				ch2 = str2.substr(i, 1);
				nZero = 0;
			}
		} else { // 该位是万亿，亿，万，元位等关键位
			if (str3 != 0 && nZero != 0) {
				ch1 = "零" + str1.substr(str3, 1);
				ch2 = str2.substr(i, 1);
				nZero = 0;
			} else if (str3 != 0 && nZero == 0) {
				ch1 = str1.substr(str3, 1);
				ch2 = str2.substr(i, 1);
				nZero = 0;
			} else if (str3 == 0 && nZero >= 3) {
				ch1 = "";
				ch2 = "";
				nZero = nZero + 1;
			} else {
				ch1 = "";
				ch2 = str2.substr(i, 1);
				nZero = nZero + 1;
			}
			if (i == (len - 11) || i == (len - 3)) { // 如果该位是亿位或元位，则必须写上
				ch2 = str2.substr(i, 1);
			}
		}
		chineseValue = chineseValue + ch1 + ch2;
	}

	/*if (str3 == 0) { // 最后一位（分）为0时，加上“整”
		chineseValue = chineseValue + "整";
	}*/

	return chineseValue;
}