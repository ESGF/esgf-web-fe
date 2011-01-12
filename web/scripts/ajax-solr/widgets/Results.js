/**
 * Results.js
 */






(function ($) {

AjaxSolr.ResultWidget = AjaxSolr.AbstractWidget.extend({
	
  beforeRequest: function () {
    $(this.target).html($('<img/>').attr('src', 'images/ajax-loader.gif'));
  },

  facetLinks: function (facet_field, facet_values) {
    var links = [];
    if (facet_values) {
      for (var i = 0, l = facet_values.length; i < l; i++) {
        links.push(AjaxSolr.theme('facet_link', facet_values[i], this.facetHandler(facet_field, facet_values[i])));
      }
    }
    return links;
  },

  facetHandler: function (facet_field, facet_value) {
	    var self = this;
	    return function () {
	      self.manager.store.remove('fq');
	      self.manager.store.addByValue('fq', facet_field + ':' + facet_value);
	      self.manager.doRequest(0);
	      return false;
	    };
  },
  
  
  afterRequest: function () {
	  
		
	    $(this.target).empty();
	    for (var i = 0, l = this.manager.response.response.docs.length; i < l; i++) {
	    //if(i < 1) //for debugging the metadata report (remove when done)
	    	//{
	      var doc = this.manager.response.response.docs[i];
	      $(this.target).append(AjaxSolr.theme('result', doc, AjaxSolr.theme('snippet', doc)));
	      //$(this.target).append(AjaxSolr.theme('result2', doc, AjaxSolr.theme('snippet2', doc)));
	      var items = [];
	      //var theDoc = findDocumentByTitle(doc.title,this.manager.response.response.docs);
	      //alert(theDoc.title);
	      //items = items.concat(this.facetLinks('topics', doc.topics));
	      //items = items.concat(this.facetLinks('organisations', doc.organisations));
	      //items = items.concat(this.facetLinks('exchanges', doc.exchanges));
	      //AjaxSolr.theme('list_items', '#links_' + doc.id, items);
	    	//}
	    }
	    
  },
  
  
  init: function () {
	  $('a.more').livequery(function () {
		  $(this).toggle(function () {
		        $(this).parent().find('span').show();
		        $(this).text('... less');
		        return false;
		  }, function () {
		        $(this).parent().find('span').hide();
		        $(this).text('... more');
		        return false;
		  });
		  
		  
		  
	  });
	  
	  
	  $('a.met').livequery(function () {

		  $(this).click(function () {
			
			
			//need to gather the following here...
			//metadatafilename
			//metadataformat
			//id
			var id = $(this).next().attr('id');
			var metadatafilename = 'metadatafilename';
			var metadatafileformat = 'metadatafileformat';
				
			
			//send the info to the metadata_link
			metadata_report(id,metadatafilename,metadatafileformat);
			
			
			/*
			var title = $(this).next().attr('title');
			var url = $(this).next().attr('url');
			var description = $(this).next().attr('description');
		  	var wd = $(this).next().attr('west_degrees');
		  	var ed = $(this).next().attr('east_degrees');
		  	var nd = $(this).next().attr('north_degrees');
		  	var sd = $(this).next().attr('south_degrees');
		  	var dStart = $(this).next().attr('datetime_start');
		  	var dStop = $(this).next().attr('datetime_stop');
		  	*/
			//alert(this.manager.response.response.docs.length);
		  	//metadata_link(id,title,url,description,wd,ed,nd,sd,dStart,dStop);
		  	//metadata_link();
		  });
		  });
	  
  }
  
   
	
});




	


})(jQuery);


/*function metadata_link(eg)
{

	alert('in metadata 1 and sent...' + eg);
}*/

/*$('a.met').livequery(function () {

$(this).click(function () {
	//alert($(this).parent().find('div').show());
	//var el = $(this).next().css('background-color', 'red');;
	var eg = $(this).next().attr('title');
	//alert('ppp' + el);
	alert('qqq' + eg);
	metadata_l(eg);
	//var doc = findDocumentByTitle(eg,this.manager.response.response.docs);
});
});
*/


/*function findDocumentByTitle(titleName,docs) {
	  var returnedDoc = docs[0];
	  for (var i = 0, l = docs.length; i < l; i++) {
		  if(docs[i].title == titleName)
		  {
			  //alert('found title name! ' + titleName + ' counter ' + i);
			  returnedDoc = docs[i];
		  }
	  }	
	  return returnedDoc;
}*/




/*
 * Metadata report
 */




/*

var popupStatusMetadataReport = 0;

var metadataString =
'	<div id="popupMetadataReport" >'+
'<a id="popupMetadataReportClose">x</a>'+
'<h1>Metadata Report</h1>'+
'<div id="metadata_download">Download</div>'+
'<div id="metadata_title_gap"></div>'+
'<div id="metadata_return">Print</div>'+
'<p>'+
'Metadata report for...'+
'</p>'+

'<!-- root element for scrollable -->'+ 
'<div class="scrollable verticalscroll">'+ 
'	<!-- root element for the items --> '+
'	<div class="itemsscroll">'+
		
		
'		<div>'+
		
'			<div class="shortitem">'+ 
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" />'+ 
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>Title</h3> '+
'				</div>'+
				
'				<div class="rightsd">'+
'					<div id="title"></div>'+
		
'				</div>'+
				
'			</div> '+
'		</div>'+
		
'		<div>'+
		
'			<div class="shortitem"> '+
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" /> '+
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>URL:</h3> '+
'				</div>'+
				
'				<div class="rightsd">'+
'					<div id="url"></div>'+
		
'				</div>'+
				
'			</div> '+
'		</div>'+
		
'		<div>'+
		
'			<div class="shortitem"> '+
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" /> '+
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>Project Leader(s):</h3> '+
'				</div>'+
				
'				<div class="rightsd">'+
'					<div id="leader"></div>'+
		
'				</div>'+
				
'			</div>'+ 
'		</div>'+
		
'		<div>'+
		
'			<div class="shortitem"> '+
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" /> '+
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>Contact Info:</h3> '+
'				</div>'+
				
'				<div class="rightsd">'+
'					<div id="contact_metadata"></div>'+
		
'				</div>'+
				
'			</div> '+
'		</div>'+
		
'		<div>'+
		
'			<div class="geospatial_item"> '+
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" /> '+
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>Geospatial Information:</h3> '+
'				</div>'+
				
				
'				<div class="rightsd">'+
'					<div id="geo_info">'+
'					Geographic boundaries'+
'						<div id="geospatial_metadata"></div>'+
'					</div>'+
					
'					<div id="geo_map">map here</div>'+
'				</div>'+
				
'			</div> '+
'		</div>'+
		
'		<div>'+
		
'			<div class="geospatial_item"> '+
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" /> '+
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>Temporal Information:</h3> '+
'				</div>'+
				
'				<div class="rightsd">'+
'					<div id="temporal_metadata"></div>'+
		
'				</div>'+
				
'			</div> '+
'		</div>'+
		
'		<div>'+
		
'			<div class="shortitem"> '+
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" /> '+
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>Keywords:</h3> '+
'				</div>'+
				
'				<div class="rightsd">'+
'					<div id="keyword_metadata"></div>'+
		
'				</div>'+
				
'			</div> '+
'		</div>'+
		
		
		
'		<div>'+
		
'			<div class="abstract_item"> '+
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" /> '+
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>Abstract:</h3> '+
'				</div>'+
				
'				<div class="rightsd">'+
'					<div id="abstract"></div>'+
		
'				</div>'+
				
'			</div> '+
'		</div>'+
'	 	</div>'+

	
'</div>'+
'</div>'+


'<div id="backgroundPopupMetadataReport"></div>';



//loading metadata report popup!
function loadPopupMetadataReport(){
	//loads popup only if it is disabled
	if(popupStatusMetadataReport==0){
		$("#backgroundPopupMetadataReport").css({
			"opacity": "0.8"
		});
		$("#backgroundPopupMetadataReport").fadeIn("slow");
		$("#popupMetadataReport").fadeIn("slow");
		popupStatusMetadataReport = 1;
	}
}




//disabling metadata report popup!
function disablePopupMetadataReport(){
	//disables popup only if it is enabled
	if(popupStatusMetadataReport==1){
		$("#backgroundPopupMetadataReport").fadeOut("slow");
		$("#popupMetadataReport").fadeOut("slow");
		popupStatusMetadataReport = 0;
	}
}


//centering popup
function centerPopupMetadataReport(){
	//request data for centering
	var windowWidth = document.documentElement.clientWidth;
	var windowHeight = document.documentElement.clientHeight;
	var popupHeight = $("#popupMetadataReport").height();
	var popupWidth = $("#popupMetadataReport").width();
	//centering
	$("#popupMetadataReport").css({
		"position": "absolute",
		"top": windowHeight/2-popupHeight/2,
		"left": windowWidth/2-popupWidth/2
	});
	//only need force for IE6
	
	$("#backgroundPopupMetadataReport").css({
		"height": windowHeight
	});
	
}


*/

//function metadata_link(doc)
//{
	//alert(doc.title);
	/*$('div#metadata_overlay').after(metadataString);
	if(popupStatusMetadataReport==0){
		$("#backgroundPopupMetadataReport").css({
			"opacity": "0.8"
		});
		$("#backgroundPopupMetadataReport").fadeIn("slow");
		$("#popupMetadataReport").fadeIn("slow");
		popupStatusMetadataReport = 1;
	}*/
	
	
	/*
	var title = 'unknown';
	var description = 'unknown';
	var west_degrees = '0';
	var east_degrees = '0';
	var north_degrees = '0';
	var south_degrees = '0';
	var datetime_start = 'unknown';
	var datetime_stop = 'unknown';
	
	$('.added1').remove();
	$('div#title').after('<div class="added1"><p>' + title + '</p></div>');
	$('div#abstract').after('<div class="added1"><p>' + description + '</p></div>');
	$('div#leader').after('<div class="added1"><p>' + 'John Harney' + '</p></div>');
	$('div#geospatial_metadata').after('<div class="added1"><p>west degrees: ' + west_degrees + '</p></div>');
	$('div#geospatial_metadata').after('<div class="added1"><p>east degrees: ' + east_degrees + '</p></div>');
	$('div#geospatial_metadata').after('<div class="added1"><p>north degrees: ' + north_degrees + '</p></div>');
	$('div#geospatial_metadata').after('<div class="added1"><p>south degrees: ' + south_degrees + '</p></div>');
	$('div#temporal_metadata').after('<div class="added1"><p>end: ' + datetime_stop + '</p></div>');
	$('div#temporal_metadata').after('<div class="added1"><p>begin: ' + datetime_start + '</p></div>');

	display_meta_map ();
	*/
//}