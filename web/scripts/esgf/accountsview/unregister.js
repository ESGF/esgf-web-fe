function unregister(user, name, desc, role, auto){
  hideAll();
  var groupName = name;
  var groupDesc = desc;
  var groupRole = role;
  var groupAuto = auto;
  var userName = user;
     
  var jsonObj = new Object;
	jsonObj.userName = userName;
	jsonObj.group = groupName;
	jsonObj.role = groupRole;

  var jsonStr = JSON.stringify(jsonObj);
	var userinfo_url = '/esgf-web-fe/leavegroupproxy';
	$.ajax({
	  type: "POST",
	  url: userinfo_url,
		async: true,
		cache: false,
	  data: {query:jsonStr},
	  dataType: 'json',
	  success: function(data) {
	    if (data.EditOutput.status == "success") {
        var classId = groupName.replace(/\s/g,"");
        $('.' + classId).hide();
        if(groupAuto == "t"){
          $('.allgroups').append('<tr class="' + classId + '"><td>' + groupName + '</td><td>' + groupDesc + '</td><td>' + groupRole + '</td><td><input id="' + groupName + '" type="submit" value="Join" class="button" onclick="javascript:register(\'' + user + '\', \'' + groupName + '\', \'' + groupDesc + '\', \'' + groupRole + '\', \'' + groupAuto + '\')"/></td></tr>');
        }
        else if(groupAuto == "f"){
          $('.allgroups').append('<tr class="' + classId + '"><td>' + groupName + '</td><td>' + groupDesc + '</td><td>' + groupRole + '</td><td><input id="' + groupName + '" type="submit" value="Request" class="button" onclick="javascript:register(\'' + user + '\', \'' + groupName + '\', \'' + groupDesc + '\', \'' + groupRole + '\', \'' + groupAuto + '\')"/></td></tr>');
        }
        $("div .success").html(data.EditOutput.comment);
        $("div .middle").append($("div .success"));
        $("div .success").show();
      } 
      else {
    	  $("div .error").html(data.EditOutput.comment);
        $("div .middle").append($("div .error"));
    		$("div .error").show();
    	}
    },
		error: function(request, status, error) {
		  $("div .error").html(request + " " + status + " " + error);
      $("div .bottom").append($("div .error"));
			$("div .error").show();
		}
	});
}
