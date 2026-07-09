import * as loader from "../loaders.js"
//ocupo traer la autenticacion? creo y tengo que traer la funcionalidad de reserva sin login

const publico = document.getElementById("contenido-publico");
const dashboard = document.getElementById("contenido-dashboard");
const contenedorMascotas = document.getElementById("contenedor-mascotas");

export function initIndexLogin() {

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
            window.location.reload();
        });
    }

    const token = localStorage.getItem("token");
    const idDueno = localStorage.getItem("idDueno");

    //ocupamos algo como esto:
   // const perfil = await obtenerPerfil(idDueno, token);

    if (perfil.error || !perfil.mascotas || perfil.mascotas.length === 0) {
        contenedorMascotas.innerHTML = `<p class="sin-mascotas">Aún no tienes mascotas registradas. ¡Registra una en tu perfil!</p>`;
        return;
    }

    contenedorMascotas.innerHTML = "";
    perfil.mascotas.forEach(mascota => {
        const tieneFoto = mascota.fotoUrl;
        const fotoMascota = tieneFoto ? mascota.fotoUrl : '/imgs/iconos/icono-gato.png'
        const edadMascota = calcularEdad(mascota.fechaNacimiento)
        const tarjeta = document.createElement("article");
        tarjeta.className = "servicio-caja";
        tarjeta.innerHTML = `
            <div class="icono">
                <img src="${fotoMascota}" alt="mascota">
            </div>
            <h3>${mascota.nombre}</h3>
            <p>Raza: ${mascota.raza || 'No especificada'}</p>
            <p>Edad: ${edadMascota} años</p>
        `;
        contenedorMascotas.appendChild(tarjeta);
    });

}

export function initIndexSinLogin() {
    const formulario = document.querySelector(".formulario-reserva");
    const selectServicio = document.getElementById("servicio");
    dashboard.style.display = "none";
    publico.style.display = "block";

    if (!formulario) return;

    loader.cargarServicios("servicio");

    const chkRegistro = document.getElementById("chk-registro");
    const camposRegistro = document.querySelector(".campos-registro");
    if (chkRegistro && camposRegistro) {
        camposRegistro.style.display = "none";
        chkRegistro.addEventListener("change", (e) => {
            camposRegistro.style.display = e.target.checked ? "grid" : "none";
            camposRegistro.querySelectorAll("input").forEach(i => i.required = e.target.checked);
        });
    }

    formulario.addEventListener("submit", async (e) => {
        e.preventDefault();

        //falta la validación de contraseñas y llamada a crearReservaSinLogin() ...
    });

}




//creo que hay que mover esta funcion, se puede usar como utils
function calcularEdad(fechaNacimiento) {
    const hoy = new Date();
    const nacimiento = new Date(fechaNacimiento);

    let edad = hoy.getFullYear() - nacimiento.getFullYear();
    const mes = hoy.getMonth() - nacimiento.getMonth();

    if (mes < 0 || (mes === 0 && hoy.getDate() < nacimiento.getDate())) {
        edad--;
    }
    return edad;
}