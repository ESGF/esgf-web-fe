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
							<div class="prepend-3 span-18 append-3 last">
								<div id="group_info"></div>
							</div> 
						</div>
						
						<!-- overlay form material here -->
						<div class="span-24 last">
						
				
							<!-- form overlay --> 
							<div class="simple_overlay" id="addUserForm"> 
								<form id="new_user_form" action="" method="post" >
								<h3 style="margin-top:10px;text-align:center;text-style:bold;font-style:italic" id="form_title">New User Information</h3>
								
								
								<!-- 
								<table id="adduser_table_id" style="font-size:10px">  
									<tr>
										<td>
											<div style="font-size:12px;font-weight:bold;margin-left:15px;">User Name *</div> 
										</td>
										<td>
											<input type="text" class="text" id="form_userName" name="userName" value=""> 
										</td>
									</tr>
									<tr>
										<td>
											<div style="font-size:12px;font-weight:bold;margin-left:15px;">First Name *</div>
										</td>
										<td>
											<input type="text" class="text" name="lastName" id="form_firstName" value=""> 
										</td>
									</tr>
									<tr>
										<td>
											<div style="font-size:12px;font-weight:bold;margin-left:15px;">Last Name *</div>
										</td>
										<td>
											<input type="text" class="text" name="lastName" id="form_lastName" value="">  
										</td>
									</tr>
									<tr>
										<td>
											<div style="font-size:12px;font-weight:bold;margin-left:15px;">Email *</div>
										</td>
										<td>
											<input type="text" class="text" name="emailAddress" id="form_emailAddress" value="">
										</td>
									</tr>
									<tr>
										<td>
											<div style="font-size:12px;font-weight:bold;margin-left:15px;">Organization </div>
										</td>
										<td>
											<input type="text" class="text" id="organization" name="organization" value=""> 
										</td>
									</tr>
									<tr>
										<td>
											<div style="font-size:12px;font-weight:bold;margin-left:15px;">City</div>
										</td>
										<td>
											<input type="text" class="text" name="city" id="city" value=""> 
										</td>
									</tr>
									<tr>
										<td>
											<div style="font-size:12px;font-weight:bold;margin-left:15px;">Country</div>
										</td>
										<td>
								    		<input type="text" class="text" id="country" name="country" value=""> 
										</td>
									</tr>
									<tr>
										<td>
											
										</td>
									</tr>
							    </table>
							    <p>
								  	<input type="hidden" name="type" id="type" value="add"/>
						      		<input style="margin-left: 15px" class="adminbutton" type="submit" value="Submit">
						      	</p>
						      	<p>
						      		<div style="margin-left:15px;font-size:10px">* denotes required</div>
						      	</p>
						      	 -->
							     
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
	
	
	/**
	* Will display the user's information when the admin clicks on a row
	*/
	$('tr.user_rows').click(function(){

		//first we must hide/remove any information previously there
		$('#new_user_form').hide();
		$('#user_info').hide();
		$('#group_info').hide();

		$('fieldset#user_info').remove();
		$('fieldset#group_info').remove();
		
		//grab the username from the id of the row
		var userName = $(this).attr("id");

		ESGF.setting.currentUserName = userName;
		
		/* from username we can get the rest of the info via an ajax call to extractuserdataproxy */
		/* but MAKE SURE THAT IT IS NOT NULL!!! */
		if(userName != null && userName != "") {
			var query = { "id" : ESGF.setting.currentUserName, "type" : "edit" };
			var userinfo_url = '/esgf-web-fe/extractuserdataproxy';
			$.ajax({
	    		url: userinfo_url,
	    		type: "GET",
	    		data: query,
	    		dataType: 'json',
	    		success: function(data) {
	    			processUserContent(data);
	    		},
				error: function() {
					alert('error');
				}
			});
			
			
			query = { "id" : ESGF.setting.currentUserName, "type" : "edit" };
			var userinfo_url = '/esgf-web-fe/extractgroupdataproxy';
			$.ajax({
	    		url: userinfo_url,
	    		type: "GET",
	    		data: query,
	    		dataType: 'json',
	    		success: function(data) {
	    			processGroupContent(data);
	    		},
				error: function() {
					alert('error');
				}
			});
			
		} else {
			alert('Must have a valid user name to perform this operation');
		}
		
	});
	
	/*
	* Helper function for post ajax call processing for user content
	*/
	function processGroupContent(data) {
		//alert('process group content ' + data);
		
		var groups = data.groups.name[1];
		var userName = data.groups.userName;
		$('div#group_info').append('<fieldset id="group_info"><legend >Group Information for ' + userName + '</legend></fieldset>');
		
		var group_info_content = getGroupInfoContent(data);
		$('fieldset#group_info').append(group_info_content);
		
		$('div#group_info').show();
		
		
	}
	
	/*
	* Helper function for displaying the userContent
	*/
	function getGroupInfoContent(data) {
		
		var content = '';
		
		//this if statement takes care of a bug in the JSON java code...if the name array is of length one, it will automatically convert 
		//that element into a string array and print out one letter at a time
		//probably need a more sophisticated way of handling this problem
		if(data.groups.name instanceof Array) {
			for(var i=0;i<data.groups.name.length;i++) {
				content = content + '<div>' + data.groups.name[i] + '</div>';
			}
		} else {
			content = content + '<div>' + data.groups.name + '</div>';
		}
		
		var group_info_content = '<div class="group_info_content">' + content + '</div>';
		
		return group_info_content;
	}
	
	/*
	* Helper function for post ajax call processing for user content
	*/
	function processUserContent(data) {
		
		var userName = data.user.userName;
		
		var user_info_content = getUserInfoContent(data);
		
		
		
		$('div#user_info').append('<fieldset id="user_info"><legend >User Information for ' + userName + '</legend></fieldset>');
		
		$('fieldset#user_info').append(user_info_content);
		$('div#user_info').show();
		
		
	}
	
	/*
	* Helper function for displaying the userName
	* Deprecated
	*/
	function getUserInfoHeader(userName) {
		var user_info_header = '<div id="' + userName + '" class="user_info_header" style="text-align:center">User Information for ' + userName + '</div>';
		return user_info_header;
	}
	
	
	
	/*
	* Helper function for displaying the userContent
	*/
	function getUserInfoContent(data) {
		var lastName = data.user.lastName;
		var firstName = data.user.firstName;
		var emailAddress = data.user.emailAddress;
		var status = data.user.status;
		var userName = data.user.userName;
		var content = '<div>First Name: ' + firstName + '</div>' +
					  '<div>Last Name: ' + lastName + '</div>' + 
					  '<div>Email: ' + emailAddress + '</div>' + 
					  '<div>Status: ' + status + '</div>'
					  ;
		var user_info_content = '<div class="user_info_content">' + content + '</div>';
		return user_info_content;
	}
	
	
	/*
	* Add User
	*/
	$("input#add_user-button[rel]").overlay({
		mask: '#000',
		onLoad: function() {
			
			$('#new_user_form').show();
			
			$('#userName_input').show();
			clearFormValues();
			//overlay method
			$('h3#form_title').html('New User Information');
			
			//first we must hide/remove any information previously there
			//$('#new_user_form').hide();
			$('#user_info').hide();
			$('#group_info').hide();

			$('fieldset#user_info').remove();
			//$('div.user_info_header').remove();
			//$('div.user_info_content').remove();
		},
	
		onClose: function() {
			$('#new_user_form').hide();
			$('#userName_input').hide();
			clearFormValues();
		}
		
	});
	
	
	
	
	
	/*
	* Edit User
	*/
	$("input#edit_user-button[rel]").overlay({
		mask: '#000',
		onLoad: function() {
			$('#new_user_form').show();
			$('#userName_input').hide();
			$('h3#form_title').html('Edit User ' + ESGF.setting.currentUserName);
			$('#new_user_form').hide();
			//$('#user_info').hide();
			$('#userName_input').hide();
			
			//$('div.user_info_header').remove();
			//$('div.user_info_content').remove();
			//clearFormValues();
			
			$('input#type').val('edit');
			
			var query = { "id" : ESGF.setting.currentUserName, "type" : "edit" };
			var userinfo_url = '/esgf-web-fe/extractuserdataproxy';
			$.ajax({
	    		url: userinfo_url,
	    		type: "GET",
	    		data: query,
	    		dataType: 'json',
	    		success: function(data) {
	    			fillFormContentForEdit(data);
	    		},
				error: function() {
					alert('error');
				}
			});

			$('#new_user_form').show();
		},
	
		onClose: function() {
			$('#new_user_form').hide();

			$('fieldset#user_info').remove();
			$('fieldset#group_info').remove();
			//$('div.user_info_header').remove();
			//$('div.user_info_content').remove();
		}
		
	});
	
	
	
	
	
	/* Helper function for filling content for edtiing data */	
	function fillFormContentForEdit(data) {
		var lastName = data.user.lastName;
		var firstName = data.user.firstName;
		var emailAddress = data.user.emailAddress;
		var status = data.user.status;
		var userName = data.user.userName;
		
		//Note there may be more values later, this is for demo purposes
		$('input#form_firstName').val(firstName);
		$('input#form_lastName').val(lastName);
		$('input#form_userName').val(userName);
		$('input#form_emailAddress').val(emailAddress);
		$('input#form_status').val(status);
		
	}
	
	/*
	* Remove User
	*/
	$('input#delete_user-button').click(function(){
		$('#new_user_form').hide();
		$('#user_info').hide();
		$('#group_info').hide();

		
		if (confirm("Are you sure you want to delete user " + ESGF.setting.currentUserName + "?")) {
		 
			if(ESGF.setting.currentUserName != '') {
				var deletedUserInput = '<input type="hidden" name="'+ 'user' +'" value="' + ESGF.setting.currentUserName + '" />';
				var input = '<input type="hidden" name="'+ 'type' +'" value="delete" />' + deletedUserInput;
				//send request
				var formStr = '<form action="" method="post">' + input + '</form>';
				
				jQuery(formStr).appendTo('body').submit().remove();
			}

			$('div.user_info_header').remove();
			$('div.user_info_content').remove();
			$('div.group_info_header').remove();
			$('div.group_info_content').remove();
			$('div.header_name').remove();
			
		}
	});
	
	
	/**
	* Toggle boxes - not used (yet)
	*/
	$("h4").click(function(){
        $(this).next(".togglebox").slideToggle("fast");
        $(this).toggleClass('open');
        var index = ($(this).index() /2);

        return true;
    });
	

	/*   Utility functions   */
	function clearFormValues() {
		$('input#form_firstName').val("");
		$('input#form_lastName').val("");
		$('input#form_userName').val("");
		$('input#form_emailAddress').val("");
		$('input#form_status').val("");
	}
	
	
	/*
     * This function is primarily used for debugging
     */
    function printObject(object) {
        var output = '';
        for (var property in object) {
          output += property + ': ' + object[property]+'; ';
        }
        alert(output);
    }
	
    
    /*
	* Edit User - same as add user but we must add the current values to the form
	*/
	$('input#edit_user-button').click(function(){
	
	});
    
	/*
	* Add User - same as add user but we must add the current values to the form
	*/
	$('input#add_user-button').click(function(){
		
	});
    
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

