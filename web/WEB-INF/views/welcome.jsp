<%@ include file="/WEB-INF/views/common/include.jsp" %>


<ti:insertDefinition name="home-layout" >
	
	<ti:putAttribute name="main">
	
		<%@ include file="/WEB-INF/views/welcome/main.jsp" %>
		
		<%@ include file="/WEB-INF/views/welcome/bottom.jsp" %>
		
	</ti:putAttribute>
	
</ti:insertDefinition>


