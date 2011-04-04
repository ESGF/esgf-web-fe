<%@ include file="/WEB-INF/views/common/include.jsp" %>

<sec:authentication property="principal" var="principal"/>
<div class="span-24 last headertop">
	<div class="span-24"></div>
	<div class="span-19 prepend-1">
		<img src="images/newlogo.png" />
	</div>
	<div class="span-3 prepend-1 last">
		<img src="" style="padding-top:5px" alt="institution" title="institution icon"/>
	</div>
</div>
<div class="span-24 last headerbottom" style="background-color:#18638a">
	<div class="span-24 navbar last">
       <div class="nav">                
           <ul>                    
               <li><a href="<c:url value='/'/> ">Home</a></li>
               <li id="search"><a href="<c:url value='/live'/> ">Search</a></li>                
               <li id="facet"> <a href="#" rel="#facet_overlay">Browse</a></li>               
               <li><a href="#">Analysis</a></li>
               <li><a href="#">Search Settings</a></li>
        		<c:choose>
            		<c:when test="${principal=='anonymousUser'}">
                   			<li> <a href="<c:url value='/login'/>" >Login</a></li>
               		</c:when>
               		<c:otherwise>
                   		<li> <a href="<c:url value='/j_spring_security_logout'/>" >Logout</a></li>
               		</c:otherwise>
           		</c:choose>                                      
           </ul>                
       </div>
   </div> 
</div>

		    