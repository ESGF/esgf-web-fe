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
			$('#potential_users').hide();
			$('#edit_users_submit').hide();
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
		$('#potential_users').show();

		$('#edit_users_submit').show();
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