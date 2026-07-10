import AuthService from "../services/auth.service.js";

export function initLogin() {
    const btnLogin = document.getElementById('btn-login');

    if (btnLogin) {
        btnLogin.addEventListener('click', login);
    }
}

async function login(e) {
    e.preventDefault();

    const correo = document.getElementById('correo').value.trim();
    const contrasenia = document.getElementById('contrasenia').value.trim();

    if (!correo || !contrasenia) {
        alert('Por favor, llena todos los campos.');
        return;
    }

    const data = await AuthService.login(correo, contrasenia);

    //BORRAR ESTO
    console.log("Respuesta del servidor en Login:", data);

    if (data && !data.error && data.token) {
        localStorage.setItem('token', data.token);
        localStorage.setItem('idDueno', data.idDueno);
        window.location.href = '/';
    } else {
        alert('Correo o contraseña incorrectos.');
    }
}