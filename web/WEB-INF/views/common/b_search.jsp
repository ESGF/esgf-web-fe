<%@ include file="include.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
     "http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <title>ESG Geo-Spatial Search</title>
    
    <link rel="stylesheet" 
        href="<c:url value="/styles/yui/2.8.1/reset-fonts/reset-fonts.css" />" 
        type="text/css">

    <!--  Google fonts -->        
    <link href='http://fonts.googleapis.com/css?family=Droid+Sans&subset=latin' 
        rel='stylesheet' type='text/css'>

    <link rel="stylesheet" href="<c:url value="/styles/jquery.jgrowl.css" />" 
        type="text/css" media="screen, projection">

    <link rel="stylesheet" href="<c:url value="/styles/jgrowl-theme.css" />" 
        type="text/css" media="screen, projection">
        
    <link rel="stylesheet" href="<c:url value="/styles/plain.css" />" 
        type="text/css" media="screen, projection">


	<link rel="stylesheet" 
		href="<c:url value="/styles/metadata_overlay.css" />" 
		type="text/css" media="screen, projection">
           
    <script type="text/javascript" src="<c:url value="/scripts/jquery/jquery-1.4.2.min.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/jquery/jquery-ui-1.8.5.min.js" /> "></script>    
    <script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.tools.min.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.jgrowl.min.js" /> "></script>
   
    <script type="text/javascript" src="<c:url value="/scripts/jquery/json.min.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.form.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/esgf/menu.js" /> "></script>            
    <script type="text/javascript" src="<c:url value="/scripts/esgf/search.js" /> "></script> 
    <script type="text/javascript" src="<c:url value="/scripts/esgf/map.js" /> "></script> 
    <script type="text/javascript" src="<c:url value="/scripts/esgf/submit.js" /> "></script> 

    
    <script type="text/javascript"
        src="http://maps.google.com/maps/api/js?sensor=false">
    </script>

   
</head>

<body>

    <div id="metadata_overlay"></div>
	
<div id="header"> 

	<table width="100%" cellpadding="0" cellspacing="0" align="center" border="0" > 
	<tr> 
	<td align="left" class="banner-left" >&nbsp;</td> 
	<td align="center" class="banner-center">&nbsp;</td> 
	<td align="right" class="banner-right">&nbsp;</td> 
	</tr> 
	</table> 

    <div id="tab">

		<table id="nav">
		<tr> 
		    <td align="left">
		    <ul> 
		        <li><a href="#">Home</a></li> 
		        <li><a href="#">Browse</a></li> 
		        <li><a href="#">Account</a></li> 
		        <li><a href="#">Contact Us</a></li> 
		    </ul>
		    </td>
		
		    <td align="right">
		     <!-- Handle Login -->                            
		    </td>
		</tr>
		</table>
	</div>

</div> <!--  header -->         

                            
<div id="wrapper">

    <div id="facets">
        <ti:insertAttribute name="facets" />
    </div>


    <div id="main">
        
        <div id="search_box">
        <ti:insertAttribute name="searchform" />    
        </div>

	    <div id="search_results">
	        <ti:insertAttribute name="result" />    
	    </div>
	                
        <div id="footer">
        <p>Copyright(c) 2010-2011, ESGF Infrastructure Team. </p>
        </div>


    </div> <!--  end of main -->
</div> <!-- end of wrapper -->

</body>
</html>