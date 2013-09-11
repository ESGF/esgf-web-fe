<%@ include file="/WEB-INF/views/common/include.jsp" %>
<%@ include file="/WEB-INF/views/common/adminContentJavaScript.jsp" %>


<!--  header info -->
<div class="container">
	<div class="prepend-3 span-18 append-3">
    <div class="main">
      <fieldset style="background: #F5F5E0">
			  <legend>Administration Home</legend>
			  <p>
          <table>
            <tr>
              <td><a href="javascript:user('start')" id="users">Manage Users</a></td>
              <td><a href="javascript:group()" id="groups">Manage Groups</a></td>
            </tr><tr>
              <td><a href="javascript:pending()" id="pendings">Pending Request</a></td>
              <td><a href="javascript:manipulation()" id="manipulation">User Role Group Manipulation</a></td>
            </tr>
          </table>
        </p>
        
  </fieldset>
    </div>
    <div class="clock" style="display: none"><center><img id="spinner" src="images/ajax-loader.gif" /><br/>Loading</center></div>
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
        <div class="showBack" style="display: none; float: left"><a href="javascript:user('back')">Back</a></div> &nbsp;  &nbsp; <div class="showNext" style="display: none"><a href="javascript:user('next')">Next</a><div>
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
							<th>Group Name</th>
							<th>Role</th>
              <th>Action</th>
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
    
    <!-- ********** Edit User X To Group Y with Role Z********** -->
    <div id="XYZ" class="XYZ" style="display: none">
      <fieldset style="background: #F5F5E0">
        <legend>Manipulation</legend>
        <a style="float: right" href="javascript:help()" id="manipulationHelp">Help</a>
        <p>
        <table>
          <thead>
          <tr>
            <th>Action</th>
            <th>Username</th>
            <th>Group</th>
            <th>Roles</th>
            <th>Approved</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>
              <select id="action">
                <option value="ADD">Add</option>
                <option value="EDIT">Edit</option>
                <option value="REMOVE">Remove</option>
              </select>
            </td>
            <td>             
              <input alt="" type="text" class="text" id="manipulationUsers" value="" style="width: 175px"/>
            </td>
            <td>
              <select id="manipulationGroups">
              </select>
            </td>
            <td id="doublecheck" class="doublecheckend">
              <select id="manipulationRoles" name="Roles" multiple="multiple">
                <option>user</option>
                <option>default</option>
                <option>none</option>
                <option>publisher</option>
                <option>admin</option>
                <option>super</option>
              </select>
            </td>
            <td>
              <select id="manipulationApproved">
                <option>true</option>
                <option>false</option>
              </select>
            </td>
          </tr>
          </tbody>
        </table>
        <input type="button" id="manipulationSubmit" value="Submit" alt="" onclick="javascript:manipulationSubmit()" style="float: left"/>
        <form action"adminview">
          <input type="submit" value="Cancel" alt="" style="float: left"/>
        </form>  
        <p>
      </fieldset>
    </div>
    <!-- ********** *********** *********** -->
  
  </div>
</div>
