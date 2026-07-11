document.getElementById('btn-enviar-registro').addEventListener('click', async function (e) {
    e.preventDefault();

    // Paso 1
    const nombre = document.getElementById('nombre').value;
    const apellidoPaterno = document.getElementById('apellidoPaterno').value;
    const apellidoMaterno = document.getElementById('apellidoMaterno').value;
    const telefono = document.getElementById('telefono').value;
    const correo = document.getElementById('correo').value;
    const contrasenia = document.getElementById('contrasenia').value;
    const fechaNacimiento = document.getElementById('fechaNacimiento').value;

    // Paso 2 
    const calle = document.getElementById('calle').value;
    const colonia = document.getElementById('colonia').value;
    const ciudad = document.getElementById('ciudad').value;
    const estado = document.getElementById('estadoDireccion').value;
    const codigoPostal = document.getElementById('codigoPostal').value;
    const numeroCasa = document.getElementById('numeroCasa').value;

    // Paso 3 
    const nombreMascota = document.getElementById('nombre-mascota').value;
    const tipoMascota = document.getElementById('tipo-mascota').value;
    const razaMascota = document.getElementById('raza-mascota').value;
    const fechaNacimientoMascota = document.getElementById('fechaNacimientoMascota').value;
    const sexoMascota = document.querySelector('input[name="sexoMascota"]:checked');
    const esterilizado = document.querySelector('input[name="esterilizadoMascota"]:checked');

    // Cita opcional
    const servicio = document.getElementById('servicio').value;
    const fecha = document.getElementById('fecha').value;
    const hora = document.getElementById('hora').value;

    if (!nombre || !apellidoPaterno || !apellidoMaterno || !telefono || !correo || !contrasenia || !fechaNacimiento) {
        alert('Por favor completa todos los campos del Paso 1');
        document.getElementById('switch-paso1').checked = true;
        return;
    }

    if (!calle || !colonia || !ciudad || !estado || !codigoPostal) {
        alert('Por favor completa todos los campos del Paso 2 (Dirección)');
        document.getElementById('switch-paso2').checked = true;
        return;
    }

    if (!nombreMascota || !tipoMascota || !razaMascota || !sexoMascota || !esterilizado) {
        alert('Por favor completa todos los campos del Paso 3 (Mascota)');
        document.getElementById('switch-paso3').checked = true;
        return;
    }

    const reservaDTO = servicio ? {
        idSucursal: "",
        idServicio: servicio,
        fecha: fecha,
        hora: hora,
        estado: "PENDIENTE"
    } : null;

    const response = await fetch('/api/registro', {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            duenoDTO: {
                nombre,
                apellidoPaterno,
                apellidoMaterno,
                correo,
                contrasenia,
                fechaNacimiento: fechaNacimiento ? fechaNacimiento + "T00:00:00Z" : null,
                telefono,
                mascotas: [{
                    nombre: nombreMascota,
                    especie: tipoMascota,
                    raza: razaMascota,
                    fechaNacimiento: fechaNacimientoMascota ? fechaNacimientoMascota + "T00:00:00Z" : null
                }],
                direccion: {
                    calle,
                    colonia,
                    ciudad,
                    estado,
                    codigoPostal,
                    numeroCasa: numeroCasa
                }
            },
            reservaDTO
        })
    });

    if (response.ok) {
        document.getElementById('switch-paso5').checked = true;
    } else {
        const mensaje = document.getElementById('mensaje-registro');
        mensaje.innerHTML = "Ocurrió un error, verifica tus datos";
    }
});