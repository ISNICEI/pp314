document.getElementById('new-user-form').addEventListener('submit', function (event) {
    event.preventDefault();

    const formData = new FormData(this);

    const rolesSelected = Array.from(document.getElementById('roles').selectedOptions).map(option => ({
        id: parseInt(option.value, 10),
        name: option.textContent.trim() // или через data-name, как выше
    }));

    const user = {
        username: formData.get('username'),
        firstName: formData.get('firstName'),
        lastName: formData.get('lastName'),
        email: formData.get('email'),
        password: formData.get('password'),
        roles: rolesSelected
    };

    console.log('Отправляем:', JSON.stringify(user));

    fetch('/admin/new', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })
        .then(response => {
            if (response.ok) {
                fetchUsers();
                this.reset();
                closeModal('newUserPopup');
                const tabTrigger = new bootstrap.Tab(document.querySelector('#nav-home-tab'));
                tabTrigger.show();
            } else {
                return response.json().then(errors => {
                    document.querySelectorAll('.error-message').forEach(el => el.textContent = '');
                    Object.keys(errors).forEach(field => {
                        const errorElement = document.getElementById(field + '-error');
                        if (errorElement) {
                            errorElement.textContent = errors[field];
                        }
                    });
                    throw new Error('Validation failed');
                });
            }
        })
        .catch(error => {
            console.error('Error creating user:', error);
        });
});