<%@include file="/WEB-INF/view/parts/page.jsp" %>
<html>
<%@include file="/WEB-INF/view/parts/head.jsp" %>
<c:set var="title" value="user/pay"/>
<body>
<%@include file="/WEB-INF/view/parts/header.jsp" %>
<div id="content">
    <div class="content-bigContainer">
        <div class="container-header"><fmt:message key="your.accounts"/></div>
        <c:if test="${not empty requestScope[Attrs.ALL_ACCOUNTS]}">
            <ctg:tagTable accounts="${requestScope[Attrs.ALL_ACCOUNTS]}">&nbsp;</ctg:tagTable>
        </c:if>
    </div>

    <%@include file="/WEB-INF/view/parts/message_container.jsp" %>
</div>
</body>
</html>


