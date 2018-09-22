<%@page import="com.dimeng.p2p.account.user.service.entity.LetterEntity" %>
<%@page import="com.dimeng.p2p.common.enums.LetterStatus" %>
<%@page import="com.dimeng.p2p.user.servlets.AbstractUserServlet" %>
<%@page import="com.dimeng.p2p.user.servlets.letter.Delete" %>
<%@page import="com.dimeng.p2p.user.servlets.letter.Index" %>
<%@page import="com.dimeng.p2p.user.servlets.letter.Update" %>
<%@page import="com.dimeng.p2p.user.servlets.letter.UpdateYd" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的<% configureProvider.format(out, SystemVariable.SITE_NAME); %>站内消息
    </title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    LetterManage letterManage = serviceSession.getService(LetterManage.class);
    int unread = letterManage.getUnReadCount();
    int count = letterManage.getCount();

    String status = request.getParameter("status");
    LetterStatus letterStatus = null;
    if (!StringHelper.isEmpty(status)) {
        letterStatus = LetterStatus.valueOf(status);
    }
    CURRENT_CATEGORY = "XXGL";
    CURRENT_SUB_CATEGORY = "ZNXX";
%>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <form action="<%=controller.getViewURI(request, Index.class)%>"
              method="post">
              <input type="hidden" name="status" value=""/>
            <div class="r_main">
                <div class="user_mod">
                    <span class="message_ico"></span>您共收到了<span id="read"><%=count%></span>条站内信，其中有<span id="unread"><%=unread%></span>条未读，请注意查看哦。如发现异常，请联系<%configureProvider.format(out, SystemVariable.SITE_NAME);%>客服:<%configureProvider.format(out, SystemVariable.SITE_CUSTOMERSERVICE_TEL);%>。</div>
                <div class="user_mod border_t15">
                    <div class="mb20" id="dataBody">
                        <input id="checkAll" type="checkbox" value="" class="mr5"/>
                        <label class="mr10" for="checkAll">全选</label>
                        <a href="javascript:delAll();" class="btn04 mr10">删除</a>
                        <a href="javascript:updateYD();" class="btn04 mr10">标记为已读</a>
                        <span class="fr read-til">
                          <a href="javascript:void();" onclick="searchForStatus(this,'');" class="cur searchForStatus">全部</a><i class="r-line"></i>
                          <a href="javascript:void();" onclick="searchForStatus(this,'<%=LetterStatus.YD.toString()%>');" class="searchForStatus">已读</a><i class="r-line"></i>
                          <a href="javascript:void();" onclick="searchForStatus(this,'<%=LetterStatus.WD.toString()%>');" class="searchForStatus">未读</a>
                        </span>
                        <%-- <select name="status" id="status" class="select6" style="width: 90px;">
                            <option value=""
                                    <%=StringHelper.isEmpty(status) ? "selected=\"selected\"" : ""%>>全部消息
                            </option>
                            <option value="<%=LetterStatus.YD.toString()%>"
                                    <%=LetterStatus.YD.toString().equals(status) ? "selected=\"selected\"" : ""%>>已读消息
                            </option>
                            <option value="<%=LetterStatus.WD.toString()%>"
                                    <%=LetterStatus.WD.toString().equals(status) ? "selected=\"selected\"" : ""%>>未读消息
                            </option>
                        </select> --%>
                    </div>
                    <div class='message_con'>
                    </div>
                    <div id="pageContent">
                    </div>
                </div>
            </div>
        </form>
        <div class="clear"></div>
    </div>
</div>
<div class="dialog" style="display:none;">
    <div class="title"><a href="javascript:void(0);" class="out"></a>提示
    </div>
    <div class="content">
        <div class="tip_information">
            <div class="doubt" id="icon_type"></div>
            <div class="tips " id="inf0Div">
                <span class="f20 gray33">确定要删除选中的站内信吗？</span>
            </div>
        </div>
        <div class="tc mt20" id="btnHtml">
            <a href="javascript:void(0)" id="ok" class="btn01" onclick="delMsg();return false">确定</a>
            <a href="javascript:void(0)" id="cancel" class="btn01 btn_gray ml20">取消</a>
        </div>
    </div>
</div>
<div class="popup_bg" style="display:none;"></div>
<script type="text/javascript">
    var currentPage = 1;
    var pageSize = 10;
    var pageCount = 0;
    var siteName = "<%=configureProvider.format(SystemVariable.SITE_NAME)%>";
    var url_update = "<%=controller.getURI(request, Update.class)%>";
    var url_del = "<%=controller.getURI(request, Delete.class)%>";
    var url_self = "<%=controller.getURI(request, Index.class)%>";
    var url_updateYd = "<%=controller.getURI(request, UpdateYd.class)%>";
    function toAjaxPage(){
    	initData();
    }
</script>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/zhankai.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/letter.js"></script>
</body>
</html>