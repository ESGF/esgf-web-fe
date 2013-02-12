function findpassword() {
  $("div .success").hide();
	$("div .error").hide();
  var openid = document.getElementById("pwdopenid").value;
  var jsonObj = new Object;
  jsonObj.openid = openid;      
	var jsonStr = JSON.stringify(jsonObj);
  var userinfo_url = '/esgf-web-fe/forgotpasswordproxy';
  $.ajax({
	  type: "POST",
	  url: userinfo_url,
		async: true,
		cache: false,
	  data: {query:jsonStr},
	  dataType: 'json',
    statusCode: {
      404: function(){
        alert("page note found");
      }},
	    success: function(data) {
	      if (data.EditOutput.status == "success") {
          document.getElementById("pwdopenid").value="";
		    	$("div .success").html(data.EditOutput.comment);
		    	$("div .success").show();
		    	$("div .error").hide();
	    	} 
        else {
	    	  $("div .error").html("Error");
	    		$("div .error").show();
	    		$("div .success").hide();
	    	}
	    },
			error: function(request, status, error) {
			  alert('request: ' + request + ' status: ' + status + ' error: ' + error);
        $("div .error").html("Error Three");
				$("div .error").show();
				$("div .success").hide();
			}
		});
}
