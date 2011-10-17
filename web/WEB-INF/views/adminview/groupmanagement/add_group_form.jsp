<div class="simple_overlay" id="addGroupForm"> 
	<form id="new_group_form" action="" method="post" >
		<h3 id="form_title">New Group Information</h3>
		<table id="addgroup_table_id" class="addgroup_table">  
			<tr>
				<td>
					<div class="addgroup_properties">Group Name</div> 
				</td>
				<td>
					<input type="text" class="text" id="groupName" name="groupName" value=""> 
				</td>
			</tr>
			<tr>
				<td>
					<div class="addgroup_properties">Group Description</div> 
				</td>
				<td>
					<textarea type="text" id="groupDescription" name="groupDescription" value="Type Description Here" class="addgroup_textarea" >
  					</textarea>
				</td>
			</tr>
		</table>					
		<p>
		  	<input type="hidden" name="type" id="type" value="add"/>
      		<input style="" class="adminbutton" type="submit" value="Submit">
      	</p>
	</form>
</div>