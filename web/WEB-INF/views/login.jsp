<%@ include file="/WEB-INF/views/common/include.jsp" %>



<ti:insertDefinition name="main-layout" >

    <ti:putAttribute name="extrastyle">
        <link rel="stylesheet" href='<c:url value="/styles/security.css" />' type="text/css">
    </ti:putAttribute>

	<ti:putAttribute name="extrascript"  value="/WEB-INF/views/login/script_search.jsp" >
	</ti:putAttribute>
        

    <ti:putAttribute name="main">
        <div class="prepend-4 span-16">

            <!-- retrieve current user status -->
            <sec:authentication property="principal" var="principal"/>
            <!-- retrieve openid cookie -->
            <c:set var="openid_cookie" value="esgf.idp.cookie"/>

            <c:choose>
                <c:when test="${principal=='anonymousUser'}">
                  
                    <!-- User is not authenticated -->
                    <p/>
	             	<h1>ESGF Login</h1>
	                                        
				    <!-- the value of the action attribute must be the same as the URL intercepted by the spring security filter  -->
	                <form name="loginForm" action='<c:url value="/j_spring_openid_security_check"/>' >
						<script language="javascript">
							function sanitize() {
								openidElement = document.getElementById("openid_identifier");
								openid = openidElement.value;
								openid = openid.replace("http:","https:")
								               .replace(/^\s\s*/, '').replace(/\s\s*$/, '');
								openidElement.value = openid;
              }

						</script>															    				
	                    <div class="panel">  	                         	
	                    	<c:if test="${param['failed']==true}">
	                    		<span class="myerror">Error: unable to resolve OpenID identifier.</span>
	                		</c:if>                           
	                        <table border="0" align="center">
	                            <tr>
	                                <td align="right" class="required"><b>Openid:</b></td>
	                                <td align="left" style="width:100%">
	                                 	<input type="text" name="openid_identifier" id="openid_identifier" size="60" value="${cookie[openid_cookie].value}" style="width:100%"/ >
	                                 </td>
	                                <td><input type="submit" value="Login" class="button" onclick="javascript:sanitize()"/></td>
	                            </tr>
	                            <tr>
	                                <td>&nbsp;</td>
	                                <td align="center" colspan="2">
	                                    <input type="checkbox" name="remember_openid" checked="checked" /> <span class="strong">Remember my OpenID</span> on this computer
	                                </td>
	                            </tr>
	                       </table>
	                                   
	                    </div>
	                </form>
	                <p/>
	                <div align="center">Please enter your OpenID. You will be redirected to your registration web site to login.</div>
                  <p/>
                  
                  <div align="center">Not a user? Register <a href='<c:url value="/createAccount"/>' >here</a>.</div>
                  
                  <div align="center"><a href="javascript:toggle()" id="user">Forgot Openid</a></div>
                  <div align="center" id="username" style="display:none">
                    <div class="panel">
                      <p> Please provide the email the misisng openid is associated with. You will recieve a email with all the openid's that are associated with this email.</p>
                      <table><tr>
                          <td><b>Email:</b></td>
                          <td><input type="text" id="usnemail" name="usnemail" size="60" style="width:100%" /></td>
                          <td><input type="submit" value="Submit" class="button" onclick="javascript:findusername()"/></td>
                      </tr></table>
                    </div>
                  </div>
                  
                  <div align="center"><a href="javascript:roggle()" id="pass">Forgot Password</a></div>
                  <div align="center" id="password" style="display:none">
                    <div class="panel">
                      <p>Please provide the email associated with this usernames. You will recieve a temporary password by email. Please remember to change your password the next time you login.</p>
                      <table><tr>
                          <td><b>Email:</b></td>
                          <td> <input type="text" id="pwdemail" name="pwdemail" size="60" style="width:100%" /></td>
                          <td><input type="submit" value="Submit" class="button" onclick="javascript:findpassword()"/></td>
                    </tr></table>
                    </div>
                  </div>
                  
                  <script language="javascript"> 
                    // MBH: start of retrieve username or password
                    function toggle() {
	                    var usr = document.getElementById("username");
	                    if(usr.style.display == "block") {
    		                usr.style.display = "none";		
  	                  }
	                    else {
		                    usr.style.display = "block";
	                    }
                    } 

                    function roggle() {
	                    var pwd = document.getElementById("password");
	                    if(pwd.style.display == "block") {
    		                pwd.style.display = "none";		
  	                  }
	                    else {
		                    pwd.style.display = "block";
	                    }
                    }

                    function findusername() {
                      var email = document.getElementById("usnemail").value;
                      alert(email);
                    }

                    function findpassword() {
                      var email = document.getElementById("pwdemail").value;
                      alert(email);
                    }
                  </script>
                </c:when>

                <c:otherwise>

					<p/>
                    <h1>ESGF Logout</h1>

                    <!-- User is authenticated -->
                    <table align="center">
                        <tr>
                            <td align="center" valign="top">
                                <div class="panel">
                                    Thanks, you are now logged in.
                                    <!-- <p/><span class="openidlink">&nbsp;</span> Your OpenID : <b><c:out value="${principal.username}"/></b> -->
                                    <form name="logoutForm" action='<c:url value="/j_spring_security_logout"/>'>
                                        <input type="submit" value="Logout" class="button"/>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </table>
                </c:otherwise>

            </c:choose>

        </div>
    </ti:putAttribute>

</ti:insertDefinition>
