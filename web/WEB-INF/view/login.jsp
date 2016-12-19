<%@include file="/WEB-INF/view/parts/page.jsp" %>
<html>
<c:set var="title" value="Login" scope="page"/>
<%@include file="/WEB-INF/view/parts/head.jsp" %>
<body>
<%@include file="/WEB-INF/view/parts/header.jsp" %>
<div id="content">
    <c:choose>
        <c:when test="${empty sessionScope[Attrs.USER_ID]}">
            <form class="content-container" action="${Pages.PATH_HOME}" method="post">
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
        </c:when>
        <c:otherwise>
            <div class="content-container">
                <fmt:message key="login.registered.msg"/> ${sessionScope[Attrs.USER_LOGIN]}
            </div>
        </c:otherwise>
    </c:choose>
    <%@include file="/WEB-INF/view/parts/message_container.jsp" %>
</div>

</body>
</html>
