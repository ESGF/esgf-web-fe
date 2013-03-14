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
              <td><a href="javascript:user()" id="users">Manage Users</a></td>
              <td><a href="javascript:group()" id="groups">Manage Groups</a></td>
              <td><a href="javascript:pending()" id="pendings">Pending Request</a></td>
            </tr>
          </table>
        </p>
        
<!--    
        <p>
          <ul style="list-style-type: none;">                    
	          <li><a href="<c:url value='/usermanagement'/> ">Manage Users</a></li>  
	          <li><a href="<c:url value='/creategroups'/> ">Manage Groups</a></li>
          </ul>
        </p>
-->

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
  
  </div>
</div>

<script language="javascript">

</script>
