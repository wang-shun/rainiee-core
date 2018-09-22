<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewGuarantee" %>
<%@page import="com.dimeng.p2p.S62.entities.T6237" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.Dbxx" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddGuaranteeXq" %>
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
    int id = IntegerParser
            .parse(request.getParameter("id"));
    int type = IntegerParser
            .parse(request.getParameter("type"));
    Dbxx t6236 = ObjectHelper.convert(request.getAttribute("t6236"), Dbxx.class);
    T6237 t6237 = ObjectHelper.convert(request.getAttribute("t6237"), T6237.class);
    String des = ObjectHelper.convert(request.getAttribute("des"), String.class);
    String operationJK = request.getParameter("operationJK");
    if (StringHelper.isEmpty(operationJK)) {
        operationJK = "CK";
    }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>查看担保信息
                        </div>

                    </div>

                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tab-content-container p20">


                            <div class="tab-item">
                                <ul class="gray6">

                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>担保机构：</span>
                                        <%StringHelper.filterHTML(out, t6236.F06); %>
                                    </li>
                                    <li class="mb10"><div class=" pr pl200"><span class="display-ib w200 tr mr5 pa left0">担保机构介绍：</span>
                                    	<div class="pl10"><%if(!StringHelper.isEmpty(des))
                                    	    {
                                    	    StringHelper.filterHTML(out, des); 
                                    	    }else{%>
                                    	    &nbsp;
                                    	    <%} %>
                                    	    </div>
                                    	<p></p></div>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl">担保情况：</span>
                                        <textarea name="dbqk" cols="100" rows="8"  style="width:700px;height:500px;visibility:hidden;"
                                                  class="required min-length-20 max-length-60000">
													<%
                                                        if (t6237 != null) {
                                                           StringHelper.format(out, t6237.F02, fileStore);
                                                        }
                                                    %>
										</textarea>
                                    </li>

                                    <li class="mb10">
                                        <div class="pl200 ml5">
                                            <%
                                                if (type == 0) {
                                            %>
                                            <input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request, AddGuaranteeXq.class) %>?loanId=<%=loanId %>&userId=<%=userId %>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="返回"/>
                                            <%} else {%>
                                            <input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request, ViewGuarantee.class) %>?loanId=<%=loanId %>&userId=<%=userId %>&operationJK=<%=operationJK%>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="返回"/>
                                            <%} %>
                                        </div>
                                    </li>
                                </ul>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script type="text/javascript">

    //富文本框
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="dbqk"]', {
            uploadJson: '',
            allowFileManager: false,
            formatUploadUrl: false,
            items: ['fullscreen'],
            afterBlur: function () {
                this.sync();
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
</script>
</body>
</html>