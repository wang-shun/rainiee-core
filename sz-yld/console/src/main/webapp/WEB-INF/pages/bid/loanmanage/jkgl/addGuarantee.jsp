<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S61.entities.T6180" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddGuarantee" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddGuaranteeXq" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Jg" %>
<%@ page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@ page import="com.dimeng.p2p.variables.FileType" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "BDGL";
    int loanId = IntegerParser
            .parse(request.getParameter("loanId"));
    int userId = IntegerParser
            .parse(request.getParameter("userId"));
    int jgId = IntegerParser
            .parse(request.getParameter("jgId"));
    String des = ObjectHelper.convert(request.getAttribute("des"), String.class);
    T6180 jgxx = ObjectHelper.convert(request.getAttribute("jgxx"), T6180.class);
    Jg[] jgs = ObjectHelper.convertArray(request.getAttribute("jgs"), Jg.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增担保信息
                        </div>

                    </div>

                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tab-content-container p20">


                            <div class="tab-item">
                                <form action="<%=controller.getURI(request, AddGuarantee.class)%>" method="post"
                                      onsubmit="return submitForm()">
                                    <input type="hidden" name="loanId" value="<%=loanId%>"/>
                                    <input type="hidden" name="userId" value="<%=userId%>"/>
                                    <input type="hidden" name="jgId" value="<%=jgId%>"/>
                                    <ul class="gray6">

                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>担保机构：</span>
                                            <select name="F03" class="border mr20 h32 mw100" id="dbgs"
                                                    onchange="changeDbgs()">
                                                <option value="">全部</option>
                                                <%
                                                    int id = IntegerParser.parse(request.getAttribute("F03"));
                                                %>
                                                <%
                                                    if (jgs != null) {
                                                        for (Jg jg : jgs) {
                                                            if (jg == null || jg.id == userId) {
                                                                continue;
                                                            }
                                                %>
                                                <option value="<%=jg.id%>" <%if (jg.id == jgId || jg.id == id) { %>
                                                        selected="selected" <%} %>><%
                                                    StringHelper.filterHTML(out, jg.name); %></option>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </select>
                                            <span id="errortip" errortip class="red"></span>
                                        </li>
                                        <li class="mb10 clearfix"><div class=" pr pl200"><span class="display-ib w200 tr mr5 pa left0">担保机构介绍：</span>
                                        <div class="pl10">
                                        	<%
	                                                if (null != jgxx) {
	                                                    StringHelper.filterHTML(out, jgxx.F04);
	                                                }else{
	                                         %>
	                                         &nbsp;
	                                         <%} %>
	                                         </div>
	                                         <p></p>
	                                        </div> 
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5 fl">担保情况：</span>
                                            <div class="pl200 ml5">
												<textarea name="dbzz" cols="100" rows="8" style="width:700px;height:500px;visibility:hidden;"
								                          class="w400 h120 border p5 required min-length-20 max-length-500">
												<%
								                    if (null != jgxx) {
								                        StringHelper.filterHTML(out, jgxx.F02);
								                    }
								                %>
												</textarea>
                                            </div>
                                        </li>

                                        <li class="mb10">
                                            <div class="pl200 ml5">
                                                <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20"
                                                       value="保存"/>
                                                <input type="button"
                                                       onclick="window.location.href='<%=controller.getURI(request, AddGuaranteeXq.class) %>?loanId=<%=loanId %>&userId=<%=userId %>'"
                                                       class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="返回"/>
                                            </div>
                                        </li>
                                    </ul>
                                </form>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script type="text/javascript">

    var editor1;
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="dbzz"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.ARTICLE_ATTACHMENT.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            afterBlur: function () {
                this.sync();
                /* if(this.count('text')=='')
                 {
                 $("#errorContent").addClass("red");
                 $("#errorContent").html("项目风控内容不能为空");
                 }
                 else
                 {
                 $("#errorContent").removeClass("red");
                 $("#errorContent").html("&nbsp;");
                 } */
            },
            afterChange: function () {
                var maxNum = 60000, text = this.text();
                if (this.count() > maxNum) {
                    text = text.substring(0, maxNum);
                    this.text(text);
                }
            }
        });
    });

    function changeDbgs() {
        var jgId = $("#dbgs").val();
        location.href = "<%=controller.getURI(request, AddGuarantee.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&jgId=" + jgId;
    }
    function submitForm() {
        var typeId = $("#dbgs").val();
        if (typeId == '') {
            $("#errortip").text("请选择担保公司");
            $("#errortip").addClass("red");
            return false;
        }
        return true;
    }
</script>
</body>
</html>