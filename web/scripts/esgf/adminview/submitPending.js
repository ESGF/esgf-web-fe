function submitPending(groupName, groupId, userId, type){
  var name = groupName;
  var gId = groupId;
  var uId = userId;
  var choice = type;
  
  var jsonObj = new Object;
      jsonObj.groupName = name;
	    jsonObj.groupId = gId;
	    jsonObj.userId = uId;
	    jsonObj.choice = choice;
		
  var jsonStr = JSON.stringify(jsonObj);
	var userinfo_url = '/esgf-web-fe/updatependingproxy';
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
   		  $("div .success").html("Successful updated");
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
