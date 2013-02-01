function register(user, name, attURL, desc, regURL){
  
  hideAll();
  
  var userName = user;
  var group = name;
  var reg = regURL;
  var att = attURL;
  var info = desc;
  
  var jsonObj = new Object;
    jsonObj.userName = userName;
	  jsonObj.group = group;
	  jsonObj.reg = reg;
    jsonObj.att = att;
			
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
        var output = data.EditOutput.comment;
        var returned = output.split("[]");
        if(returned[0] == "success"){
          var classId = group.replace(/\s/g,"");
          $('.' + classId).hide();
          $('.updatable').append('<tr class="' + classId + '"><td>' + group + '</td><td>' + info + '</td><td>user</td><td></td></tr>');
          //unregister button
          //<input alt="" id="' + group + '" type="submit" value="Leave" class="button" onclick="javascript:unregister(\'' + user + '\', \'' + group + '\', \'' + info + '\', \'' + role + '\', \'' + autoReg + '\')"/>
		      $("div .success").html(returned[1]);
          $("div .middle").append($("div .success"));
          $("div .success").show();
          } 
        else if(returned[0] == "existing"){
          var classId = group.replace(/\s/g,"");
          $('.' + classId).hide();
          $("div .error").html(returned[1]);
          $("div .middle").append($("div .error"));
			    $("div .error").show();
        }
        else if(returned[0] == "pending"){
          var classId = group.replace(/\s/g,"");
          $('.' + classId).hide();
          $('.updatable').append('<tr class="' + classId + '"><td>' + group + '</td><td>' + info + '</td><td>pending</td><td></td></tr>');
		      $("div .success").html(returned[1]);
          $("div .middle").append($("div .success"));
          $("div .success").show();
        }
      }
      else{
	      $("div .error").html(data.EditOutput.comment);
        $("div .middle").append($("div .error"));
	    	$("div .error").show();
	    }
	  },
		error: function(request, status, error) {
		  $("div .error").html("Request: " + request + "\nStatus:  " + status + "\nError:  " + error);
      $("div .middle").append($("div .error"));
			$("div .error").show();
		}
	});	
}
