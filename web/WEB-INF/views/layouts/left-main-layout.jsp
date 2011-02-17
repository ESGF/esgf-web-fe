<%@ include file="../common/include.jsp" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
		<ti:insertAttribute name="script" />
		<ti:insertAttribute name="extrascript" />
		
		<ti:insertAttribute name="style" />
		<ti:insertAttribute name="extrastyle" />
	        
	    <title> ESGF Portal</title>
	
	</head>

	<body>
	
		<!-- the banner spans the whole page so it is outside the blueprint container -->
		<div id="banner" class="banner">
			<ti:insertAttribute name="banner" />
		</div>

		<div class="container">
		
			<!-- header -->
			<div class="span-24 last" id="header" >
			    <ti:insertAttribute name="header" />
			</div>
							
			<!-- main content -->
			<div class="span-5 last" id="left">
				 <ti:insertAttribute name="left" />
			</div>
			
			<div class="prepend-1 span-18 last" id="main">
				 <ti:insertAttribute name="main" />
			</div>
		
			<!-- footer -->
			<div class="span-24 last" id="footer">
				<ti:insertAttribute name="footer" />
			</div>
		
		</div>
	
	</body>
</html>
