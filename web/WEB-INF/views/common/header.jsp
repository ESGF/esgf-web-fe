<%@ include file="/WEB-INF/views/common/include.jsp" %>



<sec:authentication property="principal" var="principal"/>

<div class="span-24 last headertop" style="border-top: 3px solid #18638a;padding-top: 5px;padding-bottom: 5px">
	
	<span id="modelName" style="display:none">${Model_Name}</span>
	<span id="datacartOpen" style="display:none">${Datacart_Open}</span>
	
	
	<c:choose>
	 	<c:when test="${principal=='anonymousUser'}">
			<span id="principal_username" style="display:none">${principal}</span>
	 	</c:when>
	 	<c:otherwise>
			<span id="principal_username" style="display:none">${principal.username}</span>
	 	</c:otherwise>
	</c:choose>
	
			<!-- <span id="principal_username" style="display:none">${principal}</span> -->
	
	<!-- <div class="span-24"></div> -->
	<div class="span-11 prepend-1">
		<img src='<c:url value="/images/esgf.png"/>' height="92px" />
	</div>
	<div class="span-12 last">
		<c:set var="logo"><spring:message code="esgf.homepage.institutionLogo" /></c:set>
		<img src='<c:url value="${logo}" />' style="padding-top:5px;margin-right:15px;float:right" alt="institution" title="institution icon"/>
	</div>
</div>
<div class="span-24 last headerbottom" style="background-color:#18638a">
	<div class="span-24 navbar last">
       <div>                
        <input type="hidden" name="offset" value="${search_input.offset}" />
    
           <ul>                    
               <li><a href="<c:url value='/'/> ">Home</a></li>
               <li id="search"><a href="<c:url value='/live'/>">Search</a></li>                
               <li id="tools"> <a href="<c:url value='/tools/tools.htm'/>">Tools</a></li>  
           	<c:choose>
           		<c:when test="${principal=='anonymousUser'}">
           			<!-- guest users -->
                	<li class="resetLocalStorage"> <a href="<c:url value='/login'/>" >Login</a></li>
              	</c:when>
              	<c:otherwise>
              		<!-- authenticated users -->
              		<li id="accounts"><a id="accountsAnchor" onclick="checkAccountsviewURL('${principal.username}')" href="<c:url value="/accountsview"/> ">Account</a></li>     
                	<!--  
              		<li id="accounts"><a href="<c:url value='/accountsview'/> ">Account</a></li>     
                	<li class="resetLocalStorage"><a href="<c:url value='/j_spring_security_logout'/>" >Logout</a></li>
                	-->
                	<!-- admin users -->
                	<sec:authorize access="hasRole('ROLE_ADMIN')">
			<li id="dashboard"><a href="dashboard.jsp"/>Dashboard</a></li>
              		</sec:authorize>
                	<sec:authorize access="hasRole('ROLE_ADMIN')">
              			<li> <a href="<c:url value='/adminview'/>" >Admin</a></li>
              		</sec:authorize>
              		<li class="resetLocalStorage"><a href="<c:url value='/j_spring_security_logout'/>" >Logout</a></li>
                	
              	</c:otherwise>
          	</c:choose>                                      
           </ul>            
       </div>
   </div> 
</div>

<!-- display warning if JavaScript is not enabled -->
<noscript>
	<div class="span-24 last">
		<div class="error-box">Warning: please enable JavaScript in your browser preferences, otherwise this site will not work properly.</div>
	</div>
</noscript>


<script type="text/javascript">
function checkAccountsviewURL(openid)
{
	// get node name from openid (e.g. https://esgf-node3.llnl.gov/esfg-idp/openid/rootAdmin)
	// look for first '/' following https://... so start looking at position 8
	var dotIndex = openid.indexOf('.', 8);  // postion of first '.'
	var slashIndex = openid.indexOf('/',8); // position of first '/'
	var nodeName = openid.substr(8,dotIndex-8);
	
	// get hostname of this host
	var host = document.location.hostname;
	var dIndex = host.indexOf('.',0);
	var hBaseName = host.substr(0,dIndex);
	if (dIndex < 0) hBaseName = host;
	
	if (nodeName != hBaseName) {
		var openIdLink = "http://" + openid.substr(8,slashIndex-8);
		var msg = "You must logon to your openID node to view your account:\n"
				  + openIdLink;
		alert(msg);
		// make Account keep us where we are
		document.getElementById('accountsAnchor').href=document.URL;
	}
	
	// TODO: When we can get cookies from redirected url, then can actually redirect as below:
	// now build the accounts view url with substring from openid and '/esgf-web-fe/accountsview' 
	//var url = openid.substr(0,slashIndex).concat('/esgf-web-fe/accountsview');
	//document.getElementById('accountsAnchor').href=url;
	
}
</script>		

