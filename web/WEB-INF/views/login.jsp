<%@ include file="/WEB-INF/views/common/include.jsp" %>
<%@ include file="/WEB-INF/views/common/logInJavaScript.jsp" %>

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
<%-- 					<form name="loginForm"> --%>
	                    <div class="panel">  	                         	
	                    	<c:if test="${param['failed']==true}">
	                    		<span class="myerror">Error: unable to resolve OpenID identifier.</span>
	                		</c:if>                           
	                        <table border="0" align="center">
	                            <tr>
	                                <td align="right" class="required"><b>Openid:</b></td>
	                                <td align="left" style="width:100%">
	                                 	<input alt="openid_identifier" type="text" name="openid_identifier" alt="openid text" id="openid_identifier" size="60" value="${cookie[openid_cookie].value}" style="width:100%"/ >
	                                 </td>
	                                <td><input alt="" type="submit" alt="openid submit" value="Login" class="button" onclick="javascript:sanitize()"/></td>
	                            </tr>
	                            <tr>
	                                <td>&nbsp;</td>
	                                <td align="center" colspan="2">
	                                    <input alt="remember_openid" type="checkbox" name="remember_openid" checked="checked" alt="openid checkbox" /> <span class="strong">Remember my OpenID</span> on this computer
	                                </td>
	                            </tr>
	                       </table>
	                                   
	                    </div>
	                </form>
	                <p/>
	                <div align="center">Please enter your OpenID. You will be redirected to your registration web site to login.</div>
                  <p/>
                  
                  <div align="center">Not a user? Register <a href='<c:url value="/createAccount"/>' >here</a>.</div>
                  
                  <div align="center">Forgot Openid? Click <a href="javascript:toggle()" id="user">here</a>.</div>
                  <div align="center" id="username" style="display:none">
                    <div class="panel">
                      <p> Please provide the email associated with the forgotten openid.</p>
                      <table><tr>
                        <td><b>Email:</b></td>
                        <td><input alt="usnemail" type="text" id="usnemail" alt="user email" name="usnemail" size="60" style="width:100%" /></td>
                        <td><input alt="" type="submit" value="Submit" alt="submit user email" class="button" onclick="javascript:findusername()"/></td>
                      </tr></table>
                  </div>
                </div>

               <div align="center">Forgot Password? Click <a href="javascript:roggle()" id="pass">here</a>.</div>
                  <div align="center" id="password" style="display:none">
                    <div class="panel">
                      <p>Please provide your openid. You will recieve a temporary password by email.<br/>Please remember to change your password the next time you login.</p>
                      <table><tr>
                          <td><b>Openid:</b></td>
                          <td> <input alt="pwdopenid" type="text" id="pwdopenid" alt="user openid" name="pwdopenid" size="60" style="width:100%" /></td>
                          <td><input alt="" type="submit" value="Submit" alt="submit user openid" class="button" onclick="javascript:findpassword()"/></td>
                    </tr></table>
                    </div>
                  </div>

                  <div class="error" align="center" style="display: none"></div>
		              <div class="success" align="center" style="display: none"></div>

            <p/>
                </c:when>

                <c:otherwise>
                  <c:redirect url="/accountsview"/>
					        
                  <h1>ESGF Logout</h1>
                    <!-- User is authenticated -->
                    <table align="center">
                        <tr>
                            <td align="center" valign="top">
                                <div class="panel">
                                    Thanks, you are now logged in.
                                    <!-- <p/><span class="openidlink">&nbsp;</span> Your OpenID : <b><c:out value="${principal.username}"/></b> -->
                                    <form name="logoutForm" action='<c:url value="/j_spring_security_logout"/>'>
                                        <input alt="" type="submit" value="Logout" class="button"/>
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
