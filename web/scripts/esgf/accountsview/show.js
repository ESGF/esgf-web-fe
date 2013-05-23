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
        var rows = output.split(",][");
        var name = "";
        var desc = "";
        var role = "user";
        if(rows.length > 1){
          for(var i = 0; i < rows.length; i++){
            var groupInfo = rows[i].split(",");
            if(groupInfo[0] == "wheel" || groupInfo[0] == ""){
              //logic?
            }
            else{
              var table = document.getElementById('groups_admin_table_id');
              var bool = true;
              //don't show groups user is registered in
              for(var r = 1, n = table.rows.length; r < n; r++){
                var groupsIn = table.rows[r].cells[0].innerHTML;
                if(groupsIn == groupInfo[0]){
                  bool = false;
                }
              }i
              if(bool){
                var classId = groupInfo[0];
                classId = classId.replace(/\s/g,"");
                var groupLocation = groupInfo[3].split("/");
                $('.allgroups').append('<tr class ="' + classId + '"><td>' + groupInfo[0] + '</td><td>' + groupInfo[2] + '</td><td>' + role  + '</td><td><input alt="" id="' + classId + '" type="submit" value="Join" class="button" onclick="javascript:register(\'' + user + '\', \'' + groupInfo[0] + '\', \'' + groupInfo[1] + '\', \'' + groupInfo[2] + '\', \'' + groupInfo[3] + '\')"/></td></tr>');
                //registration button!
              }
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

function showAllGroups(openId){
  $("div .SAR").hide();
  $("div .clock").show();
  document.getElementById('groupsRegistered').innerHTML = 'Federation Groups Registered';
  
  var Parent = document.getElementById('groups_admin_table_id');
  for(var i = Parent.rows.length - 1; i > 0; i--){
    Parent.deleteRow(i);
  }

  var oid = openId;
  var jsonObj = new Object;
	jsonObj.openId = oid;
	var jsonStr = JSON.stringify(jsonObj);
	var userinfo_url = '/esgf-web-fe/showallusersgroupsproxy';
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
        output = output.substring(1);
        output = output.substring(0, output.length -2);
        var rows = output.split(" ][");
        if(rows.length > 1){
          for(var i = 0; i < rows.length; i++){
            var groupInfo = rows[i].split(",");
            if(groupInfo[0] == "wheel" || groupInfo[0] == ""){
              //logic?
            }
            else{
                roles = groupInfo[2].split(";");
               $('.updatable').append('<tr><td>' + groupInfo[0] + '</td><td>' + groupInfo[1] + '</td><td>' + groupInfo[2] + '</td></tr>');
            }
          }
        }
        else{
          $("div .error").html("You are not a member of any groups.");
          $("div .bottom").append($("div .error"));
	    		$("div .error").show();
          $("div .loading").hide();
          $("div .loaded").hide();
        }
        $("div .clock").hide();
      } 
      else {
	   	  $("div .error").html(data.EditOutput.comment);
        $("div .middle").append($("div .error"));
	   		$("div .error").show();
	   		$("div .clock").hide();
	   	}
	  },
		error: function(request, status, error) {
			$("div .error").html(request + " | " + status + " | " + error);
			$("div .middle").append($("div .error"));
			$("div .error").show();
			$("div .clock").hide();
	  }
  });	
	
}

