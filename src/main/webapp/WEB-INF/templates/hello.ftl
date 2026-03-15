<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Java Servlet Session Demo</title>
    <style>
        body { font-family: sans-serif; padding: 20px; }
        .box { border: 1px solid #ccc; padding: 15px; margin-bottom: 10px; border-radius: 5px; }
        .label { font-weight: bold; color: #555; }
        .val { color: green; }
    </style>
</head>
<body>
<h1>${message}</h1>
<hr>

<div class="box">
    <p><span class="label">Параметр з URL (?name=...):</span> <span class="val">${paramUser}</span></p>
</div>

<div class="box" style="background-color: #f9f9f9;">
    <h3>Дані з Сесії (Server Side):</h3>
    <p><span class="label">Користувач у сесії:</span> <span class="val">${sessionUser}</span></p>
    <p><span class="label">ID сесії:</span> <small>${sessionId}</small></p>
    <p><i>(Ці дані збережуться, якщо ви оновите сторінку без параметрів)</i></p>
</div>

<div class="box" style="background-color: #eef;">
    <h3>Дані з Cookie (Client Side):</h3>
    <p><span class="label">Зчитана кука 'my_app_user':</span> <span class="val">${cookieUser}</span></p>
</div>

<div style="border: 1px solid #ccc; padding: 10px; margin-top: 20px; background-color: #e8f5e9;">
    <h3>Дані з Бази Даних (MySQL):</h3>
    <p><strong>Статус:</strong> ${dbStatus}</p>

    <#if dbList?has_content>
        <ul>
            <#list dbList as item>
                <li>${item}</li>
            </#list>
        </ul>
    <#else>
        <p>Даних поки немає або таблиця порожня.</p>
    </#if>
</div>

<hr>
<h3>Як перевірити:</h3>
<ol>
    <li><a href="hello?name=Ihor">Натисніть сюди, щоб зайти як Ihor</a> (Встановить сесію та куку)</li>
    <li><a href="hello">Натисніть сюди (чистий URL)</a> (Побачите дані, що збереглися в сесії)</li>
</ol>
</body>
</html>