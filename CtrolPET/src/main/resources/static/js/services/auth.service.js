const BASE_URL = `http://localhost:8080/api`;

class AuthService {
    static async login(email, password) {
        const response = await fetch(`${BASE_URL}/login`, {
            method: `POST`,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ correo: email, contrasenia: password })
        });

        const data = await response.json();

        if (!response.ok) {
            const error = data.error || `Error al iniciar sesión`;
            throw new Error(error);
        }

        if (data.token) {
            localStorage.setItem(`token`, data.token);
        }

        return data;
    }

    static async register(username, email, password) {
        const response = await fetch(`${BASE_URL}/users`, {
            method: `POST`,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        });

        const data = response.json();

        if (!response.ok) {
            const error = data.error || "Error al registrar el usuario";
            throw new Error(error);
        }

        return data;
    }

    static logout() {
        localStorage.removeItem(`token`);
        localStorage.removeItem(`user`);
    }

    static isAutenticate() {
        return !!localStorage.getItem(`token`);
    }

    static getToken() {
        return localStorage.getItem(`token`);
    }

    static getUser() {
        const user = localStorage.getItem(`user`);
        return JSON.parse(user);
    }
}

export default AuthService;