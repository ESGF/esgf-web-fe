var start = 1;

function user(direction){
    hideAll();
    $("div .clock").show();
    //empty table first then fill
    var Parent = document.getElementById('manage_users_table');
    for(var i = Parent.rows.length - 1; i > 0; i--){
      Parent.deleteRow(i);
    }
    
    if (direction == "back"){
    	start -= 50;
    }
    
    if(start < 1){
    	start = 1;
    }

    var userName = "admin";
    var jsonObj = new Object;
	  jsonObj.userName = userName;
	  jsonObj.start = start;
	  jsonObj.end = start + 25;
	  var jsonStr = JSON.stringify(jsonObj);
	  var userinfo_url = '/esgf-web-fe/getallusersproxy';
	  $.ajax({
	    type: "POST",
	    url: userinfo_url,
		async: true,
		cache: false,
	    data: {query:jsonStr},
	    dataType: 'json',
	    success: function(data) {
	    	if (data.EditOutput.status == "success") { 
	    		var users = data.EditOutput.comment;
	    		var rows = users.split("][");
	    		
	    		for(var i = 0; i < rows.length - 1; i++){
	    			var userInfo = rows[i].split(",");
	    			$('.userTable').append('<tr><td><a href="javascript:editUser(\'' + userInfo[4] + '\')">' + userInfo[0] + '</a></td><td>' + userInfo[1] + '</td><td>' + userInfo[2] + '</td><td>' + userInfo[3] + '</td><td>' + userInfo[4] + '</td></tr>');
	    		}
	    		
	    		if(rows.length == 26) $("div .showNext").show();
	    		if(start > 25) $("div .showBack").show();
	    		$("div .user").show();	    		
	    		start += 25;
        }
        else{
        	$("div .error").html(data.EditOutput.comment);
   			$("div .error").show();
        }
	    	$("div .clock").hide();
      },
      error: function(request, status, error) { 
   			$("div .error").html("Error " + request + " " + status + " " + error);
   			$("div .error").show();
   			$("div .clock").hide();
      }
    });
  }