function sanitize() {
  openidElement = document.getElementById("openid_identifier");
  openid = openidElement.value;
  openid = openid.replace("http:","https:")
  .replace(/^\s\s*/, '').replace(/\s\s*$/, '');
  openidElement.value = openid;								
  var credential_controller_url = '/esgf-web-fe/credential_proxy';
  var queryStr = {'openid' : openid};

  jQuery.ajax({
    url: credential_controller_url,
    query: queryStr,
    async: false,
    type: 'GET',
    success: function(data) {   
      ESGF.localStorage.remove('GO_Credential',data.credential['openid_str']);
      ESGF.localStorage.put('GO_Credential',data.credential['openid_str'],data.credential['credential_str']);
      ESGF.localStorage.printMap('GO_Credential');
      // alert('openid: ' + data.credential['openid_str'] + ' credential: ' + data.credential['credential_str']);
    },
    error: function() {
      // alert('error in getting globus online credential');
    }
  });
}
