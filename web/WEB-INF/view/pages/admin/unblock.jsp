<%@include file="/WEB-INF/view/parts/page.jsp" %>
<html>
<%@include file="/WEB-INF/view/parts/head.jsp" %>
<c:set var="title" value="admin/unblock"/>
<body>
<%@include file="/WEB-INF/view/parts/header.jsp" %>
<div id="content">
    <%--<form class="content-container" action="${Pages.PATH_FIND_ACCOUNT}" method="get">--%>
    <%--&lt;%&ndash;<input type="hidden" name="ref" value="${pageScope[Attrs.T]}">&ndash;%&gt;--%>
    <%--<div class="form-title">--%>
    <%--<fmt:message key="unblock.account.number"/>--%>
    <%--</div>--%>
    <%--<input class="form-field" type="text" name="${Attrs.ACC_NUMBER}" pattern="\d{4,32}" required--%>
    <%--oninvalid="setCustomValidity('<fmt:message key="${Msgs.ACCOUNT_FORMAT_ERROR}"/>')"--%>
    <%--onchange="try{setCustomValidity('')}catch(e){}"/>--%>
    <%--<br/>--%>
    <%--<div class="submit-container">--%>
    <%--<input class="submit-button" type="submit" value="<fmt:message key="unblock.find.button"/>"/>--%>
    <%--</div>--%>
    <%--</form>--%>
    <%--<c:if test="${not empty requestScope[Attrs.BLOCKED_ACCOUNTS]}">--%>
        <div class="content-bigcontainer">

            <form id="unblock" action="${Pages.PATH_UNBLOCK}" method="post">
                <c:set var="account" value="${requestScope[Attrs.ACCOUNT]}" scope="request"/>
                <c:set var="message" value="${requestScope[Attrs.MSG]}" scope="request"/>

                <input type="hidden" name="${Attrs.EXECUTE}" value="y"/>
                <table>
                    <tr>
                        <td><fmt:message key="table.acc.id"/></td>
                        <td><fmt:message key="table.acc.number"/></td>
                        <td><fmt:message key="table.acc.balance"/></td>
                        <td><fmt:message key="table.acc.status"/></td>
                    </tr>
                    <c:forEach items="${requestScope[Attrs.BLOCKED_ACCOUNTS]}" var="account">
                        <tr>
                            <td><c:out value="${account.getID()}"/></td>
                            <td><c:out value="${account.getAccountNumber()}"/></td>
                            <td><c:out value="${account.getAccountBalance()}"/></td>
                            <td>
                                <button name="${Attrs.ACCOUNT_ID}" value="${account.getID()}">
                                    <img src="${!account.isBlocked()?Pages.PATH_BTN_FAIL:Pages.PATH_BTN_OK}"
                                         style="height: 20px;width: 20px"></button>
                        </tr>
                    </c:forEach>
                </table>

            </form>
        </div>
    <%--</c:if>--%>
    <%@include file="/WEB-INF/view/parts/message_container.jsp" %>
</div>
</body>
</html>
