<%@ include file="/WEB-INF/views/common/include.jsp" %>
<%@ include file="/WEB-INF/views/common/accountsContentJavaScript.jsp" %>

<!--  header info -->
<div class="container">
	<div class="prepend-3 span-18 append-3">
     <div class="userInfo">
		<fieldset style="background: #F5F5E0">
			<legend>About</legend>
			<p>
				<label for="username">Username:</label>
				${accounts_userinfo.userName}			
			</p>
			<p>
				<label for="lastname">Last Name:</label>
				${accounts_userinfo.lastName}
				&nbsp;
				<label for="middleaame">Middle Name:</label>
				${accounts_userinfo.middleName}
				&nbsp;
				<label for="firstname">First Name:</label>
				${accounts_userinfo.firstName}				
			</p>
			<p>
				<label for="email">Email Address:</label>
				${accounts_userinfo.emailAddress}
			</p>
			<p>
				<label for="organization">Organization:</label>	
				${accounts_userinfo.organization}
			</p>		
			<p>
				<label for="city">City:</label>	
				${accounts_userinfo.city}
        &nbsp;
				<label for="state">State:</label>				
				${accounts_userinfo.state}
        &nbsp;
				<label for="country">Country:</label>				
				${accounts_userinfo.country}
			</p>
			<p>			
				<label for="openid">Openid:</label>
				${accounts_userinfo.openId}
			</p>
			<!-- /* Never entered by a user */
      <p>
				<label for="DN">Domain Name:</label>
				${accounts_userinfo.DN}
			</p>
      -->
      <input alt="" id="userUpdate" type="submit" value="Edit" class="button" onclick="javascript:editUserInfo()"/> 
		</fieldset>
    </div>
      
    <div class="editUserInfo" style="display:none">
    <fieldset style="background: #F5F5E0">
			<legend>About</legend>
        Last Name:    <input alt="" type="text" class="text" id="lastName" value="${accounts_userinfo.lastName}"/> <br/>
        Middle Name:  <input alt="" type="text" class="text" id="middleName" value="${accounts_userinfo.middleName}"/><br/>
        First Name:   <input alt="" type="text" class="text" id="firstName" value="${accounts_userinfo.firstName}"/><br/>
        Email:        <input alt="" type="text" class="text" id="email" value="${accounts_userinfo.emailAddress}"/><br/>
        Organization: <input alt=""  type="text" class="text" id="organization" value="${accounts_userinfo.organization}"/><br/>
        City:         <input alt="" type="text" class="text" id="city" value="${accounts_userinfo.city}"/><br/>
        State:        <input alt="" type="text" class="text" id="state" value="${accounts_userinfo.state}"/><br/>
        Country:      <input alt="" type="text" class="text" id="country" value="${accounts_userinfo.country}"/><br/>
      <input alt="" id="userUpdate" type="submit" value="Submit" class="button" onclick="javascript:submitUserInfo('${accounts_userinfo.userName}')"/> 
      <input alt="" id="cancelUpdate" type="submit" value="Cancel" class="button" onclick="javascript:cancelUserInfo()"/> 
		</fieldset>

    </div>

    <div class="top" id="top"> </div>  

		<fieldset style="background: #F5F5E0">
			<legend>Groups Registered</legend>
				<table id="groups_admin_table_id">
					<thead>
						<tr>
							<th>Name</th>
							<th>Description</th>
							<th>Role</th>
              <th></th>
						</tr>
					</thead>
					<tbody class="updatable">
          </tbody>					
				</table>
				        <c:set var="j" value="0"/>
				        <c:forEach var="group" items="${accounts_groupinfo}">
                  <script language="javascript">
                    var className = "${accounts_groupinfo[j].name}";
                    var classId = className.replace(/\s/g,"");
                    var group = "${accounts_groupinfo[j].name}";
                    var info = "${accounts_groupinfo[j].description}";
                    var role = "${accounts_roleinfo[j]}";
                    var autoReg = "t";
                    if(group != "wheel"){
                      $('.updatable').append('<tr class="' + classId + '"><td>' + group + '</td><td>' + info + '</td><td>' + role + '</td><td></td></tr>');
                      // unregister button
                      //<input alt="" id="' + group + '" type="submit" value="Leave" class="button" onclick="javascript:unregister(\'${accounts_userinfo.userName}\', \'' + group + '\', \'' + info + '\', \'' + role + '\', \'' + autoReg + '\')"/>                    
                    }
                  </script>

				          <c:set var="j" value="${j+1}"/>
						</c:forEach>
      <div class="loading"> <input alt="" id='showMore' type="submit" value="Show All" class="button" onclick="javascript:showmore('${accounts_userinfo.openId}')"/> </div>
      <div class="loaded" style="display: none"> <input alt="" id='showMore' type="submit" value="Show All" class="button" onclick="javascript:showmoregroups()"/> </div>
		</fieldset>

    <div class="middle" id="middle"></div>

    <div class="error" style="display: none"></div>
		<div class="success" style="display: none"></div>
    
    <div class="groups" style="display: none">
    <fieldset style="background: #F5F5E0">
      <legend>Groups Available</legend>
        <strong>Federation Wide</strong> 
        <table id="groups_table_id">
          <thead><tr><th>Name</th><th>Description</th><th>Role</th><th>Register</th></tr></thead>
         <tbody class="allgroups">
         </tbody>
        </table>
        <input alt="" id="showMore" type="submit" value="Hide" class="button" onclick="javascript:showless()"/> 
         &nbsp; Need help registering for other groups? Check the ESGF
         <a href="http://www.esgf.org/wiki/ESGF_Data_Download">Wiki</a>
    </fieldset>
  </div>

  <div class="bottom" id="bottom"> </div>

  <fieldset style="background: #F5F5E0">
			<legend>Change Password</legend>
        <label for="oldpasswd">Old password:</label>
        <input alt="" id="oldpasswd" name="oldpasswd" type="password"/><br/>
 				<label for="password1">New password:</label>
 				<input alt="" id="password1" name="password1" type="password"/><br/>
 				<label for="password2">Confirm password:</label>
 				<input alt="" id="password2" name="password2" type="password"/><br/>
				<input alt="" id="changepwd" value="Change password" class="button" type="button" onclick="javascript:changePassword('${accounts_userinfo.userName}')"/>
		</fieldset>
		 
	</div>
</div>

<script language="javascript">
  window.onload = errorCheck;
  
  function errorCheck(){
    var error = "${accounts_error}";
    if (error == "false"){
      //do nothing page will load normal
    }
    else{
      window.location="/"
    }
  }
</script>
