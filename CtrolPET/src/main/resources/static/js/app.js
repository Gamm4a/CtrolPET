import * as index from "./pages/inicio.js";
import AuthService from "./services/auth.service.js";
import * as servicios from "./pages/servicios.js";
import * as login from "./pages/login.js";
import * as perfil from "./pages/perfil.js";
import authService from "./services/auth.service.js";

document.addEventListener('DOMContentLoaded', async () => {
    const paginaActual = document.body.id;

    switch (paginaActual) {
        case 'index-html':
            if (AuthService.isAutenticate()){
                await index.initIndexLogin();
            } else {
                index.initIndexSinLogin();
            }
            break;
        case 'servicios-html':
            servicios.initServicios();
            break;
        case 'login-html':
            login.initLogin();
            break;
        case 'perfil-html':
            if (authService.isAutenticate()){
                await perfil.initPerfil();
            }
            break;
    }
});