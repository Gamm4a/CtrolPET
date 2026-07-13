import AuthService from "../services/auth.service.js";
import * as loader from "../loaders.js"
import {obtenerServicios, crearReservaConLogin, crearReservaSinLogin} from "../api.js";

export async function initReserva(){
    if (AuthService.isAutenticate()){
        await reservaConLogin();
    } else {
        reservaSinLogin();
    }


}

export async function reservaConLogin() {

    const token = localStorage.getItem('token');
    const idDueno = localStorage.getItem('idDueno');

    if (!token && !idDueno) return;


// se necesita el objeto completo, no solo el id, para el DTO

    let serviciosDisponibles = await obtenerServicios();
    let horariosLogueado = null;
    const elementosLogueado = {
        selectSucursal: document.getElementById('sucursal'),
        selectServicio: document.getElementById('servicio'),
        selectFecha: document.getElementById('fecha'),
        selectHora: document.getElementById('hora'),
        campoHoraContainer: document.getElementById('campo-hora-logueado'),
        mensajeElemento: document.getElementById('mensaje-reserva-logueado')
    };

    async function actualizarHorariosLogueado() {
        horariosLogueado = await actualizarHorarios(elementosLogueado);
    }

    elementosLogueado.selectSucursal.addEventListener('change', actualizarHorariosLogueado);
    elementosLogueado.selectServicio.addEventListener('change', actualizarHorariosLogueado);
    elementosLogueado.selectFecha.addEventListener('change', actualizarHorariosLogueado);

//  Reserva con login
    const formularioLogueado = document.querySelector(".formulario-reserva-logueado");
    if (formularioLogueado) {
        formularioLogueado.addEventListener('submit', async function (e) {
            e.preventDefault();

            const mascota = document.getElementById('mascota').value;
            const sucursal = elementosLogueado.selectSucursal.value;
            const servicio = elementosLogueado.selectServicio.value;
            const fecha = elementosLogueado.selectFecha.value;
            const hora = elementosLogueado.selectHora.value;

            if (!mascota || !sucursal || !servicio || !fecha || !hora) {
                alert("Por favor selecciona todos los campos");
                return;
            }

            if (!horariosLogueado || !horariosLogueado[hora]) {
                alert("Selecciona una hora válida");
                return;
            }

            const servicioCompleto = serviciosDisponibles.find(s => s.idServicio == servicio);
            const idEmpleado = horariosLogueado[hora].split("|")[0];
            const fechaHora = `${fecha}T${hora}`;
            const datosReserva = {
                idEmpleado: idEmpleado,
                fecha: fechaHora,
                estado: "PENDIENTE",
                idSucursal: sucursal,
                dueno: idDueno,
                mascota: mascota,
                servicios: servicioCompleto
            }

            let reserva = await crearReservaConLogin(idDueno, datosReserva);

            if (reserva) {
                document.getElementById('modal-fecha').textContent = new Date(reserva.fecha).toLocaleString('es');
                document.getElementById('modal-servicio').textContent = reserva.servicios.tipo;
                document.getElementById('modal-precio').textContent = "$" + reserva.servicios.precio;
                document.getElementById('modal-estado').textContent = reserva.estado;
                document.getElementById('modal-reserva-exitosa').style.display = "flex";
            } else {
                document.getElementById('modal-reserva-error').style.display = "flex";
            }
        });
    }
}

//  Reserva sin login


export function reservaSinLogin(){
    const form = document.querySelector(".formulario-reserva-sin-login");
    if (!form) return;

    let horariosSinLogin = null;

    const elementosSinLogin = {
        selectSucursal: document.getElementById('sucursalSinLogin'),
        selectServicio: document.getElementById('servicioSinLogin'),
        selectFecha: document.getElementById('fechaSinLogin'),
        selectHora: document.getElementById('horaSinLogin'),
        campoHoraContainer: document.getElementById('campo-hora-sin-login'),
        mensajeElemento: document.getElementById('mensaje-reserva')
    };

    async function actualizarHorariosSinLogin() {
        horariosSinLogin = await actualizarHorarios(elementosSinLogin);
    }

    elementosSinLogin.selectSucursal.addEventListener('change', actualizarHorariosSinLogin);
    elementosSinLogin.selectServicio.addEventListener('change', actualizarHorariosSinLogin);
    elementosSinLogin.selectFecha.addEventListener('change', actualizarHorariosSinLogin);


    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        const nombre = document.getElementById('nombre').value;
        const apellidoPaterno = document.getElementById('apellidoPaterno').value;
        const apellidoMaterno = document.getElementById('apellidoMaterno').value;
        const telefono = document.getElementById('telefono').value;
        const nombreMascota = document.getElementById('nombre-mascota').value;
        const tipoMascota = document.getElementById('tipo-mascota').value;
        const razaMascota = document.getElementById('raza-mascota').value;
        const sucursalSinLogin = elementosSinLogin.selectSucursal.value;
        const servicioSinLogin = elementosSinLogin.selectServicio.value;
        const fechaSinLogin = elementosSinLogin.selectFecha.value;
        const horaSinLogin = elementosSinLogin.selectHora.value;

        if (!nombre || !apellidoPaterno || !apellidoMaterno || !telefono || !nombreMascota ||
            !tipoMascota || !razaMascota || !sucursalSinLogin || !servicioSinLogin || !fechaSinLogin) {
            alert("Por favor completa todos los campos principales");
            return;
        }

        if (!horaSinLogin || !horariosSinLogin) {
            alert("Por favor, selecciona un horario válido");
            return;
        }

        const idEmpleado = horariosSinLogin[horaSinLogin].split("|")[0];

        let datosReserva = {
            nombre, telefono, nombreMascota,
            especieMascota: tipoMascota,
            razaMascota,
            idSucursal: sucursalSinLogin,
            idServicio: servicioSinLogin,
            fecha: fechaSinLogin,
            hora: horaSinLogin
        }

        const reserva = await crearReservaSinLogin(datosReserva);

            if (reserva) {
                document.getElementById('modal-fecha').textContent = new Date(reserva.fecha).toLocaleString('es');
                document.getElementById('modal-servicio').textContent = reserva.servicios.tipo;
                document.getElementById('modal-precio').textContent = "$" + reserva.servicios.precio;
                document.getElementById('modal-estado').textContent = reserva.estado;
                document.getElementById('modal-reserva-exitosa').style.display = "flex";
            } else {
                document.getElementById('modal-reserva-error').style.display = "flex";

            }
        });

        document.getElementById('btn-cerrar-modal-exito').addEventListener('click', function () {
            window.location.href = "/";
        });

        document.getElementById('btn-cerrar-modal').addEventListener('click', function () {
            document.getElementById('modal-reserva-error').style.display = "none";
        });



}


async function actualizarHorarios({ selectSucursal, selectServicio, selectFecha, selectHora, campoHoraContainer, mensajeElemento }) {
    const sucursal = selectSucursal.value;
    const servicio = selectServicio.value;
    const fecha = selectFecha.value;

    if (!sucursal || !servicio || !fecha) {
        campoHoraContainer.style.display = "none";
        selectHora.innerHTML = '<option value="" disabled selected>Selecciona una hora</option>';
        return null;
    }

    mensajeElemento.innerHTML = "Buscando horarios disponibles...";

    try {
        const params = new URLSearchParams({idSucursal: sucursal, idServicio: servicio, fecha: fecha});
        const response = await fetch(`/api/reservas/horarios?${params}`);

        if (!response.ok) {
            mensajeElemento.innerHTML = "Ocurrió un error buscando horarios";
            campoHoraContainer.style.display = "none";
            return null;
        }

        const horarios = await response.json();

        if (!horarios || Object.keys(horarios).length === 0) {
            mensajeElemento.innerHTML = "No hay horarios disponibles ese día";
            campoHoraContainer.style.display = "none";
            return null;
        }

        selectHora.innerHTML = '<option value="" disabled selected>Selecciona una hora</option>';
        for (const [hora, valor] of Object.entries(horarios)) {
            const [idEmpleado, nombreEmpleado] = valor.split("|");
            const option = document.createElement('option');
            option.value = hora;
            option.textContent = `${hora.substring(0, 5)} - ${nombreEmpleado}`;
            selectHora.appendChild(option);
        }

        campoHoraContainer.style.display = "block";
        mensajeElemento.innerHTML = "";
        return horarios;

    } catch (error) {
        console.error("Error al buscar horarios:", error);
        mensajeElemento.innerHTML = "Error de conexión al buscar horarios";
        return null;
    }


}
