<%@ include file="/WEB-INF/views/common/include.jsp" %>

<script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery-1.4.2.min.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery-ui-1.8.12.custom.min.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/overlay.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/overlay.apple.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/toolbox.mousewheel.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/scrollable.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/scrollable.navigator.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/scrollable.autoscroll.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/tooltip.js" /> '></script>

    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery.tmpl.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery.livequery.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery.autocomplete.js" /> '></script>

	<script type="text/javascript" src='<c:url value="/scripts/esgf/esgf-core.js" /> '></script>
	<script type="text/javascript" src='<c:url value="/scripts/esgf/usermanagement.js" /> '></script>
	

    <link rel="stylesheet"
        href='<c:url value="/styles/lightgray/jquery-ui-1.8.10.custom.css" />'
        type="text/css" media="screen">	
    
    <link rel="stylesheet"
        href='<c:url value="/styles/usermanagement.css" />'
        type="text/css" media="screen">	
    
<!-- scratch space for any additional styles
	 will be placed in usermanagement.css if needed
 -->
<style>

</style>



<sec:authentication property="principal" var="principal"/>



<div style="margin-top:20px;margin-bottom:20px;min-height:500px;">
	<c:choose>
		<c:when test="${principal=='anonymousUser1'}">
    		<div> <c:out value="${principal}"/> IS NOT AUTHORIZED TO VIEW THIS PAGE</div>
  		</c:when>
  		<c:otherwise>
      		<c:choose>
      			<c:when test="${principal=='anonymousUser'}">
  					
  					
  						<!--  header info -->
  						<div class="span-24 last" style="margin-top:20px">
  							<h2 style="text-align:center">
							Manage User Accounts
							</h2>
  						</div>
						
						<!-- user information table -->
						<div class="prepend-2 span-20 append-2 last">
							<table id="table_id">  
	  
	    						<!-- Table header -->  
	  
						        <thead>  
						            <tr>  
						                <th>User Name</th>  
						                <th>Last Name</th> 
						                <th>First Name</th>  
						                <th>Email Address</th>  
						                <th>Status</th>  
						            </tr>  
						        </thead>  
	  
						   
						    <!-- Table body -->  
						  
						        <tbody>   
							        <c:set var="j" value="0"/>
							        <c:forEach var="user" items="${ManageUsers_user}">
										 <tr class="user_rows" 
										 	 id="${ManageUsers_user[j].userName}" 
										 	 style="cursor:pointer">  
							                <td>${ManageUsers_user[j].userName}</td>  
							                <td>${ManageUsers_user[j].lastName}</td> 
							                <td>${ManageUsers_user[j].firstName}</td>  
							                <td>${ManageUsers_user[j].emailAddress}</td>    
							                <td>${ManageUsers_user[j].status}</td>    
							            </tr> 
							            <c:set var="j" value="${j+1}"/>
										
									</c:forEach>
						        </tbody>  
						  
							</table> 
							
							<!-- the add user, edit user, and delete user buttons -->
							<div class="buttons" style="margin-bottom:40px;">
			    				<input class="adminbutton" id="add_user-button" type="submit" value="Add User" rel="#addUserForm" />
			    				<input class="adminbutton" id="edit_user-button" type="submit" value="Edit User" rel="#addUserForm" />
			    				<input class="adminbutton" id="delete_user-button" type="submit" value="Remove Selected User" />
							</div>
		    				
						</div>
						
						<!-- this section displays the selected user's information -->
						<div class="span-24 last">
							<div class="prepend-3 span-18 append-3 last">
								<div id="user_info"></div>
							</div> 
							
						</div>
						
						<!-- overlay form material here -->
						<div class="span-24 last">
						
				
							<!-- form overlay --> 
							<div class="simple_overlay" id="addUserForm"> 
								<form id="new_user_form" action="" method="post" >
								<h3 style="margin-top:10px;text-align:center;text-style:bold" id="form_title">New User Information</h3>
									<p>
										<label class="formLabels" for="userName">User Name:</label>
					      		  		<input type="text" class="text" id="form_userName" name="userName" value=""> 
					      		  		<br />
					      		  	
					      		  		<label class="formLabels" id="lastName" for="lastName" style="">Last Name:</label>
						      		 	<input type="text" class="text" name="lastName" id="form_lastName" value=""> 
						      		 	<br />
						      		 	
						          		<label class="formLabels" for="firstName">First Name:</label>
						      			<input type="text" class="text" id="form_firstName" name="firstName" value=""> 
						      			<br />	
						      											
										<label class="formLabels" for="emailAddress" style="">Email:</label>
						      		 	<input type="text" class="text" name="emailAddress" id="form_emailAddress" value=""> 
						      		 	<br />
						      		 	
						          		<label class="formLabels" for="status">Status:</label>
						      			<input type="text" class="text" id="form_status" name="status" value=""> 
						      			<br />	
								      			
								      	<label class="formLabels" for="organization">Organization:</label>
								      	<input type="text" class="text" id="organization" name="organization" value=""> 
								      	<br />
								      		  	
								    	<label class="formLabels" for="city" style="">City:</label>
								    	<input type="text" class="text" name="city" id="city" value=""> 
								    	<br />
								      		 	
								    	<label class="formLabels" for="country">Country:</label>
								    	<input type="text" class="text" id="country" name="country" value=""> 
								    	<br />	
								      											
										<label class="formLabels" for="openId">OpenId:</label>
								      	<input type="text" class="text" id="openId" name="openId" value=""> 
								      	<br />
								      		  	
										<label class="formLabels" for="DN">DN:</label>
								      	<input type="text" class="text" id="DN" name="DN" value=""> 
								      	<br />
								      	
								      	<input type="hidden" name="type" id="type" value="add"/>
									</p>
									<p>
						      			<input style="margin-left: 15px" class="adminbutton" type="submit" value="Submit">
						      			<!-- cancel button...not needed for now
						      			<input style="margin-left: 15px" class="adminbutton" type="submit" value="Cancel">
						      			 -->
					      			</p>
								</form>
								
							</div> 
	 
						
						</div><!-- end overlay section -->
						
  				</c:when>
  				<c:otherwise>
  					<div> <c:out value="${principal.username}"/> IS NOT AUTHORIZED TO VIEW THIS PAGE</div>
  				</c:otherwise>
      		</c:choose>
  		</c:otherwise>
	</c:choose>   
</div>

<!-- scratch space for any additional scripts
	 will be placed in usermanagement.js if needed
 -->
<script>
$(document).ready(function(){
	
});

</script>

<!--  toggle menu that might come in handy later
	<div id="toggleMenu"> 
			<h4>Account Summary</h4> 
							<div class="togglebox"> 
 								<div class="block"> 
 								Account Summary
 								</div> 
						</div>
						<h4>Group Roles</h4> 
							<div class="togglebox"> 
 								<div class="block"> 
 								Group Roles
 								</div> 
						</div>
	</div>
-->

