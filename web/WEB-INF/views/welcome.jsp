<%@ include file="/WEB-INF/views/common/include.jsp" %>



<ti:insertDefinition name="home-layout" >
	
	<ti:putAttribute name="main">
		<!-- info -->
		<div class="span-24 info last" style="min-height:500px;">
		
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
                                 <h3>Quick Links</h3>
                             </div>
                         </div>
                         <div class="projectitembottom">
	                         <ul>
	                             <!-- <li>Getting Started Guide</li>  -->
	                             <li><a href='<c:url value="/createAccount"/>' >Create Account</a></li>
	                             <!-- <li>Browse Catalogs</li>  -->
	                             <li><a href='<c:url value="/live"/>'>Search for Data</a></li>
	                             <li><a href='http://rainbow.llnl.gov/dist/esg-myproxy-logon/MyProxyLogon.jnlp' >MyProxyLogon</a></li>
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
	                             <li>
	                             	<a href='http://esgf-node3.llnl.gov/esgf-web-fe/' >PCMDI Node</a>
	                             	<a href='http://esgf-node3.llnl.gov/esg-search/feed/datasets.rss'><img src='images/rss_icon.png'/ align="bottom"></a>
	                             </li>                             
	                             <li>
	                             	<a href='http://esg-vm-demo02.ccs.ornl.gov/esgf-web-fe/' >ORNL Node</a>
	                             	<a href='http://esg-vm-demo02.ccs.ornl.gov/esg-search/feed/datasets.rss'><img src='images/rss_icon.png'/ align="bottom"></a>
	                             </li>
	                             <li>
	                             	<a href='http://esg-datanode.jpl.nasa.gov/esgf-web-fe/' >NASA-JPL Node</a>
	                             	<a href='http://esg-datanode.jpl.nasa.gov/esg-search/feed/datasets.rss'><img src='images/rss_icon.png'/ align="bottom"></a>
	                             </li>
	                             <li>
	                             	<a href='http://esg1-gw.pnl.gov/esgf-web-fe/' >PNNL Node</a>
	                             	<a href='http://esg1-gw.pnl.gov/esg-search/feed/datasets.rss'><img src='images/rss_icon.png'/ align="bottom"></a>
	                             </li>
	                             <li>
	                             	<a href='http://dev.esg.anl.gov/esgf-web-fe/' >ANL Node</a>
	                             	<a href='http://dev.esg.anl.gov/esg-search/feed/datasets.rss'><img src='images/rss_icon.png'/ align="bottom"></a>
	                             </li>
	                         </ul>
                          </div>
                      </div> 
                      <div class="projectitem" style="margin-top:50px">
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
	                             <li><a href='http://esg.llnl.gov/cdat/' >CDAT (Climate Data Analysis Tools)</a></li>                             
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
    	
    	function addTextToStorage(fq,val) {
    		
    		if(fq == undefined) {
    			if(val == '') {
  	  				fq += 'type:Dataset;text:' + '*;';
  	  			} else {
  	  				fq += 'type:Dataset;text:' + val + ';';
  	  			}
  	  			localStorage['fq'] = fq;
    		} else if(fq == null) {
    			if(val == '') {
  	  				fq += 'type:Dataset;text:' + '*;';
  	  			} else {
  	  				fq += 'type:Dataset;text:' + val + ';';
  	  			}
  	  			localStorage['fq'] = fq;
    		} else {
    			var duplicateFlag = isDuplicate(fq,val);
      	  		//first make sure this text does not duplicate a previous text
      	  		if(!duplicateFlag) {
      	  			if(val == '') {
      	  				fq += 'text:' + '*;';
      	  			} else {
      	  				fq += 'text:' + val + ';';
      	  			}
      	  			localStorage['fq'] = fq;
      	  		}
    		}
    	}
    	
    	/*
    	 * Check for duplicate in the storage string (i.e. make sure a user doesn't query "temperature" twice)
    	 */
    	function isDuplicate(fq,val) {
    		var isDuplicate = false;
    		var allFqs = fq.split(";");
  			//loop over all existing queries - note 'length-1' was used to ignore the trailing whitespace of the last split
  	  		for(var i=0;i<allFqs.length-1;i++)
  			{
  				if(allFqs[i] == ('text:' + val)) {
  					isDuplicate = true;
  				}
  			}
    		return isDuplicate;
    	}
    	
    	
    	/* 
    	 * Search button event places the text in html5 storage and navigates user to the live search page
    	 */
    	$('#home_search-button').click(function(){
    		//alert($('input#home_query').val());
    		var searchStr = $('input#home_query').val();
    		
    		if(ESGF.setting.storage) {
    			//get the previous local storage
    			var fq = localStorage['fq'];
    			//call the add text to storage methods to make it part of the fq string
    			addTextToStorage(fq,searchStr);
    		}
      	  	
    		location.href='<c:url value="/live"/>';
      	  	
    	});
    	
    	
    	
    	$('#home_query').bind('keydown', function(e) {
    		var searchStr = $('input#home_query').val();
    		
    		if (e.which === 13) {
    			if(ESGF.setting.storage) {
    	    		var fq = localStorage['fq'];
    	    		addTextToStorage(fq,searchStr);
    			}
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
  
