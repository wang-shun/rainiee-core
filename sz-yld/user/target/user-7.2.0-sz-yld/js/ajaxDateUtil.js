/**
 * Created by luoyi on 2015/5/19.
 * 通用js方法
 */
function getTime(param) {
    var ts = param || 0;
    var y,m,d;
    if (!isNaN(param)) {//如果格式为timeStamp格式.
        t = ts ? new Date(ts) : new Date();
        y = t.getFullYear();
        m = t.getMonth()+1;
        d = t.getDate();
    }else{
        param = param.split('-');
        var date = new Date();
        date.setUTCFullYear(param[0], param[1] - 1, param[2]);
        date.setUTCHours(0, 0, 0, 0);
        y = date.getFullYear();
        m = date.getMonth()+1;
        d = date.getDate();
    }
    // 可根据需要在这里定义时间格式
    var result = y+'-'+(m<10?'0'+m:m)+'-'+(d<10?'0'+d:d);
    return result;
}

//格式化金额
//优化负数格式化问题
function fmoney(s, n) {
    n = n > 0 && n <= 20 ? n : 2;
    var f = s < 0 ? "-" : ""; //判断是否为负数
    s = parseFloat((Math.abs(s) + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";//取绝对值处理, 更改这里n数也可确定要保留的小数位
    var l = s.split(".")[0].split("").reverse(),
        r = s.split(".")[1];
    var t = "";
    for(var i = 0; i < l.length; i++ ) {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return f + t.split("").reverse().join("") + "." + r.substring(0,2);//保留2位小数  如果要改动 把substring 最后一位数改动就可
}
