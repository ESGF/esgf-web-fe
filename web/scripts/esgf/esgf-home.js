/**
 * handle home widget
 */


$(function() {

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
