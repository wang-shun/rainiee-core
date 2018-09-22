<%@include file="/WEB-INF/include/jquery.jsp" %>
<%
	//是否托管项目
	boolean isTg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
	//托管前缀
	String escrowPrefix1 = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    String page_template_script = configureProvider.getProperty(SystemVariable.PAGE_TEMPLATE);
    if("simple".equals(page_template_script)){
%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/simple/js/common.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/simple/js/user.js"></script>
<%}else{%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/common.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/user.js"></script>
<%}%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jquery-select-list.js"></script>
<script type='text/javascript'>
{
    try {
        var a = window.opener||window.parent;
        while(a.parent!=a){
            a = a.parent;
        }
        if(a.location.host!='<%=configureProvider.format(systemDefine.getSiteDomainKey())%>'){
            a.location.href='<%=configureProvider.format(systemDefine.getSiteIndexKey())%>';
        }
    }catch(e){

    }
}
var isTg = '<%=isTg%>';
var escrowPrefix = '<%=escrowPrefix1%>';
var loginUrl = "<%=configureProvider.format(URLVariable.USER_INDEX)%>login.html";
</script>