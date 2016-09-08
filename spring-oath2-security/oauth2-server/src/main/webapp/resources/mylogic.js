$(document).ready(function() {

    function doSecureRequest(url, type, headers, params) {
        return $.ajax({
            type: type,
            url: url,
            data: params,
            headers: headers
        }).done(function(data) {
            console.log('non secured POST successful request: ' + JSON.stringify(data));
        }).fail(function(e) {
            console.log('non secured POST failed request: ' + JSON.stringify(e));
        });
    };

    function getUrlVars() {
        var vars = {};
        var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,
        function(m,key,value) {
          vars[key] = value;
        });
        return vars;
      }

    $('#logoutBtn').click(function(e) {
        e.stopPropagation();
        var urlParams = getUrlVars();
        var headers = {'authorization': 'bearer ' + urlParams.token};
        doSecureRequest('/server/mylogout', 'GET', headers, urlParams)
        .done(function(data) {
            console.log('Server logout successful');
            window.location.href = urlParams.urlToReturn;
        });
        return false;
    });


});


