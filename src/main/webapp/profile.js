function onProfileLoad(user) {
    clearMessages();
    showContents(['profile-content', 'logout-content']);

    const userNameSpandEl = document.getElementById('user-name');
    const userRoleSpanEl = document.getElementById('user-role');

    userNameSpandEl.textContent = user.name;
    userRoleSpanEl.textContent = user.role;
}