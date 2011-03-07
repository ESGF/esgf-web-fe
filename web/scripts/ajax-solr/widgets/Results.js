/**
 * Results.js
 */

(function($) {
    AjaxSolr.ResultWidget = AjaxSolr.AbstractWidget.extend({

        beforeRequest: function () {
            $(this.target).html($('<img/>').attr('src', 'images/ajax-loader.gif'));
        },
        
        
        distanceInKm : function (point1,point2) {
            var seLat1 = point1.lat()*Math.PI/180.0;
            var seLat2 = point2.lat()*Math.PI/180.0;
            var lngDiff = (point2.lng()-point1.lng())*Math.PI/180.0;

            //note this is in feet
            var a = Math.acos(Math.sin(seLat1) * Math.sin(seLat2) +
                    Math.cos(seLat1) * Math.cos(seLat2) * Math.cos(lngDiff)) * 20902231.0029;

            //convert feet to km
            var aKm = a/3280.84;
            return aKm;
        },
        
        
        postSolrProcessing: function (doc) {
            var self = this;
            var isInRange = true;
            var radius,center,se,ne,sw,nw,dist=null;
            var i = null;
            if($("input[name='areaGroup']:checked").val() === 'circle') {
                //get the radius
                //radius = $("input[name='radius']").val();
                radius = Manager.widgets['geo_browse'].centroidRadius;
                
                center = Manager.widgets['geo_browse'].centroidCenter;
                //get extreme points - they must ALL be within range
                se = new google.maps.LatLng(doc.south_degrees, doc.east_degrees);
                ne = new google.maps.LatLng(doc.north_degrees, doc.east_degrees);
                sw = new google.maps.LatLng(doc.south_degrees, doc.west_degrees);
                nw = new google.maps.LatLng(doc.north_degrees, doc.west_degrees);

                dist = [];
                dist[0] = self.distanceInKm(se,center);
                dist[1] = self.distanceInKm(ne,center);
                dist[2] = self.distanceInKm(sw,center);
                dist[3] = self.distanceInKm(nw,center);

                for (i = 0, l = dist.length; i < l; i++) {
                    if(dist[i] > radius) {
                        isInRange = false;
                    }
                }
            }

            return isInRange;
        },
        
        facetLinks: function (facet_field, facet_values) {
            var links = [];
            var i = null;
            if (facet_values) {
                for (i = 0, l = facet_values.length; i < l; i++) {
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
	        var i = null;
	        var self = this;
            $(this.target).empty();
            for (i = 0, l = this.manager.response.response.docs.length; i < l; i++) {
                var doc = this.manager.response.response.docs[i];
                //if(self.postSolrProcessing(doc)) {
                    $(this.target).append(
                        AjaxSolr.theme('result', doc,
                        AjaxSolr.theme('snippet', doc),
                        AjaxSolr.theme('actions', doc)));
                //} else {
                //    console.log('Do not keep this record: ' + doc.title);
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
        }

    });

}(jQuery));


/* previous global functions
function postSolrProcessing1(doc)
{
    var isInRange = true;
    if($("input[name='areaGroup']:checked").val() == 'circle') {

        //get the radius
        var radius = $("input[name='radius']").val();

        var center = centroidCenter;

        //get extreme points - they must ALL be within range
        var se = new google.maps.LatLng(doc.south_degrees, doc.east_degrees);
        var ne = new google.maps.LatLng(doc.north_degrees, doc.east_degrees);
        var sw = new google.maps.LatLng(doc.south_degrees, doc.west_degrees);
        var nw = new google.maps.LatLng(doc.north_degrees, doc.west_degrees);

        var dist = [];
        dist[0] = distanceInKm(se,center);
        dist[1] = distanceInKm(ne,center);
        dist[2] = distanceInKm(sw,center);
        dist[3] = distanceInKm(nw,center);

        for (var i = 0, l = dist.length; i < l; i++) {
            if(dist[i] > radius) {
                isInRange = false;
            }
        }

    }


    return isInRange;

}

function distanceInKm1(point1,point2) {

    var seLat1 = point1.lat()*Math.PI/180.0;
    var seLat2 = point2.lat()*Math.PI/180.0;
    var lngDiff = (point2.lng()-point1.lng())*Math.PI/180.0;

    //note this is in feet
    var a = Math.acos(Math.sin(seLat1) * Math.sin(seLat2) +
            Math.cos(seLat1) * Math.cos(seLat2) * Math.cos(lngDiff)) * 20902231.0029;

    //convert feet to km
    var aKm = a/3280.84;

    return aKm;
}
*/
