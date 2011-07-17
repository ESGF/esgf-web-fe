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
									 	 attLastName = "${ManageUsers_user[j].lastName}"
									 	 attFirstName = "${ManageUsers_user[j].firstName}"
									 	 attEmailAddress = "${ManageUsers_user[j].emailAddress}"
									 	 attStatus = "${ManageUsers_user[j].status}"
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
							
		    				<input class="adminbutton" id="add_user-button" type="submit" value="Add User" />
		    				<input class="adminbutton" id="edit_user-button" type="submit" value="Edit User" />
		    				<input class="adminbutton" id="delete_user-button" type="submit" value="Remove Selected User" />
		    				
						</div>
						<div class="span-24 last">
						
							<div id="user_info"></div>
							
							<div class="prepend-3 span-18 append-3 last">
								
							
								<form id="new_user_form" action="" method="post" style="display:none">
									 
							      	<fieldset>
							      		<legend class="formclass">New User Form</legend>
							
							          	<p>
							          		<label class="formLabels" for="lastName" style="">Last Name:</label>
							      		 	<input type="text" class="text" name="lastName" id="lastName" value=""> <br />
							      		 	
							          		<label class="formLabels" for="firstName">First Name:</label>
							      			<input type="text" class="text" id="firstName" name="firstName" value=""> <br />	
							      											
											<label class="formLabels" for="userName">User Name:</label>
							      		  	<input type="text" class="text" id="userName" name="userName" value=""> <br />
							      		  	
							      		  	<label class="formLabels" for="emailAddress" style="">Email:</label>
							      		 	<input type="text" class="text" name="emailAddress" id="emailAddress" value=""> <br />
							      		 	
							          		<label class="formLabels" for="status">Status:</label>
							      			<input type="text" class="text" id="status" name="status" value=""> <br />	
							      				
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
							      		  	<input type="hidden" name="type" value="add"/>
							      		  	
							      		  	
							      		</p>
							      		<p>
							      			<input type="submit" value="Submit">
							      		  	<input type="reset" value="Reset">
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
	
	/**
	* Will display the user's information
	*/
	$('tr.user_rows').click(function(){

		$('#new_user_form').hide();

		$('div.user_info_header').remove();
		$('div.user_info_content').remove();
		
		
		var userName = $(this).attr("id");

		
		/* from username we can get the rest of the info via an ajax cal */
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
		
		
		//var user_info_content = '<div class="user_info_content">LastName: ' + lastName + '</div>';
		//$('div#user_info').append(user_info_content);
		
		/*
		$('div.user_content').hide();
		//alert($(this).attr("attLastName") + ' ' + $(this).attr("attFirstName") + ' ' +  $(this).attr("attEmailAddress") + ' ' + $(this).attr("attStatus"));
		
		
		
		//$('div#user_info').append('<div id="' + userName + '" class="userContent" style="text-align:center">' + userName + '</div>');
		$('div#user_info').append('<div class="user_content" style="display:none">User info</div>');

		$('div.user_content').show();
		*/
	});
	
	function processUserContent(data) {
		alert('in process UserContent');
		//printObject(data);
		var userName = data.user.userName;
		
		alert(status);
		var user_info_header = getUserInfoHeader(userName);
		var user_info_content = getUserInfoContent(data);
		
		$('div#user_info').append(user_info_header);
		$('div#user_info').append(user_info_content);
		
		
	}
	
	function getUserInfoHeader(userName) {
		var user_info_header = '<div id="' + userName + '" class="user_info_header" style="text-align:center">' + userName + '</div>';
		return user_info_header;
	}
	
	function getUserInfoContent(data) {
		var lastName = data.user.lastName;
		var firstName = data.user.firstName;
		var emailAddress = data.user.emailAddress;
		var status = data.user.status;
		var userName = data.user.userName;
		var user_info_content = '<div class="user_info_content">' + firstName + ' ' + lastName + '</div>';
		return user_info_content;
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
	* Add User
	*/
	$('input#add_user-button').click(function(){
		
		/*
		$('div.header_name').remove();
		$('div.user_content').remove();
		
		
		$('div.user_content').hide();
		//$('div.user_content').remove();
		
		$('#new_user_form').hide();
		
		$('div#user_info').append('<div id="' + userName + '" class="header_name" style="text-align:center">' + 'Add User' + '</div>');
		//$('div#user_info').append('<div id="' + userName + '" class="userContent" style="text-align:center">' + userName + '</div>');
		$('#new_user_form').show();
		*/
	});
	
	/*
	* Add User
	*/
	$('input#edit_user-button').click(function(){
		alert('In edit user...');

		
		/*)
		$('div.user_content').remove();
		$('div.header_name').remove();
		//$('div.user_content').remove();
		$('#new_user_form').hide();
		
		$('div#user_info').append('<div id="' + userName + '" class="header_name" style="text-align:center">' + 'Adding new User' + '</div>');
		//$('div#user_info').append('<div id="' + userName + '" class="userContent" style="text-align:center">' + userName + '</div>');
		$('#new_user_form').show();
		*/
		
	});
	
	/*
	* Remove User
	*/
	$('input#delete_user-button').click(function(){

		$('div.user_content').hide();
		if($('div.header_name').html() != null) {
			var url = '';
			var input = '<input type="hidden" name="'+ 'type' +'" value="delete" />';
			//send request
			var formStr = '<form action="" method="post">' + input + '</form>';
			
			jQuery(formStr).appendTo('body').submit().remove();
		}

		$('div.header_name').remove();
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
