/*****************************************************************************
 * Copyright © 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * “Licensor”) hereby grants to any person (the “Licensee”) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization’s name.  If the Software is protected by a proprietary
 * trademark owned by Licensor or the Department of Energy, then derivative
 * works of the Software may not be distributed using the trademark without
 * the prior written approval of the trademark owner.
 *
 * 2. Neither the names of Licensor nor the Department of Energy may be used
 * to endorse or promote products derived from this Software without their
 * specific prior written permission.
 *
 * 3. The Software, with or without modification, must include the following
 * acknowledgment:
 *
 *    "This product includes software produced by UT-Battelle, LLC under
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.”
 *
 * 4. Licensee is authorized to commercialize its derivative works of the
 * Software.  All derivative works of the Software must include paragraphs 1,
 * 2, and 3 above, and the DISCLAIMER below.
 *
 *
 * DISCLAIMER
 *
 * UT-Battelle, LLC AND THE GOVERNMENT MAKE NO REPRESENTATIONS AND DISCLAIM
 * ALL WARRANTIES, BOTH EXPRESSED AND IMPLIED.  THERE ARE NO EXPRESS OR
 * IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE,
 * OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY PATENT, COPYRIGHT,
 * TRADEMARK, OR OTHER PROPRIETARY RIGHTS, OR THAT THE SOFTWARE WILL
 * ACCOMPLISH THE INTENDED RESULTS OR THAT THE SOFTWARE OR ITS USE WILL NOT
 * RESULT IN INJURY OR DAMAGE.  The user assumes responsibility for all
 * liabilities, penalties, fines, claims, causes of action, and costs and
 * expenses, caused by, resulting from or arising out of, in whole or in part
 * the use, storage or disposal of the SOFTWARE.
 *
 *
 ******************************************************************************/

/*
 * @author		Feiyi Wang
 * Created: 	October 12, 2010
 */

$(document).ready(function(){


    //global vars

    var searchBox = $("#searchbox");
    var data = $("#searchbox").val();
    var searchBoxDefault = "Search projects, datasets, and more";


    //Effects for both searchbox
    searchBox.focus(function(e){
        $(this).addClass("active");
    });
    searchBox.blur(function(e){
        $(this).removeClass("active");
    });

    searchBox.focus(function(){
        if($(this).attr("value") == searchBoxDefault) $(this).attr("value", "");
    });
    searchBox.blur(function(){
        if($(this).attr("value") == "") $(this).attr("value", searchBoxDefault);
    });


    /**
     * handle reset link
     */
    $("div#showReset").click(function(){
        if ($('div#optionPane').is(":visible")) {
            $('div#optionPane').hide();
            $('div#showOptions').html('<a href="#">More Options</a>');
        }

        if ($('div#search_wrapper').is(":visible"))
            $('div#search_wrapper').hide();

        window.location='./search';
    });

    /**
     * Set up tool tip
     */

    //$("#showOptions").tooltip({effect: 'fade', delay: 0});
    $("#showReset").tooltip({effect: 'fade', delay: 0});
    $("#map_canvas").tooltip({effect: 'fade', delay: 0});

    /**
     * Set up growl
     */
    $.jGrowl.defaults.position = 'bottom-right';
    jMsg("info", "This is a demo version of Geo-spatial search." +
    "You can define three different search constraints: " +
    "(1) Facets/categories; (2) Geo-spatial information " +
    "(3) Free text.", 5000);


//	$.jGrowl("Note: This is a demo version of Geo-spatial search." +
//			"You can define three different search constraints: " +
//			"(1) Facets/categories; (2) Geo-spatial information " +
//			"(3) Free text.",
//			{
//				life: 15000,
//	    		animateOpen: {
//	    			height: 'show'
//	    		}
//
//	});


});

function jMsg(msgtype, message, duration) {
    switch(msgtype) {
    case 'info':
        var theText = '<img src="images/info32.png" class="img-thumb"/>' +
            '<span class="separator">&nbsp;</span>' +
            message;

        // this can be merged, necessary only if we want to
        // have different styles based on message type.
        $.jGrowl(theText, {
            theme: 'themed',
            life: duration,
            animateOpen: {
                height: 'show'
            }
        });

        break;
    case 'warn':
        var theText = '<img src="images/warning32.png" class="img-thumb"/>' +
        '<span class="separator">&nbsp;</span>' +
        message;

        // this can be merged, necessary only if we want to
        // have different styles based on message type.
        $.jGrowl(theText, {
            theme: 'themed',
        life: duration,
        animateOpen: {
            height: 'show'
            }
        });


    }

}