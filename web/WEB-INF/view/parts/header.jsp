<%@include file="/WEB-INF/view/parts/page.jsp" %>
<%@ page import="web.config.Attrs" %>
<%@ page import="web.config.Pages" %>
<%@ page import="web.config.Msgs" %>


<div id="header-top">
    <%--<form id="language" method="get" action="/page/changeLanguage">--%>
        <%--<select class="lang-selector" name="language" onchange="submit()">--%>
            <%--<option value="en" ${language eq 'en' ? 'selected' : ''}>EN</option>--%>
            <%--<option value="ru" ${language eq 'ru' ? 'selected' : ''}>RU</option>--%>
            <%--<option value="uk" ${language eq 'uk' ? 'selected' : ''}>UA</option>--%>
        <%--</select>--%>
        <c:if test="${not empty sessionScope[Attrs.USER_ID]}">

            <a href="${Pages.PATH_LOGOUT}" >
                <fmt:message key="header.logout"></fmt:message></a>
        </c:if>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <span style="float: right">


            <a href="?language=en" onclick="submit()">en</a>
            |
            <a href="?language=uk" onclick="submit()">ua</a>
            |
            <a href="?language=ru" onclick="submit()">ru</a>
        </span>
    <%--</form>--%>
</div>
<div id="header">
    <c:if test="${not empty sessionScope[Attrs.USER_ID]&&pageScope[Attrs.T]!='Login'}">
        <div class="header-button">
            <c:choose>
                <c:when test="${sessionScope[Attrs.IS_ADMIN]&&pageScope[Attrs.T]!='Login'}">
                    <a href="${Pages.PATH_UNBLOCK}" class="submit-button">
                        <fmt:message key="header.unblock.button"></fmt:message></a>
                </c:when>
                <c:when test="${!sessionScope[Attrs.IS_ADMIN]&&pageScope[Attrs.T]!='Login'}">
                    <a href="${Pages.PATH_PAY}" class="submit-button">
                        <fmt:message key="header.pay.button"></fmt:message></a>
                    <a href="${Pages.PATH_REFILL}" class="submit-button">
                        <fmt:message key="header.refill.button"></fmt:message></a>
                    <a href="${Pages.PATH_BLOCK}" class="submit-button">
                        <fmt:message key="header.block.button"></fmt:message></a>
                </c:when>
            </c:choose>
        </div>
    </c:if>
</div>