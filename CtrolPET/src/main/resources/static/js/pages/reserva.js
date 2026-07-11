
const token = localStorage.getItem('token');
const idDueno = localStorage.getItem('idDueno');

if (token && idDueno) {
    document.querySelector('.contenedor-reserva-sin-login').style.display = 'none';
    document.querySelector('.contenedor-reserva-logueado').style.display = 'grid';

    fetch(`/api/dueno/${idDueno}/mascotas`, {
        headers: { "Authorization": "Bearer " + token }
    })
    .then(res => res.json())
    .then(mascotas => {
        const select = document.getElementById('mascota');
        select.innerHTML = '';
        mascotas.forEach(m => {
            const option = document.createElement('option');
            option.value = m.idMascota;
            option.textContent = m.nombre;
            select.appendChild(option);
        });
    })
    .catch(() => console.error("No se pudieron cargar las mascotas"));
}

// se necesita el objeto completo, no solo el id, para el DTO
let serviciosDisponibles = [];

fetch("/api/servicios")
.then(response => response.json())
.then(data => { serviciosDisponibles = data; })
.catch(() => console.error("No se pudo cargar el catálogo de servicios"));

//  Reserva con login
const formularioLogueado = document.querySelector(".formulario-reserva-logueado");
let horariosLogueado = null;
if (formularioLogueado) {
    formularioLogueado.addEventListener('submit', async function (e) {
        e.preventDefault();

        const mascota = document.getElementById('mascota').value;
        const sucursal = document.getElementById('sucursal').value;
        const servicio = document.getElementById('servicio').value;
        const fecha = document.getElementById('fecha').value;
        const mensaje = document.getElementById('mensaje-reserva-logueado');

        if (!mascota || !sucursal || !servicio || !fecha) {
            alert("Por favor de completar todos los campos");
            return;
        }


        if (horariosLogueado === null) {
            const params = new URLSearchParams({ idSucursal: sucursal, idServicio: servicio, fecha: fecha });
            const response = await fetch(`/api/reservas/horarios?${params}`);

            if (!response.ok) {
                mensaje.innerHTML = "Ocurrió un error buscando horarios";
                return;
            }

            horariosLogueado = await response.json();

            if (Object.keys(horariosLogueado).length === 0) {
                mensaje.innerHTML = "No hay horarios disponibles ese día";
                horariosLogueado = null;
                return;
            }

            const selectHora = document.getElementById('hora');
            selectHora.innerHTML = '<option value="" disabled selected>Selecciona una hora</option>';
            for (const [hora, valor] of Object.entries(horariosLogueado)) {
                const [idEmpleado, nombreEmpleado] = valor.split("|");
                const option = document.createElement('option');
                option.value = hora;
                option.textContent = `${hora.substring(0, 5)} - ${nombreEmpleado}`;
                selectHora.appendChild(option);
            }

            document.getElementById('campo-hora-logueado').style.display = "block";
            document.getElementById('mascota').disabled = true;
            document.getElementById('sucursal').disabled = true;
            document.getElementById('servicio').disabled = true;
            document.getElementById('fecha').disabled = true;
            document.getElementById('btn-agendar-logueado').textContent = "Agendar mi cita";
            mensaje.innerHTML = "";
            return;
        }

        const hora = document.getElementById('hora').value;
        if (!hora) {
            alert("Selecciona una hora");
            return;
        }

        const idEmpleado = horariosLogueado[hora].split("|")[0];
        const idDueno = localStorage.getItem('idDueno');
        const token = localStorage.getItem('token');
        const servicioCompleto = serviciosDisponibles.find(s => s.idServicio === servicio);
        const fechaHoraCompleta = `${fecha}T${hora}`;
        const response = await fetch(`/api/reservas/dueno/${idDueno}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({
                idEmpleado: idEmpleado,
                fecha: fechaHoraCompleta,
                estado: "PENDIENTE",
                idSucursal: sucursal,
                dueno: idDueno,
                mascota: mascota,
                servicios: servicioCompleto
            })
        });

               if (response.ok) {
            const reserva = await response.json();
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

//  Reserva sin login
const formulario = document.querySelector(".formulario-reserva-sin-login");
let horariosSinLogin = null;

if (formulario) {
    formulario.addEventListener('submit', async function (e) {
        e.preventDefault();

        const nombre = document.getElementById('nombre').value;
        const apellidoPaterno = document.getElementById('apellidoPaterno').value;
        const apellidoMaterno = document.getElementById('apellidoMaterno').value;
        const telefono = document.getElementById('telefono').value;
        const nombreMascota = document.getElementById('nombre-mascota').value;
        const tipoMascota = document.getElementById('tipo-mascota').value;
        const razaMascota = document.getElementById('raza-mascota').value;
        const sucursalSinLogin = document.getElementById('sucursalSinLogin').value;
        const servicioSinLogin = document.getElementById('servicioSinLogin').value;
        const fechaSinLogin = document.getElementById('fechaSinLogin').value;
        const mensaje = document.getElementById('mensaje-reserva');

        if (!nombre || !telefono || !nombreMascota || !tipoMascota || !razaMascota
            || !sucursalSinLogin || !servicioSinLogin || !fechaSinLogin) {
            alert("Por favor completa todos los campos");
            return;
        }

        if (horariosSinLogin === null) {
            const params = new URLSearchParams({ idSucursal: sucursalSinLogin, idServicio: servicioSinLogin, fecha: fechaSinLogin });
            const response = await fetch(`/api/reservas/horarios?${params}`);

            if (!response.ok) {
                mensaje.innerHTML = "Ocurrió un error buscando horarios";
                return;
            }

            horariosSinLogin = await response.json();

            if (Object.keys(horariosSinLogin).length === 0) {
                mensaje.innerHTML = "No hay horarios disponibles ese día";
                horariosSinLogin = null;
                return;
            }

            const selectHora = document.getElementById('horaSinLogin');
            selectHora.innerHTML = '<option value="" disabled selected>Selecciona una hora</option>';
            for (const [hora, valor] of Object.entries(horariosSinLogin)) {
                const [idEmpleado, nombreEmpleado] = valor.split("|");
                const option = document.createElement('option');
                option.value = hora;
                option.textContent = `${hora.substring(0, 5)} - ${nombreEmpleado}`;
                selectHora.appendChild(option);
            }


            document.getElementById('campo-hora-sin-login').style.display = "block";
            document.getElementById('nombre').disabled = true;
            document.getElementById('telefono').disabled = true;
            document.getElementById('nombre-mascota').disabled = true;
            document.getElementById('tipo-mascota').disabled = true;
            document.getElementById('raza-mascota').disabled = true;
            document.getElementById('sucursalSinLogin').disabled = true;
            document.getElementById('servicioSinLogin').disabled = true;
            document.getElementById('fechaSinLogin').disabled = true;
            document.getElementById('btn-agendar-sin-login').textContent = "Agendar mi cita";
            mensaje.innerHTML = "";
            return;
        }

        const horaSinLogin = document.getElementById('horaSinLogin').value;
        if (!horaSinLogin) {
            alert("Selecciona una hora");
            return;
        }

        const idEmpleado = horariosSinLogin[horaSinLogin].split("|")[0];

        const response = await fetch("/api/reservas", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                nombre, telefono, nombreMascota,
                especieMascota: tipoMascota,
                razaMascota,
                idSucursal: sucursalSinLogin,
                idServicio: servicioSinLogin,
                fecha: fechaSinLogin,
                hora: horaSinLogin
            })
        });

        if (response.ok) {
            const reserva = await response.json();
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
document.getElementById('btn-cerrar-modal-exito').addEventListener('click', function () {
    window.location.href = "/";
});

document.getElementById('btn-cerrar-modal').addEventListener('click', function () {
    document.getElementById('modal-reserva-error').style.display = "none";
});