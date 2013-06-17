function submitUserInfo(username){
    var user = username;
    var lastName = $("input[id=lastName]").val();
    var firstName = $("input[id=firstName]").val();
    var middleName = $("input[id=middleName]").val();
    var email = $("input[id=email]").val();
    var organization = $("input[id=organization]").val();
    var city = $("input[id=city]").val();
    var state = $("input[id=state]").val();
    var country = $("input[id=country]").val();
    if (lastName == "" || firstName == "" || email == ""){
      $("div .error").html("First Name, Last Name, Email Can not be blank");
      $("div .error").show();
    }
    else{
      var jsonObj = new Object;
	    jsonObj.userName = user;
	    jsonObj.lastName = lastName;
	    jsonObj.middleName = middleName;
	    jsonObj.firstName = firstName;
      jsonObj.email = email;
	    jsonObj.organization = organization;
      jsonObj.city = city;
      jsonObj.state = state;
      jsonObj.country = country;
		
	    var jsonStr = JSON.stringify(jsonObj);
	    var userinfo_url = '/esgf-web-fe/updateuserinfoproxy';
	    $.ajax({
	      type: "POST",
  	    url: userinfo_url,
	      async: true,
	      cache: false,
  	    data: {query:jsonStr},
  	    dataType: 'json',
  	    success: function(data) {
   		    if (data.EditOutput.status == "success") {
            hideAll();
   		      $("div .success").html("Successful updated " + user + "'s information" );
            $("div .success").show();
          } 
          else {
   			    $("div .error").html("Updating this account has failed! " + data.EditOutput.comment);
   			    $("div .error").show();
   		    }
   	    },
		    error: function(request, status, error) {
		      $("div .error").html(request + " | " + status + " | " + error);
			    $("div .error").show();
		    }
	    });
    } 
  }

  function cancelUserInfo(){
    $("div .edit_field").hide();
  }
