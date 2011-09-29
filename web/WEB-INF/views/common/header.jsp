<%@ include file="/WEB-INF/views/common/include.jsp" %>

<sec:authentication property="principal" var="principal"/>

<div class="span-24 last headertop" style="border-top: 3px solid #18638a;padding-top: 5px;padding-bottom: 5px">
	<!-- <div class="span-24"></div> -->
	<div class="span-19 prepend-1">
		<img src='<c:url value="/images/esgf.png"/>' height="92px" />
	</div>
	<div class="span-4 last">
		<c:set var="logo"><spring:message code="esgf.homepage.institutionLogo" /></c:set>
		<img src='<c:url value="${logo}" />' style="padding-top:10px;" alt="institution" title="institution icon"/>
	</div>
</div>
<div class="span-24 last headerbottom" style="background-color:#18638a">
	<div class="span-24 navbar last">
       <div>                
        <input type="hidden" name="offset" value="${search_input.offset}" />
    
           <ul>                    
               <li><a href="<c:url value='/'/> ">Home</a></li>
               <li id="search"><a href="<c:url value='/live'/> ">Search</a></li>                
               <!-- <li id="facet"> <a href="#" rel="#facet_overlay">Browse</a></li>   -->  
               <!--  
               <li><a href="#">Analysis</a></li>
               <li><a href="#">Search Settings</a></li>  
               -->      
           	<c:choose>
           		<c:when test="${principal=='anonymousUser'}">
           			<!-- guest users -->
                	<li> <a href="<c:url value='/login'/>" >Login</a></li>
              	</c:when>
              	<c:otherwise>
              		<!-- authenticated users -->
              		<li id="accounts"><a id="accountsAnchor" onclick="checkAccountsviewURL('${principal.username}')" href="<c:url value="/accountsview"/> ">Account</a></li>     
                	<li><a href="<c:url value='/j_spring_security_logout'/>" >Logout</a></li>
                	<!-- admin users -->
                	<sec:authorize access="hasRole('ROLE_ADMIN')">
              			<li> <a href="<c:url value='/adminview'/>" >Admin</a></li>
              		</sec:authorize>
              	</c:otherwise>
          	</c:choose>                                      
           </ul>            
       </div>
   </div> 
</div>

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



		    
