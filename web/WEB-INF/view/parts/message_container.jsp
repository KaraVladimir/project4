<%@include file="/WEB-INF/view/parts/page.jsp" %>
<%@ page import="payments.helper.Attrs" %>
<%@ page import="payments.helper.Pages" %>
<%@ page import="payments.helper.Msgs" %>

<c:if test="${not empty requestScope[Attrs.MSG] }">
    <div class="message-container">
        <fmt:message key="${requestScope[Attrs.MSG]}"/>
    </div>
</c:if>
