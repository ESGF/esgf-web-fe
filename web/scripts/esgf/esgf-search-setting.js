/**
 *
 * handle search, new file, meant to be place holder of all search script.
 */


$.ajax({
        url: "setting/queryAnnotate",
        aync: false,
        dataType: 'json',
        success: function(data) {
            ESGF.setting["annotate"] = data;
            LOG.debug("annotate:" + data);
        }
 });

