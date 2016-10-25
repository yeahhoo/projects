$(document).ready(function() {

    onLoadFunc();

    function onLoadFunc() {
        console.log("LOADED");
        doRequest('/client/server/scopes', 'GET')
        .done(function(data) {
            console.log('result: ' + data);
        });
    };

    function doRequest(url, type, params, headers, contentType) {
        return $.ajax({
            type: type,
            url: url,
            data: params,
            headers: headers,
            contentType: contentType
        }).done(function(data) {
            console.log('non secured POST successful request: ' + JSON.stringify(data));
        }).fail(function(e) {
            console.log('non secured POST failed request: ' + JSON.stringify(e));
        });
    };

    function readCookie(name) {
        var nameEQ = encodeURIComponent(name) + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) === ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) === 0) return decodeURIComponent(c.substring(nameEQ.length, c.length));
        }
        return null;
    }

    $('#submitBtn').click(function(e) {
        e.stopPropagation();
        var params = {
            client: $('#client').val(),
            secret: $('#password').val(),
            grantTypes: $('#grantTypes').val().split(','),
            scopes: $('#scopes').val().split(',')
        };
        var xsrfToken = readCookie('XSRF-TOKEN');
        var headers = {'X-XSRF-TOKEN': xsrfToken};
        doRequest('/client/server/oauth_client/create', 'POST', JSON.stringify(params), headers, 'application/json; charset=utf-8')
        .done(function(data) {
            console.log('result: ' + data);
        })
        .fail(function(e) {
            console.log('non secured POST failed request: ' + JSON.stringify(e));
        });
        return false;
    });
});

