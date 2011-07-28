<%@ include file="/WEB-INF/views/common/include.jsp" %>

<!--  header info -->
<div class="span-24 last" style="margin-top:20px">
	<h2 style="text-align:center">
	Manage Groups
	</h2>
	
	<!-- group information table -->
	<div class="prepend-2 span-20 append-2 last">
		<table id="table_id">  

						<!-- Table header -->  

	        <thead>  
	            <tr>  
	                <th>Group Id</th>  
	                <th>Group Name</th>  
	                <th>Group Description</th>  
	            </tr>  
	        </thead>  
	        <tbody>   
		        <c:set var="j" value="0"/>
		        <c:forEach var="group" items="${CreateGroups_group}">
		        	<tr>
		        		<td>${CreateGroups_group[j].id}</td>
		        		<td>${CreateGroups_group[j].name}</td>
		        		<td>${CreateGroups_group[j].description}</td>
		        	</tr>
		            <c:set var="j" value="${j+1}"/>
		       	</c:forEach>
		    </tbody>
							       	
			<!--  					
	        <tbody>
	        	<tr>  
	                <td>MyGroup</td>  
	                <td>MyGroup Description</td> 
	            </tr>  
	        </tbody>
	        -->
	  </table>
	</div>
</div>


  					
<%--  
<sec:authentication property="principal" var="principal"/>

<div style="margin-top:20px;margin-bottom:20px;">
	<c:choose>
		<c:when test="${principal=='anonymousUser'}">
    		<div> <c:out value="${principal}"/> IS NOT AUTHORIZED TO VIEW THIS PAGE</div>
  		</c:when>
  		<c:otherwise>
      		<c:choose>
      			<c:when test="${principal.username=='https://pcmdi3.llnl.gov/esgcet/myopenid/jfharney'}">
  				</c:when>
  				<c:otherwise>
  					<div> <c:out value="${principal.username}"/> IS NOT AUTHORIZED TO VIEW THIS PAGE</div>
  				</c:otherwise>
      		</c:choose>
  		</c:otherwise>
	</c:choose>      
</div>
--%>
  				