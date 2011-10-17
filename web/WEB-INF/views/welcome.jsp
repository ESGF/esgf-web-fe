<%@ include file="/WEB-INF/views/common/include.jsp" %>

<%--
Script information is in scripts/esgf/welcome.js
Style information is in styles/welcomeview/welcome.css
 --%>
<ti:insertDefinition name="home-layout" >
	
	<ti:putAttribute name="main">
		<!-- info -->
		<div class="span-24 info last" >
		  
			<%@ include file="/WEB-INF/views/welcome/quick_search.jsp" %>
			
			
			<%@ include file="/WEB-INF/views/welcome/about.jsp" %>
			
			
			<%@ include file="/WEB-INF/views/welcome/resources.jsp" %>
			
		</div>
		 
		<div class="span-24 browsersLink last" >
			<div class="span-2"><p></p></div>
			<div class="span-20" style="padding-top:5px;margin-bottom:5px;text-align:center">
					<a href="http://esgf.org/esgf-web-fe-site/browser-support.html" style="font-size:10px;color:#7d5f45" target="_blank">(supported browsers)</a>
			</div>	
			<div class="span-2 last"><p></p></div>
		</div>
		
	</ti:putAttribute>
	
</ti:insertDefinition>


