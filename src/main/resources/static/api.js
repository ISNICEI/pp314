const api = {
    getUsers() {
        return fetch('/admin/users').then(res => res.json());
    },
    getRoles() {
        return fetch('/admin/users/roles').then(res => res.json());
    },
    createUser(user) {
        return fetch('/admin/new', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        });
    },
    updateUser(user) {
        return fetch('/admin/update', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        });
    },
    deleteUser(id) {
        return fetch(`/admin/delete/${id}`, {method: 'DELETE'});
    }
};