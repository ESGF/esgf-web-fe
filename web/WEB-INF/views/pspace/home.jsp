<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ include file="/WEB-INF/views/common/include.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<!--  
    <link href="<c:url value='http://fonts.googleapis.com/css?family=Droid+Sans:400'></c:url>" 
    	rel='stylesheet' type='text/css' />

    <link href="<c:url value='http://fonts.googleapis.com/css?family=Convergence'></c:url>" 
    	rel='stylesheet' type='text/css' />

	-->
	
    <link href="<c:url value='http://fonts.googleapis.com/css?family=Petrona'></c:url>" 
    	rel='stylesheet' type='text/css' />

    
	<link rel="stylesheet/less" href="<c:url value='/styles/bootstrap/lib/bootstrap.less'> </c:url>" />
	
	<script src="<c:url value='/scripts/less/less-1.1.3.min.js'> </c:url>"> </script>
	<script src="<c:url value='http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js'></c:url>"></script>

	<style type="text/css">
	h1, h2, h3, h4 {
	
		/* font-family: 'Droid Sans', sans-serif; */
		font-family: 'Petrona', serif;
	}
	
	h1#title {
		padding-top: 25px;
		position: relative;
		bottom: 0; 
	}
	
	img {
		float:right;
		padding-top: 1em;
	}
	</style>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Project Home</title>
</head>
<body>




    <div class="container">
		<div class="page-header">
		<div class="row">
			<div class="span8">	<h1 id="title">Project Space 
			<small>for <c:out value="${project}" /></small></h1></div>
			<div class="span8"> <img src="<c:url value='/images/ornl_logo.png'> </c:url>" />
			</div>
		</div>
		</div>

		<div class="row">
		
			<div class="span-one-third">
			<h3>Project Stats</h3>
				<ul>
					<li> Total number of datasets:
					<li> Total number of files:
					<li> Total data size:
					<li> Last update on the project:
				</ul>
			</div>
			
			<div class="span-one-third">
			<h3> Dataset Attributes</h3>
			     <ul>
			     </ul>			
			</div>
		</div>
		
		<hr/>
		
		<div class="content">

			<div id="readme"></div>		
		
		</div>
		
      	<footer>
        	<p>&copy; Oak Ridge National Laboratory 2011</p>
      	</footer>

    </div> <!-- /container -->
    
    <script type="text/javascript">
    	var pathname = window.location.pathname;
    	var split = pathname.split('/');
		var project = split[split.length-1]; 
		$.ajax({
			url:  project + "/readme",
			dataType: 'html',
			success: function( data ) {
				$('div#readme').html(data);
			}
		})
    </script>
</body>
</html>