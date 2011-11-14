<%@ include file="/WEB-INF/views/common/include.jsp" %>

<%--
Script information is in scripts/esgf/welcome.js
Style information is in styles/welcomeview/welcome.css
 --%>
<ti:insertDefinition name="home-layout" >
	
	<ti:putAttribute name="main">
	
		<%@ include file="/WEB-INF/views/welcome/main.jsp" %>
		
		<%@ include file="/WEB-INF/views/welcome/bottom.jsp" %>
		
	</ti:putAttribute>
	
</ti:insertDefinition>


