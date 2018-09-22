<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddEnterpriseXm"%>
<%@page import="com.dimeng.p2p.S62.entities.T6235" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DelDyw" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewAuthentication" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.UpdateDywsx" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddDywsx" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DetailDyw" %>
<%@page import="com.dimeng.p2p.S62.entities.T6234" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddUserInfoXq" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.UpdateProject" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddDyw" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddAnnexWz" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddAnnexMsk" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddGuaranteeXq" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@ page import="com.dimeng.p2p.variables.FileType" %>
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
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    T6234[] t6234s = ObjectHelper.convertArray(request.getAttribute("t6234s"), T6234.class);
    T6230 t6230 = ObjectHelper.convert(request.getAttribute("t6230"), T6230.class);
    T6235 t6235 = ObjectHelper.convert(request.getAttribute("t6235"), T6235.class);
    T6110_F06 userType = EnumParser.parse(T6110_F06.class, request.getAttribute("userType").toString());
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增借款信息
                        </div>
                    </div>
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <%
                                    if (loanId > 0) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateProject.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">项目信息</a></li>
                                <%
                                    }
                                %>
                                <%
                                    if (userType == T6110_F06.ZRR) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddUserInfoXq.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">个人信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewAuthentication.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">个人认证信息</a></li>
                                <%
                                    }
                                %>
                                <%
                                    if (userType == T6110_F06.FZRR) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddEnterpriseXm.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">企业信息</a></li>
                                <%
                                    }
                                %>
                                <li><a href="javascript:void(0);" class="tab-btn select-a">抵押物信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <%
                                    if (T6230_F11.S == t6230.F11) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddGuaranteeXq.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">担保信息</a></li>
                                <%
                                    }
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddAnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">附件(马赛克)</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, AddAnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">附件(完整版)</a></li>
                            </ul>
                        </div>
                        <form action="<%=controller.getURI(request, AddDyw.class)%>" method="post" id="form1"
                              class="form1" onsubmit="return onSubmit()">
                            <input type="hidden" name="loanId" value="<%=loanId %>"/>
                            <input type="hidden" name="userId" value="<%=userId %>"/>

                            <div class="content-container pl40 pt40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>新增/修改抵押物信息：</span>

                                        <div class="pl200 ml5">
                	<textarea name="dywxx" cols="100" rows="8" style="width:700px;height:500px;visibility:hidden;"
                              class="required min-length-20 max-length-60000">
													<%
                                                        if (t6235 != null) {
                                                            StringHelper.format(out, t6235.F04, fileStore);
                                                        }
                                                    %>
													</textarea>
                                            <span tip id="dywError">20-60000字</span>
                                            <span errortip class="" style="display: none"></span>
                                        </div>
                                    </li>

                                    <li>
                                        <div class="pl200 ml5">
                                            <%
                                                   String prevUrl = "";
                                                   if (userType == T6110_F06.FZRR) {
                                                       prevUrl = controller.getURI(request, AddEnterpriseXm.class);
                                                   }else{
                                                       prevUrl = controller.getURI(request, ViewAuthentication.class);
                                                   }
                                                   
                                                   %>
                                        	<input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10"
                                                   onclick="window.location.href='<%=prevUrl %>?loanId=<%=loanId%>&userId=<%=userId%>'"
                                                   value="上一步">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="下一步"/>
                                            <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10"
                                                   onclick="location.href='<%=controller.getURI(request, LoanList.class) %>'"
                                                   value="取消"></div>
                                    </li>
                                </ul>
                            </div>
                        </form>
                    </div>


                </div>
            </div>
        </div>
    </div>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script type="text/javascript">
var flag = false;
    //富文本框
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="dywxx"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.BID_DETAILS_FILE.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            afterBlur: function () {
                this.sync();
                $error = $("#dywError");
                if (this.count() > 60000 || this.count() < 20) {
                    $error.addClass("error_tip");
                    $error.html("长度不对，20-60000字！");
                    flag = false;
                }
                else {
                    $error.removeClass("error_tip");
                    $error.html("20-60000字");
                    flag = true;
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

    function onSubmit(){
    	var obj = $("textarea[name='dywxx']");
    	var val = obj.val();
    	if(val=="") flag = false;
    	if(val.length > 60000 || val.length < 20){
    		obj.next("span").addClass("error_tip");
            obj.next("span").html("长度不对，20-60000字！");
            flag = false;
    	}else{
    		obj.next("span").removeClass("error_tip");
    		obj.next("span").html("20-60000字");
            flag = true;
    	}
    	return flag;
    }
</script>
</body>
</html>