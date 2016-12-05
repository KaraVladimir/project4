<%@include file="/WEB-INF/view/parts/page.jsp" %>
<%@ page import="web.config.Attrs" %>
<%@ page import="web.config.Pages" %>


<div id="header-top">
    <form id="language" method="get">
        <select class="lang-selector" name="language" onchange="submit()">
            <option value="en" ${language == 'en' ? 'selected' : ''}>EN</option>
            <option value="ru" ${language == 'ru' ? 'selected' : ''}>RU</option>
            <option value="uk" ${language == 'uk' ? 'selected' : ''}>UA</option>
        </select>
    </form>
</div>
<div id="header">
    <c:if test="${not empty sessionScope[Attrs.USER_ID]&&pageScope[Attrs.T]!='Login'}">
        <div class="header-button">
            <c:choose>
                <c:when test="${sessionScope[Attrs.IS_ADMIN]}">
                    <a href="/page/unblock" class="submit-button">
                        <fmt:message key="header.unblock.button"></fmt:message></a>
                </c:when>
                <c:when test="${!sessionScope[Attrs.IS_ADMIN]&&pageScope[Attrs.T]!='Login'}">
                    <a href="${[Pages.PAGE_USER_PAY]}" class="submit-button">
                        <fmt:message key="header.pay.button"></fmt:message></a>
                    <a href="${[Pages.PAGE_USER_REFILL]}" class="submit-button">
                        <fmt:message key="header.refill.button"></fmt:message></a>
                    <a href="${[Pages.PAGE_USER_BLOCK]}" class="submit-button">
                        <fmt:message key="header.block.button"></fmt:message></a>
                </c:when>
            </c:choose>
        </div>
    </c:if>
</div>