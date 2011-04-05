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
                            <h1>Search &amp; Categories</h1>
                    </div>
                </div>
				<!-- info content -->
                <div class="span-7 last infocontent">
                	<h3>Keyword:</h3> 
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
                    <h3>Search By Category: </h3>
           			<ul>
                         <li><a href="#" >Project</a></li>
                         <li><a href="#" >Institute</a></li>
                         <li><a href="#" >Model</a></li>
                         <li><a href="#" >Experiment</a></li>
                         <li><a href="#" >Frequency</a></li>
                         <li><a href="#" >Product</a></li>
                         <li><a href="#" >Realm</a></li>
                         <li><a href="#" >Variable</a></li>
                         <li><a href="#" >Ensemble</a></li>
                    </ul>       
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
                             The Program for Climate Model Diagnosis and Intercomparison (PCMDI) was established in 1989 at the Lawrence Livermore National Laboratory (LLNL), located in the San Francisco Bay area. Our staff includes research scientists, computer scientists, and diverse support personnel. <a href="#" >read more</a>
                         </p>                                                      
                         <h3>Resources:</h3>                           
                         <ul>
                             <li><a href="#" >Getting Started Guide</a></li>
                             <li><a href="#" >Create Account</a></li>
                             <li><a href="#" >Browse Catalogs</a></li>
                             <li><a href="#" >Search for Data</a></li>
                         </ul>
                         <h3>Gateways:</h3>
                         <!--  drop down list for gateway selection -->      
                         <!--                                                                         
                         <div class="dropbg">
                             &nbsp;
                         </div>   
                         -->                                                                                                                                                                               
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
                        <h1>Projects &amp; Programs</h1>
                    </div>
                </div>
                <div class="span-7 last infocontent">
               	 	<div class="projectitem">
                         <div class="projectitemtop">
                             <div class="projectitemicon">
                                 <a href="#"><img src="images/image004.jpg" /></a>
                             </div>
                             <div class="projectitemtitle">
                                 <h3>Consectetur adipiscing</h3>
                             </div>
                         </div>
                         <div class="projectitembottom">
                             <p>
                                 Lorem ipsum dolor sit amet, consectetur adipiscing elit. consectetur adipiscing elit Nullam id odio tellus. <a href="#" >read more</a>
                             </p>
                         </div>
                     </div>
                     <div class="projectitem">
                          <div class="projectitemtop">
                              <div class="projectitemicon">
                                  <a href="#"><img src="images/image005.jpg" /></a>
                              </div>
                              <div class="projectitemtitle">
                                  <h3>Lorem ipsum dolor sit </h3>
                              </div>
                          </div>
                          <div class="projectitembottom">
                              <p>
                                  Lorem ipsum dolor sit amet, consectetur adipiscing elit. consectetur tellus. Lorem ipsum <a href="#" >read more</a>
                              </p>
                          </div>
                      </div> 
                      <div class="projectitem">
                          <div class="projectitemtop">
                              <div class="projectitemicon">
                                  <a href="#"><img src="images/image006.jpg" /></a>
                              </div>
                              <div class="projectitemtitle">
                                  <h3>Adipiscing Elit</h3>
                              </div>
                          </div>
                          <div class="projectitembottom">
                              <p>
                                  Lorem ipsum dolor sit amet, consectetur. consectetur adipiscing elit Nullam id odio tellus. Lorem ipsum <a href="#" >read more</a>
                              </p>
                          </div>
                      </div> 
      	      	</div>
            	
			</div>
		</div>
		
		
	</ti:putAttribute>
	
</ti:insertDefinition>
  