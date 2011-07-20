$(document).ready(function(){
	

	//$("a#add1[rel]").overlay();
	
	
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
			var query = { "id" : currentUserName, "type" : "edit" };
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
			//overlay method
			$('h3#form_title').html('New User Information');
			
			//first we must hide/remove any information previously there
			//$('#new_user_form').hide();
			$('#user_info').hide();

			$('div.user_info_header').remove();
			$('div.user_info_content').remove();
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
			$('h3#form_title').html('Edit User ' + currentUserName);
			$('#new_user_form').hide();
			//$('#user_info').hide();
			$('#userName_input').hide();
			
			//$('div.user_info_header').remove();
			//$('div.user_info_content').remove();
			//clearFormValues();
			
			$('input#type').val('edit');
			
			var query = { "id" : currentUserName, "type" : "edit" };
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
			$('div.user_info_header').remove();
			$('div.user_info_content').remove();
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

		
		if (confirm("Are you sure you want to delete user " + currentUserName + "?")) {
		 
			if(currentUserName != '') {
				var deletedUserInput = '<input type="hidden" name="'+ 'user' +'" value="' + currentUserName + '" />';
				var input = '<input type="hidden" name="'+ 'type' +'" value="delete" />' + deletedUserInput;
				//send request
				var formStr = '<form action="" method="post">' + input + '</form>';
				
				jQuery(formStr).appendTo('body').submit().remove();
			}

			$('div.user_info_header').remove();
			$('div.user_info_content').remove();
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
