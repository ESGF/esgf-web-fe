<%@ include file="../common/include.jsp" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

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
	<h1 id="esg_search"></h1>
</div>

<div id="subheader" class="span-24 last">
	<h3 class="alt"> Still a very experimental search</h3>
</div>

<hr />


<div class="span-17 colborder" id="content">

<div class="span-17" id="search-box">

    <!--  Search Box -->
    <table>
		<tr align="center">
			<td>
			<input class="searchbox" name="text" type="text" value="" /></td>	
	    	
	    	<td> <input class="button" type="submit" value="Search" />
	
		</tr>
		
    </table>


</div>

<div class="span-17" id="search-results">
</div>

</div>

<div class="span-6 last" id="sidebar">
 
      <div id="constraints"> 
        <h3 class="caps">Search Constraints</h3> 
        
        <div class="box"> 
          <div><a class="quiet" href="#"> Temporal</a> </div>
          <div><a class="quiet" href="#"> Geospatial</a> </div>
        </div> 
         
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