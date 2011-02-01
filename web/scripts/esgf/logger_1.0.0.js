/** 
 * Logging package for APEX
 * @author Martin Giffy D'Souza http://www.talkapex.com
 *
 * Used to wrap calls to console which allows developers to instrument their JS code
 * This supports 2 ways of logging
 * 1- As a jQuery function which supports chaining
 *    This only supports some console features
 * 2- As a standalone function
 *    This supports all console features
 *
 * Examples are documented above each function
 *
 * Reference:
 *  - Firebug Console Commands: http://getfirebug.com/logging
 *
 * Notes:
 * By using this method we're safe to use "$" internally as users may change the $ alias 
 * Pass in jQuery into this function
 * See: http://docs.jquery.com/Plugins/Authoring for more information
 */
(function($) {
  /**
   * Console Logger function for JS
   * Will display message in console (if it exists)
   * Functions supported: log, debug, info, warn, error, trace, dir, dirxml
   * Simple use one of the above functions as the first parameter to call it
   *  Default is log
   * Always logs "this" and append other parameters
   * 
   * Will look at $.console.canLog() to see if it will actually log
   *  See $.console.setLevel for level information
   *
   * - How to use - 
   * 
   * $('p').log(); //Will log all <p> tags
   * $('p').log('test').toggle() //Log all <p> objects and toggles them
   * $('p').log('warn','test'); //Warning log for all <p> objects and adds 'test' in debug message
   */
  $.fn.log = function () {
    //Note that dir and dirxml will only use "this"
    var logFns = ['log', 'debug', 'info', 'warn', 'error', 'trace', 'dir', 'dirxml'];
    var cmd = 'log'; // Default command to run
    // Convert arguments to array
    var args = Array.prototype.slice.call(arguments);

    //If first argument is a log command then use it, otherwise use log and remove it
    if( arguments.length > 0 && $.inArray(arguments[0].toLowerCase(),logFns) >= 0){
      cmd = arguments[0];
      //Remove first element since don't want it to appear anymore
      args.shift();
    }

    //If of the command actually exists
    if (window.console && console[cmd] && $.console.canLog($.console.levels[cmd])){
      this.each(function() {
        console[cmd].apply(console, $.merge([this], args));
      });
    }
    return this;
  }; //$.fn.log

  /**
   * Wrapper for console function.
   * This allows you to add "console" calls to $.console and do not need to worry about browser compatibility
   * Default level is "off" so must enable for output
   *  - APEX: If run in APEX and session is in debug mode, level is automatically set to "log"
   *
   * - How to use -
   * 
   * - Check Level:
   * $.console.getLevel();
   *
   * - Set Level:
   * $.console.setLevel('warn'); //Only enables warning messages and lower
   *
   * - Log:
   * $.console.log('this is a log message');
   * $.console.error('this is an error message');
   */
  $.console = (function(){
    //*** Private ***
    //Levels
    var levels = {
      off : 0,
      exception: 1,
      error : 2,
      warn: 4,
      info: 8,
      log : 16,
      debug : 16     
    };
    
    //Current level
    var level = levels.off; //Default to off
    
    //List of console functions
    var consoleFns = [
      {fn: 'log', level: levels.log},
      {fn: 'debug', level: levels.log},
      {fn: 'info', level: levels.info},
      {fn: 'warn', level: levels.warn},
      {fn: 'error', level: levels.error},
      {fn: 'time', level: levels.log},
      {fn: 'timeEnd', level: levels.log},
      {fn: 'profile', level: levels.log},
      {fn: 'profileEnd', level: levels.log},
      {fn: 'count', level: levels.log},
      {fn: 'trace', level: levels.log},
      {fn: 'group', level: levels.error},
      {fn: 'groupCollapsed', level: levels.error},
      {fn: 'groupEnd', level: levels.error},
      {fn: 'dir', level: levels.log},
      {fn: 'dirxml', level: levels.log},
      {fn: 'assert', level: levels.log},
      {fn: 'exception', level: levels.exception},
      {fn: 'table', level: levels.log}
    ];

    //*** Public ***
    //Return Public objects
    that = {};
    
    //Allow other functions to access the level numbers
    that.levels = levels;

    /**
     * Determines if function can be logged
     * @param pLevel Level of function being called
     * @return True if can run command, false if not
     */
    that.canLog = function(pLevel){
      return level >= pLevel;
    };//canLog

    /**
     * Set logger debug level
     * if pLevel is not valid an alert message will be displayed
     * @param pLevel Level (see this.levels for full list of levels)
     */
    that.setLevel = function(pLevel){
      var tempLevel = levels[pLevel.toLowerCase()];
      //Check to see if the level is actually a valid level
      if (tempLevel == undefined)
        alert('Invalid Level: ' + pLevel);
      else
        level = tempLevel;
    };//setLevel
    
    /**
     * @return Level in text (i.e. not the numeric value)
     */
    that.getLevel = function(){
      for (var key in levels) {
        if (levels.hasOwnProperty(key) && levels[key] == level){
          return key;
          break;
        }// if
      }//for
      
      //Didn't find it
      return 'Unknown: ' + level;
    };//getLevel
    
    /**
     * @return true if in APEX session and debug mode is set
     */
    that.isApexDebug = function(){
      var check = document.getElementById('pdebug');
      return check != undefined && $('#pdebug').val().toLowerCase() == 'yes';
    }; //isApexDebug

    //Apply console functions to "that"
    for (i=0; i < consoleFns.length; i++){
      var myFn = consoleFns[i];
      //Console exists
      (function(pF, pL){
        that[pF] = function(){
          //Only run console if you can log, and console exists.
          //Checking console at run time for engines like Firebug Lite
          if(this.canLog(pL) && window.console && console[pF])
            console[pF].apply( console, arguments );
        };
      })(myFn.fn, myFn.level);
    }//for

    return that;
  })();//$console

  //Automatically set logging level for apex in debug mode
  $(document).ready(function(){
    if ($.console.isApexDebug())
      $.console.setLevel('log');
  });
})(jQuery);