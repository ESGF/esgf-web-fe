function deleteGroupInfo(name){
  cancelDelete();

  var groupName = name;
  var jsonObj = new Object;
	jsonObj.groupName = groupName;
			
	var jsonStr = JSON.stringify(jsonObj);
	var userinfo_url = '/esgf-web-fe/deletegroupproxy';
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
        group();
   		  $("div .success").html(data.EditOutput.comment);
        $("div .success").show();
      }
      else{
   		  $("div .error").html("Deleting this account has failed! " + data.EditOutput.comment);
   		  $("div .error").show();
      }
    },
    error: function(request, status, error) { 
   		$("div .error").html("Error " + request + " " + status + " " + error + " " + data.EditOutput.comment);
   		$("div .error").show();
    }
  });
}
