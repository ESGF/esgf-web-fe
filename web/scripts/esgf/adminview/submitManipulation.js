function manipulationSubmit(){
  $("div .error").hide()
  $("div .success").hide();
	
  var action = document.getElementById("action");
  var actionType = action.options[action.selectedIndex].value;
  var userName = document.getElementById("manipulationUsers").value;
  var group = document.getElementById("manipulationGroups");
  var groupName = group.options[group.selectedIndex].value;
  var approved = document.getElementById("manipulationApproved").value;

  var roles = [];
  $('#manipulationRoles :selected').each(function(i, selected){ roles[i] = $(selected).text(); });
  if(actionType == "" || actionType == null){
 	  $("div .error").html("no action type selected");
 		$("div .error").show();
  }
  else if(userName == "" || userName == null){
 	  $("div .error").html("no user has been selected");
 		$("div .error").show();
  }
  else if(groupName == "" || groupName == null){
 	  $("div .error").html("no group has been selected");
 		$("div .error").show();
  }
  else if(roles.length == 0){
 	  $("div .error").html("no roles have been selected");
 		$("div .error").show();
  }
  else {
    var jsonObj = new Object;
    jsonObj.action = actionType;
    jsonObj.user = userName;
    jsonObj.group = groupName;
    jsonObj.roles = roles.join();
    jsonObj.approved = approved;
	  
    var jsonStr = JSON.stringify(jsonObj);
	  var userinfo_url = '/esgf-web-fe/manipulationproxy';

    $.ajax({
	    type: "POST",
	    url: userinfo_url,
		  async: true,
		  cache: false,
	    data: {query:jsonStr},
	    dataType: 'json',
      success: function(data) {
        if (data.EditOutput.status == "success") {
          var bigString = data.EditOutput.comment;
          var smallString = bigString.split("][");
          var output = "";
          for(var i = 0; i < smallString.length; i++){
            if(i != smallString.length -1) {
              output += smallString[i] + "<br/>";
            }
            else{
              output += smallString[i] 
            }
          } 
			    $("div .success").html(output);
 			    $("div .success").show();
        }
        else{
 			    $("div .error").html(data.EditOutput.comment);
 			    $("div .error").show();
        }
      },
      error: function(request, status, error) { 
 		    $("div .error").html("Error " + request + " " + status + " " + error);
 		    $("div .error").show();
      }
    });
  }
}
