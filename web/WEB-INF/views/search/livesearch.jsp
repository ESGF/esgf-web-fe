<%@ include file="../common/include.jsp" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

	<script type="text/javascript" src="<c:url value="/scripts/ajax-solr/core/Core.js" />"> </script>
	<script type="text/javascript" src="<c:url value="/scripts/ajax-solr/core/AbstractManager.js" />"></script>
	<script type="text/javascript" src="<c:url value="/scripts/ajax-solr/managers/Manager.jquery.js" />"> </script>

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
		href="<c:url value="/styles/esg-simple.css" />" 
		type="text/css" media="screen, projection">

<title>ESG Search</title>
</head>

<body>

<div class="container">


<div id="header" class="span-24 last">
<div class="span-16">
<span class="tabitem"> <a href="#"> Home </a></span> 
<span class="tabitem"> <a href="#"> Browse </a> </span>
<span class="tabitem"> <a href="#"> Analysis</a></span>
</div>
<hr/>
</div>


<div id="subheader" class="span-24 last">

	<div id="logo" class="span-4">
    <img src="<c:url value="/images/esg100.png" />" >
    
	<h4 class="alt"> Experimental Search</h4>
    
    </div>

	<div class="push-1 span-19 last" id="search-box">
			<input class="searchbox" name="text" type="text" value="" />
			<input class="button" type="submit" value="Search" />	
	</div>
    
</div>





<div class="span-24 last" id="contents">

    <div class="span-4" id="sidebar">
 
	    <div id="constraints"> 
	        <h3 class="caps">Search Constraints</h3> 
	        
	        <div class="box"> 
	          <div><a class="quiet" href="#"> Temporal</a> </div>
	          <div><a class="quiet" href="#"> Geospatial</a> </div>
	        </div> 
	         
	    </div> 

        <div id="tags"> 
            <h3 class="caps">Popular Tags</h3> 
        
            <div class="box"> 
            <div><a class="quiet" href="#"> Temporal</a> </div>
            <div><a class="quiet" href="#"> Geospatial</a> </div>
            </div> 
         
        </div> 
 
    </div>

    <div class="span-20 last" id="search-results">
    </div>

</div>


 <div id="footer">      
      <div class="prepend-top span-8 last"> 
        <span class="quiet">ESGF Copyright &copy; 2010</span> | 
        <a href="#" class="quiet">Disclaimer</a>       
        </div> 
 </div> 

</div>

</body>
</html>