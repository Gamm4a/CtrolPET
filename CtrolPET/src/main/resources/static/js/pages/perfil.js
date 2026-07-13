import {obtenerPerfil, obtenerMascotas, obtenerCitas, cancelarCita, calcularEdad, editarDueno, agregarMascota} from "../api.js";
import AuthService from "../services/auth.service.js";

let mascotasDelUsuario = [];
let citasDelUsuario = [];

export async function initPerfil(){
    const idDueno = localStorage.getItem("idDueno");
    if (AuthService.getToken() && !idDueno) {
        window.location.href = "/admin/dashboard";
        return;
    }

    await cargarDatosUsuario(idDueno);
    await cargarMascotasUsuario(idDueno);
    await cargarCitasUsuario(idDueno);

    const btnLogout = document.getElementById("btn-cerrar-sesion");
    if (btnLogout) {
        btnLogout.addEventListener("click", async (e) => {
            e.preventDefault();
            AuthService.logout();
            localStorage.clear();
            window.location.href = "login.html";
        });
    }

    const tablaCitas = document.getElementById("tabla-citas-body");
    tablaCitas.addEventListener("click", async (e) => {
        if (e.target.classList.contains("btn-cancelar")) {
            const idReserva = e.target.getAttribute("data-id");

            if (confirm("¿Estás seguro de que deseas cancelar esta cita?")) {
                const resultado = await cancelarCita(idDueno, idReserva);
                if (!resultado.error) {
                    alert("Cita cancelada con éxito");
                    await cargarCitasUsuario(idDueno);
                } else {
                    alert("No se pudo cancelar la cita.");
                }
            }
        }
    });

    const selectMascotas = document.getElementById("mascota-select");
    if (selectMascotas) {
        selectMascotas.addEventListener("change", (e) => {
            const idSeleccionado = e.target.value;
            const mascotaSeleccionada = mascotasDelUsuario.find(m => m.idMascota === idSeleccionado);
            mostrarDetalleMascota(mascotaSeleccionada);
            citasMascotaDashboard(idSeleccionado);
        });
    }

    const btnEditar = document.getElementById("btn-editar-perfil");

    if (btnEditar) {
        btnEditar.addEventListener("click", async (e) => {
            e.preventDefault();

            const inputs = [
                document.getElementById("nombre-perfil"),
                document.getElementById("apellido-paterno-perfil"),
                document.getElementById("apellido-materno-perfil"),
                document.getElementById("correo-perfil"),
                document.getElementById("telefono-perfil")
            ];

            const estaEditando = btnEditar.getAttribute("data-editando") === "true";

            if (!estaEditando) {
                inputs.forEach(input => {
                    if (input) input.disabled = false;
                });

                btnEditar.innerText = "Guardar Cambios";
                btnEditar.setAttribute("data-editando", "true");
                btnEditar.classList.add("btn-guardar");

            } else {
                const correoInput = document.getElementById("correo-perfil").value;
                const correoOriginal = localStorage.getItem("correoOriginal");

                const duenoActualizado = {
                    nombre: document.getElementById("nombre-perfil").value,
                    apellidoPaterno: document.getElementById("apellido-paterno-perfil").value,
                    apellidoMaterno: document.getElementById("apellido-materno-perfil").value,
                    correo: document.getElementById("correo-perfil").value,
                    telefono: document.getElementById("telefono-perfil").value
                };

                try {
                    const data = await editarDueno(idDueno, duenoActualizado);
                    if (correoOriginal && correoOriginal !== correoInput) {
                        alert("Tu correo ha cambiado. Por seguridad, por favor inicia sesión nuevamente con tu nuevo correo.");
                        AuthService.logout();
                        window.location.href = "login.html";
                        return;
                    }

                    inputs.forEach(input => {
                        if (input) input.disabled = true;
                    });

                    btnEditar.innerText = "Editar Perfil";
                    btnEditar.setAttribute("data-editando", "false");
                    btnEditar.classList.remove("btn-guardar");

                } catch (error) {
                    console.error(error);
                    alert("No se pudo actualizar el perfil");
                }
            }
        });

    }

    const formMascota = document.getElementById("form-agregar-mascota");
    const switchModal = document.getElementById("control-modal-mascota");

    if (formMascota) {
        formMascota.addEventListener("submit", async (e) => {
            e.preventDefault();

            const fechaInput = document.getElementById("modal-fecha-nacimiento").value;
            const fechaInstant = `${fechaInput}T00:00:00Z`;


            const nuevaMascota = {
                nombre: document.getElementById("modal-nombre").value,
                especie: document.getElementById("modal-especie").value,
                raza: document.getElementById("modal-raza").value,
                fechaNacimiento: fechaInstant
                //quiero agregar un switch aqui para asignarle la foto predeterminada segun
                //la especie de la mascota. Lo hago mañana


            };

            try {
                await agregarMascota(idDueno, nuevaMascota);

                formMascota.reset();
                if (switchModal) switchModal.checked = false;

                await cargarMascotasUsuario(idDueno);

            } catch (error) {
                console.error(error);
                alert("No se pudo registrar la mascota.");
            }
        });
    }

    const btnAgendar = document.getElementById("btn-agendar-cita-mascota");
    if (btnAgendar) {
        btnAgendar.addEventListener("click", (e) => {
            e.preventDefault();
            window.location.href = "/reserva";
        })
    }

}

async function cargarDatosUsuario(idDueno) {
    const datos = await obtenerPerfil(idDueno);
    if (!datos.error) {
        const inputNombre = document.getElementById("nombre-perfil");
        const inputApellidoPaterno = document.getElementById("apellido-paterno-perfil");
        const inputApellidoMaterno = document.getElementById("apellido-materno-perfil");
        const inputCorreo = document.getElementById("correo-perfil");
        const inputTelefono = document.getElementById("telefono-perfil");

        if (inputNombre) inputNombre.value = datos.nombre;
        if (inputCorreo) inputCorreo.value = datos.correo;
        if (inputTelefono) inputTelefono.value = datos.telefono;
        if (inputApellidoPaterno) inputApellidoPaterno.value = datos.apellidoPaterno;
        if (inputApellidoMaterno) inputApellidoMaterno.value = datos.apellidoMaterno;

        localStorage.setItem("correoOriginal", datos.correo);
        const paginaActual = document.body.id;
        if (paginaActual !== "perfil-html") {
            localStorage.removeItem("correoOriginal");
        }
    }
}

async function cargarMascotasUsuario(idDueno) {
    const mascotas = await obtenerMascotas(idDueno);
    const contenedor = document.getElementById("contenedor-mascota");
    const selectMascotas = document.getElementById("mascota-select");
    mascotasDelUsuario = [];

    if (mascotas) {
        mascotasDelUsuario = mascotas;
    } else {
        contenedor.innerHTML = "<p>No tienes mascotas registradas aún.</p>";
        return;
    }

    if (selectMascotas) {
        mascotasDelUsuario.forEach(mascota => {
            selectMascotas.innerHTML += `<option value="${mascota.idMascota}">${mascota.nombre}</option>`;
        });

        if (mascotasDelUsuario.length > 0) {
            selectMascotas.value = mascotasDelUsuario[0].idMascota;
            mostrarDetalleMascota(mascotasDelUsuario[0]);
        }
    }

}

function mostrarDetalleMascota(mascota) {
    const contenedor = document.getElementById("contenedor-mascota");
    if (!contenedor) return;

    if (!mascota) {
        contenedor.innerHTML = "<p>Selecciona una mascota para ver su detalle.</p>";
        return;
    }

    contenedor.innerHTML = `
    <div class="foto" id="foto-mascota">
        <img src="${mascota.fotoUrl}" alt="foto de ${mascota.nombre}">
    </div>
    <div class="nombre-raza">
        <h3 id="nombre-mascota-dashboard"> ${mascota.nombre}</h3>
        <p><span id="raza-mascota-dashboard">${mascota.raza} </span>  &bull; <span id="edad-mascota-dashboard"> ${calcularEdad(mascota.fechaNacimiento)}</span></p>
    </div>
    `

}

async function cargarCitasUsuario(idDueno) {
    const citas = await obtenerCitas(idDueno);
    const tablaCitas = document.getElementById("tabla-citas-body");
    if (!tablaCitas) return;

    citasDelUsuario = citas;

    tablaCitas.innerHTML = "";

    if (citas.error || citas.length === 0) {
        tablaCitas.innerHTML = "<tr><td colspan='7'>No tienes citas agendadas.</td></tr>";
        return;
    }

    citas.forEach(cita => {
        const mascotaEncontrada = mascotasDelUsuario.find(m => m.idMascota === cita.mascota);
        const nombreMascota = mascotaEncontrada ? mascotaEncontrada.nombre : "Mascota";
        const botonCancelar = cita.estado === "PENDIENTE" || cita.estado === "CONFIRMADO"
            ? `<button class="btn-cancelar" data-id="${cita.id}">Cancelar</button>`
            : `<span class="badge-${cita.estado.toLowerCase()}">${cita.estado}</span>`;

        tablaCitas.innerHTML += `
            <tr>
                <td><strong>${nombreMascota || "Mascota"}</strong></td>
                <td>${cita.fecha}</td>
                <td>${cita.servicios.tipo || "Consulta"}</td>
                <td>${cita.veterinarioNombre || "Asignado"}</td>
                <td>$${cita.servicios.precio || "0"}</td>
                <td>${botonCancelar}</td>
            </tr>
        `;
    });
}

function citasMascotaDashboard(idMascota) {
    const contenedorTarjeta = document.getElementById("citas-mascota-body");
    if (!contenedorTarjeta) return;

    contenedorTarjeta.innerHTML = "";

    if (!idMascota) {
        contenedorTarjeta.innerHTML = '<p class="gris">No hay una mascota seleccionada.</p>';
        return;
    }

    const citasFiltradas = citasDelUsuario.filter(cita => cita.mascota === idMascota);

    if (citasFiltradas.length === 0) {
        contenedorTarjeta.innerHTML = `
            <div class="item-lista">
                <p class="gris">No hay citas agendadas para esta mascota.</p>
            </div>`;
        return;
    }

    citasFiltradas.forEach(cita => {
        const fechaCita = new Date(cita.fecha).toLocaleDateString('es-MX', {
            day: '2-digit', month: 'short', hour: '2-digit', minute: '2-digit'
        });

        contenedorTarjeta.innerHTML += `
            <div class="item-lista">
                <div class="item-row">
                    <strong>${cita.servicios.tipo || "Consulta"}</strong>
                    <span class="gris">${cita.estado}</span>
                </div>
                <span class="item-fecha">${fechaCita} hs</span>
            </div>
        `;
    });
}