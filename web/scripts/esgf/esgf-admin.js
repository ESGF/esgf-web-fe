/**
 *
 * esgf fe admin panel
 *
 * @author fwang2
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



    $("#setting").ajaxForm({
            target: '#output',
            dataType: 'html',
            success: function() {
                LOG.debug("form submit sucess");
                $("#output").fadeIn();}
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
            });
            break;
        default:
            LOG.debug("no match");
        }
    }); // bind()

});
