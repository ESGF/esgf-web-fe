<%@ include file="/WEB-INF/views/common/include.jsp" %>
<style>
.accountbutton {
	font-size: 13px;
	color: white;
	border: 1px solid #9c9c9c;
	background: #838943;
	cursor: pointer;
}
	
</style>

<!--  header info -->
<div class="span-24 last" style="margin-top:20px">
	<h2 style="text-align:center">
	${accounts_userinfo.userName} Account Home
	</h2>
</div>
<div class="editstatus"></div>					

<!-- user information table -->
<div class="prepend-3 span-10">
	<h3 style="text-align:center">User account information</h3>						
	<table id="table_id">  
		<thead>
			<tr class="user_header">
				<th>Property</th>
				<th>Value</th>
			</tr>
			<tr>
				<td>User Name</td>  
				<td>${accounts_userinfo.userName}</td>      	
			</tr>
			<tr>
				<td>First Name</td>  
				<td>${accounts_userinfo.firstName}</td> 
			</tr>
			<tr>
				<td>Middle Name</td>  
				<td>${accounts_userinfo.middleName}</td> 
			</tr>
			<tr>
				<td>Last Name</td>  
				<td>${accounts_userinfo.lastName}</td> 
			</tr>
			<tr>
				<td>Email Address</td>  
				<td>${accounts_userinfo.emailAddress}</td> 
			</tr>
			<tr>
				<td>Organization</td>  
				<td>${accounts_userinfo.organization}</td> 
			</tr>
			<tr>
				<td>City</td>  
				<td>${accounts_userinfo.city}</td> 
			</tr>
			<tr>
				<td>State</td>  
				<td>${accounts_userinfo.state}</td> 
			</tr>
			<tr>
				<td>Country</td>  
				<td>${accounts_userinfo.country}</td> 
			</tr>
			<tr>
				<td>OpenId</td>  
				<td>${accounts_userinfo.openId}</td>${accounts_userinfo.userName} 
			</tr>
			<tr>
				<td>DN</td>  
				<td>${accounts_userinfo.DN}</td> 
			</tr>			
		</thead>
	</table>
	<!-- the add user, edit user, and delete user buttons -->
	<!--
	<div class="buttons" style="margin-bottom:40px;">
		<input class="accountbutton" id="edit_user-button" type="submit" value="Edit Information" />
		<input class="accountbutton" id="delete_user-button" type="submit" value="Request Group Membership" />
	</div>${accounts_userinfo.userName}
	 -->
</div>
<div class="prepend-1 span-9 append-1 last">
<h3 style="text-align:center">Groups</h3>	
<h5 style="text-align:center">Membership</h5>						
	<table id="groups_table_id">  
		<thead>
			<tr class="groups_header">
				<th>Group Name</th>
				<th>Description</th>
				<th>Role</th>
			</tr>
		</thead>
		<tbody>
	        <c:set var="j" value="0"/>
	        <c:forEach var="group" items="${accounts_groupinfo}">
				 <tr class="group_rows" 
				 	 id="${accounts_groupinfo[j].name}" 
				 	 style="cursor:pointer">  
	                <td>${accounts_groupinfo[j].name}</td>  
	                <td>${accounts_groupinfo[j].description}</td> 
	                <td>${accounts_roleinfo[j]}</td>${accounts_userinfo.userName}  
	            </tr> 
	            <c:set var="j" value="${j+1}"/>
			</c:forEach>
		</tbody>
	</table>
<!-- 
<h5 style="text-align:center">Groups Administered</h5>						
	<table id="groups__admin_table_id">  
		<thead>
			<tr class="groups_admin_header">
				<th>Group Name</th>
				<th>Description</th>
				<th>Role</th>${accounts_userinfo.userName}
			</tr>
			<tr>
				<td>g</td>
				<td>d</td>  
				<td>r</td>      	
			</tr>
		</thead>
	</table>
-->
</div>

<div class="prepend-2 span-8 last">
<h3 style="text-align:center">Change password</h3>	
<div class="myerror"></div>					
	<table id="groups_table_id">  
		<tr>
			<td> Old Password: </td>
			<td> <input type="text" name="oldpassword"/> </td>
		</tr>
		<tr>
			<td> Password: </td>
			<td> <input type="text" name="password1"/> </td>
		</tr>
		<tr>
			<td> Verify: </td>
			<td> <input type="text" name="password2"/> </td>
		</tr>
		<tr span="2">
			<td>
				<input type="button" id="changepwd" value="Change password" class="button"/>
			</td>
		</tr>
	</table>

</div>
 
<script>

$(document).ready(function(){
	$("#changepwd").click(function() {
		var oldpassword = $("input[name=oldpassword]").val();
		var password1 = $("input[name=password1]").val();
		var password2 = $("input[name=password2]").val();
		var error = false;
		
		if (password1 != password2) {
			error = true;
			$("div .errormsg").html("Password does not match!");
			return;
		} else {
			var jsonObj = new Object;
			jsonObj.userName = ${accounts_userinfo.userName};
			jsonObj.type = "editUserInfo";
			jsonObj.oldpasswd = oldpassword;
			jsonObj.newpasswd = password1;
			jsonObj.verifypasswd = password2;
			
			var query = JSON.stringify(jsonObj);
			var userinfo_url = '/esgf-web-fe/edituserinfoproxy';
			$.ajax({
	    		type: "POST",
	    		url: userinfo_url,
				async: true,
				cache: false,
	    		data: query,
	    		dataType: 'json',
	    		success: function(data) {
	    			$("div .editstatus").html("The password reset is successful!");
	    		},
				error: function() {
					$("div .editstatus").html("The password reset is failed!");
				}
			});			
		}
		
	});
	
});
</script>
	    						