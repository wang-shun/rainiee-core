<%@page import="com.dimeng.p2p.console.servlets.base.malloptsettings.scorerule.ExplainScoreRule"%>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.variables.FileType" %>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "JFGZ";
    String F02 = ObjectHelper.convert(request.getAttribute("F02"), String.class);
    String F03 = ObjectHelper.convert(request.getAttribute("F03"), String.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>积分规则说明</div>
                        <div class="content-container pt20 pl40 pr40 pb20">
                            <form action="<%=controller.getURI(request, ExplainScoreRule.class)%>" method="post"
                                  name="example" onsubmit="return onSubmit();">
                                <ul class="cell xc_jcxx ">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">&nbsp;</span>
                                        <div class="pl200 ml5 info orange" id="info_msg">
                                            <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                            <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.INFO));%>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <!-- <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">&nbsp;</span>
                                        <div class="pl200 ml5">积分说明</div>
                                        <div class="clear"></div>
                                    </li> -->
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>积分说明：</span>
                                        <div class="pl200 ml5">
                                            <textarea name="F02" cols="100" rows="8"
                                                      style="width:700px;height:250px;visibility:hidden;"><%StringHelper.format(out, F02, fileStore);%></textarea>

                                            <p id="errorF02">&nbsp;</p>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                   <!--  <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">&nbsp;</span>
                                        <div class="pl200 ml5">积分规则</div>
                                        <div class="clear"></div>
                                    </li> -->
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>积分规则：</span>
                                        <div class="pl200 ml5">
                                            <textarea name="F03" cols="100" rows="8"
                                                      style="width:700px;height:250px;visibility:hidden;"><%StringHelper.format(out, F03, fileStore);%></textarea>

                                            <p id="errorF03">&nbsp;</p>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5">
                                          <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20" value="提交"/>
                                        </div>
                                    </li>
                                </ul>
                            </form>
                        </div>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script>
    var editor1;
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="F02"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.ARTICLE_ATTACHMENT.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            afterFocus: function () {
                $("#info_msg").removeClass("orange");
                $("#info_msg").html("&nbsp;");
            },
            afterBlur: function () {
                this.sync();
                if (this.count('text') == '') {
                    $("#errorF02").addClass("red");
                    $("#errorF02").html("积分说明不能为空");
                }
                else {
                    $("#errorF02").removeClass("red");
                    $("#errorF02").html("&nbsp;");
                }
            },
            afterChange: function () {
                var maxNum = 60000, text = this.text();
                if (this.count() > maxNum) {
                    text = text.substring(0, maxNum);
                    this.text(text);
                }
            }
        });
        prettyPrint();
    });
    
    var editor2;
    KindEditor.ready(function (K) {
        editor2 = K.create('textarea[name="F03"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.ARTICLE_ATTACHMENT.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            afterFocus: function () {
                $("#info_msg").removeClass("orange");
                $("#info_msg").html("&nbsp;");
            },
            afterBlur: function () {
                this.sync();
                if (this.count('text') == '') {
                    $("#errorF03").addClass("red");
                    $("#errorF03").html("积分规则不能为空");
                }
                else {
                    $("#errorF03").removeClass("red");
                    $("#errorF03").html("&nbsp;");
                }
            },
            afterChange: function () {
                var maxNum = 60000, text = this.text();
                if (this.count() > maxNum) {
                    text = text.substring(0, maxNum);
                    this.text(text);
                }
            }
        });
        prettyPrint();
    });

    function onSubmit() {
        if (editor1.count('text') == '') {
            $("#errorF02").addClass("red");
            $("#errorF02").html("积分说明不能为空");
            return false;
        }
        if(editor2.count('text') == ''){
        	$("#errorF03").addClass("red");
            $("#errorF03").html("积分规则不能为空");
            return false;
        }
        $("#errorF02 #errorF03").removeClass("red");
        $("#errorF02 #errorF03").html("&nbsp;");
        return true;
    }
</script>
</body>
</html>