<%@ include file="/WEB-INF/views/common/include.jsp" %>

<script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery-1.6.4.min.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery-ui-1.8.12.custom.min.js" /> '></script>

    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/ui.dropdownchecklist-1.4-min.js" /> '></script>

    

    <link rel="stylesheet"
        href='<c:url value="/styles/ui.dropdownchecklist.standalone.css" />'
        type="text/css" media="screen, projection">
    <link rel="stylesheet"
        href='<c:url value="/styles/ui.dropdownchecklist.themeroller.css" />'
        type="text/css" media="screen, projection">
            
 
<div class="span-24 last" style="margin-top:40px;min-height:500px;" class="metadata_page">
 
	<%@ include file="metadata_header.jsp" %>
	
	<%@ include file="metadata_info.jsp" %>
	
	<%@ include file="file_info.jsp" %>

</div>



<script type="text/javascript">

$(function(){
   
	
	
	

   
	//check the cookie
	var ids = getIdStr();

	var metadata_id = $('#metadata_id').html();
	
	
	if(ids != undefined) {
		
		var idArr = ids.split(';');
				
		for(var i=0;i<idArr.length;i++) {
			//alert('id: ' + idArr[i].length + ' ' + metadata_id.length + ' ' + (idArr[i] == metadata_id));
			if(idArr[i] == metadata_id) {
				$('#metadata_addCart').html('Remove from Cart');
			}
			
			
		}
		
	}
	
	//reveal the page here
	$('.metadata_page').show();
	
	$('tr.meta_rows').each(function(index) {
		var text = $(this).find('.celllong').html();
		//alert(text.length);
		if(text.length > 75) {
   
			$(this).find('.textshort').html(text.substring(0,125) + '...');
			$(this).find('.meta_link').show();
		}
	});

	$('.meta_rows').show();
 
	//end initialization
	
	
	$("#s3").dropdownchecklist({ 
		maxDropHeight: 250,
		firstItemChecksAll: true,
		forceMultiple: true,
		emptyText: "Please select ...", 
		width: 150,
		onComplete: function(selector) {
			for( var i=0; i < selector.options.length; i++ ) {
				if(selector.options[i].selected == true) {
					$('#row_' + selector.options[i].value).show();
				} else {
					$('#row_' + selector.options[i].value).hide();
				}
			}
	    } 
	});
	
	
	$('a.meta_link').click(function() {
		var expandLink = $(this).html();
 
		if(expandLink == 'expand') {

			$(this).parent().parent().find('.cellshort').hide();
			$(this).parent().parent().find('.celllong').show();
			$(this).html('collapse');
		} else {
			$(this).parent().parent().find('.celllong').hide();
			$(this).parent().parent().find('.cellshort').show();
			$(this).html('expand');
		}
 
	});

	
	

	$('#metadata_addCart').click(function() {
 		//alert('Add to cart');
 		//var text = $('#metadata_addCart').html();
 		if(this.innerHTML == 'Add to Cart') {
  			this.innerHTML = 'Remove from Cart';
  			
  			var numFiles = $('#metadata_numFiles').text();
  			var peer = $('#metadata_peer').text();
  			var xlink = $('#metadata_xlink').text();
  			
  			alert('numFiles: ' + numFiles + ' peer: ' + peer + ' xlink: ' + xlink);
  			
  			if(numFiles == '') {
  				numFiles = 0;
  			}
  			if(peer == '') {
  				peer = 'undefined';
  			}
  			if(xlink = '') {
  				xlink = 'undefined';
  			}
  			
  			
  			
  			var datasetInfo = {'numFiles' : numFiles, 
  								'peer' : peer , 
  								'xlink' : xlink };

  			ESGF.localStorage.put('dataCart',metadata_id,datasetInfo);
  
  			
  			
 		} else {
  			this.innerHTML = 'Add to Cart';
 		}
 
 
	});

	$('#metadata_currentSearch').click(function() {
 		alert('display current search constraints');
	});

	$('#metadata_viewFiles').click(function() {
 		
 		if(this.innerHTML == 'View Files') {
 			this.innerHTML = 'Hide Files';

 	 		$('#files_section').show();

 			//location.href='#files_section';
 			
 		} else {
 			this.innerHTML = 'View Files';
 	 		$('#files_section').hide();
 		}	
 		

 
	});
	
	$('#metadata_viewMetadata').click(function() {
 		
 		if(this.innerHTML == 'View Metadata') {
 			this.innerHTML = 'Hide Metadata';

 	 		$('#metadata_section').show();

 			//location.href='#metadata_section';
 			
 		} else {
 			this.innerHTML = 'View Metadata';
 	 		$('#metadata_section').hide();
 		}	
 		

 
	});
	
		
	var queryStr = {'id' : metadata_id};	
		
	$.ajax({
		url: '/esgf-web-fe/metadataview_files',
		global: false,
		type: "GET",
		dataType: 'json',
		data: queryStr,
		success: function(data) {
			var fileList = '<table>';
			fileList += '<th>File Name</th>';
			fileList += '<th>File Size</th>';
			for(var i=0;i<data.files.fileId.length;i++) {
				fileList += '<tr>';
				fileList += '<td>' + data.files.fileId[i] +  '</td>'; 
				fileList += '<td>' + data.files.size[i] +  '</td>'; 
				fileList += '</tr>';
			}
			fileList += '</table>';
			$('#fileList').append(fileList);
		},
		error: function() {
			alert('error');
		}
	
	});
});

</script>