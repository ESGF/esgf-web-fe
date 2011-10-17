<div class="prepend-2 span-20 append-2 last">
	<table id="table_id">  

        <thead>  
            <tr>  
                <th>Group Name</th>  
                <th>Group Description</th>  
            </tr>  
        </thead>  
        <tbody> 
	        <c:set var="j" value="0"/>
	        <c:forEach var="group" items="${CreateGroups_group}">
	        	<tr class="group_rows" id="${CreateGroups_group[j].name}" >
	        		<td>${CreateGroups_group[j].name}</td>
	        		<td>${CreateGroups_group[j].description}</td>
	        	</tr>
	            <c:set var="j" value="${j+1}"/>
	       	</c:forEach>
	    </tbody>
 	    </table>
 
    <!-- the add user, edit user, and delete user buttons -->
    <div class="buttons">
		<input class="adminbutton" id="add_group-button" type="submit" value="Add Group" rel="#addGroupForm" />
		<input class="adminbutton" id="edit_group-button" type="submit" value="Edit Group" rel="#addGroupForm" />
		<!--  <input class="adminbutton" id="add_user_to_group-button" type="submit" value="Add User To Selected Group" rel="#addUserToGroupForm" /> -->
		<input class="adminbutton" id="delete_group-button" type="submit" value="Remove Selected Group" />
    	<input class="adminbutton" id="edit_users_in_group-button" type="submit" value="Edit User(s) In Selected Group" rel="#addUsersToGroups"/>
    </div>
  
</div>