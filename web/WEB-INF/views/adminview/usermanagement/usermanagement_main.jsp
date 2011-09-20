

<%@ include file="/WEB-INF/views/common/include.jsp" %>



<div class="admin_page_main">

	<!--  header info -->
	<div class="span-24 last" style="margin-top:20px">
		<h2 class="admin_title">
			Manage User Accounts
		</h2>
	</div>
						
	<!-- user information table -->
	<%@ include file="user_rows.jsp" %>			
	
						
	<!-- this section displays the selected user's information -->
	<%@ include file="overlay_form.jsp" %>			
	
	
	<!--  overlay form used for editing users -->	
	<%@ include file="user_display.jsp" %>				
		
					
</div>

