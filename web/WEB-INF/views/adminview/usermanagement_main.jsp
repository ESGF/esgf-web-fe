<%@ include file="/WEB-INF/views/common/include.jsp" %>

<script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery-1.4.2.min.js" /> '></script>
    <!-- 
    <script type="text/javascript" src='<c:url value="/scripts/esgf/esgf-search-setting.js" />'> </script>
    -->
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery-ui-1.8.12.custom.min.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/overlay.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/overlay.apple.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/toolbox.mousewheel.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/scrollable.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/scrollable.navigator.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/scrollable.autoscroll.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/tooltip.js" /> '></script>

    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery.tmpl.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery.livequery.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery.autocomplete.js" /> '></script>


<style>
	table tbody tr:hover { background: #aaa; }
	
	
	#toggleMenu {
	    padding: 0px 0px 0px 0px; 
	    margin: 15px 0px 30px 0px;
	    clear: left;
	    float: left; 
	    width: 220px;
	    text-align: left;
	}
	
	#toggleMenu h4 {
	    /*background: #FFFFFF url('http://www.unidata.ucar.edu/img/v3/closed.png') repeat-x bottom left; */
	    background: #aaa;
	    /* open http://www.unidata.ucar.edu/img/v3/open.png */
	    border: 1px solid #EEEEEE;
	    -moz-border-radius: 4px;
	    -webkit-border-radius: 4px;
	    -khtml-border-radius: 4px;
	    border-radius: 4px;
	    margin: 4px 0px 0px 0px;
	    padding: 0px 0px 0px 23px;
	    line-height: 32px; 
	    font-weight: normal;
	    white-space: nowrap;
	    text-decoration: none; 
	    color: #696969;
	    cursor:pointer;
	    font-size: 13px;
	}
	.togglebox {
	    -moz-border-radius-bottomleft: 4px;
	    -moz-border-radius-bottomright: 4px;
	    -webkit-border-bottom-left-radius: 4px;
	    -webkit-border-bottom-right-radius: 4px;
	    -khtml-border-radius-bottomleft: 4px;
	    -khtml-border-radius-bottomright: 4px;
	    border-bottom-right-radius: 4px;
	    border-bottom-left-radius: 4px;
	    overflow: hidden;
	    clear: both;
	    padding: 0px 0px 0px 0px; 
	    margin: 0px 0px 0px 0px;
	    border-bottom: 1px solid #EEEEEE;
	    border-left: 1px solid #EEEEEE;
	    border-right: 1px solid #EEEEEE;
	    /* background: #FFFFFF url('/img/v3/vertMenuBG.png') repeat-x bottom left;  */
	    background: #FFFFFF;
	}
	
	
	.togglebox .block {
    padding: 0px 0px 0px 0px; 
    margin: 0px 0px 0px 0px;
}


.togglebox .block ul {
    list-style: none;
    padding: 5px 0px 10px 0px;
    margin: 0px 0px 0px 0px;
}

.togglebox .block ul li {
    font-size: 11px;
    line-height: 20px;
    height: 20px;   
    padding: 0px 0px 0px 45px;
    margin: 0px 0px 0px 0px;
    /* background: transparent url(/img/v3/bullet.png) no-repeat 26px center; */
}

.togglebox .block ul li a {
    color: #006996;
    text-decoration: none;
}

.togglebox .block ul li a:hover {
    color: #0088B5;
    text-decoration: underline;
}
	
	
	
	
</style>




<sec:authentication property="principal" var="principal"/>



<div style="margin-top:20px;margin-bottom:20px;min-height:500px;">
	<c:choose>
		<c:when test="${principal=='anonymousUser'}">
    		<div> <c:out value="${principal}"/> IS NOT AUTHORIZED TO VIEW THIS PAGE</div>
  		</c:when>
  		<c:otherwise>
      		<c:choose>
      			<c:when test="${principal.username=='https://pcmdi3.llnl.gov/esgcet/myopenid/jfharney'}">
  					<div style="margin-top:20px">
  					
  						<!--  header info -->
  						<div class="span-24 last">
  							<h2 style="text-align:center">
							Manage User Accounts
							</h2>
  						</div>
						
						<!-- user table -->
						<div class="span-16">
							<table id="table_id">  
	  
	    						<!-- Table header -->  
	  
						        <thead>  
						            <tr>  
						                <th>Last Name</th> 
						                  
						                <th>First Name</th>  
						                <th>User Name</th>  
						                <th>Email Address</th>  
						                <!--<th>Status</th>  
						                --> 
						            </tr>  
						        </thead>  
	  
						    <!-- Table footer -->  
						  	<!--  
						        <tfoot>  
						            <tr>  
						                  <td>footer1</td>  
						                  <td>footer2</td>  
						                  <td>footer1</td>  
						                  <td>footer2</td>  
						                  <td>footer2</td>  
						            </tr>  
						        </tfoot>  
						  	-->
						    <!-- Table body -->  
						  
						        <tbody>   
						        <c:set var="j" value="0"/>
						        <c:forEach var="user" items="${ManageUsers_user}">
									 <tr class="user_rows" id="${ManageUsers_user[j].userName}" style="cursor:pointer">  
						                <td>${ManageUsers_user[j].lastName}</td> 
						                <td>${ManageUsers_user[j].firstName}</td>  
						                <td>${ManageUsers_user[j].userName}</td>  
						                <td>${ManageUsers_user[j].emailAddress}</td>    
						                <!--
						                <td>body55</td> 
						                -->  
						            </tr> 
						            <c:set var="j" value="${j+1}"/>
									
								</c:forEach>
						           
						            
						           
						        </tbody>  
						  
							</table> 
						</div>
						<div class="span-8 last">
							<div id="user_info_header"></div>
							
							<div id="toggleMenu"> 
 								<h4>Account Summary</h4> 
      							<div class="togglebox"> 
       								<div class="block"> 
       								Account Summary
       								</div> 
     							</div>
     							<h4>Group Roles</h4> 
      							<div class="togglebox"> 
       								<div class="block"> 
       								Group Roles
       								</div> 
     							</div>
							</div>
						
					 
						</div>
  				</c:when>
  				<c:otherwise>
  					<div> <c:out value="${principal.username}"/> IS NOT AUTHORIZED TO VIEW THIS PAGE</div>
  				</c:otherwise>
      		</c:choose>
  		</c:otherwise>
	</c:choose>      
</div>

<script>
$(document).ready(function(){
	
	/**
	*	Will display the user's information
	*/
	
	$('tr.user_rows').click(function(){
		var userName = $(this).attr("id");
		$('div#header_name').remove();
		$('div#user_info_header').append('<div id="header_name" style="text-align:center">' + userName + '</div>');
		
	});
	
	
	
	$("h4").click(function(){
        $(this).next(".togglebox").slideToggle("fast");
        $(this).toggleClass('open');
        var index = ($(this).index() /2);

        return true;
    });
});


</script>
