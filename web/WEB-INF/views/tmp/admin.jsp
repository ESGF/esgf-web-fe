<%@ include file="/WEB-INF/views/common/include.jsp" %>

<ti:insertDefinition name="left-main-right-layout" >

	<ti:putAttribute name="left">
		<div class="panel">
			<div style="height: 200px"> 
				<span class="strong">Left Sidebar</span>
			</div>
		</div>
	</ti:putAttribute>
	
	<ti:putAttribute name="main">
		<h1>Administrator Page</h1>
		<div align="center">This page is restricted to portal administrators.</div>
	</ti:putAttribute>
	
	<ti:putAttribute name="right">
		<div class="panel">
			<div style="height: 200px"> 
				<span class="strong">Right Sidebar</span>
			</div>
		</div>
	</ti:putAttribute>
	
</ti:insertDefinition>