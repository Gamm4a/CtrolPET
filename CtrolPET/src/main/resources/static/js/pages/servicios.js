import AuthService from "../services/auth.service.js";

export function initServicios() {
    obtenerServicios();
    const botonesAuth = document.getElementById("contenedor-botones-auth");
    if (botonesAuth) {
        botonesAuth.innerHTML = `
            <a href="/perfil" class="btn-login">Mi Perfil</a>
            <button id="btn-logout" class="btn-register">Cerrar Sesión</button>
        `;
        document.getElementById("btn-logout").addEventListener("click", () => {
            localStorage.removeItem("token");
            localStorage.removeItem("idDueno");
            AuthService.logout();
            window.location.reload();
        });
    }
}

export async function obtenerServicios(limite = null) {
    const contenedor = document.querySelector('#contenedor-servicios-index, #contenedor-servicios');
    const botonMas = contenedor.querySelector('.btn-servicios-sig');

    try {
        const respuesta = await fetch('/api/servicios');
        
        if (!respuesta.ok) {
            throw new Error('No se pudieron cargar los servicios');
        }

        let servicios = await respuesta.json();

        if (limite !== null) {
            servicios = servicios.slice(0, limite);
        }

        contenedor.innerHTML = '';

        servicios.forEach(servicio => {
            const tieneFotos = servicio.fotos && servicio.fotos.length > 0;
            const imagenesString = tieneFotos ? servicio.fotos.join(',') : '/imgs/iconos/logo.png';
            const primeraFoto = tieneFotos ? servicio.fotos[0] : '/imgs/iconos/logo.png';

            const servicioHtml = `
            <div class="servicio-caja tarjeta-servicio" data-imagenes="${imagenesString}" data-nombre="${servicio.tipo}">
                <div class="icono icono-servicio">
                    <img src="${primeraFoto}" alt="icono de ${servicio.tipo}">
                </div>
                <div class="texto-servicio">
                    <h3>${servicio.tipo}</h3>
                    <p>${servicio.descripcion}</p>
                </div>
            </div>
        `;

            contenedor.innerHTML += servicioHtml;
        });


        if (botonMas) {
            contenedor.appendChild(botonMas);
        }

    configurarClicsServicios();

    } catch (error) {
        console.error('Error:', error);
        if (!botonMas) {
            contenedor.innerHTML = '<p>Lo sentimos, no pudimos cargar los servicios en este momento.</p>';
        }
    }
}


function configurarClicsServicios() {
    const tarjetas = document.querySelectorAll('.tarjeta-servicio');
    const modal = document.getElementById('modal-galeria');
    const modalTitulo = document.getElementById('modal-titulo-servicio');
    const modalLista = document.getElementById('imagenes-modal-lista');
    const botonCerrar = document.querySelector('.cerrar-modal');

    tarjetas.forEach(tarjeta => {
        tarjeta.addEventListener('click', () => {
            const todasLasImagenes = tarjeta.getAttribute('data-imagenes').split(',');
            const nombreServicio = tarjeta.getAttribute('data-nombre');

            modalTitulo.textContent = `Galería de ${nombreServicio}`;
            modalLista.innerHTML = '';

            const imagenesExtras = todasLasImagenes.slice(1);

            if (imagenesExtras.length === 0 || imagenesExtras[0] === "") {
                modalLista.innerHTML = '<p>No hay fotos disponibles para este servicio aún.</p>';
            } else {
                imagenesExtras.forEach(imgn => {
                    modalLista.innerHTML += `<img src="${imgn}" alt="Foto de ${nombreServicio}">`;
                });
            }

            modal.style.display = 'flex';
        });
    });

    botonCerrar.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    window.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.style.display = 'none';
        }
    });
}