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
    
    
<!-- scratch space for css -->
<style>
</style>

<div class="span-24 last" style="margin-top:20px;min-height:500px;">

	<div class="span-23">
		<h2 style="text-align:center">
		SRM Data Transfer Page
		</h2>
	</div>
	<div class="span-12 prepend-1">
		
		<div id="show_params" style="display:none">
			<div>Dataset: ${datasetId}</div>
			<div id="datasetId" style="display:none">${datasetId}</div>
			<div>
			Type: ${type}
			</div>
			<div id="type" style="display:none">${type}</div>
			<div>
			Type: ${s_url}
			</div>
			<div id="s_url" style="display:none">${s_url}</div>
			
			<!--  
			<div>
			peerStr: ${peerStr}
			</div>
			<div id="peerStr" style="display:none">${peerStr}</div>
			<div>
			technoteStr: ${technoteStr}
			</div>
			<div id="technoteStr" style="display:none">${technoteStr}</div>
			<div>
			fqParamStr: ${fqParamStr}
			</div>
			<div id="fqParamStr" style="display:none">${fqParamStr}</div>
			
		
			<div>
			Note: Blah blah blah
			</div>
			-->
		
		</div>
		
		<input id="srm_workflow" type="submit" value="Submit SRM Request">
		<input id="show_files" type="submit" value="View Files with Dataset(s)">
		<input id="show_params_sent" type="submit" value="Show Param(s)">
	</div>
	<div class="span-10 last">
		<div id="file_contents" style="display:none">Empty</div>
	</div>
	<div class="span-24 last">
		<div id="srm_response"></div>
	</div>
</div>

<!-- scratch space for any additional scripts
 -->
<script>

$(document).ready(function(){
	
	$('input#show_params_sent').click(function() {
		$('#show_params').toggle();
	
	});
	
	$('input#show_files').click(function() {
		if($('input#show_files').val() == 'View Files with Dataset(s)') {
			$('input#show_files').val('Hide Files with Dataset(s)');
			
			
			var datasetId = $('#datasetId').html();
			var type = $('#type').html();
			var s_url = $('#s_url').html();
			
			/*
			var peerStr = $('#peerStr').html();
			var technoteStr = $('#technoteStr').html();
			var fqParamStr = $('#fqParamStr').html();
			*/
			
			
			
			if(($('#file_contents').html() == 'Empty')) {
				
				$('#file_contents').empty();
				
				var file_ids = getFileIds(datasetId,type,s_url,null,null,null);
				
				var s_urls = getS_URLs(datasetId,type,s_url,null,null,null);
				
			}
			
			
			$('div#file_contents').show();
			
			
			
			
			
		} else {
			$('input#show_files').val('View Files with Dataset(s)');
			$('div#file_contents').hide();
		}
	});
	
	
	
	
	
	
	$('input#srm_workflow').click(function() {
		

		var datasetId = $('#datasetId').html();
		var type = $('#type').html();
		var peerStr = $('#peerStr').html();
		var technoteStr = $('#technoteStr').html();
		var fqParamStr = $('#fqParamStr').html();
		
		
		var srm_url = '/esgf-web-fe/srmproxy';
		
		//var queryStr = {"file_ids" : file_ids};
		
		//type: File
		if(type == 'File') {
			
			alert('follow file workflow');
			
			//direct call to esg-srm
			
			//get the file_ids here
			var file_ids = new Array();
			file_ids.push(datasetId);
			
			queryStr = {"file_ids" : file_ids};
			
			$.ajax({
				url: srm_url,
				global: false,
				type: "POST",
				data: queryStr,
				//dataType: 'xml',
				success: function(data) {
					$('#srm_response').append("Staging successfully launched");
				},
				error: function() {
					alert('srm error');
				}
				
	    	});
			
			
		} 
		//type: Dataset
		else {
			
			
			var file_ids = getFileIds(datasetId,type,peerStr,technoteStr,fqParamStr);
			
			queryStr = {"file_ids" : file_ids};
			
			$.ajax({
				url: srm_url,
				global: false,
				type: "POST",
				data: queryStr,
				//dataType: 'xml',
				success: function(data) {
					$('#srm_response').append("Staging successfully launched");
				},
				error: function() {
					alert('srm error');
				}
				
	    	});
		}
		
		
		
		
		/*
		if(type == 'File') {
			
			var srm_url = '/esgf-web-fe/srmproxy';
			
			
			//get the file_ids here
			var file_ids = new Array();
			file_ids.push(datasetId);
			
			var queryStr = {"file_ids" : file_ids};
			
			$.ajax({
				url: srm_url,
				global: false,
				type: "POST",
				data: queryStr,
				//dataType: 'xml',
				success: function(data) {
					alert('success');
				},
				error: function() {
					alert('srm error');
				}
				
	    	});
			
		} else {
			

			//need to query solr for the files and then send to esg-srm
			var queryStr = {"idStr" : datasetId, 
					"peerStr" : peerStr, 
					"technotesStr" : technoteStr, 
					"showAllStr" : "true", 
					"fqStr" : fqParamStr, 
					"initialQuery" : "true",
					"fileCounter" : 10};
			
			var file_ids = getFileIds(datasetId,type,peerStr,technoteStr,fqParamStr);
			
			
			var srm_url = '/esgf-web-fe/srmproxy';
			
			queryStr = {"file_ids" : file_ids};
			
			
			$.ajax({
				url: srm_url,
				global: false,
				type: "POST",
				data: queryStr,
				//dataType: 'xml',
				success: function(data) {
					alert('Your SRM request has been submitted successfully.');
					
				},
				error: function() {
					alert('Your SRM request has not been submitted successfully.  Please contact your administrator and try again.');
				}
				
				
	    	});
			
		
		}
		*/
		
		
		
		

    	//query solr for the files
    	
	});
    
});


function getS_URLs(datasetId,type,s_url,peerStr,technoteStr,fqParamStr) {
	var s_urls = new Array();
	
	if(type == 'File') {
		s_urls.push(s_url);
	}
	
	return s_urls;
}

function getFileIds(datasetId,type,surl,peerStr,technoteStr,fqParamStr) {
	var file_ids = new Array();
	
	if(type == 'File') {
		file_ids.push(datasetId);
	}
	
	return file_ids;
}

/*
function getFileIds(datasetId,type,peerStr,technoteStr,fqParamStr) {
	
	var file_ids = new Array();
	
	
	//need to query solr for the files and then send to esg-srm
	var queryStr = {"idStr" : datasetId, 
			"peerStr" : peerStr, 
			"technotesStr" : technoteStr, 
			"showAllStr" : "true", 
			"fqStr" : fqParamStr, 
			"initialQuery" : "true",
			"fileCounter" : 10};
	
	
	$.ajax({
		url: '/esgf-web-fe/solrfileproxy2/datacart',
		global: false,
		async: false,
		type: "GET",
		data: queryStr,
		dataType: 'json',
		success: function(data) {
			
			
			//grab the file ids and send to the esg-srm service
			for(var i=0;i<data.docs.doc.files.file.length;i++){
				var file = data.docs.doc.files.file[i];
				file_ids.push(file.fileId);

				$('#file_contents').append('<div>' + file.fileId + '</div>');
			}
			
			
			queryStr['initialQuery'] = false;
			
			$.ajax({
				url: '/esgf-web-fe/solrfileproxy2/datacart',
				global: false,
				async: false,
				type: "GET",
				data: queryStr,
				dataType: 'json',
				success: function(data) {
					//grab the file ids and send to the esg-srm service
					for(var i=0;i<data.docs.doc.files.file.length;i++){
						var file = data.docs.doc.files.file[i];
						file_ids.push(file.fileId);

						$('#file_contents').append('<div>' + file.fileId + '</div>');
					}	
				
				},
				error: function() {
					alert('errr');
				}
			});
			
		},
		error: function() {
			alert('error');
		}
	});
	
	return file_ids;
	
}
*/


function getIndividualPeer(id) {
	var peerStr = '';
	
	var datasetInfo = ESGF.localStorage.get('dataCart',id);
	
	
	if(datasetInfo['peer'] == null || datasetInfo['peer'] == undefined) {
		peerStr = '';
	} else {
		peerStr = datasetInfo['peer'];
	}
	
	
	return peerStr.toString();
	
}
</script>
