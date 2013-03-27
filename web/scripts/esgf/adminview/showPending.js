function pending(){
  hideAll();
  //empty table first then fill
  var Parent = document.getElementById('pending_groups_table');
  for(var i = Parent.rows.length - 1; i > 0; i--){
    Parent.deleteRow(i);
  }

  var userName = "admin";
  var jsonObj = new Object;
	jsonObj.userName = userName;
			
	var jsonStr = JSON.stringify(jsonObj);
	var userinfo_url = '/esgf-web-fe/getallpendingproxy';
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
        var rows = users.slice(0,-1);
            rows = rows.split("][");
        if(rows.length > 1){    
          for(var i = 1; i < rows.length; i++){
            var userInfo = rows[i].split(", ");
            $('.pendingTable').append('<tr><td>' + userInfo[2] + '</td><td>' + userInfo[0] + '</td><td>' + userInfo[4] + '</td><td><a href="javascript:submitPending(\'' + userInfo[0] + '\', \'' + userInfo[1] + '\', \'' + userInfo[3] + '\', \'approved\')">Aprove</a> &nbsp; <a href="javascript:submitPending(\'' + userInfo[0] + '\', \'' + userInfo[1] + '\', \'' + userInfo[3] + '\', \'reject\')">Reject</a></tr>');
          }
          $("div .pending").show();
        }
        else{
   		  $("div .success").html("No pending request at this time");
   			$("div .success").show();
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
}
