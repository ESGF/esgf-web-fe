<%@ include file="../common/include.jsp" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />

        <ti:insertAttribute name="script" />
        <ti:insertAttribute name="extrascript" />

        <ti:insertAttribute name="style" />
        <ti:insertAttribute name="extrastyle" />

        <title> ESGF Portal</title>

    </head>

    <body>

        <!-- the banner spans the whole page so it is outside the blueprint container -->
        
		
        <div class="container showgrid" style="margin-top:20px;margin-bottom:20px;border:3px solid #eeeeee">
        	<!-- header -->
            <div id="header" >
                <ti:insertAttribute name="header" />
            </div>
            <c:set var="layoutType"><ti:insertAttribute name="layoutType"/></c:set>
            
		    <!-- main content filter-->
		    <!-- The first page content maps to / or /login -->
		  	<!-- Note each of these pages includes a banner -->
            <!-- The second page maps to /live -->
            <!-- The banner is not included, rather the text bar and pagination are substituted -->
		    <c:choose>
                <c:when test="${layoutType=='main'}">
                	<div id="banner" >
	                		<ti:insertAttribute name="banner" />
	            	</div>
                    <div class="span-24 last" id="main">
                         <ti:insertAttribute name="main" />
                    </div>
                </c:when>
                <c:when test="${layoutType=='accountsview'}">
                    <div class="span-24 last" id="main">
                         <ti:insertAttribute name="main" />
                    </div>
                </c:when>
                <c:when test="${layoutType=='adminview'}">
                    <div class="span-24 last" id="main">
                         <ti:insertAttribute name="main" />
                    </div>
                </c:when>
                <c:when test="${layoutType=='usermanagement'}">
                    <div class="span-24 last" id="main">
                         <ti:insertAttribute name="main" />
                    </div>
                </c:when>
                <c:when test="${layoutType=='creategroups'}">
                    <div class="span-24 last" id="main">
                         <ti:insertAttribute name="main" />
                    </div>
                </c:when>
                <c:when test="${layoutType=='left6-main18'}">
                	<div class="span-24 searchBorder" style="border: 1px solid #eeeeee; width:948px" >
	                	<div class="span-24 last" id="subheader" style="width:948px">
	                		<ti:insertAttribute name="subheader" />
	            		</div>
	                    <div class="span-6" id="left">
	                        <ti:insertAttribute name="left" />
	                    </div>
	                    <div class="span-18 last" id="main" style="width:708px;" >
	                        <ti:insertAttribute name="main" />
	                    </div>
                	</div>
                </c:when>
        	</c:choose>
        	<!-- footer -->
        	<div id="footer" >
                <ti:insertAttribute name="footer" />
            </div>
        </div>

    </body>
</html>
