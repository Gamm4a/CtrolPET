import * as index from "./pages/inicio.js";

document.addEventListener('DOMContentLoaded', () => {
    //falta agregarles estos id a los body de los html
    const paginaActual = document.body.id;

    switch (paginaActual) {
        case 'index-html':
            index.initIndexSinLogin();
            break;
    }
});