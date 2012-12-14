<%@ include file="/WEB-INF/views/common/include.jsp" %>


<h2>Experimental GeoSpatial Search:</h2>

<form method="post" id="search-form" class="form">


    <!-- create hidden facet fields, set value to search constraint if available -->                        
    <c:forEach var="entry" items="${search_output.facets}">
        <input type="hidden" alt="hidden facet"
            name="${entry.key}" value="${search_input.constraints[entry.key][0]}"/>
    </c:forEach>


    <!--  More hidden values -->
    <input type="hidden" name="offset" value="${search_input.offset}" alt="hidden offset"/>
    <input type="hidden" name="limit" value="${search_input.limit}" alt="hidden limit" />

    
    <!--  Search Box -->
    <table>
		<tr align="center">
			<td>
			<input class="searchbox" name="text" type="text"
				size="70" value="${search_input.text}" alt="search text" /></td>	
	        <!--  	
	        <td valign="middle">    	        
	        <input type="image" 
	           src="<c:url value="/images/search.png" />" height="34px" alt="search text"/>
	    	</td>
	    	-->
	    	
	    	<td> <input class="button" type="submit" value="Search" alt="search submit" />
	
		</tr>
		
		<tr>
		     <td>
                <div class="inline" id="showOptions">
                 <a href="#">Show Options</a></div>
                 
                 <div class="tooltip">
                 <b>Define:</b><p class="legend" />
                 
                 - Search scope <br/>
                 - Geo-spatial constraints <br/>
                 - Temporal properties <br/>
                 
                 </div>
                <!--  
                <div class="inline" id="showConstraints"><a href="#">Show Constraints</a></div>             
                
                <div class="tooltip">
                 <b>Not implemented yet</b><br/>
                 This is to show: <br/> 
                 - facets contraints <br/>
                 - Geo-spatial constraints <br/>
                 - Temporal constraints <br/>
                 </div>
                -->
                
                <div class="inline" id="showReset"><a href="#">Reset</a></div>             
                <div class="tooltip">
                Clear all current search
                constraints. 
                </div>
            </td>
        </tr>
		
    </table>


    <div id="search_constraints" style="display:none"> </div>

 	<div id="optionPane" style="display:none">

		<div id="dataScope"><label for="dataScope"> Scope: </label>
		<div>
			<input type="radio" name="modelGroup" value="model" alt="model radio" /> Model 
			<input type="radio" name="modelGroup" value="obs" alt="obs radio" /> Observational 
			<input type="radio" name="modelGroup" value="all" alt="all radio" Checked/> All
		</div>
	</div>
	
	
	
		
	<div id="withMap"><label for="dataMap">Show Map: </label>
	  <input type="checkbox" name="mapGroup" value="2dmap" alt="map checkbox" /> 2D 
	</div>


    <!--  Show Map Canvas -->
    <div id="mapPane" style="display:none">
    
    <div id="map_canvas"> </div>
    <div class="tooltip">
    <p class="legend"> Tips </p>
    - Click mouse to pin a marker. <br/>
    - Enter address to pin a marker <br/>
    - Drag marker on the map. <br/>
    </div>
    
    <div id="map_info"> 

	<fieldset id="area1">
    	<p class=legend> Search Type: </p>
    
	    <input type="radio" name="searchType" value="Encloses" checked alt="encloses radio" /> Encloses 
	    <input type="radio" name="searchType" value="Overlaps" alt="overlaps radio" /> Overlaps 
       
    </fieldset>


    <fieldset id="marker_fieldset">
    
	    <p class=legend> Enter address: </p>
	    <div id="geoloc">    
	    	<input type="text" name="location" size="20" alt="address text" /> <br />
	    </div>
	    
	    <input type="button" name="clear_markers" value="Clear Markers" alt="clear submit" />
	   
	    <div id="markers" style="display:none"></div>
    
    </fieldset>
    

    <fieldset id="area">
    	<p class=legend> Define Area: </p>
    
	    <input type="radio" name="areaGroup" value="square" alt="square radio" /> Square 
	    <input type="radio" name="areaGroup" value="circle" alt="circle raio" /> Circle 
	    
    	<div id="circleInputs" style="display:none">
	        <label> Radius (km):</label><input type="text" name="radius" size="3" value="5" alt="radius text" />
	        <label> Quality:</label><input type="text" size="3" name="quality" value="40" alt="quality text" />
	     	<br />  
     		<input type="button" name="redraw_circle" value="Redraw" alt="redraw submit" />
     
    	</div>
    
    	<div id="areaSelected" style="display:none"></div>
       
    </fieldset>
    
    <p class="help"> Help: first define the points of interest by putting markers on
    the map; if you select square option, then we will try
    to fit a square with all markers in it; if you select circle option, then 
    you will be asked for a radius and quality.
    </p>
    
    
    
    
    
     <input type="hidden" alt="west_degrees hidden"
            name="west_degrees"/>
 		
     <input type="hidden" alt="east_degrees hidden"
            name="east_degrees"/>
            
 	 <input type="hidden" alt="south_degrees hidden"
            name="south_degrees"/>
 		
     <input type="hidden" alt="north_degrees hidden"
            name="north_degrees"/>
            
     <input type="hidden" alt="BoundingBox hidden"
     		name="whichGeo" value="BoundingBox" /> 
           
    </div> 
    
    <!--  #mapinfo -->
    
    </div> <!--  #mapPane -->
    
</div> <!-- #optionPane -->
    

</form>

<!-- Form errors -->
<c:if test="${error_message!=null}">
    <div class="error"><c:out value="${error_message}"/></div>
</c:if>

                      
                     