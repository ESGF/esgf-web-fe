<%@ include file="/WEB-INF/views/common/include.jsp" %>
<%@ include file="/WEB-INF/views/common/adminContentJavaScript.jsp" %>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js" type="text/javascript"></script>

<!--  header info -->
<div class="container">
	<div class="prepend-3 span-18 append-3">
    <div class="main">
      <fieldset style="background: #F5F5E0">
			  <legend>Administration Home</legend>
			  <p>
          <table>
            <tr>
              <td><a href="javascript:user()" id="users">Manage Users</a></td>
              <td><a href="javascript:group()" id="groups">Manage Groups</a></td>
              <td><a href="javascript:pending()" id="pendings">Pending Request</a></td>
            </tr>
          </table>
        </p>
        
        <p>
          <ul style="list-style-type: none;">                    
	          <li><a href="<c:url value='/usermanagement'/> ">Manage Users</a></li>  
	          <li><a href="<c:url value='/creategroups'/> ">Manage Groups</a></li>
          </ul>
        </p>
      </fieldset>
    </div>

    <!-- ********** Manage User fieldsets ********** -->
    <div id="user" class="user" style="display: none">
      <fieldset style="background: #F5F5E0">
        <legend>Manage Users</legend>
        <p>
         <table id="manage_users_table">
					<thead>
						<tr>
							<th>User Name</th>
							<th>Last Name</th>
							<th>First Name</th>
              <th>User Email</th>
              <th>Open Id</th>
						</tr>
					</thead>
					<tbody class="userTable">
          </tbody>
        </table>  
        </p>
      </fieldset>
    </div>
    <!-- ********** *********** *********** -->

    <!-- ********** Manage Groups fieldsets ********** -->
    <div id="group" class="group" style="display: none">
      <fieldset style="background: #F5F5E0">
        <legend>Manage Groups</legend>
        <p>
          <table id="manage_groups_table">
					<thead>
						<tr>
							<th>Group Name</th>
							<th>Group Description</th>
							<th>Visable</th>
              <th>Auto Approval<th>
						</tr>
					</thead>
					<tbody class="groupTable">
          </tbody>
        </table>
        </p>
      </fieldset>
    </div>
    <!-- ********** *********** *********** -->

    <!-- ********** Manager Pending fieldsets ********** -->
    <div id="pending" class="pending" style="display: none">
      <fieldset style="background: #F5F5E0">
        <legend>Pending Request</legend>
        <p>
          <table id="pending_groups_table">
					<thead>
						<tr>
							<th>User Name</th>
							<th>Last Name</th>
							<th>First Name</th>
              <th>User Email</th>
							<th>Group Name</th>
							<th>Group Description</th>
							<th>Options</th>
						</tr>
					</thead>
					<tbody class="pendingTable">
          </tbody>
        </table>
        </p>
      </fieldset>
    </div>
    <!-- ********** *********** *********** -->
    
    <!-- ********** Messages ********** -->
    <div class="error" style="display: none"></div>
		<div class="success" style="display: none"></div>
    <!-- ********** *********** *********** -->
    
    <!-- ********** Edit User fieldsets ********** -->
    <div id="edit_field" class="edit_field" style="display: none">
      <fieldset style="background: #F5F5E0">
        <legend>Edit Selection</legend>
        <p>
          <table id="edit_table">
					<thead>
						<tr>
							<th>Field</th>
              <th>Value</th>
						</tr>
					</thead>
					<tbody class="editTable">
          </tbody>
        </table>
        </p>
      </fieldset>
    </div>
    <!-- ********** *********** *********** -->
  
  </div>
</div>

<script language="javascript">
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
        var rows = groups.split("][");
        for(var i = 1; i < rows.length - 1; i++){
          var userInfo = rows[i].split(",");
          $('.groupTable').append('<tr><td><a href="javascript:editGroup(\'' + userInfo[0] + '\',\'' + userInfo[1] + '\',\'' + userInfo[2] + '\',\'' + userInfo[3] + '\',\'' + userInfo[4] +  '\')">' + userInfo[1] + '</a></td><td>' + userInfo[2] + '</td><td>' + userInfo[3] + '</td><td>' + userInfo[4] + '</td></tr>');
        }
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

function editGroup(id, name, desc, vis, auto){
  //empty table first then fill
  var Parent = document.getElementById('edit_table');
  for(var i = Parent.rows.length - 1; i > 0; i--){
    Parent.deleteRow(i);
  }

  $('.editTable').append('<tr><td>Group Id</td><td>' + id + '</td></tr>' +
                                     '<tr><td>Group Name:</td><td><input alt="" type="text" class="text" id="groupName" value="' + name + '"/></td></tr>' +
                                     '<tr><td>Group Description:</td><td><input alt="" type="text" class="text" id="groupDesc" value="' + desc + '"/></td></tr>' +
                                     '<tr><td>Visable:</td><td><input alt="" type="text" class="text" id="visable" value="' + vis + '"/> (t or f)</td></tr>' +
                                     '<tr><td>Auto Approve:</td><td><input alt="" type="text" class="text" id="approve" value="' + auto + '"/> (t or f)</td></tr>' +
                                     '<tr><td></td><td><div class="submit"><input alt="" id="userUpdate" type="submit" value="Submit" class="button" onclick="javascript:groupInfo(\'' + id + '\')"/>' +
                                     '<input alt="" id="cancelUpdate" type="submit" value="Cancel" class="button" onclick="javascript:cancelUserInfo()"/>' +
                                     '<input alt="" id="userDelete" type="submit" value="Delete" class="button" onclick="javascript:areYouSure()"/></div>' +
                                     '<div class="doubleCheck" style="display: none"><font color="red"><strong>Are you sure you want to delete this group?</strong></font> ' +
                                     '<input alt="" id="canceldelete" type="submit" value="Cancel" class="button" onclick="javascript:cancelDelete()"/>' +
                                     '<input alt="" id="delete" type="submit" value="Delete" class="button" onclick="javascript:deleteGroupInfo(\'' + id + '\')"/></div>' +
                                     '</td></tr>');
  $("div .edit_field").show();
}

function groupInfo(id){
  var groupId = id;
  var groupName = $("input[id=groupName]").val();
  var groupDesc = $("input[id=groupDesc]").val();
  var groupVis = $("input[id=visable]").val();
  var groupAuto = $("input[id=approve]").val();

  if( groupName == "" || groupDesc == ""){
    $("div .error").html("\"Group Name\" and \"Groupo Description\" can not be empty.");
    $("div .error").show();
  }
  else if (!groupVis == "t" || !groupVis == "True" || groupVis != "true" || groupVis != "f" || groupVis != "False" || groupVis != "false"){
    alert(groupVis);
    $("div .error").html("Invalid input for \"Visable\". Please enter one of the following: t, true, f, false");
    $("div .error").show();
  } 
  else if (groupAuto != "t" || groupAuto != "True" || groupAuto != "true" || groupAuto != "f" || groupAuto != "False" || groupAuto != "false"){
    $("div .error").html("Invalid input for \"Auto Approval\". Please enter one of the following: t, true, f, false");
    $("div .error").show();
  }
  else{
    if(groupVis != "t" || groupVis != "True" || groupVis != "true"){
      groupVis = "t";
    }
    else {
      groupVis = "f";
    }
    if(groupAuto != "t" || groupAuto != "True" || groupAuto != "true"){
      groupAuto = "t";
    }
    else {
      groupAuto = "f";
    }

    var jsonObj = new Object;
    jsonObj.groupId = groupId;
	  jsonObj.groupName = groupName;
	  jsonObj.groupDesc = groupDesc;
	  jsonObj.groupVis = groupVis;
	  jsonObj.groupAuto = groupAuto;
	
	  var jsonStr = JSON.stringify(jsonObj);
	  var userinfo_url = '/esgf-web-fe/updategroupinfoproxy';
	  $.ajax({
	    type: "POST",
      url: userinfo_url,
	    async: true,
	    cache: false,
  	  data: {query:jsonStr},
  	  dataType: 'json',
  	  success: function(data) {
   	   if (data.EditOutput.status == "success") {
   	     $("div .success").html("Successful updated " + groupName + "'s information" );
          $("div .success").show();
          $("div .edit_field").hide();
        } 
        else {
   	      $("div .error").html("Updating this group has failed! " + data.EditOutput.comment);
   			  $("div .error").show();
   		  }
   	  },
		  error: function(request, status, error) {
	      $("div .error").html(request + " | " + status + " | " + error);
	      $("div .error").show();
	    }
	  });
  }
}

function deleteGroupInfo(id){
  alert(id);

}

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
        var rows = users.split("][");
        for(var i = 0; i < rows.length - 1; i++){
          var userInfo = rows[i].split(",");
          $('.userTable').append('<tr><td><a href="javascript:editUser(\'' + userInfo[4] + '\')">' + userInfo[0] + '</a></td><td>' + userInfo[1] + '</td><td>' + userInfo[2] + '</td><td>' + userInfo[3] + '</td><td>' + userInfo[4] + '</td></tr>');
        }
        $("div .pending").show();
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

function approve(user,group,role){

}


</script>
