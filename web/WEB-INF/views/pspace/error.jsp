<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ include file="/WEB-INF/views/common/include.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<link rel="stylesheet/less" href="<c:url value='/styles/bootstrap/lib/bootstrap.less'> </c:url>" />
	
	<script src="<c:url value='/scripts/less/less-1.1.3.min.js'> </c:url>"> </script>


	<style type="text/css">
	/* Overide some defaults */
	.alert-message {
		top: 300px;
	}
	
	</style>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Project Space Error Report</title>
</head>
<body>




    <div class="container">

      <div class="content">
        <div class="page-header">
          	<img src="<c:url value='/images/ornl_logo.png'> </c:url>" />         
        </div>
        
        
        <div class="row">
          <div class="span8">
                     
           <img src="<c:url value='/images/notfound.jpg'></c:url>" />
          
          </div>
          <div class="span8">
          	<div class="alert-message warning">
            Reason: The given project name can't be found!
            </div>
          </div>
        </div>
      </div>

      <footer>
        <p>&copy; Oak Ridge National Laboratory 2011</p>
      </footer>

    </div> <!-- /container -->
    
</body>
</html>