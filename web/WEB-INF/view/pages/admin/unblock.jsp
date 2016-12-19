<%@include file="/WEB-INF/view/parts/page.jsp" %>
<%--<%@taglib prefix="custom" uri="myTag1" %>--%>
<html>
<%@include file="/WEB-INF/view/parts/head.jsp" %>
<c:set var="title" value="admin/unblock"/>
<body>
<%@include file="/WEB-INF/view/parts/header.jsp" %>
<div id="content">
    <div class="content-bigContainer">

        <c:set var="account" value="${requestScope[Attrs.ACCOUNT]}" scope="request"/>
        <c:set var="message" value="${requestScope[Attrs.MSG]}" scope="request"/>
        <form id="unblock" action="${Pages.PATH_UNBLOCK}" method="post">
            <%--<c:set var="account" value="${requestScope[Attrs.ACCOUNT]}" scope="request"/>--%>
            <%--<c:set var="message" value="${requestScope[Attrs.MSG]}" scope="request"/>--%>

            <input type="hidden" name="${Attrs.EXECUTE}" value="y"/>

            <table>
                <tr>
                    <td><fmt:message key="table.acc.id"/></td>
                    <td><fmt:message key="table.acc.number"/></td>
                    <td><fmt:message key="table.acc.balance"/></td>
                    <td><fmt:message key="table.acc.action"/></td>
                </tr>
                <c:forEach items="${requestScope[Attrs.BLOCKED_ACCOUNTS]}" var="account">
                    <tr>
                        <td><c:out value="${account.getID()}"/></td>
                        <td><c:out value="${account.getAccountNumber()}"/></td>
                        <td><c:out value="${account.getAccountBalance()}"/></td>
                        <td>
                            <button name="${Attrs.ACCOUNT_ID}" value="${account.getID()}" class="table-button">
                                <fmt:message key="table.acc.button.unblock"/></button>
                    </tr>
                </c:forEach>
            </table>


        </form>
    </div>

    <%--<div class="content-bigcontainer">--%>
    <%--&lt;%&ndash;<custom:myT size="5"/>&ndash;%&gt;--%>
    <%--&lt;%&ndash;accounts="${requestScope[Attrs.BLOCKED_ACCOUNTS]}"&ndash;%&gt;--%>

    <%--</div>--%>
    <%--</c:if>--%>
    <%@include file="/WEB-INF/view/parts/message_container.jsp" %>
</div>
</body>
</html>
