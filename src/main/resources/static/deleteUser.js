// ----------------------------- Удаление пользователя --------------------------------------
function openDeleteUserPopup(userId) {
    // Получаем данные пользователя
    fetch(`/admin/users/${userId}`)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Не удалось загрузить данные пользователя');
            }
        })
        .then(user => {
            // Заполняем поля формы данными пользователя
            document.getElementById('deleteId').value = user.id;
            document.getElementById('deleteFirstName').value = user.firstName;
            document.getElementById('deleteLastName').value = user.lastName;
            document.getElementById('deleteEmail').value = user.email;
            document.getElementById('deleteUsername').value = user.username;
            const editRolesSelect = document.getElementById('deleteRoles');
            Array.from(editRolesSelect.options).forEach(option => {
                option.selected = user.roles.some(role => role.id === parseInt(option.value, 10));
            });

            // Открываем модальное окно
            openModal('deleteUser');
        })
        .catch(error => {
            console.error('Ошибка при загрузке данных пользователя:', error);
            alert('Ошибка при загрузке данных пользователя');
        });
}

document.getElementById('deleteUserForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const userId = document.getElementById('deleteId').value;
    fetch(`/admin/delete/${userId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                fetchUsers();
                closeModal('deleteUser');
                //$('#deleteUser').modal('hide');
            } else {
                return response.json().then(data => {
                    throw new Error(data.message || 'Не удалось удалить пользователя');
                });
            }
        })
        .catch(error => {
            console.error('Ошибка при удалении пользователя:', error);
            alert('Ошибка при удалении пользователя: ' + error.message);
        });
});