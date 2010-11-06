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
			<td>
			<input class="searchbox" name="text" type="text"
				size="70" value="${search_input.text}" /></td>	
	        <!--  	
	        <td valign="middle">    	        
	        <input type="image" 
	           src="<c:url value="/images/search.png" />" height="34px"/>
	    	</td>
	    	-->
	    	
	    	<td> <input class="button" type="submit" value="Search" />
	
		</tr>
		
		<tr>
		     <td>
                <div class="inline" id="showOptions">
                 <a href="#">Show Options</a></div>
                 
                 <div class="tooltip">
                 <b>Define:</b><br/> 
                 
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
			<input type="radio" name="modelGroup" value="model" /> Model 
			<input type="radio" name="modelGroup" value="obs" /> Observational 
			<input type="radio" name="modelGroup" value="all" Checked/> All
		</div>
	</div>
		
	<div id="withMap"><label for="dataMap">Show Map: </label>
	  <input type="checkbox" name="mapGroup" value="2dmap" /> 2D 
	</div>


    <!--  Show Map Canvas -->
    <div id="mapPane" style="display:none">
    
    <div id="map_canvas"> </div>
    <div class="tooltip">
    <b>Tips:</b> <br/>
    - Mouse click to pin a marker. <br/>
    - Enter address to pin a marker <br/>
    - Drag marker on the map. </br>
    </div>
    
    <div id="map_info"> 

    <fieldset id="marker_fieldset">
    
	    <p class=legend> Enter address: </p>
	    <div id="geoloc">    
	    	<input type="text" name="location" size="20" /> <br />
	    </div>
	    
	    <input type="button" name="clear_markers" value="Clear Markers" />
	   
	    <div id="markers" style="display:none"></div>
    
    </fieldset>
    

    <fieldset id="area">
    	<p class=legend> Define Area: </p>
    
	    <input type="radio" name="areaGroup" value="square" /> Square 
	    <input type="radio" name="areaGroup" value="circle" /> Circle 
	    
    	<div id="circleInputs" style="display:none">
	        <label> Radius (km):</label><input type="text" name="radius" size="3" value="5" />
	        <label> Quality:</label><input type="text" size="3" name="quality" value="40" />
	     	<br />  
     		<input type="button" name="redraw_circle" value="Redraw" />
     
    	</div>
    
    	<div id="areaSelected" style="display:none"></div>
       
    </fieldset>
    
    <p class="help"> Help: first define the points of interest by putting markers on
    the map; if you select square option, then we will try
    to fit a square with all markers in it; if you select circle option, then 
    you will be asked for a radius and quality.
    </p>
    
     <input type="hidden" 
            name="west_degrees" value="-180"/>
 		
     <input type="hidden" 
            name="east_degrees" value="180"/>
            
 	 <input type="hidden" 
            name="south_degrees" value="-90"/>
 		
     <input type="hidden" 
            name="north_degrees" value="90"/>
            <!--
    </form>    
                        
    -->
    </div> <!--  #mapinfo -->
    
    </div> <!--  #mapPane -->
    
</div> <!-- #optionPane -->
    

</form>

<!-- Form errors -->
<c:if test="${error_message!=null}">
    <div class="error"><c:out value="${error_message}"/></div>
</c:if>

                      
                     