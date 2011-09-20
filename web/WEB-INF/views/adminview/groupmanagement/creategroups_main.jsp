
<%@ include file="/WEB-INF/views/common/include.jsp" %>


<!--  header info -->
<div class="span-24 last groupmanagement_page_main">
	<h2 class="groupmanagement_title">
	Manage Groups
	</h2>
	
	<!-- this section displays all the groups in a table -->
	<%@ include file="group_rows.jsp" %>	
	
	
	<!-- this section displays the selected group's information -->
	<%@ include file="group_display.jsp" %>	
	
	<!-- overlay form material here -->
	<div class="span-24 last">
		
		<!-- overlay form for adding users to a group --> 
		<%@ include file="add_users_to_group_form.jsp" %>	
		
		<!-- overlay form for adding/editing groups --> 
		<%@ include file="add_group_form.jsp" %>	
		
	</div>
</div>
