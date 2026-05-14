<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Особистий кабінет</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f4f9; padding: 20px; }
        .container { max-width: 900px; margin: auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background-color: #007bff; color: white; }
        .btn { padding: 8px 12px; background: #28a745; color: white; border: none; cursor: pointer; text-decoration: none; border-radius: 4px; }
        .btn-danger { background: #dc3545; }
        .btn-warning { background: #ffc107; color: black; }
        .header { display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #eee; padding-bottom: 10px; margin-bottom: 20px; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h2>Кабінет: ${loggedUser.name} (${loggedUser.role})</h2>
        <a href="${contextPath}/logout" class="btn btn-danger">Вийти</a>
    </div>

    <#if loggedUser.role == "ADMIN">
        <h3>Управління користувачами</h3>
        <table>
            <tr><th>ID</th><th>Ім'я</th><th>Email</th><th>Роль</th><th>Дії</th></tr>
            <#if allUsers??>
                <#list allUsers as u>
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.name}</td>
                        <td>${u.email}</td>
                        <td>${u.role}</td>
                        <td>
                            <a href="${contextPath}/clinic/admin/user?action=edit&id=${u.id}" class="btn btn-warning" style="margin-right: 5px;">Редагувати</a>

                            <form action="${contextPath}/clinic/admin/user" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${u.id}">
                                <button type="submit" class="btn btn-danger" onclick="return confirm('Ви впевнені, що хочете видалити цього користувача?');">Видалити</button>
                            </form>
                        </td>
                    </tr>
                </#list>
            </#if>
        </table>
    </#if>

    <#if loggedUser.role == "DOCTOR">
        <h3>Мої записи (Пацієнти)</h3>
        <table>
            <tr><th>Дата і час</th><th>Пацієнт</th><th>Статус</th><th>Дії</th></tr>
            <#if appointments?? && appointments?has_content>
                <#list appointments as a>
                    <tr>
                        <td>${a.appointmentDate}</td>
                        <td>${a.patient.name} (${a.patient.email})</td>
                        <td><strong>${a.status}</strong></td>
                        <td>
                            <form action="${contextPath}/clinic/appointment" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="updateStatus">
                                <input type="hidden" name="appointmentId" value="${a.id}">
                                <#if a.status == "PENDING">
                                    <button type="submit" name="status" value="CONFIRMED" class="btn">Підтвердити</button>
                                    <button type="submit" name="status" value="CANCELLED" class="btn btn-danger">Скасувати</button>
                                <#elseif a.status == "CONFIRMED">
                                    <button type="submit" name="status" value="COMPLETED" class="btn btn-warning">Завершено</button>
                                </#if>
                            </form>
                        </td>
                    </tr>
                </#list>
            <#else>
                <tr><td colspan="4">У вас поки немає записів.</td></tr>
            </#if>
        </table>
    </#if>

    <#if loggedUser.role == "PATIENT">
        <h3>Записатися до лікаря</h3>
        <form action="${contextPath}/clinic/appointment" method="post" style="background: #e9ecef; padding: 15px; border-radius: 5px;">
            <input type="hidden" name="action" value="book">
            <input type="hidden" name="patientId" value="${loggedUser.id}">
            <label>Оберіть лікаря:</label>
            <select name="doctorId" required>
                <#if drHouse??>
                    <option value="${drHouse.id}">${drHouse.name} (${drHouse.role})</option>
                </#if>
            </select>
            <label>Дата та час:</label>
            <input type="datetime-local" name="date" required>
            <button type="submit" class="btn">Забронювати</button>
        </form>

        <h3 style="margin-top: 30px;">Мої записи</h3>
        <table>
            <tr><th>Дата і час</th><th>Лікар</th><th>Статус</th></tr>
            <#if myAppointments?? && myAppointments?has_content>
                <#list myAppointments as a>
                    <tr>
                        <td>${a.appointmentDate}</td>
                        <td>${a.doctor.name}</td>
                        <td><strong>${a.status}</strong></td>
                    </tr>
                </#list>
            <#else>
                <tr><td colspan="3">Ви ще не записувалися до лікаря.</td></tr>
            </#if>
        </table>
    </#if>

</div>
</body>
</html>