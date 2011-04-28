<%@ include file="/WEB-INF/views/search/_overlay.jsp" %>
<%@ include file="/WEB-INF/views/search/_select_tbl.jsp" %>


<div class="span-18 last" >
		<div class="span-12">
			<input id="query" name="text" type="text" value="" />
		</div>
		<!--  
		<span class="span-13" id="search-box">
	    	<input id="query" name="text" type="text" value="" />
	    </span>
	    -->
	    <!--  
	    
	    -->
	    <div class="span-3" style="margin-top:5px">
	    <!--  
	    	<div><a href="" style="font-size:10px">Geospatial Search</a></div>
	    	<div><a href="" style="font-size:10px">Temporal Search</a></div>
	    	
	    	
	    	<div id="temporal"><a href="<c:url value="/scripts/esgf/temporal_overlay.html" />" id="temporal" rel="#temporal_overlay" style="font-size:10px">Temporal Search</a></div>
	    	
	    	-->
	    	<div id="temporal"><a href="<c:url value="/scripts/esgf/temporal_overlay.html" />" id="temporal" rel="#temporal_overlay" style="font-size:10px">Temporal Search</a></div>
	    	<div id="geo"><a href="<c:url value="/scripts/esgf/geospatial_overlay.html" />" rel="#geospatial_overlay" style="font-size:10px" id="geo">Geospatial Search</a></div>
	    	
	    	<div><a href="" style="font-size:10px">Advanced...&#9660;</a></div>
	    </div>
	   	<div class="span-3 last">
	    	<input id="search-button" type="submit" value="Search" />
	    </div>
</div>

<div class="span-18 last" id="search-summary" style="margin-top:15px;">
		
		    <div class="span-7" id="search-speed">
		    	<div id="search-help" style="margin-left:15px;">(press ESC to close suggestions)</div>
		    </div>
		
		    <div id="page-navigation" class="span-11 last">
		      <ul id="pager"></ul>
		      <div id="pager-header"></div>
		    </div>
	
	    </div>  





<div id="dialog" title="Temporal Search">
<!--  
	<div class="overlay_header" style="display:none">
	  
		<div class="overlay_header_title">
			Temporal Search
		</div>
		<div class="overlay_header_buttons">
			
		</div>
	
	</div>
	<div class="overlay_border" style="display:none"></div>
-->
	<div class="overlay_content" style="display:none">
	<!--  this needs to be changed -->
 		<div id="temporalbox">	
	  		<div id="lb">  
		  		<div id="rb">  
			  		<div id="bb">
			  			<div id="blc">
			  				<div id="brc">  
								<div id="tb">
									<div id="tlc">
										<div id="trc">  
											<div id="temporal_range_title">  
												Date Range  
											</div>  
											<div id="temporal_content1">  	
												<div id="time_range">  
													Search from: <input type="text" id="from" name="from" size="10" />  
													to: <input type="text" id="to" name="to" size="10" />   
												</div> 
												<br />  
												<div id="adv_temp1">  
													<p class="legend"></p>	   
												</div> 
						
											</div>  
										</div>
									</div>
								</div>
							</div> 
						</div>
					</div>
				</div>
			</div>  
		</div> 
	</div>
	<div class="overlay_footer" id="tButton" style="display:none">
	    <button id="submitTemporal">Submit Temporal Constraints</button> 
	</div> 
</div>







	
<div class="span-18 last" >


	<div id="myTabs" class="l2contentrightbox">
	
	    <ul>
	
	    <li><a href="#search-results"> Results</a></li>
	
	    <li><a href="#carts"> Data Cart </a></li>
	
	    </ul>
	
	    <div id="search-results"> </div>
	
	    <div id="carts"> 
	    <table>
	    <tbody id="datasetList">
	
	    </tbody>
	    </table>
	    </div>
	</div>
</div>
 


<script type="text/javascript">

    $(function(){
    	
    	
    	$('li#showConstraints').live('click',function () {
    		var text = $(this).html();
    		var constraintsText = '';
    		if(text === 'Show more constraints') {
    			constraintsText = 'Hide more constraints';
    		} else {
    			constraintsText = 'Show more constraints';
    		}
			$('li#showConstraints').html(constraintsText);
			$('fieldset#showConstraintsBox').toggle('slow');
    		
    	});
    	
    	//$('#myform input[type=checkbox]:checked')
    	$('#constraintChoices input[type=checkbox]').live('click', function () {
    		//alert($('form#my').html());
    		alert($('input#showConstraints').attr('checked'));
    		if ($('input#showConstraints').attr('checked'))
    		{
        		alert('show constraints');
    		}
    		else
    		{
        		alert('hide constraints');
    		}
		});
        $('#myTabs').tabs();
        
        
        /*Facet overlay
        */
      //scroll wheel for facet overlay 
        $(".scrollable").scrollable({ vertical: true, mousewheel: true });

        // For the facet overlay 
        $("li#facet a[rel]").overlay({
        		 
        		mask: {opacity: 0.5, color: '#000'},
        		effect: 'apple',
        		left: "30%",
        		top: "2%",
        		
        		onBeforeLoad: function() {
        		
        			$('.apple_overlay').css({'width' : '700px'});
        			alert('loading browser');
        		},

        		onLoad: function() {
        			 //radio buttons for sorting facets 
        		    $("#facetSort").buttonset();
        			$(".overlay_header").show();
        			$(".overlay_content").show();
        			$(".overlay_footer").show();
        			$(".overlay_border").show();
        	},
        	
        	onClose: function() {
        			$(".overlay_header").hide();
        			$(".overlay_content").hide();
        			$(".overlay_footer").hide();
        			$(".overlay_border").hide();
        		}
        	
        });  

         //event trigger for facet sorting buttons 
        $("input[name='sorter']").change(function() {
            if ($("input[name='sorter']:checked").val() == 'sortbyabc') {
                Manager.sortType = 'sortbyabc';
                Manager.doRequest(0);
            } else {
                Manager.sortType = 'sortbycount';
                Manager.doRequest(0);
            }
        });
    });

</script>





<!--  old options list -->
<!--  
<div class="span-18 last">
	
	<div class="span-16 prepend-1 append-1 last constraintsBox">
		
		<fieldset id="showConstraintsBox" style="display:none">
      		<legend id="more_constraints">Advanced Search Options</legend>
      		<div class="span-14 prepend-1 last">
      			
		    	<div style="float:left;padding-right:10px;cursor:pointer">
		    		<input type="checkbox"> Observational 
		    	</div>
		    	<div style="float:left;padding-right:10px;;cursor:pointer">
		    		<input type="checkbox"> Model 
      			</div>
      			
      			 
      			<div id="geo1" style="float:left;padding-right:10px;cursor:pointer">
      				<input type="checkbox"><a href="<c:url value="/scripts/esgf/geospatial_overlay.html" />" rel="#geospatial_overlay" style="text-decoration:none;color:black" id="geo"> Geospatial</a>
      			</div>
      			
		    	<div id="temporal" style="float:left;padding-right:10px;cursor:pointer">
		    		<input type="checkbox">
		    			<a href="#" rel="#temporal_overlay" style="text-decoration:none;color:black" id="temporal"> 
		    				Temporal
		    			</a> 
		    	</div>
		    	
		    	
		
		    	
      		</div>
      		
      	 
      	</fieldset>
	
	</div>
</div>
-->
