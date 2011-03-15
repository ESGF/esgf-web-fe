/**
 * handle home widget
 */


$(function() {

    $.getJSON("setting/queryAnnotate", function(data) {
        ESGF.setting["annotate"] = data;
        LOG.debug("annotate:" + data);
    });



    $(".slidetabs").tabs(".images > div", {

        // enable "cross-fading" effect
        effect: 'fade',
        fadeOutSpeed: "slow",
        autoplay: true,
        interval: 3000,
        // start from the beginning after the last tab
        rotate: true

        // use the slideshow plugin. It accepts its own configuration
    }).slideshow();

 });
