function AjaxGet(url, parameters, successCallback) {
    //parameters["token"] = token;
    //parameters["user"] = user_name;
    $.get(url, parameters, successCallback);
}

function AjaxPost(url, parameters) {
    //parameters["token"] = token;
    //parameters["user"] = user_name;
    $.post(url, parameters, new function() {});
}

function AjaxGetPromise(url, parameters) {
    //parameters["token"] = token;
    //parameters["user"] = user_name;
    return $.get(url, parameters);
}

function AjaxPostPromise(url, parameters) {
    //parameters["token"] = token;
    //parameters["user"] = user_name;
    return $.post(url, parameters);
}

function AjaxGetBlob(url, parameters, successCallback, errorFunction) {
    //parameters["token"] = token;
    //parameters["user"] = user_name;
    $.ajax({
        xhrFields: {
           responseType: 'blob'
        },
        data : parameters,
        type : 'GET',
        url : url,
        error : errorFunction
    }).done(function(blob){
        successCallback(blob);
    });
}

function redirect(page) {
    window.location.href = window.location.origin + "/" + page;
}

export {
    AjaxGet, redirect
}