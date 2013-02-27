<%@ include file="/WEB-INF/views/common/include.jsp" %>


<script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery-1.4.2.min.js" /> '></script>
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

	<script type="text/javascript" src='<c:url value="/scripts/esgf/esgf-core.js" /> '></script>
	<script type="text/javascript" src='<c:url value="/scripts/esgf/usermanagement.js" /> '></script>
	

    <link rel="stylesheet"
        href='<c:url value="/styles/lightgray/jquery-ui-1.8.10.custom.css" />'
        type="text/css" media="screen">	

<div class="span-24 last" style="margin-top:20px;min-height:500px;">

	<div class="span-23 last">
		<h2 style="text-align:center">
		SRM GO Data Transfer Page
		</h2>
	</div>
	
	<div class="span-12 prepend-1">
	

		<div id="show_params" >
			<div>GO_HASH_ID: ${go_hash_id}</div>
			<div id="go_hash_id" style="display:none">${go_hash_id}</div>
			<div>TYPE: ${go_type}</div>
			<div id="go_type" style="display:none">${go_type}</div>
			<div>CREDENTIAL: ${go_credential}</div>
			<div id="go_type" style="display:none">${go_credential}</div>
			<div>ID: ${go_id}</div>
			<div id="go_id" style="display:none">${go_id}</div>
			
			<div >CHILD_URL: </div>
			<div id="go_child_url">
				<c:set var="j" value="0"/>
				<table>
		        <c:forEach var="user" items="${go_child_url}">
					 <tr class="go_child_url_rows" 
					 	 style="">  
		                <td>${go_child_url[j]}</td>  
		            </tr> 
					<c:set var="j" value="${j+1}"/>
				</c:forEach>
				</table>
			</div>
			
			<div>CHILD_ID: </div>
			<div id="go_child_id">
				<c:set var="j" value="0"/>
				<table>
		        <c:forEach var="user" items="${go_child_id}">
					 <tr class="go_child_id_rows" 
					 	 style="">  
		                <td>${go_child_id[j]}</td>  
		            </tr> 
					<c:set var="j" value="${j+1}"/>
				</c:forEach>
				</table>
			</div>
		</div>
	
	
		<input id="go_srm_submission" type="submit" value="Submit Globus Online Request" />
	
	
	</div>
</div>

<script>

$(document).ready(function(){
	
	$('input#go_srm_submission').click(function() {
		var go_hash_id = $('#go_hash_id').html();
		var go_type = $('#go_type').html();
		var go_credential = $('#go_credential').html();
		var go_id = $('#go_id').html();
		//alert($('#go_child_url').html());
		var go_child_urls = new Array();
		$('#go_child_url').find('tr.go_child_url_rows').each( function(index) {
    		
    		//alert($(this).find('td').html());
    		var go_child_url = $(this).find('td').html();
    		go_child_urls.push(go_child_url);
        	
		});
		
		var go_child_ids = new Array();
		$('#go_child_id').find('tr.go_child_id_rows').each( function(index) {
    		
    		//alert($(this).find('td').html());
    		var go_child_id = $(this).find('td').html();
    		go_child_ids.push(go_child_id);
        	
		});
		
		//alert(go_child_ids);
		
		
		var globus_url = '/esgf-web-fe/goauthview1';

        var openid = $('span.footer_openid').html();
        
        //begin assembling queryString
        var queryString = 'type=' + go_type + '&id=' + go_id + '&credential=' + go_credential;
        
        for(var i=0;i<go_child_urls.length;i++) {
        	queryString += '&child_url=' + go_child_urls[i];
        }
        for(var i=0;i<go_child_ids.length;i++) {
        	queryString += '&child_id=' + go_child_ids[i];
        }
        
        alert('queryString: ' + queryString);
        
        var input = '';
        jQuery.each(queryString.split('&'), function(){
          var pair = this.split('=');
          input+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />';
        });
        
        /*
        //send request
        jQuery('<form action="'+ globus_url +'" method="post">'+input+'</form>')
        .appendTo('body').submit().remove();
        */
       
		
	});
		
});
</script>