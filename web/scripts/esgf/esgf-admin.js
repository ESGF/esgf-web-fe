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

/**
 *
 * esgf fe admin panel
 *
 * @author Feiyi Wang
 *
 */


$(function(){
    $('#adminTabs').tabs();


    $.get("news/list",
        function(data) {
            $('#headline').html(data);
    });

});


$(document).ready( function() {

    $("a#create").live('click', function() {
        $.get("news/create", function(data) {
            $("#headline_list").html(data);
        });
    });

    $("a.edit").live('click', function() {
        $( "#dialog-message" ).dialog({
            modal: true,
            buttons: {
                Ok: function() {
                    $( this ).dialog( "close" );
                }
            }
        });
    });

    $("a.remove").live('click', function() {

        // for complicated href pattern, we can use
        // var newsId = $("a.remove").attr("href").match(/[0-9]+/)[1];

        var newsId = $(this).attr("href");
        LOG.debug("news id [" + newsId + "] to be removed");

        // call backend
        $.getJSON("news/remove/"+newsId);

        // TODO: show notification

        // remove, TODO: check return first
        $(this).parent().parent().remove();

        return false;
    });






    $('#adminTabs').bind('tabsselect', function(event, ui) {
        switch(ui.index)
        {
        case 0:
            LOG.debug("0 index");
            $("#output").hide();
            $.get("news/list",
                    function(data) {
                        $('#headline').html(data);
                });
            break;
        case 1:
            LOG.debug("1 index");
            $("#output").hide();
            $.get("setting/show",
                    function(data){
                        $("#search_setting").html(data);

                        // placing here is key
                        // as the DOM is loaded through ajax
                        // you have to bind here.
                        $("#settingForm").ajaxForm({
                            target: '#output',
                            success: function() {
                                LOG.debug("form submit sucess");
                                $("#output").fadeIn();}
                    });

            });
            break;
        default:
            LOG.debug("no match");
        }
    }); // bind()

});
