<%@ include file="../common/include.jsp" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <script type="text/javascript" src="<c:url value="/scripts/jquery/jquery-1.4.2.min.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/jquery/jquery-ui-1.8.5.min.js" /> "></script> 
    <script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.tools.min.js" /> "></script> 
    <script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.livequery.js" /> "></script> 
    <script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.autocomplete.js" /> "></script> 
    
	<script type="text/javascript" src="<c:url value="/scripts/ajax-solr/core/Core.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/core/Parameter.js" />"></script>	
	<script type="text/javascript" src="<c:url value="/scripts/ajax-solr/core/ParameterStore.js" />"></script>
	<script type="text/javascript" src="<c:url value="/scripts/ajax-solr/core/AbstractManager.js" />"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/core/AbstractWidget.js" />"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/core/AbstractFacetWidget.js" />"></script>
    
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/helpers/jquery/ajaxsolr.theme.js" />"></script>
	<script type="text/javascript" src="<c:url value="/scripts/ajax-solr/managers/Manager.jquery.js" />"> </script>


    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/Results.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/Pager.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/TagClouds.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/CurrentSearch.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/Text.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/AutoComplete.js" />"> </script>
    

<script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/Calendar.js" />"></script>
                
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/Temporal.js" />"> </script>
                
    <script type="text/javascript" src="<c:url value="/scripts/esgf/solr.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/esgf/solr.theme.js" />"> </script>

	<!--  overlays -->
	<script type="text/javascript"
        src="http://maps.google.com/maps/api/js?sensor=false">
    </script>
	
	<script type="text/javascript" src="<c:url value="/scripts/esgf/temporal_overlay.js" />"> </script>
	<script type="text/javascript" src="<c:url value="/scripts/esgf/geospatial_overlay.js" />"> </script>
	<script type="text/javascript" src="<c:url value="/scripts/esgf/metadata_overlay.js" />"> </script>

    <link href='http://fonts.googleapis.com/css?family=Droid+Sans&subset=latin' 
        rel='stylesheet' type='text/css'>
        
   
	
	<link rel="stylesheet" 
		href="<c:url value="/styles/blueprint/screen.css" />" 
		type="text/css" media="screen, projection">
		
	<link rel="stylesheet" 
		href="<c:url value="/styles/blueprint/print.css" />" 
		type="text/css" media="print">	
		
	<!--[if lt IE 8]><link rel="stylesheet" href="<c:url value="styles/blueprint/ie.css" />"  
		type="text/css" media="screen, projection"><![endif]-->
	
	<link rel="stylesheet" 
		href="<c:url value="/styles/blueprint/plugins/fancy-type/screen.css" />" 
		type="text/css" media="screen, projection">
	
    <link rel="stylesheet" 
        href="<c:url value="/styles/jquery.autocomplete.css" />" 
        type="text/css" media="screen, projection">
	
	<link rel="stylesheet" 
		href="<c:url value="/styles/esg-simple.css" />" 
		type="text/css" media="screen, projection">
	
	<!-- style added for the datepicker widget -->	
	<link rel="stylesheet" 
		href="<c:url value="/styles/cupertino/jquery-ui-1.8.5.custom.css" />" 
		type="text/css" media="screen, projection">
	
	<!-- style added for the temporal search overlay -->
	<!-- style added for the geospatial search overlay -->
	<!-- style added for the metadata report overlay -->
	<link rel="stylesheet" 
		href="<c:url value="/styles/temporal_overlay.css" />" 
		type="text/css" media="screen, projection">
	<link rel="stylesheet" 
		href="<c:url value="/styles/geospatial_overlay.css" />" 
		type="text/css" media="screen, projection">
	<link rel="stylesheet" 
		href="<c:url value="/styles/metadata_overlay.css" />" 
		type="text/css" media="screen, projection">
		
		
<title>ESG Search</title>
</head>

<body>

	<!-- overlays -->
	<div id="temporal_overlay"></div>
	<div id="geospatial_overlay"></div>
	<div id="metadata_overlay"></div>
	
	
	<div class="container">


		<div id="header" class="span-24 last">
			<div class="span-16 left">
			<span class="tabitem"> <a href="#"> Home </a></span> 
			<span class="tabitem"> <a href="#"> Browse </a> </span>
			<span class="tabitem"> <a href="#"> Analysis</a></span>
			</div>
		
		    <div class="prepend-4 span-4 last">
		    <span class="tabitem"> <a href="#"> Search settings</a></span>
		    <span class="tabitem"> <a href="#"> Sign in </a></span>
		    </div>
		<hr/>
		</div>


		<div id="subheader" class="span-24 last">
		 
		    <div class="push-1 span-3">
		        <img src="<c:url value="/images/search2.png" />" >        
		    </div>
		    
		    <div class="span-20 last"">
		    
		        <span id="search-box">
				<input id="query" name="text" type="text" value="" />
				</span>
				
				<input id="search-button" type="submit" value="Search" />	
					
		        <div class="span-20 last" id="search-summary">
		        
		        <div class="span-5" id="search-speed">
		        <div id="search-help">(press ESC to close suggestions)</div>
		        </div>
		    
		        <div id="page-navigation" class="span-15 last">
		          <ul id="pager"></ul>
		          <div id="pager-header"></div>          
		        </div>
		        
		    </div>
					
		    </div>
		    
		   
		</div>

		<div>
			<span id="temp_box">
			
			</span>
		
		</div>

		<div class="span-24 last" id="contents">
		
		  <div class="span-4" id="sidebar">
		 
		  <div class="round-box">
		  <div class="round-header round-top"> Current Selections</div>
		    
		    <div id="current-selection" class="round-content"></div>
		  
		  </div> <!--  current selection -->
		  
		  
		  <div class="round-box"> 
		  <div class="round-header round-top"> Search Constraints</div> 
			        
		  <div class="round-content"> 
			          <div id="temporal"><a href="#"> Temporal</a> </div>
			          <div id="geo"><a href="#"> Geospatial</a> </div>
		  </div> 	         
		  </div> <!--  Search constraints -->
		
		  <div class="round-box">
		  
		    <div class="round-header round-top">Project Tags</div> 
		    
		    <div id="project" class="round-content">
		    </div>
		    
		    
		    </div> <!--  sidebar -->
		  </div> <!--  Project tags -->
		  
		  
		
		  <div class="span-19 last" id="search-results"> </div>
		 
		</div>

<!--  I am not sure if I like this

 <div id="footer">      
      <div class="prepend-top span-8 last"> 
        <span class="quiet">ESGF Copyright &copy; 2010</span> | 
        <a href="#" class="quiet">Disclaimer</a>       
        </div> 
 </div> 
 
-->


</div>

</body>
</html>