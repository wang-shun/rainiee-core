<%if("simple".equals(configureProvider.getProperty(SystemVariable.PAGE_TEMPLATE))){%>
<%@include file="/WEB-INF/include/index/simple/footer.jsp"%>
<%}else{%>
<div class="footer">
  <div class="w1002 clearfix">
    <div class="footer_logo mt20" onclick="window.location.href='<%configureProvider.format(out,URLVariable.INDEX);%>'"><img src="<%="".equals(fileStore.getURL(configureProvider.getProperty(SystemVariable.QTYWLOGO))) ? controller.getStaticPath(request)+ "/images/footer_logo.png" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTYWLOGO)) %>"/></div>
    <div class="footer_info">
      <p>联系我们<span class="f26 ml10 bold"><%configureProvider.format(out, SystemVariable.SITE_CUSTOMERSERVICE_TEL);%></span></p>
      <p class="f12">
                 全国服务热线（服务时间：<%configureProvider.format(out, SystemVariable.SITE_WORK_TIME);%>）<br />
                 服务邮箱：<%configureProvider.format(out, SystemVariable.KFYX);%><br />
       <%configureProvider.format(out, SystemVariable.COMPANY_ADDRESS);%>
      </p>
    </div>
    <ul class="footer_nav">
      <li><a href="<%configureProvider.format(out, URLVariable.GYWM_GSJJ);%>" rel="nofollow">关于我们</a></li>
      <li><a href="<%configureProvider.format(out, URLVariable.AQBZ_BXDB);%>" rel="nofollow">安全保障</a></li>
      <%{
        com.dimeng.p2p.modules.base.front.service.ArticleManage articleManagef = serviceSession.getService(com.dimeng.p2p.modules.base.front.service.ArticleManage.class);
      %>
      <li><a href="<%configureProvider.format(out, URLVariable.INDEX_TZGL);%>" rel="nofollow"><%=articleManagef.getCategoryNameByCode("XSZY")%></a></li>

      <li><a href="<%configureProvider.format(out, URLVariable.ZXDT_MTBD);%>" rel="nofollow"><%=articleManagef.getCategoryNameByCode("MTBD")%></a></li>

      <li><a href="<%configureProvider.format(out, URLVariable.GYWM_ZXNS);%>" rel="nofollow"><%=articleManagef.getCategoryNameByCode("ZXNS")%></a></li>
      <%}%>
      <li><a href="<%configureProvider.format(out, URLVariable.HELP_CENTER);%>" rel="nofollow">帮助中心</a></li>
    </ul>
  </div>
  <div class="w1002 copyright">
    <p><%configureProvider.format(out,SystemVariable.SITE_WORD);%> <%configureProvider.format(out,SystemVariable.SITE_COPYRIGTH); %> | 备案号：<%configureProvider.format(out,SystemVariable.SITE_FILING_NUM); %></p>
    <div class="icon">
      <%configureProvider.format(out, HTMLVariable.DBTB);%>
    </div>
  </div>
</div>
<!--第三方流量统计 start-->
<%configureProvider.format(out,SystemVariable.THIRD_PART_STATISTICS_SETTING);%>
<!--第三方流量统计 end -->
<%}%>