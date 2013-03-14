function group(){
  hideAll();
  //empty table first then fill
  var Parent = document.getElementById('manage_groups_table');
  for(var i = Parent.rows.length - 1; i > 0; i--){
    Parent.deleteRow(i);
  }

  var userName = "admin";
  var jsonObj = new Object;
	jsonObj.userName = userName;
			
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
        for(var i = 1; i < rows.length; i++){
          var userInfo = rows[i].split(", ");
          $('.groupTable').append('<tr><td><a href="javascript:editGroup(\'' + userInfo[0] + '\',\'' + userInfo[1] + '\',\'' + userInfo[2] + '\',\'' + userInfo[3] + '\',\'' + userInfo[4] +  '\')">' + userInfo[1] + '</a></td><td>' + userInfo[2] + '</td><td>' + userInfo[3] + '</td><td>' + userInfo[4] + '</td></tr>');
        }
        $('.groupTable').append('<tr><td><a href="javascript:createGroup()"> Create a new group </a></td><td></td><td></td><td></td></tr>');
        $("div .group").show();
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
