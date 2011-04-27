<%@ include file="/WEB-INF/views/common/include.jsp" %>
<%@ include file="/WEB-INF/views/search/_facet_select_tbl.jsp" %>


<div class="span-6 last">

	<div class="l2contentleftbox">        
		<!--  current selection -->
		  
		<div class="round-box">
		    <div class="round-header round-top"> Current Selections</div> 
		    <div id="current-selection" class="round-content"></div>
		
		</div> 
	</div>      
	
</div>






<div class="span-6 last" style="padding-bottom:10px">
	<div class="l2contentleftbox"> 
		<ul class="ul_class" >
			<li class="li_class_title">
	   			Search Categories
			</li>
		</ul>
		 
		<table>
			<tbody id="facetList">
		
	    	</tbody>
		</table>
		
		<!--  
		<ul class="ul_class2" >
			<li class="li_class2_title">
	   			Search Categories
			</li>
		</ul>
		<table>
			<tbody id="facetList2">
		
	    	</tbody>
		</table>
		-->
		
	</div>
	
</div>


<!--  
<div class="span-12 last advMenu" style="padding-top: 50px;padding-bottom:50px" >

  
	<div class="l2contentleftbox">  
	
		<ul class="ul_class4" >
			<li class="li_class4_title">
	   			Search Categories
			</li>
		</ul>
		
		 
		<ul id="mainMenu_navi"> 
		 
			<li class="active" id="facet_project"> 
				<strong>project</strong> 
			</li> 
			<li id="facet_model"> 
				<strong>model</strong> 
			</li> 
			<li id="facet_experiment"> 
				<strong>experiment</strong> 
			</li> 
			<li id="facet_frequency"> 
				<strong>frequency</strong> 
			</li> 
			<li id="facet_realm"> 
				<strong>realm</strong> 
			</li> 
			<li id="facet_instrument"> 
				<strong>instrument</strong> 
			</li> 
			<li id="facet_variable"> 
				<strong>variable</strong> 
			</li> 
			<li id="facet_cf_variable"> 
				<strong>cf variable</strong> 
			</li> 
			<li id="facet_gcmd_variable"> 
				<strong>gcmd Variable</strong> 
			</li> 
		</ul> 
		
		
		<div id="mainMenu" style="display:none;"> 
			<div id="mainMenuPages"> 
				<div class="mainMenuPage"> 
					<div class="navi"></div> 
					<div class="scrollable"> 
						
						<div class="mainMenuItems"> 
							<div class="mainMenuItem"> 
								project facets page1
							</div> 
							<div class="mainMenuItem"> 
								project facets page2
							</div> 
						</div> 
					</div> 
				</div> 
				<div class="mainMenuPage"> 
					<div class="navi"></div> 
					<div class="scrollable"> 
						
						<div class="mainMenuItems"> 
							<div class="mainMenuItem"> 
								model facets page1
							</div> 
							<div class="mainMenuItem"> 
								model facets page2
							</div> 
						</div> 
					</div> 
				</div> 
				<div class="mainMenuPage"> 
					<div class="navi"></div> 
					<div class="scrollable"> 
						
						<div class="mainMenuItems"> 
							<div class="mainMenuItem"> 
								experiment facets page1
							</div> 
							<div class="mainMenuItem"> 
								experiment facets page2
							</div> 
						</div> 
					</div> 
				</div> 
				<div class="mainMenuPage"> 
					<div class="navi"></div> 
					<div class="scrollable"> 
						
						<div class="mainMenuItems"> 
							<div class="mainMenuItem"> 
								frequency facets page1
							</div> 
							<div class="mainMenuItem"> 
								frequency facets page2
							</div> 
						</div> 
					</div> 
				</div> 
				<div class="mainMenuPage"> 
					<div class="navi"></div> 
					<div class="scrollable"> 
						
						<div class="mainMenuItems"> 
							<div class="mainMenuItem"> 
								realm facets page1
							</div> 
							<div class="mainMenuItem"> 
								realm facets page2
							</div> 
						</div> 
					</div> 
				</div> 
				<div class="mainMenuPage"> 
					<div class="navi"></div> 
					<div class="scrollable"> 
						
						<div class="mainMenuItems"> 
							<div class="mainMenuItem"> 
								instrument facets page1
							</div> 
							<div class="mainMenuItem"> 
								instrument facets page2
							</div> 
						</div> 
					</div> 
				</div> 
				<div class="mainMenuPage"> 
					<div class="navi"></div> 
					<div class="scrollable"> 
						
						<div class="mainMenuItems"> 
							<div class="mainMenuItem"> 
								variable facets page1
							</div> 
							<div class="mainMenuItem"> 
								variable facets page2
							</div> 
						</div> 
					</div> 
				</div> 
				<div class="mainMenuPage"> 
					<div class="navi"></div> 
					<div class="scrollable"> 
						
						<div class="mainMenuItems"> 
							<div class="mainMenuItem"> 
								cf variable facets page1
							</div> 
							<div class="mainMenuItem"> 
								cf variable facets page2
							</div> 
						</div> 
					</div> 
				</div> 
				<div class="mainMenuPage"> 
					<div class="navi"></div> 
					<div class="scrollable"> 
						
						<div class="mainMenuItems"> 
							<div class="mainMenuItem"> 
								gcmd variable facets page1
							</div> 
							<div class="mainMenuItem"> 
								gcmd variable facets page2
							</div> 
						</div> 
					</div> 
				</div> 
			
			 	
			</div>
			
		</div>
	</div>
	

</div>
-->





<!--  
	<div class="span-6 last">
		
		<div class="l2contentleftbox">  
			<fieldset id="search_options">
      			<legend id="more_constraints1"></legend>
	      		<ul class="constraints_list">
					<li id="showConstraints" style="list-style-image: none;cursor:pointer">Show more constraints</li>
					<li style="list-style-image: none;cursor:pointer">Show browsing history</li>
					<li style="list-style-image: none;cursor:pointer">Related Searches</li>
					<li style="list-style-image: none;cursor:pointer">Show my bookmarks</li>
				</ul>
      		</fieldset>
		</div>
	</div>
-->









		<!--  Search constraints -->
		<!--  
		<div class="round-box"> 
		<div class="round-header round-top"> Search Constraints</div>           
		<div class="round-content"> 
		
		    <div id="temporal"><a href="<c:url value="/scripts/esgf/temporal_overlay.html" />" rel="#temporal_overlay" style="text-decoration:none"> Temporal</a> </div>
		      
		    <div id="geo"><a href="<c:url value="/scripts/esgf/geospatial_overlay.html" />" rel="#geospatial_overlay" style="text-decoration:none"> Geospatial</a> </div>
		    
		      
		    <div id="category"><a href="<c:url value="/scripts/esgf/facet_overlay.html" />" rel="#facet_overlay" style="text-decoration:none"> Categories</a> </div>
		    
		    
		</div>             
		</div> 
		-->
		
	<!--  project tags -->
	<!--  
	<div class="round-box">
	<div class="round-header round-top">Project Tags</div> 
	  
	<div id="project" class="round-content"></div>
	
	</div> 
	-->