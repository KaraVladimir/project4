<%@include file="/WEB-INF/view/parts/page.jsp" %>
<html>
<%@include file="/WEB-INF/view/parts/head.jsp" %>
<c:set var="title" value="user/pay"/>
<body>
<%@include file="/WEB-INF/view/parts/header.jsp" %>
<div id="content">
    <form action="${Pages.COM_PAY}" method="post">
        <input type="hidden" name="${Attrs.REF}" value="${Pages.PATH_PAY}">
        <div class="content-bigcontainer">
            <div>
                <fmt:message key="your.accounts"/>
                <select class="form-field" name="${Attrs.ACCOUNT_ID}" required>
                    <c:forEach items="${requestScope[Attrs.AVAILABLE_ACCOUNTS]}" var="item">
                        <option value="${item.getID()}">
                            <fmt:message key="your.account.number"/>:${item.getAccountNumber()}
                            <fmt:message key="your.account.balance"/>:${item.getAccountBalance()}
                            <br>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <fmt:message key="unblock.account.number"/>
            <input class="form-field" type="text" name="${Attrs.ACC_NUMBER}" pattern="\d{4,32}" required
                   oninvalid="setCustomValidity('<fmt:message key="${Msgs.ACCOUNT_FORMAT_ERROR}"/>')"
                   onchange="try{setCustomValidity('')}catch(e){}"/><br/>
            <fmt:message key="payment.amount"/>
            <input name="${Attrs.ACC_AMOUNT}" class="form-field" pattern="[0-9]+([.,][0-9][0-9]?)?" required
                   oninvalid="setCustomValidity('<fmt:message key="${Msgs.ACCOUNT_FORMAT_ERROR}"/>')"
                   onchange="try{setCustomValidity('')}catch(e){}"/>
            <br/>
            <input class="submit-button" type="submit" value="<fmt:message key="pay.button"/>"/>
        </div>
        <c:if test="${not empty requestScope[Attrs.MSG] }">
            <div class="message-container">
                <fmt:message key="${message}"/>
            </div>
        </c:if>
    </form>
</div>

</body>
</html>