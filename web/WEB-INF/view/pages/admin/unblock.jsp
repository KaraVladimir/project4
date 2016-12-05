
<%@include file="/WEB-INF/view/parts/page.jsp"%>
<html>
<%@include file="/WEB-INF/view/parts/head.jsp"%>
<c:set var="title" value="unblock"/>
<body>
<%@include file="/WEB-INF/view/parts/header.jsp"%>
    <form class="content-container" action="/page/findAccount" method="get">
        <div class="form-title">
            <fmt:message key="unblock.account.number"/>
        </div>
        <input class="form-field" type="text" name="number"/><br/>
        <div class="submit-container">
            <input class="submit-button" type="submit" value="<fmt:message key="unblock.find.button"/>"/>
        </div>
    </form>
    <c:if test="${not empty requestScope.get('account')}">
        <h1>OK</h1>
    </c:if>
</body>
</html>
