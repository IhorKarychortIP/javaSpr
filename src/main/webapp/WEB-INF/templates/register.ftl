<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Реєстрація (Firebase)</title>
    <script src="https://www.gstatic.com/firebasejs/9.22.1/firebase-app-compat.js"></script>
    <script src="https://www.gstatic.com/firebasejs/9.22.1/firebase-auth-compat.js"></script>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f4f9; padding: 50px; }
        .container { max-width: 400px; margin: auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        input, select { width: 100%; padding: 10px; margin: 10px 0; box-sizing: border-box; }
        button { width: 100%; padding: 10px; background: #28a745; color: white; border: none; cursor: pointer; border-radius: 4px; }
        .error { color: red; margin-bottom: 10px; font-size: 14px; }
    </style>
</head>
<body>
<div class="container">
    <h2>Реєстрація</h2>
    <div id="errorMessage" class="error"></div>

    <input type="text" id="name" placeholder="Ваше ім'я" required>
    <input type="email" id="email" placeholder="Email" required>
    <input type="password" id="password" placeholder="Пароль" required>

    <select id="role">
        <option value="PATIENT">Пацієнт</option>
        <option value="DOCTOR">Лікар</option>
        <option value="ADMIN">Адміністратор</option>
    </select>

    <button onclick="register()">Зареєструватися</button>

    <p style="text-align: center; margin-top: 15px;">
        <a href="${contextPath}/login">Вже маєте акаунт? Увійти</a>
    </p>
</div>

<script>
    // БЕЗПЕКА: Змінні FreeMarker, реальних ключів у коді немає.
    const firebaseConfig = {
        apiKey: "${apiKey}",
        authDomain: "${authDomain}",
        projectId: "${projectId}",
        storageBucket: "${storageBucket}",
        messagingSenderId: "${messagingSenderId}",
        appId: "${appId}"
    };

    if (!firebase.apps.length) {
        firebase.initializeApp(firebaseConfig);
    }

    function register() {
        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const role = document.getElementById('role').value;

        // 1. Спочатку створюємо користувача в базі Google Firebase
        firebase.auth().createUserWithEmailAndPassword(email, password)
            .then((userCredential) => {
                // 2. Якщо все добре, передаємо дані (ім'я, пошту, роль) на наш Java-сервер
                const formData = new URLSearchParams();
                formData.append('name', name);
                formData.append('email', email);
                formData.append('role', role);

                return fetch('${contextPath}/register', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: formData.toString()
                });
            })
            .then((response) => {
                if(response.ok) {
                    // Якщо Java-сервер успішно зберіг у MySQL, переходимо на логін
                    window.location.href = "${contextPath}/login";
                } else {
                    throw new Error("Помилка збереження в локальну базу даних MySQL.");
                }
            })
            .catch((error) => {
                document.getElementById('errorMessage').innerText = "Помилка: " + error.message;
                console.error(error);
            });
    }
</script>
</body>
</html>