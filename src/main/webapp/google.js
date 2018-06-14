function onSignIn(googleUser) {
  /*var profile = googleUser.getBasicProfile();
  console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
  console.log('Name: ' + profile.getName());
  console.log('Image URL: ' + profile.getImageUrl());
  console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.*/

  const params = new URLSearchParams();
  const profile = googleUser.getBasicProfile();
  params.append('email', profile.getEmail());
  const xhr = new XMLHttpRequest();
  xhr.addEventListener('load', onLoginResponse);
  xhr.addEventListener('error', onNetworkError);
  xhr.open('POST', 'login/google');
  xhr.send(params);

  document.getElementById("dimmer").remove();
  document.getElementById('login-lightbox').style.visibility = "hidden";
}

function onSuccess(googleUser) {
    console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
    onSignIn(googleUser);
}
function onFailure(error) {
    console.log(error);
}
function renderButton() {
    gapi.signin2.render('google-signin2', {
        'scope': 'profile email',
        'width': 250,
        'height': 30,
        'longtitle': true,
        'theme': 'dark',
        'onsuccess': onSuccess,
        'onfailure': onFailure
    });
}