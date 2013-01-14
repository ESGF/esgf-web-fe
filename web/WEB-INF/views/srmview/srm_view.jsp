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
		
		<div>Dataset: ${datasetId}</div>
		<div id="datasetId" style="display:none">${datasetId}</div>
		<div>
		Type: ${type}
		</div>
		<div id="type" style="display:none">${type}</div>
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
		
		<input id="srm_workflow" type="submit" value="Submit SRM Request">
		<input id="show_files" type="submit" value="View Files with Dataset(s)">
	</div>
	<div class="span-10 last">
		<div id="file_contents" style="display:none">
			File Content here
		</div>
	</div>
</div>

<!-- scratch space for any additional scripts
 -->
<script>

$(document).ready(function(){
	
	$('input#show_files').click(function() {
		if($('input#show_files').val() == 'View Files with Dataset(s)') {
			$('input#show_files').val('Hide Files with Dataset(s)');
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
		
		
		if(type == 'File') {
			
			var srm_url = '/esgf-web-fe/srmproxy';
			
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
			
			alert('queryStr: ' + queryStr);
			
			$.ajax({
				url: '/esgf-web-fe/solrfileproxy2/datacart',
				global: false,
				type: "GET",
				data: queryStr,
				dataType: 'json',
				success: function(data) {
					
					var file_ids = new Array();
					
					//grab the file ids and send to the esg-srm service
					for(var i=0;i<data.docs.doc.files.file.length;i++){
						var file = data.docs.doc.files.file[i];
						file_ids.push(file.fileId);
						//alert((file));
					}
					
					alert('file_ids: ' + file_ids);


					var srm_url = '/esgf-web-fe/srmproxy';
					
					queryStr = {"file_ids" : file_ids};
					
					
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
					
					
					
					
					
				},
				error: function () {
					alert('error');
				}
			});
			
		
		}
		
		
		
		/*
		//extract the dataset Id from the span tag
		var selectedDocId = $(this).parent().html();//($(this).parent().parent().find('span.datasetId').html()).trim();
		
		alert('selectedDocId: ' + selectedDocId);

    	
		//var openid = $('span.footer_openid').html();

        var input = '';
		
        //begin assembling queryString
        
        alert('launch workflow here ' + srm_url);
		*/
		
        /*
        //send request
        jQuery('<form action="'+ srm_url +'" method="post">'+input+'</form>')
        .appendTo('body').submit().remove();
		//alert($(this).parent().find('span').html());
		*/
		

    	//query solr for the files
    	
	});
    
});

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
