<%@ include file="/WEB-INF/views/common/include.jsp" %>

<script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery-1.4.2.min.js" /> '></script>
    <!-- 
    <script type="text/javascript" src='<c:url value="/scripts/esgf/esgf-search-setting.js" />'> </script>
    -->
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

<style>
	table tbody tr:hover { background: #aaa; }
	
	
	#toggleMenu {
	    padding: 0px 0px 0px 0px; 
	    margin: 15px 0px 30px 0px;
	    clear: left;
	    float: left; 
	    width: 220px;
	    text-align: left;
	}
	
	#toggleMenu h4 {
	    background: #aaa;
	    border: 1px solid #EEEEEE;
	    -moz-border-radius: 4px;
	    -webkit-border-radius: 4px;
	    -khtml-border-radius: 4px;
	    border-radius: 4px;
	    margin: 4px 0px 0px 0px;
	    padding: 0px 0px 0px 23px;
	    line-height: 32px; 
	    font-weight: normal;
	    white-space: nowrap;
	    text-decoration: none; 
	    color: #696969;
	    cursor:pointer;
	    font-size: 13px;
	}
	.togglebox {
	    -moz-border-radius-bottomleft: 4px;
	    -moz-border-radius-bottomright: 4px;
	    -webkit-border-bottom-left-radius: 4px;
	    -webkit-border-bottom-right-radius: 4px;
	    -khtml-border-radius-bottomleft: 4px;
	    -khtml-border-radius-bottomright: 4px;
	    border-bottom-right-radius: 4px;
	    border-bottom-left-radius: 4px;
	    overflow: hidden;
	    clear: both;
	    padding: 0px 0px 0px 0px; 
	    margin: 0px 0px 0px 0px;
	    border-bottom: 1px solid #EEEEEE;
	    border-left: 1px solid #EEEEEE;
	    border-right: 1px solid #EEEEEE;
	    background: #FFFFFF;
	}
	
	
	.togglebox .block {
    padding: 0px 0px 0px 0px; 
    margin: 0px 0px 0px 0px;
}


.togglebox .block ul {
    list-style: none;
    padding: 5px 0px 10px 0px;
    margin: 0px 0px 0px 0px;
}

.togglebox .block ul li {
    font-size: 11px;
    line-height: 20px;
    height: 20px;   
    padding: 0px 0px 0px 45px;
    margin: 0px 0px 0px 0px;
}

.togglebox .block ul li a {
    color: #006996;
    text-decoration: none;
}

.togglebox .block ul li a:hover {
    color: #0088B5;
    text-decoration: underline;
}
	
.adminbutton {
	font-size: 13px;
	color: white;
	border: 1px solid #9c9c9c;
	background: #838943;
	cursor: pointer;
}
	
.formLabels {
	font-size: 16px;
	margin-right:10px;
}

</style>




<sec:authentication property="principal" var="principal"/>



<div style="margin-top:20px;margin-bottom:20px;min-height:500px;">
	<c:choose>
		<c:when test="${principal=='anonymousUser'}">
    		<div> <c:out value="${principal}"/> IS NOT AUTHORIZED TO VIEW THIS PAGE</div>
  		</c:when>
  		<c:otherwise>
      		<c:choose>
      			<c:when test="${principal.username=='https://pcmdi3.llnl.gov/esgcet/myopenid/jfharney'}">
  					<div style="margin-top:20px">
  					
  						<!--  header info -->
  						<div class="span-24 last">
  							<h2 style="text-align:center">
							Manage User Accounts
							</h2>
  						</div>
						
						<!-- user table -->
						<div class="prepend-2 span-20 append-2 last">
							<table id="table_id">  
	  
	    						<!-- Table header -->  
	  
						        <thead>  
						            <tr>  
						                <th>Last Name</th> 
						                  
						                <th>First Name</th>  
						                <th>User Name</th>  
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
						                <td>${ManageUsers_user[j].lastName}</td> 
						                <td>${ManageUsers_user[j].firstName}</td>  
						                <td>${ManageUsers_user[j].userName}</td>  
						                <td>${ManageUsers_user[j].emailAddress}</td>    
						                <td>${ManageUsers_user[j].status}</td>    
						            </tr> 
						            <c:set var="j" value="${j+1}"/>
									
								</c:forEach>
						           
						            
						           
						        </tbody>  
						  
							</table> 
							
							<div class="buttons" style="margin-bottom:40px;">
			    				<input class="adminbutton" id="add_user-button" type="submit" value="Add User" />
			    				<input class="adminbutton" id="edit_user-button" type="submit" value="Edit User" />
			    				<input class="adminbutton" id="delete_user-button" type="submit" value="Remove Selected User" />
							</div>
		    				
						</div>
						<div class="span-24 last">
						
							<div id="user_info"></div>
							
							<div class="prepend-3 span-18 append-3 last">
								
							
								<form id="new_user_form" action="" method="post" style="display:none">
									 
							      	<fieldset>
							      		<legend class="formclass">New User Form</legend>
							
							          	<p>
							          		<label class="formLabels" for="userName">User Name:</label>
							      		  	<input type="text" class="text" id="form_userName" name="userName" value=""> <br />
							      		  	
							      		  	<label class="formLabels" id="lastName" for="lastName" style="">Last Name:</label>
							      		 	<input type="text" class="text" name="lastName" id="form_lastName" value=""> <br />
							      		 	
							          		<label class="formLabels" for="firstName">First Name:</label>
							      			<input type="text" class="text" id="form_firstName" name="firstName" value=""> <br />	
							      											
											<label class="formLabels" for="emailAddress" style="">Email:</label>
							      		 	<input type="text" class="text" name="emailAddress" id="form_emailAddress" value=""> <br />
							      		 	
							          		<label class="formLabels" for="status">Status:</label>
							      			<input type="text" class="text" id="form_status" name="status" value=""> <br />	
							      				
							      			<!--  							
											<label class="formLabels" for="organization">Organization:</label>
							      		  	<input type="text" class="text" id="organization" name="organization" value=""> <br />
							      		  	
							      		  	<label class="formLabels" for="city" style="">City:</label>
							      		 	<input type="text" class="text" name="city" id="city" value=""> <br />
							      		 	
							          		<label class="formLabels" for="country">Country:</label>
							      			<input type="text" class="text" id="country" name="country" value=""> <br />	
							      											
											<label class="formLabels" for="openId">OpenId:</label>
							      		  	<input type="text" class="text" id="openId" name="openId" value=""> <br />
							      		  	
											<label class="formLabels" for="DN">DN:</label>
							      		  	<input type="text" class="text" id="DN" name="DN" value=""> <br />
							      		  	-->
							      		  	<input type="hidden" name="type" id="type" value="add"/>
							      		  	
							      		  	
							      		</p>
							      		<p>
							      			<input class="adminbutton" type="submit" value="Submit">
							      		</p>
							
							      	</fieldset>
							      	
							    </form>
							</div> 
							
						</div>
  				</c:when>
  				<c:otherwise>
  					<div> <c:out value="${principal.username}"/> IS NOT AUTHORIZED TO VIEW THIS PAGE</div>
  				</c:otherwise>
      		</c:choose>
  		</c:otherwise>
	</c:choose>      
</div>

<script>
$(document).ready(function(){
	
	//global variable...needs to be changed!!!
	var currentUserName = '';
	
	/**
	* Will display the user's information when the admin clicks on a row
	*/
	$('tr.user_rows').click(function(){

		//first we must hide/remove any information previously there
		$('#new_user_form').hide();
		$('#user_info').hide();

		$('div.user_info_header').remove();
		$('div.user_info_content').remove();
		
		//grab the username from the id of the row
		var userName = $(this).attr("id");

		currentUserName = userName;
		
		
		
		/* from username we can get the rest of the info via an ajax call to extractuserdataproxy */
		/* but MAKE SURE THAT IT IS NOT NULL!!! */
		if(userName != null && userName != "") {
			var query = { "id" : userName };
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
		} else {
			alert('Must have a valid user name to perform this operation');
		}
		
		
		
		
	});
	
	/*
	* Helper function for post ajax call processing
	*/
	function processUserContent(data) {
		//printObject(data);
		var userName = data.user.userName;
		
		var user_info_header = getUserInfoHeader(userName);
		var user_info_content = getUserInfoContent(data);
		
		$('div#user_info').append(user_info_header);
		$('div#user_info').append(user_info_content);
		
		$('div#user_info').show();
		
	}
	
	/*
	* Helper function for displaying the userName
	*/
	function getUserInfoHeader(userName) {
		var user_info_header = '<div id="' + userName + '" class="user_info_header" style="text-align:center">' + userName + '</div>';
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
	$('input#add_user-button').click(function(){
		
		//first we must hide/remove any information previously there
		$('#new_user_form').hide();
		$('#user_info').hide();

		$('div.user_info_header').remove();
		$('div.user_info_content').remove();
		
		clearFormValues();
		
		$('input#type').val('add');
		
		
		$('#new_user_form').show();
		
	});
	
	/*
	* Edit User - same as add user but we must add the current values to the form
	*/
	$('input#edit_user-button').click(function(){
		$('#new_user_form').hide();
		$('#user_info').hide();

		$('div.user_info_header').remove();
		$('div.user_info_content').remove();
		clearFormValues();
		
		$('input#type').val('edit');
		
		var query = { "id" : currentUserName };
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

		$('div.user_info_header').remove();
		$('div.user_info_content').remove();
		clearFormValues();
		
		if(currentUserName != '') {
			var deletedUserInput = '<input type="hidden" name="'+ 'user' +'" value="' + currentUserName + '" />';
			var input = '<input type="hidden" name="'+ 'type' +'" value="delete" />' + deletedUserInput;
			//send request
			var formStr = '<form action="" method="post">' + input + '</form>';
			
			jQuery(formStr).appendTo('body').submit().remove();
		}
		alert('done deletin');
		$('div.header_name').remove();
	});
	
	
	/**
	* Toggle boxes - not used (yet)
	*/
	$("h4").click(function(){
        $(this).next(".togglebox").slideToggle("fast");
        $(this).toggleClass('open');
        var index = ($(this).index() /2);

        return true;0
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
});


</script>

<!--  
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
