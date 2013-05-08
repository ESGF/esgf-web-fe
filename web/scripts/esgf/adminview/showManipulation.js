function manipulation(){
    
    hideAll();
       
    /******************************** get all users ************************************/
    var userName = "admin";
    var jsonObj = new Object;
	  jsonObj.userName = userName;
			
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
          if(rows.length > 0){
            for(var i = 0; i < rows.length - 1; i++){
              var userInfo = rows[i].split(",");
              var option = document.createElement("option");
              option.text = userInfo[0] + ', ' + userInfo[2] + ' ' + userInfo[1] + ', ' + userInfo[3];
              option.value = userInfo[0];
              var select = document.getElementById('manipulationUsers');
              select.appendChild(option);
            }
          }
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
  /******************************** get all groups ************************************/
  var jsonStr = JSON.stringify(jsonObj);
	var userinfo_url = '/esgf-web-fe/getallgroupsproxy';
	$.ajax({
	  type: "POST",
	  url: userinfo_url,
	  async: true,
		cache: false,
	  data: {query:jsonStr},
	  dataType: 'json',
    success: function(data) {
      if (data.EditOutput.status == "success") { 
        var groups = data.EditOutput.comment;
        var rows = groups.slice(0,-1);
        rows = rows.split("][");
        if(rows.length > 0){
          for(var i = 1; i < rows.length; i++){
            var userInfo = rows[i].split(", ");
            if(userInfo[1] != "wheel"){
              var option = document.createElement("option");
              option.text = userInfo[1];
              option.value = userInfo[1];
              var select = document.getElementById('manipulationGroups');
              select.appendChild(option);
            }
          }
        }
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
  $("div .XYZ").show();
}
