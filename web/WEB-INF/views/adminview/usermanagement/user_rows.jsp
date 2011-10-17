<div class="prepend-2 span-20 append-2 last">
		<table id="table_id">  
  
			<!-- Table header -->  
			<thead>  
				<tr>  
	                <th>User Name</th>  
	                <th>Last Name</th> 
	                <th>First Name</th>  
	                <th>Email Address</th>  
				</tr>  
			</thead>  
  
			<!-- Table body -->  
			<tbody>   
		        <c:set var="j" value="0"/>
		        <c:forEach var="user" items="${ManageUsers_user}">
					 <tr class="user_rows" 
					 	 id="${ManageUsers_user[j].userName}" 
					 	 style="">  
		                <td>${ManageUsers_user[j].userName}</td>  
		                <td>${ManageUsers_user[j].lastName}</td> 
		                <td>${ManageUsers_user[j].firstName}</td>  
		                <td>${ManageUsers_user[j].emailAddress}</td>  
		            </tr> 
					<c:set var="j" value="${j+1}"/>
				</c:forEach>
			</tbody>  
					  
		</table> 
						
		<!-- the add user, edit user, and delete user buttons -->
		<div class="buttons">
  				<!--  <input class="adminbutton" id="add_user-button" type="submit" value="Add User" rel="#addUserForm" /> -->
  				<input class="adminbutton" id="edit_user-button" type="submit" value="Edit User" rel="#addUserForm" />
  				<input class="adminbutton" id="delete_user-button" type="submit" value="Remove Selected User" />
		</div>
	    				
	</div>