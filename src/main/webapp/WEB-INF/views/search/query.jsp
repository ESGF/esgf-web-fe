<%@ include file="/WEB-INF/views/common/include.jsp" %>


<h2>Experimental GeoSpatial Search:</h2>

<form method="post" id="search-form" class="form">


    <!-- create hidden facet fields, set value to search constraint if available -->                        
    <c:forEach var="entry" items="${search_output.facets}">
        <input type="hidden" 
            name="${entry.key}" value="${search_input.constraints[entry.key][0]}"/>
    </c:forEach>

    <!--  More hidden values -->
    <input type="hidden" name="offset" value="${search_input.offset}" />
    <input type="hidden" name="limit" value="${search_input.limit}" />

    
    <!--  Search Box -->
    
    <table>
	<tr align="center">
		<td><input id="searchbox" name="text" type="text"
			size="50" value="${search_input.text}" /></td>
		<td> <div id="showOptions"><a href="#">More Options</a></div>
		</td>
	</tr>
    </table>
 


    <div id="optionPane" style="display:none">

		<div id="dataScope"><label for="dataScope"> Scope: </label>
		<div>
		<input type="radio" name="modelGroup" value="model" /> Model 
		<input type="radio" name="modelGroup" value="obs" /> Observational 
		<input type="radio" name="modelGroup" value="all" Checked/> All
		</div>
		</div>
		
		<div id="withMap"><label for="dataMap">Show Map: </label>
		<div>
		  <input type="radio" name="mapGroup" value="2dmap" /> 2D 
		  <input type="radio" name="mapGroup" value="3dmap" /> 3D 
		</div>
		</div>


    
    <!--  Show Map Canvas -->
    <div id="mapPane" style="display:none">
            <div id="map_canvas"> </div>
            <div id="map_info"> </div> 
    
    </div>
    
    </div> <!--  end of optionPane -->
    
    
     
    <div id="submitBtn">
        <input type="submit" value="Search" />
    </div>

    <c:out value="${search_input.text}"/>
	<c:out value="${search_input.constraints}"/>
	

</form>

<!-- 
<form method="post" id="search-form2" class="form">
    <input id="searchbox2" name="text2" type="text"
			size="50" value='${search_input.offset}' />
			
			
</form>
-->

<!-- Form errors -->
<c:if test="${error_message!=null}">
    <div class="error"><c:out value="${error_message}"/></div>
</c:if>

                      
                       
  