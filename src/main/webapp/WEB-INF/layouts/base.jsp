<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="ti"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>ESG Geo-Spatial Search</title>
    <link rel="stylesheet" 
        href="<c:url value="/styles/yui/2.8.1/reset-fonts/reset-fonts.css" />" 
        type="text/css">
    <link rel="stylesheet" href="<c:url value="/styles/plain.css" />" 
        type="text/css" media="screen, projection">
    <script type="text/javascript" src="<c:url value="/scripts/jquery-1.4.2.min.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/json.min.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/menu.js" /> "></script>            
    <script type="text/javascript" src="<c:url value="/scripts/searchbox.js" /> "></script>            

    <jsp:include page="/WEB-INF/views/search/_search_js.jsp" flush="true" />

</head>

<body>

    
<div class="banner"> 
<table width="100%" cellpadding="0" cellspacing="0" align="center" border="0" > 
<tr> 
<td align="left" class="banner-left" >&nbsp;</td> 
<td align="center" class="banner-center">&nbsp;</td> 
<td align="right" class="banner-right">&nbsp;</td> 
</tr> 
</table> 
</div> <!-- div id="banner" -->         

<table id="nav">
<tr> <td align="left">
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
                             
<div id="wrapper">

    <div id="facets">
        <ti:insertAttribute name="facets" />
    </div>

    <div id="main">
    <ti:insertAttribute name="search" />
    
    <ti:insertAttribute name="result" />    
    </div>
        
</div>
 
<p id="copyright"> Copyright 2010, ESGF Infrastructure Team. </p>
</body>
</html>