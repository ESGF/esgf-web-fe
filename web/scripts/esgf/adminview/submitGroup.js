function groupInfo(id){
  var groupId = id;
  var groupName = $("input[id=groupName]").val();
  var groupDesc = $("input[id=groupDesc]").val();
  var groupVis = $("input[id=visable]").val();
  var groupAuto = $("input[id=approve]").val();

  if( groupName == "" || groupDesc == ""){
    $("div .error").html("\"Group Name\" and \"Group Description\" can not be empty.");
    $("div .error").show();
  }
  else if(groupVis != "t" && groupVis != "T" && groupVis != "True" && groupVis != "true" && groupVis != "f" && groupVis != "F" && groupVis != "False" && groupVis != "false"){
    $("div .error").html("Invalid input for \"Visable\". Please enter one of the following: t, true, f, false");
    $("div .error").show();
  } 
  else if (groupAuto != "t" && groupAuto != "T" && groupAuto != "True" && groupAuto != "true" && groupAuto != "F" && groupAuto != "f" && groupAuto != "False" && groupAuto != "false"){
    $("div .error").html("Invalid input for \"Auto Approval\". Please enter one of the following: t, true, f, false");
    $("div .error").show();
  }
  else{
    if(groupVis != "t" && groupVis != "T" && groupVis != "True" && groupVis != "true"){
      groupVis = "f";
    }
    else {
      groupVis = "t";
    }
    if(groupAuto != "t" && groupAuto != "T" && groupAuto != "True" && groupAuto != "true"){
      groupAuto = "f";
    }
    else {
      groupAuto = "t";
    }
    var jsonObj = new Object;
    jsonObj.groupId = groupId;
	  jsonObj.groupName = groupName;
	  jsonObj.groupDesc = groupDesc;
	  jsonObj.groupVis = groupVis;
	  jsonObj.groupAuto = groupAuto;
	
	  var jsonStr = JSON.stringify(jsonObj);
	  var userinfo_url = '/esgf-web-fe/updategroupinfoproxy';
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
   	      $("div .success").html("Successful updated " + groupName + "'s information" );
          $("div .success").show();
        } 
        else {
   	      $("div .error").html("Updating this group has failed! " + data.EditOutput.comment);
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
