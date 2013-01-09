function register(user, type, desc, role, auto){
  hideAll();
  var userName = user;
  var group = type;
  var info = desc;
  var role = role;
  var autoReg = auto;
  var jsonObj = new Object;
  jsonObj.userName = userName;
	jsonObj.group = group;
	jsonObj.role = role;
			
	var jsonStr = JSON.stringify(jsonObj);
	var userinfo_url = '/esgf-web-fe/addgroupproxy';
	$.ajax({
	  type: "POST",
	  url: userinfo_url,
		async: true,
		cache: false,
	  data: {query:jsonStr},
	  dataType: 'json',
	  success: function(data) {
	    if (data.EditOutput.status == "success") {
        var classId = group.replace(/\s/g,"");
        $('.' + classId).hide();
        $('.updatable').append('<tr class="' + classId + '"><td>' + group + '</td><td>' + info + '</td><td>' + role + '</td><td><input alt="" id="' + group + '" type="submit" value="Leave" class="button" onclick="javascript:unregister(\'' + user + '\', \'' + group + '\', \'' + info + '\', \'' + role + '\', \'' + autoReg + '\')"/></td></tr>');
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
      $("div .middle").append($("div .error"));
			$("div .error").show();
		}
	});	
}
