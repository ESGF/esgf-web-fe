<!-- Search facets accordions -->
<%@ include file="/WEB-INF/views/common/include.jsp" %>

<h2> Available Facets</h2>


<ul class="menu noaccordion">

<c:if test="${search_output.facets != null}">

    	
	<c:forEach var="entry" items="${search_output.facets}">
       
       <c:if test="${fn:length(entry.value.subFacets)>0}">
				    
	   <li><a href="#">
	      <c:out value="${facet_profile.topLevelFacets[entry.key].label}"/>
	   </a>

       <ul class="acitem">
   
         <c:forEach var="subFacet" items="${entry.value.subFacets}">
             <li> <a href='javascript:setFacet("${entry.key}","${subFacet.label}")'> 
             <c:out value="${subFacet.label}"/> 
                (<c:out value="${subFacet.counts}"/>) </a>
             </li>
         </c:forEach>
       </ul>                           
       </li>
       
       </c:if> 
	</c:forEach>
	
</c:if>

</ul>

<a id="facet_reset" href="javascript:window.location='.' "> Reset Facets </a>
<div class="tooltip">
Reset current selection of facets, effectively refresh 
and display all available categories 
</div>