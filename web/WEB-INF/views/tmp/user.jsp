
<%@ include file="/WEB-INF/views/common/include.jsp" %>

THIS PAGE CAN BE SEEN BY ALL AUTHENTICATED USERS

<sec:authorize access="hasRole('ROLE_USER')">
<p/>THIS IS MORE CONTENT PROTECTED BY THE SPRING SECURITY TAG
</sec:authorize>