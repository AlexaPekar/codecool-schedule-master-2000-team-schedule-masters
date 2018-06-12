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