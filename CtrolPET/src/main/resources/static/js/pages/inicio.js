import * as loader from "../loaders.js"
import { obtenerPerfil, calcularEdad } from "../api.js"
import { obtenerServicios } from "./servicios.js";
import { reservaSinLogin } from "./reserva.js";
import AuthService from "../services/auth.service.js";

//ocupo traer la autenticacion? creo y tengo que traer la funcionalidad de reserva sin login

const publico = document.getElementById("contenido-publico");
const dashboard = document.getElementById("contenido-dashboard");
const contenedorMascotas = document.getElementById("contenedor-mascotas");
const gridServiciosIndex = document.getElementById("contenedor-servicios");

export async function initIndexLogin() {
    if (!AuthService.isAutenticate()) return;
    const botonesAuth = document.getElementById("contenedor-botones-auth");

    publico.style.display = "none";
    dashboard.style.display = "block";

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

    const token = localStorage.getItem("token");
    const idDueno = localStorage.getItem("idDueno");

    const perfil = await obtenerPerfil(idDueno, token);



    if (!perfil || !perfil.mascotas) {
        contenedorMascotas.innerHTML = `<p class="sin-mascotas">Hubo un problema al cargar tu perfil. Por favor, intenta más tarde.</p>`;
        return;
    }

    if (perfil.mascotas.length === 0) {
        contenedorMascotas.innerHTML = `<p class="sin-mascotas">Aún no tienes mascotas registradas. ¡Registra una en tu perfil!</p>`;
        return;
    }

    contenedorMascotas.innerHTML = "";

    renderTarjetasMascotas(perfil.mascotas)

    }


export function initIndexSinLogin() {
    if(AuthService.isAutenticate()) return;
    const formulario = document.querySelector(".formulario-reserva");
    const selectServicio = document.getElementById("servicioSinLogin");
    const selectSucursal = document.getElementById("sucursalSinLogin");
    dashboard.style.display = "none";
    publico.style.display = "block";

    if (!formulario) return;

    loader.cargarServicios("servicioSinLogin");
    loader.cargarSucursales("sucursalSinLogin");
    obtenerServicios(3);


    reservaSinLogin(formulario);

}






function renderTarjetasMascotas(mascotas){
    mascotas.forEach(mascota => {
        const fotoMascota = mascota.fotoUrl ? mascota.fotoUrl : '/imgs/iconos/icono-gato.png';
        const edadMascota = calcularEdad(mascota.fechaNacimiento);

        const tarjeta = document.createElement("article");
        tarjeta.className = "servicio-caja";
        tarjeta.innerHTML = `
            <div class="icono">
                <img src="${fotoMascota}" alt="Foto de ${mascota.nombre}">
            </div>
            <h3>${mascota.nombre}</h3>
            <p><strong>Raza:</strong> ${mascota.raza}</p>
            <p><strong>Edad:</strong> ${edadMascota}</p>
            <button class="btn-reserva agendar-cita" data-id="${mascota.id}">Agendar nueva cita</button>
        `;

        const buttons = document.querySelectorAll('.agendar-cita')

        buttons.forEach(btn => {
            btn.addEventListener(`click`, ()=>{
                const id = parseInt(btn.getAttribute('data-id'))
                const mascota = mascotas.find(m => m.id === id)
                //aqui falta agregar algo para que el boton lleve a la reserva ya con esta mascota seleccionada
                window.location.href = `/reserva?mascotaId=${mascota.id}`;
            })
        })
        contenedorMascotas.appendChild(tarjeta);
    });
}
