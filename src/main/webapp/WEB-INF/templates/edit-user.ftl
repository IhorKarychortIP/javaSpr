<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Редагування користувача</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f4f9; padding: 20px; }
        .container { max-width: 500px; margin: auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        input, select { width: 100%; padding: 10px; margin: 10px 0; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        .btn { padding: 10px 15px; background: #007bff; color: white; border: none; cursor: pointer; border-radius: 4px; text-decoration: none; display: inline-block; }
        .btn-secondary { background: #6c757d; }
    </style>
</head>
<body>
<div class="container">
    <h2>Редагування користувача #${userToEdit.id}</h2>

    <form action="${contextPath}/clinic/admin/user" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${userToEdit.id}">

        <label>Ім'я:</label>
        <input type="text" name="name" value="${userToEdit.name}" required>

        <label>Email:</label>
        <input type="email" name="email" value="${userToEdit.email}" required>

        <label>Роль:</label>
        <select name="role">
            <option value="PATIENT" <#if userToEdit.role == 'PATIENT'>selected</#if>>Пацієнт</option>
            <option value="DOCTOR" <#if userToEdit.role == 'DOCTOR'>selected</#if>>Лікар</option>
            <option value="ADMIN" <#if userToEdit.role == 'ADMIN'>selected</#if>>Адміністратор</option>
        </select>

        <div style="margin-top: 15px;">
            <button type="submit" class="btn">Зберегти зміни</button>
            <a href="${contextPath}/clinic/dashboard" class="btn btn-secondary">Скасувати</a>
        </div>
    </form>
</div>
</body>
</html>