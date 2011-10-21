AjaxSolr.AbstractOverlayWidget = AjaxSolr.AbstractWidget.extend(
{
	
	
	hideOverlay: function () {
	    $(".overlay_header").hide();
	    $(".overlay_content").hide();
	    $(".overlay_header_buttons").hide();    
	    $(".overlay_footer").hide();
	    $(".overlay_border").hide();
	},
	
	showOverlay: function () {
    	$(".overlay_header").show();
        $(".overlay_content").show();
        $(".overlay_footer").show();
        $(".overlay_border").show();
    }


});