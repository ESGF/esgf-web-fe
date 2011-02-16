/**
 * 
 */


(function ($) {

AjaxSolr.AnnotatorWidget = AjaxSolr.AbstractWidget.extend({
  afterRequest: function () {
    var self = this;

    
	
	
    $("a.annotate").click(function(){
	});
    
    $(".ai_annotate a[rel]").overlay({

		mask: 'darkred',
		effect: 'apple',
		left: '5%',
		top: '5%',
		
		onBeforeLoad: function() {

			
			$('.apple_overlay').css({'width' : '750px'});
			
			// grab wrapper element inside content
			var wrap = this.getOverlay().find(".contentWrap");

			// load the page specified in the trigger
			wrap.load(this.getTrigger().attr("href"));
		},
	
		onLoad: function() {

			var keywordLimit = 10;
			var keywordCount = 0;
			var keywordArray = []; 
			
			//alert('loaded overlay');

			$( "#annotator_tabs").tabs();
			$( "#annotator_tabs").toggle();
			
			$("button").button();
			
			$("button#addKeyword").click(function(){
				alert('adding keyword')
				keywordArray[keywordCount] = $("input[name='quality']").val();
				var outputText = '';
				if(keywordCount != 0){
					outputText += ', ' + keywordArray[keywordCount];
				}else {
					outputText += keywordArray[keywordCount];
				}
				
				$('div#submission_list').append(outputText);
				keywordCount++;
				$('input#keywordannotationbox').attr("value", "");
				
			});
			 
			$("button#viewRecommendedKeyword").click(function(){
				$('div#keywordRecommendation').toggle();
			});
			
			$("button#submitKeyword").click(function(){
				alert('ajax call to backend');
			});
			
			$("#commentlist li").each(function (i) {
				i = i+1;
				$(this).prepend('<span class="commentnumber"> #'+i+'</span>');
			});
			

			
		},
		
		onClose: function() {
			$( "#annotator_tabs").toggle();
			
		}

	});
    
    /*
    $(".m a[rel]").overlay({
    	
		//mask: 'darkred',
		mask: {opacity: 0.5, color: '#000'},
		
		effect: 'apple',

		onBeforeLoad: function() {
			
			$('.apple_overlay').css({'width' : '640px'});
			//$('.apple_overlay').css({'background-image' : 'url(../images/metadata_overlay/white.png'});
			
			// grab wrapper element inside content
			var wrap = this.getOverlay().find(".contentWrap");
			// load the page specified in the trigger
			wrap.load(this.getTrigger().attr("href"));
			
		},
    
    	onLoad: function() {

    		id = globalRecordId;
    		var title = 'title';
    		
    		
    		metadata_report(id,title,metadatafilename,metadatafileformat);
    	  	
    	}

	});
	*/
  },

  removeFacet: function (facet) {
    var self = this;
    
  },

  
  init: function () {
	  
	 
  }
  
   
	
});



})(jQuery);




