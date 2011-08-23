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
               <li id="accounts"><a href="<c:url value='/accountsview'/> ">Account</a></li>     
               -->         
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

		    
