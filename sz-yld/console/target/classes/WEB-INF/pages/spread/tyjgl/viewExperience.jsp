<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.modules.account.console.experience.service.entity.Experience" %>
<%@page import="com.dimeng.util.parser.DateTimeParser" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TGGL";
    CURRENT_SUB_CATEGORY = "TYJCZ";
    Experience experience = (Experience) request.getAttribute("experience");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>查看体验金充值信息
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <ul class="gray9 ">
                                <li class="mb10"><span class="display-ib w200 tr mr5">用户名：</span><span
                                        class="display-ib mr40 gray3"><%=experience.accountName%></span></li>
                                <li class="mb10"><span class="display-ib w200 tr mr5">金额：</span><span
                                        class="display-ib mr40 gray3"><%=Formater.formatAmount(experience.F03)%>元</span>
                                </li>
                                <li class="mb10"><span class="display-ib w200 tr mr5">类别：</span><span
                                        class="display-ib mr40 gray3"><%=experience.F08.getChineseName() %></span></li>
                                <li class="mb10"><span class="display-ib w200 tr mr5">过期时间：</span><span
                                        class="display-ib mr40 gray3"><%=DateParser.format(experience.F05)%></span></li>
                                <li class="mb10"><span class="display-ib w200 tr mr5">备注：</span><span
                                        class="display-ib mr40 gray3"><%=experience.F09%></span></li>
                                <li class="mb10"><span class="display-ib w200 tr mr5">操作人：</span>
		          		<span class="display-ib mr40 gray3">
		          			<%
                                if (!"".equals(experience.operName)) {
                            %>
                                        <%=experience.operName%>
                                        <%
                                        } else {
                                        %>
                                                平台
                                        <%
                                            }
                                        %>
		          		</span>
                                </li>
                                <li class="mb10"><span class="display-ib w200 tr mr5">操作时间：</span><span
                                        class="display-ib mr40 gray3"><%=DateTimeParser.format(experience.F10, "yyyy-MM-dd HH:mm:ss")%></span>
                                </li>

                            </ul>
                            <div class="tc pt20">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                       onclick="history.back(-1)" value="返回">
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>