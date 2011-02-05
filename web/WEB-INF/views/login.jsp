<%@ include file="/WEB-INF/views/common/include.jsp" %>

<html>

<head>
	<style type="text/css">
	    body { font-family: "Lucida Grande","DejaVu Sans","Bitstream Vera Sans",Verdana,Arial,sans-serif; color: #333; background: #fff; }
		.panel { background: #e5ecf9; border: 1px solid #417690; text-align: center; padding:10px; }
		.button { background: #417690; padding: 3px 5px; color: #FFFFFF;border: 1px solid #bbb; border-color: #ddd #aaa #aaa #ddd; }
		.error { text-align: center; color: #FF0000; }
	</style>
</head>

<body>
	
	<!-- retrieve current user status -->
	<sec:authentication property="principal" var="principal"/>
	<!-- retrieve openid cookie -->
	<c:set var="openid_cookie" value="esgf.idp.cookie"/>
	
	<p/>
	<c:choose>
	
		<c:when test="${principal=='anonymousUser'}">
			
			<!-- User is not authenticated -->
			<table align="center">
				<!-- authentication error -->
				<tr>
					<td colspan="2" align="center">
						<c:if test="${param['failed']==true}">
							<span class="error">Error: unable to resolve OpenID identifier.</span>
						</c:if>
					</td>
				</tr>
				<tr>
					<td>
					    <!-- the value of the action attribute must be the same as the URL intercepted by the spring security filter  -->
						<form name="loginForm" action='<c:url value="/j_spring_openid_security_check"/>' >					
							<div class="panel">
								<table border="0" align="center">
									<tr>
										<td>
											<img src='<c:url value="/images/openid.png"/>' width="80" hspace="10px"/>
										</td>
										<td>
											<table>
											<tr>
										<td align="right" class="required">Openid:</td>
										<td align="left"><input type="text" name="openid_identifier" size="80" value="${cookie[openid_cookie].value}"/ ></td>
										<td><input type="submit" value="LOGIN" class="button"/></td>
									</tr>
									<tr>
										<td align="center" colspan="3">
											<input type="checkbox" name="remember_openid" checked="checked" /> <span class="highlight">Remember my OpenID</span> on this computer
										</td>
									</tr>		
									</table>
									</td>
									</tr>
									
								</table>
							</div>
						</form>
						<p/>Please enter your OpenID. You will be redirected to your registration web site to login.
					</td>
				</tr>
			</table>
		</c:when>
		
		<c:otherwise>
			<!-- User is authenticated -->
			<table align="center">
				<tr>
					<td align="center">
						<div class="panel">
						    <img src='<c:url value="/images/openid.png"/>' align="top" height="20px" /> Your OpenID: <b><c:out value="${principal.username}"/></b>
							<form name="logoutForm" action='<c:url value="/j_spring_security_logout"/>'>
								<p/>Thanks, you are now logged in.
								<p/><input type="submit" value="LOGOUT" class="button"/>
							</form>
						</div>
					</td>
				</tr>
			</table>
		</c:otherwise>
		
    </c:choose>


</body>
</html>