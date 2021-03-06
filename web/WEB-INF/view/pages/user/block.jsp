<%@include file="/WEB-INF/view/parts/page.jsp" %>
<html>
<%@include file="/WEB-INF/view/parts/head.jsp" %>
<c:set var="title" value="user/pay"/>
<body>
<%@include file="/WEB-INF/view/parts/header.jsp" %>
<div id="content">
    <div class="content-bigContainer">
        <c:if test="${not empty requestScope[Attrs.AVAILABLE_ACCOUNTS]}">
            <form id="block"
                  action="${Pages.PATH_BLOCK}" method="post">
                <input type="hidden" name="${Attrs.EXECUTE}" value="y"/>
                <c:set var="accounts" value="${requestScope[Attrs.AVAILABLE_ACCOUNTS]}" scope="request"/>
                <table>
                    <tr>
                        <td><fmt:message key="table.acc.id"/></td>
                        <td><fmt:message key="table.acc.number"/></td>
                        <td><fmt:message key="table.acc.balance"/></td>
                        <td><fmt:message key="table.acc.action"/></td>
                    </tr>
                    <c:forEach items="${requestScope[Attrs.AVAILABLE_ACCOUNTS]}" var="account" varStatus="counter">
                        <tr>
                            <td><c:out value="${account.getID()}"/></td>
                            <td><c:out value="${account.getAccountNumber()}"/></td>
                            <td><c:out value="${account.getAccountBalance()}"/></td>
                            <td>
                                <button name="${Attrs.ACCOUNT_ID}" value="${account.getID()}" class="table-button">
                                    <fmt:message key="table.acc.button.block"/></button>
                            </td>
                        </tr>
                    </c:forEach>
                </table>

            </form>
        </c:if>
    </div>
    <%@include file="/WEB-INF/view/parts/message_container.jsp" %>
</div>
</body>
</html>