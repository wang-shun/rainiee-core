<%@page import="com.dimeng.p2p.variables.defines.URLVariable"%>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@include file="/WEB-INF/include/style.jsp" %>
<div class="foot-container">
      <p class="foot-txt"><%configureProvider.format(out,SystemVariable.SITE_NAME); %>    <%configureProvider.format(out,SystemVariable.SITE_COPYRIGTH); %>      <%configureProvider.format(out,SystemVariable.SITE_FILING_NUM); %></p>
</div>