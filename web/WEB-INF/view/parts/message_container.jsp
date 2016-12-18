<%@include file="/WEB-INF/view/parts/page.jsp" %>
<%@ page import="payments.config.Attrs" %>
<%@ page import="payments.config.Pages" %>
<%@ page import="payments.config.Msgs" %>

<c:if test="${not empty requestScope[Attrs.MSG] }">
    <div class="message-container">
        <fmt:message key="${requestScope[Attrs.MSG]}"/>
    </div>
</c:if>
