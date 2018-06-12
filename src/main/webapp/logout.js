function onLogoutResponse() {
    if (this.status === OK) {
        setUnauthorized();
        clearMessages();
        var auth2 = gapi.auth2.getAuthInstance();
        auth2.signOut();
        showContents(['login-register-buttons']);
    } else {
        onOtherResponse(logoutContentDivEl, this);
    }
}

function onLogoutButtonClicked(event) {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLogoutResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'protected/logout');
    xhr.send();
}
