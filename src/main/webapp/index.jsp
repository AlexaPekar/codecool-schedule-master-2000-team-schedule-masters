<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
<head>

    <title>Schedule Master 2000</title>
    <script src="index.js"></script>
    <script src="login.js"></script>
    <script src="logout.js"></script>
    <script src="profile.js"></script>
    <script src="schedules.js"></script>

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

    <div id="schedules-content" class="hidden content">
        <h1>All schedules</h1>
        <div id="schedules" class="hidden content">

        </div>
    </div>

    <div id="schedule-content" class="hidden content">
        <h1>Title</h1>  <!--TODO: hivatkozni a schedule nevére -->
        <div id="schedule" class="hidden content">

        </div>
    </div>

    <div id="tasks-content" class="hidden content">
        <h1>All tasks</h1>
        <div id="tasks" class="hidden content">

        </div>
    </div>

    <div id="task-content" class="hidden content">
        <h1>Title</h1> <!-- TODO: hivatkozni a task nevére-->
        <div id="task" class="hidden content">

        </div>
    </div>

    <div id="users-content" class="hidden content">
        <h1>Users</h1>
        <div id="users" class="hidden content">

        </div>
    </div>

    <div id="user-content" class="hidden content">
        <h1>Name</h1> <!-- TODO: hivatkozni a user nevére-->
        <div id="user" class="hidden content">

        </div>
    </div>

    <div id="logout-content" class="hidden content">
        <button id="logout-button">Logout</button>
    </div>

</body>
</html>
