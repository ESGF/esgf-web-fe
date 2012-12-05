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
<%-- 					<form name="loginForm"> --%>
						<script language="javascript">
							function sanitize() {
								openidElement = document.getElementById("openid_identifier");
								openid = openidElement.value;
								openid = openid.replace("http:","https:")
								               .replace(/^\s\s*/, '').replace(/\s\s*$/, '');
								openidElement.value = openid;
								
								
								var credential_controller_url = '/esgf-web-fe/credential_proxy';
								
								var queryStr = {'openid' : openid};
								
						        jQuery.ajax({
						        	  url: credential_controller_url,
						        	  query: queryStr,
						        	  async: false,
						        	  type: 'GET',
						        	  success: function(data) {   

						        		  ESGF.localStorage.remove('GO_Credential',data.credential['openid_str']);
						        		  
						        		  
						        		  ESGF.localStorage.put('GO_Credential',data.credential['openid_str'],data.credential['credential_str']);
						        		  
						        		  ESGF.localStorage.printMap('GO_Credential');

										// alert('openid: ' + data.credential['openid_str'] + ' credential: ' + data.credential['credential_str']);
						        	  },
						          	  error: function() {
						          		  // alert('error in getting globus online credential');
						          	  }
						        });
								

								
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
	                                 	<input type="text" name="openid_identifier" alt="openid text" id="openid_identifier" size="60" value="${cookie[openid_cookie].value}" style="width:100%"/ >
	                                 </td>
	                                <td><input type="submit" alt="openid submit" value="Login" class="button" onclick="javascript:sanitize()"/></td>
	                            </tr>
	                            <tr>
	                                <td>&nbsp;</td>
	                                <td align="center" colspan="2">
	                                    <input type="checkbox" name="remember_openid" checked="checked" alt="openid checkbox" /> <span class="strong">Remember my OpenID</span> on this computer
	                                </td>
	                            </tr>
	                       </table>
	                                   
	                    </div>
	                </form>
	                <p/>
	                <div align="center">Please enter your OpenID. You will be redirected to your registration web site to login.</div>
	                <p/>
	                <div align="center">Not a user? Register <a href='<c:url value="/createAccount"/>' >here</a>.</div>
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
