<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Панель управління - Поліклініка</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; margin: 0; background-color: #f9f9f9; }
        header { background-color: #2e7d32; color: white; padding: 15px 40px; display: flex; justify-content: space-between; align-items: center; }
        .container { padding: 30px; max-width: 1000px; margin: 0 auto; }
        .card { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); margin-bottom: 20px; border-left: 5px solid #2196f3; }
        .role-badge { padding: 5px 10px; border-radius: 20px; font-size: 12px; font-weight: bold; text-transform: uppercase; }
        .badge-doctor { background: #e3f2fd; color: #1976d2; }
        .badge-patient { background: #f1f8e9; color: #388e3c; }

        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { text-align: left; padding: 12px; border-bottom: 1px solid #eee; }
        th { background-color: #f5f5f5; color: #333; }
        .btn-logout { color: #ffebee; text-decoration: none; font-weight: bold; border: 1px solid white; padding: 5px 15px; border-radius: 4px; }
        .btn-action { text-decoration: none; padding: 5px 10px; border-radius: 3px; font-size: 13px; margin-right: 5px; }
        .btn-edit { background: #fff3e0; color: #ef6c00; }
        .btn-delete { background: #ffebee; color: #c62828; }
        .btn-add { display: inline-block; background: #2e7d32; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px; margin-bottom: 15px; }
    </style>
</head>
<body>

<header>
    <div>
        <span style="font-size: 20px; font-weight: bold;">🏥 ClinicSystem</span>
    </div>
    <div>
        <span>Вітаємо, <strong>${loggedUser.name}</strong></span>
        <a href="/logout" class="btn-logout" style="margin-left: 20px;">Вийти</a>
    </div>
</header>

<div class="container">
    <div class="card">
        <h2>Особистий профіль</h2>
        <p><strong>Ваш Email:</strong> ${loggedUser.email}</p>
        <p><strong>Ваша роль:</strong>
            <span class="role-badge ${ (loggedUser.role == 'DOCTOR')?string('badge-doctor', 'badge-patient') }">
                ${ (loggedUser.role == 'DOCTOR')?string('Лікар', 'Пацієнт') }
            </span>
        </p>
    </div>

    <#if loggedUser.role == "DOCTOR">
        <div class="card" style="border-left-color: #2e7d32;">
            <h2>Управління базою користувачів (CRUD)</h2>
            <a href="/clinic/users/add" class="btn-add">+ Додати нового користувача</a>

            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Ім'я</th>
                    <th>Email</th>s
                    <th>Роль</th>
                    <th>Дії</th>
                </tr>
                </thead>
                <tbody>
                <#list allUsers as u>
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.name}</td>
                        <td>${u.email}</td>
                        <td>${u.role}</td>
                        <td>
                            <a href="/clinic/users/edit?id=${u.id}" class="btn-action btn-edit">Редагувати</a>
                            <a href="/clinic/users/delete?id=${u.id}" class="btn-action btn-delete" onclick="return confirm('Ви впевнені?')">Видалити</a>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    <#else>
        <div class="card" style="border-left-color: #4caf50;">
            <h2>Інформація для пацієнта</h2>
            <p>Тут ви можете побачити свої медичні записи та історію відвідувань. Наразі записів немає.</p>
        </div>
    </#if>
</div>

</body>
</html>