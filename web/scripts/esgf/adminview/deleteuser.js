function deleteUserInfo(username){
    cancelDelete();

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
          $("div .edit_field").hide();
          user();
   		    $("div .success").html(data.EditOutput.comment);
          $("div .success").show();
        }
        else{
   			  $("div .error").html("Deleting this group has failed! " + data.EditOutput.comment);
   			  $("div .error").show();
        }
      },
      error: function(request, status, error) { 
   			  $("div .error").html("internal Error: " + request + " " + status + " " + error);
   			  $("div .error").show();
      }
    });
  }

  function areYouSure(username){
    $("div .submit").hide();
    $("div .doubleCheck").show();
  }

  function cancelDelete(){
    $("div .doubleCheck").hide();
    $("div .submit").show();
  }
