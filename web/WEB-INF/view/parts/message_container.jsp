<%@include file="/WEB-INF/view/parts/page.jsp" %>
<%@ page import="web.config.Attrs" %>
<%@ page import="web.config.Pages" %>
<%@ page import="web.config.Msgs" %>

<c:if test="${not empty requestScope[Attrs.MSG] }">
    <div class="message-container">
        <fmt:message key="${requestScope[Attrs.MSG]}"/>
    </div>
</c:if>
