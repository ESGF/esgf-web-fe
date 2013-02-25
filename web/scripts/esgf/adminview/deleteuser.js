function deleteUserInfo(username){
    cancelUserDelete();

    var userName = username;
    var jsonObj = new Object;
	  jsonObj.userName = userName;
			
	  var jsonStr = JSON.stringify(jsonObj);
	  var userinfo_url = '/esgf-web-fe/deleteuserproxy';
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
          $("div .success").show();
          $("div .user_info").hide();
          user();
        }
        else{
   			  $("div .error").html("Deleting this account has failed! " + data.EditOutput.comment);
   			  $("div .error").show();
        }
      },
      error: function(request, status, error) { 
        alert("Error " + request + " " + status + " " + error);
      }
    });
  }

  function areYouSure(username){
    $("div .submit").hide();
    $("div .doubleCheck").show();
  }

  function cancelUserDelete(){
    $("div .doubleCheck").hide();
    $("div .submit").show();
  }
