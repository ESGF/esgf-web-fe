<%@ include file="/WEB-INF/views/search/_overlay.jsp" %>
<%@ include file="/WEB-INF/views/search/_select_tbl.jsp" %>

<style>
.distribbutton {
	font-size: 13px;
	color: white;
	border: 1px solid #9c9c9c;
	background: #838943;
	cursor: pointer;
}
</style>

<div class="span-18 last" >
        <div class="span-15">
	       <span id="search-box">
	               <input id="query" name="text" type="text" value="" />
	       </span>
	       <input id="search-button" type="submit" value="Search" />
	       <br/>&nbsp; &nbsp;
	       Examples: <i>temperature</i>, <i>"surface temperature"</i>, <i>climate AND project:CMIP5 AND variable:hus</i>.
	       <br/>&nbsp; &nbsp;
	       To download data: add datasets to your Data Cart, then click on <i>Expand</i> or <i>wget</i>.
        </div>
	    <div class="span-3 last" style="margin-top:5px">

	    	<div id="temporal"><a href="<c:url value="/scripts/esgf/overlays/temporal_overlay.html" />" id="temporal" rel="#temporal_overlay" style="font-size:10px">Temporal Search</a></div>
	    	<div id="geo"><a href="<c:url value="/scripts/esgf/overlays/geospatial_overlay.html" />" rel="#geospatial_overlay" style="font-size:10px" id="geo">Geospatial Search</a></div>
	    	<!-- <div id="distributed"><a href="#" style="font-size:10px" id="distributed">Turn on Distributed Search</a></div>
	    	 -->
	    	 <div id="facet_browser_overlay"><a href="#" rel="#facet_overlay" style="font-size:10px" >Browse Categories</a></div>
	    	<!-- <div><a href="#" style="font-size:10px">Advanced...&#9660;</a></div>  -->
	    	<div id="search_help"><a href="http://www.esgf.org/wiki/ESGF_Web_Search_User_Guide" style="font-size:10px" >Search Help</a></div>
	    	<div id="search_cv"><a href="http://www.esgf.org/wiki/ESGF_Search_CV" style="font-size:10px" >Search Controlled Vocabulary</a></div>
	    </div>
	   	
	   	  
		<div class="span-18 last" style="margin-bottom:5px"> 
		<!--  
			<input class="distribbutton1" id="distribbutton" type="submit" style="margin-left:20px"/>
		-->
			<input type="checkbox" class="distribcheckbox1" id="distribcheckbox" style="margin-left:20px" checked="yes" /> <span style="font-weight:bold">Search All Sites</span>
			<input type="checkbox" class="replicacheckbox1" id="replicacheckbox" style="margin-left:20px"/> <span style="font-weight:bold">Show All Replicas</span>
			<input type="checkbox" class="versioncheckbox1" id="versioncheckbox" style="margin-left:20px"/> <span style="font-weight:bold">Show All Versions</span>
			
		</div>
		
</div>

<div class="span-18 last" id="search-summary" style="margin-top:15px;">
		
		    	  
		    <div class="span-7" id="search-speed">
		    	<!--<div id="search-help" style="margin-left:15px;">(press ESC to close suggestions)</div>-->
		    </div>
		    
		
		    <div id="page-navigation" class="span-11 last">
		      <ul id="pager"></ul>
		      <div id="pager-header"></div>
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
	    <!-- 
	    <table style="width:100%;table-layout: fixed">
	    <tbody id="datasetList">
	
	    </tbody>
	    </table>
	    -->
	    </div>
	</div>
</div>
 

<script type="text/javascript">

    $(function(){
    	
    	//event is trigger on both logout and login links (for now)
    	//there is a little disconnect with the header.jsp file so this can be seen as a temporary fix until a main page clean up is performed 
    	$('li.resetLocalStorage').live('click', function() {
    		//reset the localStorage to querying over the dataset type
      	  	ESGF.localStorage.removeAll('esgf_fq');
      	  
      	  	//reset the contents of the datacart
      	  	ESGF.localStorage.removeAll('dataCart');
      	  	

      	  	//reset the contents of the datacart
      	  	ESGF.localStorage.removeAll('esgf_queryString');
      	  	
    	});
    	
    	
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
    		/*
    		if ($('input#showConstraints').attr('checked'))
    		{
        		alert('show constraints');
    		}
    		else
    		{
        		alert('hide constraints');
    		}
    		*/
		});
        $('#myTabs').tabs();
        
        
        /*Facet overlay
        */
      //scroll wheel for facet overlay 
        $(".scrollable").scrollable({ vertical: true, mousewheel: true });

        $("div#facet_browser_overlay a[rel]").overlay({
        		 
        		mask: {opacity: 0.5, color: '#000'},
        		effect: 'apple',
        		left: "30%",
        		top: "2%",
        		
        		onBeforeLoad: function() {
        		
        			$('.apple_overlay').css({'width' : '700px'});
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

