/*****************************************************************************
 * Copyright © 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * ÒLicensorÓ) hereby grants to any person (the ÒLicenseeÓ) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organizationÕs name.  If the Software is protected by a proprietary
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
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.Ó
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

/**
 * Ajax submit
 *
 * - fwang2@ornl.gov
 *
 */
$(document).ready(function(){

    var opt_facets = {
            type: 'GET',
            //beforeSubmit: showRequest,
            //success: showResponse,
            complete: styleFacets,
            target: '#facets',
            url: 'search/facets'

    };

    var opt_results = {
            type: 'GET',
            //beforeSubmit: showRequest,
            success: restoreOpacity,
            target: '#search_results',
            url: 'search/results'
    }

    $('#search-form').bind('submit', function() {
        // inside event callback, 'this' is the DOM element
        // so we first warp it in a jQuery object and then
        // invoke ajaxSubmit
        $("#search_results").css('opacity', 0.4);
        $(this).ajaxSubmit(opt_results);

        // must return false to prevent standard browser submit
        return false;
    });

    $('#search-form').bind('submit', function() {
        $(this).ajaxSubmit(opt_facets);

        return false;
    });

});


function showRequest(formData, jqForm, options) {
    var queryString = $.param(formData);

    alert('About to submit:\n\n' + queryString);
}

function showResponse(responseText, statusText, xhr, $form) {
//    alert('status: ' + statusText + '\n\nresponseText: \n' + responseText +
//    '\n\nThe output div should have already been updated with the responseText.');

    $('ul.acitem > li').trigger('click');

}

/**
 * This is where we clean up the mess (facets data transferred back)
 *
 * @param XMLHttpRequest
 * @param textStatus
 */
function styleFacets(XMLHttpRequest, textStatus) {
    $('.acitem > li > a').trigger('dbclick');
}

function restoreOpacity() {
    $('#search_results').css("opacity", 1);
}
function getSearchForm() {
    return $("#search-form")[0];
}

function setFacet(facetKey, facetValue) {
    var searchForm = getSearchForm();
    var input = searchForm[facetKey];
    input.value = facetValue;
    search(0);
}

function resetFacet(facetKey) {
    var searchForm = getSearchForm();
    var input = searchForm[facetKey];
    input.value = "";
    search(0);
}


function search(offset) {
        var searchForm = getSearchForm();
        searchForm.offset.value = offset;
        // this is the tricky part
        // I have to manually trigger this event
        // and ajax submit must be done within the ready handler
        $("#search-form").trigger("submit");
        //searchForm.submit();
}
