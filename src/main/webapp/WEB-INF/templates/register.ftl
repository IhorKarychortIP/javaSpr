<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Реєстрація - Приватна Поліклініка</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background-color: #f4f7f6; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .login-container { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); width: 350px; border-top: 5px solid #2e7d32; }
        h2 { color: #2e7d32; text-align: center; margin-bottom: 25px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; color: #555; }
        input, select { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; padding: 12px; background-color: #2e7d32; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; margin-top: 10px; }
        button:hover { background-color: #1b5e20; }
        .error-msg { color: #d32f2f; background: #ffebee; padding: 10px; border-radius: 4px; margin-bottom: 15px; text-align: center; font-size: 14px; }
        .link { text-align: center; margin-top: 15px; display: block; color: #2e7d32; text-decoration: none; font-size: 14px; }
        .link:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="login-container">
        <h2>Створити акаунт</h2>

        <#if error??>
            <div class="error-msg">${error}</div>
        </#if>

        <form action="/register" method="POST">
            <div class="form-group">
                <label>Повне ім'я:</label>
                <input type="text" name="name" required placeholder="Іван Іванов">
            </div>
            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" required placeholder="example@clinic.com">
            </div>
            <div class="form-group">
                <label>Пароль:</label>
                <input type="password" name="password" required placeholder="••••••••">
            </div>
            <div class="form-group">
                <label>Роль:</label>
                <select name="role" required>
                    <option value="PATIENT">Пацієнт</option>
                    <option value="DOCTOR">Лікар</option>
                    <option value="ADMIN">Адміністратор</option>
                </select>
            </div>
            <button type="submit">Зареєструватися</button>
        </form>
        <a href="/login" class="link">Вже є акаунт? Увійти</a>
    </div>
</body>
</html>