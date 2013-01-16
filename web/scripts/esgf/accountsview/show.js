function showless(){
  hideAll();
  $("div .loaded").show();
  $("div .groups").hide();
}

function showmoregroups(){
  hideAll();
  $("div .groups").show();
  $("div .loaded").hide();
}

function showmore(user){
  $("div .loading").hide();
  $("div .loaded").hide();
  hideAll();
  var Parent = document.getElementById('groups_table_id');
  for(var i = Parent.rows.length - 1; i > 0; i--){
    Parent.deleteRow(i);
  }

  var userName = user;
  var jsonObj = new Object;
	jsonObj.userName = userName;
			
	var jsonStr = JSON.stringify(jsonObj);
	var userinfo_url = '/esgf-web-fe/showallgroupsproxy';
	$.ajax({
	  type: "POST",
	  url: userinfo_url,
		async: true,
		cache: false,
	  data: {query:jsonStr},
	  dataType: 'json',
	  success: function(data) {
	    if (data.EditOutput.status == "success") {
        var output = data.EditOutput.comment;
        var rows = output.split("][");
        var name = "";
        var desc = "";
        var role = "user";
        if(rows.length > 2){
          for(var i = 0; i < rows.length; i++){
            var groupInfo = rows[i].split(", ");
            var classId = groupInfo[1];
            classId = classId.replace(/\s/g,"");
            if(groupInfo[1] == "wheel"){
              //logic?
            }
            else{
              $('.allgroups').append('<tr class ="' + classId + '"><td>' + groupInfo[1] + '</td><td>' + groupInfo[2] + '</td><td>' + role  + '</td><td><input alt="" id="' + groupInfo[1] + '" type="submit" value="Join" class="button" onclick="javascript:register(\'' + user + '\', \'' + groupInfo[1] + '\', \'' + groupInfo[2] + '\', \'' + role + '\', \'' + groupInfo[4] + '\')"/></td></tr>');
            }
          }
          $("div .groups").show();
        }
        else{
          $("div .error").html("You are already a member of all groups on this node.");
          $("div .bottom").append($("div .error"));
	    		$("div .error").show();
          $("div .loading").hide();
          $("div .loaded").hide();
        }
      } 
      else {
	   	  $("div .error").html(data.EditOutput.comment);
        $("div .middle").append($("div .error"));
	   		$("div .error").show();
	   	}
	  },
		error: function(request, status, error) {
			$("div .error").html(request + " | " + status + " | " + error);
			$("div .middle").append($("div .error"));
      $("div .error").show();
	  }
  });	
}
