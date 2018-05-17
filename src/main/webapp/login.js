function onLoginResponse() {
    if (this.status === OK) {
        const user = JSON.parse(this.responseText);
        setAuthorization(user);
        onProfileLoad(user);
        showContents(['menu', 'profile-content', 'logout-content']);
        onLoadSchedules();
    } else {
        onOtherResponse(loginContentDivEl, this);
    }
}

function onLoginButtonClicked() {
    const loginFormEl = document.forms['login-form'];

    const nameInputEl = loginFormEl.querySelector('input[name="name"]');
    const passwordInputEl = loginFormEl.querySelector('input[name="password"]');

    const name = nameInputEl.value;
    const password = passwordInputEl.value;

    const params = new URLSearchParams();
    params.append('name', name);
    params.append('password', password);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLoginResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'login');
    xhr.send(params);
}