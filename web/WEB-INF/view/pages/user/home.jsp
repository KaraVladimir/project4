<%@include file="/WEB-INF/view/parts/page.jsp" %>
<html>
<%@include file="/WEB-INF/view/parts/head.jsp" %>
<c:set var="title" value="user/pay"/>
<body>
<%@include file="/WEB-INF/view/parts/header.jsp" %>
<div id="content">
    <div class="content-bigcontainer">
        <div class="container-header"><fmt:message key="your.accounts"/></div>
        <table>
            <tr>
                <td><fmt:message key="table.acc.id"/> </td>
                <td><fmt:message key="table.acc.number"/></td>
                <td><fmt:message key="table.acc.balance"/></td>
                <td><fmt:message key="table.acc.status"/></td>
            </tr>
            <c:forEach items="${requestScope[Attrs.ALL_ACCOUNTS]}" var="account" varStatus="counter">
                <tr>
                    <td><c:out value="${account.getID()}"/></td>
                    <td><c:out value="${account.getAccountNumber()}"/></td>
                    <td><c:out value="${account.getAccountBalance()}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${account.isBlocked()}">
                                <fmt:message key="table.acc.block"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="table.acc.unblock"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>

    <%@include file="/WEB-INF/view/parts/message_container.jsp" %>
</div>
</body>
</html>


