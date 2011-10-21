AjaxSolr.AbstractGeospatialWidget = AjaxSolr.AbstractOverlayWidget.extend(
{
	
	/**
	 * Assembles solr query string for an "encloses" bounding box geo query 
	 * 
	 * @param boundingboxSD
	 * @param boundingboxWD
	 * @param boundingboxND
	 * @param boundingboxED
	 * @returns
	 */
    getEnclosesBBQuery: function (boundingboxSD,boundingboxWD,boundingboxND,boundingboxED) {
    	
        var geoQueryString, west_degreesFQ, east_degreesFQ, north_degreesFQ, south_degreesFQ; 
    	
    	//convert to solr range
        south_degreesFQ = 'south_degrees:[' + boundingboxSD + ' TO *]';
        west_degreesFQ = 'west_degrees:[' + boundingboxWD + ' TO *]';
        north_degreesFQ = 'north_degrees:[ * TO ' + boundingboxND + ']';
        east_degreesFQ = 'east_degrees:[ * TO ' + boundingboxED + ']';

        //place in solr formatted query string
        geoQueryString = '(' + south_degreesFQ + ' AND ' + west_degreesFQ + ' AND ' + north_degreesFQ + ' AND ' + east_degreesFQ + ')';
        
        return geoQueryString;
    },
	
    /**
     * Assembles solr query string for an "overlaps" bounding box geo query 
     * 
     * @param boundingboxSD
     * @param boundingboxWD
     * @param boundingboxND
     * @param boundingboxED
     */
	getOverlapsBBQuery: function (boundingboxSD,boundingboxWD,boundingboxND,boundingboxED) {
		var geoQueryString = '';
		
		//case 1
        //NE point in bounding box
        geoQueryString += '(east_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
	                 'north_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';
        geoQueryString += ' OR ';
        //case 2
        //SE point in bounding box
        geoQueryString += '(east_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
            'south_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';
        geoQueryString += ' OR ';
        //case 3
        //SW point in bounding box
        geoQueryString += '(west_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
            'south_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';
	    geoQueryString += ' OR ';		    
        //case 4
	    //NW point in bounding box
        geoQueryString += '(west_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
            'north_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';
        geoQueryString += ' OR ';
        //case 5
        //east degree in range and n & s are above and below respectively
        geoQueryString += '(east_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
            'south_degrees:[ * TO ' + boundingboxSD + '] AND ' +
            'north_degrees:[' + boundingboxND + ' TO ' + '* ])';
        geoQueryString += ' OR ';
        //case 6
        //west degree in range and n & s are above and below respectively
        geoQueryString += '(west_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
            'south_degrees:[ * TO ' + boundingboxSD + '] AND ' +
            'north_degrees:[' + boundingboxND + ' TO ' + '* ])';
        geoQueryString += ' OR ';
        //case 7
        //north degree in range and n & s are above and below respectively
        geoQueryString += '(north_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '] AND ' +
            'west_degrees:[ * TO ' + boundingboxWD + '] AND ' +
            'east_degrees:[' + boundingboxED + ' TO ' + '* ])';
        geoQueryString += ' OR ';
        //case 8
        //south degree in range and n & s are above and below respectively
        geoQueryString += '(south_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
            'west_degrees:[ * TO ' + boundingboxWD + '] AND ' +
            'east_degrees:[' + boundingboxED + ' TO ' + '* ])';
        geoQueryString += ' OR ';
        //case 9
        //data box > user defined bounding box              
        geoQueryString += '(east_degrees:[' + boundingboxED + ' TO ' + ' *] AND ' +
            'west_degrees:[ * TO ' + boundingboxWD + '] AND ' +
            'south_degrees:[ * TO ' + boundingboxSD + '] AND ' +
            'north_degrees:[' + boundingboxND + ' TO ' + '* ])';
        
        
        return geoQueryString;
	}
	


});