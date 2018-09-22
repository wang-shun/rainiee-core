<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%CURRENT_CATEGORY = "JCXXGL";
boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <!--用户详细信息-->
                <div class="p20">
                    <div class="user-container border p20 pl30 pr30 gray6">
                        <ul class="lh24">
                            <li class="pb10 pt20 border-b-s">
                                <h3 class="f18">业务设置</h3>
                                <p class="">对平台部分业务信息进行设置，包含信用认证项管理、标产品管理、标类型管理、标附件类型管理、抵押物属性管理、不良资产处理方案设置。</p>
                            </li>
                            <li class="pb10 pt20 border-b-s">
                                <h3 class="f18">运营设置</h3>
                                <p class="">对平台部分运营信息进行设置，包含交易类型管理、区域管理、银行设置、协议模板设置、平台图标管理、平台常量管理、账单提醒设置、定时任务管理、第三方统计设置、充值说明管理、提现说明管理、筛选条件设置
                                    <% if(Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS))){%>
                                    、风险评估设置、风险评估问题设置
                                    <%}%>。
                                </p>
                            </li>
                            <%
                              if(is_mall){
                            %>
                            <li class="pb10 pt20 border-b-s">
                                <h3 class="f18">商城运营设置</h3>
                                <p class="">对积分商城运营信息进行设置，包含积分设置、积分清零设置、积分规则说明、商品类别设置、商城筛选条件设置。</p>
                            </li>
                            <%}%>
                        </ul>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
</body>
</html>