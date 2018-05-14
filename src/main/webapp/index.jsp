<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
<head>

    <script src="index.js"></script>
    <script src="login.js"></script>
    <script src="logout.js"></script>
    <script src="profile.js"></script>


</head>

<body>

    <div id="login-content" class="content">
        <h1>Login</h1>
        <form id="login-form" onsubmit="return false;">
            <p>Name:<input type="text" name="name"></p>
            <p>Password:<input type="password" name="password"></p>
            <button id="login-button">Login</button>
        </form>
    </div>

    <div id="profile-content" class="hidden content">
        <p>Name: <span id="user-name"></span></p>
        <p>Role: <span id="user-role"></span></p>
    </div>

    <div id="logout-content" class="hidden content">
        <button id="logout-button">Logout</button>
    </div>

</body>
</html>
