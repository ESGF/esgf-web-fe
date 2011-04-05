<%@ include file="/WEB-INF/views/common/include.jsp" %>

<ti:insertDefinition name="main-layout" >

    <ti:putAttribute name="extrastyle">
        <link rel="stylesheet" href='<c:url value="/styles/security.css" />' type="text/css">
    </ti:putAttribute>


    <ti:putAttribute name="main">

        <c:set var="openid" value="<%= org.esgf.security.OpenidCookieFilter.PARAMETER_OPENID %>"/>
		
        <div style="width:600px;" class="panel">
	        <b>SUCCESS.</b>
	        <br/>Congratulations, your account is active immediately.
	        <br/>You can now <a href='<c:url value="/login"/>'>log in</a> with your openid:
	        <span class="openidlink"> &nbsp; <c:out value="${param[openid]}"/></span>.
        </div>
    </ti:putAttribute>
</ti:insertDefinition>