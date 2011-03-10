<%@ include file="/WEB-INF/views/common/include.jsp" %>

<ti:insertDefinition name="main-layout" >

	 <ti:putAttribute name="extrastyle">
        <link rel="stylesheet" href='<c:url value="/styles/security.css" />' type="text/css">
    </ti:putAttribute>
    
    <ti:putAttribute name="main">
    	
		<h1>Disable Account Feedback</h1> 
		<div class="panel">
			<c:out value="${user.message}"/>
		</div>
    
    </ti:putAttribute>

</ti:insertDefinition>