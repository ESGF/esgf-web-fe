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
        href='<c:url value="/styles/groupmanagement.css" />'
        type="text/css" media="screen">	



<!--  header info -->
<div class="span-24 last" style="margin-top:20px;min-height:500px;">
	<h2 style="text-align:center">
	Manage Groups
	</h2>
	
	<div class="prepend-2 span-20 append-2 last">
		<table id="table_id">  

	        <thead>  
	            <tr>  
	                <th>Group Name</th>  
	                <th>Group Description</th>  
	            </tr>  
	        </thead>  
	        <tbody> 
		        <c:set var="j" value="0"/>
		        <c:forEach var="group" items="${CreateGroups_group}">
		        	<tr class="group_rows" id="${CreateGroups_group[j].name}" >
		        		<td>${CreateGroups_group[j].name}</td>
		        		<td>${CreateGroups_group[j].description}</td>
		        	</tr>
		        	<%--<tr>
		        		  <td colspan="2"> 
		        			<!--  this section displays users in group -->
		        			<div class="span-24 last">
		        				<div class="prepend-3 span-18 append-3 last">
		        					<div id="group_user_info"></div>
		        				</div>
		        			</div>
		        		
		        	</tr>--%>
		            <c:set var="j" value="${j+1}"/>
		       	</c:forEach>
		       	
		       
		    </tbody>
							       	
			
			
			
  	    </table>
  
	    <!-- the add user, edit user, and delete user buttons -->
	    <div class="buttons" style="margin-bottom:40px;">
			<input class="adminbutton" id="add_group-button" type="submit" value="Add Group" rel="#addGroupForm" />
			<input class="adminbutton" id="edit_group-button" type="submit" value="Edit Group" rel="#addGroupForm" />
			<!--  <input class="adminbutton" id="add_user_to_group-button" type="submit" value="Add User To Selected Group" rel="#addUserToGroupForm" /> -->
			<input class="adminbutton" id="delete_group-button" type="submit" value="Remove Selected Group" />
	    	<input class="adminbutton" id="edit_users_in_group-button" type="submit" value="Edit User(s) In Selected Group" rel="#addUsersToGroups"/>
	    </div>
	  
	</div>
	
	
	
	<!-- this section displays the selected group's information -->
	<div class="span-24 last"> 
		<div class="prepend-3 span-18 append-3 last">
			<div id="group_info"></div>
		</div> 
	</div> 
	
	<!-- overlay form material here -->
	<div class="span-24 last">
		
		<div class="simple_overlay" id="addUsersToGroups">
			<form id="add_group_form" action="" method="post" >
				<h3 style="margin-top:10px;text-align:center;text-style:bold;font-style:italic" id="form_title1">Add Users To Group</h3>
				<div id="potential_users"></div>
				<p>
					<input type="hidden" name="type" id="type" value="editUsersInGroup"/>
					<input type="hidden" name="groupName" id="groupName" value=""/>
				  	<input style="margin-left: 15px" class="adminbutton" type="submit" value="Submit">
		      	</p>
			</form>
		</div>
		
		<!-- form overlay --> 
		
		
		<div class="simple_overlay" id="addGroupForm"> 
			<form id="new_group_form" action="" method="post" >
				<h3 style="margin-top:10px;text-align:center;text-style:bold;font-style:italic" id="form_title">New User Information</h3>
				<table id="addgroup_table_id" style="font-size:10px">  
					<tr>
						<td>
							<div style="font-size:12px;font-weight:bold;margin-left:15px;">Group Name</div> 
						</td>
						<td>
							<input type="text" class="text" id="groupName" name="groupName" value=""> 
						</td>
					</tr>
					<tr>
						<td>
							<div style="font-size:12px;font-weight:bold;margin-left:15px;">Group Description</div> 
						</td>
						<td>
							<textarea type="text" id="groupDescription" name="groupDescription" value="Type Description Here" style="width:300px;height:200px" >
    						</textarea>
						</td>
					</tr>
				</table>					
				<p>
				  	<input type="hidden" name="type" id="type" value="add"/>
		      		<input style="margin-left: 15px" class="adminbutton" type="submit" value="Submit">
		      	</p>
			</form>
		</div>
	
	</div>
	
	
	
	
</div>

<script type="text/javascript">

$(document).ready(function(){
	
	/*
    var prevrow = null;
    $("#table_id tr:odd").addClass("odd");
    $("#table_id tr:not(.odd)").hide();
    $("#table_id tr:first-child").show(); // header

    $("#table_id tr.odd").click(function() {
        if (prevrow) prevrow.hide();
    	$(this).next("tr").toggle();
    	prevrow = $(this).next("tr");
    });
    */
	/**
	* Will display the group's information when the admin clicks on a row
	*/
	$('tr.group_rows').click(function(){

		var groupName = $(this).attr("id");
		

		$('tr#' + ESGF.setting.currentGroupName).css('background','#f9f6f1');
		$(this).css('background','#FAECC8');
		
		//first we must hide/remove any information previously there
		$('#new_group_form').hide();
		$('#group_info').hide();
		$('fieldset#group_info').remove();
		
		//set the current group name variable to the clicked groupname
		ESGF.setting.currentGroupName = groupName;
		
		//if this value is not empty or null
		//then make the ajax call to the group 
		if(ESGF.setting.currentGroupName != null && ESGF.setting.currentGroupName != "") {
			
			query = { "groupName" : ESGF.setting.currentGroupName, "type" : "getGroupInfo" };
			var groupinfo_url = '/esgf-web-fe/extractgroupdataproxy';
			$.ajax({
	    		url: groupinfo_url,
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
			
		}
		
		
	});	
	
	
	function processGroupContent(data) {
		
		var group_info_content = '';
		
		//call helper function that assembles all of the user's group data
		//if(typeof data.groupinfo.user != 'undefined') {
		//	group_info_content = getGroupInfoContent(data);
		//}
		group_info_content = getGroupInfoContent(data);
		//append the fieldset to the div user_info element and fill it with the user's info
		$('div#group_info').append('<fieldset id="group_info"><legend >' + ESGF.setting.currentGroupName + 
									'</legend>' +
									'<h5>Group Information</h5><div style="margin-bottom:10px;margin-left:10px">Description: ' + data.groupinfo.group.groupdescription +'</div>' +
									'</fieldset>');
		$('fieldset#group_info').append(group_info_content);
		
		//show the user's group info
		$('div#group_info').show();
		
	}
	
	/*
	* Helper function for displaying the group info attached to a user
	*/
	/*
	* Need to come back here...there is a race condition that I need to resolve here
	*/
	function getGroupInfoContent(data) {
		
		var content = '';
		
		content = content + '<hr /><h5 style="margin-top:10px;">Users In Group</h5>';
		if(typeof data.groupinfo.user != 'undefined') {
			if(data.groupinfo.user instanceof Array) {
				for(var i=0;i<data.groupinfo.user.length;i++) {
					var userId = data.groupinfo.user[i].id;
					var userName = data.groupinfo.user[i].username;
					var email = data.groupinfo.user[i].email;
					var lastName = data.groupinfo.user[i].last;
					var firstName = data.groupinfo.user[i].first;
					var middleName = data.groupinfo.user[i].middle;
					var organization = data.groupinfo.user[i].organization;
					var city = data.groupinfo.user[i].city;
					var state = data.groupinfo.user[i].state;
					var country = data.groupinfo.user[i].country;
					var dn = data.groupinfo.user[i].dn;
					var openid = data.groupinfo.user[i].openid;
					
					var roleType = '';
					if(userName == 'rootAdmin') {
						roleType = 'super';
					} else {
						roleType = 'default';
					}
					
					//user info
					content = content + '<div style="border: 1px dotted #eee;margin-top:5px;margin-left:10px" id="userListing_' + userName + '"">User: ' + userName + 
																				' UserId: ' + userId + 
																				' email: ' + email + 
																				' lastName: ' + lastName + 
																				
																				//' middleName: ' + middleName + 
																				//' firstName: ' + firstName + 
																				//' organization: ' + organization + 
																				//' city: ' + city + 
																				//' state: ' + state + 
																				//' country: ' + country + 
																				//' dn: ' + dn + 
																				//' openid: ' + openid + 
																				
																				'</div>';
					//role info
					content = content + '<div style="margin-top:5px;margin-left:40px;font-style: italic;">' + 'Role: '  + roleType + '</div>';
				}
				
			} else {
				var userName = data.groupinfo.user.username;
				var userId = data.groupinfo.user.id;
				var email = data.groupinfo.user.email;
				var lastName = data.groupinfo.user.last;
				var firstName = data.groupinfo.user.first;
				var middleName = data.groupinfo.user.middle;
				var organization = data.groupinfo.user.organization;
				var city = data.groupinfo.user.city;
				var state = data.groupinfo.user.state;
				var country = data.groupinfo.user.country;
				var dn = data.groupinfo.user.dn;
				var openid = data.groupinfo.user.openid;
				var roleType = '';
				if(userName == 'rootAdmin') {
					roleType = 'super';
				} else {
					roleType = 'default';
				}
				
				content = content + '<div style="border: 1px dotted #eee;margin-top:5px;margin-left:10px" id="userListing_' + userName + '"">User: ' + userName + 
																			' UserId: ' + userId + 
																			' email: ' + email + 
																			' lastName: ' + lastName + 
																			
																			//' middleName: ' + middleName + 
																			//' firstName: ' + firstName + 
																			//' organization: ' + organization + 
																			//' city: ' + city + 
																			//' state: ' + state + 
																			//' country: ' + country + 
																			//' dn: ' + dn + 
																			//' openid: ' + openid + 
																			
																			'</div>';
				
			}
		} else {
			content = content + '<div style="margin-left:10px">' + 'There are no users currently in this group</div>';
			
		}
		
		var group_info_content = '<div class="group_info_content">' + content + '</div>';
		return group_info_content;
	}
	
	/*
	* Add User
	*/
	$("input#add_group-button[rel]").overlay({
		mask: '#000',
		onLoad: function() {
			
			
			$('#new_group_form').show();
			
			//$('#userName_input').show();
			clearFormValues();
			//overlay method
			$('h3#form_title').html('New Group Information');
			
			//first we must hide/remove any information previously there
			//$('#new_user_form').hide();
			//$('#user_info').hide();
			$('#group_info').hide();

			$('fieldset#group_info').remove();
			//$('div.user_info_header').remove();
			//$('div.user_info_content').remove();
			
		},
	
		onClose: function() {
			
			$('#new_group_form').hide();
			//$('#userName_input').hide();
			clearFormValues();
			
		}
		
	});
	
	
	/*
	* Edit Group
	*/
	$("input#edit_group-button[rel]").overlay({
		mask: '#000',
		onLoad: function() {
			
			$('h3#form_title').html('Edit User ' + ESGF.setting.currentGroupName);
			$('input#type').val('edit');
			
			query = { "groupName" : ESGF.setting.currentGroupName,"type" : "getGroupInfo" };
			var groupinfo_url = '/esgf-web-fe/extractgroupdataproxy';
			if(ESGF.setting.currentGroupName == 'default') {
				alert('You must select a group to edit');
			} else {
			
				$.ajax({
		    		url: groupinfo_url,
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
			
			$('#new_group_form').show();
		}
			
		},
	
		onClose: function() {
			
			$('#new_group_form').hide();

			//$('fieldset#user_info').remove();
			$('fieldset#group_info').remove();
			
		}
		
	});
	
	
	
	
	
	/* Helper function for filling content for edtiing data */	
	function fillFormContentForEdit(data) {

		var groupName = data.groupinfo.group.groupname;
		var groupDescription = data.groupinfo.group.groupdescription;
		
		$('input#groupName').val(groupName);
		$('textarea#groupDescription').val(groupDescription);
		
	}
	
	
	/*
	* Add User To Group
	*/
		
	$('input#edit_users_in_group-button[rel]').overlay({
		mask: '#000',
		onLoad: function() {
			var query = { "groupName" : ESGF.setting.currentGroupName ,"type" : "getAllUsersInGroup" };
			var groupinfo_url = '/esgf-web-fe/extractgroupdataproxy';
			
			
			if(ESGF.setting.currentGroupName == 'default') {
				alert('You must select a group to edit user priviledges');
			} else {
				$.ajax({
		    		url: groupinfo_url,
		    		type: "GET",
		    		data: query,
		    		dataType: 'json',
		    		success: function(data) {
		    			displayPotentialUsers(data);
		    		},
					error: function() {
						alert('error');
					}
				});
			}
			
			
		},
		onClose: function() {
			$('#potential_users').empty();
		}
	});
	
	/* Data coming in as follows:
		
	 */
	function displayPotentialUsers(data) {
		
		var checkbox = '';
		$('input#groupName').val(ESGF.setting.currentGroupName);
		
		for(var i=0;i<data.users.allusers.user.length;i++) {
			var userName = data.users.allusers.user[i].username;
			//if(userName != 'rootAdmin') {
				if(isUserInGroup(data,userName)) {
					checkbox = '<p><input style="margin-left:10px;margin-bottom:0px" type="checkbox" checked="yes" id="userChoices" name="' + userName + '" value="' + userName + '" /> ' + userName + '</p>';
				} else {
					checkbox = '<p><input style="margin-left:10px;margin-bottom:0px" type="checkbox" id="userChoices" name="' + userName + '" value="' + userName + '" /> ' + userName + '</p>';
				}
				$('#potential_users').append(checkbox);
			//}
			
		}
	}
	
	
	/* boolean function to determine if a user is in a group
	*/
	function isUserInGroup(data,user) {
		var isUserInGroup = false;
		
		if(data.users.ingroup.user instanceof Array) {
			
			for(var i=0;i<data.users.ingroup.user.length;i++) {
				if(user == data.users.ingroup.user[i].username) {
					isUserInGroup = true;
				}
			}
			
		} else {
			//alert('user: ' + user + ' username: ' + data.users.ingroup.user.username);
			if(user == data.users.ingroup.user.username) {
				isUserInGroup = true;
			}
		}
		return isUserInGroup;
	}
	
	
	/*
	* Remove User
	*/
	$('input#delete_group-button').click(function(){
		
		if(ESGF.setting.currentGroupName == 'default') {
			alert('You must select a group to delete');
		} else {
			$('#new_group_form').hide();
			//$('#user_info').hide();
			$('#group_info').hide();
	
			
			if (confirm("Are you sure you want to delete user " + ESGF.setting.currentGroupName + "?")) {
			 
				if(ESGF.setting.currentGroupName != '') {
					var deletedUserInput = '<input type="hidden" name="'+ 'groupName' +'" value="' + ESGF.setting.currentGroupName + '" />';
					var input = '<input type="hidden" name="'+ 'type' +'" value="delete" />' + deletedUserInput;
					//send request
					var formStr = '<form action="" method="post">' + input + '</form>';
					
					jQuery(formStr).appendTo('body').submit().remove();
				}
	
				//$('div.user_info_header').remove();
				//$('div.user_info_content').remove();
				//$('div.group_info_header').remove();
				$('div.group_info_content').remove();
				$('div.header_name').remove();
				
			}
		}
	});
	
	/*   Utility functions   */
	function clearFormValues() {
		$('textarea#groupDescription').val("");
		$('input#groupName').val("");
	}
	
	
	
});
</script>		

