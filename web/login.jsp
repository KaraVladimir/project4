<%@include file="/WEB-INF/view/parts/page.jsp" %>
<html>
<c:set var="title" value="Login" scope="page"/>
<%@include file="/WEB-INF/view/parts/head.jsp" %>
<body>
<%@include file="/WEB-INF/view/parts/header.jsp" %>
<div id="content">
    <form class="content-container" action="/page/login" method="post">
        <%--<input type="hidden" name="command" value="login">--%>
        <div class="form-title">
            <fmt:message key="login.form"/>
        </div>
        <input class="form-field" type="text" name="login"/><br/>
        <div class="form-title">
            <fmt:message key="password.form"/>
        </div>
        <input class="form-field" type="password" name="password"/><br/>
        <div class="submit-container">
            <input class="submit-button" type="submit" value="<fmt:message key="login.button"/>"/>
        </div>
    </form>
</div>

<%--TODO footer in all jsp--%>
</body>
</html>
