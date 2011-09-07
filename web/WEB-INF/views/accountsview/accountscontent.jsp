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
				<td>${accounts_userinfo.openId}</td> 
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
	</div>
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
	                <td>${accounts_roleinfo[j]}</td>  
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
				<th>Role</th>
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
 
<script>

$(document).ready(function(){

	
});
</script>
	    						