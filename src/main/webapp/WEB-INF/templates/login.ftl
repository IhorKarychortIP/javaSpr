<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Вхід (Firebase)</title>
    <script src="https://www.gstatic.com/firebasejs/9.22.1/firebase-app-compat.js"></script>
    <script src="https://www.gstatic.com/firebasejs/9.22.1/firebase-auth-compat.js"></script>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f4f9; padding: 50px; }
        .container { max-width: 400px; margin: auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        input { width: 100%; padding: 10px; margin: 10px 0; box-sizing: border-box; }
        .btn { width: 100%; padding: 10px; color: white; border: none; cursor: pointer; margin-bottom: 10px; border-radius: 4px; }
        .btn-primary { background: #007bff; }
        .btn-google { background: #db4437; }
        .error { color: red; margin-bottom: 10px; font-size: 14px; }
        .divider { text-align: center; margin: 15px 0; color: #888; font-size: 14px; }
    </style>
</head>
<body>
<div class="container">
    <h2>Вхід у систему</h2>
    <div id="errorMessage" class="error"></div>

    <input type="email" id="email" placeholder="Email" required>
    <input type="password" id="password" placeholder="Пароль" required>
    <button class="btn btn-primary" onclick="login()">Увійти</button>

    <div class="divider">АБО</div>

    <button class="btn btn-google" onclick="loginWithGoogle()">Увійти через Google</button>

    <p style="text-align: center; margin-top: 15px;">
        <a href="${contextPath}/register">Немає акаунта? Зареєструватися</a>
    </p>
</div>

<script>
    // БЕЗПЕКА: Тут немає реальних ключів. Java підставить їх сюди з файлу config.properties під час рендеру.
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

    // Вхід через Email/Пароль
    function login() {
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        firebase.auth().signInWithEmailAndPassword(email, password)
            .then((userCredential) => {
                return userCredential.user.getIdToken();
            })
            .then((token) => {
                // Зберігаємо токен і йдемо на дашборд
                document.cookie = "firebaseToken=" + token + "; path=/";
                window.location.href = "${contextPath}/clinic/dashboard";
            })
            .catch((error) => {
                document.getElementById('errorMessage').innerText = "Помилка Firebase: " + error.message;
                console.error(error);
            });
    }

    // Вхід через Google
    function loginWithGoogle() {
        const provider = new firebase.auth.GoogleAuthProvider();

        firebase.auth().signInWithPopup(provider)
            .then((result) => {
                return result.user.getIdToken();
            })
            .then((token) => {
                document.cookie = "firebaseToken=" + token + "; path=/";
                window.location.href = "${contextPath}/clinic/dashboard";
            })
            .catch((error) => {
                document.getElementById('errorMessage').innerText = "Помилка Google: " + error.message;
                console.error(error);
            });
    }
</script>
</body>
</html>