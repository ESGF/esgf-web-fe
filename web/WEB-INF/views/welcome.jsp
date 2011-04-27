<%@ include file="/WEB-INF/views/common/include.jsp" %>



<ti:insertDefinition name="home-layout" >
	
	<ti:putAttribute name="main">
		<!-- info -->
		<div class="span-24 info last">
		
			<!-- info left -->
			<div class="span-7 infoinner">
			
				<!-- info header -->
				<div class="span-7 last infoheader">
                    <div class="infoheadericon">
                        <img src="images/image001.jpg" />
                    </div>
                    <div class="infoheadertxt">
                            <h1>Quick Search</h1>
                    </div>
                </div>
				<!-- info content -->
                <div class="span-7 last infocontent">
                	<h3>Keyword:</h3> 
                	<div class="span-7 last">
                	<!--  <form action="<c:url value="/live"/>" > -->
                		<input id="home_query" name="text" type="text" value="" />
	    				<input id="home_search-button" type="submit" value="Search" />
                	
                	<!--  </form> -->
                	</div>
					<div class="span-7 last">
                             	&nbsp;
                    </div>
                	<!--  
                	<div class="searchformcontainer">
                		<form action="" class="searchform" method="get">   
                			  <div class="search-field-left">
                             	&nbsp;
                           	</div>
							<input type="text" class="search-field" value="test" />
                          	<input name="submit" type="image" src="images/search_btn2.jpg" class="search-btn" value="search" />                                                
                                                                                                                         
                    	</form>
                	</div>
                    -->  
                    <h3 id="navigate">Advanced Search (Category, Geospatial, Temporal, and more)... </h3>
                    <!--  
           			<ul>
                         <li id="home_project"><a href='#' >Project</a></li>
                         <li><a href="#" >Institute</a></li>
                         <li><a href="#" >Model</a></li>
                         <li><a href="#" >Experiment</a></li>
                         <li><a href="#" >Frequency</a></li>
                         <li><a href="#" >Product</a></li>
                         <li><a href="#" >Realm</a></li>
                         <li><a href="#" >Variable</a></li>
                         <li><a href="#" >Ensemble</a></li>
                    </ul> 
                    -->      
                </div>
			</div>
			
			<!-- info center -->
			<div class="span-8 infoinner2">
				<!-- info header -->
				<div class="span-8 last infoheader">
                    <div class="infoheadericon">
                        <img src="images/image002.jpg" />
                    </div>
                    <div class="infoheadertxt">
                        <h1>About <spring:message code="esgf.datanode.name" /></h1>
                    </div>
                </div>
				
				<!-- info content -->
	            <div class="span-8 last infocontent">
	            	<div class="infocontenttop">
                         <p>
                            <spring:message code="esgf.homepage.about" />
                         </p>                                                      
                     </div>
                     <div class="infocontentbottom">
                         &nbsp;   	       
                     </div>   
	            </div>	
			</div>
			<!-- info right -->
			<div class="span-7 last infoinner3">
				 
				<div class="span-7 last infoheader">
                    <div class="infoheadericon">
                        <img src="images/image003.jpg" />
                    </div>
                    <div class="infoheadertxt">
                        <h1>Resources</h1>
                    </div>
                </div>
                <div class="span-7 last infocontent">
               	 	<div class="projectitem">
                         <div class="projectitemtop">
                             <div class="projectitemicon">
                                 <a href="#"><img src="images/image004.jpg" /></a>
                             </div>
                             <div class="projectitemtitle">
                                 <h3>Quick Linkss</h3>
                             </div>
                         </div>
                         <div class="projectitembottom">
	                         <ul>
	                             <li>Getting Started Guide</li>
	                             <li><a href='<c:url value="/createAccount"/>' >Create Account</a></li>
	                             <li>Browse Catalogs</li> 
	                             <li><a href='<c:url value="/live"/>'>Search for Data</a></li>
	                         </ul>
                         </div>
                     </div>
                     <div class="projectitem">
                          <div class="projectitemtop">
                              <div class="projectitemicon">
                                  <a href="#"><img src="images/image005.jpg" /></a>
                              </div>
                              <div class="projectitemtitle">
                                  <h3>ESGF Peer Nodes</h3>
                              </div>
                          </div>
                          <div class="projectitembottom">
	                         <ul>
	                             <li><a href='http://esg-test1.llnl.gov/esgf-web-fe/' >PCMDI Node</a></li>                             
	                             <li><a href='http://esg-datanode.jpl.nasa.gov/esgf-web-fe/' >ORNL Node</a></li>
	                             <li><a href='http://esg-datanode.jpl.nasa.gov/esgf-web-fe/' >NASA-JPL Node</a></li>
	                         </ul>
                          </div>
                      </div> 
                      <div class="projectitem">
                          <div class="projectitemtop">
                              <div class="projectitemicon">
                                  <a href="#"><img src="images/image006.jpg" /></a>
                              </div>
                              <div class="projectitemtitle">
                                  <h3>Analysis Tools</h3>
                              </div>
                          </div>
                          <div class="projectitembottom">
	                         <ul>
	                             <li><a href='http://esg.llnl.gov/cdat/' >CDAT (Climate DAta Tools)</a></li>                             
	                             <li><a href='http://cdx.jpl.nasa.gov/' >CDX (Climate Data Exchange)</a></li>
	                             <li><a href='http://ferret.pmel.noaa.gov/LAS' >LAS (Live Access Server)</a></li>
	                         </ul>
                          </div>
                      </div> 
      	      	</div>
            	
			</div>
		</div>
		
		
	</ti:putAttribute>
	
</ti:insertDefinition>

<script type="text/javascript">

    $(function(){
    	
    	function addToStorage(fq) {
    		if(fq == undefined) {
     	  		//alert('add text:' + $('input#home_query').val() + ';');
      	  		if($('input#home_query').val() == '') {
      	  			fq = 'text:' + '*;';
      	  		} else {
      	  			fq = 'text:' + $('input#home_query').val() + ';';
      	  		}
      	  		localStorage['fq'] = fq;
      	  		
      	  	} 
      	  	else {
      	  		//alert('add text:' + $('input#home_query').val() + ';');
      	  		if(!isDuplicate(fq)) {
	      	  		if($('input#home_query').val() == '') {
	      	  			fq += 'text:' + '*;';
	      	  		} else {
	      	  			fq += 'text:' + $('input#home_query').val() + ';';
	      	  		}
	      	  		localStorage['fq'] = fq;
      	  		}
      	  		
      	  	}
    	}
    	
    	function isDuplicate(fq) {
    		var flag = false;
    		var allFqs = fq.split(";");
  			//loop over all existing queries - note 'length-1' was used to ignore the trailing whitespace of the last split
  	  		for(var i=0;i<allFqs.length-1;i++)
  			{
  				if(allFqs[i] == ('text:' + $('input#home_query').val().toString())) {
  					flag = true;
  				}
  			}
    		return flag;
    	}
    	
    	$('#home_search-button').click(function(){
    		//alert($('input#home_query').val());
    		var fq = localStorage['fq'];
    		addToStorage(fq);
      	  location.href='<c:url value="/live"/>';
    	});
    	$('#home_query').bind('keydown', function(e) {
    		if (e.which === 13) {
    			//alert($('input#home_query').val());
        		var fq = localStorage['fq'];
        		addToStorage(fq);
          	  location.href='<c:url value="/live"/>';
    		}
    	});
    	
    	
    	
    	$('h3#navigate').hover(
    		function () {
        		$(this).css({'color':'blue','cursor':'pointer'});
    		},
    		function () {
    			$(this).css({'color':'#7d5f45'});
    		}
    	);
    	
    	$('h3#navigate').live('click',function(){
			location.href='<c:url value="/live"/>';
    	});
    });

</script>
  