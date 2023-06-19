function AjaxGet(url, parameters, successCallback) {
    $.get(url, parameters, successCallback);
}

function redirect(page) {
    window.location.href = urlBase + "/" + page;
}

export {
    AjaxGet, redirect
}