<!--底部开始-->
<div class="footer">
  <div class="wrap clearfix">
    <div class="info fl">
      <div class="footer_logo"><a href="<%configureProvider.format(out,URLVariable.INDEX);%>"><img src="<%=controller.getStaticPath(request)%>/simple/images/footer_logo.png"></a></div>
      <p class="mt10"><span class="f24 arial bold"><%configureProvider.format(out, SystemVariable.SITE_CUSTOMERSERVICE_TEL);%> </span>(<%configureProvider.format(out, SystemVariable.SITE_WORK_TIME);%>)</p>
      <p class="address">服务邮箱：<%configureProvider.format(out, SystemVariable.KFYX);%> <br>
        <%configureProvider.format(out, SystemVariable.COMPANY_ADDRESS);%></p>
    </div>
    <div class="line"></div>
    <div class="focus">
      <div class="til">关注我们 :</div>
      <div class="box"> <i class="weixin_icon"></i>
        <div class="code"> <i class="arrow"></i>
          <div class="border"> <img src="<%="".equals(fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))) ? controller.getStaticPath(request)+"/images/weixin.png" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))%>"><br />
            扫一扫关注微信 </div>
        </div>
      </div>
      <div class="box ml15"> <i class="app_icon"></i>
        <div class="code"> <i class="arrow"></i>
          <div class="border"> <img src="<%="".equals(fileStore.getURL(configureProvider.getProperty(SystemVariable.QTSJKHD))) ? controller.getStaticPath(request)+"/images/weixin.png" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTSJKHD))%>"><br />下载客户端APP </div>
        </div>
      </div>
    </div>
    <div class="line"></div>
    <div class="nav">
      <ul class="clearfix">
        <li><a href="<%configureProvider.format(out, URLVariable.GYWM_GSJJ);%>">关于我们</a></li>
        <li><a href="<%configureProvider.format(out, URLVariable.AQBZ_BXDB);%>">安全保障</a></li>
        <%{
          com.dimeng.p2p.modules.base.front.service.ArticleManage articleManagef = serviceSession.getService(com.dimeng.p2p.modules.base.front.service.ArticleManage.class);
        %>
        <li><a href="<%configureProvider.format(out, URLVariable.INDEX_TZGL);%>"><%=articleManagef.getCategoryNameByCode("XSZY")%></a></li>

        <li><a href="<%configureProvider.format(out, URLVariable.ZXDT_MTBD);%>"><%=articleManagef.getCategoryNameByCode("MTBD")%></a></li>

        <li><a href="<%configureProvider.format(out, URLVariable.GYWM_ZXNS);%>"><%=articleManagef.getCategoryNameByCode("ZXNS")%></a></li>
        <%}%>
        <li><a href="<%configureProvider.format(out, URLVariable.HELP_CENTER);%>">帮助中心</a></li>
      </ul>
    </div>
  </div>
  <div class="wrap copyright tc">
    <p class="mb10"><%configureProvider.format(out,SystemVariable.SITE_WORD);%> <%configureProvider.format(out,SystemVariable.SITE_COPYRIGTH); %> | 备案号：<%configureProvider.format(out,SystemVariable.SITE_FILING_NUM); %></p>
    <div class="tc">
      <%configureProvider.format(out, HTMLVariable.DBTB);%>
    </div>
  </div>
</div>
<!--底部结束-->
<!--第三方流量统计 start-->
<%configureProvider.format(out,SystemVariable.THIRD_PART_STATISTICS_SETTING);%>
<!--第三方流量统计 end -->