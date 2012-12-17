function changePassword(user){
    hideAll();
		var oldpassword = $("input[name=oldpasswd]").val();
		var password1 = $("input[name=password1]").val();
		var password2 = $("input[name=password2]").val();
		var error = false;
		var errorMessage = "error";

    if(password1 != password2){
      error = true;
      errorMessage = "Passwords did not match";
    }
    else if(password1.length < 6){
      error = true;
      errorMessage = "Password not long enough";
    }
    else if(password1.search(/\d/) == -1) {
      error = true;
      errorMessage = "You must use at least one number in your password";
    }
    else if(password1.search(/[a-zA-Z]/) == -1) {
      error = true;
      errorMessage = "You must use at least one letter in your password";
    }

		if(error) {
			$("div .error").html(errorMessage);
      $("div .bottom").append($("div .error"));
			$("div .error").show();
			return;
    } 
    else {
			var jsonObj = new Object;
			jsonObj.userName = user;
			jsonObj.type = "editUserInfo";
			jsonObj.oldpasswd = oldpassword;
			jsonObj.newpasswd = password1;
			jsonObj.verifypasswd = password2;
			
      //TODO new password must conform to our rules
			var jsonStr = JSON.stringify(jsonObj);
			var userinfo_url = '/esgf-web-fe/edituserinfoproxy';
			$.ajax({
	    	  type: "POST",
	    	  url: userinfo_url,
				  async: true,
				  cache: false,
	    		data: {query:jsonStr},
	    		dataType: 'json',
	    		success: function(data) {
	    			if (data.EditOutput.status == "success") {
		    			$("div .success").html("Your password has been reset successfully!");
		    			document.getElementById("oldpasswd").value="";
              document.getElementById("password1").value="";
              document.getElementById("password2").value="";
              $("div .bottom").append($("div .success"));
              $("div .success").show();
	    			} else {
	    				$("div .error").html("The password reset has failed! " + data.EditOutput.comment);
              $("div .bottom").append($("div .error"));
	    				$("div .error").show();
	    			}
	    		},
				error: function(request, status, error) {
					$("div .error").html("The password reset has failed!");
          $("div .bottom").append($("div .error"));
					$("div .error").show();
				}
			});			
		}
	}
