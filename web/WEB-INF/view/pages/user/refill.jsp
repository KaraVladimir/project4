<%@include file="/WEB-INF/view/parts/page.jsp" %>
<html>
<%@include file="/WEB-INF/view/parts/head.jsp" %>
<c:set var="title" value="user/refill"/>
<body>
<%@include file="/WEB-INF/view/parts/header.jsp" %>
<div id="content">
    <form action="${Pages.PATH_REFILL}" method="post">
        <div class="content-bigContainer">
            <div class="container-menuRow">
                <div class="container-leftMenuInBigContainer">
                    <fmt:message key="your.accounts"/>
                </div>
                <div class="container-rightMenuInBigContainer">
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
            </div>
            <div class="container-menuRow">
                <div class="container-leftMenuInBigContainer">
                    <fmt:message key="payment.amount"/>
                </div>
                <div class="container-rightMenuInBigContainer">
                    <input name="${Attrs.ACC_AMOUNT}" class="form-field" pattern="[0-9]+([.,][0-9][0-9]?)?" required
                           oninvalid="setCustomValidity('<fmt:message key="${Msgs.AMOUNT_FORMAT_ERROR}"/>')"
                           onchange="try{setCustomValidity('')}catch(e){}"/>
                </div>
                <br/>
            </div>

            <input type="hidden" name="${Attrs.EXECUTE}" value="y"/>
            <input class="submit-button" type="submit" value="<fmt:message key="refill.button"/>"/>
        </div>
    </form>
    <%@include file="/WEB-INF/view/parts/message_container.jsp" %>
</div>

</body>
</html>