var loaded = 0;
function manipulation(){    
    hideAll();	
    if(loaded === 0){
        $("div .clock").show();
        
	    /******************************** get all groups ************************************/
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
				$("div .clock").hide();
			},
			error: function(request, status, error) { 
				$("div .error").html("Error " + request + " " + status + " " + error);
				$("div .error").show();
				$("div .clock").hide();
			}
	  });
	  loaded = 1;	
    }
  $("div .XYZ").show();
}
