async function fetchUserData(url) {
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error('Network error: ' + response.status);
        }
        const user = await response.json();
        console.log(response)
        displayUserData(user);
    } catch (error) {
        console.error('Error', error);
        document.getElementById('user-table-body').innerHTML = '<tr><td colspan="4" class="text-danger">Error</td></tr>';
    }
}

function displayUserData(user) {
    const usernameElement = document.getElementById('email');
    usernameElement.textContent = user.email || 'Не указано';

    const rolesElement = document.getElementById('roles');
    rolesElement.textContent = user.roles ? user.roles.map(role => role.name).join(', ') : 'Не указано';

    const userTableBody = document.getElementById('user-table-body');

    userTableBody.innerHTML = `<tr>
                <td>${user.id || 'Не указано'}</td>
                <td>${user.name || 'Не указано'}</td>
                <td>${user.surname || 'Не указано'}</td>
                <td>${user.year || 'Не указано'}</td>
                <td>${user.email || 'Не указано'}</td>
                <td>${user.roles ? user.roles.map(role => role.name).join(', ') : 'Не указано'}</td>
            </tr>`;
    console.log(user)
}

(async () => {
    await fetchUserData("/api/viewUser");
})();