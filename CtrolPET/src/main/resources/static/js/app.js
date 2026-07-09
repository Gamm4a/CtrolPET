import * as index from "./pages/inicio.js";
import AuthService from "./services/auth.service.js";

document.addEventListener('DOMContentLoaded', () => {
    //falta agregarles estos id a los body de los html
    const paginaActual = document.body.id;

    switch (paginaActual) {
        case 'index-html':
            if (AuthService.isAutenticate()){
                index.initIndexLogin();
            } else {
                index.initIndexSinLogin();
            }
            break;
    }
});