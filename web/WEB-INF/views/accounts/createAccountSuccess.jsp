<%@ include file="/WEB-INF/views/common/include.jsp" %>

<ti:insertDefinition name="main-layout" >

	<ti:putAttribute name="extrastyle">
		<link rel="stylesheet" href='<c:url value="/styles/base.css" />' type="text/css">
	</ti:putAttribute>
	

	<ti:putAttribute name="main">
		
		<c:set var="openid_cookie" value="<%= org.esgf.security.OpenidCookieFilter.OPENID_COOKIE_NAME %>"/>

		<div style="margin:0 auto; width:600px; text-align:center;" class="panel">
		<b>SUCCESS.</b>
		<br/>Congratulations, your account is active immediately.
		<br/>You can now <a href='<c:url value="/login"/>'>log in</a> with your openid: 
		<span class="openidlink"> &nbsp; <c:out value="${cookie[openid_cookie].value}"/></span>.
		
		</div>
	</ti:putAttribute>
</ti:insertDefinition>