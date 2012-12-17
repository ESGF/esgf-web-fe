function cancelUserInfo(){
  hideAll();
  $("div .editUserInfo").hide();
  $("div .userInfo").show();
}

function editUserInfo(){
  hideAll();
  $("div .userInfo").hide();
  $("div .editUserInfo").show();
}

function submitUserInfo(user){
  hideAll();
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
    $("div .top").append($("div .error"));
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
   		    $("div .success").html(data.EditOutput.comment);
   			  $("div .top").append($("div .success"));
          $("div .success").show();
 			    $("div .editUserInfo").hide();
          $("div .userInfo").show();
          setTimeout(function(){window.location="accountsview"},1000);
        } 
        else {
   			  $("div .error").html("Updating your account has failed! " + data.EditOutput.comment);
          $("div .top").append($("div .error"));
   			  $("div .error").show();
   		  }
   	  },
		  error: function(request, status, error) {
		    $("div .error").html(request + " | " + status + " | " + error);
        $("div .top").append($("div .error"));
			  $("div .error").show();
		  }
	  });
  } 
}
