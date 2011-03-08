<%@ include file="/WEB-INF/views/common/include.jsp" %>

<sec:authentication property="principal" var="principal"/>

<!-- left tabs -->
<div class="span-15 left">
        <span class="tabitem"> <a href="<c:url value='/'/> ">Home</a></span>
        <span class="tabitem" id="search"> <a href="<c:url value='/live'/> ">Search</a></span>
        <span class="tabitem" id="facet"> <a href="#" rel="#facet_overlay">Browse</a></span>
        <span class="tabitem"><a href="#">Analysis</a></span>

</div>

<!-- right tabs -->
<div class="prepend-1 span-8 last">
        <span class="tabitem"> <a href="#">Search Settings</a></span>
        <c:choose>
            <c:when test="${principal=='anonymousUser'}">
                   <span class="tabitem"> <a href="<c:url value='/login'/>" >Login</a></span>
               </c:when>
               <c:otherwise>
                   <span class="tabitem"> <a href="<c:url value='/j_spring_security_logout'/>" >Logout</a></span>
               </c:otherwise>
           </c:choose>
        <span class="tabitem"> <a href='<c:url value="/createAccount"/> '>Create Account</a></span>
</div>

<hr/>