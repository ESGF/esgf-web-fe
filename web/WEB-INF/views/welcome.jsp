<%@ include file="/WEB-INF/views/common/include.jsp" %>

<%--
Script information is in scripts/esgf/welcome.js
Style information is in styles/welcomeview/welcome.css
 --%>
<ti:insertDefinition name="home-layout" >
	
	<ti:putAttribute name="main">
		<!-- info -->
		<div class="span-24 info last" >
		
			<%-- 
			<%@ include file="/WEB-INF/views/welcome/quick_search.jsp" %>
			
			
			<!-- about info column -->
			<%@ include file="/WEB-INF/views/welcome/about.jsp" %>
			
			
			<!-- resource column -->
			<%@ include file="/WEB-INF/views/welcome/resources.jsp" %>
			--%>
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
	                             	<a href='http://pcmdi9.llnl.gov/esgf-web-fe/' >PCMDI Node</a>
	                             	<a href='http://pcmdi9.llnl.gov/esg-search/feed/datasets.rss'><img src='images/rss_icon.png'/ align="bottom"></a>
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
		<div class="span-24 info last" >
			<div class="span-2"><p></p></div>
			<div class="span-20" style="padding-top:5px;margin-bottom:5px;text-align:center">
					<a href="http://esgf.org/esgf-web-fe-site/browser-support.html" style="font-size:10px;color:#7d5f45" target="_blank">(supported browsers)</a>
			</div>	
			<div class="span-2 last" ><p></p></div>
		</div>
		
	</ti:putAttribute>
	
</ti:insertDefinition>


