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

	<div class="span-23 last">
		<h2 style="text-align:center">
		SRM Data Transfer Page
		</h2>
	</div>
	<div class="span-22 prepend-1 last" style="margin-top:20px;margin-bottom:20px">
	
	Welcome to the Storage Resource Management request page.  
	The data that you have selected is currently located in a tertiary storage device and will require some time to retrieve.  
	Please select from the options below.  
	
	
	Note that if you select one of the "Submit request" options, you will be receive two emails from ESGF.  The first will
	confirm that the request has been submitted.  The second will notify you that the data is available on disk with further 
	access instructions.   
	
	</div>
	<div class="span-12 prepend-1">
	
		
		
		<input id="srm_workflow" type="submit" value="Submit SRM Request (WGET)">
		<input id="srm_workflow_go" type="submit" value="Submit SRM Request (Globus Online)">
		<input id="srm_workflow_guc" type="submit" value="Submit SRM Request (Globus URL Copy)">
		<input id="show_files" type="submit" value="View Files with Dataset(s)">
		<input id="show_params_sent" type="submit" value="Show Param(s)">
		<input id="back" type="submit" value="Back to Search Page">
		
	</div>
	
	<div class="span-24 prepend-1 last" style="margin-top:40px;">
		<div id="srm_response"></div>
	</div>
	<div class="span-18 prepend-1 last" style="margin-top:30px;margin-bottom:30px">
	
		<div id="show_params" style="display:none">
			<div>Type: ${type}</div>
			<div id="type" style="display:none">${type}</div>
			<div>Dataset: ${dataset_id}</div>
			<div id="dataset_id" style="display:none">${dataset_id}</div>
			<div>filtered: ${filtered}</div>
			<div id="filtered" style="display:none">${filtered}</div>
			<div>file_id: ${file_id}</div>
			<div id="file_id" style="display:none">${file_id}</div>
			<div>file_url: ${file_url}</div>
			<div id="file_url" style="display:none">${file_url}</div>
			<div>peerStr: ${peerStr}</div>
			<div id="peerStr" style="display:none">${peerStr}</div>
			<div>technoteStr: ${technoteStr}</div>
			<div id="technoteStr" style="display:none">${technoteStr}</div>
			<div>fqParamStr: ${fqParamStr}</div>
			<div id="fqParamStr" style="display:none">${fqParamStr}</div>
			<div>initialQuery: ${initialQuery}</div>
			<div id="initialQuery" style="display:none">${initialQuery}</div>
			<div>fileCounter: ${fileCounter}</div>
			<div id="fileCounter" style="display:none">${fileCounter}</div>
			
		
		</div>
		
		
		
	</div>
	
	<div class="span-18 prepend-1 last" style="margin-top:30px;margin-bottom:30px">
	
		<div id="file_contents" style="display:none;font-weight:bold">
			File(s) to be transferred from HPSS
			<div id="files" style="font-size:10px">
			
			</div>
		</div>
		
		
		
	</div>
</div>

<!-- scratch space for any additional scripts
 -->
<script>

$(document).ready(function(){
	
	$('input#back').click(function() {
		location.href='/esgf-web-fe/live';
	});
	
	
	$('input#show_params_sent').click(function() {
		$('#show_params').toggle();
	});
	
	$('input#show_files').click(function() {
		if($('input#show_files').val() == 'View Files with Dataset(s)') {
			$('input#show_files').val('Hide Files with Dataset(s)');
			
			$('div#file_contents').show();
			
			var type = $('#type').html();
			var dataset_id = $('#dataset_id').html();
			var filtered = $('#filtered').html();
			var file_id = $('#file_id').html();
			var file_url = $('#file_url').html();
			
			
			var queryStr = { 
					'file_id':file_id,
				 	'file_url':file_url,
					'constraints': fqParamStr,
				 	'dataset_id':dataset_id,
				 	'filtered':filtered,
				 	'type':type,
				 	//'scriptType':scriptType
				 	
				};

			LOG.debug('dataset_id: ' + queryStr['dataset_id']);
			LOG.debug('constraints: ' + queryStr['constraints']);
			LOG.debug('file_id: ' + queryStr['file_id']);
			LOG.debug('file_url: ' + queryStr['file_url']);
			LOG.debug('type: ' + queryStr['type']);
			//LOG.debug('scriptType: ' + queryStr['scriptType']);
			
			
			var srm_url = '/esgf-web-fe/srmfilesrequest';
			
			$.ajax({
				url: srm_url,
				global: false,
				type: "POST",
				data: queryStr,
				//dataType: 'xml',
				success: function(data) {
					for(var i=0;i<data.length;i++) {
						$('#files').append('<div>' + data[i] + '</div>')
					}
				},
				error: function() {
					alert('srm error');
				}
				
	    	});
			
			
			
		} else {
			$('input#show_files').val('View Files with Dataset(s)');
			$('div#file_contents').hide();
		}
	});
	
	
	$('input#srm_workflow_go').click(function() {
		
		var scriptType = 'GO';            
	
		alert('Globus online workflow is not supported at this time');
		
	});
	
	$('input#srm_workflow_guc').click(function() {
	
		
		//alert('in globus url copy workflow');
		
		var type = $('#type').html();
		var dataset_id = $('#dataset_id').html();
		var filtered = $('#filtered').html();
		var file_id = $('#file_id').html();
		var file_url = $('#file_url').html();
		
		var scriptType = 'gridftp';            
		
		var srm_url = '/esgf-web-fe/srmproxy';
		
		//alert('type: ' + type + ' dataset_id: ' + dataset_id);
		
		alert('An email has been sent to your account indicating that your request has been submitted.');
					
		if(type == 'File') {
			
			
			var queryStr = { 
								'file_id':file_id,
							 	'file_url':file_url,
								'constraints': fqParamStr,
							 	'dataset_id':dataset_id,
							 	'filtered':filtered,
							 	'type':type,
							 	'scriptType':scriptType
							 	
							}
			
			LOG.debug('dataset_id: ' + queryStr['dataset_id']);
			LOG.debug('constraints: ' + queryStr['constraints']);
			LOG.debug('file_id: ' + queryStr['file_id']);
			LOG.debug('file_url: ' + queryStr['file_url']);
			LOG.debug('type: ' + queryStr['type']);
			LOG.debug('scriptType: ' + queryStr['scriptType']);
			
			
			$.ajax({
				url: srm_url,
				global: false,
				type: "POST",
				data: queryStr,
				//dataType: 'xml',
				success: function(data) {
					alert('Your data has been staged and an email has been sent to your account.  Please follow the instructions included.');
					// $('#srm_response').append("Staging successfully launched"); 
				},
				error: function (request, status, error) {
			        alert("SRM Request error: " + request.responseText);
				}
				
	    	});
			
		} else {

			//query solr for file ids and urls before sending request
			var fqParamStr = $('#fqParamStr').html();
		
			var queryStr = { 
								'dataset_id': dataset_id,
								'constraints': fqParamStr,
								'type': type,
							 	'filtered':filtered,
								'file_id':file_id,
					 			'file_url':file_url,
							 	'scriptType':scriptType
					 	   }

			LOG.debug('dataset_id: ' + queryStr['dataset_id']);
			LOG.debug('constraints: ' + queryStr['constraints']);
			LOG.debug('file_id: ' + queryStr['file_id']);
			LOG.debug('file_url: ' + queryStr['file_url']);
			LOG.debug('type: ' + queryStr['type']);
			LOG.debug('scriptType: ' + queryStr['scriptType']);
			
			
			
			$.ajax({
				url: srm_url,
				global: false,
				type: "POST",
				data: queryStr,
				//dataType: 'xml',
				success: function(data) {
					alert('Your data has been staged and an email has been sent to your account.  Please follow the instructions included.');
					
				},
				error: function(jqXHR, textStatus,errorThrown) {
					//alert('error retrieving data from srm');
					alert('textStatus: ' + textStatus + ' errorThrown: ' + errorThrown);
				}
				
			});
			
			//$('#srm_response').append("Staging launched");
			
			
		}
		
		
	});
	
	
	
	$('input#srm_workflow').click(function() {

		//alert('in wget workflow');
		
		var type = $('#type').html();
		var dataset_id = $('#dataset_id').html();
		var filtered = $('#filtered').html();
		var file_id = $('#file_id').html();
		var file_url = $('#file_url').html();
		
		var scriptType = 'http';

		var srm_url = '/esgf-web-fe/srmproxy';
		
		alert('An email has been sent to your account indicating that your request has been submitted.');
		
		if(type == 'File') {
			
			
			var queryStr = { 
								'file_id':file_id,
							 	'file_url':file_url,
								'constraints': fqParamStr,
							 	'dataset_id':dataset_id,
							 	'filtered':filtered,
							 	'type':type,
							 	'scriptType':scriptType
							 	
							}
			
			LOG.debug('dataset_id: ' + queryStr['dataset_id']);
			LOG.debug('constraints: ' + queryStr['constraints']);
			LOG.debug('file_id: ' + queryStr['file_id']);
			LOG.debug('file_url: ' + queryStr['file_url']);
			LOG.debug('type: ' + queryStr['type']);
			LOG.debug('scriptType: ' + queryStr['scriptType']);
			
			
			$.ajax({
				url: srm_url,
				global: false,
				type: "POST",
				data: queryStr,
				//dataType: 'xml',
				success: function(data) {
					alert('Your data has been staged and an email has been sent to your account.  Please follow the instructions included.');
					// $('#srm_response').append("Staging successfully launched"); 
				},
				error: function() {
					alert('srm error');
				}
				
	    	});
			
		} else {

			//query solr for file ids and urls before sending request
			var fqParamStr = $('#fqParamStr').html();
		
			var queryStr = { 
								'dataset_id': dataset_id,
								'constraints': fqParamStr,
								'type': type,
							 	'filtered':filtered,
								'file_id':file_id,
					 			'file_url':file_url,
							 	'scriptType':scriptType
					 	   }

			LOG.debug('dataset_id: ' + queryStr['dataset_id']);
			LOG.debug('constraints: ' + queryStr['constraints']);
			LOG.debug('file_id: ' + queryStr['file_id']);
			LOG.debug('file_url: ' + queryStr['file_url']);
			LOG.debug('type: ' + queryStr['type']);
			LOG.debug('scriptType: ' + queryStr['scriptType']);
			
			
			
			$.ajax({
				url: srm_url,
				global: false,
				type: "POST",
				data: queryStr,
				//dataType: 'xml',
				success: function(data) {
					alert('An email has been sent to your account.  Please follow the instructions included.');
					
				},
				error: function(jqXHR, textStatus,errorThrown) {
					//alert('error retrieving data from srm');
					alert('textStatus: ' + textStatus);
					alert('errorThrown: ' + errorThrown);
					for(var key in jqXHR) {
						//alert('key: ' + key + ' value: ' + jqXHR[key]);
					}
				}
				
			});
			
			//$('#srm_response').append("Staging launched");
			
			
		}
		
		
		
		
		
		
		/*
		//var queryStr = {"file_ids" : file_ids};
		
		//type: File
		if(type == 'File') {
			
			alert('follow file workflow');
			
			//direct call to esg-srm
			
			//get the file_ids here
			var file_ids = new Array();
			file_ids.push(datasetId);
			
			queryStr = {"file_ids" : file_ids};
			
			alert('sending file_ids: ' + file_ids);
			
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
		*/
		
		
		
		
		
		
    	
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

/* show files script
var datasetId = $('#datasetId').html();
var type = $('#type').html();
var s_url = $('#s_url').html();


if(($('#file_contents').html() == 'Empty')) {
	
	$('#file_contents').empty();
	
	var file_ids = getFileIds(datasetId,type,s_url,null,null,null);
	
	var s_urls = getS_URLs(datasetId,type,s_url,null,null,null);
	
}

*/
</script>
