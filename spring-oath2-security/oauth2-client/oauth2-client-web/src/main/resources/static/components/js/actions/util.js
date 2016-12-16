function wrapPreRequestDispatchAction(actionName, params) {
    return {
        type: actionName,
        params: params
    };
}
/*
function doRequest(url, type, contentType, data) {
    return fetch(url, {
        method: type,
        body: data,
        headers: new Headers({
            'Content-Type': contentType
        })
    }).then((response) => {
        console.log('request succeeded: ' + url);
        return response;
    }).catch(error => {
        console.log('request failed: ' + error);
        return error;
    });
}
*/
function doRequest(url, type, contentType, data) {
    return $.ajax({
        url: url,
        type: type,
        data: data,
        contentType: contentType
    }).done(function(data) {
        console.log('request succeeded: ' + JSON.stringify(data));
    }).fail(function(e) {
        console.log('request failed: ' + e);
    });
}

function wrapAfterRequestDispatchAction(type, data) {
    return {
        type: type,
        data: data,
        receivedAt: Date.now()
    };
}

export { doRequest, wrapPreRequestDispatchAction, wrapAfterRequestDispatchAction };