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
	
	$('tr.user_rows').hover(function() {
		
	});
	
	/**
	* Will display the user's information when the admin clicks on a row
	*/
	$('tr.user_rows').click(function(){

		//first we must hide/remove any information that has previously been displayed
		//overlay user form
		$('#new_user_form').hide();
		//user information displayed on page
		$('#user_info').hide();
		//group information displayed on page
		$('#group_info').hide();

		//content in the user and groups info divs
		$('fieldset#user_info').remove();
		$('fieldset#group_info').remove();
		
		//grab the username from the id of the row
		var userName = $(this).attr("id");

		//change the background color of the row so that the user
		//can see the selection highlighted
		$(this).css('background','#FAECC8');

		//change the previously selected userName back to the previous background
		//NOTE-> this background (ffffff) DOES NOT MATCH the main background
		$('tr#' + ESGF.setting.currentUserName).css('background','#f9f6f1');
		
		//set the global current user name to the username selected
		ESGF.setting.currentUserName = userName;
		
		// from username we can get the rest of the info via an ajax call to extractuserdataproxy //
		// but MAKE SURE THAT IT IS NOT NULL!!! //
		if(userName != null && userName != "") {
			var query = { "userName" : ESGF.setting.currentUserName, "type" : "getUserInfo" };
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
			
			
			//query = { "userName" : ESGF.setting.currentUserName, "type" : "groupsForUser" };
			//var groupinfo_url = '/esgf-web-fe/extractgroupdataproxy';
			//$.ajax({
	    	//	url: groupinfo_url,
	    	//	type: "GET",
	    	//	data: query,
	    	//	dataType: 'json',
	    	//	success: function(data) {
	    	//		processGroupContent(data);
	    	//	},
			//	error: function() {
			//		alert('error');
			//	}
			//});
			
			
		} else {
			alert('Must have a valid user name to perform this operation');
		}
		
	});
	
	/*
	* Helper function for viewing user content
	* This is the resulting callback function from the ajax call to the extractdatauserproxy
	* The data has the following hierarchical structure:
		userinfo
			user
				<user properties (first, last, etc)>
			groups
				group
					<group properties>
			
	*/
	function processUserContent(data) {
		
		//get the userName from the returned jsonContent
		var userName = data.userinfo.user.username;

		//call helper function that assembles all of the user's data
		var user_info_content = getUserInfoContent(data);

		
		//append the fieldset to the div user_info element and fill it with the user's info
		$('div#user_info').append('<fieldset id="user_info"><legend >' + userName + '</legend></fieldset>');		
		$('fieldset#user_info').append(user_info_content);
		
		
		//show the user's info
		$('div#user_info').show();
		
		//var l = $('fieldset#user_info').html();
		
	}
	
	/*
	* Helper function for displaying the userContent
	* Basically assembles the properties of the user and the groups to which they belong
	*/
	function getUserInfoContent(data) {
		
		var content = '';
		
		var lastName = data.userinfo.user.last;
		var firstName = data.userinfo.user.first;
		var middleName = data.userinfo.user.middle;
		var emailAddress = data.userinfo.user.email;
		var userName = data.userinfo.user.username;
		var organization = data.userinfo.user.organization;
		var city = data.userinfo.user.city;
		var state = data.userinfo.user.state;
		var country = data.userinfo.user.country;
		
		content = '<h5 style="margin-top:10px;">User Information</h5>';
		content = content + '<div style="margin-bottom:5px;margin-left:10px">First Name: ' + firstName + ' Middle Name: ' + middleName + ' Last Name: ' + lastName + '</div>' +
					  '<div style="margin-bottom:5px;margin-left:10px">Email: ' + emailAddress + ' Organization ' + organization + '</div>' +
					  '<div style="margin-bottom:5px;margin-left:10px">City: ' + city + ' State: ' + state + ' Country: ' + country + '</div>'
					  ;
		content = content + '<div style="margin-bottom:10px"></div><hr /><h5 style="margin-top:10px;">Group Memberships</h5>';
		
		var roleType = '';
		if(userName == 'rootAdmin') {
			roleType = 'super';
		} else {
			roleType = 'default';
		}
		
		if(typeof data.userinfo.groups.group != 'undefined') {
			if(data.userinfo.groups.group instanceof Array) {
				for(var i=0;i<data.userinfo.groups.group.length;i++) {
					var groupId = data.userinfo.groups.group[i].groupid;
					var groupName = data.userinfo.groups.group[i].groupname;
					var groupDescription = data.userinfo.groups.group[i].groupdescription;
					
					content = content + '<div style="border: 1px dotted #eee;margin-top:5px;margin-left:10px" id="userListing_' + 
										groupName + '"">Group: ' + groupName + ' Description: ' + groupDescription + ' Role: ' + roleType + '</div>';
					
				}
			} else {

				var groupId = data.userinfo.groups.group.groupid;
				var groupName = data.userinfo.groups.group.groupname;
				var groupDescription = data.userinfo.groups.group.groupdescription;
				
				content = content + '<div style="border: 1px dotted #eee;margin-top:5px;margin-left:10px" id="userListing_' + 
				groupName + '"">Group: ' + groupName + ' Description: ' + groupDescription + ' Role: ' + roleType + '</div>';

			}
		} else {
			content = content + '<div style="margin-left:10px">' + 'No group memberships</div>';
		}
		
		var user_info_content = '<div class="user_info_content">' + content + '</div>';
		return user_info_content;
	}

	
	
	
	/*
	* Helper function for post ajax call processing for user content
	*/
	/*
	function processGroupContent(data) {
		
		//call helper function that assembles all of the user's group data
		var group_info_content = getGroupInfoContent(data);
		
		//append the fieldset to the div user_info element and fill it with the user's info
		$('div#group_info').append('<fieldset id="group_info"><legend >Group Information for ' + ESGF.setting.currentUserName + '</legend></fieldset>');
		$('fieldset#group_info').append(group_info_content);

		
		//show the user's group info
		$('div#group_info').show();
		
	}
	*/
	
	
	/*
	* Helper function for displaying the group info attached to a user
	*/
	/*
	* Need to come back here...there is a race condition that I need to resolve here
	*/
	/*
	function getGroupInfoContent(data) {
		
		var query = '';
		var content = '';
		
		if(data.groups.group instanceof Array) {
			for(var i=0;i<data.groups.group.length;i++) {
				ESGF.setting.currentGroupName = data.groups.group[i].groupname;
				
				var groupName = data.groups.group[i].groupname;
				var groupDescription = data.groups.group[i].groupdescription;
				content = content + '<div id="groupListing_' + groupName + '"">Group: ' + groupName + ' Group Description: ' + groupDescription + ' Role: ' + '</div>';
				
				query = { "userName" : ESGF.setting.currentUserName, "groupName" : groupName };
				var roleinfo_url = '/esgf-web-fe/extractroledataproxy';
				$.ajax({
		    		url: roleinfo_url,
		    		type: "GET",
		    		data: query,
		    		dataType: 'json',
		    		success: function(data) {
		    			var gn = groupName;
		    			processUserRoleForGroup(gn,data);
		    		},
					error: function() {
						alert('error');
					}
				});
				
				
			}
		} else {

			ESGF.setting.currentGroupName = data.groups.group.groupname;
			
			content = content + '<div id="groupListing_' + ESGF.setting.currentGroupName + '"">Group: ' + ESGF.setting.currentGroupName + ' Group Description: ' + data.groups.group.groupdescription + ' Role: ' + '</div>';
			
			query = { "userName" : ESGF.setting.currentUserName, "groupName" : ESGF.setting.currentGroupName };
			
			var roleinfo_url = '/esgf-web-fe/extractroledataproxy';
			$.ajax({
	    		url: roleinfo_url,
	    		type: "GET",
	    		data: query,
	    		dataType: 'json',
	    		success: function(data) {
	    			var gn = groupName;
	    			processUserRoleForGroup(gn,data);
	    		},
				error: function() {
					alert('error');
				}
			});
		}
		
		var group_info_content = '<div class="group_info_content">' + content + '</div>';
		
		return group_info_content;
		
	}
	*/
	
	
	function processUserRoleForGroup (groupName,data) {
		
		var roleid = data.role.roleid;
		var rolename = data.role.rolename;
		var roledescription = data.role.roledescription;
		$("div#groupListing_"+groupName).append(rolename);
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
	*
	* When the "Edit User" button is clicked, an overlay will be displayed.
	* This overlay is the form (form element "new_user_form") containing the following data:
		- firstName
		- lastName
		- emailAddress
		- organization
		- city 
		- country
		
		To add more editable values, add fields to the new user form html as well as  
		fillFormContentForEdit function.
	*/
	$("input#edit_user-button[rel]").overlay({
		mask: '#000',
		//several events must be triggered when the overlay is loaded
		onLoad: function() {

			//hide the username input...the username CANNOT be changed once it has been created
			$('#userName_input').hide();
			
			//Change the form title to "Edit User" (default is "Add New User")
			$('h3#form_title').html('Edit User ' + ESGF.setting.currentUserName);
			
			$('#new_user_form').hide();
			//$('#user_info').hide();
			$('#userName_input').hide();
			
			//change the input type parameter to "edit"
			//this is an important parameter as it is sent to the ManageUsersController which
			//must distinguish an 'update' operation from a 'create' operation
			$('input#type').val('edit');
			
			//change the user name paramter to the current username
			$('input#userName').val(ESGF.setting.currentUserName);
			
			//make an ajax call to get the pre-existing values of the user
			//The call is sent to the "ExtractUserInfoController" and se
			var query = { "userName" : ESGF.setting.currentUserName, "type" : "getUserInfo" };
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

			//reveal the form
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

		var firstName = data.userinfo.user.first;
		var middleName = data.userinfo.user.middle;
		var lastName = data.userinfo.user.last;
		var email = data.userinfo.user.email;
		var organization = data.userinfo.user.organization;
		var city = data.userinfo.user.city;
		var state = data.userinfo.user.state;
		var country = data.userinfo.user.country;

		$('input#form_firstName').val(firstName);
		$('input#form_middleName').val(middleName);
		$('input#form_lastName').val(lastName);
		$('input#form_email').val(email);
		$('input#form_organization').val(organization);
		$('input#form_city').val(city);
		$('input#form_state').val(state);
		$('input#form_country').val(country);
		
		//Note there may be more values later, this is for demo purposes
		
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
		$('input#form_middleName').val("");
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
