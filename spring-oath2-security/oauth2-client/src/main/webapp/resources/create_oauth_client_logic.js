$(document).ready(function() {

    onLoadFunc();

    function onLoadFunc() {
        console.log("LOADED");
        doRequest('/client/server/scopes', 'GET')
        .done(function(data) {
            console.log('result: ' + data);
        });
    };

    function doRequest(url, type, params, contentType) {
        return $.ajax({
            type: type,
            url: url,
            data: params,
            contentType: contentType
        }).done(function(data) {
            console.log('non secured POST successful request: ' + JSON.stringify(data));
        }).fail(function(e) {
            var data = JSON.parse(e.responseText);
            alert('client caused: \nerror: ' + data.error + '\nexception: ' + data.exception
                + '\nmessage: ' + data.message + '\nstatus: ' + data.status + '\ntrace: ' + data.trace);
        });
    };

    /*
    the reason of sending header 'X-XSRF-TOKEN' with the same value as cookie 'XSRF-TOKEN' is to comply with authorisation mechanism:
    1) server gets value of the token from session;
    2) server extracts value of the header from request;
    3) compare them to make sure that user has sent it.
    so if to make a special filter on server side that will add the header for every request then it will compromise security.

    the source: org.springframework.security.web.csrf.CsrfFilter
    */
    function doSecureRequest(url, type, params, contentType) {
        return $.ajax({
            type: type,
            url: url,
            data: params,
            headers: {'X-XSRF-TOKEN': readCookie('XSRF-TOKEN')},
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
        doRequest('/client/server/oauth_client/create', 'POST', JSON.stringify(params), 'application/json; charset=utf-8')
        .done(function(data) {
            alert('client created: ' + data);
        });
        return false;
    });

    /* just for test reasons to show REST exception 'correctly' */
    $('#expBtn').click(function(e) {
        e.stopPropagation();
        doRequest('/client/server/oauth_client/exception', 'POST', {}, 'application/json; charset=utf-8')
        .done(function(data) {
            alert('client caused: \nerror: ' + data.error + '\nexception: ' + data.exception
                + '\nmessage: ' + data.message + '\nstatus: ' + data.status + '\ntrace: ' + data.trace);
        });
        return false;
    });

});

