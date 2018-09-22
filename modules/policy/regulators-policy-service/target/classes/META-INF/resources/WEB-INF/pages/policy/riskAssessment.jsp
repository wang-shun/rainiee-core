<%@ page import="java.util.List" %>
<%@ page import="com.dimeng.p2p.repeater.policy.query.QuesQuery" %>
<%@ page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.user.servlets.policy.RiskAssessment" %>
<%@ page import="com.dimeng.p2p.S61.entities.T6147" %>
<%@ page import="com.dimeng.p2p.common.FormToken" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>
    </title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    List<QuesQuery> list = ObjectHelper.convert(request.getAttribute("list"),List.class);
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    String infoMessage = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(infoMessage)) {
        T6147 t6147 = ObjectHelper.convert(request.getAttribute("t6147"),T6147.class);
        if(t6147 != null){
%>
<div class="popup_bg"></div>
<div class="dialog">
    <div class="title"><a href="<%=configureProvider.format(URLVariable.USER_BASES)%>?userBasesFlag=1" class="out"></a>评估成功</div>
    <div class="content">
        <div class="tip_information">
            <div class="successful"></div>
            <div class="tips">
                <span class="f20 gray3">评估完成，您的得分为<%=t6147.F03%>分！</span><br/>
                <span class="f20 gray3">您的风险承受能力为：<span class="orange"><%=t6147.F04.getChineseName()%></span>
            </div>
        </div>
        <div class="tc mt20"><a href="<%=configureProvider.format(URLVariable.USER_BASES)%>?userBasesFlag=1" class="btn01">确定</a></div>
    </div>
</div>
<%}}%>
<div class="main_bg">
    <div class="main_mod risk_com risk_mod">
        <div class="ability_til clearfix">
            <div class="fl appetite_til">
                <h2>风险承受能力评估</h2>
                <p class="f14 gray9">我们将根据以下问题对您进行风险评估。为您提供更好的资产选择，请您认真作答，感谢您的配合!</p>
            </div>
            <div class="fr pen_pic"></div>
        </div>
        <form action="<%=controller.getURI(request,RiskAssessment.class)%>" method="post" class="form1" onsubmit="return onSubmit();">
            <%=FormToken.hidden(serviceSession.getSession()) %>
            <div class="survey">
                <ul>
            <%
            if(list != null && list.size() > 0 ){
                for(int i = 0 ; i < list.size() ; i++ ){
                    QuesQuery quesQuery = list.get(i);
            %>
                <li class="clearfix" id="q<%=quesQuery.F01%>">
                    <p class="risk_til clearfix">
                        <span class="risk_t1"><%=i+1%>.<%StringHelper.filterHTML(out,quesQuery.F02);%></span>
                        <span errortip class="emptyWarn" style="display: none;"><i class="survey_icon"></i>请选择</span>
                    </p>
                    <div class="choices clearfix verticalList">
                        <input class="ques" type="hidden" name="quesId_<%=quesQuery.F01%>" value="<%=quesQuery.F01%>"/>
                        <div class="form_radio">
                            <label>
                                <input name="answer_<%=quesQuery.F01%>" type="radio" class="required" value="A" /><span class="quesDesc" style="cursor: pointer">A.<%StringHelper.filterHTML(out,quesQuery.F03);%></span>
                            </label>
                        </div>
                        <div class="form_radio">
                            <label>
                                <input name="answer_<%=quesQuery.F01%>" type="radio" class="required" value="B" /><span class="quesDesc" style="cursor: pointer">B.<%StringHelper.filterHTML(out,quesQuery.F04);%></span>
                            </label>
                        </div>
                        <div class="form_radio">
                            <label>
                                <input name="answer_<%=quesQuery.F01%>" type="radio" class="required" value="C" /><span class="quesDesc" style="cursor: pointer">C.<%StringHelper.filterHTML(out,quesQuery.F05);%></span>
                            </label>
                        </div>
                        <div class="form_radio">
                            <label>
                                <input name="answer_<%=quesQuery.F01%>" type="radio" class="required" value="D" /><span class="quesDesc" style="cursor: pointer">D.<%StringHelper.filterHTML(out,quesQuery.F06);%></span>
                            </label>
                        </div>
                    </div>
                </li>
            <%}}%>

        </div>
        <div class="clearfix tc pt40">
            <%--<a class="btn06" href="#">提交</a>--%>
            <input type="submit" class="btn06 sumbitForme" fromname="form1" value="提交"/>
            <input type="button" class="btn06 btn_gray ml20"  value="取消" onclick="cannelBtn()"/>
        </div>
        </form>
    </div>

</div>
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo('<%=errorMessage%>', "error"));
</script>
<%} %>
<script type="text/javascript">
    $(function(){
        $(".quesDesc").click(function(){
            if($(this).prev("input[type='radio']").attr("checked")){
                $(this).prev("input[type='radio']").attr("checked","false");
            }else{
                $(this).prev("input[type='radio']").attr("checked","true");
            }
        });
    });
    var leaveflag=true;
    function onSubmit(){
        var flag = true;
        var index = 0;
        $(".survey li").each(function(){
            var quesId = $(this).find(".ques").val();
            var radio = $(this).find("input[name='answer_"+quesId+"']:checked");
            if(!radio.val()){
                $(this).find("span[errortip]").show();
                flag = false;
                if(index == 0){
                    index = quesId;
                }
                $(this).find("p").addClass("risk_bg");
            }else{
                $(this).find("span[errortip]").hide();
                $(this).find("p").removeClass("risk_bg");
            }
        });
        leaveflag = false;
        if(index > 0){
            var scroll_offset = $("#q"+index).offset();
            $("body,html").animate({
                scrollTop:scroll_offset.top  //让body的scrollTop等于pos的top，就实现了滚动
            },0);
        }
        return flag;
    }

   /* function leavePrompt(){
        if(leaveflag){
            return "您正在进行风险承受能力测试";
        }
    }*/

    function cannelBtn(){
        $("#info").html(showForwardInfo("您确定放弃未完成的评估吗？", "question", "<%configureProvider.format(out,URLVariable.USER_INDEX);%>"));
        $("div.popup_bg").show();
    }
</script>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>
