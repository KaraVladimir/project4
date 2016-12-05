<%@include file="/WEB-INF/view/parts/page.jsp"%>
<html>
<%@include file="/WEB-INF/view/parts/head.jsp"%>
<c:set var="title" value="Pay"/>
<body>
    <%@include file="/WEB-INF/view/parts/header.jsp"%>
    <h1>${not empty sessionScope[Attrs.USER_ID]}</h1>
    <h1>${sessionScope[Attrs.IS_ADMIN]}</h1>
</body>
</html>