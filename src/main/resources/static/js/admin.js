document.addEventListener('DOMContentLoaded', function () {
    loadUsers();
});


async function loadUsers() {
    try {
        const response = await fetch('/api/users');
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.statusText);
        }
        const users = await response.json();
        const userTableBody = document.getElementById('userTableBody');
        userTableBody.innerHTML = '';

        users.forEach(user => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${user.id || 'Не указано'}</td>
                <td>${user.name || 'Не указано'}</td>
                <td>${user.surname || 'Не указано'}</td>
                <td>${user.year || 'Не указано'}</td>
                <td>${user.email || 'Не указано'}</td>
                <td>${user.roles ? user.roles.map(role => role.name).join(', ') : 'Не указано'}</td>
                <td><button class="btn btn__edit" onclick="editUser(${user.id})">Edit</button></td>
                <td><button class="btn btn-danger" onclick="showDeleteModal(${user.id})">Delete</button></td>
            `;
            userTableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error fetching users:', error);
    }
}

async function loadRoles() {
    try {
        const rolesResponse = await fetch(`/api/roles`);
        if (!rolesResponse.ok) {
            throw new Error('Error fetching roles');
        }
        return await rolesResponse.json();
    } catch (error) {
        console.error('Error loading roles:', error);
        return [];
    }
}

async function editUser(userId) {
    try {
        const response = await fetch(`/api/users/${userId}`);
        if (!response.ok) {
            throw new Error('Error fetching user data');
        }
        const user = await response.json();
        document.getElementById('editID').value = user.id;
        document.getElementById('editUserID').value = user.id;
        document.getElementById('editName').value = user.name;
        document.getElementById('editSurname').value = user.surname;
        document.getElementById('editYear').value = user.year;
        document.getElementById("editEmail").value = user.email
        const allRoles = await loadRoles();

        const rolesSelect = document.getElementById('editRoles');
        rolesSelect.innerHTML = '';

        allRoles.forEach(role => {
            const option = document.createElement('option');
            option.value = role.id; // ID роли
            option.textContent = role.name; // Имя роли
            option.selected = user.roles && user.roles.some(userRole => userRole.id === role.id);
            rolesSelect.appendChild(option);
        });

        const editModal = new bootstrap.Modal(document.getElementById('editModal'));
        editModal.show();
    } catch (error) {
        console.error('Error fetching user:', error);
    }
}

document.getElementById('editUserForm').addEventListener('submit', async (event) => {
    console.log('Form submitted');
    event.preventDefault();

    // ТУТ ДОБАВИТЬ ОСТАЛЬНОЕ ЗАПОЛНЕНИЕ
    const userId = document.getElementById('editUserID').value;
    const userData = {
        name: document.getElementById('editName').value,
        surname: document.getElementById('editSurname').value,
        year: parseInt(document.getElementById('editYear').value),
        email: document.getElementById("editEmail").value,
        password: document.getElementById('editPassword').value,
        roles: Array.from(document.getElementById('editRoles').selectedOptions).map(option => option.textContent)
    };
    try {
        const response = await fetch(`/api/users/${userId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        let responseText = await response.text();
        console.log('Response Text:', responseText);

        if (!response.ok) {
            const invalidUser = JSON.parse(responseText)
            console.log(invalidUser)
            document.getElementById('editIDError').innerText = invalidUser.idError
            document.getElementById('editNameError').innerText = invalidUser.nameError
            document.getElementById('editSurnameError').innerText = invalidUser.surnameError
            document.getElementById('editYearError').innerText = invalidUser.yearError
            document.getElementById("editEmailError").innerText = invalidUser.emailError
            document.getElementById("editPasswordError").innerText = invalidUser.passwordError
            document.getElementById("editRolesError").innerText = invalidUser.rolesError
            throw new Error('Failed to update user');
        }
        document.getElementById('editNameError').innerText = ""
        document.getElementById('editSurnameError').innerText = ""
        document.getElementById('editYearError').innerText = ""
        document.getElementById("editEmailError").innerText = ""
        document.getElementById("editPasswordError").innerText = ""
        document.getElementById("editRolesError").innerText = ""
        const updatedUser = JSON.parse(responseText);
        console.log('User updated successfully:', updatedUser);
        await loadUsers();
        const editModal = bootstrap.Modal.getInstance(document.getElementById('editModal'));
        if (editModal) {
            editModal.hide();
        }
    } catch (error) {
        console.error('Error updating user:', error);
    }
});


async function showDeleteModal(userId) {
    try {
        const response = await fetch(`/api/users/${userId}`);
        if (!response.ok) {
            throw new Error('Error fetching user data');
        }
        const user = await response.json();
        // ТУТ ДОБАВИТЬ ОСТАЛЬНОЕ ЗАПОЛНЕНИЕ
        document.getElementById('deleteID').value = user.id;
        document.getElementById('deleteName').value = user.name;
        document.getElementById('deleteSurname').value = user.surname;
        document.getElementById('deleteYear').value = user.year;
        // ТУТ Я УДАЛИЛ ПАРОЛЬ ДЛЯ ВЫВОДА НА ФОРМУ
        document.getElementById("deleteEmail").value = user.email
        const rolesContainer = document.getElementById('deleteRoles');
        rolesContainer.innerHTML = '';
        user.roles.forEach(role => {
            const option = document.createElement('option');
            option.textContent = role.name;
            rolesContainer.appendChild(option);
        });


        const deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
        deleteModal.show();

        document.getElementById('deleteChanges').onclick = async (event) => {
            event.preventDefault();
            await deleteUser(userId);
            deleteModal.hide();
        };
    } catch (error) {
        console.error('Error fetching user for deletion:', error);
    }
}

async function deleteUser(userId) {
    try {
        const response = await fetch(`/api/users/${userId}`, {
            method: 'DELETE'
        });
        if (response.ok) {
            await loadUsers();
        } else {
            throw new Error('Failed to delete user');
        }
    } catch (error) {
        console.error('Error deleting user:', error);
    }
}

async function addUser() {
    // ТУТ ДОБАВИТЬ ОСТАЛЬНОЕ ЗАПОЛНЕНИЕ
    const userData = {
        name: document.getElementById('inputName').value,
        surname: document.getElementById('inputSurname').value,
        year: parseInt(document.getElementById('inputYear').value),
        email: document.getElementById("inputEmail").value,
        password: document.getElementById('inputPassword').value,
        roles: Array.from(document.getElementById('inputRoles').selectedOptions).map(option => option.textContent)
    };


    try {

        const response = await fetch(`/api/users`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        let responseText = await response.text();
        console.log('Response Text:', responseText);

        if (!response.ok) {
            const invalidUser = JSON.parse(responseText)
            console.log(invalidUser)
            document.getElementById('addNameError').innerText = invalidUser.nameError
            document.getElementById('addSurnameError').innerText = invalidUser.surnameError
            document.getElementById('addYearError').innerText = invalidUser.yearError
            document.getElementById("addEmailError").innerText = invalidUser.emailError
            document.getElementById("addPasswordError").innerText = invalidUser.passwordError
            document.getElementById("addRolesError").innerText = invalidUser.rolesError
            throw new Error('Failed to add user');
        }
        document.getElementById('addNameError').innerText = ""
        document.getElementById('addSurnameError').innerText = ""
        document.getElementById('addYearError').innerText = ""
        document.getElementById("addEmailError").innerText = ""
        document.getElementById("addPasswordError").innerText = ""
        document.getElementById("addRolesError").innerText = ""
        const newUser = JSON.parse(responseText);
        console.log('User added successfully:', newUser);
        await loadUsers();
        document.getElementById('addUserForm').reset();
        const allUsersTab = new bootstrap.Tab(document.getElementById('nav-home-tab'));
        allUsersTab.show();
    } catch (error) {
        console.error('Error adding user:', error);
    }
}

async function initializeRoles() {
    const roles = await loadRoles();
    const rolesSelect = document.getElementById('inputRoles');
    rolesSelect.innerHTML = '';

    roles.forEach(role => {
        const option = document.createElement('option');
        option.value = role.id;
        option.textContent = role.name;
        rolesSelect.appendChild(option);
    });
}


document.getElementById('addUserForm').addEventListener('submit', async (event) => {
    console.log('Add user form submitted');
    event.preventDefault();
    await addUser();
});
document.addEventListener('DOMContentLoaded', initializeRoles);

(async () => {
    await fetchUserData("/api/user");
})();