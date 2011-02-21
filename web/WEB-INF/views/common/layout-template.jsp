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

            <!-- sub-header -->
            <div class="span-24 last" id="subheader" >
                <ti:insertAttribute name="subheader" />
            </div>

            <!-- main content organized according to different layout geometries -->
            <c:set var="layoutType"><ti:insertAttribute name="layoutType"/></c:set>
            <c:choose>
                <c:when test="${layoutType=='main'}">
                    <div class="span-24 last" id="main">
                         <ti:insertAttribute name="main" />
                    </div>
                </c:when>

                <c:when test="${layoutType=='left-main-search'}">
                    <div class="span-4 last" id="left">
                        <ti:insertAttribute name="left" />
                    </div>
                    <div class="prepend-1 span-19 last" id="main">
                        <ti:insertAttribute name="main" />
                    </div>
                </c:when>

                <c:when test="${layoutType=='left-main'}">
                    <div class="span-5 last" id="left">
                         <ti:insertAttribute name="left" />
                    </div>
                    <div class="prepend-1 span-18 last" id="main">
                         <ti:insertAttribute name="main" />
                    </div>
                </c:when>
                <c:when test="${layoutType=='left-main-right'}">
                    <div class="span-5 last" id="left">
                         <ti:insertAttribute name="left" />
                    </div>
                    <div class="prepend-1 span-12" id="main">
                         <ti:insertAttribute name="main" />
                    </div>
                    <div class="prepend-1 span-5 last" id="right">
                         <ti:insertAttribute name="right" />
                    </div>
                </c:when>
                <c:otherwise>Unknown Layout</c:otherwise>
            </c:choose>

            <!-- footer -->
            <div class="span-24 last" id="footer">
                <ti:insertAttribute name="footer" />
            </div>

        </div>

    </body>
</html>
