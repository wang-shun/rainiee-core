<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.thirdstat.ThirdStat" %>
<%@page import="com.dimeng.framework.config.entity.VariableBean" %>
<%@page import="com.dimeng.util.filter.TextAreaFilter" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "DSFTJSZ";
    VariableBean variable = ObjectHelper.convert(request.getAttribute("codeValue"), VariableBean.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>第三方统计设置
                        </div>
                        <form id="form1" action="<%=controller.getURI(request, ThirdStat.class)%>" method="post"
                              class="form1">
                            <div class="content-container p40">
                                <h2 class="f16 fb lh40">百度流量统计代码：</h2>
                                <ul class="gray6 lh30">
                                    <li>1. 请先进入 <a href="http://tongji.baidu.com/web/register" target="_blank"
                                                  style="color:#4776c8">百度统计</a> 系统免费注册
                                    </li>
                                    <li>2. 请将注册后显示的统计代码复制到文件框，点“保存”</li>
                                    <li>3. 可通过登录百度流查统计查看本站流量管理和运营分析</li>
                                    <li>
                                        <textarea id="TextId" class="ww90 p5 mh300 border required"
                                                  name="codeValue"><%new TextAreaFilter(out).append(variable.getValue());%></textarea>
                                        <p><span tip>注：禁止在输入alert,prompt,confirm,汉字等非法字符!</span>
                                        <span errortip class="" style="display: none"></span></p>
                                    </li>
                                </ul>                                
                                <div class="tc f16">
                                    <input value="提交 " class=" btn btn-blue2 radius-6 pl20 pr20"
                                           fromname="form1" type="button" onclick="checkSubmit()"/>
                                </div>
                            </div>
                        </form>
                    </div>
                    <%
                        String message = controller.getPrompt(request, response, PromptLevel.WARRING);
                        if (!StringHelper.isEmpty(message)) {
                    %>
                    <div class="popup-box" id="showDiv" style="min-height:250px;width: 450px;">
                        <div class="popup-title-container">
                            <h3 class="pl20 f18">提示</h3>
                            <a class="icon-i popup-close2" href="javascript:void(0);"
                               onclick="$('#showDiv').hide()"></a>
                        </div>
                        <div class="popup-content-container pt20 ob20 clearfix">
                            <div class="tc mb30">
                                <span class="icon-i w30 h30 va-middle radius-yes-icon"></span>
			        	<span class="f20 h30 va-middle ml10">
			            <%StringHelper.filterHTML(out, message); %>
			            </span>
                            </div>
                            <div class="tc f16 "><input type="button" name="button2" onclick="$('#showDiv').hide()"
                                                        value="确认" class="btn-blue2 btn white radius-6 pl20 pr20"/>
                            </div>
                        </div>
                    </div>
                    <%} %>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
    function checkSubmit() {
        var textarea = $("#TextId");
        $error = textarea.nextAll("p[errortip]");
        $tip = textarea.nextAll("p[tip]");
        var flag = false;
        if (textarea.val() != "") {
            flag = true;
        } else {
            $error.addClass("error_tip");
            $error.html("不能为空!");
            $tip.hide();
            $error.show();
            flag = false;
        }
        if (flag) {
            $("#form1").submit();
        }
    }
</script>
</body>
</html>