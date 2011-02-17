<%@ include file="/WEB-INF/views/common/include.jsp" %>

<ti:insertDefinition name="left-main-layout" >

	<ti:putAttribute name="left">
		<div class="panel">
			<div style="height: 200px"> 
				<span class="strong">Left Sidebar</span>
			</div>
		</div>
	</ti:putAttribute>

	<ti:putAttribute name="main">
		<h1>Authenticated User Page</h1>
		
		<div align="center">
		
			THIS PAGE CAN BE SEEN BY ALL AUTHENTICATED USERS	
			<sec:authorize access="hasRole('ROLE_USER')">
				<p/>THIS IS MORE CONTENT PROTECTED BY THE SPRING SECURITY TAG
			</sec:authorize>
		</div>
		
	</ti:putAttribute>
	
</ti:insertDefinition>