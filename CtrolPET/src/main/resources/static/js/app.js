import * as index from "./pages/inicio.js";
import AuthService from "./services/auth.service.js";
import * as servicios from "./pages/servicios.js";
import * as login from "./pages/login.js";

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
    }
});